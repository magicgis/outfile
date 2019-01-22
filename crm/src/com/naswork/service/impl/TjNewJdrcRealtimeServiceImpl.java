package com.naswork.service.impl;

import com.naswork.dao.TjNewJdrcRealtimeDao;
import com.naswork.model.TjNewJdrcRealtime;
import com.naswork.service.TjNewJdrcRealtimeService;

import com.naswork.utils.ConvertUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: white
 * @Date: create in 2018-08-22 16:37
 * @Description:
 * @Modify_By:
 */
@Service(value = "TjNewJdrcRealtimeServiceImpl")
public class TjNewJdrcRealtimeServiceImpl implements TjNewJdrcRealtimeService {

    @Resource
    private TjNewJdrcRealtimeDao tjNewJdrcRealtimeDao;

    private static Logger logger = LoggerFactory.getLogger(TjNewJdrcRealtimeServiceImpl.class);

    @Override
    public void getList(PageModel<TjNewJdrcRealtime> page, String where, GridSort sort){
        page.put("where", where);
        if (sort!=null) {
            sort.setName(ConvertUtil.toDBName(sort.getName()));
            String sotr=ConvertUtil.convertSort(sort);
            page.put("orderby", ConvertUtil.convertSort(sort));
        }else {
            page.put("orderby", null);
        }
        page.setEntities(tjNewJdrcRealtimeDao.getList(page));
    }

    @Override
    public List<TjNewJdrcRealtime> getSpotNameList(PageModel<TjNewJdrcRealtime> pageModel) {
        return tjNewJdrcRealtimeDao.getSpotNameList(pageModel);
    }


}
