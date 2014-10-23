package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.services.GamePrinter;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.Printer;
import com.codenjoy.dojo.services.RandomDice;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 8:29 PM
 */
public class WallsTest {

    private final static int SIZE = 9;
    private IBoard board;
    private Walls walls;

    @Before
    public void setup() {
        board = mock(IBoard.class);
        when(board.size()).thenReturn(SIZE);
        when(board.isBarrier(anyInt(), anyInt(), anyBoolean())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                int x = (Integer)invocation.getArguments()[0];
                int y = (Integer)invocation.getArguments()[1];
                return walls.itsMe(x, y);
            }
        });
    }

    @Test
    public void testOriginalWalls() {
        assertEquals(
                "☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼ ☼ ☼ ☼ ☼\n" +
                "☼       ☼\n" +
                "☼ ☼ ☼ ☼ ☼\n" +
                "☼       ☼\n" +
                "☼ ☼ ☼ ☼ ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n",
                print(new OriginalWalls(v(SIZE))));
    }

    private String print(final Walls walls) {
        return new Printer(SIZE, new GamePrinter() {
            @Override
            public boolean init() {
                return true;
            }

            @Override
            public Enum get(Point pt) {
                Wall wall = walls.get(pt.getX(), pt.getY());
                if (!wall.itsMe(pt)) return Elements.EMPTY;

                if (wall instanceof MeatChopper) return Elements.MEAT_CHOPPER;
                if (wall instanceof DestroyWall) return Elements.DESTROY_WALL;
                if (wall instanceof Wall) return Elements.WALL;

                return Elements.EMPTY;
            }

            @Override
            public void printAll(Filler filler) {
                // do nothing
            }
        }).toString();
    }

    @Test
    public void testWalls() {
        assertEquals(
                "         \n" +
                "         \n" +
                "         \n" +
                "         \n" +
                "         \n" +
                "         \n" +
                "         \n" +
                "         \n" +
                "         \n",
                print(new WallsImpl()));
    }

    @Test
    public void checkPrintDestroyWalls() {
        String actual = getBoardWithDestroyWalls();

        int countBlocks = actual.length() - actual.replace("#", "").length();
        assertEquals(SIZE * SIZE / 10, countBlocks);

        assertEquals(
                "☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼ ☼ ☼ ☼ ☼\n" +
                "☼       ☼\n" +
                "☼ ☼ ☼ ☼ ☼\n" +
                "☼       ☼\n" +
                "☼ ☼ ☼ ☼ ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n", actual.replace('#', ' '));
    }

    @Test
    public void checkPrintMeatChoppers() {
        walls = new MeatChoppers(new OriginalWalls(v(SIZE)), board, v(10), new RandomDice());
        walls.tick();
        String actual = print(walls);

        int countBlocks = actual.length() - actual.replace("&", "").length();
        assertEquals(10, countBlocks);

        assertEquals(
                "☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼ ☼ ☼ ☼ ☼\n" +
                "☼       ☼\n" +
                "☼ ☼ ☼ ☼ ☼\n" +
                "☼       ☼\n" +
                "☼ ☼ ☼ ☼ ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n", actual.replace('&', ' '));
    }

    private String getBoardWithDestroyWalls() {
        walls = new EatSpaceWalls(new OriginalWalls(v(SIZE)), board, v(SIZE * SIZE / 10), new RandomDice());
        walls.tick();
        return print(walls);
    }

}
