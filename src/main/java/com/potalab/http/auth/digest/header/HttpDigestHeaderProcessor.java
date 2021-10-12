package com.potalab.http.auth.digest.header;

import com.potalab.http.auth.digest.exception.NonceNotCreatedException;
import com.potalab.http.auth.digest.security.Encryptor;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Base64;

public abstract class HttpDigestHeaderProcessor {
    protected String createNonce(String key1, String key2) throws NonceNotCreatedException {
        if(key1 == null || key1.isEmpty() || key2 == null || key2.isEmpty()) {
            throw new NonceNotCreatedException("Missing required keys.");
        }
        String value = String.format("%d:%s:%s", System.currentTimeMillis(), key1, key2);
        return Base64.getEncoder().encodeToString(value.getBytes());
    }

    public void appendSeparator(StringBuilder builder, boolean flag) {
        if(flag) {
            builder.append(", ");
        }
    }

    protected boolean appendQop(StringBuilder builder, String qopStringValue, boolean appendSeparator) {
        if(qopStringValue == null || qopStringValue.isEmpty()) {
            return false;
        }

        appendSeparator(builder, appendSeparator);

        builder.append("qop=");
        builder.append("\"");
        builder.append(qopStringValue);
        builder.append("\"");
        return true;
    }

    protected void appendNonce(
            StringBuilder builder,
            WwwAuthenticateHeader header,
            String nonceCombinKey1,
            String nonceCombinKey2, boolean appendSeparator
    ) throws NonceNotCreatedException {
        appendSeparator(builder, appendSeparator);

        String nonce = createNonce(nonceCombinKey1, nonceCombinKey2);
        String nonceValue = Encryptor.encode(
                header.getAlgorithm(),
                nonce
        );
        builder.append("nonce=");
        builder.append("\"");
        builder.append(nonceValue);
        builder.append("\"");
    }

    protected void appendCnonce(StringBuilder builder, String value, boolean appendSeparator) {
        appendSeparator(builder, appendSeparator);
        builder.append("cnonce=");
        builder.append(value);
    }

    protected void appendNc(StringBuilder builder, String value, boolean appendSeparator) {
        appendSeparator(builder, appendSeparator);
        builder.append("nc=");
        builder.append(value);
    }

    protected String readRequestBody(HttpServletRequest request) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(request.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String buffer;
        while ((buffer = input.readLine()) != null) {
            if (builder.length() > 0) {
                builder.append("\n");
            }
            builder.append(buffer);
        }
        return builder.toString();
    }
}
