package com.potalab.http.digest.auth.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MD5EncoderTest {

    @Test
    void encode() {
        String value1 = MD5Encoder.encode("cat");
        assertNotNull(value1);
        assertTrue(!value1.isEmpty());
    }

    @Test
    void matching() {
        String value1 = MD5Encoder.encode("cat");
        MD5Encoder.matching(value1, "cat");
    }
}