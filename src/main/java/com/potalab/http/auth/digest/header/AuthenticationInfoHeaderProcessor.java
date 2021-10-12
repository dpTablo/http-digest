package com.potalab.http.auth.digest.header;

import com.potalab.http.auth.digest.field.HttpDigestAlgorithm;
import com.potalab.http.auth.digest.field.Qop;
import com.potalab.http.auth.digest.security.Encryptor;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class AuthenticationInfoHeaderProcessor extends HttpDigestHeaderProcessor {
    /**
     *
     * @param authenticationInfoHeader
     * @param algorithm
     * @param request entity 정보 처리를 위한 request
     * @param uri "Authorization" 헤더 정보의 uri
     * @return
     */
    public String createAuthenticationInfoHeaderValue(
            AuthenticationInfoHeader authenticationInfoHeader,
            HttpDigestAlgorithm algorithm,
            HttpServletRequest request,
            String uri
    ) {
        try {
            StringBuilder builder = new StringBuilder();
            appendQop(builder, authenticationInfoHeader.getQopString(), false);
            appendNextNonce(builder, authenticationInfoHeader);
            appendRspauth(builder, authenticationInfoHeader, algorithm, request, uri);
            appendReturnCnonce(builder, authenticationInfoHeader);
            appendReturnNc(builder, authenticationInfoHeader);
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

    }

    private void appendNextNonce(StringBuilder builder, AuthenticationInfoHeader header) {
        if(header.getNextnonce() == null || header.getNextnonce().isEmpty()) {
            return;
        }

        appendSeparator(builder, true);
        builder.append("nextnonce=");
        builder.append("\"");
        builder.append(header.getNextnonce());
        builder.append("\"");
    }

    /**
     * QOP auth
     * A2 = ":" request-uri
     *
     * QOP auth-int
     * A2 = ":" request-uri ":" H(entity-body)
     */
    private void appendRspauth(
            StringBuilder builder,
            AuthenticationInfoHeader authenticationInfoHeader,
            HttpDigestAlgorithm algorithm,
            HttpServletRequest request,
            String uri
    ) throws IOException {
        String value = "";
        if(authenticationInfoHeader.getQopSet().contains(Qop.AUTH)) {
            value = ":" + Encryptor.encode(algorithm, uri);
        } else if(authenticationInfoHeader.getQopSet().contains(Qop.AUTH_INT)) {
            value = ":" + uri + ":" + Encryptor.encode(algorithm, readRequestBody(request));
        }

        if(!value.isEmpty()) {
            appendSeparator(builder, true);
            builder.append("rspauth=");
            builder.append("\"");
            builder.append(value);
            builder.append("\"");
        }
    }

    private void appendReturnCnonce(StringBuilder builder, AuthenticationInfoHeader authenticationInfoHeader) {
        if(authenticationInfoHeader.getCnonce().isEmpty()) {
            return;
        }

        super.appendCnonce(builder, authenticationInfoHeader.getCnonce(), true);
    }

    private void appendReturnNc(StringBuilder builder, AuthenticationInfoHeader authenticationInfoHeader) {
        if(authenticationInfoHeader.getNc().isEmpty()) {
            return;
        }

        appendNc(builder, authenticationInfoHeader.getNc(), true);
    }
}
