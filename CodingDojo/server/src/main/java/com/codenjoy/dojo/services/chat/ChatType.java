package com.codenjoy.dojo.services.chat;

import java.util.Arrays;

public enum ChatType {

    ROOM(1),
    TOPIC(2),
    FIELD(3);

    private final int id;

    ChatType(int id) {
        this.id = id;
    }

    public static ChatType valueOf(int id) {
        return Arrays.stream(values())
                .filter(type -> type.id() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Bad chat type: " + id));
    }

    public int id() {
        return id;
    }
}
