package com.codenjoy.dojo.snake.battle.client;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2017 Codenjoy
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


import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import org.junit.Before;
import org.junit.Test;

import static com.codenjoy.dojo.services.Direction.RIGHT;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * @author K.ilya
 *         Это пример для реализации unit-тестов твоего бота
 *         Необходимо раскомментировать существующие тесты, добиться их выполнения ботом.
 *         Затем создавай свои тесты, улучшай бота и проверяй что не испортил предыдущие достижения.
 */

public class YourSolverTest {

    Solver<Board> yourSolver;
    Board b;

    private Dice dice;

    @Before
    public void setup() {
        dice = mock(Dice.class);
        yourSolver = new YourSolver(dice);
    }

    private void givenFl(String board) {
        b = new Board();
        b.forString(board);
//      System.out.println("Размер доски: " + b.size());
    }

    private void testSolution(Direction expected) {
        testSolution(expected.toString());
    }

    private void testSolution(String expected) {
        assertEquals(expected, yourSolver.get(b));
    }

    // корректный старт змейки из "стартового бокса"
    @Test
    public void startFromBox() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼☼     ☼" +
                "→►     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼☼☼☼☼☼☼");
        testSolution(RIGHT);
    }

//    // некуда поворачивать кроме как вверх
//    @Test
//    public void onlyUpTurn() {
//        givenFl("☼☼☼☼☼☼☼☼" +
//                "☼☼     ☼" +
//                "☼#     ☼" +
//                "☼☼     ☼" +
//                "☼☼ →►☼ ☼" +
//                "☼☼  ☼☼ ☼" +
//                "☼☼     ☼" +
//                "☼☼☼☼☼☼☼☼");
//        testSolution(UP);
//    }
}
