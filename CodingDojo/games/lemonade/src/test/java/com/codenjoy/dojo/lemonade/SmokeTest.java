package com.codenjoy.dojo.lemonade;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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


import com.codenjoy.dojo.client.local.LocalGameRunner;
import com.codenjoy.dojo.lemonade.client.Board;
import com.codenjoy.dojo.lemonade.client.ai.AISolver;
import com.codenjoy.dojo.lemonade.services.GameRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import org.junit.Test;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SmokeTest {

    private Dice dice;

    @Test
    public void test() {
        // given
        List<String> messages = new LinkedList<>();

        LocalGameRunner.timeout = 0;
        LocalGameRunner.out = (e) -> messages.add(e);
        LocalGameRunner.countIterations = 10;
        LocalGameRunner.printScores = false;

        Dice dice = LocalGameRunner.getDice( // "random numbers"
                1);

        GameRunner gameType = new GameRunner() {
            @Override
            public Dice getDice() {
                return dice;
            }

            @Override
            public SettingsImpl createSettings(){
                SettingsImpl settings = new SettingsImpl();
                settings.addEditBox("Limit days").type(Integer.class).def(30).update(0);
                return settings;
            }
        };

        // when
        LocalGameRunner.run(gameType,
                new AISolver(dice),
                new Board());

        // construct expected result
        final int dataSize = 6;
        final String heatWave = "A HEAT WAVE IS PREDICTED FOR TODAY!";
        final String[] data = new String[]{
                // assets before, move, expenses, profit, weather forecast, extra messages
                "2.00", "31,4,8", "1.22", "1.26", "SUNNY", null,
                "3.26", "50,7,8", "2.05", "1.95", "SUNNY", null,
                "5.21", "40,5,8", "2.35", "0.85", "SUNNY", null,
                "6.06", "47,6,8", "2.78", "0.98", "SUNNY", "(YOUR MOTHER QUIT GIVING YOU FREE SUGAR)\\n",
                "7.04", "80,11,10", "4.85", "3.15", "HOT AND DRY", null,
                "10.19", "115,16,10", "7.00", "4.50", "HOT AND DRY", heatWave,
                "14.69", "133,19,10", "9.50", "2.40", "HOT AND DRY", heatWave,
                "17.09", "106,15,8", "7.55", "-2.03", "SUNNY", "(THE PRICE OF LEMONADE MIX JUST WENT UP)\\n" + heatWave,
                "15.06", "94,13,8", "6.65", "-1.13", "SUNNY", null,
                "13.93", "87,12,8", "0.20", "-0.63", "SUNNY", null,
                "13.30", null, null, null, null, null, null
        };
        StringBuilder expected = new StringBuilder();
        List<String> history = new LinkedList<>();
        double lemonadePricePrev = 0.0;
        double incomePrev = 0.0;
        int lemonadeMadePrev = 0;
        int signsMadePrev = 0;
        String expensesStrPrev = null;
        String profitStrPrev = null;
        int lemonadeSoldPrev = 0;
        expected.append("DICE:1\n");
        for (int day = 0; day < 10; day++) {
            if (day > 0)
                expected.append("\n");
            String moveStr = data[day * dataSize + 1];
            String expensesStr = data[day * dataSize + 2];
            String profitStr = data[day * dataSize + 3];
            String[] moveStrSplit = moveStr.split(",");
            int lemonadeMade = Integer.parseInt(moveStrSplit[0]);
            int signsMade = Integer.parseInt(moveStrSplit[1]);
            double lemonadePrice = Integer.parseInt(moveStrSplit[2]) * .01;
            double income = Double.valueOf(expensesStr) + Double.valueOf(profitStr);
            int lemonadeSold = (int) (income / lemonadePrice + .5);

            expected.append("1:BoardData {\n");
            String shistory = (history.size() == 0)
                    ? "[]"
                    : "[\n" + String.join(",\n", history) + "\n1:  ]";
            String lemonadeCost = (day < 2) ? "0.02" : ((day < 6) ? "0.04" : "0.05");
            expected.append("1:  'assets':" + jsonStringForDouble(data[day * dataSize]) + ",\n" +
                    "1:  'day':" + (day + 1) + ",\n" +
                    "1:  'history':" + shistory + ",\n" +
                    "1:  'isBankrupt':false,\n" +
                    "1:  'isGameOver':false,\n" +
                    "1:  'lemonadeCost':" + jsonStringForDouble(lemonadeCost) + ",\n" +
                    "1:  'messages':'");
            if (day == 0)
                expected.append("HI! WELCOME TO LEMONSVILLE, CALIFORNIA!\\n\\nIN THIS SMALL TOWN, YOU ARE IN CHARGE OF RUNNING YOUR OWN LEMONADE STAND.\\nHOW MUCH PROFIT YOU MAKE IS UP TO YOU.\\nIF YOU MAKE THE MOST MONEY, YOU'RE THE WINNER!!\\n\\nTO MANAGE YOUR LEMONADE STAND, YOU WILL NEED TO MAKE THESE DECISIONS EVERY DAY:\\n1. HOW MANY GLASSES OF LEMONADE TO MAKE (ONLY ONE BATCH IS MADE EACH MORNING)\\n2. HOW MANY ADVERTISING SIGNS TO MAKE (THE SIGNS COST FIFTEEN CENTS EACH)\\n3. WHAT PRICE TO CHARGE FOR EACH GLASS\\n\\nYOU WILL BEGIN WITH $2.00 CASH (ASSETS). BECAUSE YOUR MOTHER GAVE YOU SOME SUGAR,\\nYOUR COST TO MAKE LEMONADE IS $0.02 (TWO CENTS A GLASS, THIS MAY CHANGE IN THE FUTURE).\\n\\nYOUR EXPENSES ARE THE SUM OF THE COST OF THE LEMONADE AND THE COST OF THE SIGNS.\\nYOUR PROFITS ARE THE DIFFERENCE BETWEEN THE INCOME FROM SALES AND YOUR EXPENSES.\\nTHE NUMBER OF GLASSES YOU SELL EACH DAY DEPENDS ON THE PRICE YOU CHARGE, AND ON\\nTHE NUMBER OF ADVERTISING SIGNS YOU USE.\\nKEEP TRACK OF YOUR ASSETS, BECAUSE YOU CAN'T SPEND MORE MONEY THAN YOU HAVE!\\n");
            else {
                expected.append("** LEMONSVILLE DAILY FINANCIAL REPORT, DAY " + day + " **\\n" +
                        "GLASSES SOLD: " + lemonadeSoldPrev + ", PRICE " + formatCurrency(lemonadePricePrev) + " PER GLASS\\n" +
                        "INCOME:   " + formatCurrency(incomePrev) + "\\n" +
                        "GLASSES MADE: " + lemonadeMadePrev + ", SIGNS MADE: " + signsMadePrev + "\\n" +
                        "EXPENSES: $" + expensesStrPrev + "\\nPROFIT:   $" + profitStrPrev + "\\n" +
                        "ASSETS:   $" + data[day * dataSize] + "\\n");
            }
            expected.append("\\nYOUR ASSETS: $" + data[day * dataSize] + "\\n" +
                    "LEMONSVILLE WEATHER REPORT:  " + data[day * dataSize + 4] + "\\n" +
                    "ON DAY " + (day + 1) + ", THE COST OF LEMONADE IS $" + lemonadeCost + "\\n");
            String extraMessage = data[(day + 1) * dataSize + 5];
            if (extraMessage != null)
                expected.append(extraMessage);
            expected.append("',\n");
            expected.append("1:  'weatherForecast':'" + data[day * dataSize + 4].replace(' ', '_') + "'\n");
            expected.append("1:}\n");
            expected.append("1:Answer: message('go " + moveStr + "')\n");
            expected.append("1:Fire Event: WIN (" + profitStr + ", " + data[(day + 1) * dataSize] + ")\n");
            expected.append("------------------------------------------");

            history.add("1:    {\n" +
                    "1:      'assetsAfter':" + jsonStringForDouble(data[(day + 1) * dataSize]) + ",\n" +
                    "1:      'assetsBefore':" + jsonStringForDouble(data[day * dataSize]) + ",\n" +
                    "1:      'day':" + (day + 1) + ",\n" +
                    "1:      'expenses':" + jsonStringForDouble(expensesStr) + ",\n" +
                    "1:      'income':" + jsonStringForDouble(income) + ",\n" +
                    "1:      'lemonadeMade':" + lemonadeMade + ",\n" +
                    "1:      'lemonadePrice':" + jsonStringForDouble(lemonadePrice) + ",\n" +
                    "1:      'lemonadeSold':" + lemonadeSold + ",\n" +
                    "1:      'profit':" + jsonStringForDouble(profitStr) + ",\n" +
                    "1:      'signsMade':" + signsMade + "\n" +
                    "1:    }");

            lemonadeMadePrev = lemonadeMade;
            signsMadePrev = signsMade;
            lemonadePricePrev = lemonadePrice;
            lemonadeSoldPrev = lemonadeSold;
            incomePrev = income;
            expensesStrPrev = expensesStr;
            profitStrPrev = profitStr;
        }

        // then
        assertEquals(expected.toString(), String.join("\n", messages));
    }

    static String jsonStringForDouble(String value) {
        Double d = Double.valueOf(value);
        return jsonStringForDouble(d);
    }

    // Formats a double value the same way as JSONObject.numberToString() do
    static String jsonStringForDouble(Double value) {
        String string = new Double(Math.round(value * 100) / 100.0).toString();
        if (string.indexOf(46) > 0 && string.indexOf(101) < 0 && string.indexOf(69) < 0) {
            while (string.endsWith("0")) {
                string = string.substring(0, string.length() - 1);
            }

            if (string.endsWith(".")) {
                string = string.substring(0, string.length() - 1);
            }
        }

        return string;
    }

    private static DecimalFormat formatter = new DecimalFormat("0.00");

    private static String formatCurrency(double value) {
        return "$" + formatter.format(value);
    }
}
