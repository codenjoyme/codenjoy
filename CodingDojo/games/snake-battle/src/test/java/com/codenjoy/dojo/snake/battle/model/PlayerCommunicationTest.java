package com.codenjoy.dojo.snake.battle.model;

import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.PrinterFactory;
import com.codenjoy.dojo.services.PrinterFactoryImpl;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * @author Kors
 */
public class PlayerCommunicationTest {
    private SnakeBoard game;
    private Hero hero;
    private Hero enemy;
    private Dice dice;
    private EventListener listener;
    private Player player;
    private PrinterFactory printer = new PrinterFactoryImpl();

    String simpleField =
            "☼☼☼☼☼☼☼" +
                    "☼     ☼" +
                    "☼ →►  ☼" +
                    "☼     ☼" +
                    "☼ ⇒>  ☼" +
                    "☼     ☼" +
                    "☼☼☼☼☼☼☼";

    @Before
    public void setup() {
        dice = mock(Dice.class);
    }

    private void givenFl(String board) {
        LevelImpl level = new LevelImpl(board);
        Hero hero = level.getHero().get(0);
        Hero enemy = level.getEnemy().get(0);
        game = new SnakeBoard(level, dice);
        listener = mock(EventListener.class);
        player = new Player(listener);
        Player player2 = new Player(mock(EventListener.class));
        game.newGame(player);
        game.newGame(player2);
        player2.setHero(enemy);
        player.setHero(hero);
        hero.init(game);
        enemy.init(game);
        this.hero = game.getHeroes().get(0);
        this.enemy = game.getHeroes().get(1);
    }

    // проверяем что змейка умирает, когда врезается в соперника
    @Test
    public void diedByAnotherHero() {
        givenFl(simpleField);
        hero.down();
        enemy.up();
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ↓  ☼" +
                "☼  ☻  ☼" +
                "☼  ⇑  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    private void assertE(String expected) {
        assertEquals(TestUtils.injectN(expected),
                printer.getPrinter(game.reader(), player).print());
    }
}
