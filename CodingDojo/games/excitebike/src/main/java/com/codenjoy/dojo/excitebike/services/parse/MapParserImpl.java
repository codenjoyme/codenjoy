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

import com.codenjoy.dojo.excitebike.model.items.Accelerator;
import com.codenjoy.dojo.excitebike.model.items.Fence;
import com.codenjoy.dojo.excitebike.model.elements.GameElementType;
import com.codenjoy.dojo.excitebike.model.items.Inhibitor;
import com.codenjoy.dojo.excitebike.model.items.LineChanger;
import com.codenjoy.dojo.excitebike.model.items.Obstacle;
import com.codenjoy.dojo.excitebike.model.items.SpringboardElement;
import com.codenjoy.dojo.excitebike.model.elements.SpringboardElementType;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.printer.CharElements;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MapParserImpl implements MapParser {

    private String map;
    private int xSize;

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
    public List<Accelerator> getAccelerators() {
        return parseAndConvertElements(Accelerator::new, GameElementType.ACCELERATOR);
    }

    @Override
    public List<Fence> getFences() {
        return parseAndConvertElements(Fence::new, GameElementType.FENCE);
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
    public List<SpringboardElement> getSpringboardDarkElements() {
        return parseAndConvertElements(point -> new SpringboardElement(point, SpringboardElementType.SPRINGBOARD_LEFT), SpringboardElementType.SPRINGBOARD_LEFT);
    }

    @Override
    public List<SpringboardElement> getSpringboardLightElements() {
        return parseAndConvertElements(point -> new SpringboardElement(point, SpringboardElementType.SPRINGBOARD_RIGHT), SpringboardElementType.SPRINGBOARD_RIGHT);
    }

    @Override
    public List<SpringboardElement> getSpringboardLeftDownElements() {
        return parseAndConvertElements(point -> new SpringboardElement(point, SpringboardElementType.SPRINGBOARD_LEFT_DOWN), SpringboardElementType.SPRINGBOARD_LEFT_DOWN);
    }

    @Override
    public List<SpringboardElement> getSpringboardLeftUpElements() {
        return parseAndConvertElements(point -> new SpringboardElement(point, SpringboardElementType.SPRINGBOARD_LEFT_UP), SpringboardElementType.SPRINGBOARD_LEFT_UP);
    }

    @Override
    public List<SpringboardElement> getSpringboardRightDownElements() {
        return parseAndConvertElements(point -> new SpringboardElement(point, SpringboardElementType.SPRINGBOARD_RIGHT_DOWN), SpringboardElementType.SPRINGBOARD_RIGHT_DOWN);
    }

    @Override
    public List<SpringboardElement> getSpringboardRightUpElements() {
        return parseAndConvertElements(point -> new SpringboardElement(point, SpringboardElementType.SPRINGBOARD_RIGHT_UP), SpringboardElementType.SPRINGBOARD_RIGHT_UP);
    }

    @Override
    public List<SpringboardElement> getSpringboardNoneElements() {
        return parseAndConvertElements(point -> new SpringboardElement(point, SpringboardElementType.SPRINGBOARD_TOP), SpringboardElementType.SPRINGBOARD_TOP);
    }

    private <T> List<T> parseAndConvertElements(Function<Point, T> elementConstructor, CharElements... elements) {
        return IntStream.range(0, map.length())
                .filter(index -> Arrays.stream(elements).anyMatch(e -> map.charAt(index) == e.ch()))
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
