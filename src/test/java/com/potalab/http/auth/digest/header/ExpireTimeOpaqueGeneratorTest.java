package com.potalab.http.auth.digest.header;

import com.potalab.http.auth.digest.field.HttpDigestAlgorithm;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExpireTimeOpaqueGeneratorTest {

    @Test
    void get() {
        final long TIMEOUT = 60000 * 30;
        ExpireTimeOpaqueGenerator opaqueGenerator = new ExpireTimeOpaqueGenerator("pk", TIMEOUT);
        assertFalse(opaqueGenerator.get(HttpDigestAlgorithm.MD5).isEmpty());
    }
}