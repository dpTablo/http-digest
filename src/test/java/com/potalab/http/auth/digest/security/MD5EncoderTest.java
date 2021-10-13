package com.potalab.http.auth.digest.security;

import com.potalab.http.auth.digest.field.HttpDigestAlgorithm;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MD5EncoderTest {

    @Test
    void encode() {
        String encodeValue = MD5Encoder.encode("1111");
        assertEquals("b59c67bf196a4758191e42f76670ceba", encodeValue);
    }

    @Test
    void matching() {
        String value1 = MD5Encoder.encode("cat");
        assertTrue(Encryptor.matching(HttpDigestAlgorithm.MD5, value1, "cat"));
    }
}