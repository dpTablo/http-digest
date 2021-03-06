package com.potalab.http.auth.digest;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class HttpDigestAuthorizationModuleTest {

    @Mock
    HttpServletRequest request;

    @Test
    void isAuthorization() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        doReturn("GET").when(request).getMethod();
        doReturn(
                "Digest username=\"admin\", realm=\"testingRealm\", nonce=\"f48d840613c7de72b69d0a0af62985eb\", uri=\"/http-digest/digest\", response=\"305b13fd068d1f121bd95fd832d7f879\", qop=auth, nc=00000002, cnonce=\"3c3563774b67e4f3\""
        ).when(request).getHeader(HttpHeadersConstants.AUTHORIZATION);

        HttpServletResponse response = mock(HttpServletResponse.class);

        HttpDigestConfiguration configuration = new HttpDigestConfiguration();

        assertDoesNotThrow(() -> {
            HttpDigestAuthorizationModule module = new HttpDigestAuthorizationModule(configuration);
            module.authorize(request, response);
        });
    }
}