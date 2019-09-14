package com.codenjoy.dojo.expansion.model;

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


import com.codenjoy.dojo.utils.JsonUtils;
import com.codenjoy.dojo.expansion.model.levels.LevelsTest;
import com.codenjoy.dojo.expansion.services.Events;
import org.junit.Test;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static com.codenjoy.dojo.expansion.services.SettingsWrapper.data;
import static junit.framework.Assert.assertEquals;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 4:47
 */
public class SingleTest extends AbstractSinglePlayersTest {

    protected boolean isSingleTrainingOrMultiple() {
        return true;
    }

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

        assertL("╔══┐" +
                "║1E│" +
                "║..│" +
                "└──┘", PLAYER1);

        assertE("----" +
                "-♥--" +
                "----" +
                "----", PLAYER1);

        assertF("-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#\n" +
                "-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#\n", PLAYER1);

        // when
        hero(PLAYER1, 1, 2).right();
        spreader.tickAll();

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

        assertF("-=#-=#-=#-=#\n" +
                "-=#00B001-=#\n" +
                "-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#\n", PLAYER1);

        // when
        spreader.reloadLevelForWinner(PLAYER1);
        spreader.tickAll();

        // then
        assertL("╔══┐" +
                "║1.│" +
                "║E.│" +
                "└──┘", PLAYER1);

        assertE("----" +
                "-♥--" +
                "----" +
                "----", PLAYER1);

        assertF("-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#\n" +
                "-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#\n", PLAYER1);

        // when
        hero(PLAYER1, 1, 2).down();
        spreader.tickAll();

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

        assertF("-=#-=#-=#-=#\n" +
                "-=#00B-=#-=#\n" +
                "-=#001-=#-=#\n" +
                "-=#-=#-=#-=#\n", PLAYER1);

        // when
        spreader.reloadLevelForWinner(PLAYER1);
        spreader.tickAll();

        // then
        assertL("╔══┐" +
                "║1.│" +
                "║.E│" +
                "└──┘", PLAYER1);

        assertE("----" +
                "-♥--" +
                "----" +
                "----", PLAYER1);

        assertF("-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#\n" +
                "-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#\n", PLAYER1);

        // when
        hero(PLAYER1, 1, 2).down();
        spreader.tickAll();

        // then
        assertL("╔══┐" +
                "║1.│" +
                "║.E│" +
                "└──┘", PLAYER1);

        assertE("----" +
                "-♥--" +
                "-♥--" +
                "----", PLAYER1);

        assertF("-=#-=#-=#-=#\n" +
                "-=#00B-=#-=#\n" +
                "-=#001-=#-=#\n" +
                "-=#-=#-=#-=#\n", PLAYER1);

        // when
        hero(PLAYER1, 1, 1).right();
        spreader.tickAll();

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

        assertF("-=#-=#-=#-=#\n" +
                "-=#00B-=#-=#\n" +
                "-=#002001-=#\n" +
                "-=#-=#-=#-=#\n", PLAYER1);

        // when
        spreader.reloadLevelForWinner(PLAYER1);
        spreader.tickAll();

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

        assertF("-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#\n" +
                "-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#\n", PLAYER1);
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

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER1);

        assertE("-------" +
                "-♥-----" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------", PLAYER2);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER2);

        // when
        // hero1 goes to multiple level
        hero(PLAYER1, 1, 5).right();
        spreader.tickAll();

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#00B001-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER1);

        hero(PLAYER1, 2, 5).right();
        spreader.tickAll();

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#00B002001-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER1);

        verify(PLAYER1).event(Events.WIN(0));
        reset(PLAYER1);
        verifyNoMoreInteractions(PLAYER2);

        spreader.reloadLevelForWinner(PLAYER1);
        spreader.tickAll();

        // then
        // hero1 on their own start base
        assertE("-------" +
                "-----♥-" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------", PLAYER1);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER1);

        // for hero2 nothing will be changed
        assertE("-------" +
                "-♥-----" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------", PLAYER2);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER2);
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

        assertF("-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#\n" +
                "-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#\n", PLAYER1);

        assertF("-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#\n" +
                "-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#\n", PLAYER2);

        // when
        hero(PLAYER1, 1, 2).right();
        spreader.tickAll();

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

        assertF("-=#-=#-=#-=#\n" +
                "-=#00B001-=#\n" +
                "-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#\n", PLAYER1);

        assertL("╔══┐" +
                "║1E│" +
                "║..│" +
                "└──┘", PLAYER2);

        assertE("----" +
                "-♥--" +
                "----" +
                "----", PLAYER2);

        assertF("-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#\n" +
                "-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#\n", PLAYER2);

        // when
        hero(PLAYER2, 1, 2).right();

        spreader.reloadLevelForWinner(PLAYER1);
        spreader.tickAll(); // player1 goes multiple

        // then
        assertL("╔══┐" +
                "║12│" +
                "║.E│" +
                "└──┘", PLAYER1);

        assertE("----" +
                "-♥--" +
                "----" +
                "----", PLAYER1);

        assertF("-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#\n" +
                "-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#\n", PLAYER1);

        assertL("╔══┐" +
                "║1E│" +
                "║..│" +
                "└──┘", PLAYER2);

        assertE("----" +
                "-♥♥-" +
                "----" +
                "----", PLAYER2);

        assertF("-=#-=#-=#-=#\n" +
                "-=#00B001-=#\n" +
                "-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#\n", PLAYER2);

        // when
        spreader.reloadLevelForWinner(PLAYER2);
        spreader.tickAll(); // player2 goes multiple

        // then
        verifyNoMoreInteractions(PLAYER1);
        verify(PLAYER2).event(Events.WIN(0));
        reset(PLAYER2);

        // when
        hero(PLAYER1, 1, 2).down();
        spreader.tickAll();

        assertL("╔══┐" +
                "║12│" +
                "║.E│" +
                "└──┘", PLAYER1);

        assertE("----" +
                "-♥♦-" +
                "-♥--" +
                "----", PLAYER1);

        assertF("-=#-=#-=#-=#\n" +
                "-=#00B00A-=#\n" +
                "-=#001-=#-=#\n" +
                "-=#-=#-=#-=#\n", PLAYER1);

        assertL("╔══┐" +
                "║12│" +
                "║.E│" +
                "└──┘", PLAYER2);

        assertE("----" +
                "-♥♦-" +
                "-♥--" +
                "----", PLAYER2);

        assertF("-=#-=#-=#-=#\n" +
                "-=#00B00A-=#\n" +
                "-=#001-=#-=#\n" +
                "-=#-=#-=#-=#\n", PLAYER2);

        // when
        hero(PLAYER1, 1, 1).right(); // finished
        spreader.tickAll();

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

        assertF("-=#-=#-=#-=#\n" +
                "-=#00B00A-=#\n" +
                "-=#002001-=#\n" +
                "-=#-=#-=#-=#\n", PLAYER1);

        assertL("╔══┐" +
                "║12│" +
                "║.E│" +
                "└──┘", PLAYER2);

        assertE("----" +
                "-♥♦-" +
                "-♥♥-" +
                "----", PLAYER2);

        assertF("-=#-=#-=#-=#\n" +
                "-=#00B00A-=#\n" +
                "-=#002001-=#\n" +
                "-=#-=#-=#-=#\n", PLAYER2);

        // then
        spreader.reloadLevelForWinner(PLAYER1);
        spreader.tickAll();

        assertL("╔══┐" +
                "║12│" +
                "║.E│" +
                "└──┘", PLAYER1);

        assertE("----" +
                "-♥--" +
                "----" +
                "----", PLAYER1);

        assertL("╔══┐" +
                "║12│" +
                "║.E│" +
                "└──┘", PLAYER2);

        assertE("----" +
                "--♦-" +
                "----" +
                "----", PLAYER2);

        // when
        // player2 finished
        hero(PLAYER2, 2, 2).down();
        spreader.tickAll();

        // then
        verifyNoMoreInteractions(PLAYER1);
        verify(PLAYER2).event(Events.WIN(0));
        reset(PLAYER2);

        assertL("╔══┐" +
                "║12│" +
                "║.E│" +
                "└──┘", PLAYER1);

        assertE("----" +
                "-♥--" +
                "----" +
                "----", PLAYER1);

        assertF("-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#\n" +
                "-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#\n", PLAYER1);

        assertL("╔══┐" +
                "║12│" +
                "║.E│" +
                "└──┘", PLAYER2);

        assertE("----" +
                "--♦-" +
                "--♦-" +
                "----", PLAYER2);

        assertF("-=#-=#-=#-=#\n" +
                "-=#-=#00B-=#\n" +
                "-=#-=#001-=#\n" +
                "-=#-=#-=#-=#\n", PLAYER2);

        // when
        spreader.reloadLevelForWinner(PLAYER2);
        spreader.tickAll(); // player2 started

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

        assertF("-=#-=#-=#-=#\n" +
                "-=#00A00A-=#\n" +
                "-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#\n", PLAYER1);

        assertL("╔══┐" +
                "║12│" +
                "║.E│" +
                "└──┘", PLAYER2);

        assertE("----" +
                "-♥♦-" +
                "----" +
                "----", PLAYER2);

        assertF("-=#-=#-=#-=#\n" +
                "-=#00A00A-=#\n" +
                "-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#\n", PLAYER2);

        // when
        hero(PLAYER1, 1, 2).down();
        spreader.tickAll();

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

        assertF("-=#-=#-=#-=#\n" +
                "-=#00B00A-=#\n" +
                "-=#001-=#-=#\n" +
                "-=#-=#-=#-=#\n", PLAYER1);

        assertL("╔══┐" +
                "║12│" +
                "║.E│" +
                "└──┘", PLAYER2);

        assertE("----" +
                "-♥♦-" +
                "-♥--" +
                "----", PLAYER2);

        assertF("-=#-=#-=#-=#\n" +
                "-=#00B00A-=#\n" +
                "-=#001-=#-=#\n" +
                "-=#-=#-=#-=#\n", PLAYER2);

        // when
        hero(PLAYER1, 1, 1).right();
        spreader.tickAll(); // player1 finished

        verify(PLAYER1).event(Events.WIN(0));
        reset(PLAYER1);

        hero(PLAYER2, 2, 2).down();

        spreader.reloadLevelForWinner(PLAYER1);
        spreader.tickAll(); // player1 started // player2 finished


        verify(PLAYER2).event(Events.WIN(0));
        reset(PLAYER2);

        spreader.reloadLevelForWinner(PLAYER2);
        spreader.tickAll(); // player2 started

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

        assertF("-=#-=#-=#-=#\n" +
                "-=#00A00A-=#\n" +
                "-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#\n", PLAYER1);

        assertL("╔══┐" +
                "║12│" +
                "║.E│" +
                "└──┘", PLAYER2);

        assertE("----" +
                "-♥♦-" +
                "----" +
                "----", PLAYER2);

        assertF("-=#-=#-=#-=#\n" +
                "-=#00A00A-=#\n" +
                "-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#\n", PLAYER2);
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

        assertF("-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#\n" +
                "-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#\n", PLAYER1);

        // when done 1 level - go to 2 (single)
        hero(PLAYER1, 1, 2).right();
        spreader.tickAll();
        spreader.reloadLevelForWinner(PLAYER1);
        spreader.tickAll();

        // then
        assertL("╔══┐" +
                "║.1│" +
                "║.E│" +
                "└──┘", PLAYER1);

        assertE("----" +
                "--♥-" +
                "----" +
                "----", PLAYER1);

        assertF("-=#-=#-=#-=#\n" +
                "-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#\n", PLAYER1);

        // when done 2 level - go to 3 (single)
        hero(PLAYER1, 2, 2).down();
        spreader.tickAll();
        spreader.reloadLevelForWinner(PLAYER1);
        spreader.tickAll();

        // then
        assertL("╔══┐" +
                "║..│" +
                "║E1│" +
                "└──┘", PLAYER1);

        assertE("----" +
                "----" +
                "--♥-" +
                "----", PLAYER1);

        assertF("-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#\n" +
                "-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#\n", PLAYER1);

        // when done 3 level - go to 4 (multiple)
        hero(PLAYER1, 2, 1).left();
        spreader.tickAll();
        spreader.reloadLevelForWinner(PLAYER1);
        spreader.tickAll();

        // then
        assertL("╔══┐" +
                "║E.│" +
                "║1.│" +
                "└──┘", PLAYER1);

        assertE("----" +
                "----" +
                "-♥--" +
                "----", PLAYER1);

        assertF("-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#\n" +
                "-=#-=#-=#-=#\n", PLAYER1);

        // when done 4 level - start 4 again (multiple)
        hero(PLAYER1, 1, 1).up();
        spreader.tickAll();
        spreader.reloadLevelForWinner(PLAYER1);
        spreader.tickAll();

        // then
        assertL("╔══┐" +
                "║E.│" +
                "║1.│" +
                "└──┘", PLAYER1);

        assertE("----" +
                "----" +
                "-♥--" +
                "----", PLAYER1);

        assertF("-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#\n" +
                "-=#-=#-=#-=#\n", PLAYER1);

        // when done 4 level - start 4 again multiple)
        hero(PLAYER1, 1, 1).up();
        spreader.tickAll();
        spreader.reloadLevelForWinner(PLAYER1);
        spreader.tickAll();

        // then
        assertL("╔══┐" +
                "║E.│" +
                "║1.│" +
                "└──┘", PLAYER1);

        assertE("----" +
                "----" +
                "-♥--" +
                "----", PLAYER1);

        assertF("-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#\n" +
                "-=#-=#-=#-=#\n", PLAYER1);
    }

    @Test
    public void shouldResetOnMultipleWillResetOnlyMultipleLevel() {
        // given
        shouldAllLevelsAreDone();

        // when
        hero(PLAYER1, 1, 1).reset();
        spreader.reloadLevel(PLAYER1);
        spreader.tickAll();
        spreader.tickAll();

        assertF("-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#\n" +
                "-=#-=#-=#-=#\n", PLAYER1);

        hero(PLAYER1, 1, 1).right();
        spreader.tickAll();

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

        assertF("-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#\n" +
                "-=#00B001-=#\n" +
                "-=#-=#-=#-=#\n", PLAYER1);

        // when
        hero(PLAYER1, 1, 1).reset();
        spreader.reloadLevel(PLAYER1);
        spreader.tickAll();

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

        assertF("-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#\n" +
                "-=#-=#-=#-=#\n", PLAYER1);
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
                "-=#-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n",
                pt(1, 3), PLAYER1);

        // when then
        assertBoardData(
                "{'current':0,'lastPassed':-1,'multiple':false,'scores':true,'total':1}",
                "{'x':0,'y':0}",
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
                "-=#-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n",
                pt(1, 3), PLAYER2);

        // players go to next level
        hero(PLAYER1, 1, 3).right();
        hero(PLAYER2, 1, 3).right();
        spreader.tickAll();

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

        assertF("-=#-=#-=#-=#-=#\n" +
                "-=#00B001-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n", PLAYER1);

        assertF("-=#-=#-=#-=#-=#\n" +
                "-=#00B001-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n", PLAYER2);

        // players started on multiple
        spreader.reloadLevelForWinner(PLAYER1);
        spreader.reloadLevelForWinner(PLAYER2);
        spreader.tickAll();

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

        assertF("-=#-=#-=#-=#-=#\n" +
                "-=#-=#00A-=#-=#\n" +
                "-=#00A-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n", PLAYER1);

        assertF("-=#-=#-=#-=#-=#\n" +
                "-=#-=#00A-=#-=#\n" +
                "-=#00A-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n", PLAYER2);

        // then select different way
        hero(PLAYER1, 1, 2).down();
        hero(PLAYER2, 2, 3).right();
        spreader.tickAll();

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

        assertF("-=#-=#-=#-=#-=#\n" +
                "-=#-=#00B001-=#\n" +
                "-=#00B-=#-=#-=#\n" +
                "-=#001-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n", PLAYER1);

        assertF("-=#-=#-=#-=#-=#\n" +
                "-=#-=#00B001-=#\n" +
                "-=#00B-=#-=#-=#\n" +
                "-=#001-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n", PLAYER2);

        // when then
        assertBoardData(
                "{'current':0,'lastPassed':0,'multiple':true,'scores':true,'total':1}",
                "{'x':0,'y':0}",
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
                "-=#-=#-=#-=#-=#\n" +
                "-=#-=#00B001-=#\n" +
                "-=#00B-=#-=#-=#\n" +
                "-=#001-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n",
                pt(1, 2), PLAYER1);

        // when then
        assertBoardData(
                "{'current':0,'lastPassed':0,'multiple':true,'scores':true,'total':1}",
                "{'x':0,'y':0}",
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
                "-=#-=#-=#-=#-=#\n" +
                "-=#-=#00B001-=#\n" +
                "-=#00B-=#-=#-=#\n" +
                "-=#001-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n",
                pt(2, 3), PLAYER2);
    }

    @Test
    public void testGetBoardAsString_structure() {
        // given
        testGetBoardAsString();

        // when then
        // one more time with structure
        assertEquals("{" +
                "'available':10," +
                "'forces':'-=#-=#-=#-=#-=#-=#-=#00B001-=#-=#00B-=#-=#-=#-=#001-=#-=#-=#-=#-=#-=#-=#-=#'," +
                "'layers':['╔═══┐║.2.│║1..│║..E│└───┘'," +
                "'-------♦♦--♥----♥--------']," +
                "'myBase':{'x':1,'y':2}," +
                "'myColor':0," +
                "'offset':{'x':0,'y':0}," +
                "'round':2," +
                "'rounds':10000," +
                "'tick':2" +
                "}", JsonUtils.toStringSorted(getLayer(PLAYER1)).replace('"', '\''));

        assertEquals("{" +
                "'available':10," +
                "'forces':'-=#-=#-=#-=#-=#-=#-=#00B001-=#-=#00B-=#-=#-=#-=#001-=#-=#-=#-=#-=#-=#-=#-=#'," +
                "'layers':['╔═══┐║.2.│║1..│║..E│└───┘'," +
                "'-------♦♦--♥----♥--------']," +
                "'myBase':{'x':2,'y':3}," +
                "'myColor':1," +
                "'offset':{'x':0,'y':0}," +
                "'round':2," +
                "'rounds':10000," +
                "'tick':2" +
                "}", JsonUtils.toStringSorted(getLayer(PLAYER2)).replace('"', '\''));
    }

    @Test
    public void testGetBoardAsString_structure_roundTicksDisabled() {
        int old = data.roundTicks();
        try {
            data.roundUnlimited();

            // given
            testGetBoardAsString();

            // when then
            String json = JsonUtils.toStringSorted(getLayer(PLAYER1)).replace('"', '\'');
            assertEquals(json, true, json.contains("'round':-1,'rounds':-1,"));

            json = JsonUtils.toStringSorted(getLayer(PLAYER2)).replace('"', '\'');
            assertEquals(json, true, json.contains("'round':-1,'rounds':-1,"));
        } finally {
            data.roundTicks(old);
        }
    }

    @Test
    public void testGetHeroData_heroCoordinateShowOnlyBase() {
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

        // when then
        assertEquals("{'additionalData':{'lastAction':{'increase':[{'count':2,'region':{'x':1,'y':2}}],'movements':[{'count':1,'direction':'DOWN','region':{'x':1,'y':2}}]}}," +
                        "'coordinate':{'x':1,'y':2},'level':0,'multiplayer':true}",
                JsonUtils.clean(JsonUtils.toStringSorted(spreader.single(PLAYER1).getHero())));

        assertEquals("{'additionalData':{'lastAction':{'increase':[{'count':2,'region':{'x':2,'y':3}}],'movements':[{'count':1,'direction':'RIGHT','region':{'x':2,'y':3}}]}}," +
                        "'coordinate':{'x':2,'y':3},'level':0,'multiplayer':true}",
                JsonUtils.clean(JsonUtils.toStringSorted(spreader.single(PLAYER2).getHero())));

        // when
        hero(PLAYER1, 1, 2).right();
        hero(PLAYER2, 3, 3).down();

        spreader.tickAll();

        // then
        assertE("-----" +
                "--♦♦-" +
                "-♥♥♦-" +
                "-♥---" +
                "-----", PLAYER1);

        assertF("-=#-=#-=#-=#-=#\n" +
                "-=#-=#00B002-=#\n" +
                "-=#00C001001-=#\n" +
                "-=#001-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n", PLAYER1);

        assertEquals("{'additionalData':{'lastAction':{'increase':[{'count':2,'region':{'x':1,'y':2}}],'movements':[{'count':1,'direction':'RIGHT','region':{'x':1,'y':2}}]}}," +
                        "'coordinate':{'x':1,'y':2},'level':0,'multiplayer':true}",
                JsonUtils.clean(JsonUtils.toStringSorted(spreader.single(PLAYER1).getHero())));

        assertEquals("{'additionalData':{'lastAction':{'increase':[{'count':2,'region':{'x':3,'y':3}}],'movements':[{'count':1,'direction':'DOWN','region':{'x':3,'y':3}}]}}," +
                        "'coordinate':{'x':2,'y':3},'level':0,'multiplayer':true}",
                JsonUtils.clean(JsonUtils.toStringSorted(spreader.single(PLAYER2).getHero())));
    }

    @Test
    public void testGetHeroData_clearLastActionAfterTick() {
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

        // when move
        hero(PLAYER1, 1, 1).right();
        hero(PLAYER2, 3, 3).down();

        spreader.tickAll();

        // then
        assertEquals("{'additionalData':{'lastAction':{'increase':[{'count':2,'region':{'x':1,'y':1}}]," +
                        "'movements':[{'count':1,'direction':'RIGHT','region':{'x':1,'y':1}}]}}," +
                        "'coordinate':{'x':1,'y':2},'level':0,'multiplayer':true}",
                JsonUtils.clean(JsonUtils.toStringSorted(spreader.single(PLAYER1).getHero())));

        assertEquals("{'additionalData':{'lastAction':{'increase':[{'count':2,'region':{'x':3,'y':3}}]," +
                        "'movements':[{'count':1,'direction':'DOWN','region':{'x':3,'y':3}}]}}," +
                        "'coordinate':{'x':2,'y':3},'level':0,'multiplayer':true}",
                JsonUtils.clean(JsonUtils.toStringSorted(spreader.single(PLAYER2).getHero())));

        // when
        spreader.tickAll();

        // then
        // still here
        assertEquals("{'additionalData':{'lastAction':{}}," +
                        "'coordinate':{'x':1,'y':2},'level':0,'multiplayer':true}",
                JsonUtils.clean(JsonUtils.toStringSorted(spreader.single(PLAYER1).getHero())));

        assertEquals("{'additionalData':{'lastAction':{}}," +
                        "'coordinate':{'x':2,'y':3},'level':0,'multiplayer':true}",
                JsonUtils.clean(JsonUtils.toStringSorted(spreader.single(PLAYER2).getHero())));

        // when
        spreader.tickAll();

        // then
        // removed
        assertEquals("{'additionalData':{'lastAction':{}},'coordinate':{'x':1,'y':2},'level':0,'multiplayer':true}",
                JsonUtils.clean(JsonUtils.toStringSorted(spreader.single(PLAYER1).getHero())));

        assertEquals("{'additionalData':{'lastAction':{}}," +
                        "'coordinate':{'x':2,'y':3},'level':0,'multiplayer':true}",
                JsonUtils.clean(JsonUtils.toStringSorted(spreader.single(PLAYER2).getHero())));
    }

    @Test
    public void testGetBoardAsString_whenBigFrame() {
        // given
        givenSize(LevelsTest.LEVEL_SIZE);
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
                "{'current':0,'lastPassed':-1,'multiple':false,'scores':true,'total':1}",
                "{'x':0,'y':4}",
                "╔═══════════════" +
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
                "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n",
                pt(1, 18), PLAYER1);

        assertBoardData(
                "{'current':0,'lastPassed':-1,'multiple':false,'scores':true,'total':1}",
                "{'x':0,'y':4}",
                "╔═══════════════" +
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
                "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n",
                pt(1, 18), PLAYER2);

        // when
        for (int i = 0; i < 17; i++) {
            hero(PLAYER1, i + 1, 18).right();
            hero(PLAYER2, 1, 18 - i).down();
            spreader.tickAll();
        }

        // then
        assertBoardData(
                "{'current':0,'lastPassed':-1,'multiple':false,'scores':true,'total':1}",
                "{'x':4,'y':4}",
                "═══════════════┐" +
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
                "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "002002002002002002002002002002002002002002001-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n",
                pt(1, 18), PLAYER1);

        assertBoardData(
                "{'current':0,'lastPassed':-1,'multiple':false,'scores':true,'total':1}",
                "{'x':0,'y':0}",
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
                "-=#002-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#002-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#002-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#002-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#002-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#002-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#002-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#002-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#002-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#002-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#002-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#002-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#002-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#002-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#001-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\n",
                pt(1, 18), PLAYER2);
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

        assertF("-=#-=#-=#-=#-=#\n" +
                "-=#-=#00B001-=#\n" +
                "-=#00B-=#-=#-=#\n" +
                "-=#001-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n", PLAYER1);

        assertF("-=#-=#-=#-=#-=#\n" +
                "-=#-=#00B001-=#\n" +
                "-=#00B-=#-=#-=#\n" +
                "-=#001-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n", PLAYER2);

        // when
        spreader.destroy(PLAYER2);

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

//        assertL("╔═══┐" +
//                "║.2.│" +
//                "║1..│" +
//                "║..E│" +
//                "└───┘", PLAYER2);
//
//        assertE("-----" +
//                "-----" +
//                "-♥---" +
//                "-♥---" +
//                "-----", PLAYER2);

        assertF("-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n" +
                "-=#00B-=#-=#-=#\n" +
                "-=#001-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n", PLAYER1);

//        assertF("-=#-=#-=#-=#-=#\n" +
//                "-=#-=#-=#-=#-=#\n" +
//                "-=#00B-=#-=#-=#\n" +
//                "-=#001-=#-=#-=#\n" +
//                "-=#-=#-=#-=#-=#\n", PLAYER2);
    }

}
