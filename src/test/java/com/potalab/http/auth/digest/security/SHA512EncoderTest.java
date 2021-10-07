package com.potalab.http.auth.digest.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SHA512EncoderTest {

    @Test
    void encode() {
        String encodeValue = SHA512Encoder.encode("1111");
        assertEquals("33275a8aa48ea918bd53a9181aa975f15ab0d0645398f5918a006d08675c1cb27d5c645dbd084eee56e675e25ba4019f2ecea37ca9e2995b49fcb12c096a032e", encodeValue);
    }
}