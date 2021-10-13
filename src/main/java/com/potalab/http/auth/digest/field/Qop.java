package com.potalab.http.auth.digest.field;

public enum Qop {
    AUTH("auth"),
    AUTH_INT("auth-int"),
    NONE("none");

    private String value;
    Qop(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
