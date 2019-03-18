package com.codenjoy.dojo.lemonade.client;

import org.junit.Assert;
import org.junit.Test;

public class BoardTest {

    String input = "{\n" +
            "    history : [\n" +
            "        {\n" +
            "            day : \"1\",\n" +
            "            glassesSold : \"2\",\n" +
            "            glassPrice : \"0.34\",\n" +
            "            income : \"0.00\",\n" +
            "            glassesMade : \"3\",\n" +
            "            signsMade : \"3\",\n" +
            "            expenses : \"4.3\",\n" +
            "            profit : \"-0.45\",\n" +
            "            assets : \"2.55\"\n" +
            "        },\n" +
            "        {\n" +
            "            day : \"2\",\n" +
            "            glassesSold : \"5\",\n" +
            "            glassPrice : \"0.34\",\n" +
            "            income : \"0.60\",\n" +
            "            glassesMade : \"3\",\n" +
            "            signsMade : \"3\",\n" +
            "            expenses : \"4.3\",\n" +
            "            profit : \"-0.45\",\n" +
            "            assets : \"2.55\"\n" +
            "        }\n" +
            "    ],\n" +
            "    inputData: {\n" +
            "        day : \"2\",\n" +
            "        costOfLemonade : \"0.02\",\n" +
            "        lemonadeStand : \"1\",\n" +
            "        assets : \"1.55\",\n" +
            "        signCost : \"0.15\",\n" +
            "        forecast : \"SUNNY\"\n" +
            "    }\n" +
            "}";

    @Test
    public void parseBoard(){
        Board parsedBoard = (Board)new Board().forString(input);
        Assert.assertEquals(2, parsedBoard.getHistory().size());
        Assert.assertNotNull(parsedBoard.getInputData());
    }
}
