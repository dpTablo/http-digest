package com.potalab.http.auth.digest.header;

import com.potalab.http.auth.digest.HttpDigestConfiguration;
import com.potalab.http.auth.digest.security.Encryptor;

import java.util.Base64;

public class ExpireTimeOpaqueValidator implements OpaqueValidator {
    private HttpDigestConfiguration configuration;

    public ExpireTimeOpaqueValidator(HttpDigestConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public boolean validate(String opaque) {
        String[] array = opaque.split(":");
        if(array.length != 2) {
            return false;
        }

        String opaquePrivateKeyValue = array[0];
        if(!Encryptor.matching(
                configuration.getAlgorithm(),
                opaquePrivateKeyValue,
                configuration.getOpaquePrivateKey())
        ) {
            return false;
        }

        String opaqueTimeoutValue = new String(Base64.getDecoder().decode(array[1]));
        long opaqueTimeout = Long.parseLong(opaqueTimeoutValue);
        if(System.currentTimeMillis() > opaqueTimeout) {
            return false;
        } else {
            return true;
        }
    }
}
