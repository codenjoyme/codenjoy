package com.codenjoy.dojo.icancode.model;

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


import com.codenjoy.dojo.icancode.services.Events;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class ICanCodeTest extends AbstractGameTest {

    @Test
    public void shouldFieldAtStart() {
        // given
        givenFl("╔═════════┐" +
                "║.........│" +
                "║.S.┌─╗...│" +
                "║...│ ║...│" +
                "║.┌─┘ └─╗.│" +
                "║.│     ║.│" +
                "║.╚═┐ ╔═╝.│" +
                "║...│ ║...│" +
                "║...╚═╝...│" +
                "║........E│" +
                "└─────────┘");

        // then
        assertL("╔═════════┐" +
                "║.........│" +
                "║.S.┌─╗...│" +
                "║...│ ║...│" +
                "║.┌─┘ └─╗.│" +
                "║.│     ║.│" +
                "║.╚═┐ ╔═╝.│" +
                "║...│ ║...│" +
                "║...╚═╝...│" +
                "║........E│" +
                "└─────────┘");

        assertE("-----------" +
                "-----------" +
                "--☺--------" +
                "-----------" +
                "-----------" +
                "-----------" +
                "-----------" +
                "-----------" +
                "-----------" +
                "-----------" +
                "-----------");
    }

    @Test
    public void shouldWalk() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        // when
        hero.right();
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "---☺-" +
                "-----" +
                "-----");

        // when
        hero.left();
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺--" +
                "-----" +
                "-----");

        // when
        hero.down();
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "-----" +
                "--☺--" +
                "-----");

        // when
        hero.up();
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺--" +
                "-----" +
                "-----");

        assertL("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");
    }

    @Test
    public void shouldStopWhenNoCommands() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        // when
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺--" +
                "-----" +
                "-----");

        // when
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺--" +
                "-----" +
                "-----");

        assertL("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");
    }

    @Test
    public void shouldStopWhenWallForDirections() {
        // given
        givenFl("╔═┐" +
                "║S│" +
                "└─┘");

        // when
        hero.left();
        game.tick();

        // then
        assertE("---" +
                "-☺-" +
                "---");

        // when
        hero.right();
        game.tick();

        // then
        assertE("---" +
                "-☺-" +
                "---");

        // when
        hero.up();
        game.tick();

        // then
        assertE("---" +
                "-☺-" +
                "---");

        // when
        hero.down();
        game.tick();

        // then
        assertE("---" +
                "-☺-" +
                "---");

        assertL("╔═┐" +
                "║S│" +
                "└─┘");
    }

    @Test
    public void shouldStopWhenWalls() {
        // given
        givenFl(".╔." +
                "║S═" +
                ".┐.");

        // when
        hero.left();
        game.tick();

        // then
        assertE("---" +
                "-☺-" +
                "---");

        // when
        hero.right();
        game.tick();

        // then
        assertE("---" +
                "-☺-" +
                "---");

        // when
        hero.up();
        game.tick();

        // then
        assertE("---" +
                "-☺-" +
                "---");

        // when
        hero.down();
        game.tick();

        // then
        assertE("---" +
                "-☺-" +
                "---");
    }

    @Test
    public void shouldStopWhenWalls2() {
        // given
        givenFl(".│." +
                "┘S└" +
                ".─.");

        // when
        hero.left();
        game.tick();

        // then
        assertE("---" +
                "-☺-" +
                "---");

        // when
        hero.right();
        game.tick();

        // then
        assertE("---" +
                "-☺-" +
                "---");

        // when
        hero.up();
        game.tick();

        // then
        assertE("---" +
                "-☺-" +
                "---");

        // when
        hero.down();
        game.tick();

        // then
        assertE("---" +
                "-☺-" +
                "---");
    }

    @Test
    public void shouldStopWhenWalls3() {
        // given
        givenFl(".┌." +
                "╝S╗" +
                ".╚.");

        // when
        hero.left();
        game.tick();

        // then
        assertE("---" +
                "-☺-" +
                "---");

        // when
        hero.right();
        game.tick();

        // then
        assertE("---" +
                "-☺-" +
                "---");

        // when
        hero.up();
        game.tick();

        // then
        assertE("---" +
                "-☺-" +
                "---");

        // when
        hero.down();
        game.tick();

        // then
        assertE("---" +
                "-☺-" +
                "---");
    }

    @Test
    public void shouldWin() {
        // given
        givenFl("     " +
                "╔═══┐" +
                "║.SE│" +
                "└───┘" +
                "     ");

        // when
        hero.right();
        game.tick();

        // then
        verify(listener).event(Events.WIN(0));

        assertE("-----" +
                "-----" +
                "---☺-" +
                "-----" +
                "-----");

        assertL("     " +
                "╔═══┐" +
                "║.SE│" +
                "└───┘" +
                "     ");
    }

    @Test
    public void shouldNewGameAfterWin() {
        // given
        givenFl("     " +
                "╔═══┐" +
                "║.SE│" +
                "└───┘" +
                "     ");

        // when
        hero.right();
        game.tick();

        // then
        assertEquals(true, player.isWin());
        assertEquals(false, player.isAlive());

        // when
        game.newGame(player);
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺--" +
                "-----" +
                "-----");

        assertL("     " +
                "╔═══┐" +
                "║.SE│" +
                "└───┘" +
                "     ");

        assertEquals(false, player.isWin());
        assertEquals(true, player.isAlive());
    }

    @Test
    public void shouldStartInNewPlaceWhenAct() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║S..│" +
                "║...│" +
                "└───┘");

        hero.right();
        game.tick();

        hero.right();
        game.tick();

        assertE("-----" +
                "-----" +
                "---☺-" +
                "-----" +
                "-----");

        // when
        hero.reset();

        assertE("-----" +
                "-----" +
                "---☺-" +
                "-----" +
                "-----");

        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "-☺---" +
                "-----" +
                "-----");
    }

    @Test
    public void demo1_generalWay() {
        // given
        givenFl("     " +
                "╔═══┐" +
                "║S.E│" +
                "└───┘" +
                "     ");

        // when
        hero.right();
        game.tick();

        hero.right();
        game.tick();

        // then
        verify(listener).event(Events.WIN(0));

        assertL("     " +
                "╔═══┐" +
                "║S.E│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "---☺-" +
                "-----" +
                "-----");
    }

    @Test
    public void shouldNextLevelWhenFinishCurrent() {
        // given
        givenFl("╔══┐" +
                "║SE│" +
                "║..│" +
                "└──┘");

        // when
        hero.right();
        game.tick();

        verify(listener).event(Events.WIN(0));

        assertEquals(true, player.isWin());
        assertEquals(false, player.isAlive());

        assertL("╔══┐" +
                "║SE│" +
                "║..│" +
                "└──┘");

        assertE("----" +
                "--☺-" +
                "----" +
                "----");

        // when
        game.newGame(player);
        game.tick();

        // then
        verifyNoMoreInteractions(listener);

        assertEquals(false, player.isWin());
        assertEquals(true, player.isAlive());

        assertL("╔══┐" +
                "║SE│" +
                "║..│" +
                "└──┘");

        assertE("----" +
                "-☺--" +
                "----" +
                "----");
    }

    @Test
    public void shouldAfterNextLevelHeroCanMove() {
        // given
        shouldNextLevelWhenFinishCurrent();

        assertL("╔══┐" +
                "║SE│" +
                "║..│" +
                "└──┘");

        assertE("----" +
                "-☺--" +
                "----" +
                "----");

        // when
        hero.down();
        game.tick();

        // then
        assertL("╔══┐" +
                "║SE│" +
                "║..│" +
                "└──┘");

        assertE("----" +
                "----" +
                "-☺--" +
                "----");
    }

    @Test
    public void shouldLooseWhenFallInHole() {
        // given
        givenFl("╔══┐" +
                "║SO│" +
                "║.E│" +
                "└──┘");

        // when
        hero.right();
        game.tick();

        // then
        verify(listener).event(Events.LOOSE());

        assertL("╔══┐" +
                "║SO│" +
                "║.E│" +
                "└──┘");

        assertE("----" +
                "--o-" +
                "----" +
                "----");

        assertEquals(false, player.isAlive());
        assertEquals(false, player.isWin());
    }

    @Test
    public void shouldNewGameAfterFallInHole() {
        // given
        shouldLooseWhenFallInHole();

        assertL("╔══┐" +
                "║SO│" +
                "║.E│" +
                "└──┘");

        assertE("----" +
                "--o-" +
                "----" +
                "----");

        assertEquals(false, player.isAlive());
        assertEquals(false, player.isWin());

        // when
        game.newGame(player);
        game.tick();

        // then
        assertEquals(true, player.isAlive());
        assertEquals(false, player.isWin());

        assertL("╔══┐" +
                "║SO│" +
                "║.E│" +
                "└──┘");

        assertE("----" +
                "-☺--" +
                "----" +
                "----");
    }

    @Test
    public void shouldCanGoAfterFallInHole() {
        // given
        shouldNewGameAfterFallInHole();

        assertL("╔══┐" +
                "║SO│" +
                "║.E│" +
                "└──┘");

        assertE("----" +
                "-☺--" +
                "----" +
                "----");

        // when
        hero.down();
        game.tick();

        // then
        assertL("╔══┐" +
                "║SO│" +
                "║.E│" +
                "└──┘");

        assertE("----" +
                "----" +
                "-☺--" +
                "----");

    }

    @Test
    public void shouldDoubleScoreWhenGetGold() {
        // given
        givenFl("     " +
                "╔═══┐" +
                "║S$E│" +
                "└───┘" +
                "     ");

        // when
        hero.right();
        game.tick();

        hero.right();
        game.tick();

        // then
        verify(listener).event(Events.WIN(1));

        assertL("     " +
                "╔═══┐" +
                "║S.E│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "---☺-" +
                "-----" +
                "-----");
    }

    @Test
    public void shouldDoubleScoreWhenGetTwoGold() {
        // given
        givenFl("      " +
                "╔════┐" +
                "║S$$E│" +
                "└────┘" +
                "      " +
                "      ");

        // when
        hero.right();
        game.tick();

        hero.right();
        game.tick();

        hero.right();
        game.tick();

        // then
        verify(listener).event(Events.WIN(2));

        assertL("      " +
                "╔════┐" +
                "║S..E│" +
                "└────┘" +
                "      " +
                "      ");

        assertE("------" +
                "------" +
                "----☺-" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldNoScoreWhenGetGold() {
        // given
        givenFl("     " +
                "╔═══┐" +
                "║S$E│" +
                "└───┘" +
                "     ");

        // when
        hero.right();
        game.tick();

        // then
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void shouldHideGoldWhenGet() {
        // given
        givenFl("      " +
                "╔════┐" +
                "║S$$E│" +
                "└────┘" +
                "      " +
                "      ");

        // when
        hero.right();
        game.tick();

        // then
        assertL("      " +
                "╔════┐" +
                "║S.$E│" +
                "└────┘" +
                "      " +
                "      ");

        assertE("------" +
                "------" +
                "--☺---" +
                "------" +
                "------" +
                "------");

        // when
        hero.right();
        game.tick();

        // then
        assertL("      " +
                "╔════┐" +
                "║S..E│" +
                "└────┘" +
                "      " +
                "      ");

        assertE("------" +
                "------" +
                "---☺--" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldReNewGoldWhenReset() {
        // given
        givenFl("      " +
                "╔════┐" +
                "║S$$E│" +
                "└────┘" +
                "      " +
                "      ");

        hero.right();
        game.tick();

        hero.right();
        game.tick();

        assertL("      " +
                "╔════┐" +
                "║S..E│" +
                "└────┘" +
                "      " +
                "      ");

        assertE("------" +
                "------" +
                "---☺--" +
                "------" +
                "------" +
                "------");

        // when
        hero.reset();
        game.tick();

        // then
        assertL("      " +
                "╔════┐" +
                "║S$$E│" +
                "└────┘" +
                "      " +
                "      ");

        assertE("------" +
                "------" +
                "-☺----" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldJump() {
        // given
        givenFl("     " +
                "╔═══┐" +
                "║S..│" +
                "└───┘" +
                "     ");

        // when
        hero.jump();

        // then
        assertL("     " +
                "╔═══┐" +
                "║S..│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "-☺---" +
                "-----" +
                "-----");

        assertF("-----" +
                "-----" +
                "-----" +
                "-----" +
                "-----");

        // when
        game.tick();

        // then
        assertL("     " +
                "╔═══┐" +
                "║S..│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "-----" +
                "-----" +
                "-----");

        assertF("-----" +
                "-----" +
                "-*---" +
                "-----" +
                "-----");

        // when
        game.tick();

        // then
        assertL("     " +
                "╔═══┐" +
                "║S..│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "-☺---" +
                "-----" +
                "-----");

        assertF("-----" +
                "-----" +
                "-----" +
                "-----" +
                "-----");
    }

    @Test
    public void shouldJumpRight() {
        // given
        givenFl("     " +
                "╔═══┐" +
                "║S..│" +
                "└───┘" +
                "     ");

        // when
        hero.jump();
        hero.right();

        // then
        assertL("     " +
                "╔═══┐" +
                "║S..│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "-☺---" +
                "-----" +
                "-----");

        assertF("-----" +
                "-----" +
                "-----" +
                "-----" +
                "-----");

        // when
        game.tick();

        // then
        assertL("     " +
                "╔═══┐" +
                "║S..│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "-----" +
                "-----" +
                "-----");

        assertF("-----" +
                "-----" +
                "--*--" +
                "-----" +
                "-----");

        // when
        game.tick();

        // then
        assertL("     " +
                "╔═══┐" +
                "║S..│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "---☺-" +
                "-----" +
                "-----");

        assertF("-----" +
                "-----" +
                "-----" +
                "-----" +
                "-----");
    }

    @Test
    public void shouldNotJumpOnBox() {
        // given
        givenFl("     " +
                "╔═══┐" +
                "║SBB│" +
                "└───┘" +
                "     ");

        // when
        hero.jump();
        hero.right();

        // then
        assertL("     " +
                "╔═══┐" +
                "║S..│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "-☺BB-" +
                "-----" +
                "-----");

        assertF("-----" +
                "-----" +
                "-----" +
                "-----" +
                "-----");

        // when
        game.tick();

        // then
        assertL("     " +
                "╔═══┐" +
                "║S..│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "--BB-" +
                "-----" +
                "-----");

        assertF("-----" +
                "-----" +
                "-*---" +
                "-----" +
                "-----");

        // when
        game.tick();

        // then
        assertL("     " +
                "╔═══┐" +
                "║S..│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "-☺BB-" +
                "-----" +
                "-----");

        assertF("-----" +
                "-----" +
                "-----" +
                "-----" +
                "-----");
    }

    @Test
    public void shouldCantChangeDirectionWhenJumpForward() {
        // given
        givenFl("     " +
                "╔═══┐" +
                "║S..│" +
                "└───┘" +
                "     ");

        // when
        hero.jump();
        hero.right();

        // then
        assertL("     " +
                "╔═══┐" +
                "║S..│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "-☺---" +
                "-----" +
                "-----");

        assertF("-----" +
                "-----" +
                "-----" +
                "-----" +
                "-----");

        // when
        game.tick();

        // then
        assertL("     " +
                "╔═══┐" +
                "║S..│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "-----" +
                "-----" +
                "-----");

        assertF("-----" +
                "-----" +
                "--*--" +
                "-----" +
                "-----");

        // when
        hero.left(); // will be ignored
        hero.right();
        hero.up();
        hero.down();

        game.tick();

        // then
        assertL("     " +
                "╔═══┐" +
                "║S..│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "---☺-" +
                "-----" +
                "-----");

        assertF("-----" +
                "-----" +
                "-----" +
                "-----" +
                "-----");
    }

    @Test
    public void shouldLeaveGoldWhenJump() {
        // given
        givenFl("     " +
                "╔═══┐" +
                "║S$.│" +
                "└───┘" +
                "     ");

        // when
        hero.jump();
        hero.right();

        game.tick();

        // then
        assertL("     " +
                "╔═══┐" +
                "║S$.│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "-----" +
                "-----" +
                "-----");

        assertF("-----" +
                "-----" +
                "--*--" +
                "-----" +
                "-----");

        // when
        game.tick();

        // then
        assertL("     " +
                "╔═══┐" +
                "║S$.│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "---☺-" +
                "-----" +
                "-----");

        assertF("-----" +
                "-----" +
                "-----" +
                "-----" +
                "-----");
    }

    @Test
    public void shouldJumpOverHole() {
        // given
        givenFl("     " +
                "╔═══┐" +
                "║SO.│" +
                "└───┘" +
                "     ");

        // when
        hero.jump();
        hero.right();

        game.tick();

        // then
        assertL("     " +
                "╔═══┐" +
                "║SO.│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "-----" +
                "-----" +
                "-----");

        assertF("-----" +
                "-----" +
                "--*--" +
                "-----" +
                "-----");

        // when
        game.tick();

        // then
        assertL("     " +
                "╔═══┐" +
                "║SO.│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "---☺-" +
                "-----" +
                "-----");

        assertF("-----" +
                "-----" +
                "-----" +
                "-----" +
                "-----");
    }

    @Test
    public void shouldJumpOverExit() {
        // given
        givenFl("     " +
                "╔═══┐" +
                "║.SE│" +
                "└───┘" +
                "     ");

        // when
        hero.jump();
        hero.right();

        game.tick();

        // then
        assertL("     " +
                "╔═══┐" +
                "║.SE│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "-----" +
                "-----" +
                "-----");

        assertF("-----" +
                "-----" +
                "---*-" +
                "-----" +
                "-----");

        // when
        game.tick();

        // then
        assertL("     " +
                "╔═══┐" +
                "║.SE│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "---☺-" +
                "-----" +
                "-----");

        assertF("-----" +
                "-----" +
                "-----" +
                "-----" +
                "-----");
    }

    @Test
    public void shouldLaserFireOn6Ticks() {
        // given
        givenFl("╔═════┐" +
                "║˃...˅│" +
                "║.S...│" +
                "║.....│" +
                "║.....│" +
                "║˄...˂│" +
                "└─────┘");

        // when
        game.tick();

        // then
        assertL("╔═════┐" +
                "║˃...˅│" +
                "║.S...│" +
                "║.....│" +
                "║.....│" +
                "║˄...˂│" +
                "└─────┘");

        assertE("-------" +
                "-------" +
                "--☺----" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        // when
        ticks(FIRE_TICKS - 1);

        // then
        assertL("╔═════┐" +
                "║►...▼│" +
                "║.S...│" +
                "║.....│" +
                "║.....│" +
                "║▲...◄│" +
                "└─────┘");

        assertE("-------" +
                "-------" +
                "--☺----" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        // when
        game.tick();

        // then
        assertL("╔═════┐" +
                "║˃...˅│" +
                "║.S...│" +
                "║.....│" +
                "║.....│" +
                "║˄...˂│" +
                "└─────┘");

        assertE("-------" +
                "--→----" +
                "--☺--↓-" +
                "-------" +
                "-↑-----" +
                "----←--" +
                "-------");

        // when
        game.tick();

        // then
        assertL("╔═════┐" +
                "║˃...˅│" +
                "║.S...│" +
                "║.....│" +
                "║.....│" +
                "║˄...˂│" +
                "└─────┘");

        assertE("-------" +
                "---→---" +
                "--☺----" +
                "-↑---↓-" +
                "-------" +
                "---←---" +
                "-------");

        // when
        game.tick();

        // then
        assertL("╔═════┐" +
                "║˃...˅│" +
                "║.S...│" +
                "║.....│" +
                "║.....│" +
                "║˄...˂│" +
                "└─────┘");

        assertE("-------" +
                "----→--" +
                "-↑☺----" +
                "-------" +
                "-----↓-" +
                "--←----" +
                "-------");

        // when
        game.tick();

        // then
        assertL("╔═════┐" +
                "║˃...˅│" +
                "║.S...│" +
                "║.....│" +
                "║.....│" +
                "║˄...˂│" +
                "└─────┘");

        assertE("-------" +
                "-------" +
                "--☺----" +
                "-------" +
                "-------" +
                "-------" +
                "-------");
    }

    @Test
    public void shouldLaserDoNotTouchGoldPitStartEnd() {
        // given
        givenFl("╔═════┐" +
                "║˃.O.˅│" +
                "║.....│" +
                "║S...E│" +
                "║.....│" +
                "║˄.$.˂│" +
                "└─────┘");

        // when
        hero.right();
        ticks(FIRE_TICKS);
        game.tick();

        // then
        assertL("╔═════┐" +
                "║˃.O.˅│" +
                "║.....│" +
                "║S...E│" +
                "║.....│" +
                "║˄.$.˂│" +
                "└─────┘");

        assertE("-------" +
                "--→----" +
                "-----↓-" +
                "--☺----" +
                "-↑-----" +
                "----←--" +
                "-------");

        // when
        game.tick();

        // then
        assertL("╔═════┐" +
                "║˃.O.˅│" +
                "║.....│" +
                "║S...E│" +
                "║.....│" +
                "║˄.$.˂│" +
                "└─────┘");

        assertE("-------" +
                "---→---" +
                "-------" +
                "-↑☺--↓-" +
                "-------" +
                "---←---" +
                "-------");

        // when
        game.tick();

        // then
        assertL("╔═════┐" +
                "║˃.O.˅│" +
                "║.....│" +
                "║S...E│" +
                "║.....│" +
                "║˄.$.˂│" +
                "└─────┘");

        assertE("-------" +
                "----→--" +
                "-↑-----" +
                "--☺----" +
                "-----↓-" +
                "--←----" +
                "-------");
    }

    @Test
    public void shouldLaserKillRobo() {
        // given
        givenFl("╔═════┐" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        hero.up();
        ticks(FIRE_TICKS);
        game.tick();

        assertL("╔═════┐" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        assertE("-------" +
                "--→☺---" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        // when
        game.tick();

        // then
        verify(listener).event(Events.LOOSE());

        assertL("╔═════┐" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        assertE("-------" +
                "---☻---" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        assertEquals(false, player.isAlive());
        assertEquals(false, player.isWin());

        // when
        game.newGame(player);
        game.tick();

        // then
        assertEquals(true, player.isAlive());
        assertEquals(false, player.isWin());

        assertL("╔═════┐" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        assertE("-------" +
                "-------" +
                "---☺---" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        // when
        game.tick();

        // then
        assertEquals(true, player.isAlive());
        assertEquals(false, player.isWin());

        verifyNoMoreInteractions(listener);
    }

    @Test
    public void shouldAliveWhenJumpOverLaser() {
        // given
        givenFl("╔═════┐" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        hero.up();
        ticks(FIRE_TICKS);
        game.tick();

        assertL("╔═════┐" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        assertE("-------" +
                "--→☺---" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        assertF("-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        // when
        hero.jump();
        game.tick();

        // then
        verifyNoMoreInteractions(listener);

        assertL("╔═════┐" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        assertE("-------" +
                "---→---" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        assertF("-------" +
                "---*---" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        // when
        game.tick();

        // then
        assertL("╔═════┐" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        assertE("-------" +
                "---☺→--" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        assertF("-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        // when
        game.tick();

        // then
        verifyNoMoreInteractions(listener);

        // then
        assertL("╔═════┐" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        assertE("-------" +
                "---☺-→-" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        assertF("-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");
    }

    @Test
    public void shouldAliveWhenJumpToLaser() {
        // given
        givenFl("╔═════┐" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        hero.up();
        ticks(FIRE_TICKS);
        game.tick();

        assertL("╔═════┐" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        assertE("-------" +
                "--→☺---" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        assertF("-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        // when
        hero.jump();
        hero.left();
        game.tick();

        // then
        verifyNoMoreInteractions(listener);

        assertL("╔═════┐" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        assertE("-------" +
                "---→---" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        assertF("-------" +
                "--*----" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        // when
        game.tick();

        // then
        assertL("╔═════┐" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        assertE("-------" +
                "--☺-→--" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        assertF("-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        // when
        game.tick();

        // then
        verifyNoMoreInteractions(listener);

        // then
        assertL("╔═════┐" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        assertE("-------" +
                "--☺--→-" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        assertF("-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");
    }

    @Test
    public void shouldDieWhenFallOnLaser() {
        // given
        givenFl("╔═════┐" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        hero.up();
        ticks(FIRE_TICKS);
        hero.jump();
        game.tick();

        assertL("╔═════┐" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        assertE("-------" +
                "--→----" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        assertF("-------" +
                "---*---" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        // when
        game.tick();

        // then
        verify(listener).event(Events.LOOSE());

        assertL("╔═════┐" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        assertE("-------" +
                "---☻---" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        assertF("-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        assertEquals(false, player.isAlive());
        assertEquals(false, player.isWin());

        // when
        game.newGame(player);
        game.tick();

        // then
        assertEquals(true, player.isAlive());
        assertEquals(false, player.isWin());

        verifyNoMoreInteractions(listener);

        assertL("╔═════┐" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        assertE("-------" +
                "-------" +
                "---☺---" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        assertF("-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        // when
        game.tick();

        // then
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void shouldDieWhenJumpWhenLaserComeInCell() {
        // given
        givenFl("╔═════┐" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        hero.up();
        ticks(FIRE_TICKS);
        game.tick();

        assertL("╔═════┐" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        assertE("-------" +
                "--→☺---" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        // when
        game.tick();
        hero.jump();

        // then
        verify(listener).event(Events.LOOSE());

        assertL("╔═════┐" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        assertE("-------" +
                "---☻---" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        assertEquals(false, player.isAlive());
        assertEquals(false, player.isWin());

        // when
        game.newGame(player);
        game.tick();

        // then
        assertEquals(true, player.isAlive());
        assertEquals(false, player.isWin());

        verifyNoMoreInteractions(listener);

        assertL("╔═════┐" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        assertE("-------" +
                "-------" +
                "---☺---" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        // when
        game.tick();

        // then
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void shouldJumpAtHoleWhenNearWall() {
        // given
        givenFl("╔══┐" +
                "║SO│" +
                "║..│" +
                "└──┘");

        // when
        hero.right();
        hero.jump();
        game.tick();

        // then
        assertL("╔══┐" +
                "║SO│" +
                "║..│" +
                "└──┘");

        assertE("----" +
                "----" +
                "----" +
                "----");

        assertF("----" +
                "--*-" +
                "----" +
                "----");

        // when
        game.tick();

        // then
        assertL("╔══┐" +
                "║SO│" +
                "║..│" +
                "└──┘");

        assertE("----" +
                "--o-" +
                "----" +
                "----");

        assertF("----" +
                "----" +
                "----" +
                "----");

    }

    @Test
    public void shouldJumpAtGoldWhenNearWall() {
        // given
        givenFl("╔══┐" +
                "║S$│" +
                "║..│" +
                "└──┘");

        // when
        hero.right();
        hero.jump();
        game.tick();

        // then
        assertL("╔══┐" +
                "║S$│" +
                "║..│" +
                "└──┘");

        assertE("----" +
                "----" +
                "----" +
                "----");

        assertF("----" +
                "--*-" +
                "----" +
                "----");

        // when
        game.tick();

        // then
        assertL("╔══┐" +
                "║S.│" +
                "║..│" +
                "└──┘");

        assertE("----" +
                "--☺-" +
                "----" +
                "----");

        assertF("----" +
                "----" +
                "----" +
                "----");
    }

    @Test
    public void shouldJumpAtLaserWhenNearWall() {
        // given
        givenFl("╔══┐" +
                "║S.│" +
                "║.˄│" +
                "└──┘");

        ticks(FIRE_TICKS - 1);

        // when
        hero.right();
        hero.jump();
        game.tick();

        // then
        assertL("╔══┐" +
                "║S.│" +
                "║.▲│" +
                "└──┘");

        assertE("----" +
                "----" +
                "----" +
                "----");

        assertF("----" +
                "--*-" +
                "----" +
                "----");

        // when
        game.tick();

        // then
        verify(listener).event(Events.LOOSE());

        assertL("╔══┐" +
                "║S.│" +
                "║.˄│" +
                "└──┘");

        assertE("----" +
                "--☻-" +
                "----" +
                "----");

        assertF("----" +
                "----" +
                "----" +
                "----");

    }

    private void ticks(int count) {
        for (int i = 0; i < count; i++) {
            game.tick();
        }
    }

    @Test
    public void shouldAliveWhenJumpLeftOverLaser_case1() {
        // given
        givenFl("╔═════┐" +
                "║˃...S│" +
                "║.....│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        ticks(FIRE_TICKS);
        game.tick();

        assertL("╔═════┐" +
                "║˃...S│" +
                "║.....│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        assertE("-------" +
                "--→--☺-" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        assertF("-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        // when
        hero.jump();
        hero.left();
        game.tick();

        // then
        verifyNoMoreInteractions(listener);

        assertL("╔═════┐" +
                "║˃...S│" +
                "║.....│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        assertE("-------" +
                "---→---" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        assertF("-------" +
                "----*--" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        // when
        game.tick();

        // then
        verify(listener).event(Events.LOOSE());

        assertL("╔═════┐" +
                "║˃...S│" +
                "║.....│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        assertE("-------" +
                "---☻---" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        assertF("-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");
    }

    @Test
    public void shouldAliveWhenJumpLeftOverLaser_case2() {
        // given
        givenFl("╔═════┐" +
                "║˃..S.│" +
                "║.....│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        ticks(FIRE_TICKS);
        game.tick();

        assertL("╔═════┐" +
                "║˃..S.│" +
                "║.....│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        assertE("-------" +
                "--→-☺--" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        assertF("-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        // when
        hero.jump();
        hero.left();
        game.tick();

        // then
        verifyNoMoreInteractions(listener);

        assertL("╔═════┐" +
                "║˃..S.│" +
                "║.....│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        assertE("-------" +
                "---→---" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        assertF("-------" +
                "---*---" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        // when
        game.tick();

        // then
        verifyNoMoreInteractions(listener);

        assertL("╔═════┐" +
                "║˃..S.│" +
                "║.....│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        assertE("-------" +
                "--☺-→--" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        assertF("-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");
    }

    @Test
    public void shouldAliveWhenJumpLeftOverLaser_case3() {
        // given
        givenFl("╔═════┐" +
                "║.....│" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       ");

        ticks(FIRE_TICKS);
        game.tick();

        assertL("╔═════┐" +
                "║.....│" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       ");

        assertE("-------" +
                "-------" +
                "--→----" +
                "---☺---" +
                "-------" +
                "-------" +
                "-------");

        assertF("-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        // when
        hero.jump();
        hero.up();
        game.tick();

        // then
        verifyNoMoreInteractions(listener);

        assertL("╔═════┐" +
                "║.....│" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       ");

        assertE("-------" +
                "-------" +
                "---→---" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        assertF("-------" +
                "-------" +
                "---*---" +
                "-------" +
                "-------" +
                "-------" +
                "-------");


        // when
        game.tick();

        // then
        verifyNoMoreInteractions(listener);

        assertL("╔═════┐" +
                "║.....│" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       ");

        assertE("-------" +
                "---☺---" +
                "----→--" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        assertF("-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");
    }

    @Test
    public void shouldIgnoreJumpWhenFlying() {
        // given
        givenFl("     " +
                "╔═══┐" +
                "║S..│" +
                "└───┘" +
                "     ");

        hero.jump();
        game.tick();

        assertL("     " +
                "╔═══┐" +
                "║S..│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "-----" +
                "-----" +
                "-----");

        assertF("-----" +
                "-----" +
                "-*---" +
                "-----" +
                "-----");

        // when
        hero.jump();
        game.tick();

        // then
        assertL("     " +
                "╔═══┐" +
                "║S..│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "-☺---" +
                "-----" +
                "-----");

        assertF("-----" +
                "-----" +
                "-----" +
                "-----" +
                "-----");

        // when
        game.tick();

        // then
        assertL("     " +
                "╔═══┐" +
                "║S..│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "-☺---" +
                "-----" +
                "-----");

        assertF("-----" +
                "-----" +
                "-----" +
                "-----" +
                "-----");

        // when
        hero.jump();
        game.tick();

        assertL("     " +
                "╔═══┐" +
                "║S..│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "-----" +
                "-----" +
                "-----");

        assertF("-----" +
                "-----" +
                "-*---" +
                "-----" +
                "-----");
    }

    @Test
    public void shouldScrollingView() {
        //given
        givenFl("╔══════════════════┐" +
                "║S.................│" +
                "║..................│" +
                "║....┌──╗..........│" +
                "║....│  ║..........│" +
                "║..┌─┘  └─╗........│" +
                "║..│      ║........│" +
                "║..│      ║........│" +
                "║..╚═┐  ╔═╝........│" +
                "║....│  ║..........│" +
                "║....╚══╝..........│" +
                "║..................│" +
                "║..................│" +
                "║..................│" +
                "║..................│" +
                "║..................│" +
                "║..................│" +
                "║..................│" +
                "║.................E│" +
                "└──────────────────┘");

        //then
        assertL("╔═══════════════" +
                "║S.............." +
                "║..............." +
                "║....┌──╗......." +
                "║....│  ║......." +
                "║..┌─┘  └─╗....." +
                "║..│      ║....." +
                "║..│      ║....." +
                "║..╚═┐  ╔═╝....." +
                "║....│  ║......." +
                "║....╚══╝......." +
                "║..............." +
                "║..............." +
                "║..............." +
                "║..............." +
                "║...............");

        //when
        for (int i = 0; i < 10; ++i) {
            hero.right();
            game.tick();
        }

        //then
        assertL("╔═══════════════" +
                "║S.............." +
                "║..............." +
                "║....┌──╗......." +
                "║....│  ║......." +
                "║..┌─┘  └─╗....." +
                "║..│      ║....." +
                "║..│      ║....." +
                "║..╚═┐  ╔═╝....." +
                "║....│  ║......." +
                "║....╚══╝......." +
                "║..............." +
                "║..............." +
                "║..............." +
                "║..............." +
                "║...............");

        //when
        hero.right();
        game.tick();

        //then
        assertL("════════════════" +
                "S..............." +
                "................" +
                "....┌──╗........" +
                "....│  ║........" +
                "..┌─┘  └─╗......" +
                "..│      ║......" +
                "..│      ║......" +
                "..╚═┐  ╔═╝......" +
                "....│  ║........" +
                "....╚══╝........" +
                "................" +
                "................" +
                "................" +
                "................" +
                "................");


        //when
        for (int i = 0; i < 12; ++i) {
            hero.right();
            game.tick();
        }

        //then
        assertL("═══════════════┐" +
                "...............│" +
                "...............│" +
                ".┌──╗..........│" +
                ".│  ║..........│" +
                "─┘  └─╗........│" +
                "      ║........│" +
                "      ║........│" +
                "═┐  ╔═╝........│" +
                ".│  ║..........│" +
                ".╚══╝..........│" +
                "...............│" +
                "...............│" +
                "...............│" +
                "...............│" +
                "...............│");

        //when
        hero.left();
        game.tick();

        for (int i = 0; i < 20; ++i) {
            hero.down();
            game.tick();
        }

        assertL(".│  ║..........│" +
                "─┘  └─╗........│" +
                "      ║........│" +
                "      ║........│" +
                "═┐  ╔═╝........│" +
                ".│  ║..........│" +
                ".╚══╝..........│" +
                "...............│" +
                "...............│" +
                "...............│" +
                "...............│" +
                "...............│" +
                "...............│" +
                "...............│" +
                "..............E│" +
                "───────────────┘");

        //when
        for (int i = 0; i < 20; ++i) {
            hero.left();
            game.tick();
        }

        assertL("║....│  ║......." +
                "║..┌─┘  └─╗....." +
                "║..│      ║....." +
                "║..│      ║....." +
                "║..╚═┐  ╔═╝....." +
                "║....│  ║......." +
                "║....╚══╝......." +
                "║..............." +
                "║..............." +
                "║..............." +
                "║..............." +
                "║..............." +
                "║..............." +
                "║..............." +
                "║..............." +
                "└───────────────");
    }

    @Test
    public void shouldGoldOnBoardWhenLaserOnTop() {
        // given
        givenFl("╔═════┐" +
                "║˃...$│" +
                "║....S│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        ticks(FIRE_TICKS + 2);
        game.tick();

        assertL("╔═════┐" +
                "║˃...$│" +
                "║....S│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        assertE("-------" +
                "----→--" +
                "-----☺-" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        // when
        game.tick();

        // then
        assertL("╔═════┐" +
                "║˃...$│" +
                "║....S│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        assertE("-------" +
                "-----→-" +
                "-----☺-" +
                "-------" +
                "-------" +
                "-------" +
                "-------");
    }

    @Test
    public void shouldStartOnCenter() {
        //given
        givenFl("╔════════════════════════════════════┐" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║.................S..................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║...................................E│" +
                "└────────────────────────────────────┘");

        //then
        assertL("................" +
                "................" +
                "................" +
                "................" +
                "................" +
                "................" +
                "................" +
                "........S......." +
                "................" +
                "................" +
                "................" +
                "................" +
                "................" +
                "................" +
                "................" +
                "................");
    }

    @Test
    public void shouldMovableBoxOnBoard() {
        // given
        givenFl("╔════┐" +
                "║SB..│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // then
        assertL("╔════┐" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "-☺B---" +
                "------" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldMovedBoxWhenHeroPushItLeft() {
        // given
        givenFl("╔════┐" +
                "║....│" +
                "║....│" +
                "║....│" +
                "║..BS│" +
                "└────┘");

        // when
        hero.pull();
        hero.left();
        game.tick();

        // then
        assertL("╔════┐" +
                "║....│" +
                "║....│" +
                "║....│" +
                "║...S│" +
                "└────┘");

        assertE("------" +
                "------" +
                "------" +
                "------" +
                "--B☺--" +
                "------");

        // when
        hero.left();
        game.tick();

        // then
        assertL("╔════┐" +
                "║....│" +
                "║....│" +
                "║....│" +
                "║...S│" +
                "└────┘");

        assertE("------" +
                "------" +
                "------" +
                "------" +
                "-B☺---" +
                "------");
    }

    @Test
    public void shouldMovedBoxWhenHeroPushItRight() {
        // given
        givenFl("╔════┐" +
                "║SB..│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        hero.pull();
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "--☺B--" +
                "------" +
                "------" +
                "------" +
                "------");

        // when
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "---☺B-" +
                "------" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldMovedBoxWhenHeroPushItDown() {
        // given
        givenFl("╔════┐" +
                "║S...│" +
                "║B...│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        hero.pull();
        hero.down();
        game.tick();

        // then
        assertL("╔════┐" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "------" +
                "-☺----" +
                "-B----" +
                "------" +
                "------");

        // when
        hero.down();
        game.tick();

        // then
        assertL("╔════┐" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "------" +
                "------" +
                "-☺----" +
                "-B----" +
                "------");
    }

    @Test
    public void shouldMovedBoxWhenHeroPushItUp() {
        // given
        givenFl("╔════┐" +
                "║....│" +
                "║....│" +
                "║...B│" +
                "║...S│" +
                "└────┘");

        // when
        hero.pull();
        hero.up();
        game.tick();

        // then
        assertL("╔════┐" +
                "║....│" +
                "║....│" +
                "║....│" +
                "║...S│" +
                "└────┘");

        assertE("------" +
                "------" +
                "----B-" +
                "----☺-" +
                "------" +
                "------");

        // when
        hero.up();
        game.tick();

        // then
        assertL("╔════┐" +
                "║....│" +
                "║....│" +
                "║....│" +
                "║...S│" +
                "└────┘");

        assertE("------" +
                "----B-" +
                "----☺-" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldNotMovedBoxWhenHeroPushItOnWall() {
        // given
        givenFl("╔════┐" +
                "║..SB│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║..S.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "---☺B-" +
                "------" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldNotMovedBoxWhenHeroPushItOnOtherBox() {
        // given
        givenFl("╔════┐" +
                "║SBB.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "-☺BB--" +
                "------" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldNotMovedBoxWhenHeroPushItOnGold() {
        // given
        givenFl("╔════┐" +
                "║SB$.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║S.$.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "-☺B---" +
                "------" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldNotMovedBoxWhenHeroPushItOnLaserMachine() {
        // given
        givenFl("╔════┐" +
                "║SB˃.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║S.˃.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "-☺B---" +
                "------" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldNotMovedBoxWhenHeroPushItOnOtherHero() {
        // given
        givenFl("╔════┐" +
                "║SBX.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        hero.down();
        game.tick();
        hero.right();
        game.tick();
        hero.right();
        game.tick();
        hero.up();
        game.tick();

        assertL("╔════┐" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "-XB☺--" +
                "------" +
                "------" +
                "------" +
                "------");

        // when
        hero.left();
        game.tick();

        // then
        assertL("╔════┐" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "-XB☺--" +
                "------" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldMovedBoxOnLaserWithDestroyIt_caseOnLaser() {
        // given
        givenFl("╔════┐" +
                "║....│" +
                "║SB..│" +
                "║..↑.│" +
                "║....│" +
                "└────┘");

        // when
        hero.pull();
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║....│" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "------" +
                "--☺B--" +
                "------" +
                "------" +
                "------");

        // when
        game.tick();

        // then
        assertL("╔════┐" +
                "║....│" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "------" +
                "--☺B--" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldMovedBoxOnLaserWithDestroyIt_caseBeforeLaser() {
        // given
        givenFl("╔════┐" +
                "║....│" +
                "║SB..│" +
                "║....│" +
                "║..↑.│" +
                "└────┘");

        // when
        hero.pull();
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║....│" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "------" +
                "--☺B--" +
                "---↑--" +
                "------" +
                "------");

        // when
        game.tick();

        // then
        assertL("╔════┐" +
                "║....│" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "------" +
                "--☺B--" +
                "------" +
                "------" +
                "------");

        // when
        game.tick();

        // then
        assertL("╔════┐" +
                "║....│" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "------" +
                "--☺B--" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldMovedBoxOnLaserWithoutDestroyIt_caseAfterLaser() {
        // given
        givenFl("╔════┐" +
                "║....│" +
                "║SB↑.│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        hero.pull();
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║....│" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "---↑--" +
                "--☺B--" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldMovedBoxWhenHeroPushItOnHoleSoWeHaveHiddenHole() {
        // given
        givenFl("╔════┐" +
                "║SBO.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        hero.pull();
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║S.O.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "--☺B--" +
                "------" +
                "------" +
                "------" +
                "------");

        // when
        hero.right();
        game.tick();

        // then
        verify(listener).event(Events.LOOSE());

        assertL("╔════┐" +
                "║S.O.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "---oB-" +
                "------" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldMovedBoxWhenHeroPushItOnEdit() {
        // given
        givenFl("╔════┐" +
                "║SBE.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        hero.pull();
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║S.E.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "--☺B--" +
                "------" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldMovedBoxWhenHeroPushItOnStart() {
        // given
        givenFl("╔════┐" +
                "║SBS.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        hero.pull();
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║S.S.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "--☺B--" +
                "------" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldMovedBoxWhenHeroPullItRight() {
        // given
        givenFl("╔════┐" +
                "║....│" +
                "║....│" +
                "║....│" +
                "║BS..│" +
                "└────┘");

        // when
        hero.pull();
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║....│" +
                "║....│" +
                "║....│" +
                "║.S..│" +
                "└────┘");

        assertE("------" +
                "------" +
                "------" +
                "------" +
                "--B☺--" +
                "------");

        // when
        hero.pull();
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║....│" +
                "║....│" +
                "║....│" +
                "║.S..│" +
                "└────┘");

        assertE("------" +
                "------" +
                "------" +
                "------" +
                "---B☺-" +
                "------");
    }

    @Test
    public void shouldMovedBoxWhenHeroPullItLeft() {
        // given
        givenFl("╔════┐" +
                "║..SB│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        hero.pull();
        hero.left();
        game.tick();

        // then
        assertL("╔════┐" +
                "║..S.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "--☺B--" +
                "------" +
                "------" +
                "------" +
                "------");

        // when
        hero.pull();
        hero.left();
        game.tick();

        // then
        assertL("╔════┐" +
                "║..S.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "-☺B---" +
                "------" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldMovedBoxWhenHeroPullItUp() {
        // given
        givenFl("╔════┐" +
                "║....│" +
                "║....│" +
                "║S...│" +
                "║B...│" +
                "└────┘");

        // when
        hero.pull();
        hero.up();
        game.tick();

        // then
        assertL("╔════┐" +
                "║....│" +
                "║....│" +
                "║S...│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "------" +
                "-☺----" +
                "-B----" +
                "------" +
                "------");

        // when
        hero.pull();
        hero.up();
        game.tick();

        // then
        assertL("╔════┐" +
                "║....│" +
                "║....│" +
                "║S...│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "-☺----" +
                "-B----" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldMovedBoxWhenHeroPullItDown() {
        // given
        givenFl("╔════┐" +
                "║...B│" +
                "║...S│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        hero.pull();
        hero.down();
        game.tick();

        // then
        assertL("╔════┐" +
                "║....│" +
                "║...S│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "------" +
                "----B-" +
                "----☺-" +
                "------" +
                "------");

        // when
        hero.pull();
        hero.down();
        game.tick();

        // then
        assertL("╔════┐" +
                "║....│" +
                "║...S│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "------" +
                "------" +
                "----B-" +
                "----☺-" +
                "------");
    }

    @Test
    public void shouldJumpOverBoxDoNotMoveIt() {
        // given
        givenFl("╔════┐" +
                "║SB..│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // then
        assertL("╔════┐" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "-☺B---" +
                "------" +
                "------" +
                "------" +
                "------");

        // when
        hero.jump();
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "--B---" +
                "------" +
                "------" +
                "------" +
                "------");

        assertF("------" +
                "--*---" +
                "------" +
                "------" +
                "------" +
                "------");

        // when
        game.tick();

        // then
        assertL("╔════┐" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "--B☺--" +
                "------" +
                "------" +
                "------" +
                "------");

        assertF("------" +
                "------" +
                "------" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldMovedBoxWhenHeroPullUpright() {
        // given
        givenFl("╔═════┐" +
                "║..B..│" +
                "║.BSB.│" +
                "║.B.B.│" +
                "║.BBB.│" +
                "║.....│" +
                "└─────┘");

        // when
        hero.pull();
        hero.down();
        game.tick();

        // then
        assertL("╔═════┐" +
                "║.....│" +
                "║..S..│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "└─────┘");

        assertE("-------" +
                "-------" +
                "--BBB--" +
                "--B☺B--" +
                "--BBB--" +
                "-------" +
                "-------");

        // when
        hero.pull();
        hero.up();
        game.tick();

        // then
        assertL("╔═════┐" +
                "║.....│" +
                "║..S..│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "└─────┘");

        assertE("-------" +
                "---B---" +
                "--B☺B--" +
                "--B-B--" +
                "--BBB--" +
                "-------" +
                "-------");
    }

    @Test
    public void shouldMovedBoxWhenHeroPullHorizontally() {
        // given
        givenFl("╔══════┐" +
                "║......│" +
                "║..BB..│" +
                "║.BS.B.│" +
                "║..BB..│" +
                "║......│" +
                "║......│" +
                "└──────┘");

        // when
        hero.pull();
        hero.right();
        game.tick();

        // then
        assertL("╔══════┐" +
                "║......│" +
                "║......│" +
                "║..S...│" +
                "║......│" +
                "║......│" +
                "║......│" +
                "└──────┘");

        assertE("--------" +
                "--------" +
                "---BB---" +
                "---B☺B--" +
                "---BB---" +
                "--------" +
                "--------" +
                "--------");

        // when
        hero.pull();
        hero.left();
        game.tick();

        // then
        assertL("╔══════┐" +
                "║......│" +
                "║......│" +
                "║..S...│" +
                "║......│" +
                "║......│" +
                "║......│" +
                "└──────┘");

        assertE("--------" +
                "--------" +
                "---BB---" +
                "--B☺-B--" +
                "---BB---" +
                "--------" +
                "--------" +
                "--------");
    }

    @Test
    public void shouldStartWhenSeveralStarts_case1() {
        // given
        when(dice.next(anyInt())).thenReturn(0);

        givenFl("╔═════┐" +
                "║S...S│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "║S...S│" +
                "└─────┘");

        // when
        game.tick();

        // then
        assertL("╔═════┐" +
                "║S...S│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "║S...S│" +
                "└─────┘");

        assertE("-------" +
                "-☺-----" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");
    }

    @Test
    public void shouldStartWhenSeveralStarts_case2() {
        // given
        when(dice.next(anyInt())).thenReturn(1);

        givenFl("╔═════┐" +
                "║S...S│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "║S...S│" +
                "└─────┘");

        // when
        game.tick();

        // then
        assertL("╔═════┐" +
                "║S...S│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "║S...S│" +
                "└─────┘");

        assertE("-------" +
                "-----☺-" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");
    }

    @Test
    public void shouldStartWhenSeveralStarts_case3() {
        // given
        when(dice.next(anyInt())).thenReturn(2);

        givenFl("╔═════┐" +
                "║S...S│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "║S...S│" +
                "└─────┘");

        // when
        game.tick();

        // then
        assertL("╔═════┐" +
                "║S...S│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "║S...S│" +
                "└─────┘");

        assertE("-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-☺-----" +
                "-------");
    }

    @Test
    public void shouldStartWhenSeveralStarts_case4() {
        // given
        when(dice.next(anyInt())).thenReturn(3);

        givenFl("╔═════┐" +
                "║S...S│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "║S...S│" +
                "└─────┘");

        // when
        game.tick();

        // then
        assertL("╔═════┐" +
                "║S...S│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "║S...S│" +
                "└─────┘");

        assertE("-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-----☺-" +
                "-------");
    }

    @Test
    public void shouldFlyOnShootingMachine() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║S˂.│" +
                "║...│" +
                "└───┘");

        // when
        ticks(FIRE_TICKS - 1);
        hero.jump();
        hero.right();
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "-----" +
                "-----" +
                "-----");

        assertF("-----" +
                "-----" +
                "--*--" +
                "-----" +
                "-----");

        assertL("╔═══┐" +
                "║...│" +
                "║S◄.│" +
                "║...│" +
                "└───┘");

        // when
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "-←-☺-" +
                "-----" +
                "-----");

        assertF("-----" +
                "-----" +
                "-----" +
                "-----" +
                "-----");

        assertL("╔═══┐" +
                "║...│" +
                "║S˂.│" +
                "║...│" +
                "└───┘");
    }

    /*@Test
    public void shouldFlyOnOtherPlayer() {
        // given
        givenFlWithOnePlayer(
                "╔═══┐" +
                "║..˂│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        // when
        ticks((FIRE_TICKS - 1) * 2);
        hero.right();
        ticks(2);

        // then
        assertE("-----" +
                "-----" +
                "--X☺-" +
                "-----" +
                "-----");

        assertL("╔═══┐" +
                "║..◄│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        // when
        hero.jump();
        hero.left();
        ticks(2);

        // then
        assertE("-----" +
                "--←--" +
                "--X☺-" +
                "-----" +
                "-----");

        assertL("╔═══┐" +
                "║..◄│" +
                "║.S.│" +
                "║...│" +
                "└───┘");
    }*/
}
