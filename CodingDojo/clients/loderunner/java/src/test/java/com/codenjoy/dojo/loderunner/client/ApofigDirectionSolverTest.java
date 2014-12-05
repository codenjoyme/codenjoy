package com.codenjoy.dojo.loderunner.client;

import com.codenjoy.dojo.loderunner.client.ApofigDirectionSolver;
import com.codenjoy.dojo.loderunner.client.Direction;
import com.codenjoy.dojo.loderunner.client.utils.Board;
import com.codenjoy.dojo.loderunner.client.utils.Dice;
import com.codenjoy.dojo.loderunner.client.utils.Point;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Created by Sanja on 24.10.2014.
 */
public class ApofigDirectionSolverTest {

    private ApofigDirectionSolver solver;
    private Dice dice;

    @Before
    public void setup() {
        dice = mock(Dice.class);
        solver = new ApofigDirectionSolver(dice);
    }

    @Test
    public void test() {
        assertC("☼☼☼☼☼" +
                "☼► $☼" +
                "☼###☼" +
                "☼###☼" +
                "☼☼☼☼☼", "[RIGHT, RIGHT]");

        assertC("☼☼☼☼☼" +
                "☼►  ☼" +
                "☼##H☼" +
                "☼$  ☼" +
                "☼☼☼☼☼", "[RIGHT, RIGHT, DOWN, DOWN, LEFT, LEFT]");

        assertC("☼☼☼☼☼" +
                "☼►  ☼" +
                "☼#~~☼" +
                "☼ #$☼" +
                "☼☼☼☼☼", "[RIGHT, DOWN, RIGHT, DOWN]");

        assertC("☼☼☼☼☼☼" +
                "☼H►  ☼" +
                "☼H###☼" +
                "☼H  $☼" +
                "☼H   ☼" +
                "☼☼☼☼☼☼",
                "[]");

        assertB("☼☼☼☼☼☼" +
                "☼H►  ☼" +
                "☼H###☼" +
                "☼H  $☼" +
                "☼H   ☼" +
                "☼☼☼☼☼☼", Point.pt(4, 1), "[LEFT]");

        assertW("☼☼☼☼☼☼" +
                "☼H►  ☼" +
                "☼H###☼" +
                "☼H  $☼" +
                "☼H   ☼" +
                "☼☼☼☼☼☼",
                "                  \n" +
                " ☼  ☼  ☼  ☼  ☼  ☼ \n" +
                "                  \n" +
                "                  \n" +
                " ☼  H++►++*++*  ☼ \n" +
                "    +             \n" +
                "    +             \n" +
                " ☼  H  #  #  #  ☼ \n" +
                "    +             \n" +
                "    +             \n" +
                " ☼  H+ *  *  $  ☼ \n" +
                "    +  +  +  +    \n" +
                "    +             \n" +
                " ☼  H++*++*++*  ☼ \n" +
                "                  \n" +
                "                  \n" +
                " ☼  ☼  ☼  ☼  ☼  ☼ \n" +
                "                  \n");

        assertC("☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼           ☼" +
                "☼##H########☼" +
                "☼  H     $  ☼" +
                "☼H☼☼####  $ ☼" +
                "☼H►    #    ☼" +
                "☼H######    ☼" +
                "☼H     #~~~~☼" +
                "☼H  $  #    ☼" +
                "☼H     #    ☼" +
                "☼☼###☼##☼## ☼" +
                "☼☼###☼$     ☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼",
                "[LEFT, UP, UP, RIGHT, RIGHT, RIGHT, RIGHT, RIGHT, RIGHT, RIGHT, DOWN, DOWN, DOWN, DOWN, DOWN, DOWN, RIGHT, RIGHT, RIGHT, DOWN, DOWN, LEFT, LEFT, LEFT, LEFT, LEFT]");

        assertW("☼☼☼☼☼☼☼☼☼☼" +
                "☼    H   ☼" +
                "☼    H~~~☼" +
                "☼ H#### $☼" +
                "☼ Y      ☼" +
                "☼☼☼☼☼☼☼☼☼☼" +
                "☼☼☼☼☼☼☼☼☼☼" +
                "☼☼☼☼☼☼☼☼☼☼" +
                "☼☼☼☼☼☼☼☼☼☼" +
                "☼☼☼☼☼☼☼☼☼☼",
                "                              \n" +
                " ☼  ☼  ☼  ☼  ☼  ☼  ☼  ☼  ☼  ☼ \n" +
                "                              \n" +
                "                              \n" +
                " ☼  *  *  *  * +H+ *  *  *  ☼ \n" +
                "    +  +  +  +  +  +  +  +    \n" +
                "                +             \n" +
                " ☼  * +*++*++*++H++~++~++~  ☼ \n" +
                "    +  +              +  +    \n" +
                "       +                      \n" +
                " ☼  * +H  #  #  #  #  *  $  ☼ \n" +
                "    +  +              +  +    \n" +
                "       +                      \n" +
                " ☼  *++Y++*++*++*++*++*++*  ☼ \n" +
                "                              \n" +
                "                              \n" +
                " ☼  ☼  ☼  ☼  ☼  ☼  ☼  ☼  ☼  ☼ \n" +
                "                              \n" +
                "                              \n" +
                " ☼  ☼  ☼  ☼  ☼  ☼  ☼  ☼  ☼  ☼ \n" +
                "                              \n" +
                "                              \n" +
                " ☼  ☼  ☼  ☼  ☼  ☼  ☼  ☼  ☼  ☼ \n" +
                "                              \n" +
                "                              \n" +
                " ☼  ☼  ☼  ☼  ☼  ☼  ☼  ☼  ☼  ☼ \n" +
                "                              \n" +
                "                              \n" +
                " ☼  ☼  ☼  ☼  ☼  ☼  ☼  ☼  ☼  ☼ \n" +
                "                              \n");

        assertC("☼☼☼☼☼☼☼☼☼☼" +
                "☼    H   ☼" +
                "☼    H~~~☼" +
                "☼ H#### $☼" +
                "☼ H►     ☼" +
                "☼☼☼☼☼☼☼☼☼☼" +
                "☼☼☼☼☼☼☼☼☼☼" +
                "☼☼☼☼☼☼☼☼☼☼" +
                "☼☼☼☼☼☼☼☼☼☼" +
                "☼☼☼☼☼☼☼☼☼☼",
                "[LEFT, UP, UP, RIGHT, RIGHT, RIGHT, RIGHT, RIGHT, RIGHT, DOWN]");

        assertC("☼☼☼☼☼☼☼☼☼☼" +
                "☼    H   ☼" +
                "☼    H~~~☼" +
                "☼ H#### $☼" +
                "☼ Y      ☼" +
                "☼☼☼☼☼☼☼☼☼☼" +
                "☼☼☼☼☼☼☼☼☼☼" +
                "☼☼☼☼☼☼☼☼☼☼" +
                "☼☼☼☼☼☼☼☼☼☼" +
                "☼☼☼☼☼☼☼☼☼☼",
                "[UP, UP, RIGHT, RIGHT, RIGHT, RIGHT, RIGHT, RIGHT, DOWN]");

        assertC("☼☼☼☼☼☼☼☼☼☼" +
                "☼     H  ☼" +
                "☼     H  ☼" +
                "☼ ~~~~H  ☼" +
                "☼ $   H  ☼" +
                "☼     H  ☼" +
                "☼     H  ☼" +
                "☼     H  ☼" +
                "☼    ►H  ☼" +
                "☼☼☼☼☼☼☼☼☼☼",
                "[RIGHT, UP, UP, UP, UP, UP, LEFT, LEFT, LEFT, LEFT, DOWN]");

        assertC("☼☼☼☼☼☼☼☼☼☼" +
                "☼     H  ☼" +
                "☼     H  ☼" +
                "☼ ~~~{H  ☼" +
                "☼ $   H  ☼" +
                "☼     H  ☼" +
                "☼     H  ☼" +
                "☼     H  ☼" +
                "☼     H  ☼" +
                "☼☼☼☼☼☼☼☼☼☼",
                "[LEFT, LEFT, LEFT, DOWN]");

        assertC("☼☼☼☼☼☼☼☼☼☼" +
                "☼     H  ☼" +
                "☼     H  ☼" +
                "☼ ~~~}H  ☼" +
                "☼ $   H  ☼" +
                "☼     H  ☼" +
                "☼     H  ☼" +
                "☼     H  ☼" +
                "☼     H  ☼" +
                "☼☼☼☼☼☼☼☼☼☼",
                "[LEFT, LEFT, LEFT, DOWN]");

        assertC("☼☼☼☼☼☼☼☼☼☼" +
                "☼     H  ☼" +
                "☼     H  ☼" +
                "☼ HHHYH  ☼" +
                "☼ $   H  ☼" +
                "☼     H  ☼" +
                "☼     H  ☼" +
                "☼     H  ☼" +
                "☼     H  ☼" +
                "☼☼☼☼☼☼☼☼☼☼",
                "[LEFT, LEFT, LEFT, DOWN]");

        assertC("☼☼☼☼☼☼☼☼☼☼" +
                "☼     H  ☼" +
                "☼     H  ☼" +
                "☼ $  ►H  ☼" +
                "☼  HHHH  ☼" +
                "☼     H  ☼" +
                "☼     H  ☼" +
                "☼     H  ☼" +
                "☼     H  ☼" +
                "☼☼☼☼☼☼☼☼☼☼",
                "[LEFT, LEFT, LEFT]");

        assertC("☼☼☼☼☼☼☼☼☼☼" +
                "☼        ☼" +
                "☼        ☼" +
                "☼        ☼" +
                "☼        ☼" +
                "☼        $☼" +
                "☼  Y#####☼" +
                "☼  H#####☼" +
                "☼########☼" +
                "☼☼☼☼☼☼☼☼☼☼",
                "[UP, RIGHT, RIGHT, RIGHT, RIGHT, RIGHT]");

        assertC("☼☼☼☼☼" +
                "☼$H ☼" +
                "☼ H ☼" +
                "☼ Q►☼" +
                "☼☼☼☼☼", "[]");

    }

    private void assertW(String boardString, String expected) {
        ApofigDirectionSolver.DeikstraFindWay way = new ApofigDirectionSolver.DeikstraFindWay();
        Board board = new Board(boardString);
        way.setupPossibleWays(board);
        Map<Point, List<Direction>> possibleWays = way.possibleWays;

        char[][] chars = new char[board.size() * 3][board.size() * 3];
        for (int x = 0; x < chars.length; x++) {
            Arrays.fill(chars[x], ' ');
        }

        for (int x = 0; x < board.size(); x++) {
            for (int y = 0; y < board.size(); y++) {
                int cx = x*3 + 1;
                int cy = y*3 + 1;

                char ch = board.getAt(x, y).getChar();
                chars[cx][cy] = (ch == ' ')?'*':ch;
                for (Direction direction : possibleWays.get(Point.pt(x, y))) {
                    chars[direction.changeX(cx)][direction.changeY(cy)] = '+';
                }
            }
        }

        StringBuffer buffer = new StringBuffer();
        for (int x = 0; x < chars.length; x++) {
            for (int y = 0; y < chars.length; y++) {
                buffer.append(chars[y][x]);
            }
            buffer.append('\n');
        }

        assertEquals(expected, buffer.toString());
    }

    private void assertB(String boardString, Point pt, String expected) {
        Board board = new Board(boardString);
        ApofigDirectionSolver.DeikstraFindWay way = new ApofigDirectionSolver.DeikstraFindWay();
        List<Direction> possible = new LinkedList<Direction>();
        for (Direction direction : Arrays.asList(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT)) {
            boolean possible1 = way.isPossible(board, pt, direction);
            if (possible1) {
                possible.add(direction);
            }
        }
        assertEquals(expected, possible.toString());
    }

    private void assertC(String boardString, String expected) {
        List<Direction> command = solver.getShortestWay(new Board(boardString));
        assertEquals(expected, command.toString());
    }
}
