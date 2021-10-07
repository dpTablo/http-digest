package com.potalab.http.auth.digest;

import com.potalab.http.auth.digest.security.Encryptor;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

public abstract class HttpDigestHeaderProcessor {
    protected String createNonce() {
        return UUID.randomUUID().toString();
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

    protected void appendNonce(StringBuilder builder, WwwAuthenticateHeader header, boolean appendSeparator) {
        appendSeparator(builder, appendSeparator);

        String nonceValue = Encryptor.encode(header.getAlgorithm(), createNonce());
        builder.append("nonce=");
        builder.append("\"");
        builder.append(nonceValue);
        builder.append("\"");
    }

    protected void appendCnonce(StringBuilder builder, String value, boolean appendSeparator) {
        appendSeparator(builder, appendSeparator);
        builder.append("cnonce=");
        builder.append("\"");
        builder.append(value);
        builder.append("\"");
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
