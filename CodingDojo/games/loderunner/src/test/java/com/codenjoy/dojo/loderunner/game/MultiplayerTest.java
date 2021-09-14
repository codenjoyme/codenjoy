package com.codenjoy.dojo.loderunner.game;

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


import com.codenjoy.dojo.loderunner.model.items.Brick;
import com.codenjoy.dojo.loderunner.model.items.Potion.PotionType;
import com.codenjoy.dojo.loderunner.services.Events;
import org.junit.Test;

import static com.codenjoy.dojo.loderunner.services.GameSettings.Keys.ROBBERS_COUNT;
import static com.codenjoy.dojo.loderunner.services.GameSettings.Keys.MASK_POTIONS_COUNT;
import static com.codenjoy.dojo.services.round.RoundSettings.Keys.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class MultiplayerTest extends AbstractGameTest {

    // появляется другие игроки, игра становится мультипользовательской
    @Test
    public void shouldMultipleGame() { // TODO разделить тест на части
        givenFl("☼☼☼☼☼☼\n" +
                "☼► ► ☼\n" +
                "☼####☼\n" +
                "☼ ► $☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        assertF("☼☼☼☼☼☼\n" +
                "☼► ( ☼\n" +
                "☼####☼\n" +
                "☼ ( $☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n", 0);

        assertF("☼☼☼☼☼☼\n" +
                "☼( ► ☼\n" +
                "☼####☼\n" +
                "☼ ( $☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n", 1);

        assertF("☼☼☼☼☼☼\n" +
                "☼( ( ☼\n" +
                "☼####☼\n" +
                "☼ ► $☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n", 2);

        hero(0).right();
        hero(1).right();
        hero(2).left();

        tick();

        assertF("☼☼☼☼☼☼\n" +
                "☼ ► (☼\n" +
                "☼####☼\n" +
                "☼)  $☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n", 0);

        assertF("☼☼☼☼☼☼\n" +
                "☼ ( ►☼\n" +
                "☼####☼\n" +
                "☼)  $☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n", 1);

        assertF("☼☼☼☼☼☼\n" +
                "☼ ( (☼\n" +
                "☼####☼\n" +
                "☼◄  $☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n", 2);

        hero(0).act();
        game(1).close();

        tick();

        assertF("☼☼☼☼☼☼\n" +
                "☼ R  ☼\n" +
                "☼##*#☼\n" +
                "☼)  $☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n", 0);

        assertF("☼☼☼☼☼☼\n" +
                "☼ ⌊  ☼\n" +
                "☼##*#☼\n" +
                "☼◄  $☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n", 2);

        try {
            assertF("☼☼☼☼☼☼\n" +
                    "☼ ⌊  ☼\n" +
                    "☼##*#☼\n" +
                    "☼)  $☼\n" +
                    "☼####☼\n" +
                    "☼☼☼☼☼☼\n", 1);
        } catch (IllegalStateException e) {
            assertEquals("No board for this player", e.getMessage());
        }

        hero(0).right();

        tick();
        tick();
        tick();

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼## #☼\n" +
                "☼) ►$☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n", 0);

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼## #☼\n" +
                "☼◄ ($☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n", 2);

        hero(0).left();
        hero(0).act();
        hero(2).right();

        tick();

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼## #☼\n" +
                "☼ ⊏Я$☼\n" +
                "☼#*##☼\n" +
                "☼☼☼☼☼☼\n", 0);

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼## #☼\n" +
                "☼ [⌋$☼\n" +
                "☼#*##☼\n" +
                "☼☼☼☼☼☼\n", 2);

        for (int c = 2; c < Brick.CRACK_TIMER; c++) {
            tick();
        }

        tick();

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼####☼\n" +
                "☼  Я$☼\n" +
                "☼#Z##☼\n" +
                "☼☼☼☼☼☼\n", 0);

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼####☼\n" +
                "☼  ⌋$☼\n" +
                "☼#Ѡ##☼\n" +
                "☼☼☼☼☼☼\n", 2);

        events.verifyAllEvents(
                "listener(0) => [KILL_ROBBER]\n" +
                "listener(1) => [KILL_HERO]\n" +
                "listener(2) => [KILL_HERO]\n");
        assertEquals(true, game(1).isGameOver());

        when(dice.next(anyInt())).thenReturn(1, 4);

        game(2).newGame();

        tick();

        assertF("☼☼☼☼☼☼\n" +
                "☼(   ☼\n" +
                "☼####☼\n" +
                "☼  Я$☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n", 0);

        assertF("☼☼☼☼☼☼\n" +
                "☼►   ☼\n" +
                "☼####☼\n" +
                "☼  ⌋$☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n", 2);

        hero(0).right();

        when(dice.next(anyInt())).thenReturn(1, 2);

        tick();

        assertF("☼☼☼☼☼☼\n" +
                "☼(   ☼\n" +
                "☼####☼\n" +
                "☼$  ►☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n", 0);

        assertF("☼☼☼☼☼☼\n" +
                "☼►   ☼\n" +
                "☼####☼\n" +
                "☼$  (☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n", 2);

        events.verifyAllEvents(
                "listener(0) => [GET_KNIFE_CLUE]\n" +
                "listener(1) => []\n" +
                "listener(2) => []\n");
    }

    @Test
    public void thatRobbersDoNotHauntMaskPlayers() {
        settings.integer(ROBBERS_COUNT, 1)
                .integer(MASK_POTIONS_COUNT, 1);
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼► » ► ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        robber().disableMock();
        hero(1).pick(PotionType.MASK_POTION);

        dice(0); // охотимся за первым игроком // TODO потестить когда поохотимся за вторым
        tick();

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼(«  ⊳ ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n", 1);
    }

    @Test
    public void thatTwoMasksWalkThroughEachOther() {
        settings.integer(MASK_POTIONS_COUNT, 1);
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼►►    ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        hero(0).pick(PotionType.MASK_POTION);
        hero(1).pick(PotionType.MASK_POTION);

        hero(0).right();

        tick();

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼ ⊳    ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n", 0);

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼ ⊳    ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n", 1);

        verify(listener(0), never()).event(Events.KILL_ROBBER);
        verify(listener(1), never()).event(Events.KILL_HERO);
    }

    @Test
    public void thatMaskKillsNonMaskPlayer() {
        settings.integer(MASK_POTIONS_COUNT, 1);
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼►►    ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        hero().pick(PotionType.MASK_POTION);

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼⊳(    ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n", 0);

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼⋉►    ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n", 1);

        hero().right();

        tick();

        events.verifyAllEvents(
                "listener(0) => [KILL_ROBBER]\n" +
                "listener(1) => [KILL_HERO]\n");

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼ ⊳    ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n", 0);

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼ Ѡ    ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n", 1);

        field.remove(player(1)); // он геймовер его уберут
        tick();

        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n");

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼ ⊳    ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n", 0);

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼ Ѡ    ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n", 1);
    }

    @Test
    public void thatMaskFallsAtTheRegularPlayerAndKillsHim() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼►     ☼" +
                "☼►     ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        settings.integer(MASK_POTIONS_COUNT, 1);
        hero().pick(PotionType.MASK_POTION);

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼⊄     ☼\n" +
                "☼(     ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n", 0);

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼⋢     ☼\n" +
                "☼►     ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n", 1);

        dice(3, 3); // new potion
        tick();

        events.verifyAllEvents(
                "listener(0) => [KILL_ROBBER]\n" +
                "listener(1) => [KILL_HERO]\n");

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼  S   ☼\n" +
                "☼⊳     ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n", 0);

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼  S   ☼\n" +
                "☼Ѡ     ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n", 1);

        field.remove(player(1)); // он геймовер его уберут
        tick();

        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n");

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼  S   ☼\n" +
                "☼⊳     ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n", 0);

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼  S   ☼\n" +
                "☼Ѡ     ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n", 1);
    }

    @Test
    public void thatMaskStairsUpTheLadderAtTheRegularPlayerAndKillsHim() {
        settings.integer(MASK_POTIONS_COUNT, 1);
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ►   ☼" +
                "☼ ►H   ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        hero(1).pick(PotionType.MASK_POTION);

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼  ►   ☼\n" +
                "☼ ⋉H   ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n", 0);

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼  (   ☼\n" +
                "☼ ⊳H   ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n", 1);

        hero(1).right();
        tick();

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼  ►   ☼\n" +
                "☼  ⋕   ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n", 0);

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼  (   ☼\n" +
                "☼  ⍬   ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n", 1);

        hero(1).up();
        tick();

        events.verifyAllEvents(
                "listener(0) => [KILL_HERO]\n" +
                        "listener(1) => [KILL_ROBBER]\n");

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼  Ѡ   ☼\n" +
                "☼  H   ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n", 0);

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼  ⊳   ☼\n" +
                "☼  H   ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n", 1);

        field.remove(player(0)); // он геймовер его уберут
        tick();

        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n");

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼  Ѡ   ☼\n" +
                "☼  H   ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n", 0);

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼  ⊳   ☼\n" +
                "☼  H   ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n", 1);
    }

    // можно ли проходить героям друг через дурга? Нет
    @Test
    public void shouldICantGoViaAnotherPlayer_whenAtWay() {
        givenFl("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼►►  ☼" +
                "☼####☼" +
                "☼☼☼☼☼☼");

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼►(  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n", 0);

        hero(0).right();
        tick();

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼►(  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n", 0);

        hero(1).left();
        tick();

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼►)  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n", 0);
    }

    @Test
    public void shouldICantGoViaAnotherPlayer_whenAtLadder() {
        givenFl("☼☼☼☼☼☼" +
                "☼ ►  ☼" +
                "☼ H  ☼" +
                "☼►H  ☼" +
                "☼####☼" +
                "☼☼☼☼☼☼");

        assertF("☼☼☼☼☼☼\n" +
                "☼ (  ☼\n" +
                "☼ H  ☼\n" +
                "☼►H  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n", 1);

        hero(0).down();
        hero(1).right();
        tick();

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ U  ☼\n" +
                "☼ Y  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n", 1);

        hero(0).down();
        hero(1).up();
        tick();

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ U  ☼\n" +
                "☼ Y  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n", 1);

        hero(0).down();
        hero(1).up();
        tick();

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ U  ☼\n" +
                "☼ Y  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n", 1);
    }

    @Test
    public void shouldICantGoViaAnotherPlayer_whenAtPipe() {
        givenFl("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼►~~►☼" +
                "☼#  #☼" +
                "☼####☼" +
                "☼☼☼☼☼☼");

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼►~~(☼\n" +
                "☼#  #☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n", 0);

        hero(0).right();
        hero(1).left();
        tick();

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ }Э ☼\n" +
                "☼#  #☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n", 0);

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ Є{ ☼\n" +
                "☼#  #☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n", 1);

        hero(0).right();
        hero(1).left();
        tick();

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ }Э ☼\n" +
                "☼#  #☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n", 0);

        hero(0).right();
        hero(1).left();
        tick();

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ }Э ☼\n" +
                "☼#  #☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n", 0);
    }

    // могу ли я прострелить под другим героем? Нет
    @Test
    public void shouldICantCrackUnderOtherHero() {
        givenFl("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼►►  ☼" +
                "☼####☼" +
                "☼☼☼☼☼☼");

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼►(  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n", 0);

        hero(0).act();
        hero(1).left();
        hero(1).act();
        tick();

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼►)  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n", 0);
    }

    // если я прыгаю сверху на героя, то я должен стоять у него на голове
    @Test
    public void shouldICanStayAtOtherHeroHead() {
        givenFl("☼☼☼☼☼☼" +
                "☼ ►  ☼" +
                "☼    ☼" +
                "☼ ►  ☼" +
                "☼####☼" +
                "☼☼☼☼☼☼");

        assertF("☼☼☼☼☼☼\n" +
                "☼ [  ☼\n" +
                "☼    ☼\n" +
                "☼ (  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n", 0);

        tick();

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ ►  ☼\n" +
                "☼ (  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n", 0);

        hero().down();  //и даже если я сильно захочу я не смогу впрыгнуть в него

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ ►  ☼\n" +
                "☼ (  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n", 0);
    }

    // если я прыгаю сверху на героя который на трубе, то я должен стоять у него на голове
    @Test
    public void shouldICantStayAtOtherHeroHeadWhenOnPipe() {
        givenFl("☼☼☼☼☼☼" +
                "☼ ►  ☼" +
                "☼    ☼" +
                "☼ ►  ☼" +
                "☼ ~  ☼" +
                "☼☼☼☼☼☼");

        tick();

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ [  ☼\n" +
                "☼    ☼\n" +
                "☼ Є  ☼\n" +
                "☼☼☼☼☼☼\n", 0);

        tick();

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼ ►  ☼\n" +
                "☼ Є  ☼\n" +
                "☼☼☼☼☼☼\n", 0);

        hero().down();
        tick();

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼ ►  ☼\n" +
                "☼ Є  ☼\n" +
                "☼☼☼☼☼☼\n", 0);
    }

    @Test
    public void shouldCanMoveWhenAtOtherHero() {
        shouldICantStayAtOtherHeroHeadWhenOnPipe();

        hero(1).left();
        tick();

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼ [  ☼\n" +
                "☼)~  ☼\n" +
                "☼☼☼☼☼☼\n", 0);

        tick();

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼)}  ☼\n" +
                "☼☼☼☼☼☼\n", 0);

        hero(1).right();  // нельзя входить в друг в друга :) даже на трубе
        tick();

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼(}  ☼\n" +
                "☼☼☼☼☼☼\n", 0);

        hero(0).left();  // нельзя входить в друг в друга :) даже на трубе
        tick();

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼({  ☼\n" +
                "☼☼☼☼☼☼\n", 0);
    }

    // когда два героя на трубе, они не могут друг в друга войти
    @Test
    public void shouldStopOnPipe() {
        givenFl("☼☼☼☼☼☼" +
                "☼ ►► ☼" +
                "☼~~~~☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");

        tick();

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼~}Є~☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n", 0);

        hero(1).left();
        tick();

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼~}Э~☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n", 0);

        hero(0).right();
        tick();

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼~}Э~☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n", 0);

        hero(0).right();
        hero(1).left();
        tick();

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼~}Э~☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n", 0);
    }

    @Test
    public void shouldICanGoWhenIAmAtOtherHero() {
        shouldICanStayAtOtherHeroHead();

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ ►  ☼\n" +
                "☼ (  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n", 0);

        hero().left();
        tick();

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼]   ☼\n" +
                "☼ (  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n", 0);

        tick();

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼◄(  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n", 0);
    }

    @Test
    public void shouldICanGoWhenAtMeIsOtherHero() {
        shouldICanStayAtOtherHeroHead();

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ ►  ☼\n" +
                "☼ (  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n", 0);

        hero(1).right();
        tick();

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ [  ☼\n" +
                "☼  ( ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n", 0);

        tick();

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼ ►( ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n", 0);
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
                "☼► ►   ☼" +
                "☼☼☼☼☼☼☼☼");

        // when
        // юзеров недостаточно, ничего не происходит
        tick();

        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n");

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼Ѡ Z   ☼\n" +
                "☼☼☼☼☼☼☼☼\n", 0);
        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼Z Ѡ   ☼\n" +
                "☼☼☼☼☼☼☼☼\n", 1);

        // when
        // попытка переместиться
        hero(0).right();
        hero(1).right();

        tick();

        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n");

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼Ѡ Z   ☼\n" +
                "☼☼☼☼☼☼☼☼\n", 0);
        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼Z Ѡ   ☼\n" +
                "☼☼☼☼☼☼☼☼\n", 1);

        // when
        givenPlayer(5, 1);

        // вот а тут уже укомплектована комната - погнали!
        tick();

        events.verifyAllEvents(
                "listener(0) => [START_ROUND, [Round 1]]\n" +
                "listener(1) => [START_ROUND, [Round 1]]\n" +
                "listener(2) => [START_ROUND, [Round 1]]\n");

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼► ( ( ☼\n" +
                "☼☼☼☼☼☼☼☼\n", 0);
        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼( ► ( ☼\n" +
                "☼☼☼☼☼☼☼☼\n", 1);
        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼( ( ► ☼\n" +
                "☼☼☼☼☼☼☼☼\n", 2);

        // when
        // попытка переместиться
        hero(0).right();
        hero(1).right();
        hero(2).right();

        tick();

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼ ► ( (☼\n" +
                "☼☼☼☼☼☼☼☼\n", 0);

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼ ( ► (☼\n" +
                "☼☼☼☼☼☼☼☼\n", 1);

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼ ( ( ►☼\n" +
                "☼☼☼☼☼☼☼☼\n", 2);
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

        assertF("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼(      (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼(  HH  (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼( ►HH( (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n", 0);

        crack(2, 3);

        assertF("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼(      (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼(  HH  (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼ ⊏ЯHH⌊⊐ ☼\n" +
                "☼#*#HH#*#☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n", 0);

        goUp();

        assertF("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼(      (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼( ◄HH( (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#(#HH#)#☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n", 0);

        crack(4, 5);

        assertF("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼(      (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼ ⊏ЯHH⌊⊐ ☼\n" +
                "☼#*#HH#*#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#(#HH#)#☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n", 0);

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
                "listener(0) => [KILL_ROBBER]\n" +
                "listener(1) => [KILL_ROBBER]\n" +
                "listener(2) => [KILL_HERO]\n" +
                "listener(3) => [KILL_HERO]\n" +
                "listener(4) => []\n" +
                "listener(5) => []\n" +
                "listener(6) => []\n" +
                "listener(7) => []\n");

        assertF("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼( ◄  ( (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#(#HH#)#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#Z#HH#Z#☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n", 0);

        tick();

        assertF("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼( ◄  ( (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#(#HH#)#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n", 0);

        crack(6, 7);

        assertF("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼ ⊏Я  ⌊⊐ ☼\n" +
                "☼#*#HH#*#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#(#HH#)#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n", 0);

        tick();

        assertF("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼  Я  ⌊  ☼\n" +
                "☼#(#HH#)#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#(#HH#)#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n", 0);

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
                "listener(0) => [KILL_ROBBER]\n" +
                "listener(1) => [KILL_ROBBER]\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n" +
                "listener(4) => [KILL_HERO]\n" +
                "listener(5) => [KILL_HERO]\n" +
                "listener(6) => []\n" +
                "listener(7) => []\n");


        assertF("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼  Я  ⌊  ☼\n" +
                "☼#(#HH#)#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#Z#HH#Z#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n", 0);

        tick();

        assertF("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼  Я  ⌊  ☼\n" +
                "☼#(#HH#)#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n", 0);

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
                "listener(0) => [KILL_ROBBER]\n" +
                "listener(1) => [KILL_ROBBER]\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n" +
                "listener(4) => []\n" +
                "listener(5) => []\n" +
                "listener(6) => [KILL_HERO]\n" +
                "listener(7) => [KILL_HERO]\n");

        assertF("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼  Я  ⌊  ☼\n" +
                "☼#Z#HH#Z#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n", 0);

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

        assertF("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼  Я  ⌊  ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n", 0);

        tick();

        assertF("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼  Я  ⌊  ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n", 0);

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

        assertF("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼  Ѡ  Z  ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n", 0);

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

        assertF("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼        ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n", 0);

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

        assertF("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼        ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n", 0);
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

        assertF("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼(      (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼(  HH  (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼( ►HH( (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n", 0);

        crack(2, 3);

        assertF("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼(      (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼(  HH  (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼ ⊏ЯHH⌊⊐ ☼\n" +
                "☼#*#HH#*#☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n", 0);

        goUp();

        assertF("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼(      (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼( ◄HH( (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#(#HH#)#☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n", 0);

        crack(4, 5);

        assertF("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼(      (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼ ⊏ЯHH⌊⊐ ☼\n" +
                "☼#*#HH#*#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#(#HH#)#☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n", 0);

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
                "listener(0) => [KILL_ROBBER]\n" +
                "listener(1) => [KILL_ROBBER]\n" +
                "listener(2) => [KILL_HERO]\n" +
                "listener(3) => [KILL_HERO]\n" +
                "listener(4) => []\n" +
                "listener(5) => []\n" +
                "listener(6) => []\n" +
                "listener(7) => []\n");

        assertF("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼( ◄  ( (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#(#HH#)#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#Z#HH#Z#☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n", 0);

        tick();

        assertF("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼( ◄  ( (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#(#HH#)#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n", 0);

        crack(6, -1);

        assertF("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼ ⊏Я  ⌊ (☼\n" +
                "☼#*#HH#*#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#(#HH#)#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n", 0);

        tick();

        assertF("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼  Я  ⌊ (☼\n" +
                "☼#(#HH# #☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#(#HH#)#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n", 0);

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
                "listener(0) => [KILL_ROBBER]\n" +
                "listener(1) => [KILL_ROBBER]\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n" +
                "listener(4) => [KILL_HERO]\n" +
                "listener(5) => [KILL_HERO]\n" +
                "listener(6) => []\n" +
                "listener(7) => []\n");

        assertF("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼  Я  ⌊ (☼\n" +
                "☼#(#HH# #☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#Z#HH#Z#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n", 0);

        tick();

        assertF("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼  Я  ⌊ (☼\n" +
                "☼#(#HH# #☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n", 0);

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
                "listener(0) => [KILL_ROBBER]\n" +
                "listener(1) => []\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n" +
                "listener(4) => []\n" +
                "listener(5) => []\n" +
                "listener(6) => [KILL_HERO]\n" +
                "listener(7) => []\n");

        assertF("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼  Я  ⌊ (☼\n" +
                "☼#Z#HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n", 0);

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

        assertF("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼  Я  ⌊ (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n", 0);

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

        assertF("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼  Ѡ  Z Z☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n", 0);

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

        assertF("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼        ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n", 0);
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

        assertF("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼(      (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼(  HH  (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼( ►HH( (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n", 0);

        crack(2, 3);

        assertF("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼(      (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼(  HH  (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼ ⊏ЯHH⌊⊐ ☼\n" +
                "☼#*#HH#*#☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n", 0);

        goUp();

        assertF("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼(      (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼( ◄HH( (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#(#HH#)#☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n", 0);

        crack(4, 5);

        assertF("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼(      (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼ ⊏ЯHH⌊⊐ ☼\n" +
                "☼#*#HH#*#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#(#HH#)#☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n", 0);

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
                "listener(0) => [KILL_ROBBER]\n" +
                "listener(1) => [KILL_ROBBER]\n" +
                "listener(2) => [KILL_HERO]\n" +
                "listener(3) => [KILL_HERO]\n" +
                "listener(4) => []\n" +
                "listener(5) => []\n" +
                "listener(6) => []\n" +
                "listener(7) => []\n");

        assertF("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼( ◄  ( (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#(#HH#)#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#Z#HH#Z#☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n", 0);

        tick();

        assertF("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼( ◄  ( (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#(#HH#)#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n", 0);

        crack(6, -1);

        assertF("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼ ⊏Я  ⌊ (☼\n" +
                "☼#*#HH#*#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#(#HH#)#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n", 0);

        tick();

        assertF("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼  Я  ⌊ (☼\n" +
                "☼#(#HH# #☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#(#HH#)#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n", 0);

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
                "listener(0) => [KILL_ROBBER]\n" +
                "listener(1) => [KILL_ROBBER]\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n" +
                "listener(4) => [KILL_HERO]\n" +
                "listener(5) => [KILL_HERO]\n" +
                "listener(6) => []\n" +
                "listener(7) => []\n");

        assertF("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼  Я  ⌊ (☼\n" +
                "☼#(#HH# #☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼#Z#HH#Z#☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n", 0);

        tick();

        assertF("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼  Я  ⌊ (☼\n" +
                "☼#(#HH# #☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n", 0);

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
                "listener(0) => [KILL_ROBBER]\n" +
                "listener(1) => []\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n" +
                "listener(4) => []\n" +
                "listener(5) => []\n" +
                "listener(6) => [KILL_HERO]\n" +
                "listener(7) => []\n");

        assertF("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼  Я  ⌊ (☼\n" +
                "☼#Z#HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n", 0);

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

        assertF("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼  Я  ⌊ (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n", 0);

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

        assertF("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼        ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼  ѠHHZ Z☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n", 0);

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

        assertF("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼        ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼  ►HH( (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n", 0);

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

        assertF("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼        ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼   HH   ☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼HH☼☼☼☼\n" +
                "☼  ►HH( (☼\n" +
                "☼###HH###☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n", 0);
    }

    private void goUp() {
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

    private void crack(int left, int right) {
        // простреливают ямки
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

    @Override
    protected void tick() {
        removeAllDied();
        // эмуляция проверки загрузки комнаты, если комната недогружена то не тикаем
        // вообше это делает фреймворк, тут лишь эмулируем
        if (settings.bool(ROUNDS_ENABLED)) {
            if (settings.integer(ROUNDS_PLAYERS_PER_ROOM) != players.size()) {
                return;
            }
        }
        field.tick();
    }

    // TODO тут дублирование с mollymage, может продумать единую архитектуру
    //      тестов работающую и для rounds и реализовать во всех играх начиная
    //      с mollymage, loderunnes, snakebattle и battlecity
    private void removeAllDied() {
        players.forEach(player -> {
            if (!player.isAlive()) {
                field.remove(player(players.indexOf(player)));
            }
        });
    }
}
