package com.codenjoy.dojo.lemonade.client;

import org.json.JSONObject;

public class InputData {
    private final int day;
    private final float lemonadePrice;
    private final float assets;
    private final WeatherForecast weatherForecast;

    public InputData(int day, float lemonadePrice, float assets, WeatherForecast weatherForecast) {

        this.day = day;
        this.lemonadePrice = lemonadePrice;
        this.assets = assets;
        this.weatherForecast = weatherForecast;
    }

    public static InputData fromJson(JSONObject inputJson){
        int day = inputJson.getInt("day");
        float lemonadePrice = inputJson.getFloat("lemonadePrice");
        float assets = inputJson.getFloat("assets");
        WeatherForecast weatherForecast = inputJson.getEnum(WeatherForecast.class, "weatherForecast");
        return new InputData(day, lemonadePrice, assets, weatherForecast);
    }
}
