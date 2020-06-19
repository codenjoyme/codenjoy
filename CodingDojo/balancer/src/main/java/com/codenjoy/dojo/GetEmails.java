package com.codenjoy.dojo;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.codenjoy.dojo.FinalistsSorter.load;

public class GetEmails {

    public static void main(String[] args) {
        String day = "day5_2020-06-19";
        JSONArray users = load(day + "/users.txt");

        for (int i = 0; i < users.length(); i++) {
            JSONObject json = users.getJSONObject(i);
            System.out.println(json.get("email"));
        }
    }
}
