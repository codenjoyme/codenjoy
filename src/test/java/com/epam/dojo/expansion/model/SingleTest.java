package com.epam.dojo.expansion.model;

/*-
 * #%L
 * iCanCode - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 EPAM
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


import com.epam.dojo.expansion.services.Events;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 4:47
 */
public class SingleTest extends AbstractSinglePlayersTest {

    @Test
    public void shouldNextLevelWhenFinishCurrent() {
        // given
        givenFl("╔══┐" +
                "║1E│" +
                "║..│" +
                "└──┘",
                "╔══┐" +
                "║1.│" +
                "║E.│" +
                "└──┘",
                "╔══┐" +
                "║1.│" +
                "║.E│" +
                "└──┘");
        createPlayers(1);

        assertE("----" +
                "-♥--" +
                "----" +
                "----", PLAYER1);

        assertF("[[1,2]=10]", PLAYER1);

        // when
        hero(PLAYER1, 1, 2).right();
        tickAll();

        verify(PLAYER1).event(Events.WIN(0));
        reset(PLAYER1);

        assertL("╔══┐" +
                "║1E│" +
                "║..│" +
                "└──┘", PLAYER1);

        assertE("----" +
                "-♥♥-" +
                "----" +
                "----", PLAYER1);

        assertF("[[1,2]=11," +
                " [2,2]=1]", PLAYER1);

        // when
        tickAll();

        // then
        assertL("╔══┐" +
                "║1.│" +
                "║E.│" +
                "└──┘", PLAYER1);

        assertE("----" +
                "-♥--" +
                "----" +
                "----", PLAYER1);

        assertF("[[1,2]=10]", PLAYER1);

        // when
        hero(PLAYER1, 1, 2).down();
        tickAll();

        // then
        verify(PLAYER1).event(Events.WIN(0));
        reset(PLAYER1);

        assertL("╔══┐" +
                "║1.│" +
                "║E.│" +
                "└──┘", PLAYER1);

        assertE("----" +
                "-♥--" +
                "-♥--" +
                "----", PLAYER1);

        assertF("[[1,2]=11," +
                " [1,1]=1]", PLAYER1);

        // when
        tickAll();

        // then
        assertL("╔══┐" +
                "║1.│" +
                "║.E│" +
                "└──┘", PLAYER1);

        assertE("----" +
                "-♥--" +
                "----" +
                "----", PLAYER1);

        assertF("[[1,2]=10]", PLAYER1);

        // when
        hero(PLAYER1, 1, 2).down();
        tickAll();

        // then
        assertL("╔══┐" +
                "║1.│" +
                "║.E│" +
                "└──┘", PLAYER1);

        assertE("----" +
                "-♥--" +
                "-♥--" +
                "----", PLAYER1);

        assertF("[[1,2]=11," +
                " [1,1]=1]", PLAYER1);

        // when
        hero(PLAYER1, 1, 1).right();
        tickAll();

        // then
        verify(PLAYER1).event(Events.WIN(0));
        reset(PLAYER1);

        assertL("╔══┐" +
                "║1.│" +
                "║.E│" +
                "└──┘", PLAYER1);

        assertE("----" +
                "-♥--" +
                "-♥♥-" +
                "----", PLAYER1);

        assertF("[[1,2]=11," +
                " [1,1]=2," +
                " [2,1]=1]", PLAYER1);

        // when
        tickAll();

        // then
        verifyNoMoreInteractions(PLAYER1);

        assertL("╔══┐" +
                "║1.│" +
                "║.E│" +
                "└──┘", PLAYER1);

        assertE("----" +
                "-♥--" +
                "----" +
                "----", PLAYER1);

        assertF("[[1,2]=10]", PLAYER1);
    }

    @Test
    public void shouldEveryHeroHasTheirOwnStartBase() {
        // given
        givenFl("╔═════┐" +
                "║1.E..│" +
                "║.....│" +
                "║E...E│" +
                "║.....│" +
                "║..E..│" +
                "└─────┘",
                "╔═════┐" +
                "║4.E.1│" +
                "║.....│" +
                "║E...E│" +
                "║.....│" +
                "║3.E.2│" +
                "└─────┘");
        createPlayers(2);

        // level 1 - single for everyone

        assertE("-------" +
                "-♥-----" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------", PLAYER1);

        assertF("[[1,5]=10]", PLAYER1);

        assertE("-------" +
                "-♥-----" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------", PLAYER2);

        assertF("[[1,5]=10]", PLAYER2);

        // when
        // hero1 goes to multiple level
        hero(PLAYER1, 1, 5).right();
        tickAll();

        assertF("[[1,5]=11," +
                " [2,5]=1]", PLAYER1);

        hero(PLAYER1, 2, 5).right();
        tickAll();

        assertF("[[1,5]=11," +
                " [2,5]=2," +
                " [3,5]=1]", PLAYER1);

        verify(PLAYER1).event(Events.WIN(0));
        reset(PLAYER1);
        verifyNoMoreInteractions(PLAYER2);

        tickAll();

        // then
        // hero1 on their own start base
        assertE("-------" +
                "-----♥-" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------", PLAYER1);

        assertF("[[5,5]=10]", PLAYER1);

        // for hero2 nothing will be changed
        assertE("-------" +
                "-♥-----" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------", PLAYER2);

        assertF("[[1,5]=10]", PLAYER2);

    }

    @Test
    public void shouldSeveralPlayersCollectionAtLastLevel() {
        // given
        givenFl("╔══┐" +
                "║1E│" +
                "║..│" +
                "└──┘",
                "╔══┐" +
                "║12│" +
                "║.E│" +
                "└──┘");
        createPlayers(2);

        assertF("[[1,2]=10]", PLAYER1);
        assertF("[[1,2]=10]", PLAYER2);

        // when
        hero(PLAYER1, 1, 2).right();
        tickAll();

        verify(PLAYER1).event(Events.WIN(0));
        reset(PLAYER1);
        verifyNoMoreInteractions(PLAYER2);

        assertL("╔══┐" +
                "║1E│" +
                "║..│" +
                "└──┘", PLAYER1);

        assertE("----" +
                "-♥♥-" +
                "----" +
                "----", PLAYER1);

        assertF("[[1,2]=11," +
                " [2,2]=1]", PLAYER1);

        assertL("╔══┐" +
                "║1E│" +
                "║..│" +
                "└──┘", PLAYER2);

        assertE("----" +
                "-♥--" +
                "----" +
                "----", PLAYER2);

        assertF("[[1,2]=10]", PLAYER2);

        // when
        hero(PLAYER2, 1, 2).right();
        tickAll(); // player1 goes multiple

        // then
        assertL("╔══┐" +
                "║12│" +
                "║.E│" +
                "└──┘", PLAYER1);

        assertE("----" +
                "-♥--" +
                "----" +
                "----", PLAYER1);

        assertF("[[1,2]=10]", PLAYER1);

        assertL("╔══┐" +
                "║1E│" +
                "║..│" +
                "└──┘", PLAYER2);

        assertE("----" +
                "-♥♥-" +
                "----" +
                "----", PLAYER2);

        assertF("[[1,2]=11," +
                " [2,2]=1]", PLAYER2);

        // when
        hero(PLAYER1, 1, 2).down();
        tickAll(); // player2 goes multiple

        // then
        verifyNoMoreInteractions(PLAYER1);
        verify(PLAYER2).event(Events.WIN(0));
        reset(PLAYER2);

        assertL("╔══┐" +
                "║12│" +
                "║.E│" +
                "└──┘", PLAYER1);

        assertE("----" +
                "-♥♦-" +
                "-♥--" +
                "----", PLAYER1);

        assertF("[[1,2]=11," +
                " [2,2]=10," +
                " [1,1]=1]", PLAYER1);

        assertL("╔══┐" +
                "║12│" +
                "║.E│" +
                "└──┘", PLAYER2);

        assertE("----" +
                "-♥♦-" +
                "-♥--" +
                "----", PLAYER2);

        assertF("[[1,2]=11," +
                " [2,2]=10," +
                " [1,1]=1]", PLAYER2);

        // when
        hero(PLAYER1, 1, 1).right(); // finished
        tickAll();

        // then
        verify(PLAYER1).event(Events.WIN(0));
        reset(PLAYER1);
        verifyNoMoreInteractions(PLAYER2);

        assertL("╔══┐" +
                "║12│" +
                "║.E│" +
                "└──┘", PLAYER1);

        assertE("----" +
                "-♥♦-" +
                "-♥♥-" +
                "----", PLAYER1);

        assertF("[[1,2]=11," +
                " [2,2]=10," +
                " [1,1]=2," +
                " [2,1]=1]", PLAYER1);

        assertL("╔══┐" +
                "║12│" +
                "║.E│" +
                "└──┘", PLAYER2);

        assertE("----" +
                "-♥♦-" +
                "-♥♥-" +
                "----", PLAYER2);

        assertF("[[1,2]=11," +
                " [2,2]=10," +
                " [1,1]=2," +
                " [2,1]=1]", PLAYER2);

        // when
        hero(PLAYER2, 2, 2).down();
        // player1 started
        // player2 finished
        tickAll();


        // then
        verifyNoMoreInteractions(PLAYER1);
        verify(PLAYER2).event(Events.WIN(0));
        reset(PLAYER2);

        assertL("╔══┐" +
                "║12│" +
                "║.E│" +
                "└──┘", PLAYER1);

        assertE("----" +
                "-♥♦-" +
                "--♦-" +
                "----", PLAYER1);

        assertF("[[1,2]=10," +
                " [2,2]=11," +
                " [2,1]=1]", PLAYER1);

        assertL("╔══┐" +
                "║12│" +
                "║.E│" +
                "└──┘", PLAYER2);

        assertE("----" +
                "-♥♦-" +
                "--♦-" +
                "----", PLAYER2);

        assertF("[[1,2]=10," +
                " [2,2]=11," +
                " [2,1]=1]", PLAYER2);

        // when
        tickAll(); // player2 started

        // then
        verifyNoMoreInteractions(PLAYER1);
        verifyNoMoreInteractions(PLAYER2);

        assertL("╔══┐" +
                "║12│" +
                "║.E│" +
                "└──┘", PLAYER1);

        assertE("----" +
                "-♥♦-" +
                "----" +
                "----", PLAYER1);

        assertF("[[1,2]=10," +
                " [2,2]=10]", PLAYER1);

        assertL("╔══┐" +
                "║12│" +
                "║.E│" +
                "└──┘", PLAYER2);

        assertE("----" +
                "-♥♦-" +
                "----" +
                "----", PLAYER2);

        assertF("[[1,2]=10," +
                " [2,2]=10]", PLAYER2);

        // when
        hero(PLAYER1, 1, 2).down();
        tickAll();

        // then
        verifyNoMoreInteractions(PLAYER1);
        verifyNoMoreInteractions(PLAYER2);

        assertL("╔══┐" +
                "║12│" +
                "║.E│" +
                "└──┘", PLAYER1);

        assertE("----" +
                "-♥♦-" +
                "-♥--" +
                "----", PLAYER1);

        assertF("[[1,2]=11," +
                " [2,2]=10," +
                " [1,1]=1]", PLAYER1);

        assertL("╔══┐" +
                "║12│" +
                "║.E│" +
                "└──┘", PLAYER2);

        assertE("----" +
                "-♥♦-" +
                "-♥--" +
                "----", PLAYER2);

        assertF("[[1,2]=11," +
                " [2,2]=10," +
                " [1,1]=1]", PLAYER2);

        // when
        hero(PLAYER1, 1, 1).right();
        tickAll(); // player1 finished

        verify(PLAYER1).event(Events.WIN(0));
        reset(PLAYER1);

        hero(PLAYER2, 2, 2).down();

        tickAll(); // player1 started // player2 finished


        verify(PLAYER2).event(Events.WIN(0));
        reset(PLAYER2);

        tickAll(); // player2 started

        verifyNoMoreInteractions(PLAYER1);
        verifyNoMoreInteractions(PLAYER2);

        // then
        assertL("╔══┐" +
                "║12│" +
                "║.E│" +
                "└──┘", PLAYER1);

        assertE("----" +
                "-♥♦-" +
                "----" +
                "----", PLAYER1);

        assertF("[[1,2]=10," +
                " [2,2]=10]", PLAYER1);

        assertL("╔══┐" +
                "║12│" +
                "║.E│" +
                "└──┘", PLAYER2);

        assertE("----" +
                "-♥♦-" +
                "----" +
                "----", PLAYER2);

        assertF("[[1,2]=10," +
                " [2,2]=10]", PLAYER2);
    }

    @Test
    public void shouldAllLevelsAreDone() {
        // given
        givenFl("╔══┐" +
                "║1E│" +
                "║..│" +
                "└──┘",
                "╔══┐" +
                "║.1│" +
                "║.E│" +
                "└──┘",
                "╔══┐" +
                "║..│" +
                "║E1│" +
                "└──┘",
                "╔══┐" + // multiple
                "║E.│" +
                "║1.│" +
                "└──┘");
        createPlayers(1);

        assertL("╔══┐" +
                "║1E│" +
                "║..│" +
                "└──┘", PLAYER1);

        assertE("----" +
                "-♥--" +
                "----" +
                "----", PLAYER1);

        assertF("[[1,2]=10]", PLAYER1);

        // when done 1 level - go to 2 (single)
        hero(PLAYER1, 1, 2).right();
        tickAll();
        tickAll();

        // then
        assertL("╔══┐" +
                "║.1│" +
                "║.E│" +
                "└──┘", PLAYER1);

        assertE("----" +
                "--♥-" +
                "----" +
                "----", PLAYER1);

        assertF("[[2,2]=10]", PLAYER1);

        // when done 2 level - go to 3 (single)
        hero(PLAYER1, 2, 2).down();
        tickAll();
        tickAll();

        // then
        assertL("╔══┐" +
                "║..│" +
                "║E1│" +
                "└──┘", PLAYER1);

        assertE("----" +
                "----" +
                "--♥-" +
                "----", PLAYER1);

        assertF("[[2,1]=10]", PLAYER1);

        // when done 3 level - go to 4 (multiple)
        hero(PLAYER1, 2, 1).left();
        tickAll();
        tickAll();

        // then
        assertL("╔══┐" +
                "║E.│" +
                "║1.│" +
                "└──┘", PLAYER1);

        assertE("----" +
                "----" +
                "-♥--" +
                "----", PLAYER1);

        assertF("[[1,1]=10]", PLAYER1);

        // when done 4 level - start 4 again (multiple)
        hero(PLAYER1, 1, 1).up();
        tickAll();
        tickAll();

        // then
        assertL("╔══┐" +
                "║E.│" +
                "║1.│" +
                "└──┘", PLAYER1);

        assertE("----" +
                "----" +
                "-♥--" +
                "----", PLAYER1);

        assertF("[[1,1]=10]", PLAYER1);

        // when done 4 level - start 4 again multiple)
        hero(PLAYER1, 1, 1).up();
        tickAll();
        tickAll();

        // then
        assertL("╔══┐" +
                "║E.│" +
                "║1.│" +
                "└──┘", PLAYER1);

        assertE("----" +
                "----" +
                "-♥--" +
                "----", PLAYER1);

        assertF("[[1,1]=10]", PLAYER1);
    }

    @Test
    public void shouldSelectLevelWhenAllLevelsAreDone() {
        // given
        shouldAllLevelsAreDone();

        // when try to change level 1  - success from multiple to single
        hero(PLAYER1, 1, 1).loadLevel(0);
        tickAll();

        // then
        assertL("╔══┐" +
                "║1E│" +
                "║..│" +
                "└──┘", PLAYER1);

        assertE("----" +
                "-♥--" +
                "----" +
                "----", PLAYER1);

        assertF("[[1,2]=10]", PLAYER1);

        // when try to change level 2  - success from single to single
        hero(PLAYER1, 1, 2).loadLevel(1);
        tickAll();

        // then
        assertL("╔══┐" +
                "║.1│" +
                "║.E│" +
                "└──┘", PLAYER1);

        assertE("----" +
                "--♥-" +
                "----" +
                "----", PLAYER1);

        assertF("[[2,2]=10]", PLAYER1);

        // when try to change level 3  - success from single to single
        hero(PLAYER1, 2, 2).loadLevel(2);
        tickAll();

        // then
        assertL("╔══┐" +
                "║..│" +
                "║E1│" +
                "└──┘", PLAYER1);

        assertE("----" +
                "----" +
                "--♥-" +
                "----", PLAYER1);

        assertF("[[2,1]=10]", PLAYER1);

        // when try to change level 4 - success from single to multiple
        hero(PLAYER1, 2, 1).loadLevel(3);
        tickAll();

        // then
        assertL("╔══┐" +
                "║E.│" +
                "║1.│" +
                "└──┘", PLAYER1);

        assertE("----" +
                "----" +
                "-♥--" +
                "----", PLAYER1);

        assertF("[[1,1]=10]", PLAYER1);

        // when try to change level 500 - fail
        hero(PLAYER1, 1, 1).right();
        tickAll();
        hero(PLAYER1, 1, 1).loadLevel(500);
        tickAll();

        // then
        assertL("╔══┐" +
                "║E.│" +
                "║1.│" +
                "└──┘", PLAYER1);

        assertE("----" +
                "----" +
                "-♥♥-" +
                "----", PLAYER1
);

        assertF("[[1,1]=11," +
                " [2,1]=1]", PLAYER1);

        // when try to change level 2 - success from multiple to single
        hero(PLAYER1, 2, 1).loadLevel(1);
        tickAll();

        // then
        assertL("╔══┐" +
                "║.1│" +
                "║.E│" +
                "└──┘", PLAYER1);

        assertE("----" +
                "--♥-" +
                "----" +
                "----", PLAYER1);

        assertF("[[2,2]=10]", PLAYER1);
    }

    @Test
    public void shouldWinOnPassedLevelThanCanSelectAnother_caseGoFromMultiple() {
        // given
        shouldAllLevelsAreDone();

        // when win on level then try to change to last - success
        hero(PLAYER1, 1, 1).loadLevel(3);
        tickAll();
        tickAll();

        assertF("[[1,1]=10]", PLAYER1);

        hero(PLAYER1, 1, 1).right();
        tickAll();

        // then
        assertL("╔══┐" +
                "║E.│" +
                "║1.│" +
                "└──┘", PLAYER1);

        assertE("----" +
                "----" +
                "-♥♥-" +
                "----", PLAYER1);

        assertF("[[1,1]=11," +
                " [2,1]=1]", PLAYER1);

        // when try to change level 3 (previous) - success
        hero(PLAYER1, 2, 1).loadLevel(2);
        tickAll();

        // then
        assertL("╔══┐" +
                "║..│" +
                "║E1│" +
                "└──┘", PLAYER1);

        assertE("----" +
                "----" +
                "--♥-" +
                "----", PLAYER1);

        assertF("[[2,1]=10]", PLAYER1);
    }

    @Test
    public void shouldResetOnMultipleWillResetOnlyMultipleLevel() {
        // given
        shouldAllLevelsAreDone();

        // when
        hero(PLAYER1, 1, 1).reset();
        tickAll();
        tickAll();

        assertF("[[1,1]=10]", PLAYER1);

        hero(PLAYER1, 1, 1).right();
        tickAll();

        // then
        // still multiple
        assertL("╔══┐" +
                "║E.│" +
                "║1.│" +
                "└──┘", PLAYER1); 

        assertE("----" +
                "----" +
                "-♥♥-" +
                "----", PLAYER1);

        assertF("[[1,1]=11," +
                " [2,1]=1]", PLAYER1);

        // when
        hero(PLAYER1, 1, 1).reset();
        tickAll();

        // then
        // still multiple
        assertL("╔══┐" +
                "║E.│" +
                "║1.│" +
                "└──┘", PLAYER1);

        assertE("----" +
                "----" +
                "-♥--" +
                "----", PLAYER1);

        assertF("[[1,1]=10]", PLAYER1);
    }

    @Test
    public void testGetBoardAsString() {
        // given
        givenFl("╔═══┐" +
                "║1E.│" +
                "║...│" +
                "║...│" +
                "└───┘",
                "╔═══┐" +
                "║.2.│" +
                "║1..│" +
                "║..E│" +
                "└───┘");
        createPlayers(2);

        // when then
        assertBoardData(
                "{'current':0,'lastPassed':-1,'multiple':false,'scores':true,'total':1}", 
                "{'x':0,'y':0}", 
                true, 
                "╔═══┐" +
                "║1E.│" +
                "║...│" +
                "║...│" +
                "└───┘", 
                "-----" +
                "-♥---" +
                "-----" +
                "-----" +
                "-----", 
                "[[1,3]=10]", Elements.FORCE1, PLAYER1);

        // when then
        assertBoardData(
                "{'current':0,'lastPassed':-1,'multiple':false,'scores':true,'total':1}", 
                "{'x':0,'y':0}", 
                true, 
                "╔═══┐" +
                "║1E.│" +
                "║...│" +
                "║...│" +
                "└───┘", 
                "-----" +
                "-♥---" +
                "-----" +
                "-----" +
                "-----", 
                "[[1,3]=10]", Elements.FORCE1, PLAYER2);

        // players go to next level
        hero(PLAYER1, 1, 3).right();
        hero(PLAYER2, 1, 3).right();
        tickAll();

        assertL("╔═══┐" +
                "║1E.│" +
                "║...│" +
                "║...│" +
                "└───┘", PLAYER1);

        assertE("-----" +
                "-♥♥--" +
                "-----" +
                "-----" +
                "-----", PLAYER1);

        assertL("╔═══┐" +
                "║1E.│" +
                "║...│" +
                "║...│" +
                "└───┘", PLAYER2);

        assertE("-----" +
                "-♥♥--" +
                "-----" +
                "-----" +
                "-----", PLAYER2);

        assertF("[[1,3]=11," +
                " [2,3]=1]", PLAYER1);

        assertF("[[1,3]=11," +
                " [2,3]=1]", PLAYER2);

        // players started on multiple
        tickAll();

        assertL("╔═══┐" +
                "║.2.│" +
                "║1..│" +
                "║..E│" +
                "└───┘", PLAYER1);

        assertE("-----" +
                "--♦--" +
                "-♥---" +
                "-----" +
                "-----", PLAYER1);

        assertL("╔═══┐" +
                "║.2.│" +
                "║1..│" +
                "║..E│" +
                "└───┘", PLAYER2);

        assertE("-----" +
                "--♦--" +
                "-♥---" +
                "-----" +
                "-----", PLAYER2);

        assertF("[[2,3]=10," +
                " [1,2]=10]", PLAYER1);

        assertF("[[2,3]=10," +
                " [1,2]=10]", PLAYER2);

        // then select different way
        hero(PLAYER1, 1, 2).down();
        hero(PLAYER2, 2, 3).right();
        tickAll();

        // then
        assertL("╔═══┐" +
                "║.2.│" +
                "║1..│" +
                "║..E│" +
                "└───┘", PLAYER1);

        assertE("-----" +
                "--♦♦-" +
                "-♥---" +
                "-♥---" +
                "-----", PLAYER1);

        assertL("╔═══┐" +
                "║.2.│" +
                "║1..│" +
                "║..E│" +
                "└───┘", PLAYER2);

        assertE("-----" +
                "--♦♦-" +
                "-♥---" +
                "-♥---" +
                "-----", PLAYER2);

        assertF("[[2,3]=11," +
                " [3,3]=1," +
                " [1,2]=11," +
                " [1,1]=1]", PLAYER1);

        assertF("[[2,3]=11," +
                " [3,3]=1," +
                " [1,2]=11," +
                " [1,1]=1]", PLAYER2);

        // when then
        assertBoardData(
                "{'current':0,'lastPassed':0,'multiple':true,'scores':true,'total':1}", 
                "{'x':0,'y':0}", 
                false, 
                "╔═══┐" +
                "║.2.│" +
                "║1..│" +
                "║..E│" +
                "└───┘", "-----" +
                "--♦♦-" +
                "-♥---" +
                "-♥---" +
                "-----", "[[2,3]=11," +
                " [3,3]=1," +
                " [1,2]=11," +
                " [1,1]=1]", Elements.FORCE1, PLAYER1);

        // when then
        assertBoardData(
                "{'current':0,'lastPassed':0,'multiple':true,'scores':true,'total':1}", 
                "{'x':0,'y':0}", 
                false, 
                "╔═══┐" +
                "║.2.│" +
                "║1..│" +
                "║..E│" +
                "└───┘", 
                "-----" +
                "--♦♦-" +
                "-♥---" +
                "-♥---" +
                "-----", 
                "[[2,3]=11," +
                " [3,3]=1," +
                " [1,2]=11," +
                " [1,1]=1]", Elements.FORCE2, PLAYER2);
    }

    @Test
    public void testGetBoardAsString_whenBigFrame() {
        // given
        String field =
                "╔══════════════════┐" +
                "║1.................│" +
                "║..............B...│" +
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
                "║.B................│" +
                "║..................│" +
                "║..................│" +
                "║.................E│" +
                "└──────────────────┘";
        givenFl(field, field);
        createPlayers(2);

        // when then
        assertBoardData(
                "{'current':0,'lastPassed':-1,'multiple':false,'scores':true,'total':1}", "{'x':0,'y':4}", true, "╔═══════════════" +
                "║1.............." +
                "║..............B" +
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
                "║.B.............",
                "----------------" +
                "-♥--------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------",
                "[[1,18]=10]", Elements.FORCE1, PLAYER1);

        assertBoardData(
                "{'current':0,'lastPassed':-1,'multiple':false,'scores':true,'total':1}", "{'x':0,'y':4}", true, "╔═══════════════" +
                "║1.............." +
                "║..............B" +
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
                "║.B.............",
                "----------------" +
                "-♥--------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------",
                "[[1,18]=10]", Elements.FORCE1, PLAYER2);

        // when
        for (int i = 0; i < 17; i++) {
            hero(PLAYER1, i + 1, 18).right();
            hero(PLAYER2, 1, 18 - i).down();
            tickAll();
        }

        // then
        assertBoardData(
                "{'current':0,'lastPassed':-1,'multiple':false,'scores':true,'total':1}", "{'x':4,'y':4}", true, "═══════════════┐" +
                "...............│" +
                "...........B...│" +
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
                "...............│",
                "----------------" +
                "♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥-" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------",
                "[[4,18]=2," +
                " [5,18]=2," +
                " [6,18]=2," +
                " [7,18]=2," +
                " [8,18]=2," +
                " [9,18]=2," +
                " [10,18]=2," +
                " [11,18]=2," +
                " [12,18]=2," +
                " [13,18]=2," +
                " [14,18]=2," +
                " [15,18]=2," +
                " [16,18]=2," +
                " [17,18]=2," +
                " [18,18]=1]", Elements.FORCE1, PLAYER1);

        assertBoardData(
                "{'current':0,'lastPassed':-1,'multiple':false,'scores':true,'total':1}", "{'x':0,'y':0}", true, "║....│  ║......." +
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
                "║.B............." +
                "║..............." +
                "║..............." +
                "║..............." +
                "└───────────────",
                "-♥--------------" +
                "-♥--------------" +
                "-♥--------------" +
                "-♥--------------" +
                "-♥--------------" +
                "-♥--------------" +
                "-♥--------------" +
                "-♥--------------" +
                "-♥--------------" +
                "-♥--------------" +
                "-♥--------------" +
                "-♥--------------" +
                "-♥--------------" +
                "-♥--------------" +
                "-♥--------------" +
                "----------------",
                "[[1,15]=2," +
                " [1,14]=2," +
                " [1,13]=2," +
                " [1,12]=2," +
                " [1,11]=2," +
                " [1,10]=2," +
                " [1,9]=2," +
                " [1,8]=2," +
                " [1,7]=2," +
                " [1,6]=2," +
                " [1,5]=2," +
                " [1,4]=2," +
                " [1,3]=2," +
                " [1,2]=2," +
                " [1,1]=1]", Elements.FORCE1, PLAYER2);
    }

    @Test
    public void shouldRemoveOnePlayerFromMultiple() {
        // given
        testGetBoardAsString();

        assertL("╔═══┐" +
                "║.2.│" +
                "║1..│" +
                "║..E│" +
                "└───┘", PLAYER1);

        assertE("-----" +
                "--♦♦-" +
                "-♥---" +
                "-♥---" +
                "-----", PLAYER1);

        assertL("╔═══┐" +
                "║.2.│" +
                "║1..│" +
                "║..E│" +
                "└───┘", PLAYER2);

        assertE("-----" +
                "--♦♦-" +
                "-♥---" +
                "-♥---" +
                "-----", PLAYER2);

        assertF("[[2,3]=11," +
                " [3,3]=1," +
                " [1,2]=11," +
                " [1,1]=1]", PLAYER1);

        assertF("[[2,3]=11," +
                " [3,3]=1," +
                " [1,2]=11," +
                " [1,1]=1]", PLAYER2);

        // when
        destroy(PLAYER2);

        // then
        assertL("╔═══┐" +
                "║.2.│" +
                "║1..│" +
                "║..E│" +
                "└───┘", PLAYER1);

        assertE("-----" +
                "-----" +
                "-♥---" +
                "-♥---" +
                "-----", PLAYER1);

        assertL("╔═══┐" +
                "║.2.│" +
                "║1..│" +
                "║..E│" +
                "└───┘", PLAYER2);

        assertE("-----" +
                "-----" +
                "-♥---" +
                "-♥---" +
                "-----", PLAYER2
);

        assertF("[[1,2]=11," +
                " [1,1]=1]", PLAYER1);

        assertF("[[1,2]=11," +
                " [1,1]=1]", PLAYER2);
    }

    @Test
    public void shouldChangeLevelToSingleFromMultiple_thenOtherPlayerShouldNotHide() {
        // given
        testGetBoardAsString();

        assertL("╔═══┐" +
                "║.2.│" +
                "║1..│" +
                "║..E│" +
                "└───┘", PLAYER1);

        assertE("-----" +
                "--♦♦-" +
                "-♥---" +
                "-♥---" +
                "-----", PLAYER1);

        assertL("╔═══┐" +
                "║.2.│" +
                "║1..│" +
                "║..E│" +
                "└───┘", PLAYER2);

        assertE("-----" +
                "--♦♦-" +
                "-♥---" +
                "-♥---" +
                "-----", PLAYER2);

        assertF("[[2,3]=11," +
                " [3,3]=1," +
                " [1,2]=11," +
                " [1,1]=1]", PLAYER1);

        assertF("[[2,3]=11," +
                " [3,3]=1," +
                " [1,2]=11," +
                " [1,1]=1]", PLAYER2);

        // when
        hero(PLAYER2, 1, 2).loadLevel(0);
        tickAll();

        // then
        assertL("╔═══┐" +
                "║.2.│" +
                "║1..│" +
                "║..E│" +
                "└───┘", PLAYER1);

        assertE("-----" +
                "-----" +
                "-♥---" +
                "-♥---" +
                "-----", PLAYER1);

        assertL("╔═══┐" +
                "║1E.│" +
                "║...│" +
                "║...│" +
                "└───┘", PLAYER2);

        assertE("-----" +
                "-♥---" +
                "-----" +
                "-----" +
                "-----", PLAYER2);

        assertF("[[1,2]=11," +
                " [1,1]=1]", PLAYER1);

        assertF("[[1,3]=10]", PLAYER2);
    }

}
