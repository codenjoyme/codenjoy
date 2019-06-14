package com.codenjoy.dojo.excitebike.services.parse;

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

import com.codenjoy.dojo.excitebike.model.items.*;
import com.codenjoy.dojo.excitebike.model.items.bike.Bike;
import com.codenjoy.dojo.excitebike.model.items.OffRoad;
import com.codenjoy.dojo.excitebike.model.items.bike.BikeType;
import com.codenjoy.dojo.excitebike.model.items.springboard.SpringboardElement;
import com.codenjoy.dojo.excitebike.model.items.springboard.SpringboardElementType;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.printer.CharElements;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MapParserImpl implements MapParser {

    private String map;
    private int xSize;

    public MapParserImpl(String map) {
        this.xSize = (int) Math.sqrt(map.length());
        this.map = map;
    }

    public MapParserImpl(String map, int xSize) {
        this.map = map;
        this.xSize = xSize;
    }

    @Override
    public int getXSize() {
        return xSize;
    }

    @Override
    public int getYSize() {
        return map.length() / xSize;
    }

    @Override
    public List<Bike> getBikes() {
        return parseAndConvertElements(Bike::new, BikeType.BIKE);
    }

    @Override
    public List<Accelerator> getAccelerators() {
        return parseAndConvertElements(Accelerator::new, GameElementType.ACCELERATOR);
    }

    @Override
    public List<Border> getBorders() {
        return parseAndConvertElements(Border::new, GameElementType.BORDER);
    }

    @Override
    public List<Inhibitor> getInhibitors() {
        return parseAndConvertElements(Inhibitor::new, GameElementType.INHIBITOR);
    }

    @Override
    public List<LineChanger> getLineUpChangers() {
        return parseAndConvertElements(point -> new LineChanger(point, true), GameElementType.LINE_CHANGER_UP);
    }

    @Override
    public List<LineChanger> getLineDownChangers() {
        return parseAndConvertElements(point -> new LineChanger(point, false), GameElementType.LINE_CHANGER_DOWN);
    }

    @Override
    public List<Obstacle> getObstacles() {
        return parseAndConvertElements(Obstacle::new, GameElementType.OBSTACLE);
    }

    @Override
    public List<OffRoad> getOffRoads() {
        return parseAndConvertElements(OffRoad::new, GameElementType.OFF_ROAD);
    }

    @Override
    public List<SpringboardElement> getSpringboardDarkElements() {
        return parseAndConvertElements(point -> new SpringboardElement(point, SpringboardElementType.DARK), SpringboardElementType.DARK);
    }

    @Override
    public List<SpringboardElement> getSpringboardLightElements() {
        return parseAndConvertElements(point -> new SpringboardElement(point, SpringboardElementType.LIGHT), SpringboardElementType.LIGHT);
    }

    @Override
    public List<SpringboardElement> getSpringboardLeftDownElements() {
        return parseAndConvertElements(point -> new SpringboardElement(point, SpringboardElementType.LEFT_DOWN), SpringboardElementType.LEFT_DOWN);
    }

    @Override
    public List<SpringboardElement> getSpringboardLeftUpElements() {
        return parseAndConvertElements(point -> new SpringboardElement(point, SpringboardElementType.LEFT_UP), SpringboardElementType.LEFT_UP);
    }

    @Override
    public List<SpringboardElement> getSpringboardRightDownElements() {
        return parseAndConvertElements(point -> new SpringboardElement(point, SpringboardElementType.RIGHT_DOWN), SpringboardElementType.RIGHT_DOWN);
    }

    @Override
    public List<SpringboardElement> getSpringboardRightUpElements() {
        return parseAndConvertElements(point -> new SpringboardElement(point, SpringboardElementType.RIGHT_UP), SpringboardElementType.RIGHT_UP);
    }

    private <T> List<T> parseAndConvertElements(Function<Point, T> elementConstructor, CharElements elementType) {
        return IntStream.range(0, map.length())
                .filter(index -> map.charAt(index) == elementType.ch())
                .mapToObj(this::convertToPoint)
                .map(elementConstructor)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    private Point convertToPoint(int position) {
        return position == -1
                ? null
                : PointImpl.pt(position % xSize, (this.map.length() - position - 1) / xSize);
    }
}
