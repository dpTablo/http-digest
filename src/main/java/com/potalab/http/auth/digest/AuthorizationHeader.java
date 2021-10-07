package com.potalab.http.auth.digest;

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
