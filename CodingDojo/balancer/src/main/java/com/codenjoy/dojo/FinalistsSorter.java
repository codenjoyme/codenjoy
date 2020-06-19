package com.codenjoy.dojo;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;

public class FinalistsSorter {

    public static void main(String[] args) {
        String day = "day3";
        JSONArray finalists = load(day + "/finalists.txt");
        JSONArray users = load(day + "/users.txt");

        // --------

        Map<String, JSONObject> usersMap = new HashMap<>();
        for (int i = 0; i < users.length(); i++) {
            JSONObject json = users.getJSONObject(i);
            usersMap.put(json.getString("id"), json);
        }

        // --------

        List<JSONObject> list = new ArrayList<>();
        for (int i = 0; i < finalists.length(); i++) {
            JSONObject json = finalists.getJSONObject(i);
            json.remove("time");
            json.remove("server");

            JSONObject user = usersMap.get(json.getString("id"));
            copy(user, json, "fullName");
            copy(user, json, "email");
            copy(user, json, "phone");
            copy(user, json, "city");
            copy(user, json, "skills");

            list.add(json);
        }

        Collections.sort(list, comparing((JSONObject o) -> getString(o)).reversed());

        list.forEach(it -> System.out.println("\n" + it.toString()));
    }

    @SneakyThrows
    public static JSONArray load(String fileName) {
        File file = new File("battle/" + fileName);
        StringBuilder result = new StringBuilder();
        try (Stream linesStream = Files.lines(file.toPath())) {
            linesStream.forEach(line -> result.append(line));
        }
        return new JSONArray(result.toString());
    }

    private static void copy(JSONObject from, JSONObject to, String key) {
        to.put(key, from.getString(key));
    }

    private static String getString(JSONObject o) {
        String score = String.valueOf(o.getInt("score"));
        return o.getString("day") + "_" + StringUtils.leftPad(score, 10, "0");
    }
}
