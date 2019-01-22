package com.naswork.backend.utils.md5;

import java.security.MessageDigest;

/**
 * @Program: MD5Util
 * @Description:
 * @Author: White
 * @DateTime: 2018-12-18 19:29:45
 **/

public class MD5Util {

    public static String md5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            str = buf.toString();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return str;
    }
    public static void main(String[] args) {
        System.out.println(md5("admin"));
    }



}
