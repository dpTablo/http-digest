package com.potalab.http.auth.digest;

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
     * @param cnonce Authorization 헤더로부터 전달된 cnonce
     * @param nc Authorization 헤더로부터 전달된 nc
     */
    public AuthenticationInfoHeader(String cnonce, String nc) {
        this.cnonce = cnonce;
        this.nc = nc;
    }

    public Set<Qop> getQopSet() {
        return Collections.unmodifiableSet(qopSet);
    }

    public String getQopString() {
        String result = "";
        for(Qop qop : qopSet) {
            result += qop.getValue() + ", ";
        }
        return result.substring(0, result.length() - 2);
    }
}
