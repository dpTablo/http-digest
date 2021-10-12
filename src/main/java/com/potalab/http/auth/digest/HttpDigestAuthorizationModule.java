package com.potalab.http.auth.digest;

import com.potalab.http.auth.digest.exception.HttpDigestModuleRuntimeException;
import com.potalab.http.auth.digest.exception.NonceNotCreatedException;
import com.potalab.http.auth.digest.header.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpDigestAuthorizationModule {
    private HttpDigestConfiguration httpDigestConfiguration;

    private UserAuthcertificationData userAuthcertificationData = new UserAuthcertificationData();
    private AuthorizationHeaderProcessor authorizationHeaderProcessor = new AuthorizationHeaderProcessor();
    private WwwAuthenticateHeaderProcessor authenticateHeaderProcessor;
    private AuthenticationInfoHeaderProcessor authenticationInfoHeaderProcessor = new AuthenticationInfoHeaderProcessor();

    public HttpDigestAuthorizationModule(HttpDigestConfiguration httpDigestConfiguration) throws HttpDigestModuleRuntimeException {
        this.httpDigestConfiguration = httpDigestConfiguration;

        ExpireTimeOpaqueGenerator opaqueGenerator = new ExpireTimeOpaqueGenerator(
                httpDigestConfiguration.getOpaquePrivateKey(),
                httpDigestConfiguration.getTimeout()
        );
        authenticateHeaderProcessor = new WwwAuthenticateHeaderProcessor(opaqueGenerator);
    }

    public void authorize(HttpServletRequest request, HttpServletResponse response) throws NonceNotCreatedException {
        String authorization = request.getHeader(HttpHeadersConstants.AUTHORIZATION);
        AuthorizationHeader authorizationHeader = new AuthorizationHeader(authorization);

        if(isAuthorization(authorizationHeader, request)) {
            AuthenticationInfoHeader authenticationInfoHeader = new AuthenticationInfoHeader(authorizationHeader);
            String headerValue = authenticationInfoHeaderProcessor.createAuthenticationInfoHeaderValue(
                    authenticationInfoHeader,
                    httpDigestConfiguration.getAlgorithm(),
                    request,
                    authorizationHeader.getUri()
            );

            response.setHeader(HttpHeadersConstants.AUTHENTICATION_INFO, headerValue);
        } else {
            WwwAuthenticateHeader authenticateHeader = new WwwAuthenticateHeader(httpDigestConfiguration.getRealmName());
            authenticateHeader.setDomain("www.potalab.com");
            authenticateHeader.addQop(httpDigestConfiguration.getQopSet());
            authenticateHeader.setAlgorithm(httpDigestConfiguration.getAlgorithm());
            authenticateHeader.setOpaque("aaabbbccc");

            String headerValue = authenticateHeaderProcessor.createUnauthorizedHeaderValue(
                    authenticateHeader,
                    request.getRemoteAddr(),
                    httpDigestConfiguration.getNoncePrivateKey()
            );

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setHeader(HttpHeadersConstants.WWW_AUTHENTICATE, headerValue);
        }
    }

    private boolean isAuthorization(AuthorizationHeader authorizationHeader, HttpServletRequest request) {
        try {
            String validResponseValue = authorizationHeaderProcessor.createResponse(
                    authorizationHeader,
                    request,
                    httpDigestConfiguration.getAlgorithm(),
                    userAuthcertificationData.getPassword(authorizationHeader.getUserNameWithRemoveDoubleQuotes())
            );
            return validResponseValue.equals(authorizationHeader.getResponseWithRemovedDoubleQuotes());
        } catch(Throwable t) {
            return false;
        }
    }
}
