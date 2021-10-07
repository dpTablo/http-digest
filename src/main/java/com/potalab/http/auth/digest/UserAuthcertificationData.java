package com.potalab.http.auth.digest;

import java.util.HashMap;
import java.util.Map;

public class UserAuthcertificationData {
    private Map<String, String> map = new HashMap<>();

    public UserAuthcertificationData() {
        map.put("admin", "1234");
        map.put("user1", "1111");
        map.put("user2", "2222");
    }

    public String getPassword(String userId) {
        return map.get(userId);
    }
}
