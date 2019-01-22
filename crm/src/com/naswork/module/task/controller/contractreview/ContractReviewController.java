package com.naswork.module.task.controller.contractreview;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.naswork.common.controller.BaseController;
import com.naswork.model.ClientProfitmargin;
import com.naswork.model.ClientWeatherOrder;
import com.naswork.model.ClientWeatherOrderElement;
import com.naswork.model.OnPassageStorage;
import com.naswork.model.OrderApproval;
import com.naswork.model.OrderBankCharges;
import com.naswork.model.SystemCode;
import com.naswork.module.purchase.controller.importpackage.ImportPackageElementVo;
import com.naswork.module.purchase.controller.importpackage.StorageFlowVo;
import com.naswork.module.purchase.controller.supplierorder.SupplierListVo;
import com.naswork.service.ClientOrderElementService;
import com.naswork.service.ClientOrderService;
import com.naswork.service.ClientProfitmarginService;
import com.naswork.service.ClientWeatherOrderElementService;
import com.naswork.service.ClientWeatherOrderService;
import com.naswork.service.ContractReviewService;
import com.naswork.service.ImportpackageElementService;
import com.naswork.service.OrderApprovalService;
import com.naswork.service.OrderBankChargesService;
import com.naswork.service.SupplierOrderElementService;
import com.naswork.utils.EntityUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.JQGridMapVo;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.UserVo;

@Controller
@RequestMapping("/contractReview")
public class ContractReviewController extends BaseController {
    @Resource
    private ClientOrderElementService clientOrderElementService;
    @Resource
    private SupplierOrderElementService supplierOrderElementService;
    @Resource
    private ImportpackageElementService importpackageElementService;
    @Resource
    private OrderApprovalService orderApprovalService;
    @Resource
    private ClientOrderService clientOrderService;
    @Resource
    private ContractReviewService contractReviewService;
    @Resource
    private ClientWeatherOrderService clientWeatherOrderService;
    @Resource
    private ClientWeatherOrderElementService clientWeatherOrderElementService;
    @Resource
    private OrderBankChargesService orderBankChargesService;
    @Resource
    private ClientProfitmarginService clientProfitmarginService;

    /**
     * 发起合同审批
     */
    @RequestMapping(value = "/orderReview", method = RequestMethod.POST)
    public @ResponseBody
    ResultVo orderReview(HttpServletRequest request) {
        String message = "";
        boolean success = false;
        String id = request.getParameter("id");
        String clientId = request.getParameter("clientId");
        String userId = "";
        Double bankCharges = 0.0;
        //通过预订单ID获取预订单的明细
        List<ClientWeatherOrderElement> clientWeatherOrderElements = clientWeatherOrderElementService.selectByForeignKey(Integer.parseInt(id));
        //通过客户id获取该客户的利润边缘
        ClientProfitmargin clientProfitmargin = clientProfitmarginService.selectByClientId(Integer.parseInt(clientId));
        //通过id获取预订单信息
        ClientWeatherOrder clientWeatherOrder = clientWeatherOrderService.selectByPrimaryKey(Integer.parseInt(id));
        //通过id获取预订单的总额
        Double smPrice = clientWeatherOrderElementService.sumPrice(clientWeatherOrder.getId());
        //通过客户id获取银行手续费
        List<OrderBankCharges> list = orderBankChargesService.orderBankChargesByClientId(clientId);
        //遍历获取到的银行手续费用
        for (OrderBankCharges orderBankCharges : list) {
            if (null == orderBankCharges.getOrderPriceAbove() && smPrice < orderBankCharges.getOrderPriceFollowing()) {
                bankCharges = orderBankCharges.getBankCharges();
                break;
            } else if (null == orderBankCharges.getOrderPriceFollowing() && smPrice > orderBankCharges.getOrderPriceAbove()) {
                bankCharges = orderBankCharges.getBankCharges();
                break;
            } else if (smPrice > orderBankCharges.getOrderPriceAbove() && smPrice < orderBankCharges.getOrderPriceFollowing()) {
                bankCharges = orderBankCharges.getBankCharges();
                break;
            }
        }
        //遍历预订单明细
        for (ClientWeatherOrderElement clientWeatherOrderElement : clientWeatherOrderElements) {
            clientWeatherOrderElement.setClientProfitMargin(clientProfitmargin.getProfitMargin());
            //clientWeatherOrderElement.setBankCharges(bankCharges);
            clientWeatherOrderElementService.updateByPrimaryKeySelective(clientWeatherOrderElement);

            List<OrderApproval> approvals = orderApprovalService.selectByCoeId(clientWeatherOrderElement.getId());
            if (approvals.size() > 0) {
                continue;
            }
            clientWeatherOrderElement.setExchangeRate(clientWeatherOrder.getExchangeRate());
            clientWeatherOrderElement.setCurrencyId(clientWeatherOrder.getCurrencyId());
            Integer cieElementId = supplierOrderElementService.getElementId(clientWeatherOrderElement.getClientQuoteElementId());
            Integer sqeElementId = supplierOrderElementService.getSqeElementId(clientWeatherOrderElement.getClientQuoteElementId());
            List<StorageFlowVo> supplierList = new ArrayList<StorageFlowVo>();
            List<OnPassageStorage> onpasssupplierList = new ArrayList<OnPassageStorage>();
            List<ImportPackageElementVo> elementVos = importpackageElementService.findStorageByElementId(cieElementId, sqeElementId);
//            System.out.print("件号：" + clientWeatherOrderElement.getPartNumber());
            if (elementVos.size() > 0) {
                contractReviewService.Stock(elementVos, clientWeatherOrderElement, supplierList);//查库存
            }
            List<SupplierListVo> onPassageList = supplierOrderElementService.getOnPassagePartNumber(cieElementId, sqeElementId);
            if (onPassageList.size() > 0) {
                contractReviewService.onPassageStock(onPassageList, clientWeatherOrderElement, onpasssupplierList);//查在途库存
            }
            for (StorageFlowVo flowVo : supplierList) {
                OrderApproval orderApproval = new OrderApproval();
                orderApproval.setImportPackageElementId(flowVo.getImportPackageElementId());
                orderApproval.setSupplierQuoteElementId(flowVo.getId());
                orderApproval.setClientOrderElementId(clientWeatherOrderElement.getId());
                orderApproval.setClientOrderId(clientWeatherOrderElement.getClientWeatherOrderId());
                orderApproval.setAmount(flowVo.getStorageAmount());
                orderApproval.setPrice(flowVo.getPrice());
                if (flowVo.getCorrelation() != null && flowVo.getCorrelation()) {
                    orderApproval.setState(0);//0 利润不通过
                    orderApproval.setType(0);//0 自有库存
                } else if (flowVo.getProfitMargin() < clientProfitmargin.getProfitMargin() || !clientWeatherOrderElement.getUnit().trim().equals(flowVo.getUnit().trim())) {
                    orderApproval.setState(0);//0 利润不通过
                    orderApproval.setType(0);//0 自有库存
                } else {
                    orderApproval.setState(1);//1 利润通过
                    orderApproval.setType(0);//0 自有库存  1 在途库存
                }
                orderApprovalService.insert(orderApproval);
            }
            for (OnPassageStorage onPassageStorage : onpasssupplierList) {
                OrderApproval orderApproval = new OrderApproval();
                orderApproval.setSupplierOrderElementId(onPassageStorage.getSupplierOrderElementId());
                orderApproval.setClientOrderElementId(clientWeatherOrderElement.getId());
                orderApproval.setClientOrderId(clientWeatherOrderElement.getClientWeatherOrderId());
                orderApproval.setAmount(onPassageStorage.getAmount());
                orderApproval.setPrice(onPassageStorage.getPrice());
                if (onPassageStorage.getProfitMargin() < clientProfitmargin.getProfitMargin() || !clientWeatherOrderElement.getUnit().equals(onPassageStorage.getUnit())) {
                    orderApproval.setState(0);//0 利润不通过
                    orderApproval.setType(1);//1 在途库存
                } else {
                    orderApproval.setState(1);//1 利润通过
                    orderApproval.setType(1);//0 自有库存  1 在途库存
                }
                orderApprovalService.insert(orderApproval);
            }
            List<Integer> integers = clientWeatherOrderElementService.findUser(clientWeatherOrderElement.getId());
            String ids = "";
            if (integers.size() == 0) {
                ids = userId;
            } else {
                ids = integers.get(0).toString();
                if (!userId.contains(integers.get(0).toString())) {
                    userId = integers.get(0).toString();
                }
                for (int i = 1; i < integers.size(); i++) {
                    if (!userId.contains(integers.get(i).toString())) {
                        userId += ";" + integers.get(i).toString();
                    }
                    ids += ";" + integers.get(i).toString();
                }
            }
            contractReviewService.orderApproval(clientWeatherOrderElement, ids, null);
        }
        message = "发起成功";
        success = true;
        return new ResultVo(success, message);
    }


    /*
     * 利润管理
     */
    @RequestMapping(value = "/toClientProfitmargin", method = RequestMethod.GET)
    public String toClientProfitmargin() {
        return "/marketing/clientweatherorder/list";
    }

    /*
     * 利润管理页面
     */
    @RequestMapping(value = "/clientProfitmarginData", method = RequestMethod.POST)
    public @ResponseBody
    JQGridMapVo clientProfitmarginData(HttpServletRequest request) {
        PageModel<ClientProfitmargin> page = getPage(request);
        JQGridMapVo jqgrid = new JQGridMapVo();
        UserVo userVo = getCurrentUser(request);
        GridSort sort = getSort(request);
        String where = request.getParameter("searchString");

        clientProfitmarginService.listPage(page, where, sort);

        if (page.getEntities().size() > 0) {
            jqgrid.setPage(page.getPageNo());
            jqgrid.setRecords(page.getRecordCount());
            jqgrid.setTotal(page.getPageCount());

            List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
            for (ClientProfitmargin clientProfitmargin : page.getEntities()) {

                Map<String, Object> map = EntityUtil.entityToTableMap(clientProfitmargin);
                mapList.add(map);
            }
            jqgrid.setRows(mapList);
            String exportModel = getString(request, "exportModel");


        } else {
            jqgrid.setRecords(0);
            jqgrid.setTotal(0);
            jqgrid.setRows(new ArrayList<Map<String, Object>>());
        }


        return jqgrid;
    }

    /*
     * 保存新增机型
     */
    @RequestMapping(value = "/saveEdit", method = RequestMethod.POST)
    public @ResponseBody
    ResultVo saveEdit(HttpServletRequest request, @ModelAttribute ClientProfitmargin clientProfitmargin) {
        String message = "";
        boolean success = false;

        if (clientProfitmargin.getId() != null) {
            clientProfitmarginService.updateByPrimaryKeySelective(clientProfitmargin);
            success = true;
            message = "更新成功！";
        } else {
            message = "更新失败！";
        }

        return new ResultVo(success, message);

    }

}
