package com.codenjoy.dojo.excitebike.model.level;

import com.codenjoy.dojo.excitebike.model.items.Elements;
import com.codenjoy.dojo.excitebike.model.items.springboard.SpringboardType;
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
                Elements.BORDER,
                Elements.ACCELERATOR,
                Elements.INHIBITOR,
                Elements.OBSTACLE,
                Elements.LINE_CHANGER_UP,
                Elements.LINE_CHANGER_DOWN,
                Elements.ROAD,
                SpringboardType.DARK,
                SpringboardType.LIGHT,
                SpringboardType.LEFT_DOWN,
                SpringboardType.LEFT_UP,
                SpringboardType.RIGHT_DOWN,
                SpringboardType.RIGHT_UP
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
        if (element == Elements.BORDER) {
            return (List<T>) mapParser.getBorders();
        } else if (element == Elements.ACCELERATOR) {
            return (List<T>) mapParser.getAccelerators();
        } else if (element == Elements.INHIBITOR) {
            return (List<T>) mapParser.getInhibitors();
        } else if (element == Elements.OBSTACLE) {
            return (List<T>) mapParser.getObstacles();
        } else if (element == Elements.LINE_CHANGER_UP) {
            return (List<T>) mapParser.getLineUpChangers();
        } else if (element == Elements.LINE_CHANGER_DOWN) {
            return (List<T>) mapParser.getLineDownChangers();
        } else if (element == Elements.ROAD) {
            return (List<T>) mapParser.getRoadElements();
        } else if (element == SpringboardType.DARK) {
            return (List<T>) mapParser.getSpringboardDarkElements();
        } else if (element == SpringboardType.LEFT_DOWN) {
            return (List<T>) mapParser.getSpringboardLeftDownElements();
        } else if (element == SpringboardType.LEFT_UP) {
            return (List<T>) mapParser.getSpringboardLeftUpElements();
        } else if (element == SpringboardType.LIGHT) {
            return (List<T>) mapParser.getSpringboardLightElements();
        } else if (element == SpringboardType.RIGHT_DOWN) {
            return (List<T>) mapParser.getSpringboardRightDownElements();
        } else if (element == SpringboardType.RIGHT_UP) {
            return (List<T>) mapParser.getSpringboardRightUpElements();
        }
        return null;
    }
}
