package com.codenjoy.dojo.sudoku.client;

import com.codenjoy.dojo.sudoku.client.utils.BoardImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class ApofigSolverTest {

    private DirectionSolver ai;
    private BoardImpl board;

    @Before
    public void setup() {
        ai = new ApofigDirectionSolver();
    }

    private void shBoard(String boardString) {
        this.board = new BoardImpl(boardString);
    }

    @Test
    public void should() {
        shBoard("☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼53 ☼ 7 ☼   ☼" +
                "☼6  ☼195☼   ☼" +
                "☼ 98☼   ☼ 6 ☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼8  ☼ 6 ☼  3☼" +
                "☼4  ☼8 3☼  1☼" +
                "☼7  ☼ 2 ☼  6☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼ 6 ☼   ☼28 ☼" +
                "☼   ☼419☼  5☼" +
                "☼   ☼ 8 ☼ 79☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼");

        assertNext(5, 5, 5);
        assertNext(2, 5, 2);

        asrtBrd("☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼53 ☼ 7 ☼   ☼" +
                "☼6  ☼195☼   ☼" +
                "☼ 98☼   ☼ 6 ☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼8  ☼ 6 ☼  3☼" +
                "☼42 ☼853☼  1☼" +
                "☼7  ☼ 2 ☼  6☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼ 6 ☼   ☼28 ☼" +
                "☼   ☼419☼  5☼" +
                "☼   ☼ 8 ☼ 79☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼");

        assertNext(4, 4, 9);
        assertNext(4, 6, 7);
        assertNext(5, 3, 3);
        assertNext(4, 3, 5);
        assertNext(5, 7, 4);
        assertNext(6, 3, 7);

        asrtBrd("☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼53 ☼ 7 ☼   ☼" +
                "☼6  ☼195☼   ☼" +
                "☼ 98☼ 4 ☼ 6 ☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼8  ☼76 ☼  3☼" +
                "☼42 ☼853☼  1☼" +
                "☼7  ☼92 ☼  6☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼ 6 ☼537☼28 ☼" +
                "☼   ☼419☼  5☼" +
                "☼   ☼ 8 ☼ 79☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼");

        assertNext(10, "[(6:7=2), (1:7=1), (1:3=9), (4:7=3), (4:9=6), (4:1=2), (1:1=3), (1:2=2), (3:2=7), (2:2=8)]");
        assertNext(10, "[(6:1=6), (6:9=8), (8:2=3), (7:2=6), (8:5=9), (3:5=6), (7:5=7), (7:7=5), (7:6=4), (6:6=1)]");
        assertNext(10, "[(2:6=5), (2:4=1), (2:1=4), (2:8=7), (3:3=1), (3:1=5), (3:4=3), (3:6=9), (6:4=4), (7:1=1)]");
        assertNext(10, "[(7:4=8), (7:8=3), (7:9=9), (8:4=5), (8:6=2), (8:8=4), (3:8=2), (3:9=4), (8:9=1), (9:3=4)]");

        asrtBrd("☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼534☼678☼91 ☼" +
                "☼672☼195☼34 ☼" +
                "☼198☼342☼56 ☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼859☼761☼423☼" +
                "☼426☼853☼791☼" +
                "☼713☼924☼856☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼961☼537☼284☼" +
                "☼287☼419☼635☼" +
                "☼345☼286☼179☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼");

        assertNext(3, "[(9:7=7), (9:8=8), (9:9=2)]");

        asrtBrd("☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼534☼678☼912☼" +
                "☼672☼195☼348☼" +
                "☼198☼342☼567☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼859☼761☼423☼" +
                "☼426☼853☼791☼" +
                "☼713☼924☼856☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼961☼537☼284☼" +
                "☼287☼419☼635☼" +
                "☼345☼286☼179☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼");

    }

    public static String assertE(String expected) {
        int size = (int) Math.sqrt(expected.length());
        return inject(expected, size, "\n");
    }

    public static String inject(String string, int position, String substring) {
        StringBuilder result = new StringBuilder();
        for (int index = 1; index < string.length() / position + 1; index++) {
            result.append(string.substring((index - 1)*position, index*position)).append(substring);
        }
        result.append(string.substring((string.length() / position) * position, string.length()));
        return result.toString();
    }

    private void asrtBrd(String expected) {
        assertEquals(assertE(expected), board.toString());
    }

    private void assertNext(int x, int y, int n) {
        String actual = ai.get(board);
        assertEquals(String.format("ACT(%s,%s,%s)", x, y, n), actual);
        board.set(x, y, n);
    }

    private void assertNext(int count, String expected) {
        List<String> result = new LinkedList<String>();
        for (int i = 0; i < count; i++) {
            String actual = ai.get(board);
            if (actual.length() == 0) break;
            int x = Integer.valueOf("" + actual.charAt(4));
            int y = Integer.valueOf("" + actual.charAt(6));
            int n = Integer.valueOf("" + actual.charAt(8));
            result.add(String.format("(%s:%s=%s)", x, y, n));
            board.set(x, y, n);
        }
        assertEquals(expected, result.toString());
    }

}
