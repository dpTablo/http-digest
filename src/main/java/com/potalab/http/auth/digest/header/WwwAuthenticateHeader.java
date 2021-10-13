package com.potalab.http.auth.digest.header;

import com.potalab.http.auth.digest.field.HttpDigestAlgorithm;
import com.potalab.http.auth.digest.field.Qop;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class WwwAuthenticateHeader {
    @Getter
    private String realm;

    @Setter
    @Getter
    private String domain;

    @Setter
    @Getter
    private String nonce;

    @Setter
    @Getter
    private String opaque;

    @Setter
    @Getter
    private boolean stale;

    @Setter
    @Getter
    private HttpDigestAlgorithm algorithm = HttpDigestAlgorithm.MD5;

    private Set<Qop> qopSet = new HashSet<>();

    @Getter
    private String charset;

    @Getter
    private boolean userHash = false;

    public WwwAuthenticateHeader(String realm) {
        this.realm = realm;
    }

    public void setCharset(String value) {
        if(value.equals("UTF-8")) {
            this.charset = "UTF-8";
        }
    }

    public void addQop(Qop value) {
        this.qopSet.add(value);
    }

    public void addQop(Set<Qop> values) {
        this.qopSet.addAll(values);
    }

    public void getQopSet() {
        Collections.unmodifiableSet(this.qopSet);
    }

    public String getQopString() {
        if(qopSet.contains(Qop.AUTH)) {
            return Qop.AUTH.getValue();
        } else if(qopSet.contains(Qop.AUTH_INT)) {
            return Qop.AUTH_INT.getValue();
        } else {
            return "";
        }
    }
}
