package com.potalab.http.auth.digest.security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA512Encoder {
    public static String encode(String value) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            messageDigest.update(value.getBytes());
            return String.format("%0128x", new BigInteger(1, messageDigest.digest()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
}
