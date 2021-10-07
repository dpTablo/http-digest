package com.potalab.http.auth.digest.security;

import java.math.BigInteger;
import java.security.MessageDigest;

public class SHA256Encoder {
    public static String encode(String value) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(value.getBytes());
            return String.format("%064x", new BigInteger(1, messageDigest.digest()));
        } catch (Throwable e) {
            e.printStackTrace();
            return "";
        }
    }
}
