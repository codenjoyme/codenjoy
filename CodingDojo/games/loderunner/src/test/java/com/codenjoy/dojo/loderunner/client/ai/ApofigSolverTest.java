package com.codenjoy.dojo.loderunner.client.ai;

import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.loderunner.client.Board;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.algs.DeikstraFindWay;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Created by Sanja on 24.10.2014.
 */
public class ApofigSolverTest {

    private ApofigSolver solver;
    private Dice dice;

    @Before
    public void setup() {
        dice = mock(Dice.class);
        solver = new ApofigSolver(dice);
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
                "☼☼☼☼☼☼", pt(4, 1), "[LEFT]");

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

    @Test
    public void testEnemyAtWay() {
        assertC("☼☼☼☼☼☼☼☼☼☼" +
                "☼     H  ☼" +
                "☼     H  ☼" +
                "☼H$   H  ☼" +
                "☼H ###H  ☼" +
                "☼H    H  ☼" +
                "☼H    H  ☼" +
                "☼H    H  ☼" +
                "☼H  ► H  ☼" +
                "☼☼☼☼☼☼☼☼☼☼",
                "[LEFT, LEFT, LEFT, UP, UP, UP, UP, UP, RIGHT]");

        assertC("☼☼☼☼☼☼☼☼☼☼" +
                "☼     H  ☼" +
                "☼     H  ☼" +
                "☼H$   H  ☼" +
                "☼H ###H  ☼" +
                "☼H    H  ☼" +
                "☼Q    H  ☼" +
                "☼H    H  ☼" +
                "☼H  ► H  ☼" +
                "☼☼☼☼☼☼☼☼☼☼",
                "[RIGHT, RIGHT, UP, UP, UP, UP, UP, LEFT, LEFT, LEFT, LEFT]");
    }

    @Test
    public void testOtherHeroAtWay() {
        assertC("☼☼☼☼☼☼☼☼☼☼" +
                "☼     H  ☼" +
                "☼     H  ☼" +
                "☼H$   H  ☼" +
                "☼H ###H  ☼" +
                "☼H    H  ☼" +
                "☼H    H  ☼" +
                "☼H    H  ☼" +
                "☼H  ► H  ☼" +
                "☼☼☼☼☼☼☼☼☼☼",
                "[LEFT, LEFT, LEFT, UP, UP, UP, UP, UP, RIGHT]");

        assertC("☼☼☼☼☼☼☼☼☼☼" +
                "☼     H  ☼" +
                "☼     H  ☼" +
                "☼H$   H  ☼" +
                "☼H ###H  ☼" +
                "☼H    H  ☼" +
                "☼U    H  ☼" +
                "☼H    H  ☼" +
                "☼H  ► H  ☼" +
                "☼☼☼☼☼☼☼☼☼☼",
                "[RIGHT, RIGHT, UP, UP, UP, UP, UP, LEFT, LEFT, LEFT, LEFT]");
    }

    @Test
    public void testFindBestWay() {
        assertC("☼☼☼☼☼☼☼☼☼☼" +
                "☼     H  ☼" +
                "☼     H  ☼" +
                "☼H$   H  ☼" +
                "☼H ###H  ☼" +
                "☼H    H  ☼" +
                "☼H    H~ ☼" +
                "☼H    H  ☼" +
                "☼H  ► H  ☼" +
                "☼☼☼☼☼☼☼☼☼☼",
                "[LEFT, LEFT, LEFT, UP, UP, UP, UP, UP, RIGHT]");

        assertC("☼☼☼☼☼☼☼☼☼☼" +
                "☼     H  ☼" +
                "☼     H  ☼" +
                "☼H$   H  ☼" +
                "☼H ###H  ☼" +
                "☼H    H  ☼" +
                "☼H    H$ ☼" +
                "☼H    H  ☼" +
                "☼H  ► H  ☼" +
                "☼☼☼☼☼☼☼☼☼☼",
                "[RIGHT, RIGHT, UP, UP, RIGHT]");
    }

    private void assertW(String boardString, String expected) {
        DeikstraFindWay way = new DeikstraFindWay();
        Board board = (Board) new Board().forString(boardString);
        ApofigSolver solver = new ApofigSolver(dice);
        solver.getDirections(board);
        Map<Point, List<Direction>> possibleWays = solver.getWay().getPossibleWays();

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
                for (Direction direction : possibleWays.get(pt(x, y))) {
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
        Board board = (Board) new Board().forString(boardString);
        ApofigSolver solver = new ApofigSolver(dice);
        List<Direction> possible = new LinkedList<Direction>();
        for (Direction direction : Arrays.asList(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT)) {
            boolean possible1 =  solver.possible(board).possible(pt, direction);
            if (possible1) {
                possible.add(direction);
            }
        }
        assertEquals(expected, possible.toString());
    }

    private void assertC(String boardString, String expected) {
        Board board = (Board) new Board().forString(boardString);
        List<Direction> command = new ApofigSolver(dice).getDirections(board);
        assertEquals(expected, command.toString());
    }
}
