package com.codenjoy.dojo.tetris.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;

public class TestUtils {
    public static final int HEIGHT = 20;
    public static int WIDTH = 10;

    @Test
    public void test(){
        // TODO do nothing
    }

    public static void assertContainsPlot(final int x, final int y, final Elements color, List<Plot> plots) {
        Object foundPlot = CollectionUtils.find(plots, (Predicate) object -> {
            Plot plot = (Plot) object;
            return plot.getColor() == color && plot.getX() == x && plot.getY() == y;
        });
        assertNotNull("Plot with coordinates (" + x + "," + y + ") color: " + color + " not found", foundPlot);
    }

    public static void assertContainsPlot(final int x, final int y,  List<Plot> plots) {
        Object foundPlot = CollectionUtils.find(plots, (Predicate) object -> {
            Plot plot = (Plot) object;
            return plot.getX() == x && plot.getY() == y;
        });
        assertNotNull("Plot with coordinates (" + x + "," + y + ") not found", foundPlot);
    }

}
