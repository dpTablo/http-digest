package com.potalab.http.auth.digest;

/**
 * WWW-Authenticate 헤더의 값을 생성합니다.
 *
 * - 임시구현 항목
 * 'qop' : 'auth, auth-int' 고정 출력
 *
 * - 미구현 항목
 * 'token68'
 */
public class WwwAuthenticateHeaderProcessor extends HttpDigestHeaderProcessor {
    public String createUnauthorizedHeaderValue(WwwAuthenticateHeader header) {
        StringBuilder builder = new StringBuilder();

        appendAuthScheme(builder);
        appendRealm(builder, header);
        appendDomain(builder, header);
        appendNonce(builder, header, true);
        appendQop(builder, header.getQopString(), true);
        appendAlgorithm(builder, header);

        return builder.toString();
    }

    private void appendAuthScheme(StringBuilder builder) {
        builder.append("Digest ");
    }

    private void appendRealm(StringBuilder builder, WwwAuthenticateHeader header) {
        if(header.getRealm() == null || header.getRealm().isEmpty()) {
            return;
        }

        builder.append("realm=");
        builder.append("\"");
        builder.append(header.getRealm());
        builder.append("\"");
    }

    private void appendDomain(StringBuilder builder, WwwAuthenticateHeader header) {
        if(header.getDomain() == null || header.getDomain().isEmpty()) {
            return;
        }

        appendSeparator(builder, true);
        builder.append("domain=");
        builder.append("\"");
        builder.append(header.getDomain());
        builder.append("\"");
    }

    private void appendAlgorithm(StringBuilder builder, WwwAuthenticateHeader header) {
        builder.append(", algorithm=");
        builder.append("\"");
        builder.append(header.getAlgorithm().getValue());
        builder.append("\"");
    }
}
