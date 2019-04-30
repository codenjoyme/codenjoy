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


import com.codenjoy.dojo.client.LocalGameRunner;
import com.codenjoy.dojo.lemonade.client.Board;
import com.codenjoy.dojo.lemonade.client.ai.AISolver;
import com.codenjoy.dojo.lemonade.services.GameRunner;
import com.codenjoy.dojo.services.Dice;
import org.junit.Test;

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

        Dice dice = LocalGameRunner.getDice(
                1, 1, 2, 3, 4, // random numbers
                0, 2, 2, 3, 1,
                0, 1, 4, 1, 2,
                1, 3, 2, 4, 1,
                1, 3, 0, 0, 1,
                0, 0, 1, 0, 2,
                1, 3, 2, 4, 1,
                0, 1, 1, 3, 3,
                0, 2, 0, 0, 2,
                0, 1, 2, 2, 1,
                1, 3, 4, 3, 4,
                1, 3, 2, 4, 1,
                0, 1, 3, 1, 1);

        GameRunner gameType = new GameRunner() {
            @Override
            public Dice getDice() {
                return dice;
            }
        };

        // when
        LocalGameRunner.run(gameType,
                new AISolver(dice),
                new Board());

        // construct expected result
        final int dataSize = 6;
        final String[] data = new String[]{
                // assets, expenses, profit, weather forecast, extra messages, event
                "2.00", "0.17", "-0.16", "UNKNOWN", null, "WIN (-16)",
                "1.84", "0.17", "-0.16", "CLOUDY", null, "WIN (-16)",
                "1.68", "0.17", "-0.16", "SUNNY", null, "WIN (-16)",
                "1.52", "0.17", "-0.16", "SUNNY", null, "WIN (-16)",
                "1.34", "0.19", "-0.18", "SUNNY", "(YOUR MOTHER QUIT GIVING YOU FREE SUGAR)\\n", "WIN (-18)",
                "1.16", "0.19", "-0.18", "HOT AND DRY", null, "WIN (-18)",
                "0.98", "0.19", "-0.18", "HOT AND DRY", "A HEAT WAVE IS PREDICTED FOR TODAY!", "WIN (-18)",
                "0.80", "0.19", "-0.18", "HOT AND DRY", "A HEAT WAVE IS PREDICTED FOR TODAY!", "WIN (-18)",
                "0.61", "0.20", "-0.19", "SUNNY", "(THE PRICE OF LEMONADE MIX JUST WENT UP)\\nA HEAT WAVE IS PREDICTED FOR TODAY!", "WIN (-19)",
                "0.42", "0.20", "-0.19", "SUNNY", null, "WIN (-19)",
                "0.40", "0.20", "-0.19", "", null, "WIN (-19)"
        };
        StringBuilder expected = new StringBuilder();
        List<String> history = new LinkedList<>();
        expected.append("DICE:1\n");
        for (int day = 0; day < 10; day++) {
            if (day > 0)
                expected.append("\n");

            expected.append("1:BoardData {\n");
            String shistory = (history.size() == 0)
                    ? "[]"
                    : "[\n" + String.join(",\n", history) + "\n1:  ]";
            expected.append("1:  'assets':" + jsonStringForDouble(data[day * dataSize]) + ",\n" +
                    "1:  'day':" + day + ",\n" +
                    "1:  'history':" + shistory + ",\n" +
                    "1:  'isBankrupt':false,\n" +
                    "1:  'lemonadePrice':" + (day == 0 ? "0" : "0.01") + ",\n" +
                    "1:  'messages':'\\n");
            if (day == 0)
                expected.append("HI! WELCOME TO LEMONSVILLE, CALIFORNIA!\\n\\nIN THIS SMALL TOWN, YOU ARE IN CHARGE OF RUNNING YOUR OWN LEMONADE STAND.\\nHOW MUCH PROFIT YOU MAKE IS UP TO YOU.\\nIF YOU MAKE THE MOST MONEY, YOU'RE THE WINNER!!\\n\\nTO MANAGE YOUR LEMONADE STAND, YOU WILL NEED TO MAKE THESE DECISIONS EVERY DAY:\\n1. HOW MANY GLASSES OF LEMONADE TO MAKE (ONLY ONE BATCH IS MADE EACH MORNING)\\n2. HOW MANY ADVERTISING SIGNS TO MAKE (THE SIGNS COST FIFTEEN CENTS EACH)\\n3. WHAT PRICE TO CHARGE FOR EACH GLASS\\n\\nYOU WILL BEGIN WITH $2.00 CASH (ASSETS).BECAUSE YOUR MOTHER GAVE YOU SOME SUGAR,\\nYOUR COST TO MAKE LEMONADE IS $0.02 (TWO CENTS A GLASS, THIS MAY CHANGE IN THE FUTURE).\\n\\nYOUR EXPENSES ARE THE SUM OF THE COST OF THE LEMONADE AND THE COST OF THE SIGNS.\\nYOUR PROFITS ARE THE DIFFERENCE BETWEEN THE INCOME FROM SALES AND YOUR EXPENSES.\\nTHE NUMBER OF GLASSES YOU SELL EACH DAY DEPENDS ON THE PRICE YOU CHARGE, AND ON\\nTHE NUMBER OF ADVERTISING SIGNS YOU USE.\\nKEEP TRACK OF YOUR ASSETS, BECAUSE YOU CAN'T SPEND MORE MONEY THAN YOU HAVE!\\n\\n");
            else {
                String lemonadeCost = (day <= 3) ? "0.02" : ((day <= 7) ? "0.04" : "0.05");
                expected.append("** LEMONSVILLE DAILY FINANCIAL REPORT, DAY " + (day - 1) + " **\\n" +
                        "GLASSES SOLD: 1, PRICE $0.01 PER GLASS\\n" +
                        "INCOME:   $0.01\\nGLASSES MADE: 1, SIGNS MADE: 1\\n" +
                        "EXPENSES: $" + data[day * dataSize + 1] + "\\nPROFIT:   $" + data[day * dataSize + 2] + "\\n" +
                        "ASSETS:   $" + data[day * dataSize] + "\\n\\n" +
                        "YOUR ASSETS: $" + data[day * dataSize] + "\\n" +
                        "LEMONSVILLE WEATHER REPORT:  " + data[day * dataSize + 3] + "\\n" +
                        "ON DAY " + day + ", THE COST OF LEMONADE IS $" + lemonadeCost + "\\n");
                String extraMessage = data[(day + 1) * dataSize + 4];
                if (extraMessage != null)
                    expected.append(extraMessage);
            }
            expected.append("',\n");
            expected.append("1:  'weatherForecast':'" + data[day * dataSize + 3].replace(' ', '_') + "'\n");
            expected.append("1:}\n");
            expected.append("1:Answer: message('go 1,1,1')\n");
            expected.append("Fire Event: " + data[(day + 1) * dataSize + 5] + "\n");
            expected.append("------------------------------------------");

            history.add("1:    {\n" +
                    "1:      'assets':" + jsonStringForDouble(data[(day + 1) * dataSize]) + ",\n" +
                    "1:      'day':" + (day + 1) + ",\n" +
                    "1:      'expenses':" + jsonStringForDouble(data[(day + 1) * dataSize + 1]) + ",\n" +
                    "1:      'income':0.01,\n" +
                    "1:      'lemonadeMade':1,\n" +
                    "1:      'lemonadePrice':0.01,\n" +
                    "1:      'lemonadeSold':1,\n" +
                    "1:      'profit':" + jsonStringForDouble(data[(day + 1) * dataSize + 2]) + ",\n" +
                    "1:      'signsMade':1\n" +
                    "1:    }");
        }

        // then
        assertEquals(expected.toString(), String.join("\n", messages));
    }

    // Formats a double value the same way as JSONObject.numberToString() do
    static String jsonStringForDouble(String value) {
        Double d = Double.valueOf(value);

        String string = d.toString();
        if (string.indexOf(46) > 0 && string.indexOf(101) < 0 && string.indexOf(69) < 0) {
            while(string.endsWith("0")) {
                string = string.substring(0, string.length() - 1);
            }

            if (string.endsWith(".")) {
                string = string.substring(0, string.length() - 1);
            }
        }

        return string;
    }
}
