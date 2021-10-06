package com.potalab.http.digest.auth;

import com.potalab.http.digest.auth.security.HttpDigestAlgorithm;
import com.potalab.http.digest.auth.security.MD5Encoder;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * WWW-Authenticate 헤더의 값을 생성합니다.
 *
 * - 임시구현 항목
 * 'qop' : 'auth, auth-int' 고정 출력
 *
 * - 미구현 항목
 * 'token68'
 */
public class WwwAuthenticateHttpHeaderGenerator {
    @Setter
    private String realm = "potalab";

    @Setter
    private String domain = "";

    @Setter
    private HttpDigestAlgorithm algorithm = HttpDigestAlgorithm.MD5;

    @Setter
    private String qop = "auth";

    public String generateToUnauthorized(HttpServletRequest request, HttpServletResponse response) {
        StringBuilder builder = new StringBuilder();

        appendAuthScheme(builder);
        appendRealm(builder);
        appendNonce(builder);
        appendQop(builder);

        return builder.toString();
    }

    public String generate(HttpServletRequest request, HttpServletResponse response) {
        StringBuilder builder = new StringBuilder();

        appendAuthScheme(builder);
        appendRealm(builder);
        appendDomain(builder);
        appendAlgorithm(builder);
        appendNonce(builder);
        appendQop(builder);

        return builder.toString();
    }

    private void appendAuthScheme(StringBuilder builder) {
        builder.append("Digest ");
    }

    private void appendRealm(StringBuilder builder) {
        if(realm == null || realm.isEmpty()) {
            return;
        }

        builder.append("realm=");
        builder.append("\"");
        builder.append(realm);
        builder.append("\"");
    }

    private void appendDomain(StringBuilder builder) {
        if(domain == null || domain.isEmpty()) {
            return;
        }

        builder.append(", domain=");
        builder.append("\"");
        builder.append(realm);
        builder.append("\"");
    }

    private void appendAlgorithm(StringBuilder builder) {
        builder.append(", algorithm=");
        builder.append("\"");
        builder.append(algorithm.getValue());
        builder.append("\"");
    }

    private void appendNonce(StringBuilder builder) {
        String value = UUID.randomUUID().toString();

        String nonceValue = "";
        if(algorithm == HttpDigestAlgorithm.SHA_256) {
            // TODO
        } else if(algorithm == HttpDigestAlgorithm.SHA_512) {
            // TODO
        } else {
            nonceValue = MD5Encoder.encode(value);
        }

        builder.append(", nonce=");
        builder.append("\"");
        builder.append(nonceValue);
        builder.append("\"");
    }

    private void appendQop(StringBuilder builder) {
        builder.append(", qop=");
        builder.append("\"");
        builder.append("auth");
        builder.append("\"");
    }
}
