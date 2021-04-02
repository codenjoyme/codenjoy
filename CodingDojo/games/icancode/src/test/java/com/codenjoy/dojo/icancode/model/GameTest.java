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
import com.codenjoy.dojo.icancode.services.Levels;
import com.codenjoy.dojo.services.Direction;
import org.junit.Test;

import static com.codenjoy.dojo.icancode.services.GameSettings.Keys.*;
import static com.codenjoy.dojo.icancode.services.LevelsTest.VIEW_SIZE_TESTING;
import static com.codenjoy.dojo.services.Direction.STOP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class GameTest extends AbstractGameTest {

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
    public void shouldStop_whenNoCommands() {
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
    public void shouldStop_whenWallForDirections() {
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
    public void shouldStop_whenWalls() {
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
    public void shouldStop_whenWalls2() {
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
    public void shouldStop_whenWalls3() {
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
    public void shouldStartInNewPlace_whenReset() {
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
    public void shouldNextLevel_whenFinishCurrent() {
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
        shouldNextLevel_whenFinishCurrent();

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
    public void shouldLoose_whenFallInHole() {
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
        shouldLoose_whenFallInHole();

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
    public void shouldDoubleScore_whenGetGold() {
        // given
        givenFl("     " +
                "╔═══┐" +
                "║S$E│" +
                "└───┘" +
                "     ");

        assertL("     " +
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
    public void shouldDoubleScore_whenGetTwoGold() {
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
    public void shouldPutAllGoldOnField_afterHeroCollectIt_whenReset_caseSingle() {
        // given
        mode = ICanCode.TRAINING;

        givenFl("       " +
                "╔═════┐" +
                "║S$$..│" +
                "║....E│" +
                "└─────┘" +
                "       " +
                "       ");

        hero.right();
        game.tick();

        hero.right();
        game.tick();

        hero.right();
        game.tick();

        assertL("       " +
                "╔═════┐" +
                "║S....│" +
                "║....E│" +
                "└─────┘" +
                "       " +
                "       ");

        assertE("-------" +
                "-------" +
                "----☺--" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        // when
        dice(0); // index of start position
        hero.reset();
        game.tick();

        // then
        assertL("       " +
                "╔═════┐" +
                "║S$$..│" +
                "║....E│" +
                "└─────┘" +
                "       " +
                "       ");

        assertE("-------" +
                "-------" +
                "-☺-----" +
                "-------" +
                "-------" +
                "-------" +
                "-------");
    }

    @Test
    public void shouldPutAllGoldOnField_afterHeroCollectIt_whenReset_caseMultiple() {
        // given
        mode = ICanCode.CONTEST;

        givenFl("       " +
                "╔═════┐" +
                "║S$$..│" +
                "║....E│" +
                "└─────┘" +
                "       " +
                "       ");

        hero.right();
        game.tick();

        hero.right();
        game.tick();

        hero.right();
        game.tick();

        assertL("       " +
                "╔═════┐" +
                "║S....│" +
                "║....E│" +
                "└─────┘" +
                "       " +
                "       ");

        assertE("-------" +
                "-------" +
                "----☺--" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        // when
        dice(0, 3, 4 - 1); // index of start position, then 2 indexes of floors for gold
        hero.reset();
        game.tick();

        // then
        assertL("       " +
                "╔═════┐" +
                "║S...$│" +
                "║$...E│" +
                "└─────┘" +
                "       " +
                "       ");

        assertE("-------" +
                "-------" +
                "-☺-----" +
                "-------" +
                "-------" +
                "-------" +
                "-------");
    }

    @Test
    public void shouldNoScore_whenGetGold() {
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
    public void shouldHideGold_whenGet() {
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
    public void shouldReNewGold_whenReset() {
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
    public void shouldLeftHaveNoEffect_whenFlying() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        assertE("-----" +
                "-----" +
                "--☺--" +
                "-----" +
                "-----");

        // when
        hero.jump();
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

        // when
        hero.left();
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺--" +
                "-----" +
                "-----");
        
        assertNotEquals(Direction.LEFT, hero.getDirection());
    }

    @Test
    public void shoudlRightHaveNoEffect_whenFlying() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        assertE("-----" +
                "-----" +
                "--☺--" +
                "-----" +
                "-----");

        // when
        hero.jump();
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

        // when
        hero.right();
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺--" +
                "-----" +
                "-----");
        
        assertNotEquals(Direction.RIGHT, hero.getDirection());
    }

    @Test
    public void shoudlUpHaveNoEffect_whenFlying() {
        // given 
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        assertE("-----" +
                "-----" +
                "--☺--" +
                "-----" +
                "-----");

        // when 
        hero.jump();
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

        // when
        hero.up();
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺--" +
                "-----" +
                "-----");
        
        assertNotEquals(Direction.UP, hero.getDirection());
    }

    @Test
    public void shouldDownHaveNoEffect_whenFlying() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        assertE("-----" +
                "-----" +
                "--☺--" +
                "-----" +
                "-----");

        // when
        hero.jump();
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

        // when
        hero.down();
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺--" +
                "-----" +
                "-----");
        
        assertNotEquals(Direction.DOWN, hero.getDirection());
    }

    @Test
    public void shoudlResetHaveNoEffect_whenFlying() {
        // given 
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        assertE("-----" +
                "-----" +
                "--☺--" +
                "-----" +
                "-----");

        // when
        hero.jump();
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

        // when
        hero.reset();
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺--" +
                "-----" +
                "-----");
        
        assertEquals(false, hero.isReset());
    }

    @Test
    public void shouldKumpHaveNoEffect_whenFlying() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        assertE("-----" +
                "-----" +
                "--☺--" +
                "-----" +
                "-----");

        // when
        hero.jump();
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

        // when
        hero.jump();
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺--" +
                "-----" +
                "-----");
        
        assertEquals(false, hero.isJump());
    }

    @Test
    public void shouldPullHaveNoEffect_whenFlying() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        assertE("-----" +
                "-----" +
                "--☺--" +
                "-----" +
                "-----");

        // when
        hero.jump();
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

        // when
        hero.pull();
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺--" +
                "-----" +
                "-----");
        
        assertEquals(false, hero.isPull());
    }

    @Test
    public void shouldFireHaveNoEffect_whenFlying() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        assertE("-----" +
                "-----" +
                "--☺--" +
                "-----" +
                "-----");

        // when
        hero.jump();
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

        // when
        hero.fire();
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺--" +
                "-----" +
                "-----");
        
        assertEquals(false, hero.isFire());
    }

    @Test
    public void shouldCantChangeDirection_whenJumpForward() {
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
    public void shouldLeaveGold_whenJump() {
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
    public void killZombieNearYouByFirstLaserTick() {
        // given
        settings.integer(PERK_AVAILABILITY, 10)
                .integer(PERK_ACTIVITY, 10);

        givenFl("╔════┐" +
                "║.S..│" +
                "║.♂..│" +
                "║....│" +
                "║....│" +
                "└────┘");

        givenZombie().thenReturn(STOP);

        // when
        hero.down();
        hero.fire();
        game.tick();

        // then
        assertE("------" +
                "--☺---" +
                "--✝---" +
                "------" +
                "------" +
                "------");

        game.tick();

        assertE("------" +
                "--☺---" +
                "------" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldJumpAtHole_whenNearWall() {
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
    public void shouldJumpAtGold_whenNearWall() {
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
    public void shouldJumpAtLaser_whenNearWall() {
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

    @Test
    public void shouldIgnoreJump_whenFlying() {
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
        // given
        givenFl(VIEW_SIZE_TESTING,
                "╔══════════════════┐" +
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

        // then
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

        // when
        for (int i = 0; i < 10; ++i) {
            hero.right();
            game.tick();
        }

        // then
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

        // when
        hero.right();
        game.tick();

        // then
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


        // when
        for (int i = 0; i < 12; ++i) {
            hero.right();
            game.tick();
        }

        // then
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

        // when
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

        // when
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
    public void shouldGoldOnBoard_whenLaserOnTop() {
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
        // given
        givenFl(VIEW_SIZE_TESTING,
                "╔════════════════════════════════════┐" +
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

        // then
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
    public void shouldStart_whenSeveralStarts_case1() {
        // given
        dice(0);

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
    public void shouldStart_whenSeveralStarts_case2() {
        // given
        dice(1);

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
    public void shouldStart_whenSeveralStarts_case3() {
        // given
        dice(2);

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
    public void shouldStart_whenSeveralStarts_case4() {
        // given
        dice(3);

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
    public void shouldFly_onShootingMachine() {
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

    @Test
    public void shouldShoot_withDelay() {
        // given
        settings.integer(GUN_RECHARGE, 2);

        givenFl("╔═══┐" +
                "║.S.│" +
                "║...│" +
                "║...│" +
                "└───┘");

        // when
        hero.fire();
        hero.down();
        game.tick();

        // then
        assertE("-----" +
                "--☺--" +
                "--↓--" +
                "-----" +
                "-----");

        // when
        hero.fire();
        hero.down();
        game.tick();

        // then
        assertE("-----" +
                "--☺--" +
                "-----" +
                "--↓--" +
                "-----");
        // when
        hero.fire();
        hero.down();
        game.tick();

        // then
        assertE("-----" +
                "--☺--" +
                "-----" +
                "-----" +
                "-----");

        // when
        hero.fire();
        hero.down();
        game.tick();

        // then
        assertE("-----" +
                "--☺--" +
                "--↓--" +
                "-----" +
                "-----");
    }
}
