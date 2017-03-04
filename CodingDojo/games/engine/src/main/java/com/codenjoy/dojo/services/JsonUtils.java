package com.codenjoy.dojo.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONArray;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by indigo on 2017-03-04.
 */
public class JsonUtils {

    public static List<String> getStrings(JSONArray array) {
        List<String> result = new LinkedList<>();
        for (Object object : array.toList()) {
            result.add((String)object);
        }
        return result;
    }

    public static String prettyPrint(Object object) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(object);
    }

    public static String prettyPrint(String jsonString) {
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(jsonString).getAsJsonObject();

        return prettyPrint(json);
    }
}
