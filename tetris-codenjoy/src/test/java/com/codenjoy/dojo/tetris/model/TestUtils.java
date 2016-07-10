package com.codenjoy.dojo.tetris.model;

import com.codenjoy.dojo.tetris.model.Plot;
import com.codenjoy.dojo.tetris.model.PlotColor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertNotNull;

public class TestUtils {
    public static final int HEIGHT = 20;
    public static int WIDTH = 10;

    @Test
    public void test(){
        // TODO do nothing
    }

    public static void assertContainsPlot(final int x, final int y, final PlotColor color, List<Plot> plots) {
        Object foundPlot = CollectionUtils.find(plots, new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                Plot plot = (Plot) object;
                return plot.getColor() == color && plot.getX() == x && plot.getY() == y;
            }
        });
        assertNotNull("Plot with coordinates (" + x + "," + y + ") color: " + color + " not found", foundPlot);
    }

    public static void assertContainsPlot(final int x, final int y,  List<Plot> plots) {
        Object foundPlot = CollectionUtils.find(plots, new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                Plot plot = (Plot) object;
                return plot.getX() == x && plot.getY() == y;
            }
        });
        assertNotNull("Plot with coordinates (" + x + "," + y + ") not found", foundPlot);
    }

}
