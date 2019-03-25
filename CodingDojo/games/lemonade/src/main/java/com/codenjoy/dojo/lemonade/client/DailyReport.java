package com.codenjoy.dojo.lemonade.client;

import org.json.JSONObject;

public class DailyReport {
    private final int day;
    private final int lemonadeSold;
    private final float lemonadePrice;
    private final float income;
    private final int lemonadeMade;
    private final int signsMade;
    private final float expenses;
    private final float profit;
    private final float assets;

    public DailyReport(int day, int lemonadeSold, float lemonadePrice, float income,
                       int lemonadeMade, int signsMade, float expenses, float profit, float assets) {
        this.day = day;
        this.lemonadeSold = lemonadeSold;
        this.lemonadePrice = lemonadePrice;
        this.income = income;
        this.lemonadeMade = lemonadeMade;
        this.signsMade = signsMade;
        this.expenses = expenses;
        this.profit = profit;
        this.assets = assets;
    }

    public static DailyReport fromJson(Object report) {
        JSONObject reportJson = (JSONObject)report;
        int day = reportJson.getInt("day");
        int lemonadeSold = reportJson.getInt("lemonadeSold");
        float lemonadePrice = reportJson.getFloat("lemonadePrice");
        float income = reportJson.getFloat("income");
        int lemonadeMade = reportJson.getInt("lemonadeMade");
        int signsMade = reportJson.getInt("signsMade");
        float expenses = reportJson.getFloat("expenses");
        float profit = reportJson.getFloat("profit");
        float assets = reportJson.getFloat("assets");
        return new DailyReport(day,
                lemonadeSold,
                lemonadePrice,
                income,
                lemonadeMade,
                signsMade,
                expenses,
                profit,
                assets);
    }
}
