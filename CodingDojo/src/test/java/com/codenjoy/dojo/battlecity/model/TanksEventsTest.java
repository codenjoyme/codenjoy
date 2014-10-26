package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.battlecity.services.BattlecityEvents;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.EventListener;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;

/**
 * User: sanja
 * Date: 13.12.13
 * Time: 10:29
 */
public class TanksEventsTest {

    private Tank aiTank;
    private Battlecity game;
    private EventListener events;
    private Player player;
    private Tank tank;
    private TanksTest utils = new TanksTest();

    @Before
    public void setup() {
        aiTank = utils.tank(1, 5, Direction.DOWN);

        game = new Battlecity(7, Arrays.asList(new Construction[0]), aiTank);

        events = mock(EventListener.class);
        player = utils.player(1, 1, 2, 2, events);
        game.newGame(player);
        tank = player.getTank();
    }

    @Test
    public void shouldKillAiTankEvent() {
        assertDraw(
                "☼☼☼☼☼☼☼\n" +
                "☼˅    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        tank.act();
        game.tick();

        assertDraw(
                "☼☼☼☼☼☼☼\n" +
                "☼˅    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        noEvents(events);

        game.tick();

        assertDraw(
                "☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        onlyEvent(events, BattlecityEvents.KILL_OTHER_TANK);
    }

    @Test
    public void shouldKillMyTankByAIEvent() {
        assertDraw(
                "☼☼☼☼☼☼☼\n" +
                "☼˅    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        aiTank.act();
        game.tick();

        assertDraw(
                "☼☼☼☼☼☼☼\n" +
                "☼˅    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        noEvents(events);

        game.tick();

        assertDraw(
                "☼☼☼☼☼☼☼\n" +
                "☼˅    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        onlyEvent(events, BattlecityEvents.KILL_YOUR_TANK);
    }

    @Test
    public void shouldKillOtherPlayerTankEvent() {
        EventListener events2 = mock(EventListener.class);
        Player player2 = utils.player(5, 1, events2);
        game.newGame(player2);
        Tank tank2 = player2.getTank();

        assertDraw(
                "☼☼☼☼☼☼☼\n" +
                "☼˅    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        tank.right();
        tank.act();
        game.tick();

        assertDraw(
                "☼☼☼☼☼☼☼\n" +
                "☼˅    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ►• ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        noEvents(events);
        noEvents(events2);

        game.tick();

        assertDraw(
                "☼☼☼☼☼☼☼\n" +
                "☼˅    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ►  Ѡ☼\n" +
                "☼☼☼☼☼☼☼\n");

        onlyEvent(events, BattlecityEvents.KILL_OTHER_TANK);
        onlyEvent(events2, BattlecityEvents.KILL_YOUR_TANK);
    }

    @Test
    public void shouldKillMyTankByOtherPlayerTankEvent() {
        EventListener events2 = mock(EventListener.class);
        Player player2 = utils.player(5, 1, events2);
        game.newGame(player2);
        Tank tank2 = player2.getTank();

        assertDraw(
                "☼☼☼☼☼☼☼\n" +
                "☼˅    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        tank2.left();
        tank2.act();
        game.tick();

        assertDraw(
                "☼☼☼☼☼☼☼\n" +
                "☼˅    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲ •˂ ☼\n" +
                "☼☼☼☼☼☼☼\n");

        noEvents(events);
        noEvents(events2);

        game.tick();

        assertDraw(
                "☼☼☼☼☼☼☼\n" +
                "☼˅    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ  ˂ ☼\n" +
                "☼☼☼☼☼☼☼\n");

        onlyEvent(events, BattlecityEvents.KILL_YOUR_TANK);
        onlyEvent(events2, BattlecityEvents.KILL_OTHER_TANK);
    }

    private void noEvents(EventListener ev) {
        Mockito.verifyNoMoreInteractions(ev);
        reset(events);
    }

    @Test
    public void shouldIKillOtherTankWhenKillMeByAi() {
        EventListener events2 = mock(EventListener.class);
        Player player2 = utils.player(5, 1, events2);
        game.newGame(player2);
        Tank tank2 = player2.getTank();

        assertDraw(
                "☼☼☼☼☼☼☼\n" +
                "☼˅    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        tank.turn(Direction.RIGHT);
        aiTank.act();
        game.tick();

        assertDraw(
                "☼☼☼☼☼☼☼\n" +
                "☼˅    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼►   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        tank.act();
        game.tick();

        assertDraw(
                "☼☼☼☼☼☼☼\n" +
                "☼˅    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ • ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        onlyEvent(events, BattlecityEvents.KILL_YOUR_TANK);
        noEvents(events2);

        game.tick();

        assertDraw(
                "☼☼☼☼☼☼☼\n" +
                "☼˅    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        noEvents(events);
        noEvents(events2);
    }

    private void onlyEvent(EventListener ev, BattlecityEvents event) {
        Mockito.verify(ev).event(event);
        noEvents(ev);
        reset(events);
    }

    private void assertDraw(String field) {
        assertEquals(field, TanksTest.getPrinterFor(game, player).toString());
    }

    @Test
    public void shouldMyBulletsRemovesWhenKillMe() {
        assertDraw(
                "☼☼☼☼☼☼☼\n" +
                "☼˅    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        tank.turn(Direction.RIGHT);
        aiTank.act();
        game.tick();

        assertDraw(
                "☼☼☼☼☼☼☼\n" +
                "☼˅    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼►    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        tank.act();
        game.tick();

        assertDraw(
                "☼☼☼☼☼☼☼\n" +
                "☼˅    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ •  ☼\n" +
                "☼☼☼☼☼☼☼\n");


        assertFalse(player.getTank().isAlive());
        game.newGame(player);
        game.tick();

        assertDraw(
                "☼☼☼☼☼☼☼\n" +
                "☼˅    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ►   ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

}
