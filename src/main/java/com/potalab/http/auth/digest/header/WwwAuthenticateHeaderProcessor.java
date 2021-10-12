package com.potalab.http.auth.digest.header;

import com.potalab.http.auth.digest.exception.HttpDigestModuleRuntimeException;
import com.potalab.http.auth.digest.exception.NonceNotCreatedException;

/**
 * WWW-Authenticate 헤더의 값을 생성합니다.
 *
 * - 미구현 항목
 * 'token68'
 */
public class WwwAuthenticateHeaderProcessor extends HttpDigestHeaderProcessor {
    private OpaqueGenerator opaqueGenerator;

    public WwwAuthenticateHeaderProcessor(OpaqueGenerator opaqueGenerator) throws HttpDigestModuleRuntimeException {
        if(opaqueGenerator == null) {
            throw new HttpDigestModuleRuntimeException("OpaqueGenerator is required.");
        }
        this.opaqueGenerator = opaqueGenerator;
    }

    public String createUnauthorizedHeaderValue(WwwAuthenticateHeader header, String nonceCombinKey1, String nonceCombinKey2) throws NonceNotCreatedException {
        StringBuilder builder = new StringBuilder();

        appendAuthScheme(builder);
        appendRealm(builder, header);
        appendDomain(builder, header);
        appendNonce(builder, header, nonceCombinKey1, nonceCombinKey2,true);
        appendQop(builder, header.getQopString(), true);
        appendAlgorithm(builder, header);
        appendStale(builder, header);
        appendOpaque(builder, header);

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
        appendSeparator(builder, true);
        builder.append("algorithm=");
        builder.append("\"");
        builder.append(header.getAlgorithm().getValue());
        builder.append("\"");
    }

    private void appendStale(StringBuilder builder, WwwAuthenticateHeader header) {
        // TODO not implement.
        return;
    }

    private void appendOpaque(StringBuilder builder, WwwAuthenticateHeader header) {
        appendSeparator(builder, true);
        builder.append("opaque=");
        builder.append("\"");
        builder.append(opaqueGenerator.get(header.getAlgorithm()));
        builder.append("\"");
    }
}
