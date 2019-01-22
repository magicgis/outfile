package com.naswork.service;

import java.util.List;

import com.naswork.model.ClientOrderElement;
import com.naswork.model.ClientWeatherOrderElement;
import com.naswork.model.OnPassageStorage;
import com.naswork.model.OrderApproval;
import com.naswork.module.purchase.controller.importpackage.ImportPackageElementVo;
import com.naswork.module.purchase.controller.importpackage.StorageFlowVo;
import com.naswork.module.purchase.controller.supplierorder.SupplierListVo;

public interface ContractReviewService {
	 boolean Stock(List<ImportPackageElementVo> elementVos,ClientWeatherOrderElement clientWeatherOrderElement ,List<StorageFlowVo> supplierList);
	 
	 boolean onPassageStock(List<SupplierListVo> elementVos,ClientWeatherOrderElement clientWeatherOrderElement ,List<OnPassageStorage> supplierList);
	 
	 public void orderApproval(ClientWeatherOrderElement clientWeatherOrderElement,String ids,OrderApproval orderApproval);
	 
	 public Object weatherpass(String businessKey,
				String taskId, String outcome, String assignee, String comment);
}
