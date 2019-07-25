package com.codenjoy.dojo.excitebike;

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

import com.codenjoy.dojo.excitebike.model.GameField;
import com.codenjoy.dojo.excitebike.model.Player;
import com.codenjoy.dojo.excitebike.model.items.Bike;
import com.codenjoy.dojo.excitebike.model.elements.BikeType;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.printer.CharElements;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.Mockito.mock;

/**
 * Created by Pavel Bobylev 6/26/2019
 */
public class TestUtils {

    public static List<Bike> parseBikes(String map) {
        return parseAndConvertElements(map, Bike::new, Arrays.stream(BikeType.values())
                .filter(e -> !e.name().contains(Bike.OTHER_BIKE_PREFIX))
                .toArray(BikeType[]::new));
    }

    private static <T> List<T> parseAndConvertElements(String map, Function<Point, T> elementConstructor, CharElements... elements) {
        return IntStream.range(0, map.length())
                .filter(index -> Arrays.stream(elements).anyMatch(e -> map.charAt(index) == e.ch()))
                .mapToObj(i -> convertToPoint(map, i))
                .map(elementConstructor)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    private static Point convertToPoint(String map, int position) {
        int xSize = (int) Math.sqrt(map.length());
        return position == -1
                ? null
                : PointImpl.pt(position % xSize, (map.length() - position - 1) / xSize);
    }

    public static String printField(GameField gameField, Player player) {
        return (String) new PrinterFactoryImpl().getPrinter(gameField.reader(), player).print();
    }

    public static Player getPlayer(Bike bike) {
        Player player = new Player(mock(EventListener.class));
        player.setHero(bike);
        return player;
    }
}
