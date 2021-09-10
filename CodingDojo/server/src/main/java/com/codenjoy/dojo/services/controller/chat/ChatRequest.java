package com.codenjoy.dojo.services.controller.chat;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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
