package com.potalab.http.digest.auth;

import com.potalab.http.digest.auth.security.Encryptor;
import com.potalab.http.digest.auth.security.HttpDigestAlgorithm;

import javax.servlet.http.HttpServletRequest;

public class AuthorizationHeaderProcessor {
    public String createA1(AuthorizationHeader header, HttpDigestAlgorithm algorithm, String password) {
        switch (algorithm) {
            case MD5_SESS:
            case SHA_256_SESS:
            case SHA_512_SESS:
                return createA1_sess(header, algorithm, password);
            default:
                return createA1_normal(header, algorithm, password);
        }
    }

    public String createA2(AuthorizationHeader header, HttpServletRequest request, HttpDigestAlgorithm algorithm) {
        String qop = header.getQop();
        String uri = removeDoubleQuotes(header.getUri());

        if(qop.contains("auth-int")) {
            return createA2_auth_int(header, request, algorithm);
        } else {
            return createA2_normal(header, request, algorithm);
        }
    }

    /**
     * A2       = Method ":" request-uri
     *
     * @return
     */
    private String createA2_auth_int(AuthorizationHeader header, HttpServletRequest request, HttpDigestAlgorithm algorithm) {
        if(request.getMethod().isEmpty() ||
                header.getUri().isEmpty()
        ) {
            return "";
        }
        return String.format("%s:%s", request.getMethod(), header.getUri());
    }

    /**
     * A2       = Method ":" request-uri ":" H(entity-body)
     *
     * entity-body: 메세지 본문이 아닌 메세지 본문의 해시
     *
     * @return
     */
    private String createA2_normal(AuthorizationHeader header, HttpServletRequest request, HttpDigestAlgorithm algorithm) {
        return "";
    }

    /**
     * unq(username) ":" unq(realm) ":" passwd
     * @return
     */
    private String createA1_normal(AuthorizationHeader header, HttpDigestAlgorithm algorithm, String password) {
        if(header.getUserName().isEmpty() ||
                header.getRealm().isEmpty() ||
                password.isEmpty()
        ) {
            return "";
        }

        String value = String.format("%s:%s:%s",
                removeDoubleQuotes(header.getUserName()),
                removeDoubleQuotes(header.getRealm()),
                password
        );
        return Encryptor.encode(algorithm, value);
    }

    /**
     * H( unq(username) ":" unq(realm) ":" passwd )
     *      ":" unq(nonce-prime) ":" unq(cnonce-prime)
     * @return
     */
    private String createA1_sess(AuthorizationHeader header, HttpDigestAlgorithm algorithm, String password) {
        if(header.getUserName().isEmpty() ||
                header.getRealm().isEmpty() ||
                password.isEmpty() ||
                header.getNonce().isEmpty() ||
                header.getCnonce().isEmpty()
        ) {
            return "";
        }

        String hBlockValue = String.format("%s:%s:%s",
                removeDoubleQuotes(header.getUserName()),
                removeDoubleQuotes(header.getRealm()),
                password
        ).trim();

        String value = String.format("%s:%s:%s",
                hBlockValue,
                removeDoubleQuotes(header.getNonce()),
                removeDoubleQuotes(header.getCnonce())
        );

        return Encryptor.encode(algorithm, value);
    }

    private String removeDoubleQuotes(String value) {
        return value.replace("\"", "");
    }
}
