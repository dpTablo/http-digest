package com.potalab.http.digest.auth;

import com.potalab.http.digest.UserAuthcertificationData;
import com.potalab.http.digest.auth.security.HttpDigestAlgorithm;
import com.potalab.http.digest.auth.security.MD5Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpDigestAuthorizationModule {
    private String REALM_NAME = "potalab_realm";
    private HttpDigestAlgorithm DIGEST_ALGORITHM = HttpDigestAlgorithm.MD5;
    private String QOP = "auth";

    private UserAuthcertificationData userAuthcertificationData = new UserAuthcertificationData();
    private WwwAuthenticateHttpHeaderGenerator authenticateHeaderGenerator = new WwwAuthenticateHttpHeaderGenerator();

    public HttpDigestAuthorizationModule(String realmName) {
        this.REALM_NAME = realmName;

        authenticateHeaderGenerator.setRealm(realmName);
        authenticateHeaderGenerator.setDomain("www.potalab.com");
        authenticateHeaderGenerator.setAlgorithm(DIGEST_ALGORITHM);
        authenticateHeaderGenerator.setQop(QOP);
    }

    public void authorize(HttpServletRequest request, HttpServletResponse response) {
        if(isAuthorization(request, response)) {
            // TODO 인증에 성공한 경우의 대한 header 출력
        } else {
            String value = authenticateHeaderGenerator.generateToUnauthorized(request, response);

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setHeader(HttpHeaders.WWW_AUTHENTICATE, value);
        }
    }

    private boolean isAuthorization(HttpServletRequest request, HttpServletResponse response) {
        try {
            String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
            assert authorization != null;

            AuthorizationHeader authorizationHeader = new AuthorizationHeader(authorization);

            //TODO 인증 로직
            String authorizationValue = getAuthorizationValue(request, authorizationHeader);



            return false;
        } catch(Throwable t) {
            return false;
        }
    }

    private String getAuthorizationValue(
            HttpServletRequest request,
            AuthorizationHeader authorizationHeader
    ) {
        String ha1 = String.format(
                "%s:%s:%s",
                authorizationHeader.getUserName().replace("\"",""),
                authorizationHeader.getRealm().replace("\"",""),
                userAuthcertificationData.getPassword(authorizationHeader.getUserName().replace("\"",""))
        );

        String ha2 = String.format(
                "%s:%s",
                request.getMethod(),
                authorizationHeader.getUri().replace("\"", "")
        );

        String responseValue = String.format(
                "%s:%s:%s:%s:%s:%s",
                ha1,
                authorizationHeader.getNonce(),
                authorizationHeader.getNc(),
                authorizationHeader.getCnonce(),
                authorizationHeader.getQop(),
                ha2
        );

        return encodeDigest(responseValue);
    }

    private String encodeDigest(String value) {
        switch (DIGEST_ALGORITHM) {
            case MD5:
                return MD5Encoder.encode(value);
            default:
                return "";
        }
    }
}
