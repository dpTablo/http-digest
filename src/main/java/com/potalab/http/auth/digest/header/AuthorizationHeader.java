package com.potalab.http.auth.digest.header;

import com.potalab.http.auth.digest.field.Qop;

import java.util.*;

public class AuthorizationHeader {
    private Map<String, String> map = new HashMap<>();

    public AuthorizationHeader(String value) {
        parse(value);
    }

    public String getType() {
        return map.get("type");
    }

    public String getUserName() {
        return map.get("username");
    }

    public String getUserNameWithRemoveDoubleQuotes() {
        return map.get("username") != null ? map.get("username").replace("\"", "") : "";
    }

    public String getRealm() {
        return map.get("realm");
    }

    public String getNonce() {
        return map.get("nonce");
    }

    public Set<Qop> getQopSet() {
        try {
            Set<Qop> list = new HashSet<>();

            String[] qopArray = map.get("qop").split(",");
            for(String item : qopArray) {
                Qop qop = Qop.valueOf(item.trim().toUpperCase().replace("-", "_"));
                assert qop != null;

                list.add(qop);
            }
            return list;
        } catch (Throwable e) {
            return Collections.emptySet();
        }
    }

    public String getQopString() {
        return map.get("qop");
    }

    public String getNc() {
        return map.get("nc");
    }

    public String getCnonce() {
        return map.get("cnonce");
    }

    public String getResponse() {
        return map.get("response");
    }

    public String getResponseWithRemovedDoubleQuotes() {
        return map.get("response") != null ? map.get("response").replace("\"", "") : "";
    }

    public String getUri() {
        return map.get("uri");
    }

    public String getOpaque() {
        return map.get("opaque");
    }

    private void parse(String value) {
        if(value == null || value.isEmpty()) {
            return;
        }

        Map<String, String> resultMap = new HashMap<>();

        int endIndex = 0;

        endIndex = value.indexOf(" ");
        String type = value.substring(0, endIndex);
        resultMap.put("type", type);

        value = value.substring(endIndex + 1);

        int commaCount = 0;
        String headerName = "";
        String headerValue = "";
        StringBuilder characterBuilder = new StringBuilder();
        char[] chars = value.toCharArray();

        for(int i = 0; i < chars.length; i++) {
            char character = chars[i];

            if(character == ',') {
                commaCount++;
            }

            if(commaCount == 1) {
                putHeader(map, characterBuilder.toString());

                characterBuilder.setLength(0);
                commaCount = 0;
                continue;
            }

            characterBuilder.append(character);
        }

        putHeader(map, characterBuilder.toString());
        map.putAll(resultMap);
    }

    private void putHeader(Map<String, String> map, String value) {
        int equalsIndex = value.indexOf('=');

        String headerName = value.substring(0, equalsIndex);
        String headerValue = value.substring(equalsIndex + 1);
        map.put(headerName.trim(), headerValue);
    }
}
