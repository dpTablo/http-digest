package com.potalab.http.digest.auth.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Encoder {
    public static String encode(String value) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(value.getBytes());

            byte byteData[] = messageDigest.digest();
            StringBuffer sb = new StringBuffer();

            for (int i = 0; i < byteData.length; i++) {
                sb.append(
                        Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1)
                );
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean matching(String originalValue, String compareValue){
        String md5 = MD5Encoder.encode(compareValue);
        return md5.equals(originalValue);
    }
}
