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
import com.codenjoy.dojo.excitebike.model.items.Border;
import com.codenjoy.dojo.excitebike.model.items.Elements;
import com.codenjoy.dojo.excitebike.model.items.Hero;
import com.codenjoy.dojo.excitebike.model.items.Inhibitor;
import com.codenjoy.dojo.excitebike.model.items.LineChanger;
import com.codenjoy.dojo.excitebike.model.items.Obstacle;
import com.codenjoy.dojo.excitebike.model.items.RoadElement;
import com.codenjoy.dojo.excitebike.model.items.springboard.SpringboardElement;
import com.codenjoy.dojo.excitebike.model.items.springboard.SpringboardType;
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
    public List<Hero> getHeroes() {
        //TODO adjust after Bike class realization
        return parseAndConvertElements(Hero::new, null);
    }

    @Override
    public List<Accelerator> getAccelerators() {
        return parseAndConvertElements(Accelerator::new, Elements.ACCELERATOR);
    }

    @Override
    public List<Border> getBorders() {
        return parseAndConvertElements(Border::new, Elements.BORDER);
    }

    @Override
    public List<Inhibitor> getInhibitors() {
        return parseAndConvertElements(Inhibitor::new, Elements.INHIBITOR);
    }

    @Override
    public List<LineChanger> getLineUpChangers() {
        return parseAndConvertElements(point -> new LineChanger(point, true), Elements.LINE_CHANGER_UP);
    }

    @Override
    public List<LineChanger> getLineDownChangers() {
        return parseAndConvertElements(point -> new LineChanger(point, false), Elements.LINE_CHANGER_DOWN);
    }

    @Override
    public List<Obstacle> getObstacles() {
        return parseAndConvertElements(Obstacle::new, Elements.OBSTACLE);
    }

    @Override
    public List<RoadElement> getRoadElements() {
        return parseAndConvertElements(RoadElement::new, Elements.ROAD);
    }

    @Override
    public List<SpringboardElement> getSpringboardDarkElements() {
        return parseAndConvertElements(point -> new SpringboardElement(point, SpringboardType.DARK), SpringboardType.DARK);
    }

    @Override
    public List<SpringboardElement> getSpringboardLightElements() {
        return parseAndConvertElements(point -> new SpringboardElement(point, SpringboardType.LIGHT), SpringboardType.LIGHT);
    }

    @Override
    public List<SpringboardElement> getSpringboardLeftDownElements() {
        return parseAndConvertElements(point -> new SpringboardElement(point, SpringboardType.LEFT_DOWN), SpringboardType.LEFT_DOWN);
    }

    @Override
    public List<SpringboardElement> getSpringboardLeftUpElements() {
        return parseAndConvertElements(point -> new SpringboardElement(point, SpringboardType.LEFT_UP), SpringboardType.LEFT_UP);
    }

    @Override
    public List<SpringboardElement> getSpringboardRightDownElements() {
        return parseAndConvertElements(point -> new SpringboardElement(point, SpringboardType.RIGHT_DOWN), SpringboardType.RIGHT_DOWN);
    }

    @Override
    public List<SpringboardElement> getSpringboardRightUpElements() {
        return parseAndConvertElements(point -> new SpringboardElement(point, SpringboardType.RIGHT_UP), SpringboardType.RIGHT_UP);
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
