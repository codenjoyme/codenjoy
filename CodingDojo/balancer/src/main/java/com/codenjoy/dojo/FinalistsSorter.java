package com.codenjoy.dojo;

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
        String day = "day2";
        JSONArray finalists = load(day + "/finalists.txt");
        JSONArray users = load(day + "/users.txt");
        JSONArray todayScores = load(day + "/todayScores.txt");

        // --------

        Map<String, JSONObject> usersMap = new HashMap<>();
        for (int i = 0; i < users.length(); i++) {
            JSONObject json = users.getJSONObject(i);
            usersMap.put(json.getString("id"), json);
        }

        // --------

        String today = todayScores.getJSONObject(0).getString("time").split("T")[0];




        // ----------

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
    private static JSONArray load(String fileName) {
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
