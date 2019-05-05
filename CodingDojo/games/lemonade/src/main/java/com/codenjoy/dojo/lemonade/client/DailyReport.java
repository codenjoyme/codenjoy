package com.codenjoy.dojo.lemonade.client;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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
    private final float assetsAfter;

    public DailyReport(int day, int lemonadeSold, float lemonadePrice, float income,
                       int lemonadeMade, int signsMade, float expenses, float profit, float assetsAfter) {
        this.day = day;
        this.lemonadeSold = lemonadeSold;
        this.lemonadePrice = lemonadePrice;
        this.income = income;
        this.lemonadeMade = lemonadeMade;
        this.signsMade = signsMade;
        this.expenses = expenses;
        this.profit = profit;
        this.assetsAfter = assetsAfter;
    }

    public static DailyReport fromJson(Object report) {
        JSONObject reportJson = (JSONObject) report;
        int day = reportJson.getInt("day");
        int lemonadeSold = reportJson.getInt("lemonadeSold");
        float lemonadePrice = reportJson.getFloat("lemonadePrice");
        float income = reportJson.getFloat("income");
        int lemonadeMade = reportJson.getInt("lemonadeMade");
        int signsMade = reportJson.getInt("signsMade");
        float expenses = reportJson.getFloat("expenses");
        float profit = reportJson.getFloat("profit");
        float assetsAfter = reportJson.getFloat("assetsAfter");
        return new DailyReport(day,
                lemonadeSold,
                lemonadePrice,
                income,
                lemonadeMade,
                signsMade,
                expenses,
                profit,
                assetsAfter);
    }

    public int getDay() {
        return day;
    }

    public int getLemonadeMade() {
        return lemonadeMade;
    }

    public int getSignsMade() {
        return signsMade;
    }

    public float getLemonadePrice() {
        return lemonadePrice;
    }

    public int getLemonadeSold() {
        return lemonadeSold;
    }

    public float getExpenses() {
        return expenses;
    }

    public float getIncome() {
        return income;
    }

    public float getProfit() {
        return profit;
    }

    public float getAssetsAfter() {
        return assetsAfter;
    }
}
