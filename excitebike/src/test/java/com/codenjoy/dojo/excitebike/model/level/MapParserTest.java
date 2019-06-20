package com.codenjoy.dojo.excitebike.model.level;

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

import com.codenjoy.dojo.excitebike.model.items.GameElementType;
import com.codenjoy.dojo.excitebike.model.items.springboard.SpringboardElementType;
import com.codenjoy.dojo.excitebike.services.parse.MapParserImpl;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.printer.CharElements;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

/**
 * Created by Pavel Bobylev 6/4/2019
 */
@RunWith(Parameterized.class)
public class MapParserTest {

    private CharElements element;

    public MapParserTest(CharElements element) {
        this.element = element;
    }

    @Parameterized.Parameters(name = "''{0}''")
    public static Collection data() {
        return Lists.newArrayList(
                GameElementType.BORDER,
                GameElementType.ACCELERATOR,
                GameElementType.INHIBITOR,
                GameElementType.OBSTACLE,
                GameElementType.LINE_CHANGER_UP,
                GameElementType.LINE_CHANGER_DOWN,
                SpringboardElementType.SPRINGBOARD_DARK,
                SpringboardElementType.SPRINGBOARD_LIGHT,
                SpringboardElementType.SPRINGBOARD_LEFT_DOWN,
                SpringboardElementType.SPRINGBOARD_LEFT_UP,
                SpringboardElementType.SPRINGBOARD_RIGHT_DOWN,
                SpringboardElementType.SPRINGBOARD_RIGHT_UP
        );
    }

    @Test
    public void getPointImplMethods__shouldReturnAllAcceleratorsWithCorrectCoordinates__ifGivenMapIsSquareWithDifferentObjects() {
        //given
        String map = "     " +
                "   " + element.ch() + " " +
                "  " + element.ch() + element.ch() + " " +
                "     " +
                element.ch() + "    ";
        int fieldHeight = 5;
        MapParserImpl mapParser = new MapParserImpl(map, fieldHeight);

        //when
        List<PointImpl> result = callTestMethod(mapParser);

        //then
        assertThat(result, hasSize(4));

        assertThat(result.get(0).getX(), is(3));
        assertThat(result.get(0).getY(), is(3));

        assertThat(result.get(1).getX(), is(2));
        assertThat(result.get(1).getY(), is(2));

        assertThat(result.get(2).getX(), is(3));
        assertThat(result.get(2).getY(), is(2));

        assertThat(result.get(3).getX(), is(0));
        assertThat(result.get(3).getY(), is(0));
    }

    @Test
    public void getPointImplMethods__shouldReturnAllAcceleratorsWithCorrectCoordinates__ifGivenMapIsSquareWithAcceleratorsOnly() {
        //given
        String map = "" + element.ch() + element.ch() + element.ch() +
                element.ch() + element.ch() + element.ch() +
                element.ch() + element.ch() + element.ch();
        int fieldHeight = 3;
        MapParserImpl mapParser = new MapParserImpl(map, fieldHeight);

        //when
        List<PointImpl> result = callTestMethod(mapParser);

        //then
        assertThat(result, hasSize(9));

        assertThat(result.get(0).getX(), is(0));
        assertThat(result.get(0).getY(), is(2));

        assertThat(result.get(1).getX(), is(1));
        assertThat(result.get(1).getY(), is(2));

        assertThat(result.get(2).getX(), is(2));
        assertThat(result.get(2).getY(), is(2));

        assertThat(result.get(3).getX(), is(0));
        assertThat(result.get(3).getY(), is(1));

        assertThat(result.get(4).getX(), is(1));
        assertThat(result.get(4).getY(), is(1));

        assertThat(result.get(5).getX(), is(2));
        assertThat(result.get(5).getY(), is(1));

        assertThat(result.get(6).getX(), is(0));
        assertThat(result.get(6).getY(), is(0));

        assertThat(result.get(7).getX(), is(1));
        assertThat(result.get(7).getY(), is(0));

        assertThat(result.get(8).getX(), is(2));
        assertThat(result.get(8).getY(), is(0));
    }

    @Test
    public void getPointImplMethods__shouldReturnAllAcceleratorsWithCorrectCoordinates__ifGivenMapIsRectangleWithDifferentObjects() {
        //given
        String map = "     " +
                "   " + element.ch() + " " +
                "  " + element.ch() + element.ch() + " ";
        int fieldHeight = 5;
        MapParserImpl mapParser = new MapParserImpl(map, fieldHeight);

        //when
        List<PointImpl> result = callTestMethod(mapParser);

        //then
        assertThat(result, hasSize(3));

        assertThat(result.get(0).getX(), is(3));
        assertThat(result.get(0).getY(), is(1));

        assertThat(result.get(1).getX(), is(2));
        assertThat(result.get(1).getY(), is(0));

        assertThat(result.get(2).getX(), is(3));
        assertThat(result.get(2).getY(), is(0));
    }

    @Test
    public void getPointImplMethods__shouldReturnAllAcceleratorsWithCorrectCoordinates__ifGivenMapIsRectangleWithAcceleratorsOnly() {
        //given
        String map = "" + element.ch() + element.ch() + element.ch() +
                element.ch() + element.ch() + element.ch();
        int fieldHeight = 3;
        MapParserImpl mapParser = new MapParserImpl(map, fieldHeight);

        //when
        List<PointImpl> result = callTestMethod(mapParser);

        //then
        assertThat(result, hasSize(6));

        assertThat(result.get(0).getX(), is(0));
        assertThat(result.get(0).getY(), is(1));

        assertThat(result.get(1).getX(), is(1));
        assertThat(result.get(1).getY(), is(1));

        assertThat(result.get(2).getX(), is(2));
        assertThat(result.get(2).getY(), is(1));

        assertThat(result.get(3).getX(), is(0));
        assertThat(result.get(3).getY(), is(0));

        assertThat(result.get(4).getX(), is(1));
        assertThat(result.get(4).getY(), is(0));

        assertThat(result.get(5).getX(), is(2));
        assertThat(result.get(5).getY(), is(0));
    }

    private <T extends PointImpl> List<T> callTestMethod(MapParserImpl mapParser) {
        if (element == GameElementType.BORDER) {
            return (List<T>) mapParser.getBorders();
        } else if (element == GameElementType.ACCELERATOR) {
            return (List<T>) mapParser.getAccelerators();
        } else if (element == GameElementType.INHIBITOR) {
            return (List<T>) mapParser.getInhibitors();
        } else if (element == GameElementType.OBSTACLE) {
            return (List<T>) mapParser.getObstacles();
        } else if (element == GameElementType.LINE_CHANGER_UP) {
            return (List<T>) mapParser.getLineUpChangers();
        } else if (element == GameElementType.LINE_CHANGER_DOWN) {
            return (List<T>) mapParser.getLineDownChangers();
        } else if (element == SpringboardElementType.SPRINGBOARD_DARK) {
            return (List<T>) mapParser.getSpringboardDarkElements();
        } else if (element == SpringboardElementType.SPRINGBOARD_LEFT_DOWN) {
            return (List<T>) mapParser.getSpringboardLeftDownElements();
        } else if (element == SpringboardElementType.SPRINGBOARD_LEFT_UP) {
            return (List<T>) mapParser.getSpringboardLeftUpElements();
        } else if (element == SpringboardElementType.SPRINGBOARD_LIGHT) {
            return (List<T>) mapParser.getSpringboardLightElements();
        } else if (element == SpringboardElementType.SPRINGBOARD_RIGHT_DOWN) {
            return (List<T>) mapParser.getSpringboardRightDownElements();
        } else if (element == SpringboardElementType.SPRINGBOARD_RIGHT_UP) {
            return (List<T>) mapParser.getSpringboardRightUpElements();
        }
        return null;
    }
}
