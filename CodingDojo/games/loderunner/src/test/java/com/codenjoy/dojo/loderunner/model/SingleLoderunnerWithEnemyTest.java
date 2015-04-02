package com.codenjoy.dojo.loderunner.model;

import com.codenjoy.dojo.loderunner.services.LoderunnerEvents;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.PrinterFactory;
import com.codenjoy.dojo.services.PrinterFactoryImpl;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * User: sanja
 * Date: 19.12.13
 * Time: 5:22
 */
public class SingleLoderunnerWithEnemyTest {

    private Dice dice;
    private EventListener listener;
    private SingleLoderunner game;
    private Loderunner loderunner;
    private PrinterFactory printerFactory = new PrinterFactoryImpl();

    // чертик идет за тобой
    @Test
    public void shoulEnemyGoToHero() {
        setupGm("☼☼☼☼☼☼☼☼" +
                "☼     »☼" +
                "☼H#####☼" +
                "☼H     ☼" +
                "☼###H  ☼" +
                "☼►  H  ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        atGame(
                "☼☼☼☼☼☼☼☼\n" +
                "☼    « ☼\n" +
                "☼H#####☼\n" +
                "☼H     ☼\n" +
                "☼###H  ☼\n" +
                "☼►  H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        game.tick();
        game.tick();
        game.tick();
        game.tick();

        atGame(
                "☼☼☼☼☼☼☼☼\n" +
                "☼«     ☼\n" +
                "☼H#####☼\n" +
                "☼H     ☼\n" +
                "☼###H  ☼\n" +
                "☼►  H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        game.tick();
        game.tick();

        atGame(
                "☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼H#####☼\n" +
                "☼Q     ☼\n" +
                "☼###H  ☼\n" +
                "☼►  H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        game.tick();
        game.tick();
        game.tick();

        atGame(
                "☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼H#####☼\n" +
                "☼H  »  ☼\n" +
                "☼###H  ☼\n" +
                "☼►  H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        game.tick();
        game.tick();
        game.tick();

        atGame(
                "☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼H#####☼\n" +
                "☼H     ☼\n" +
                "☼###H  ☼\n" +
                "☼► «H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        game.tick();
        game.tick();

        atGame(
                "☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼H#####☼\n" +
                "☼H     ☼\n" +
                "☼###H  ☼\n" +
                "☼Ѡ  H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        verify(listener).event(LoderunnerEvents.KILL_HERO);
        assertTrue(game.isGameOver());

        when(dice.next(anyInt())).thenReturn(1, 4);

        game.newGame();

        game.tick();

        atGame(
                "☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼H#####☼\n" +
                "☼H  ►  ☼\n" +
                "☼###H  ☼\n" +
                "☼ » H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");
    }

    // чертик идет за тобой по более короткому маршруту
    @Test
    public void shouldEnemyGoToHeroShortestWay() {
        setupGm("☼☼☼☼☼☼☼☼" +
                "☼     »☼" +
                "☼H####H☼" +
                "☼H    H☼" +
                "☼###H##☼" +
                "☼  ►H  ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        atGame(
                "☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼H####Q☼\n" +
                "☼H    H☼\n" +
                "☼###H##☼\n" +
                "☼  ►H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

    }

    @Test
    public void shouldEnemyGoToHeroShortestWay2() {
        setupGm("☼☼☼☼☼☼☼☼" +
                "☼»     ☼" +
                "☼H####H☼" +
                "☼H    H☼" +
                "☼###H##☼" +
                "☼  ►H  ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        atGame(
                "☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼Q####H☼\n" +
                "☼H    H☼\n" +
                "☼###H##☼\n" +
                "☼  ►H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

    }

    // другой чертик чертику не помеха
    @Test
    public void shouldEnemyGoToHeroShortestWayGetRoundOther() {
        setupGm("☼☼☼☼☼☼☼☼" +
                "☼»    »☼" +
                "☼H####H☼" +
                "☼H    H☼" +
                "☼###H##☼" +
                "☼  ►H  ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        atGame(
                "☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼Q####Q☼\n" +
                "☼H    H☼\n" +
                "☼###H##☼\n" +
                "☼  ►H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

    }

    @Test
    public void shouldEnemyGoToHeroShortestWayGetRoundOther2() {
        setupGm("☼☼☼☼☼☼☼☼" +
                "☼» »   ☼" +
                "☼H####H☼" +
                "☼H    H☼" +
                "☼###H##☼" +
                "☼  ►H  ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        atGame(
                "☼☼☼☼☼☼☼☼\n" +
                "☼ «    ☼\n" +
                "☼Q####H☼\n" +
                "☼H    H☼\n" +
                "☼###H##☼\n" +
                "☼  ►H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

    }

    // если чертику не достать одного он бежит за другим а не зависает
    @Test
    public void shouldEnemyGoToNewHeroIfOneIsHidden() {
        setupGm("☼☼☼☼☼☼☼☼" +
                "☼   ►  ☼" +
                "☼######☼" +
                "☼      ☼" +
                "☼###H##☼" +
                "☼»  H  ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();
        game.tick();
        game.tick();
        game.tick();

        atGame(
                "☼☼☼☼☼☼☼☼\n" +
                "☼   ►  ☼\n" +
                "☼######☼\n" +
                "☼      ☼\n" +
                "☼###H##☼\n" +
                "☼»  H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        setupPlayer(1, 4);
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        atGame(
                "☼☼☼☼☼☼☼☼\n" +
                "☼   (  ☼\n" +
                "☼######☼\n" +
                "☼Ѡ     ☼\n" +
                "☼###H##☼\n" +
                "☼   H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");
    }


    private void atGame(String expected) {
        assertEquals(expected, game.getBoardAsString());
    }

    private void setupPlayer(int x, int y) {
        listener = mock(EventListener.class);
        game = new SingleLoderunner(loderunner, listener, printerFactory);
        when(dice.next(anyInt())).thenReturn(x, y);
        game.newGame();
    }

    private void setupGm(String map) {
        Level level = new LevelImpl(map);
        dice = mock(Dice.class);
        loderunner = new Loderunner(level, dice);

        int px = level.getHeroes().get(0).getX();
        int py = level.getHeroes().get(0).getY();
        setupPlayer(px, py);
    }
}
