package com.codenjoy.dojo.lemonade.model;

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

public class SalesResult {
    private final int day;
    private final double assetsBefore;
    private final int lemonadeSold;
    private final double lemonadePrice;
    private final double income;
    private final int lemonadeMade;
    private final int signsMade;
    private final double expenses;
    private final double profit;
    private final double assetsAfter;
    private final boolean isBankrupt;
    private final boolean isInputError;

    public SalesResult(int day, double assetsBefore, int lemonadeSold, double lemonadePrice, double income, int lemonadeMade, int signsMade,
                       double expenses, double profit, double assetsAfter, boolean isBankrupt, boolean isInputError) {

        this.day = day;
        this.assetsBefore = toMoneyFormat(assetsBefore);
        this.lemonadeSold = lemonadeSold;
        this.lemonadePrice = toMoneyFormat(lemonadePrice);
        this.income = toMoneyFormat(income);
        this.lemonadeMade = lemonadeMade;
        this.signsMade = signsMade;
        this.expenses = toMoneyFormat(expenses);
        this.profit = toMoneyFormat(profit);
        this.assetsAfter = toMoneyFormat(assetsAfter);
        this.isBankrupt = isBankrupt;
        this.isInputError = isInputError;
    }

    public boolean isBankrupt() {
        return isBankrupt;
    }

    public JSONObject toJSONObject() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("day", day);
        jsonObj.put("assetsBefore", assetsBefore);
        jsonObj.put("lemonadeSold", lemonadeSold);
        jsonObj.put("lemonadePrice", lemonadePrice);
        jsonObj.put("income", income);
        jsonObj.put("lemonadeMade", lemonadeMade);
        jsonObj.put("signsMade", signsMade);
        jsonObj.put("expenses", expenses);
        jsonObj.put("profit", profit);
        jsonObj.put("assetsAfter", assetsAfter);
        return jsonObj;
    }

    public double getProfit() {
        return this.profit;
    }

    public boolean isInputError(){
        return this.isInputError;
    }

    private static double toMoneyFormat(double dollars) {
        return Math.round(dollars * 100) / 100.0;
    }

    public int getDay() {
        return day;
    }

    public double getAssetsAfter() {
        return assetsAfter;
    }
}
