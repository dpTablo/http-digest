package com.potalab.http.digest.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthorizationHeaderTest {

    @Test
    void parse1() {
        /*
            Digest
            username="admin",
            realm="testingRealm",
            nonce="960a48d7330f27e8d16abcf60c4d03c1",
            uri="/http-digest/digest",
            response="98a852bb03e027262d347511352123c7",
            qop=auth,
            nc=00000002,
            cnonce="370ad495c868bd67"
         */
        String testHeaderValue = "Digest username=\"admin\", realm=\"testingRealm\", nonce=\"960a48d7330f27e8d16abcf60c4d03c1\", uri=\"/http-digest/digest\", response=\"98a852bb03e027262d347511352123c7\", qop=auth, nc=00000002, cnonce=\"370ad495c868bd67\"";

        AuthorizationHeader header = new AuthorizationHeader(testHeaderValue);
        assertEquals("\"admin\"", header.getUserName());
        assertEquals("\"testingRealm\"", header.getRealm());
        assertEquals("\"960a48d7330f27e8d16abcf60c4d03c1\"", header.getNonce());
        assertEquals("\"/http-digest/digest\"", header.getUri());
        assertEquals("\"98a852bb03e027262d347511352123c7\"", header.getResponse());
        assertEquals("auth", header.getQop());
        assertEquals("00000002", header.getNc());
        assertEquals("\"370ad495c868bd67\"", header.getCnonce());
    }
}