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
    private final int lemonadeSold;
    private final double lemonadePrice;
    private final double income;
    private final int lemonadeMade;
    private final int signsMade;
    private final double expenses;
    private final double profit;
    private final double assets;
    private final boolean isBunkrupt;

    public SalesResult(int day, int lemonadeSold, double lemonadePrice, double income, int lemonadeMade, int signsMade,
                       double expenses, double profit, double assets, boolean isBunkrupt) {

        this.day = day;
        this.lemonadeSold = lemonadeSold;
        this.lemonadePrice = lemonadePrice;
        this.income = income;
        this.lemonadeMade = lemonadeMade;
        this.signsMade = signsMade;
        this.expenses = expenses;
        this.profit = profit;
        this.assets = assets;
        this.isBunkrupt = isBunkrupt;
    }

    public boolean isBunkrupt(){
        return isBunkrupt;
    }

    public JSONObject toJSONObject(){
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("day", day);
        jsonObj.put("lemonadeSold", lemonadeSold);
        jsonObj.put("lemonadePrice", lemonadePrice);
        jsonObj.put("income", income);
        jsonObj.put("lemonadeMade", lemonadeMade);
        jsonObj.put("signsMade", signsMade);
        jsonObj.put("expenses", expenses);
        jsonObj.put("profit", profit);
        jsonObj.put("assets", assets);
        return jsonObj;
    }

    public double getProfit() {
        return this.profit;
    }
}
