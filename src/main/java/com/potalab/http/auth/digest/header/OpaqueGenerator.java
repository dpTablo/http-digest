package com.potalab.http.auth.digest.header;

import com.potalab.http.auth.digest.field.HttpDigestAlgorithm;

public interface OpaqueGenerator {
    String get(HttpDigestAlgorithm algorithm);
}
