package com.potalab.http.auth.digest.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SHA256EncoderTest {

    @Test
    void encode() {
        String encodeValue = SHA256Encoder.encode("1111");
        assertEquals("0ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c", encodeValue);
    }
}