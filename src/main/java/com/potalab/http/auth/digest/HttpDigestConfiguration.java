package com.potalab.http.auth.digest;

import com.potalab.http.auth.digest.field.HttpDigestAlgorithm;
import com.potalab.http.auth.digest.field.Qop;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

public class HttpDigestConfiguration {
    @Getter
    @Setter
    private String noncePrivateKey = "potalab";

    @Getter
    @Setter
    private String opaquePrivateKey = "potalab";

    /**
     * milliseconds
     */
    @Getter
    @Setter
    private int timeout = 60000 * 30;

    @Getter
    @Setter
    private String realmName = "potalab_realm";

    @Getter
    @Setter
    private HttpDigestAlgorithm algorithm = HttpDigestAlgorithm.MD5;

    @Getter
    private Set<Qop> qopSet = new HashSet<>();

    public void addQopSet(Qop qop) {
        qopSet.add(qop);
    }
}
