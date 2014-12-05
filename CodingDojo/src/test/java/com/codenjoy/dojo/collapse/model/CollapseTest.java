package com.codenjoy.dojo.collapse.model;

import com.codenjoy.dojo.loderunner.model.LoderunnerTest;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.Printer;
import com.codenjoy.dojo.collapse.services.CollapseEvents;
import org.junit.Test;

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

    private void givenFl(String board) {
        LevelImpl level = new LevelImpl(board);

        game = new Collapse(level);
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

        joystick.act(2, 2);
        joystick.left();
        game.tick();

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

        joystick.act(2, 2);
        joystick.right();
        game.tick();

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

        joystick.act(2, 2);
        joystick.down();
        game.tick();

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

        joystick.act(2, 2);
        joystick.up();
        game.tick();

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

        joystick.act(1, 1);
        joystick.down();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼111☼" +
                "☼111☼" +
                "☼211☼" +
                "☼☼☼☼☼");
    }

    // если в ходе моих перемещений образуются конгломераты :) то я получаю выиграшные очки за каждый блок
    // после того как конгломерат исчезнет сверху упадет еще немного новых рендомных циферок
    // ходить надо в течении одного тика, все недоделанные ходы за тик стиратся

}