package com.codenjoy.dojo.moebius.model;

import com.codenjoy.dojo.moebius.services.Events;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.stubbing.OngoingStubbing;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

public class MoebiusTest {

    private Moebius game;
    private Dice dice;
    private EventListener listener;
    private Player player;
    private PrinterFactory printer = new PrinterFactoryImpl();
    private Joystick joystick;

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
        listener = mock(EventListener.class);
        game = new Moebius(level, dice, listener);
        player = new Player(listener);
        game.newGame(player);
        joystick = game.getJoystick();
    }

    private void assertE(String expected) {
        assertEquals(TestUtils.injectN(expected),
                printer.getPrinter(game.reader(), player).print());
    }

    // есть карта с границами
    @Test
    public void shouldFieldAtStart() {
        givenFl("╔═══╗" +
                "║   ║" +
                "║   ║" +
                "║   ║" +
                "╚═══╝");

        assertE("╔═══╗" +
                "║   ║" +
                "║   ║" +
                "║   ║" +
                "╚═══╝");
    }

    // за тик рендомно появляется 1 линия
    @Test
    public void shouldAddRandomLine_whenTick() {
        givenFl("╔═══╗" +
                "║   ║" +
                "║   ║" +
                "║   ║" +
                "╚═══╝");

        dice(1, 3, 0);
        game.tick();

        assertE("╔═══╗" +
                "║   ║" +
                "║   ║" +
                "║╝  ║" +
                "╚═══╝");

        dice(1, 2, 1);
        game.tick();

        assertE("╔═══╗" +
                "║   ║" +
                "║╚  ║" +
                "║╝  ║" +
                "╚═══╝");

        dice(1, 1, 2);
        game.tick();

        assertE("╔═══╗" +
                "║╔  ║" +
                "║╚  ║" +
                "║╝  ║" +
                "╚═══╝");

        dice(2, 1, 3);
        game.tick();

        assertE("╔═══╗" +
                "║╔╗ ║" +
                "║╚  ║" +
                "║╝  ║" +
                "╚═══╝");

        dice(3, 1, 4);
        game.tick();

        assertE("╔═══╗" +
                "║╔╗═║" +
                "║╚  ║" +
                "║╝  ║" +
                "╚═══╝");

        dice(3, 2, 5);
        game.tick();

        assertE("╔═══╗" +
                "║╔╗═║" +
                "║╚ ║║" +
                "║╝  ║" +
                "╚═══╝");

        dice(3, 3, 6);
        game.tick();

        assertE("╔═══╗" +
                "║╔╗═║" +
                "║╚ ║║" +
                "║╝ ╬║" +
                "╚═══╝");
    }

    // если не так линия, то ошибку получаем
    @Test
    public void shouldError_whenBadLineType() {
        givenFl("╔═══╗" +
                "║   ║" +
                "║   ║" +
                "║   ║" +
                "╚═══╝");

        try {
            dice(3, 1, 7);
            game.tick();
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Неопознанная линия: ' '", e.getMessage());
        }
    }

    // линия не может появиться на другой линии
    @Test
    public void shouldAddRandomLine_onlyAtFreeSpace() {
        givenFl("╔═══╗" +
                "║   ║" +
                "║   ║" +
                "║   ║" +
                "╚═══╝");

        dice(1, 3, 0);
        game.tick();

        assertE("╔═══╗" +
                "║   ║" +
                "║   ║" +
                "║╝  ║" +
                "╚═══╝");

        dice(1, 3,
             1, 3,
             1, 3,
             1, 3,
             2, 2, 1);
        game.tick();

        assertE("╔═══╗" +
                "║   ║" +
                "║ ╚ ║" +
                "║╝  ║" +
                "╚═══╝");
    }

    // линия не может появиться на границе
    @Test
    public void shouldAddRandomLine_onlyAtFreeSpaceBetweenBorders() {
        givenFl("╔═══╗" +
                "║   ║" +
                "║   ║" +
                "║   ║" +
                "╚═══╝");

        dice(0, 0,
                3, 15,
                0, 3,
                3, 4,
                4, 3,
                3, 0,
                -1, 3,
                3, -1,
                15, 3,
                1, 1, 1);
        game.tick();

        assertE("╔═══╗" +
                "║╚  ║" +
                "║   ║" +
                "║   ║" +
                "╚═══╝");
    }

    // я могу повернуть одну любую линию по часовой стрелке
    @Test
    public void shouldRotateClockwiseOneLine_caseCorner() {
        givenFl("╔═══╗" +
                "║ ╗ ║" +
                "║   ║" +
                "║   ║" +
                "╚═══╝");

        joystick.act(2, 1);
        game.tick();

        assertE("╔═══╗" +
                "║ ╝ ║" +
                "║   ║" +
                "║   ║" +
                "╚═══╝");

        joystick.act(2, 1);
        game.tick();

        assertE("╔═══╗" +
                "║ ╚ ║" +
                "║   ║" +
                "║   ║" +
                "╚═══╝");

        joystick.act(2, 1);
        game.tick();

        assertE("╔═══╗" +
                "║ ╔ ║" +
                "║   ║" +
                "║   ║" +
                "╚═══╝");

        joystick.act(2, 1);
        game.tick();

        assertE("╔═══╗" +
                "║ ╗ ║" +
                "║   ║" +
                "║   ║" +
                "╚═══╝");
    }

    @Test
    public void shouldRotateClockwiseOneLine_caseLine() {
        givenFl("╔═══╗" +
                "║ ═ ║" +
                "║   ║" +
                "║   ║" +
                "╚═══╝");

        joystick.act(2, 1);
        game.tick();

        assertE("╔═══╗" +
                "║ ║ ║" +
                "║   ║" +
                "║   ║" +
                "╚═══╝");

        joystick.act(2, 1);
        game.tick();

        assertE("╔═══╗" +
                "║ ═ ║" +
                "║   ║" +
                "║   ║" +
                "╚═══╝");
    }

    @Test
    public void shouldRotateClockwiseOneLine_caseCross() {
        givenFl("╔═══╗" +
                "║ ╬ ║" +
                "║   ║" +
                "║   ║" +
                "╚═══╝");

        joystick.act(2, 1);
        game.tick();

        assertE("╔═══╗" +
                "║ ╬ ║" +
                "║   ║" +
                "║   ║" +
                "╚═══╝");
    }

    // я не могу повернуть пустое место
    @Test
    public void shouldRotateEmptySpace() {
        givenFl("╔═══╗" +
                "║   ║" +
                "║   ║" +
                "║   ║" +
                "╚═══╝");

        joystick.act(2, 1);
        game.tick();

        assertE("╔═══╗" +
                "║   ║" +
                "║   ║" +
                "║   ║" +
                "╚═══╝");
    }

    // любая команда кроме act игнорится
    @Test
    public void shouldIgnoreCommandsExceptAct() {
        givenFl("╔═══╗" +
                "║╔╗═║" +
                "║╚╗║║" +
                "║╝╚╬║" +
                "╚═══╝");

        joystick.left();
        joystick.right();
        joystick.up();
        joystick.down();
        game.tick();
        game.tick();
        game.tick();

        assertE("╔═══╗" +
                "║╔╗═║" +
                "║╚╗║║" +
                "║╝╚╬║" +
                "╚═══╝");
    }

    // если я во время вращения сделаю круг, то все линии пропадут
    @Test
    public void shouldRemoveLine_whenCycling() {
        givenFl("╔═══╗" +
                "║╔╗ ║" +
                "║╚╗ ║" +
                "║   ║" +
                "╚═══╝");

        joystick.act(2, 2);
        dice(1, 1, 1);
        game.tick();

        verifyEvent(new Events(Events.Event.WIN, 4));

        assertE("╔═══╗" +
                "║╚  ║" +
                "║   ║" +
                "║   ║" +
                "╚═══╝");
    }

    private void verifyEvent(Events expected) {
        ArgumentCaptor<Events> event = ArgumentCaptor.forClass(Events.class);
        verify(listener).event(event.capture());
        Events actual = event.getValue();
        assertEquals(expected.getType(), actual.getType());
        assertEquals(expected.getLines(), actual.getLines());
    }

    @Test
    public void shouldRemoveLine_whenCycling2() {
        givenFl("╔═══╗" +
                "║╔═╗║" +
                "║║ ║║" +
                "║╚═╗║" +
                "╚═══╝");

        joystick.act(3, 3);
        dice(1, 1, 1);
        game.tick();

        verifyEvent(new Events(Events.Event.WIN, 8));

        assertE("╔═══╗" +
                "║╚  ║" +
                "║   ║" +
                "║   ║" +
                "╚═══╝");
    }

    @Test
    public void shouldRemoveLine_whenCycling3() {
        givenFl("╔════╗" +
                "║╔══╗║" +
                "║║╔═╝║" +
                "║║╚═╗║" +
                "║╚══╗║" +
                "╚════╝");

        joystick.act(4, 4);
        dice(1, 1, 1);
        game.tick();

        verifyEvent(new Events(Events.Event.WIN, 16));

        assertE("╔════╗" +
                "║╚   ║" +
                "║    ║" +
                "║    ║" +
                "║    ║" +
                "╚════╝");
    }

    @Test
    public void shouldRemoveLine_whenCycling_withNoise() {
        givenFl("╔══════╗" +
                "║╔═╔║══║" +
                "║║╔══╗╗║" +
                "║╔║║╔║║║" +
                "║╚║║║║║║" +
                "║║╚══╗║║" +
                "║╔╗╔╗║║║" +
                "╚══════╝");

        joystick.act(5, 5);
        dice(2, 2, 1);
        game.tick();

        verifyEvent(new Events(Events.Event.WIN, 12));

        assertE("╔══════╗" +
                "║╔═╔║══║" +
                "║║╚   ╗║" +
                "║╔ ║╔ ║║" +
                "║╚ ║║ ║║" +
                "║║    ║║" +
                "║╔╗╔╗║║║" +
                "╚══════╝");
    }

    // любая команда кроме act игнорится
    @Test
    public void shouldGameOver_whenNoMoreSpace() {
        givenFl("╔═══╗" +
                "║╔╗═║" +
                "║╚╗║║" +
                "║╝╚╬║" +
                "╚═══╝");

        assertFalse(game.isGameOver());

        game.tick();

        verifyEvent(new Events(Events.Event.GAME_OVER));

        assertTrue(game.isGameOver());
    }

    // если есть CROSS линия, то по ней можно пройтись дважды
    @Test
    public void shouldRemoveLine_whenCycling_caseCross() {
        givenFl("╔═════╗" +
                "║  ╔═╗║" +
                "║  ║ ║║" +
                "║╔═╬═╝║" +
                "║║ ║  ║" +
                "║╚═╗  ║" +
                "╚═════╝");

        joystick.act(3, 5);
        dice(1, 1, 1);
        game.tick();

        verifyEvent(new Events(Events.Event.WIN, 15));

        assertE("╔═════╗" +
                "║╚    ║" +
                "║     ║" +
                "║     ║" +
                "║     ║" +
                "║     ║" +
                "╚═════╝");
    }

    // если кросс линия использована не полностью, то она все равно удаляется
    @Test
    public void shouldRemoveLine_whenCycling_caseCrossDoNotRemove() {
        givenFl("╔═════╗" +
                "║╔═╗  ║" +
                "║║ ║  ║" +
                "║║═╬═╝║" +
                "║║ ║  ║" +
                "║╚═╗  ║" +
                "╚═════╝");

        joystick.act(3, 5);
        dice(1, 1, 1);
        game.tick();

        verifyEvent(new Events(Events.Event.WIN, 12));

        assertE("╔═════╗" +
                "║╚    ║" +
                "║     ║" +
                "║ ═ ═╝║" +
                "║     ║" +
                "║     ║" +
                "╚═════╝");
    }

    @Test
    public void shouldRemoveLine_whenCycling_caseCrossDoNotRemove2() {
        givenFl("╔═════╗" +
                "║╔═══╗║" +
                "║║ ║ ║║" +
                "║╚═╬═╗║" +
                "║  ║  ║" +
                "║  ║  ║" +
                "╚═════╝");

        joystick.act(5, 3);
        dice(1, 1, 1);
        game.tick();

        verifyEvent(new Events(Events.Event.WIN, 12));

        assertE("╔═════╗" +
                "║╚    ║" +
                "║  ║  ║" +
                "║     ║" +
                "║  ║  ║" +
                "║  ║  ║" +
                "╚═════╝");
    }
}
