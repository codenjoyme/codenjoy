package com.globallogic.snake.services.playerdata;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * User: oleksandr.baglai
 * Date: 10/1/12
 * Time: 4:00 AM
 */
public class PlayerDataTest {

    @Test
    public void shouldCollectData() {
        List<Plot> plots = new LinkedList<Plot>();
        plots.add(new Plot(0, 0, PlotColor.APPLE));
        plots.add(new Plot(1, 1, PlotColor.BODY));
        plots.add(new Plot(2, 2, PlotColor.EMPTY));
        plots.add(new Plot(3, 3, PlotColor.HEAD));
        plots.add(new Plot(4, 4, PlotColor.STONE));
        plots.add(new Plot(5, 5, PlotColor.TAIL));
        plots.add(new Plot(6, 6, PlotColor.WALL));

        PlayerData data = new PlayerData(plots, 10, 1);

        assertEquals("PlayerData[" +
                "Plots:[" +
                    "Plot{x=0, y=0, color=APPLE}, " +
                    "Plot{x=1, y=1, color=BODY}, " +
                    "Plot{x=2, y=2, color=EMPTY}, " +
                    "Plot{x=3, y=3, color=HEAD}, " +
                    "Plot{x=4, y=4, color=STONE}, " +
                    "Plot{x=5, y=5, color=TAIL}, " +
                    "Plot{x=6, y=6, color=WALL}], " +
                "Score:10, " +
                "CurrentLevel:1]", data.toString());
    }
}
