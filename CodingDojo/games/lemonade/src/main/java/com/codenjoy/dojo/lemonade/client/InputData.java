package com.codenjoy.dojo.lemonade.client;

import org.json.JSONObject;

public class InputData {
    private final int day;
    private final float costOfLemonade;
    private final int lemonadeStand;
    private final float assets;
    private final float signCost;
    private final Forecast forecast;

    public InputData(int day, float costOfLemonade, int lemonadeStand, float assets, float signCost, Forecast forecast) {

        this.day = day;
        this.costOfLemonade = costOfLemonade;
        this.lemonadeStand = lemonadeStand;
        this.assets = assets;
        this.signCost = signCost;
        this.forecast = forecast;
    }

    public static InputData fromJson(JSONObject inputJson){
        int day = inputJson.getInt("day");
        float costOfLemonade = inputJson.getFloat("costOfLemonade");
        int lemonadeStand = inputJson.getInt("lemonadeStand");
        float assets = inputJson.getFloat("assets");
        float signCost = inputJson.getFloat("signCost");
        Forecast forecast = inputJson.getEnum(Forecast.class, "forecast");
        return new InputData(day, costOfLemonade, lemonadeStand, assets, signCost, forecast);
    }
}
