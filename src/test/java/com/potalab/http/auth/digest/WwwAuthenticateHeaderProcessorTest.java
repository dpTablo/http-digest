package com.potalab.http.auth.digest;

import com.potalab.http.auth.digest.field.HttpDigestAlgorithm;
import com.potalab.http.auth.digest.field.Qop;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WwwAuthenticateHeaderProcessorTest {

    WwwAuthenticateHeaderProcessor processor = new WwwAuthenticateHeaderProcessor();

    @Test
    void createUnauthorizedHeaderValue() {
        WwwAuthenticateHeader header = new WwwAuthenticateHeader("testR1");
        header.setDomain("www.potalab.com");
        header.setAlgorithm(HttpDigestAlgorithm.MD5);
        header.addQop(Qop.AUTH);

        String headerValue = processor.createUnauthorizedHeaderValue(header);
        assertTrue(headerValue.contains("Digest realm=\"testR1\", domain=\"www.potalab.com\", nonce="));
        assertTrue(headerValue.contains("\", qop=\"auth\", algorithm=\"MD5\""));
    }
}