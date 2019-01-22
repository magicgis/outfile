package com.naswork.backend.utils.md5;

import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Program: MD5Util
 * @Description:
 * @Author: White
 * @DateTime: 2018-12-17 00:36:59
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
        System.out.println(md5("mzly2018"));
    }


//    //将明文进行加密
//    public static String md5(String src) throws NoSuchAlgorithmException {
//        MessageDigest md = MessageDigest.getInstance("MD5");
//        byte[] output = md.digest(src.getBytes());
//        String s = Base64.encodeBase64String(output);
//        return s;
//    }
//
//    public static void main(String[] args) throws NoSuchAlgorithmException {
//        String out = md5("123456");
//        System.out.println(out);
//    }


}
