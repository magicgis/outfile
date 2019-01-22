package com.naswork.backend.utils;

/**
 * @Program: StringTools
 * @Description:
 * @Author: White
 * @DateTime: 2018-12-18 19:28:14
 **/

public class StringTools {

    public static boolean isNullOrEmpty(String str) {
        return null == str || "".equals(str) || "null".equals(str);
    }

    public static boolean isNullOrEmpty(Object obj) {
        return null == obj || "".equals(obj);
    }



}
