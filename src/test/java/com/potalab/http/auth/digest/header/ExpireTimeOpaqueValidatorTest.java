package com.potalab.http.auth.digest.header;

import com.potalab.http.auth.digest.HttpDigestConfiguration;
import com.potalab.http.auth.digest.field.HttpDigestAlgorithm;
import com.potalab.http.auth.digest.security.Encryptor;
import org.junit.jupiter.api.Test;

import java.util.Base64;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExpireTimeOpaqueValidatorTest {

    @Test
    void validate() {
        HttpDigestConfiguration configuration = mock(HttpDigestConfiguration.class);
        doReturn(HttpDigestAlgorithm.MD5).when(configuration).getAlgorithm();
        doReturn("potalab").when(configuration).getOpaquePrivateKey();

        Base64.getEncoder();

        ExpireTimeOpaqueValidator validator = new ExpireTimeOpaqueValidator(configuration);
        assertFalse(validator.validate("fail opaque"));

        String encryptedPrivateKey = Encryptor.encode(configuration.getAlgorithm(), configuration.getOpaquePrivateKey());

        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, 8, 20);
        long expiredTime = calendar.getTimeInMillis();
        String expiredTimeString = Base64.getEncoder().encodeToString(
                String.valueOf(expiredTime).getBytes()
        );
        assertFalse(validator.validate(encryptedPrivateKey + ":" + expiredTimeString));

        calendar.set(2030, 8, 20);
        long validTime = calendar.getTimeInMillis();
        String validTimeString = Base64.getEncoder().encodeToString(
                String.valueOf(validTime).getBytes()
        );
        assertTrue(validator.validate( encryptedPrivateKey + ":" + validTimeString));
    }
}