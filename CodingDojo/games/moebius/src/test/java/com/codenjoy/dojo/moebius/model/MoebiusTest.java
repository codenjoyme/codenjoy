package com.codenjoy.dojo.moebius.model;

import com.codenjoy.dojo.moebius.services.Events;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import static junit.framework.Assert.assertEquals;
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
        game = new Moebius(level, dice);
        listener = mock(EventListener.class);
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

        dice(1, 1, 0);
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

        dice(1, 3, 2);
        game.tick();

        assertE("╔═══╗" +
                "║╔  ║" +
                "║╚  ║" +
                "║╝  ║" +
                "╚═══╝");

        dice(2, 3, 3);
        game.tick();

        assertE("╔═══╗" +
                "║╔╗ ║" +
                "║╚  ║" +
                "║╝  ║" +
                "╚═══╝");

        dice(3, 3, 4);
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
            dice(3, 1, 6);
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

        dice(1, 1, 0);
        game.tick();

        assertE("╔═══╗" +
                "║   ║" +
                "║   ║" +
                "║╝  ║" +
                "╚═══╝");

        dice(1, 1,
             1, 1,
             1, 1,
             1, 1,
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
                "║   ║" +
                "║   ║" +
                "║   ║" +
                "╚═══╝");
    }

    // я могу повернуть одну любую линию по часовой стрелке
    // я не могу повернуть пустое место
    // любая команда кроме act игнорится

}
