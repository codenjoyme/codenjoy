package com.codenjoy.dojo.loderunner.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.loderunner.TestSettings;
import com.codenjoy.dojo.loderunner.model.Pill.PillType;
import com.codenjoy.dojo.loderunner.services.Events;
import com.codenjoy.dojo.loderunner.services.GameSettings;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.multiplayer.Single;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.loderunner.services.GameSettings.Keys.ENEMIES_COUNT;
import static com.codenjoy.dojo.loderunner.services.GameSettings.Keys.SHADOW_PILLS_COUNT;
import static com.codenjoy.dojo.services.round.RoundSettings.Keys.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class MultiplayerTest {

    private Dice dice;
    private List<EventListener> listeners = new LinkedList<>();
    private List<Player> players = new LinkedList<>();
    private List<Hero> heroes = new LinkedList<>();
    private List<Game> games = new LinkedList<>();
    private Loderunner field;
    private PrinterFactory printerFactory;
    private GameSettings settings;
    protected EventsListenersAssert events = new EventsListenersAssert(listeners);

    @Before
    public void setUp()  {
        dice = mock(Dice.class);
        printerFactory = new PrinterFactoryImpl();
        settings = new TestSettings();
    }
    
    // появляется другие игроки, игра становится мультипользовательской
    @Test
    public void shouldMultipleGame() { // TODO разделить тест на части
        givenFl("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼####☼" +
                "☼   $☼" +
                "☼####☼" +
                "☼☼☼☼☼☼");

        // TODO сделать как в других играх, чтобы инфа о плеерах тянулась с givenFl
        givenPlayer(1, 4);
        givenPlayer(2, 2);
        givenPlayer(3, 4);

        assert1("☼☼☼☼☼☼\n" +
                "☼► ( ☼\n" +
                "☼####☼\n" +
                "☼ ( $☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        assert2("☼☼☼☼☼☼\n" +
                "☼( ( ☼\n" +
                "☼####☼\n" +
                "☼ ► $☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        assert3("☼☼☼☼☼☼\n" +
                "☼( ► ☼\n" +
                "☼####☼\n" +
                "☼ ( $☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        hero(0).right();
        hero(1).left();
        hero(2).right();

        field.tick();

        assert1("☼☼☼☼☼☼\n" +
                "☼ ► (☼\n" +
                "☼####☼\n" +
                "☼)  $☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        assert2("☼☼☼☼☼☼\n" +
                "☼ ( (☼\n" +
                "☼####☼\n" +
                "☼◄  $☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        assert3("☼☼☼☼☼☼\n" +
                "☼ ( ►☼\n" +
                "☼####☼\n" +
                "☼)  $☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        hero(0).act();
        game(2).close();

        field.tick();

        assert1("☼☼☼☼☼☼\n" +
                "☼ R  ☼\n" +
                "☼##*#☼\n" +
                "☼)  $☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        assert2("☼☼☼☼☼☼\n" +
                "☼ ⌊  ☼\n" +
                "☼##*#☼\n" +
                "☼◄  $☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        try {
            assert3("☼☼☼☼☼☼\n" +
                    "☼ ⌊  ☼\n" +
                    "☼##*#☼\n" +
                    "☼)  $☼\n" +
                    "☼####☼\n" +
                    "☼☼☼☼☼☼\n");
        } catch (IllegalStateException e) {
            assertEquals("No board for this player", e.getMessage());
        }

        hero(0).right();

        field.tick();
        field.tick();
        field.tick();

        assert1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼## #☼\n" +
                "☼) ►$☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        assert2("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼## #☼\n" +
                "☼◄ ($☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        hero(0).left();
        hero(0).act();
        hero(1).right();

        field.tick();

        assert1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼## #☼\n" +
                "☼ ⊏Я$☼\n" +
                "☼#*##☼\n" +
                "☼☼☼☼☼☼\n");

        assert2("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼## #☼\n" +
                "☼ [⌋$☼\n" +
                "☼#*##☼\n" +
                "☼☼☼☼☼☼\n");

        for (int c = 2; c < Brick.DRILL_TIMER; c++) {
            field.tick();
        }

        field.tick();

        assert1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼####☼\n" +
                "☼  Я$☼\n" +
                "☼#Z##☼\n" +
                "☼☼☼☼☼☼\n");

        assert2("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼####☼\n" +
                "☼  ⌋$☼\n" +
                "☼#Ѡ##☼\n" +
                "☼☼☼☼☼☼\n");

        verify(listener(1)).event(Events.KILL_HERO);
        verify(listener(0)).event(Events.KILL_ENEMY);
        assertTrue(game(1).isGameOver());

        when(dice.next(anyInt())).thenReturn(1, 4);

        game(1).newGame();

        field.tick();

        assert1("☼☼☼☼☼☼\n" +
                "☼(   ☼\n" +
                "☼####☼\n" +
                "☼  Я$☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        assert2("☼☼☼☼☼☼\n" +
                "☼►   ☼\n" +
                "☼####☼\n" +
                "☼  ⌋$☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        hero(0).right();

        when(dice.next(anyInt())).thenReturn(1, 2);

        field.tick();

        assert1("☼☼☼☼☼☼\n" +
                "☼(   ☼\n" +
                "☼####☼\n" +
                "☼$  ►☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        assert2("☼☼☼☼☼☼\n" +
                "☼►   ☼\n" +
                "☼####☼\n" +
                "☼$  (☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        verify(listener(0)).event(Events.GET_YELLOW_GOLD);

    }

    private Hero hero(int index) {
        return (Hero) game(index).getPlayer().getHero();
    }

    private EventListener listener(int index) {
        return listeners.get(index);
    }

    private Game game(int index) {
        return games.get(index);
    }

    @Test
    public void thatEnemiesDoNotHauntShadowPlayers() {
        settings.integer(ENEMIES_COUNT, 1);
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  »   ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        settings.integer(SHADOW_PILLS_COUNT, 1);
        givenPlayer(5, 2)
                .getHero().pick(PillType.SHADOW_PILL);
        givenPlayer(1, 2);

        dice(0); // охотимся за первым игроком // TODO потестить когда поохотимся за вторым
        field.tick();

        assert1("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼(«  ⊳ ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void thatTwoShadowsWalkThroughEachOther() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        settings.integer(SHADOW_PILLS_COUNT, 1);
        givenPlayer(1, 2)
                .getHero().pick(PillType.SHADOW_PILL);
        givenPlayer(2, 2)
                .getHero().pick(PillType.SHADOW_PILL);

        hero(0).right();

        field.tick();

        assert1("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼ ⊳    ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        assert2("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼ ⊳    ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        verify(listener(0), never()).event(Events.KILL_ENEMY);
        verify(listener(1), never()).event(Events.KILL_HERO);
    }

    @Test
    public void thatShadowKillsNonShadowPlayer() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        settings.integer(SHADOW_PILLS_COUNT, 1);
        givenPlayer(1, 2)
                .getHero().pick(PillType.SHADOW_PILL);
        givenPlayer(2, 2);

        hero(0).right();

        field.tick();

        assert1("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼ ⊳    ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        assert2("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼ Ѡ    ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        verify(listener(0)).event(Events.KILL_ENEMY);
        verify(listener(1)).event(Events.KILL_HERO);

        field.tick();

        assert1("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼ ⊳    ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        assert2("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼ Ѡ    ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void thatShadowFallsAtTheRegularPlayerAndKillsHim() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        settings.integer(SHADOW_PILLS_COUNT, 1);
        givenPlayer(1, 3)
                .getHero().pick(PillType.SHADOW_PILL);
        givenPlayer(1, 2);

        assert1("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼⊄     ☼\n" +
                "☼(     ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        assert2("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼⋢     ☼\n" +
                "☼►     ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        dice(3, 3); // new pill
        field.tick();

        assert1("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼  S   ☼\n" +
                "☼⊳     ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        assert2("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼  S   ☼\n" +
                "☼Ѡ     ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        verify(listener(0)).event(Events.KILL_ENEMY);
        verify(listener(1)).event(Events.KILL_HERO);

        field.tick();

        assert1("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼  S   ☼\n" +
                "☼⊳     ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        assert2("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼  S   ☼\n" +
                "☼Ѡ     ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void thatShadowStairsUpTheLadderAtTheRegularPlayerAndKillsHim() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  H   ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        settings.integer(SHADOW_PILLS_COUNT, 1);
        givenPlayer(2, 2)
            .getHero().pick(PillType.SHADOW_PILL);
        givenPlayer(3, 3);

        assert1("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼  (   ☼\n" +
                "☼ ⊳H   ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        assert2("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼  ►   ☼\n" +
                "☼ ⋉H   ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        hero(0).right();
        field.tick();

        assert1("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼  (   ☼\n" +
                "☼  ⍬   ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        assert2("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼  ►   ☼\n" +
                "☼  ⋕   ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        hero(0).up();
        field.tick();

        assert1("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼  ⊳   ☼\n" +
                "☼  H   ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        assert2("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼  Ѡ   ☼\n" +
                "☼  H   ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        verify(listener(0)).event(Events.KILL_ENEMY);
        verify(listener(1)).event(Events.KILL_HERO);

        field.tick();

        assert1("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼  ⊳   ☼\n" +
                "☼  H   ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        assert2("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼  Ѡ   ☼\n" +
                "☼  H   ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");
    }

    // можно ли проходить героям друг через дурга? Нет
    @Test
    public void shouldICantGoViaAnotherPlayer_whenAtWay() {
        givenFl("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼####☼" +
                "☼☼☼☼☼☼");

        givenPlayer(1, 2);
        givenPlayer(2, 2);

        assert1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼►(  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        hero(0).right();
        field.tick();

        assert1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼►(  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        hero(1).left();
        field.tick();

        assert1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼►)  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldICantGoViaAnotherPlayer_whenAtLadder() {
        givenFl("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼ H  ☼" +
                "☼ H  ☼" +
                "☼####☼" +
                "☼☼☼☼☼☼");

        givenPlayer(1, 2);
        givenPlayer(2, 4);

        assert1("☼☼☼☼☼☼\n" +
                "☼ (  ☼\n" +
                "☼ H  ☼\n" +
                "☼►H  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        hero(0).right();
        hero(1).down();
        field.tick();

        assert1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ U  ☼\n" +
                "☼ Y  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        hero(0).up();
        hero(1).down();
        field.tick();

        assert1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ U  ☼\n" +
                "☼ Y  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        hero(0).up();
        hero(1).down();
        field.tick();

        assert1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ U  ☼\n" +
                "☼ Y  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldICantGoViaAnotherPlayer_whenAtPipe() {
        givenFl("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼ ~~ ☼" +
                "☼#  #☼" +
                "☼####☼" +
                "☼☼☼☼☼☼");

        givenPlayer(1, 3);
        givenPlayer(4, 3);

        assert1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼►~~(☼\n" +
                "☼#  #☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        hero(0).right();
        hero(1).left();
        field.tick();

        assert1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ }Э ☼\n" +
                "☼#  #☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        assert2("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ Є{ ☼\n" +
                "☼#  #☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        hero(0).right();
        hero(1).left();
        field.tick();

        assert1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ }Э ☼\n" +
                "☼#  #☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        hero(0).right();
        hero(1).left();
        field.tick();

        assert1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ }Э ☼\n" +
                "☼#  #☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");
    }

    // могу ли я сверлить под другим героем? Нет
    @Test
    public void shouldICantDrillUnderOtherHero() {
        givenFl("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼►►  ☼" +
                "☼####☼" +
                "☼☼☼☼☼☼");

        givenPlayer(1, 2);
        givenPlayer(2, 2);

        assert1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼►(  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        hero(0).act();
        hero(1).left();
        hero(1).act();
        field.tick();

        assert1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼►)  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");
    }

    // если я прыгаю сверху на героя, то я должен стоять у него на голове
    @Test
    public void shouldICanStayAtOtherHeroHead() {
        givenFl("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼####☼" +
                "☼☼☼☼☼☼");

        givenPlayer(2, 4);
        givenPlayer(2, 2);

        assert1("☼☼☼☼☼☼\n" +
                "☼ [  ☼\n" +
                "☼    ☼\n" +
                "☼ (  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        field.tick();

        assert1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ ►  ☼\n" +
                "☼ (  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        hero(0).down();  //и даже если я сильно захочу я не смогу впрыгнуть в него

        assert1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ ►  ☼\n" +
                "☼ (  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");
    }

    // если я прыгаю сверху на героя который на трубе, то я должен стоять у него на голове
    @Test
    public void shouldICantStayAtOtherHeroHeadWhenOnPipe() {
        givenFl("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼ ~  ☼" +
                "☼☼☼☼☼☼");

        givenPlayer(2, 4);
        givenPlayer(2, 2);
        field.tick();

        assert1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ [  ☼\n" +
                "☼    ☼\n" +
                "☼ Є  ☼\n" +
                "☼☼☼☼☼☼\n");

        field.tick();

        assert1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼ ►  ☼\n" +
                "☼ Є  ☼\n" +
                "☼☼☼☼☼☼\n");

        hero(0).down();
        field.tick();

        assert1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼ ►  ☼\n" +
                "☼ Є  ☼\n" +
                "☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldCanMoveWhenAtOtherHero() {
        shouldICantStayAtOtherHeroHeadWhenOnPipe();

        hero(1).left();
        field.tick();

        assert1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼ [  ☼\n" +
                "☼)~  ☼\n" +
                "☼☼☼☼☼☼\n");

        field.tick();

        assert1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼)}  ☼\n" +
                "☼☼☼☼☼☼\n");

        hero(1).right();  // нельзя входить в друг в друга :) даже на трубе
        field.tick();

        assert1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼(}  ☼\n" +
                "☼☼☼☼☼☼\n");

        hero(0).left();  // нельзя входить в друг в друга :) даже на трубе
        field.tick();

        assert1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼({  ☼\n" +
                "☼☼☼☼☼☼\n");
    }

    // когда два героя на трубе, они не могут друг в друга войти
    @Test
    public void shouldStopOnPipe() {
        givenFl("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼~~~~☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");

        givenPlayer(2, 4);
        givenPlayer(3, 4);
        field.tick();

        assert1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼~}Є~☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n");

        hero(1).left();
        field.tick();

        assert1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼~}Э~☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n");

        hero(0).right();
        field.tick();

        assert1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼~}Э~☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n");

        hero(0).right();
        hero(1).left();
        field.tick();

        assert1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼~}Э~☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n");
    }

    private void assert1(String expected) {
        assertBoard(expected, 0);
    }

    private void assertBoard(String expected, int index) {
        assertEquals(expected, game(index).getBoardAsString());
    }

    private void assert2(String expected) {
        assertBoard(expected, 1);
    }

    private void assert3(String expected) {
        assertBoard(expected, 2);
    }

    private Player givenPlayer(int x, int y) {
        EventListener listener = mock(EventListener.class);
        listeners.add(listener);
        Player player = new Player(listener, settings);
        players.add(player);
        Single game = new Single(player, printerFactory);
        games.add(game);
        game.on(field);
        dice(x, y);
        game.newGame();
        heroes.add(player.getHero());
        return player;
    }

    private void dice(int... ints) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int i : ints) {
            when = when.thenReturn(i);
        }
    }

    private void givenFl(String map) {
        Level level = new LevelImpl(map, dice);
        field = new Loderunner(level, dice, settings);
    }

    @Test
    public void shouldICanGoWhenIAmAtOtherHero() {
        shouldICanStayAtOtherHeroHead();

        assert1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ ►  ☼\n" +
                "☼ (  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        hero(0).left();
        field.tick();

        assert1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼]   ☼\n" +
                "☼ (  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        field.tick();

        assert1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼◄(  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldICanGoWhenAtMeIsOtherHero() {
        shouldICanStayAtOtherHeroHead();

        assert1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ ►  ☼\n" +
                "☼ (  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        hero(1).right();
        field.tick();

        assert1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ [  ☼\n" +
                "☼  ( ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        field.tick();

        assert1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼ ►( ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");
    }


    // тест с раундами: игра не начнется, пока не соберутся игроки
    // в достаточном количестве
    // TODO тест реально не тестирует этого, а просто эмулирует -
    //      возможно в будущем придумается фреймворк, который будет
    //      тестить эту фичу вглубь с rooms логикой которая сейчас на
    //      стороне сервера а могла бы быть в engine.
    @Test
    public void shouldStopGame_whenRoundIsNotStarted() {
        settings.bool(ROUNDS_ENABLED, true)
                .integer(ROUNDS_TIME_BEFORE_START, 1)
                .integer(ROUNDS_PER_MATCH, 1)
                .integer(ROUNDS_PLAYERS_PER_ROOM, 3);

        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        givenPlayer(1, 1);
        givenPlayer(3, 1);

        // when
        // юзеров недостаточно, ничего не происходит
        tick();

        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n");

        assert1("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼► (   ☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        assert2("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼( ►   ☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        // when
        // попытка переместиться
        hero(0).right();
        hero(1).right();

        tick();

        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n");

        assert1("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼► (   ☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        assert2("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼( ►   ☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        // when
        givenPlayer(5, 1);

        // вот а тут уже укомплектована комната - погнали!
        tick();

        events.verifyAllEvents(
                "listener(0) => [START_ROUND, [Round 1]]\n" +
                "listener(1) => [START_ROUND, [Round 1]]\n" +
                "listener(2) => [START_ROUND, [Round 1]]\n");

        assert1("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼► ( ( ☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        assert2("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼( ► ( ☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        assert3("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼( ( ► ☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        // when
        // попытка переместиться
        hero(0).right();
        hero(1).right();
        hero(2).right();

        tick();

        assert1("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼ ► ( (☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        assert2("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼ ( ► (☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        assert3("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼ ( ( ►☼\n" +
                "☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void winnerIsTheOneWhoBuriedTheMostPlayers() {
        settings.bool(ROUNDS_ENABLED, true)
                .integer(ROUNDS_TIME_BEFORE_START, 1)
                .integer(ROUNDS_PER_MATCH, 1)
                .integer(ROUNDS_TIME, 30)
                .integer(ROUNDS_TIME_FOR_WINNER, 5)
                .integer(ROUNDS_PLAYERS_PER_ROOM, 8);

        givenFl("☼☼☼☼☼☼☼☼☼☼" +
                "☼        ☼" +
                "☼###HH###☼" +
                "☼☼☼☼HH☼☼☼☼" +
                "☼   HH   ☼" +
                "☼###HH###☼" +
                "☼☼☼☼HH☼☼☼☼" +
                "☼   HH   ☼" +
                "☼###HH###☼" +
                "☼☼☼☼☼☼☼☼☼☼");

        givenPlayer(3, 2); // соревнующиеся ребята
        givenPlayer(6, 2);

        givenPlayer(1, 2); // 1 этаж
        givenPlayer(8, 2);

        givenPlayer(1, 5); // 2 этаж
        givenPlayer(8, 5);

        givenPlayer(1, 8); // 3 этаж
        givenPlayer(8, 8);

        tick();

        events.verifyAllEvents(
                "listener(0) => [START_ROUND, [Round 1]]\n" +
                "listener(1) => [START_ROUND, [Round 1]]\n" +
                "listener(2) => [START_ROUND, [Round 1]]\n" +
                "listener(3) => [START_ROUND, [Round 1]]\n" +
                "listener(4) => [START_ROUND, [Round 1]]\n" +
                "listener(5) => [START_ROUND, [Round 1]]\n" +
                "listener(6) => [START_ROUND, [Round 1]]\n" +
                "listener(7) => [START_ROUND, [Round 1]]\n");

        assert1("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼(      (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼(  HH  (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼( ►HH( (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        drill(2, 3);

        assert1("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼(      (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼(  HH  (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼ ⊏ЯHH⌊⊐ ☼\n" +
                "☼#*#HH#*#☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        goUp();

        assert1("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼(      (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼( ◄HH( (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#(#HH#)#☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        drill(4, 5);

        assert1("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼(      (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼ ⊏ЯHH⌊⊐ ☼\n" +
                "☼#*#HH#*#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#(#HH#)#☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        goUp();

        assertScores(0, 0);
        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n" +
                "listener(4) => []\n" +
                "listener(5) => []\n" +
                "listener(6) => []\n" +
                "listener(7) => []\n");

        tick();

        assertScores(1, 1);
        events.verifyAllEvents(
                "listener(0) => [KILL_ENEMY]\n" +
                "listener(1) => [KILL_ENEMY]\n" +
                "listener(2) => [KILL_HERO]\n" +
                "listener(3) => [KILL_HERO]\n" +
                "listener(4) => []\n" +
                "listener(5) => []\n" +
                "listener(6) => []\n" +
                "listener(7) => []\n");

        assert1("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼( ◄  ( (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#(#HH#)#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#Z#HH#Z#☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        tick();

        assert1("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼( ◄  ( (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#(#HH#)#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        drill(6, 7);

        assert1("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼ ⊏Я  ⌊⊐ ☼\n" +
                "☼#*#HH#*#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#(#HH#)#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        tick();

        assert1("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼  Я  ⌊  ☼\n" +
                "☼#(#HH#)#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#(#HH#)#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        tick();
        tick();

        assertScores(1, 1);
        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n" +
                "listener(4) => []\n" +
                "listener(5) => []\n" +
                "listener(6) => []\n" +
                "listener(7) => []\n");

        tick();

        assertScores(2, 2);
        events.verifyAllEvents(
                "listener(0) => [KILL_ENEMY]\n" +
                "listener(1) => [KILL_ENEMY]\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n" +
                "listener(4) => [KILL_HERO]\n" +
                "listener(5) => [KILL_HERO]\n" +
                "listener(6) => []\n" +
                "listener(7) => []\n");


        assert1("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼  Я  ⌊  ☼\n" +
                "☼#(#HH#)#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#Z#HH#Z#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        tick();

        assert1("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼  Я  ⌊  ☼\n" +
                "☼#(#HH#)#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        tick();
        tick();
        tick();
        tick();
        tick();
        tick();

        assertScores(2, 2);
        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n" +
                "listener(4) => []\n" +
                "listener(5) => []\n" +
                "listener(6) => []\n" +
                "listener(7) => []\n");

        tick();

        assertScores(3, 3);
        events.verifyAllEvents(
                "listener(0) => [KILL_ENEMY]\n" +
                "listener(1) => [KILL_ENEMY]\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n" +
                "listener(4) => []\n" +
                "listener(5) => []\n" +
                "listener(6) => [KILL_HERO]\n" +
                "listener(7) => [KILL_HERO]\n");

        assert1("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼  Я  ⌊  ☼\n" +
                "☼#Z#HH#Z#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        tick();

        assertScores(3, 3);
        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n" +
                "listener(4) => []\n" +
                "listener(5) => []\n" +
                "listener(6) => []\n" +
                "listener(7) => []\n");

        assert1("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼  Я  ⌊  ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        tick();

        assert1("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼  Я  ⌊  ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        tick();

        assertScores(0, 0);
        events.verifyAllEvents(
                "listener(0) => [WIN_ROUND]\n" +
                "listener(1) => [WIN_ROUND]\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n" +
                "listener(4) => []\n" +
                "listener(5) => []\n" +
                "listener(6) => []\n" +
                "listener(7) => []\n");

        assert1("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼  Ѡ  Z  ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        tick();

        assertScores(0, 0);
        assertEquals(0, field.players().size());
        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n" +
                "listener(4) => []\n" +
                "listener(5) => []\n" +
                "listener(6) => []\n" +
                "listener(7) => []\n");

        assert1("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼        ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        tick();

        assertScores(0, 0);
        assertEquals(0, field.players().size());
        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n" +
                "listener(4) => []\n" +
                "listener(5) => []\n" +
                "listener(6) => []\n" +
                "listener(7) => []\n");

        assert1("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼        ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");
    }

    public void assertScores(int score1, int score2) {
        assertEquals(score1, hero(0).scores());
        assertEquals(score2, hero(1).scores());
    }

    @Test
    public void twoWinnersIfTheyHaveEqualKillsBeforeTimeout_caseOneRounds() {
        settings.bool(ROUNDS_ENABLED, true)
                .integer(ROUNDS_TIME_BEFORE_START, 1)
                .integer(ROUNDS_PER_MATCH, 1)
                .integer(ROUNDS_TIME, 30)
                .integer(ROUNDS_TIME_FOR_WINNER, 5)
                .integer(ROUNDS_PLAYERS_PER_ROOM, 8);

        givenFl("☼☼☼☼☼☼☼☼☼☼" +
                "☼        ☼" +
                "☼###HH###☼" +
                "☼☼☼☼HH☼☼☼☼" +
                "☼   HH   ☼" +
                "☼###HH###☼" +
                "☼☼☼☼HH☼☼☼☼" +
                "☼   HH   ☼" +
                "☼###HH###☼" +
                "☼☼☼☼☼☼☼☼☼☼");

        givenPlayer(3, 2); // соревнующиеся ребята
        givenPlayer(6, 2);

        givenPlayer(1, 2); // 1 этаж
        givenPlayer(8, 2);

        givenPlayer(1, 5); // 2 этаж
        givenPlayer(8, 5);

        givenPlayer(1, 8); // 3 этаж
        givenPlayer(8, 8);

        tick();

        assertScores(0, 0);
        events.verifyAllEvents(
                "listener(0) => [START_ROUND, [Round 1]]\n" +
                "listener(1) => [START_ROUND, [Round 1]]\n" +
                "listener(2) => [START_ROUND, [Round 1]]\n" +
                "listener(3) => [START_ROUND, [Round 1]]\n" +
                "listener(4) => [START_ROUND, [Round 1]]\n" +
                "listener(5) => [START_ROUND, [Round 1]]\n" +
                "listener(6) => [START_ROUND, [Round 1]]\n" +
                "listener(7) => [START_ROUND, [Round 1]]\n");

        assert1("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼(      (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼(  HH  (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼( ►HH( (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        drill(2, 3);

        assert1("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼(      (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼(  HH  (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼ ⊏ЯHH⌊⊐ ☼\n" +
                "☼#*#HH#*#☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        goUp();

        assert1("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼(      (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼( ◄HH( (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#(#HH#)#☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        drill(4, 5);

        assert1("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼(      (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼ ⊏ЯHH⌊⊐ ☼\n" +
                "☼#*#HH#*#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#(#HH#)#☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        goUp();

        assertScores(0, 0);
        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n" +
                "listener(4) => []\n" +
                "listener(5) => []\n" +
                "listener(6) => []\n" +
                "listener(7) => []\n");

        tick();

        assertScores(1, 1);
        events.verifyAllEvents(
                "listener(0) => [KILL_ENEMY]\n" +
                "listener(1) => [KILL_ENEMY]\n" +
                "listener(2) => [KILL_HERO]\n" +
                "listener(3) => [KILL_HERO]\n" +
                "listener(4) => []\n" +
                "listener(5) => []\n" +
                "listener(6) => []\n" +
                "listener(7) => []\n");

        assert1("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼( ◄  ( (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#(#HH#)#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#Z#HH#Z#☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        tick();

        assert1("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼( ◄  ( (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#(#HH#)#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        drill(6, -1);

        assert1("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼ ⊏Я  ⌊ (☼\n" +
                "☼#*#HH#*#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#(#HH#)#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        tick();

        assert1("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼  Я  ⌊ (☼\n" +
                "☼#(#HH# #☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#(#HH#)#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        tick();
        tick();

        assertScores(1, 1);
        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n" +
                "listener(4) => []\n" +
                "listener(5) => []\n" +
                "listener(6) => []\n" +
                "listener(7) => []\n");

        tick();

        assertScores(2, 2);
        events.verifyAllEvents(
                "listener(0) => [KILL_ENEMY]\n" +
                "listener(1) => [KILL_ENEMY]\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n" +
                "listener(4) => [KILL_HERO]\n" +
                "listener(5) => [KILL_HERO]\n" +
                "listener(6) => []\n" +
                "listener(7) => []\n");

        assert1("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼  Я  ⌊ (☼\n" +
                "☼#(#HH# #☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#Z#HH#Z#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        tick();

        assert1("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼  Я  ⌊ (☼\n" +
                "☼#(#HH# #☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        tick();
        tick();
        tick();
        tick();
        tick();
        tick();

        assertScores(2, 2);
        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n" +
                "listener(4) => []\n" +
                "listener(5) => []\n" +
                "listener(6) => []\n" +
                "listener(7) => []\n");

        tick();

        assertScores(3, 2);
        events.verifyAllEvents(
                "listener(0) => [KILL_ENEMY]\n" +
                "listener(1) => []\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n" +
                "listener(4) => []\n" +
                "listener(5) => []\n" +
                "listener(6) => [KILL_HERO]\n" +
                "listener(7) => []\n");

        assert1("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼  Я  ⌊ (☼\n" +
                "☼#Z#HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        tick();

        assertScores(3, 2);
        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n" +
                "listener(4) => []\n" +
                "listener(5) => []\n" +
                "listener(6) => []\n" +
                "listener(7) => []\n");

        assert1("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼  Я  ⌊ (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        tick();

        assertScores(3, 2);

        tick();

        assertScores(0, 2);    // TODO тут надо чистить так же очки hero когда ему приходит [Time is over]
        events.verifyAllEvents(
                "listener(0) => [WIN_ROUND]\n" +
                "listener(1) => [[Time is over]]\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n" +
                "listener(4) => []\n" +
                "listener(5) => []\n" +
                "listener(6) => []\n" +
                "listener(7) => [[Time is over]]\n");

        assert1("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼  Ѡ  Z Z☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        tick();

        assertScores(0, 2);
        assertEquals(0, field.players().size());
        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n" +
                "listener(4) => []\n" +
                "listener(5) => []\n" +
                "listener(6) => []\n" +
                "listener(7) => []\n");

        assert1("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼        ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");
    }
    
    @Test
    public void twoWinnersIfTheyHaveEqualKillsBeforeTimeout_caseTwoRounds() {
        settings.bool(ROUNDS_ENABLED, true)
                .integer(ROUNDS_TIME_BEFORE_START, 1)
                .integer(ROUNDS_PER_MATCH, 2)
                .integer(ROUNDS_TIME, 30)
                .integer(ROUNDS_TIME_FOR_WINNER, 5)
                .integer(ROUNDS_PLAYERS_PER_ROOM, 8);

        givenFl("☼☼☼☼☼☼☼☼☼☼" +
                "☼        ☼" +
                "☼###HH###☼" +
                "☼☼☼☼HH☼☼☼☼" +
                "☼   HH   ☼" +
                "☼###HH###☼" +
                "☼☼☼☼HH☼☼☼☼" +
                "☼   HH   ☼" +
                "☼###HH###☼" +
                "☼☼☼☼☼☼☼☼☼☼");

        givenPlayer(3, 2); // соревнующиеся ребята
        givenPlayer(6, 2);

        givenPlayer(1, 2); // 1 этаж
        givenPlayer(8, 2);

        givenPlayer(1, 5); // 2 этаж
        givenPlayer(8, 5);

        givenPlayer(1, 8); // 3 этаж
        givenPlayer(8, 8);

        tick();

        assertScores(0, 0);
        events.verifyAllEvents(
                "listener(0) => [START_ROUND, [Round 1]]\n" +
                "listener(1) => [START_ROUND, [Round 1]]\n" +
                "listener(2) => [START_ROUND, [Round 1]]\n" +
                "listener(3) => [START_ROUND, [Round 1]]\n" +
                "listener(4) => [START_ROUND, [Round 1]]\n" +
                "listener(5) => [START_ROUND, [Round 1]]\n" +
                "listener(6) => [START_ROUND, [Round 1]]\n" +
                "listener(7) => [START_ROUND, [Round 1]]\n");

        assert1("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼(      (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼(  HH  (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼( ►HH( (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        drill(2, 3);

        assert1("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼(      (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼(  HH  (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼ ⊏ЯHH⌊⊐ ☼\n" +
                "☼#*#HH#*#☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        goUp();

        assert1("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼(      (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼( ◄HH( (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#(#HH#)#☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        drill(4, 5);

        assert1("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼(      (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼ ⊏ЯHH⌊⊐ ☼\n" +
                "☼#*#HH#*#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#(#HH#)#☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        goUp();

        assertScores(0, 0);
        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n" +
                "listener(4) => []\n" +
                "listener(5) => []\n" +
                "listener(6) => []\n" +
                "listener(7) => []\n");

        tick();

        assertScores(1, 1);
        events.verifyAllEvents(
                "listener(0) => [KILL_ENEMY]\n" +
                "listener(1) => [KILL_ENEMY]\n" +
                "listener(2) => [KILL_HERO]\n" +
                "listener(3) => [KILL_HERO]\n" +
                "listener(4) => []\n" +
                "listener(5) => []\n" +
                "listener(6) => []\n" +
                "listener(7) => []\n");

        assert1("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼( ◄  ( (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#(#HH#)#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#Z#HH#Z#☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        tick();

        assert1("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼( ◄  ( (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#(#HH#)#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        drill(6, -1);

        assert1("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼ ⊏Я  ⌊ (☼\n" +
                "☼#*#HH#*#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#(#HH#)#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        tick();

        assert1("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼  Я  ⌊ (☼\n" +
                "☼#(#HH# #☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#(#HH#)#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        tick();
        tick();

        assertScores(1, 1);
        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n" +
                "listener(4) => []\n" +
                "listener(5) => []\n" +
                "listener(6) => []\n" +
                "listener(7) => []\n");

        tick();

        assertScores(2, 2);
        events.verifyAllEvents(
                "listener(0) => [KILL_ENEMY]\n" +
                "listener(1) => [KILL_ENEMY]\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n" +
                "listener(4) => [KILL_HERO]\n" +
                "listener(5) => [KILL_HERO]\n" +
                "listener(6) => []\n" +
                "listener(7) => []\n");

        assert1("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼  Я  ⌊ (☼\n" +
                "☼#(#HH# #☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#Z#HH#Z#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        tick();

        assert1("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼  Я  ⌊ (☼\n" +
                "☼#(#HH# #☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        tick();
        tick();
        tick();
        tick();
        tick();
        tick();

        assertScores(2, 2);
        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n" +
                "listener(4) => []\n" +
                "listener(5) => []\n" +
                "listener(6) => []\n" +
                "listener(7) => []\n");

        tick();

        assertScores(3, 2);
        events.verifyAllEvents(
                "listener(0) => [KILL_ENEMY]\n" +
                "listener(1) => []\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n" +
                "listener(4) => []\n" +
                "listener(5) => []\n" +
                "listener(6) => [KILL_HERO]\n" +
                "listener(7) => []\n");

        assert1("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼  Я  ⌊ (☼\n" +
                "☼#Z#HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        tick();

        assertScores(3, 2);
        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n" +
                "listener(4) => []\n" +
                "listener(5) => []\n" +
                "listener(6) => []\n" +
                "listener(7) => []\n");

        assert1("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼  Я  ⌊ (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        tick();

        assertScores(3, 2);

        dice(3, 2,
            6, 2,
            8, 2);

        tick();

        assertScores(0, 0);
        events.verifyAllEvents(
                "listener(0) => [WIN_ROUND]\n" +
                "listener(1) => [[Time is over]]\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n" +
                "listener(4) => []\n" +
                "listener(5) => []\n" +
                "listener(6) => []\n" +
                "listener(7) => [[Time is over]]\n");

        assert1("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼        ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼  ►HH( (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        tick();

        assertScores(0, 0);
        events.verifyAllEvents(
                "listener(0) => [START_ROUND, [Round 2]]\n" +
                "listener(1) => [START_ROUND, [Round 2]]\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n" +
                "listener(4) => []\n" +
                "listener(5) => []\n" +
                "listener(6) => []\n" +
                "listener(7) => [START_ROUND, [Round 2]]\n");

        assert1("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼        ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼  ►HH( (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        tick();

        assertScores(0, 0);
        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n" +
                "listener(4) => []\n" +
                "listener(5) => []\n" +
                "listener(6) => []\n" +
                "listener(7) => []\n");

        assert1("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼        ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼  ►HH( (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");
    }

    public void goUp() {
        // идут на следующий этаж
        hero(0).right();
        hero(1).left();
        tick();

        hero(0).up();
        hero(1).up();
        tick();

        hero(0).up();
        hero(1).up();
        tick();

        hero(0).up();
        hero(1).up();
        tick();

        hero(0).left();
        hero(1).right();
        tick();
    }

    public void drill(int left, int right) {
        // сверлят ямки
        hero(0).left();
        hero(0).act();
        hero(1).right();
        hero(1).act();
        // падают в ямки
        if (left != -1) {
            hero(left).right();
        }
        if (right != -1) {
            hero(right).left();
        }
        tick();
    }

    public void tick() {
        events.verifyNoEvents();
        removeAllDied();
        // эмуляция проверки загрузки комнаты, если комната недогружена то не тикаем
        // вообше это делает фреймворк, тут лишь эмулируем
        if (settings.bool(ROUNDS_ENABLED)) {
            if (settings.integer(ROUNDS_PLAYERS_PER_ROOM) != heroes.size()) {
                return;
            }
        }
        field.tick();
    }

    // TODO тут дублирование с bomberman, может продумать единую архитектуру
    //      тестов работающую и для rounds и реализовать во всех играх начиная
    //      с bomberman, loderunnes, snakebattle и battlecity
    protected void removeAllDied() {
        players.forEach(player -> {
            if (!player.isAlive()) {
                field.remove(player(players.indexOf(player)));
            }
        });
    }

    protected Player player(int index) {
        return players.get(index);
    }

    protected void resetHeroes() {
        heroes.clear();
        players.forEach(player -> heroes.add(player.getHero()));
    }
}
