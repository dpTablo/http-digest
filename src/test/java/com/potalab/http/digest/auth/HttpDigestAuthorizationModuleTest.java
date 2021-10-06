package com.potalab.http.digest.auth;

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
                "Digest username=\"admin\", realm=\"testingRealm\", nonce=\"960a48d7330f27e8d16abcf60c4d03c1\", uri=\"/http-digest/digest\", response=\"98a852bb03e027262d347511352123c7\", qop=auth, nc=00000002, cnonce=\"370ad495c868bd67\""
        ).when(request).getHeader(HttpHeaders.AUTHORIZATION);

        HttpServletResponse response = mock(HttpServletResponse.class);

        HttpDigestAuthorizationModule module = new HttpDigestAuthorizationModule("realm1");
        module.authorize(request, response);

    }
}