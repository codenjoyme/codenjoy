package com.codenjoy.dojo.loderunner.services.ai;

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


import com.codenjoy.dojo.games.loderunner.Board;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.algs.DeikstraFindWay;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class AISolverTest {

    private AISolver solver;
    private Dice dice;

    @Before
    public void setup() {
        dice = mock(Dice.class);
        solver = new AISolver(dice);
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
                " ☼  H→←►→←.→←.  ☼ \n" +
                "    ↓             \n" +
                "    ↑             \n" +
                " ☼  H  #  #  #  ☼ \n" +
                "    ↓             \n" +
                "    ↑             \n" +
                " ☼  H→ .  .  $  ☼ \n" +
                "    ↓  ↓  ↓  ↓    \n" +
                "    ↑             \n" +
                " ☼  H→←.→←.→←.  ☼ \n" +
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
                "[LEFT, UP, UP, RIGHT, RIGHT, RIGHT, RIGHT, RIGHT, RIGHT, RIGHT, DOWN, DOWN, DOWN, DOWN, RIGHT, DOWN, DOWN, RIGHT, RIGHT, DOWN, DOWN, LEFT, LEFT, LEFT, LEFT, LEFT]");

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
                " ☼  .  .  .  . ←H→ .  .  .  ☼ \n" +
                "    ↓  ↓  ↓  ↓  ↓  ↓  ↓  ↓    \n" +
                "                ↑             \n" +
                " ☼  . ←.→←.→←.→←H→←~→←~→←~  ☼ \n" +
                "    ↓  ↓              ↓  ↓    \n" +
                "       ↑                      \n" +
                " ☼  . ←H  #  #  #  #  .  $  ☼ \n" +
                "    ↓  ↓              ↓  ↓    \n" +
                "       ↑                      \n" +
                " ☼  .→←Y→←.→←.→←.→←.→←.→←.  ☼ \n" +
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
    public void testRobberAtWay() {
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

    /**
     * Проверяет куда вообще можно походить из каждой клетки
     * @param boardString поле
     * @param expected ожидаемая карта возможных движений из каждой клетки
     */
    private void assertW(String boardString, String expected) {
        Board board = (Board) new Board().forString(boardString);
        AISolver solver = new AISolver(dice);
        solver.getDirections(board);
        Map<Point, List<Direction>> possibleWays = solver.getWay().getBasic().toMap();

        String actual = TestUtils.drawPossibleWays(3,
                possibleWays,
                board.size(),
                pt -> board.getAt(pt).getChar());

        assertEquals(expected, actual);
    }

    /**
     * Проверяет куда можно походить на этом поле из заданной координаты
     * @param boardString поле
     * @param from проверяемая координата
     * @param expected ожидаемые дозволенные направленияч движения из этой клетки
     */
    private void assertB(String boardString, Point from, String expected) {
        Board board = (Board) new Board().forString(boardString);
        AISolver solver = new AISolver(dice);
        DeikstraFindWay.Possible possible = solver.possible(board);

        List<Direction> actual = new LinkedList<>();
        for (Direction direction : Direction.getValues()) {
            if (possible.check(board.size(), from, direction)) {
                actual.add(direction);
            }
        }
        assertEquals(expected, actual.toString());
    }

    /**
     * Проверяет куда пойдет AISolver на этом поле
     * @param boardString поле
     * @param expected ожидаемый путь до горки золота
     */
    private void assertC(String boardString, String expected) {
        Board board = (Board) new Board().forString(boardString);
        List<Direction> command = new AISolver(dice).getDirections(board);

        assertEquals(expected, command.toString());
    }
}
