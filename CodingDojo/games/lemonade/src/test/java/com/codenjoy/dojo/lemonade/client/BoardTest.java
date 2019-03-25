package com.codenjoy.dojo.lemonade.client;

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
            "    weatherForecast : SUNNY" +
            "}";

    @Test
    public void parseBoard(){
        Board parsedBoard = (Board)new Board().forString(input);
        Assert.assertEquals(2, parsedBoard.getHistory().size());
        Assert.assertNotNull(parsedBoard.getInputData());
    }
}
