package com.naswork.backend.common.Exception;

import com.alibaba.fastjson.JSONObject;
import com.naswork.backend.common.ResultCode;
import com.naswork.backend.utils.CommonUtil;

/**
 * @Program: CommonJsonException
 * @Description:
 * @Author: White
 * @DateTime: 2018-12-07 10:41:56
 **/

public class CommonJsonException extends RuntimeException{

    private JSONObject resultJson;

    /**
     * 调用时可以在任何代码处直接throws这个Exception,
     * 都会统一被拦截,并封装好json返回给前台
     *
     * @param resultCode 以错误的ErrorEnum做参数
     */
    public CommonJsonException(ResultCode resultCode) {
        this.resultJson = CommonUtil.errorJson(resultCode);
    }

    public CommonJsonException(JSONObject resultJson) {
        this.resultJson = resultJson;
    }

    public JSONObject getResultJson() {
        return resultJson;
    }

}
