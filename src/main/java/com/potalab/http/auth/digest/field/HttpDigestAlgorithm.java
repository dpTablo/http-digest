package com.potalab.http.auth.digest.field;

public enum HttpDigestAlgorithm {
    MD5("MD5"),
    MD5_SESS("MD5-sess"),
    SHA_256("SHA-256"),
    SHA_256_SESS("SHA-256-sess"),
    SHA_512("SHA-512"),
    SHA_512_SESS("SHA-512-sess");

    private String value;
    HttpDigestAlgorithm(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
