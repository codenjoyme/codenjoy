package com.codenjoy.dojo.lemonade.client;

import org.json.JSONObject;

public class DailyReport {
    private final int day;
    private final int glassesSold;
    private final float glassPrice;
    private final float income;
    private final int glassesMade;
    private final int signsMade;
    private final float expenses;
    private final float profit;
    private final float assets;

    public DailyReport(int day, int glassesSold, float glassPrice, float income,
                       int glassesMade, int signsMade, float expenses, float profit, float assets) {
        this.day = day;
        this.glassesSold = glassesSold;
        this.glassPrice = glassPrice;
        this.income = income;
        this.glassesMade = glassesMade;
        this.signsMade = signsMade;
        this.expenses = expenses;
        this.profit = profit;
        this.assets = assets;
    }

    public static DailyReport fromJson(Object report) {
        JSONObject reportJson = (JSONObject)report;
        int day = reportJson.getInt("day");
        int glassesSold = reportJson.getInt("glassesSold");
        float glassPrice = reportJson.getFloat("glassPrice");
        float income = reportJson.getFloat("income");
        int glassesMade = reportJson.getInt("glassesMade");
        int signsMade = reportJson.getInt("signsMade");
        float expenses = reportJson.getFloat("expenses");
        float profit = reportJson.getFloat("profit");
        float assets = reportJson.getFloat("assets");
        return new DailyReport(day,
                glassesSold,
                glassPrice,
                income,
                glassesMade,
                signsMade,
                expenses,
                profit,
                assets);
    }
}
