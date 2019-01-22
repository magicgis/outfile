package com.naswork.dao;

import java.util.List;


import com.naswork.model.ClientInquiry;
import com.naswork.model.ClientQuote;
import com.naswork.module.marketing.controller.clientinquiry.ClientInquiryVo;
import com.naswork.module.marketing.controller.clientinquiry.ElementVo;
import com.naswork.vo.PageModel;

public interface ClientInquiryDao {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(ClientInquiry record);

    ClientInquiry selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ClientInquiry record);

    int updateByPrimaryKey(ClientInquiry record);
    
    /*
     * 列表页面数据
     * 
     */
    public List<ClientInquiryVo> listPage(PageModel<ClientInquiryVo> page);
    
    /*
     * 列表页面数据
     * 
     */
    public int findMaxSeq();
    
    /*
     * 列表页面数据
     * 
     */
    public void insert(ClientInquiry clientInquiry);
    
    /*
     * 明细列表页面数据
     * 
     */
    public List<ElementVo> ElePage(PageModel<ElementVo> page);
    
    /*
     * 根据单号获取id
     */
    public Integer findByQuoteNumber(String quoteNumber);
    
    /*
     * 获取报价单数量
     */
    public Integer getQuoteCount(ClientQuote clientQuote);
    
    /*
     * 竞争对手页面明细
     */
    public List<ElementVo> tenderEle(Integer id);
    
}	