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
                    "            lemonadeMade : 3," +
                    "            signsMade : 3," +
                    "            expenses : 4.3," +
                    "            profit : -0.45," +
                    "            assets : 2.55" +
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
                    "            assets : 2.55" +
                    "        }" +
                    "    ]," +
                    "    day : 2," +
                    "    lemonadePrice : 0.02," +
                    "    assets : 1.55," +
                    "    weatherForecast : SUNNY," +
                    "    messages : \"my test message\"," +
                    "    isBankrupt : true" +
                    "}";

    @Test
    public void parseBoard() {
        Board parsedBoard = (Board) new Board().forString(input);
        Assert.assertEquals(2, parsedBoard.getHistory().size());
        Assert.assertNotNull(parsedBoard.getMessages());
        Assert.assertTrue(parsedBoard.isGameOver());
    }
}
