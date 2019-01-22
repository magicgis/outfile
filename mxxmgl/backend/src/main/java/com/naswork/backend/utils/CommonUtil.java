package com.naswork.backend.utils;

import com.alibaba.fastjson.JSONObject;
import com.naswork.backend.common.Exception.CommonJsonException;
import com.naswork.backend.common.ResultCode;

/**
 * @Program: CommonUtil
 * @Description:
 * @Author: White
 * @DateTime: 2018-12-07 10:38:19
 **/

public class CommonUtil {

    public static JSONObject errorJson(ResultCode resultCode) {
        JSONObject resultJson = new JSONObject();
        resultJson.put("status", resultCode.getCode());
        resultJson.put("msg", resultCode.getDesc());
        resultJson.put("data", new JSONObject());
        return resultJson;
    }



    public static void hasAllRequired(final JSONObject jsonObject, String requiredColumns) {
        if (!StringTools.isNullOrEmpty(requiredColumns)) {
            //验证字段非空
            String[] columns = requiredColumns.split(",");
            String missCol = "";
            for (String column : columns) {
                Object val = jsonObject.get(column.trim());
                if (StringTools.isNullOrEmpty(val)) {
                    missCol += column + "  ";
                }
            }
            if (!StringTools.isNullOrEmpty(missCol)) {
                jsonObject.clear();
                jsonObject.put("status", ResultCode.ERROR.getCode());
                jsonObject.put("msg", "缺少必填参数:" + missCol.trim());
                jsonObject.put("data", new JSONObject());
                throw new CommonJsonException(jsonObject);
            }
        }
    }

}
