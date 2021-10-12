package com.potalab.http.auth.digest.header;

import com.potalab.http.auth.digest.field.Qop;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class AuthenticationInfoHeader {
    @Setter
    @Getter
    private String nextnonce = "";

    private Set<Qop> qopSet = new HashSet<>();

    @Setter
    @Getter
    private String rspauth = "";

    @Getter
    private String cnonce = "";

    @Getter
    private String nc = "";

    /**
     *
     * cnonce Authorization 헤더로부터 전달된 cnonce
     * Authorization 헤더로부터 전달된 nc
     */
    public AuthenticationInfoHeader(AuthorizationHeader authorizationHeader) {
        this.qopSet.addAll(authorizationHeader.getQopSet());
        this.cnonce = authorizationHeader.getCnonce();
        this.nc = authorizationHeader.getNc();
    }

    public Set<Qop> getQopSet() {
        return Collections.unmodifiableSet(qopSet);
    }

    public String getQopString() {
        if(qopSet.isEmpty()) {
            return "";
        }

        String result = "";
        for(Qop qop : qopSet) {
            result += qop.getValue() + ", ";
        }
        return result.substring(0, result.length() - 2);
    }
}
