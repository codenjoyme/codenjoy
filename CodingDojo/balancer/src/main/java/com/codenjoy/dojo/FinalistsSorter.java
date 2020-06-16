package com.codenjoy.dojo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Comparator.comparingInt;

public class FinalistsSorter {
    public static void main(String[] args) {
        JSONArray object = new JSONArray("[{\"id\":\"66ulvrge91942grmxua1\",\"name\":\"Stiven Pupkin\",\"score\":133,\"day\":null,\"time\":\"2020-06-15T19:00:05.001+0300\",\"server\":\"botchallenge.cloud.epam.com\",\"winner\":false},\n" +
                "{\"id\":\"ipipsyrfsxtodwrvqpoa\",\"name\":\"Eva Pupkina\",\"score\":700,\"day\":null,\"time\":\"2020-06-15T19:00:05.001+0300\",\"server\":\"botchallenge.cloud.epam.com\",\"winner\":false}]");

        JSONArray sorted = new JSONArray();
        List<JSONObject> list = new ArrayList<>();
        for (int i = 0; i < object.length(); i++) {
            JSONObject json = object.getJSONObject(i);
            json.remove("day");
            json.remove("time");
            json.remove("server");
            list.add(json);
        }

        Collections.sort(list, comparingInt((JSONObject a) -> a.getInt("score")).reversed());

        for (int i = 0; i < object.length(); i++) {
            sorted.put(list.get(i));
        }

        System.out.println(sorted.toString());
    }
}
