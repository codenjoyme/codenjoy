package com.codenjoy.dojo.tetris.client;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.tetris.model.Elements;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.util.Arrays;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static org.junit.Assert.assertEquals;

/**
 * Created by indigo on 2018-11-09.
 */
public class BoardTest {

    @Test
    public void test() {
        Board board = getBoard(
                "......." +
                "......I" +
                "..LL..I" +
                "...LI.I" +
                ".SSLI.I" +
                "SSOOIOO" +
                "..OOIOO",
                "T",
                pt(1, 2),
                new String[]{"I", "O", "L", "Z"});

        assertEquals(Elements.NONE, board.getGlass().getAt(0, 0));
        assertEquals(true, board.isFree(0, 0));

        assertEquals(Elements.YELLOW, board.getGlass().getAt(2, 0));
        assertEquals(false, board.isFree(2, 0));

        assertEquals(Elements.GREEN, board.getGlass().getAt(2, 2));
        assertEquals(false, board.isFree(2, 2));

        assertEquals(Elements.ORANGE, board.getGlass().getAt(3, 4));
        assertEquals(false, board.isFree(3, 0));

        assertEquals("[[0,1], [1,1], [1,2], [2,2]]",
                board.getGlass().get(Elements.GREEN).toString());

        assertEquals("[., L, ., L, L, ., I, ., .]",
                board.getGlass().getNear(pt(3, 4)).toString());

        assertEquals("[1,2]", board.getCurrentFigurePoint().toString());
        assertEquals("T", board.getCurrentFigureType().toString());
        assertEquals("[I, O, L, Z]", board.getFutureFigures().toString());
    }

    public static Board getBoard(String glass, String figureType,
                                 Point point, String[] futureFigures)
    {
        JSONObject result = new JSONObject(){{
            put("layers", new JSONArray(){{
                put(glass);
            }});
            put("currentFigureType", figureType);
            put("currentFigurePoint", new JSONObject(point));
            put("futureFigures", new JSONArray(){{
                Arrays.stream(futureFigures)
                        .forEach(s -> this.put(s));
            }});
        }};

        return (Board) new Board().forString(result.toString());
    }

}
