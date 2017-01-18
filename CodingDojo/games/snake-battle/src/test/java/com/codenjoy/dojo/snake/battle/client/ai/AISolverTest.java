package com.codenjoy.dojo.snake.battle.client.ai;

import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.snake.battle.client.Board;
import com.codenjoy.dojo.snake.battle.model.Player;
import com.codenjoy.dojo.snake.battle.model.board.SnakeBoard;
import com.codenjoy.dojo.snake.battle.model.hero.Hero;
import com.codenjoy.dojo.snake.battle.model.level.LevelImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.codenjoy.dojo.services.Direction.RIGHT;
import static com.codenjoy.dojo.services.Direction.UP;
import static junit.framework.Assert.assertEquals;
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
//      System.out.println("Размер доски: " + b.size());

        // этот весь код ниже используется сейчас только для распечатки изображения доски (для наглядности)
        // можно смело убирать, если мешает
        LevelImpl level = new LevelImpl(board);
        List<Hero> heroes = level.getHero();
        Hero hero = heroes.isEmpty() ? null : heroes.get(0);
        SnakeBoard game = new SnakeBoard(level, dice);
        game.debugMode = true;
        EventListener listener = mock(EventListener.class);
        Player player = new Player(listener);
        game.newGame(player);
        if (hero != null) {
            player.setHero(hero);
            hero.init(game);
            hero.setActive(true);
        }
        System.out.println(printer.getPrinter(game.reader(), player).print());
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
                "→►     ☼" +
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
                "☼☼→►☼  ☼" +
                "☼☼ ☼☼  ☼" +
                "☼☼    ○☼" +
                "☼☼☼☼☼☼☼☼");
        testSolution(UP);
    }
}
