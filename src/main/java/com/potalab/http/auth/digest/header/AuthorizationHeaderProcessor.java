package com.potalab.http.auth.digest.header;

import com.potalab.http.auth.digest.exception.AuthorizationHeaderNullPointerException;
import com.potalab.http.auth.digest.field.HttpDigestAlgorithm;
import com.potalab.http.auth.digest.field.Qop;
import com.potalab.http.auth.digest.security.Encryptor;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * H : digest algorithm 을 적용 (해쉬함수)
 * unc : double quotes 제거
 * KD(secret, data) = H(concat(secret, ":", data))
 */
public class AuthorizationHeaderProcessor extends HttpDigestHeaderProcessor {
    /**
     * KD ( H(A1), unq(nonce)
     *      ":" nc
     *      ":" unq(cnonce)
     *      ":" unq(qop)
     *      ":" H(A2)
     * )
     *
     * @return
     */
    public String createResponse(AuthorizationHeader header, HttpServletRequest request, HttpDigestAlgorithm algorithm, String password) throws AuthorizationHeaderNullPointerException {
        Set<Qop> qopSet = header.getQopSet();
        if(!qopSet.contains(Qop.AUTH) && !qopSet.contains(Qop.AUTH_INT)) {
            throw new AuthorizationHeaderNullPointerException("Qop is null");
        }

        String ha1 = Encryptor.encode(algorithm, createA1(header, algorithm, password));
        String unq_nonce = removeDoubleQuotes(header.getNonce());
        String nc = header.getNc();
        String unq_cnonce = removeDoubleQuotes(header.getCnonce());
        String unq_qop = removeDoubleQuotes(header.getQopString());
        String ha2 = Encryptor.encode(algorithm, createA2(header, request, algorithm));
        String data = String.format(
                "%s:%s:%s:%s:%s",
                unq_nonce,
                nc,
                unq_cnonce,
                unq_qop,
                ha2
        );
        return Encryptor.encode(algorithm, ha1 + ":" + data);
    }

    private String createA1(AuthorizationHeader header, HttpDigestAlgorithm algorithm, String password) {
        switch (algorithm) {
            case MD5_SESS:
            case SHA_256_SESS:
            case SHA_512_SESS:
                return createA1_sess(header, algorithm, password);
            default:
                return createA1_normal(header, algorithm, password);
        }
    }

    private String createA2(AuthorizationHeader header, HttpServletRequest request, HttpDigestAlgorithm algorithm) {
        Set<Qop> qopSet = header.getQopSet();

        if(qopSet.contains(Qop.AUTH_INT)) {
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
    private String createA2_normal(AuthorizationHeader header, HttpServletRequest request, HttpDigestAlgorithm algorithm) {
        if(request.getMethod().isEmpty() ||
                header.getUri().isEmpty()
        ) {
            return "";
        }

        return String.format("%s:%s",
                request.getMethod(),
                removeDoubleQuotes(header.getUri())
        );
    }

    /**
     * A2       = Method ":" request-uri ":" H(entity-body)
     *
     * entity-body: 메세지 본문이 아닌 메세지 본문의 해시
     *
     * @return
     */
    private String createA2_auth_int(AuthorizationHeader header, HttpServletRequest request, HttpDigestAlgorithm algorithm) {
        if(request.getMethod().isEmpty() ||
                header.getUri().isEmpty()
        ) {
            return "";
        }

        try {
            return String.format("%s:%s:%s",
                    request.getMethod(),
                    removeDoubleQuotes(header.getUri()),
                    Encryptor.encode(algorithm, readRequestBody(request))
            );
        } catch (Throwable e) {
            e.printStackTrace();
            return "";
        }
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

        return String.format("%s:%s:%s",
                removeDoubleQuotes(header.getUserName()),
                removeDoubleQuotes(header.getRealm()),
                password
        );

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

        return String.format("%s:%s:%s",
                Encryptor.encode(algorithm, hBlockValue),
                removeDoubleQuotes(header.getNonce()),
                removeDoubleQuotes(header.getCnonce())
        );
    }

    private String removeDoubleQuotes(String value) {
        return value.replace("\"", "");
    }
}
