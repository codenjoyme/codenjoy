package com.codenjoy.dojo.snakebattle.client.ai;

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


import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.services.settings.SimpleParameter;
import com.codenjoy.dojo.snakebattle.client.Board;
import com.codenjoy.dojo.snakebattle.model.Player;
import com.codenjoy.dojo.snakebattle.model.board.SnakeBoard;
import com.codenjoy.dojo.snakebattle.model.board.Timer;
import com.codenjoy.dojo.snakebattle.model.hero.Hero;
import com.codenjoy.dojo.snakebattle.model.level.LevelImpl;
import org.junit.Before;
import org.junit.Test;

import static com.codenjoy.dojo.services.Direction.RIGHT;
import static com.codenjoy.dojo.services.Direction.UP;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * @author Kors
 */
public class AISolverTest {

    Solver<Board> solver;
    Board b;

    private Dice dice;

    private PrinterFactory printer = new PrinterFactoryImpl();

    @Before
    public void setup() {
        dice = mock(Dice.class);
        solver = new AISolver(dice);
    }

    private void givenFl(String board) {
        b = new Board();
        b.forString(board);

        // этот весь код ниже используется сейчас только для распечатки изображения доски (для наглядности)
        // можно смело убирать, если мешает
        LevelImpl level = new LevelImpl(board);

        SnakeBoard game = new SnakeBoard(level, dice,
                new Timer(new SimpleParameter<>(0)),
                new Timer(new SimpleParameter<>(300)),
                new Timer(new SimpleParameter<>(1)),
                new SimpleParameter<>(5),
                new SimpleParameter<>(10),
                new SimpleParameter<>(10),
                new SimpleParameter<>(3),
                new SimpleParameter<>(2));

        Hero hero = level.getHero(game);
        EventListener listener = mock(EventListener.class);
        Player player = new Player(listener);

        game.newGame(player);
        if (hero != null) {
            player.setHero(hero);
            hero.init(game);
            hero.setActive(true);
        }
    }

    private void testSolution(Direction expected) {
        testSolution(expected.toString());
    }

    private void testSolution(String expected) {
        assertEquals(expected, solver.get(b));
    }

    // корректный старт змейки из "стартового бокса"
    @Test
    public void startFromBox() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼☼     ☼" +
                "╘►     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼  ○  ☼" +
                "☼☼☼☼☼☼☼☼");
        testSolution(RIGHT);
    }

    // некуда поворачивать кроме как вверх
    @Test
    public void onlyUpTurn() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼☼     ☼" +
                "☼#     ☼" +
                "☼☼     ☼" +
                "☼☼╘►☼  ☼" +
                "☼☼ ☼☼  ☼" +
                "☼☼    ○☼" +
                "☼☼☼☼☼☼☼☼");
        testSolution(UP);
    }
}
