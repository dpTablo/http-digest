package com.potalab.http.auth.digest;

import com.potalab.http.auth.digest.exception.HttpDigestModuleRuntimeException;
import com.potalab.http.auth.digest.field.HttpDigestAlgorithm;
import com.potalab.http.auth.digest.field.Qop;
import com.potalab.http.auth.digest.header.OpaqueGenerator;
import com.potalab.http.auth.digest.header.WwwAuthenticateHeader;
import com.potalab.http.auth.digest.header.WwwAuthenticateHeaderProcessor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WwwAuthenticateHeaderProcessorTest {

    @Test
    void createUnauthorizedHeaderValue() {
        OpaqueGenerator opaqueGenerator = mock(OpaqueGenerator.class);
        doReturn("potalab:4548838821").when(opaqueGenerator).get(HttpDigestAlgorithm.MD5);
        try {
            WwwAuthenticateHeaderProcessor processor = new WwwAuthenticateHeaderProcessor(opaqueGenerator);

            WwwAuthenticateHeader header = new WwwAuthenticateHeader("testR1");
            header.setDomain("www.potalab.com");
            header.setAlgorithm(HttpDigestAlgorithm.MD5);
            header.addQop(Qop.AUTH);

            String headerValue = processor.createUnauthorizedHeaderValue(header, "key1", "key2");
            assertTrue(headerValue.contains("Digest realm=\"testR1\", domain=\"www.potalab.com\", nonce="));
            assertTrue(headerValue.contains("\", qop=\"auth\", algorithm=\"MD5\""));
        } catch (Exception e) {
            fail(e);
        }
    }
}