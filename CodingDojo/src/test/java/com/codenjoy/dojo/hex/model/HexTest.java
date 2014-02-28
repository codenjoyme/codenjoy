package com.codenjoy.dojo.hex.model;

import com.codenjoy.dojo.loderunner.model.LoderunnerTest;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.Printer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 4:47
 */
public class HexTest {

    private Hex game;
    private Dice dice;

    private EventListener listener1;
    private Hero hero1;
    private Player player1;
    private Joystick joystick1;

    private EventListener listener2;
    private Hero hero2;
    private Player player2;
    private Joystick joystick2;

    @Before
    public void setup() {
        dice = mock(Dice.class);
    }

    private void dice(int...ints) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int i : ints) {
            when = when.thenReturn(i);
        }
    }

    private void givenFl(String board) {
        LevelImpl level = new LevelImpl(board);
        game = new Hex(level, dice);

        hero1 = level.getHeroes().get(0);
        setupPlayer1();

        if (level.getHeroes().size() > 1) {
            hero2 = level.getHeroes().get(1);
            setupPlayer2();
        }
    }

    private void setupPlayer1() {
        listener1 = mock(EventListener.class);
        player1 = new Player(listener1, game);
        joystick1 = player1.getJoystick();

        dice(hero1.getX(), hero1.getY());

        game.newGame(player1);
        hero1.init(game);
    }

    private void setupPlayer2() {
        listener2 = mock(EventListener.class);
        player2 = new Player(listener2, game);
        joystick2 = player2.getJoystick();

        dice(hero2.getX(), hero2.getY());

        game.newGame(player2);
        hero2.init(game);
    }

    private void assertE(String expected) {
        LoderunnerTest.assertE(new Printer(game.getSize(), new HexPrinter(game, player1)), expected);
    }

    @Test
    public void shouldFieldAtStart() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldSplitUpWhenGoUp() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");


        joystick1.act(2, 2);
        joystick1.up();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼ ☺ ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldSplitDownWhenGoDown() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");


        joystick1.act(2, 2);
        joystick1.down();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼ ☺ ☼" +
                "☼☼☼☼☼");
    }

    @Test
      public void shouldSplitLeftWhenGoLeft() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");


        joystick1.act(2, 2);
        joystick1.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼☺☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

    }

    @Test
    public void shouldSplitLeftAndRight() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");


        joystick1.act(2, 2);
        joystick1.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼☺☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        joystick1.act(2, 2);
        joystick1.right();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼☺☺☺☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldSplitLeftThenUp() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");


        joystick1.act(2, 2);
        joystick1.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼☺☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        joystick1.act(1, 2);
        joystick1.up();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼☺  ☼" +
                "☼☺☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

    }

    @Test
    public void shouldHitTheWall() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");


        joystick1.act(2, 2);
        joystick1.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼☺☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        joystick1.act(1, 2);
        joystick1.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼☺☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

    }

    @Test
    public void shouldHitTheHero() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");


        joystick1.act(2, 2);
        joystick1.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼☺☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        joystick1.act(1, 2);
        joystick1.right();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼☺☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        assertEquals(2, player1.heroes.size());

    }

    @Test
    public void shouldNotPickEmptyField() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        joystick1.act(1, 1);

        joystick1.up();
        game.tick();

        joystick1.down();
        game.tick();

        joystick1.left();
        game.tick();

        joystick1.right();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldTwoPlayersOnBoard() {
        givenFl("☼☼☼☼☼" +
                "☼  ☻☼" +
                "☼   ☼" +
                "☼☺  ☼" +
                "☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼" +
                "☼  ☻☼" +
                "☼   ☼" +
                "☼☺  ☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldSecondPlayerMoveDown() {
        givenFl("☼☼☼☼☼" +
                "☼  ☻☼" +
                "☼   ☼" +
                "☼☺  ☼" +
                "☼☼☼☼☼");

        joystick2.act(3,3);
        joystick2.down();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼  ☻☼" +
                "☼  ☻☼" +
                "☼☺  ☼" +
                "☼☼☼☼☼");

        joystick1.act(1,1);
        joystick1.up();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼  ☻☼" +
                "☼☺ ☻☼" +
                "☼☺  ☼" +
                "☼☼☼☼☼");

    }


}
