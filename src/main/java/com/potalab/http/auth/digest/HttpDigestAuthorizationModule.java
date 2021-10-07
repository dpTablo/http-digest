package com.potalab.http.auth.digest;

import com.potalab.http.auth.digest.field.HttpDigestAlgorithm;
import com.potalab.http.auth.digest.field.Qop;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HttpDigestAuthorizationModule {
    private String REALM_NAME = "potalab_realm";
    private HttpDigestAlgorithm DIGEST_ALGORITHM = HttpDigestAlgorithm.MD5;
    private String QOP = "auth";

    private UserAuthcertificationData userAuthcertificationData = new UserAuthcertificationData();
    private AuthorizationHeaderProcessor authorizationHeaderProcessor = new AuthorizationHeaderProcessor();
    private WwwAuthenticateHeaderProcessor authenticateHeaderProcessor = new WwwAuthenticateHeaderProcessor();
    private AuthenticationInfoHeaderProcessor authenticationInfoHeaderProcessor = new AuthenticationInfoHeaderProcessor();

    public HttpDigestAuthorizationModule(String realmName) {
        this.REALM_NAME = realmName;
    }

    public void authorize(HttpServletRequest request, HttpServletResponse response) {
        if(isAuthorization(request, response)) {
            // TODO 인증에 성공한 경우의 대한 header 출력
//            response.setStatus(HttpServletResponse.SC_OK);
//            try {
//                response.getWriter().println("인증성공");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

//            String headerValue = authenticationInfoHeaderProcessor.createAuthenticationInfoHeaderValue();

        } else {
            WwwAuthenticateHeader authenticateHeader = new WwwAuthenticateHeader(REALM_NAME);
            authenticateHeader.setDomain("www.potalab.com");
            authenticateHeader.addQop(Qop.AUTH);
            authenticateHeader.setAlgorithm(HttpDigestAlgorithm.MD5);
            String headerValue = authenticateHeaderProcessor.createUnauthorizedHeaderValue(authenticateHeader);

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setHeader(HttpHeaders.WWW_AUTHENTICATE, headerValue);
        }
    }

    private boolean isAuthorization(HttpServletRequest request, HttpServletResponse response) {
        try {
            String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
            assert authorization != null;

            AuthorizationHeader authorizationHeader = new AuthorizationHeader(authorization);
            String validResponseValue = authorizationHeaderProcessor.createResponse(
                    authorizationHeader,
                    request,
                    DIGEST_ALGORITHM,
                    userAuthcertificationData.getPassword(authorizationHeader.getUserNameWithRemoveDoubleQuotes())
            );
            return validResponseValue.equals(authorizationHeader.getResponseWithRemovedDoubleQuotes());
        } catch(Throwable t) {
            return false;
        }
    }
}
