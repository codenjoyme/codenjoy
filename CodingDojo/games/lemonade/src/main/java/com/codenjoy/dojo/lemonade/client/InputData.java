package com.codenjoy.dojo.lemonade.client;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.sun.org.apache.xml.internal.serializer.utils.Utils.messages;

public class InputData {
    private final int day;
    private final float lemonadePrice;
    private final float assets;
    private final WeatherForecast weatherForecast;
    private final ArrayList<String> messages;

    public InputData(int day, float lemonadePrice, float assets, WeatherForecast weatherForecast, ArrayList<String> messages) {

        this.day = day;
        this.lemonadePrice = lemonadePrice;
        this.assets = assets;
        this.weatherForecast = weatherForecast;
        this.messages = messages;
    }

    public static InputData fromJson(JSONObject inputJson){
        int day = inputJson.getInt("day");
        float lemonadePrice = inputJson.getFloat("lemonadePrice");
        float assets = inputJson.getFloat("assets");
        WeatherForecast weatherForecast = inputJson.getEnum(WeatherForecast.class, "weatherForecast");
        JSONArray messagesJson = inputJson.getJSONArray("messages");
        ArrayList<String> messages = new ArrayList<String>();
        for (int i=0; i<messagesJson.length(); i++) {
            messages.add( messagesJson.getString(i) );
        }
        return new InputData(day, lemonadePrice, assets, weatherForecast, messages);
    }
}
