package com.codenjoy.dojo.collapse.model;

import com.codenjoy.dojo.loderunner.model.LoderunnerTest;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.Printer;
import com.codenjoy.dojo.collapse.services.CollapseEvents;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 4:47
 */
public class CollapseTest {

    private Collapse game;
    private EventListener listener;
    private Player player;
    private Joystick joystick;
    private Dice dice;

    private void givenFl(String board) {
        LevelImpl level = new LevelImpl(board);

        dice = mock(Dice.class);
        game = new Collapse(level, dice);
        listener = mock(EventListener.class);
        player = new Player(listener);
        game.newGame(player);
        joystick = player.getJoystick();
    }

    private void assertE(String expected) {
        LoderunnerTest.assertE(Printer.getSimpleFor(game.reader(), player, Elements.NONE), expected);
    }

    // я вижу поле
    @Test
    public void shouldFieldAtStart() {
        givenFl("☼☼☼☼☼" +
                "☼123☼" +
                "☼456☼" +
                "☼789☼" +
                "☼☼☼☼☼");

        // then
        assertE("☼☼☼☼☼" +
                "☼123☼" +
                "☼456☼" +
                "☼789☼" +
                "☼☼☼☼☼");
    }

    // я могу походить заменив местами две циферки влево
    @Test
    public void shouldExchangeLeft() {
        givenFl("☼☼☼☼☼" +
                "☼111☼" +
                "☼231☼" +
                "☼111☼" +
                "☼☼☼☼☼");

        // when
        joystick.act(2, 2);
        joystick.left();
        game.tick();

        // then
        assertE("☼☼☼☼☼" +
                "☼111☼" +
                "☼321☼" +
                "☼111☼" +
                "☼☼☼☼☼");
    }

    // я могу походить вправо
    @Test
    public void shouldExchangeRight() {
        givenFl("☼☼☼☼☼" +
                "☼111☼" +
                "☼123☼" +
                "☼111☼" +
                "☼☼☼☼☼");

        // when
        joystick.act(2, 2);
        joystick.right();
        game.tick();

        // then
        assertE("☼☼☼☼☼" +
                "☼111☼" +
                "☼132☼" +
                "☼111☼" +
                "☼☼☼☼☼");
    }

    // я могу походить вниз
    @Test
    public void shouldExchangDown() {
        givenFl("☼☼☼☼☼" +
                "☼111☼" +
                "☼121☼" +
                "☼131☼" +
                "☼☼☼☼☼");

        // when
        joystick.act(2, 2);
        joystick.down();
        game.tick();

        // then
        assertE("☼☼☼☼☼" +
                "☼111☼" +
                "☼131☼" +
                "☼121☼" +
                "☼☼☼☼☼");
    }

    // я могу походить вверх
    @Test
    public void shouldExchangUp() {
        givenFl("☼☼☼☼☼" +
                "☼131☼" +
                "☼121☼" +
                "☼111☼" +
                "☼☼☼☼☼");

        // when
        joystick.act(2, 2);
        joystick.up();
        game.tick();

        // then
        assertE("☼☼☼☼☼" +
                "☼121☼" +
                "☼131☼" +
                "☼111☼" +
                "☼☼☼☼☼");
    }

    // я не могу походить заменив циферку на место стенки - стенка не трогается
    @Test
    public void shouldCantExchangeWithWall() {
        givenFl("☼☼☼☼☼" +
                "☼111☼" +
                "☼111☼" +
                "☼211☼" +
                "☼☼☼☼☼");

        // when
        joystick.act(1, 1);
        joystick.down();
        game.tick();

        // then
        assertE("☼☼☼☼☼" +
                "☼111☼" +
                "☼111☼" +
                "☼211☼" +
                "☼☼☼☼☼");
    }

    // если в ходе моих перемещений образуются конгломераты :) то я получаю выиграшные очки за каждый блок
    @Test
    public void shouldCleanAfterExchange() {
        givenFl("☼☼☼☼☼" +
                "☼311☼" +
                "☼121☼" +
                "☼333☼" +
                "☼☼☼☼☼");

        // when
        joystick.act(1, 2);
        joystick.right();
        game.tick();

        // then
        assertE("☼☼☼☼☼" +
                "☼3  ☼" +
                "☼2  ☼" +
                "☼333☼" +
                "☼☼☼☼☼");

        assertEvent(4, CollapseEvents.SUCCESS);

        // when
        joystick.act(1, 3);
        joystick.down();
        game.tick();

        // then
        assertE("☼☼☼☼☼" +
                "☼211☼" +
                "☼ 11☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        assertEvent(4, CollapseEvents.SUCCESS);
    }

    // если в ходе моих перемещений образуются конгломераты :)
    // удаляются конгломераты всех цветов
    @Test
    public void shouldCleanAfterExchange2() {
        givenFl("☼☼☼☼☼" +
                "☼333☼" +
                "☼233☼" +
                "☼222☼" +
                "☼☼☼☼☼");

        // when
        joystick.act(1, 2);
        joystick.right();
        game.tick();

        // then
        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        assertEvent(9, CollapseEvents.SUCCESS);
    }

    private void assertEvent(int expected, CollapseEvents expectedType) {
        ArgumentCaptor<CollapseEvents> event = ArgumentCaptor.forClass(CollapseEvents.class);
        verify(listener).event(event.capture());
        CollapseEvents value = event.getValue();
        assertEquals(expectedType, value);
        assertEquals(expected, value.getCount());

        Mockito.verifyNoMoreInteractions(listener);
        reset(listener);
    }

    // после того как конгломерат исчезнет сверху упадет еще немного новых рендомных циферок
    @Test
    public void shouldNewNumbersAfterClear() {
        // given
        givenFl("☼☼☼☼☼" +
                "☼311☼" +
                "☼121☼" +
                "☼333☼" +
                "☼☼☼☼☼");

        joystick.act(1, 2);
        joystick.right();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼3  ☼" +
                "☼2  ☼" +
                "☼333☼" +
                "☼☼☼☼☼");

        // when
        dice(6 - 1, 7 - 1, 8 - 1, 9 - 1);
        game.tick();

        // then
        assertE("☼☼☼☼☼" +
                "☼379☼" +
                "☼268☼" +
                "☼333☼" +
                "☼☼☼☼☼");

    }

    private void dice(int... next) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int i : next) {
            when = when.thenReturn(i);
        }
    }

    // ходить надо в течении одного тика, все недоделанные ходы за тик стиратся
    // если после удаления конгломерата ничего не делать, то со следующим тиком упадут те, кто сверху над пустотами
}