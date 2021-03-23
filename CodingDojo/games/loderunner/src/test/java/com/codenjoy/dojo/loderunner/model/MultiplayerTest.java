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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class MultiplayerTest {

    private Dice dice;
    private List<EventListener> listeners = new LinkedList<>();
    private List<Game> games = new LinkedList<>();
    private Loderunner field;
    private PrinterFactory printerFactory;
    private GameSettings settings;

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

    private Joystick hero(int index) {
        return game(index).getPlayer().getHero();
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
        Single game = new Single(player, printerFactory);
        games.add(game);
        game.on(field);
        dice(x, y);
        game.newGame();
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
}
