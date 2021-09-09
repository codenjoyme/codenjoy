package com.codenjoy.dojo.services.controller.chat;

import com.codenjoy.dojo.services.chat.Filter;
import org.json.JSONObject;

import java.util.function.Function;

public class ChatRequest {

    private final JSONObject data;
    private final String method;

    public ChatRequest(String message) {
        JSONObject json = new JSONObject(message);
        data = json.getJSONObject("data");
        method = json.getString("command");
    }

    public Filter filter() {
        return Filter
                .room(room())
                .afterId(afterId())
                .beforeId(beforeId())
                .count(count())
                .inclusive(inclusive())
                .get();
    }

    public String method() {
        return method;
    }

    public Boolean inclusive() {
        return getBoolean("inclusive");
    }

    public Integer count() {
        return getInteger("count");
    }

    public Integer beforeId() {
        return getInteger("beforeId");
    }

    public Integer afterId() {
        return getInteger("afterId");
    }

    public String room() {
        return getString("room");
    }

    public String text() {
        return getString("text");
    }

    public Integer id() {
        return getInteger("id");
    }

    private <T> T getNullable(String key, Function<String, T> get) {
        if (!data.has(key)) {
            return null;
        }

        return get.apply(key);
    }

    private Integer getInteger(String key) {
        return getNullable(key, data::getInt);
    }

    private String getString(String key) {
        return getNullable(key, data::getString);
    }

    private Boolean getBoolean(String key) {
        return getNullable(key, data::getBoolean);
    }

    @Override
    public String toString() {
        return "ChatRequest" + data.toString();
    }
}
