package com.potalab.http.auth.digest.header;

import com.potalab.http.auth.digest.field.HttpDigestAlgorithm;
import com.potalab.http.auth.digest.security.Encryptor;

import java.util.Base64;

public class ExpireTimeOpaqueGenerator implements OpaqueGenerator {
    private long timeoutMillis;
    private String opaquePrivateKey;

    public ExpireTimeOpaqueGenerator(String opaquePrivateKey, long timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
        this.opaquePrivateKey = opaquePrivateKey;
    }

    /**
     * [opaque private key]:[expire time]
     * @return
     */
    @Override
    public String get(HttpDigestAlgorithm algorithm) {
        return Encryptor.encode(algorithm, opaquePrivateKey) + ":" +
                Base64.getEncoder().encodeToString(
                        String.valueOf(System.currentTimeMillis() + timeoutMillis).getBytes()
                );
    }
}
