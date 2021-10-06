package com.potalab.http.digest.auth;

import java.util.HashMap;
import java.util.Map;

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

    public String getRealm() {
        return map.get("realm");
    }

    public String getNonce() {
        return map.get("nonce");
    }

    public String getQop() {
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

    public String getUri() {
        return map.get("uri");
    }

    private void parse(String value) {
        Map<String, String> resultMap = new HashMap<>();

        int endIndex = 0;

        endIndex = value.indexOf(" ");
        String type = value.substring(0, endIndex);
        resultMap.put("type", type);

        value = value.substring(endIndex + 1);

        int equalsCount = 0;
        String headerName = "";
        String headerValue = "";
        char[] chars = value.toCharArray();

        for(int i = 0; i < chars.length; i++) {
            char character = chars[i];

            if(character == '=') {
                equalsCount++;
                continue;
            }

            if(character == ',' && equalsCount == 1) {
                equalsCount = 0;

                resultMap.put(headerName.trim(), headerValue);
                headerName = "";
                headerValue = "";
                continue;
            }

            if(equalsCount < 1) {
                headerName += character;
            } else {
                headerValue += character;
            }
        }

        if(!headerName.isEmpty() && !headerValue.isEmpty()) {
            resultMap.put(headerName.trim(), headerValue);
        }
        map.putAll(resultMap);
    }
}
