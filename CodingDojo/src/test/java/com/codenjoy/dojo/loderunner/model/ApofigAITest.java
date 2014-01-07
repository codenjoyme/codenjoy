package com.codenjoy.dojo.loderunner.model;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static org.junit.Assert.assertEquals;

/**
 * User: sanja
 * Date: 07.01.14
 * Time: 20:49
 */
public class ApofigAITest {

    @Test
    public void shouldGeneratePossibleWays1() {
        assertP("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ◄ ☼" +
                "☼###☼" +
                "☼☼☼☼☼",

                "{[1,2]=[RIGHT], [1,3]=[DOWN], " +
                        "[2,2]=[LEFT, RIGHT], [2,3]=[DOWN], " +
                        "[3,2]=[LEFT], [3,3]=[DOWN]}");
    }

    @Test
    public void shouldGeneratePossibleWays_withLadder() {
        assertP("☼☼☼☼☼" +
                "☼  H☼" +
                "☼ ◄H☼" +
                "☼###☼" +
                "☼☼☼☼☼",

                "{[1,2]=[RIGHT], [1,3]=[DOWN], " +
                        "[2,2]=[LEFT, RIGHT], [2,3]=[DOWN], " +
                        "[3,2]=[LEFT, UP], [3,3]=[LEFT, DOWN]}");
    }

    @Test
    public void shouldGeneratePossibleWays_withPipe() {
        assertP("☼☼☼☼☼" +
                "☼   ☼" +
                "☼◄~~☼" +
                "☼#  ☼" +
                "☼☼☼☼☼",

                "{[1,2]=[RIGHT], [1,3]=[DOWN], " +
                        "[2,1]=[RIGHT], [2,2]=[LEFT, RIGHT, DOWN], [2,3]=[DOWN], " +
                        "[3,1]=[LEFT], [3,2]=[LEFT, DOWN], [3,3]=[DOWN]}");
    }

    @Test
    public void shouldGeneratePossibleWays_withLadder2() {
        assertP("☼☼☼☼☼" +
                "☼   ☼" +
                "☼◄HH☼" +
                "☼#  ☼" +
                "☼☼☼☼☼",

                "{[1,2]=[RIGHT], [1,3]=[DOWN], " +
                        "[2,1]=[RIGHT], [2,2]=[LEFT, RIGHT, UP, DOWN], [2,3]=[LEFT, RIGHT, DOWN], " +
                        "[3,1]=[LEFT], [3,2]=[LEFT, UP, DOWN], [3,3]=[LEFT, DOWN]}");
    }

    @Test
    public void shouldGeneratePossibleWays_withoutBorders() {
        assertP("   H" +
                "~~ H" +
                "H  H" +
                "H  H",

                "{[0,0]=[RIGHT, UP], [0,1]=[RIGHT, UP, DOWN], [0,2]=[RIGHT, DOWN], [0,3]=[DOWN], " +
                        "[1,0]=[LEFT, RIGHT], [1,1]=[DOWN], [1,2]=[LEFT, RIGHT, DOWN], [1,3]=[DOWN], " +
                        "[2,0]=[LEFT, RIGHT], [2,1]=[DOWN], [2,2]=[DOWN], [2,3]=[DOWN], " +
                        "[3,0]=[LEFT, UP], [3,1]=[LEFT, UP, DOWN], [3,2]=[LEFT, UP, DOWN], [3,3]=[LEFT, DOWN]}");
    }

    private void assertP(String map, String expected) {
        Level level = new LevelImpl(map);

        Loderunner loderunner = new Loderunner(level, null);

        ApofigAI ai = new ApofigAI();
        ai.getDirection(loderunner, pt(0, 0));

        Map<Point, List<Direction>> result = new TreeMap<Point, List<Direction>>();
        for (Map.Entry<Point, List<Direction>> entry : ai.possibleWays.entrySet()) {
            List<Direction> value = entry.getValue();
            if (!value.isEmpty()) {
                result.put(entry.getKey(), value);
            }
        }

        assertEquals(expected, result.toString());
    }
}
