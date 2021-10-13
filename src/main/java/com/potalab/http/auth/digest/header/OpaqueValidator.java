package com.potalab.http.auth.digest.header;

public interface OpaqueValidator {
    boolean validate(String opaque);
}
