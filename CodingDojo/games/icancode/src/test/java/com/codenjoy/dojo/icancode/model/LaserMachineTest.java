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


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LaserMachineTest extends AbstractGameTest {

    @Test
    public void shouldLaserFire_on6Ticks() {
        // given
        givenFl("╔═════┐" +
                "║˃...˅│" +
                "║.S...│" +
                "║.....│" +
                "║.....│" +
                "║˄...˂│" +
                "└─────┘");

        // then
        assertLaserMachineReadyToFire();

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
    public void shouldResetLaserMachine_whenTrainingLevel() {
        // given
        mode = ICanCode.TRAINING;

        givenFl("╔═════┐" +
                "║˃...˅│" +
                "║.S...│" +
                "║.....│" +
                "║.....│" +
                "║˄...˂│" +
                "└─────┘");

        // when
        assertLaserMachineReadyToFire();

        // when
        game.tick();
        game.tick();
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
        hero.reset();

        // then
        assertLaserMachineReadyToFire();

        // when
        hero.reset();

        // then
        assertLaserMachineReadyToFire();

    }

    @Test
    public void shouldNotResetLaserMachine_whenContestLevel() {
        // given
        mode = ICanCode.CONTEST;

        givenFl("╔═════┐" +
                "║˃...˅│" +
                "║.S...│" +
                "║.....│" +
                "║.....│" +
                "║˄...˂│" +
                "└─────┘");

        // when
        assertLaserMachineReadyToFire();

        // when
        game.tick();
        game.tick();
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
        hero.reset(); // ignored

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

        game.tick();

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

        game.tick();

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

        game.tick();

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

        game.tick();

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

    }

    private void assertLaserMachineReadyToFire() {
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
    }

    @Test
    public void shouldLaser_doNotTouchGoldPitStartEnd() {
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
    public void shouldLaser_killRobo() {
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
        events.verifyAllEvents("[LOSE(gold=0, kill=0, single)]");

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

        events.verifyNoEvents();
    }

    @Test
    public void shouldAlive_whenStepToCellWithLeavingSelfLaser() {
        // given
        givenFl("     " +
                "╔═══┐" +
                "║S..│" +
                "└───┘" +
                "     ");

        //when
        hero.right();
        hero.fire();
        game.tick();

        assertL("     " +
                "╔═══┐" +
                "║S..│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "-☺→--" +
                "-----" +
                "-----");

        //when()
        hero.right();
        game.tick();

        //then
        assertL("     " +
                "╔═══┐" +
                "║S..│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "--☺→-" +
                "-----" +
                "-----");

        assertEquals(true, player.isAlive());

    }

    @Test
    public void shouldAlive_whenStepToCellWithLeavingForeignLaser() {
        // given
        givenFl("╔═════┐" +
                "║˃....│" +
                "║.S...│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        ticks(FIRE_TICKS);
        game.tick();

        assertL("╔═════┐" +
                "║˃....│" +
                "║.S...│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        assertE("-------" +
                "--→----" +
                "--☺----" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        // when
        hero.up();
        game.tick();

        // then
        assertL("╔═════┐" +
                "║˃....│" +
                "║.S...│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        assertE("-------" +
                "--☺→---" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        assertEquals(true, player.isAlive());
    }

    @Test
    public void shouldAlive_whenJumpToCellWithLeavingLaser() {
        // given
        givenFl("╔═════┐" +
                "║˃....│" +
                "║.....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       ");

        // when
        ticks(FIRE_TICKS);
        game.tick();

        // then
        assertL("╔═════┐" +
                "║˃....│" +
                "║.....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       ");

        assertE("-------" +
                "--→----" +
                "-------" +
                "---☺---" +
                "-------" +
                "-------" +
                "-------");

        // when
        hero.up();
        hero.jump();
        game.tick();

        // then
        assertL("╔═════┐" +
                "║˃....│" +
                "║.....│" +
                "║..S..│" +
                "└─────┘" +
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
                "-------" +
                "---*---" +
                "-------" +
                "-------" +
                "-------" +
                "-------");
        // when
        game.tick();

        // then
        assertL("╔═════┐" +
                "║˃....│" +
                "║.....│" +
                "║..S..│" +
                "└─────┘" +
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

        assertEquals(true, player.isAlive());
    }

    @Test
    public void shouldAlive_whenJumpLeftOverLaser_case1() {
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
        events.verifyNoEvents();

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
        assertL("╔═════┐" +
                "║˃...S│" +
                "║.....│" +
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
        assertEquals(true, player.isAlive());
    }

    @Test
    public void shouldAlive_whenJumpLeftOverLaser_case2() {
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
        events.verifyNoEvents();

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
        events.verifyNoEvents();

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
        assertEquals(true, player.isAlive());
    }

    @Test
    public void shouldAlive_whenJumpLeftOverLaser_case3() {
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
        events.verifyNoEvents();

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
        events.verifyNoEvents();

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
        assertEquals(true, player.isAlive());
    }

    @Test
    public void shouldAlive_whenJumpToLaser() {
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
        events.verifyNoEvents();

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
        events.verifyNoEvents();

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
        assertEquals(true, player.isAlive());
    }

    @Test
    public void shouldDie_whenFallOnLaser() {
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
        events.verifyAllEvents("[LOSE(gold=0, kill=0, single)]");

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

        events.verifyNoEvents();

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
        events.verifyNoEvents();
    }

    @Test
    public void shouldDie_whenJump_whenLaserComeInCell() {
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
        events.verifyAllEvents("[LOSE(gold=0, kill=0, single)]");

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

        events.verifyNoEvents();

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
        events.verifyNoEvents();
    }

    @Test
    public void shouldAlive_whenJumpOverLaser() {
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
        events.verifyNoEvents();

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
        events.verifyNoEvents();

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
}
