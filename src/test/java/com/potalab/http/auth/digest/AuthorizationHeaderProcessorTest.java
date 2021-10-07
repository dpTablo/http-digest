package com.potalab.http.auth.digest;

import com.potalab.http.auth.digest.field.HttpDigestAlgorithm;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorizationHeaderProcessorTest {

    AuthorizationHeaderProcessor processor = new AuthorizationHeaderProcessor();

    @Test
    void createResponse() {
        /**
         * 1) 서버에서 아래 헤더값을 전달.
         * WWW-Authenticate: Digest realm="testingRealm", nonce="63bfa91a337f0ff179df6b1bf2d29922", qop="auth"
         *
         * 2) 클라이언트로부터 아래 헤더값을 받은것을 가정.
         * Authorization: Digest username="admin", realm="testingRealm", nonce="63bfa91a337f0ff179df6b1bf2d29922", uri="/http-digest/digest", response="34e68c62ae99b2392016c4084bca3ceb", qop=auth, nc=00000002, cnonce="f99890f941945470"
         */
        String testHeaderValue = "Digest username=\"admin\", realm=\"testingRealm\", nonce=\"63bfa91a337f0ff179df6b1bf2d29922\", uri=\"/http-digest/digest\", response=\"34e68c62ae99b2392016c4084bca3ceb\", qop=auth, nc=00000002, cnonce=\"f99890f941945470\"";
        AuthorizationHeader authorizationHeader = new AuthorizationHeader(testHeaderValue);

        HttpServletRequest request = mock(HttpServletRequest.class);
        doReturn("GET").when(request).getMethod();

        String responseString = processor.createResponse(authorizationHeader, request, HttpDigestAlgorithm.MD5, "1234");
        assertEquals("34e68c62ae99b2392016c4084bca3ceb", responseString);
//        processor.createResponse()
    }
}