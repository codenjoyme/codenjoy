package com.codenjoy.dojo.hex.model;

import com.codenjoy.dojo.hex.services.HexEvents;
import com.codenjoy.dojo.loderunner.model.LoderunnerTest;
import com.codenjoy.dojo.sample.services.SampleEvents;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.Printer;
import org.junit.Before;
import org.junit.Ignore;
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

    private void dice(int... ints) {
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


        joystick1.act(3, 3);
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


        joystick1.act(3, 3);
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


        joystick1.act(3, 3);
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


        joystick1.act(3, 3);
        joystick1.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼☺☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        joystick1.act(3, 3);
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


        joystick1.act(3, 3);
        joystick1.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼☺☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        joystick1.act(2, 3);
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


        joystick1.act(3, 3);
        joystick1.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼☺☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        joystick1.act(2, 3);
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


        joystick1.act(3, 3);
        joystick1.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼☺☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        joystick1.act(2, 3);
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

        joystick1.act(2, 2);

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
    public void shouldSecondPlayerMoveDownAndFirstUp() {
        givenFl("☼☼☼☼☼" +
                "☼  ☻☼" +
                "☼   ☼" +
                "☼☺  ☼" +
                "☼☼☼☼☼");

        joystick2.act(4, 4);
        joystick2.down();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼  ☻☼" +
                "☼  ☻☼" +
                "☼☺  ☼" +
                "☼☼☼☼☼");

        joystick1.act(2, 2);
        joystick1.up();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼  ☻☼" +
                "☼☺ ☻☼" +
                "☼☺  ☼" +
                "☼☼☼☼☼");

    }

    @Test
    public void shouldSecondPlayerMoveDownAndFirstUpDuringOneTick() {
        givenFl("☼☼☼☼☼" +
                "☼  ☻☼" +
                "☼   ☼" +
                "☼☺  ☼" +
                "☼☼☼☼☼");

        joystick2.act(4, 4);
        joystick2.down();
        joystick1.act(2, 2);
        joystick1.up();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼  ☻☼" +
                "☼☺ ☻☼" +
                "☼☺  ☼" +
                "☼☼☼☼☼");

    }

    @Test
    public void shouldAnnigilateWhenTwoPlayersMoveTowardsEachOther() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼  ☻☼" +
                "☼☺  ☼" +
                "☼☼☼☼☼");

        joystick1.act(2, 2);
        joystick1.right();
        joystick2.act(4, 3);
        joystick2.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☻☻☼" +
                "☼☺☺ ☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldDoNothingWhenTwoPlayersMoveTowardsEachOtherWhenBadY() {
        givenFl("☼☼☼☼☼" +
                "☼☻  ☼" +
                "☼   ☼" +
                "☼☺  ☼" +
                "☼☼☼☼☼");

        joystick1.act(2, 2);
        joystick1.right();
        joystick2.act(2, 4);
        joystick2.right();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼☻☻ ☼" +
                "☼   ☼" +
                "☼☺☺ ☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldDoNothingWhenTwoPlayersMoveTowardsEachOther2() {
        givenFl("☼☼☼☼☼☼" +
                "☼☻   ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼☺   ☼" +
                "☼☼☼☼☼☼");

        joystick1.act(2, 2);
        joystick1.up();
        joystick2.act(2, 5);
        joystick2.down();
        game.tick();

        assertE("☼☼☼☼☼☼" +
                "☼☻   ☼" +
                "☼☻   ☼" +
                "☼☺   ☼" +
                "☼☺   ☼" +
                "☼☼☼☼☼☼");
    }

    @Test
    public void shouldKillWhenPlayerMoveTowardsStanding() {
        // given
        givenFl("☼☼☼☼☼" +
                "☼  ☻☼" +
                "☼   ☼" +
                "☼☺  ☼" +
                "☼☼☼☼☼");

        joystick2.act(4, 4);
        joystick2.down();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼  ☻☼" +
                "☼  ☻☼" +
                "☼☺  ☼" +
                "☼☼☼☼☼");

        // when
        joystick1.act(2, 2);
        joystick1.right();
        game.tick();

        //then
        assertE("☼☼☼☼☼" +
                "☼  ☻☼" +
                "☼  ☺☼" +
                "☼☺☺ ☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldCaptureManyEnemies() {
        // given
        givenFl("☼☼☼☼☼" +
                "☼  ☻☼" +
                "☼   ☼" +
                "☼☺  ☼" +
                "☼☼☼☼☼");

        joystick2.act(4, 4);
        joystick2.down();
        game.tick();
        joystick2.act(4, 3);
        joystick2.down();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼  ☻☼" +
                "☼  ☻☼" +
                "☼☺ ☻☼" +
                "☼☼☼☼☼");

        // when
        joystick2.act(4, 4);
        joystick2.left();

        joystick1.act(2, 2);
        joystick1.right();
        game.tick();

        // then
        assertE("☼☼☼☼☼" +
                "☼ ☻☻☼" +
                "☼  ☺☼" +
                "☼☺☺☺☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldNotMoveOnTheSamePlace() {
        givenFl("☼☼☼☼☼" +
                "☼  ☻☼" +
                "☼   ☼" +
                "☼  ☺☼" +
                "☼☼☼☼☼");

        // when
        joystick2.act(4, 4);
        joystick2.down();
        joystick1.act(4, 2);
        joystick1.up();
        game.tick();

        // then
        assertE("☼☼☼☼☼" +
                "☼  ☻☼" +
                "☼  ☼☼" +
                "☼  ☺☼" +
                "☼☼☼☼☼");

        assertEquals(1, player1.getHeroes().size());
        assertEquals(1, player2.getHeroes().size());
    }

    // начисляются очки за зажваченных героев
    // 1) я размножил и должен получить + за новое место
    @Test
    public void shouldScoreOnDuplicate() {
        givenFl("☼☼☼☼☼" +
                "☼  ☺☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        // when
        joystick1.act(4, 4);
        joystick1.down();
        game.tick();

        // then
        verify(listener1).event(HexEvents.WIN);

        assertE("☼☼☼☼☼" +
                "☼  ☺☼" +
                "☼  ☺☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }

    // 2) я захватил противника, у меня должно быть столько плюсов, сколько я захватил, а у напарника столько же минусов
    // 2.1) я захватил 1 фишку
    @Test
    public void shouldScoreOnCapture() {
        givenFl("☼☼☼☼☼" +
                "☼  ☻☼" +
                "☼   ☼" +
                "☼☺  ☼" +
                "☼☼☼☼☼");

        joystick2.act(4, 4);
        joystick2.down();
        game.tick();
        reset(listener1);
        reset(listener2);

        assertE("☼☼☼☼☼" +
                "☼  ☻☼" +
                "☼  ☻☼" +
                "☼☺  ☼" +
                "☼☼☼☼☼");

        // when
        joystick1.act(2, 2);
        joystick1.right();
        game.tick();

        // then
        verify(listener1, times(2)).event(HexEvents.WIN);
        verify(listener2).event(HexEvents.LOOSE);

        assertE("☼☼☼☼☼" +
                "☼  ☻☼" +
                "☼  ☺☼" +
                "☼☺☺ ☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldNotSaveLastActive() {
        givenFl("☼☼☼☼☼" +
                "☼  ☻☼" +
                "☼   ☼" +
                "☼☺  ☼" +
                "☼☼☼☼☼");

        joystick2.act(4, 4);
        joystick2.down();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼  ☻☼" +
                "☼  ☻☼" +
                "☼☺  ☼" +
                "☼☼☼☼☼");


        joystick1.act(2, 2);
        joystick1.up();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼  ☻☼" +
                "☼☺ ☻☼" +
                "☼☺  ☼" +
                "☼☼☼☼☼");

        // when
        joystick1.act(4, 2);
        joystick1.right();
        game.tick();

        // then
        assertE("☼☼☼☼☼" +
                "☼  ☻☼" +
                "☼☺ ☻☼" +
                "☼☺  ☼" +
                "☼☼☼☼☼");
    }

    // 2.2) я захватил 2 фишки
    @Test
    public void shouldScoreTwoOnCaptureTwo() {
        givenFl("☼☼☼☼☼" +
                "☼  ☻☼" +
                "☼   ☼" +
                "☼☺  ☼" +
                "☼☼☼☼☼");

        joystick2.act(4, 4);
        joystick2.down();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼  ☻☼" +
                "☼  ☻☼" +
                "☼☺  ☼" +
                "☼☼☼☼☼");


        joystick1.act(2, 2);
        joystick1.up();
        game.tick();

        reset(listener1);
        reset(listener2);


        assertE("☼☼☼☼☼" +
                "☼  ☻☼" +
                "☼☺ ☻☼" +
                "☼☺  ☼" +
                "☼☼☼☼☼");

        // when
        joystick1.act(2, 3);
        joystick1.right();
        game.tick();



        assertE("☼☼☼☼☼" +
                "☼  ☺☼" +
                "☼☺☺☺☼" +
                "☼☺  ☼" +
                "☼☼☼☼☼");

        // then
        verify(listener1, times(3)).event(HexEvents.WIN);
        verify(listener2, times(2)).event(HexEvents.LOOSE);

    }

    @Test
    public void shouldNotMoveFromEmptySpace() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☺  ☼" +
                "☼☼☼☼☼");

        joystick1.act(3, 3);
        joystick1.up();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☺  ☼" +
                "☼☼☼☼☼");


    }

    // Новая игра должна давать новое поле
    @Test
    public void shouldStartNewGameOnGameOver() {
        shouldScoreTwoOnCaptureTwo();

        game.tick();

        assertFalse(player2.isAlive());
        assertFalse(player1.isAlive());

        dice(1, 1);
        game.newGame(player1);
        dice(3, 3);
        game.newGame(player2);

        assertE("☼☼☼☼☼" +
                "☼  ☻☼" +
                "☼   ☼" +
                "☼☺  ☼" +
                "☼☼☼☼☼");
    }

    // можно ходить через 2 клеточки, при этом герой мувается + учитывать очки при захвате
    @Test
    public void shouldJumpUp() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☺  ☼" +
                "☼☼☼☼☼");

        joystick1.act(2, 2, 1);
        joystick1.up();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼☺  ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldJumpDown() {
        givenFl("☼☼☼☼☼" +
                "☼☺  ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        joystick1.act(2, 4, 1);
        joystick1.down();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☺  ☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldJumpLeft() {
        givenFl("☼☼☼☼☼" +
                "☼  ☺☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        joystick1.act(4, 4, 1);
        joystick1.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼☺  ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldJumpRight() {
        givenFl("☼☼☼☼☼" +
                "☼☺  ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        joystick1.act(2, 4, 1);
        joystick1.right();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼  ☺☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }

    // герой не прыгает на ледующий шаг, если уже прыгал
    @Test
    public void shouldNoTwiceJump() {
        shouldJumpRight();
        assertE("☼☼☼☼☼" +
                "☼  ☺☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        joystick1.act(4, 4);
        joystick1.down();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼  ☺☼" +
                "☼  ☺☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }

    // захват вражеских игроков, при прыжке
    @Test
    public void shouldGetOnJump() {
        givenFl("☼☼☼☼☼☼" +
                "☼   ☻☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  ☺ ☼" +
                "☼☼☼☼☼☼");

        joystick2.act(5, 5);
        joystick2.left();
        game.tick();

        assertE("☼☼☼☼☼☼" +
                "☼  ☻☻☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  ☺ ☼" +
                "☼☼☼☼☼☼");
        reset(listener1, listener2);

        // when
        joystick1.act(4, 2, 1);
        joystick1.up();
        game.tick();

        // then
        assertE("☼☼☼☼☼☼" +
                "☼  ☺☺☼" +
                "☼  ☺ ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");

        verify(listener1, times(2)).event(HexEvents.WIN);
        verify(listener2, times(2)).event(HexEvents.LOOSE);
    }

    @Test
    public void shouldGetOnJumpBlack() {
        givenFl("☼☼☼☼☼☼" +
                "☼   ☻☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  ☺ ☼" +
                "☼☼☼☼☼☼");

        // when
        joystick2.act(5, 5, 1);
        joystick2.down();
        game.tick();

        // then
        assertE("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼   ☻☼" +
                "☼  ☻ ☼" +
                "☼☼☼☼☼☼");

        verify(listener1, times(1)).event(HexEvents.LOOSE);
        verify(listener2, times(1)).event(HexEvents.WIN);
    }

    // геймовер, когда оба игрока jump нули и анигизировались
    @Test
    public void shouldBreakWhenJumpAtOnePlace() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼  ☻  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ☺  ☼" +
                "☼☼☼☼☼☼☼");

        // when
        joystick2.act(4, 6, 1);
        joystick2.down();

        joystick1.act(4, 2, 1);
        joystick1.up();
        game.tick();

        // then
        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ☼  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        verify(listener1, times(1)).event(HexEvents.LOOSE);
        verify(listener2, times(1)).event(HexEvents.LOOSE);

        // when
        game.tick();

        // then
        assertFalse(player2.isAlive());
        assertFalse(player1.isAlive());
    }

    // если прыгаем друг к дружке впритык, то не захватываем территорий
    @Test
    public void shouldDoNothingWhenJumpAtOnePlace() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼  ☻   ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ☺   ☼" +
                "☼☼☼☼☼☼☼☼");

        // when
        joystick2.act(4, 7, 1);
        joystick2.down();

        joystick1.act(4, 2, 1);
        joystick1.up();
        game.tick();

        // then
        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ☻   ☼" +
                "☼  ☺   ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        verifyNoMoreInteractions(listener1, listener2);
    }

    // если ходим друг к дружке впритык, то не захватываем территорий
    @Test
    public void shouldDoNothingWhenMoveAtOnePlace() {
        givenFl("☼☼☼☼☼☼" +
                "☼  ☻ ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  ☺ ☼" +
                "☼☼☼☼☼☼");

        // when
        joystick2.act(4, 5);
        joystick2.down();

        joystick1.act(4, 2);
        joystick1.up();
        game.tick();

        // then
        assertE("☼☼☼☼☼☼" +
                "☼  ☻ ☼" +
                "☼  ☻ ☼" +
                "☼  ☺ ☼" +
                "☼  ☺ ☼" +
                "☼☼☼☼☼☼");

        verify(listener1, times(1)).event(HexEvents.WIN);
        verify(listener2, times(1)).event(HexEvents.WIN);
    }

    // геймовер, когда некуда больше ходить
    public void givenNoSpace() {
        givenFl("☼☼☼☼☼" +
                "☼ ☻ ☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼☼☼☼☼");

        joystick2.act(3, 4);
        joystick2.left();

        joystick1.act(3, 2);
        joystick1.left();
        game.tick();


        joystick2.act(3, 4);
        joystick2.right();

        joystick1.act(3, 2);
        joystick1.right();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼☻☻☻☼" +
                "☼   ☼" +
                "☼☺☺☺☼" +
                "☼☼☼☼☼");

        joystick2.act(2, 4);
        joystick2.down();

        joystick1.act(4, 2);
        joystick1.up();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼☻☺☺☼" +
                "☼☻ ☺☼" +
                "☼☻☻☺☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldGameOverWhenNoSpace_whenGo() {
        givenNoSpace();
        assertE("☼☼☼☼☼" +
                "☼☻☺☺☼" +
                "☼☻ ☺☼" +
                "☼☻☻☺☼" +
                "☼☼☼☼☼");

        assertTrue(player2.isAlive());
        assertTrue(player1.isAlive());

        joystick2.act(3, 2);
        joystick2.up();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼☻☻☻☼" +
                "☼☻☻☻☼" +
                "☼☻☻☻☼" +
                "☼☼☼☼☼");

        assertTrue(player2.isAlive());
        assertTrue(player1.isAlive());

        // when
        game.tick();

        // then
        assertFalse(player1.isAlive());
        assertFalse(player2.isAlive());
    }

    @Test
    public void shouldGameOverWhenNoSpace_whenAnig() {
        givenNoSpace();
        assertE("☼☼☼☼☼" +
                "☼☻☺☺☼" +
                "☼☻ ☺☼" +
                "☼☻☻☺☼" +
                "☼☼☼☼☼");

        // when
        assertTrue(player2.isAlive());
        assertTrue(player1.isAlive());

        joystick2.act(3, 2);
        joystick2.up();

        joystick1.act(3, 4);
        joystick1.down();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼☻☺☺☼" +
                "☼☻☼☺☼" +
                "☼☻☻☺☼" +
                "☼☼☼☼☼");

        assertTrue(player2.isAlive());
        assertTrue(player1.isAlive());

        // when
        game.tick();

        // then
        assertFalse(player1.isAlive());
        assertFalse(player2.isAlive());
    }
}
