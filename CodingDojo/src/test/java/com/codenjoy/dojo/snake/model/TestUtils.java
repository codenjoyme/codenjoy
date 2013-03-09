package com.codenjoy.dojo.snake.model;

import com.codenjoy.dojo.services.Plot;
import com.codenjoy.dojo.snake.services.playerdata.PlotColor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import java.util.List;

import static junit.framework.Assert.assertNotNull;

public class TestUtils {
    public static final int HEIGHT = 20;
    static int WIDTH = 10;

    public static void assertContainsPlot(final int x, final int y, final PlotColor color, List<Plot> plots) {
        Object foundPlot = CollectionUtils.find(plots, new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                Plot plot = (Plot) object;
                return plot.getColor() == color && plot.getX() == x && plot.getY() == y;
            }
        });
        assertNotNull("Plot with coordinates (" + x + "," + y + ") color: " + color + " not found\n" +
                ", but present " + plots.toString(), foundPlot);
    }

    public static void assertContainsPlot(final int x, final int y,  List<Plot> plots) {
        Object foundPlot = CollectionUtils.find(plots, new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                Plot plot = (Plot) object;
                return plot.getX() == x && plot.getY() == y;
            }
        });
        assertNotNull("Plot with coordinates (" + x + "," + y + ") not found\n" +
                ", but present " + plots.toString(), foundPlot);
    }

    public static void assertContainsPlot(final PlotColor color, List<Plot> plots) {
        Object foundPlot = CollectionUtils.find(plots, new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                Plot plot = (Plot) object;
                return plot.getColor() == color;
            }
        });
        assertNotNull("Plot with color : \" + color + \" not found\n" +
                ", but present " + plots.toString(), foundPlot);
    }

}
