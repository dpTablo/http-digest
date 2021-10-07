package com.potalab.http.auth.digest.security;

import com.potalab.http.auth.digest.field.HttpDigestAlgorithm;

public class Encryptor {
    public static String encode(HttpDigestAlgorithm algorithm, String value) {
        switch (algorithm) {
            case SHA_256:
            case SHA_256_SESS:
                return SHA256Encoder.encode(value);
            case SHA_512:
            case SHA_512_SESS:
                return SHA512Encoder.encode(value);
            default:
                return MD5Encoder.encode(value);
        }
    }
}
