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

import com.codenjoy.dojo.lemonade.model.WeatherForecast;
import org.junit.Assert;
import org.junit.Test;

public class BoardTest {

    String input =
            "{" +
                    "    history : [" +
                    "        {" +
                    "            day : 1," +
                    "            lemonadeSold : 2," +
                    "            lemonadePrice : 0.34," +
                    "            income : 0.00," +
                    "            lemonadeMade : 4," +
                    "            signsMade : 3," +
                    "            expenses : 4.3," +
                    "            profit : -0.45," +
                    "            assetsAfter : 2.55" +
                    "        }," +
                    "        {" +
                    "            day : 2," +
                    "            lemonadeSold : 5," +
                    "            lemonadePrice : 0.34," +
                    "            income : 0.60," +
                    "            lemonadeMade : 3," +
                    "            signsMade : 3," +
                    "            expenses : 4.3," +
                    "            profit : -0.45," +
                    "            assetsAfter : 2.55" +
                    "        }" +
                    "    ]," +
                    "    day : 3," +
                    "    lemonadeCost : 0.02," +
                    "    assets : 1.55," +
                    "    weatherForecast : \"SUNNY\"," +
                    "    messages : \"my test message\"," +
                    "    isBankrupt : true" +
                    "}";

    @Test
    public void parseBoard() {
        Board parsedBoard = (Board) new Board().forString(input);
        Assert.assertEquals(3, parsedBoard.getDay());
        Assert.assertEquals(2, parsedBoard.getHistory().size());
        Assert.assertEquals(1, parsedBoard.getHistory().get(0).getDay());
        Assert.assertEquals(4, parsedBoard.getHistory().get(0).getLemonadeMade());
        Assert.assertEquals(3, parsedBoard.getHistory().get(0).getSignsMade());
        Assert.assertEquals(0.34, parsedBoard.getHistory().get(0).getLemonadePrice(), 0.001);
        Assert.assertEquals(2, parsedBoard.getHistory().get(0).getLemonadeSold());
        Assert.assertEquals(4.3, parsedBoard.getHistory().get(0).getExpenses(), 0.001);
        Assert.assertEquals(0.0, parsedBoard.getHistory().get(0).getIncome(), 0.001);
        Assert.assertEquals(-0.45, parsedBoard.getHistory().get(0).getProfit(), 0.001);
        Assert.assertEquals(2.55, parsedBoard.getHistory().get(0).getAssetsAfter(), 0.001);
        Assert.assertEquals(2, parsedBoard.getHistory().get(1).getDay());
        Assert.assertTrue(parsedBoard.isGameOver());
        Assert.assertEquals("my test message", parsedBoard.getMessages());
        Assert.assertEquals(1.55, parsedBoard.getAssets(), 0.001);
        Assert.assertEquals(0.02, parsedBoard.getLemonadeCost(), 0.001);
        Assert.assertEquals(WeatherForecast.SUNNY, parsedBoard.getWeatherForecast());
    }
}
