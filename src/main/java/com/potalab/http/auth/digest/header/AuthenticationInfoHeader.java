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

    @Getter
    private Qop qop = Qop.NONE;

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
        this.qop = authorizationHeader.getQop();
        this.cnonce = authorizationHeader.getCnonce();
        this.nc = authorizationHeader.getNc();
    }

    public String getQopString() {
        return qop.getValue();
    }
}
