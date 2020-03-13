package com.codenjoy.dojo.config.oauth2;

import java.util.Map;

public class UserData {

    private String id;
    private String email;
    private String readableName;

    public UserData(Map<String, ?> map) {
        id = (String) map.get("player_id");
        email = (String) map.get("email");
        readableName = (String) map.get("name");
    }

    public String id() {
        return id;
    }

    public String email() {
        return email;
    }

    public String readableName() {
        return readableName;
    }
}
