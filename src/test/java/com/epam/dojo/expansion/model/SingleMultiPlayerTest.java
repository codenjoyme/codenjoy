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


import com.codenjoy.dojo.utils.JsonUtils;
import com.epam.dojo.expansion.model.levels.LevelsFactory;
import com.epam.dojo.expansion.services.Events;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 4:47
 */
public class SingleMultiPlayerTest extends AbstractSinglePlayersTest {

    public static final String FIRST_SINGLE_LEVEL =
            "╔═════┐" +
            "║1.E..│" +
            "║.....│" +
            "║E...E│" +
            "║.....│" +
            "║..E..│" +
            "└─────┘";
    public static final String MULTIPLE_LEVEL =
            "╔═════┐" +
            "║4.E.1│" +
            "║.....│" +
            "║E...E│" +
            "║.....│" +
            "║3.E.2│" +
            "└─────┘";

    private MultipleGameFactory gameFactory;

    @Override
    protected GameFactory getGameFactory(LevelsFactory single, LevelsFactory multiple) {
        gameFactory = new MultipleGameFactory(single, multiple);
        return gameFactory;
    }

    @Test
    public void shouldEveryHeroHasTheirOwnStartBase() {
        // given
        givenFl(FIRST_SINGLE_LEVEL,
                MULTIPLE_LEVEL);
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
        goMultiple(PLAYER1);

        // then
        // player1 on their own start base
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

        // for player2 nothing will be changed
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
        goMultiple(PLAYER2);

        // then
        // player2 on their own start base
        assertE("-------" +
                "-----♥-" +
                "-------" +
                "-------" +
                "-------" +
                "-----♦-" +
                "-------", PLAYER2);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER1);

        // player1 also sees this results
        assertE("-------" +
                "-----♥-" +
                "-------" +
                "-------" +
                "-------" +
                "-----♦-" +
                "-------", PLAYER1);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER1);

        // when
        // another player3 register
        createOneMorePlayer();
        tickAll();

        // then
        // player1-2 on multiple
        assertE("-------" +
                "-----♥-" +
                "-------" +
                "-------" +
                "-------" +
                "-----♦-" +
                "-------", PLAYER1);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER1);

        assertE("-------" +
                "-----♥-" +
                "-------" +
                "-------" +
                "-------" +
                "-----♦-" +
                "-------", PLAYER2);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER2);

        assertE("-------" +
                "-♥-----" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------", PLAYER3);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER3);

        // when
        goMultiple(PLAYER3);

        // then all they are on multiple
        assertE("-------" +
                "-----♥-" +
                "-------" +
                "-------" +
                "-------" +
                "-♣---♦-" +
                "-------", PLAYER3);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER3);

        // when
        // another player4 registered
        createOneMorePlayer();
        tickAll();
        goMultiple(PLAYER4);

        // then all they are on multiple
        assertE("-------" +
                "-♠---♥-" +
                "-------" +
                "-------" +
                "-------" +
                "-♣---♦-" +
                "-------", PLAYER4);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER4);
    }

    @Test
    public void shouldIfThereAreMoreThan4PlayersThenCreateNewMultiple() {
        // given
        shouldEveryHeroHasTheirOwnStartBase();

        // when
        // another player5 registered
        createOneMorePlayer();
        tickAll();

        goMultiple(PLAYER5);

        // then he is on their own multiple board
        assertE("-------" +
                "-----♥-" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------", PLAYER5);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER5);

        // then all 4 players are on their multiple
        assertE("-------" +
                "-♠---♥-" +
                "-------" +
                "-------" +
                "-------" +
                "-♣---♦-" +
                "-------", PLAYER1);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER1);

        // when
        // another player6 registered
        createOneMorePlayer();
        tickAll();

        goMultiple(PLAYER6);

        // then all they are on multiple
        assertE("-------" +
                "-----♥-" +
                "-------" +
                "-------" +
                "-------" +
                "-----♦-" +
                "-------", PLAYER5);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER5);

        // when
        // another player7 registered
        createOneMorePlayer();
        tickAll();

        goMultiple(PLAYER7);

        // then all they are on multiple
        assertE("-------" +
                "-----♥-" +
                "-------" +
                "-------" +
                "-------" +
                "-♣---♦-" +
                "-------", PLAYER5);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER5);

        // when
        // another player8 registered
        createOneMorePlayer();
        tickAll();

        goMultiple(PLAYER8);

        // then all they are on multiple
        assertE("-------" +
                "-♠---♥-" +
                "-------" +
                "-------" +
                "-------" +
                "-♣---♦-" +
                "-------", PLAYER5);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER5);

        // when
        // another player9 player10 registered
        createOneMorePlayer();
        tickAll();

        goMultiple(PLAYER9);

        createOneMorePlayer();
        tickAll();

        goMultiple(PLAYER10);


        // then all they are on multiple
        assertE("-------" +
                "-----♥-" +
                "-------" +
                "-------" +
                "-------" +
                "-----♦-" +
                "-------", PLAYER9);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER9);

        // when
        // check that we are on different boards
        hero(PLAYER1, 5, 5).left();
        hero(PLAYER2, 5, 1).up();
        hero(PLAYER3, 1, 1).right();
        hero(PLAYER4, 1, 5).down();

        hero(PLAYER5, 5, 5).down();
        hero(PLAYER6, 5, 1).left();
        hero(PLAYER7, 1, 1).up();
        hero(PLAYER8, 1, 5).right();

        hero(PLAYER9, 5, 5).down();
        hero(PLAYER10, 5, 1).up();
        tickAll();

        // then
        assertE("-------" +
                "-♠--♥♥-" +
                "-♠-----" +
                "-------" +
                "-----♦-" +
                "-♣♣--♦-" +
                "-------", PLAYER1);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#00B-=#-=#00100B-=#\n" +
                "-=#001-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#001-=#\n" +
                "-=#00B001-=#-=#00B-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER1);

        assertE("-------" +
                "-♠♠--♥-" +
                "-----♥-" +
                "-------" +
                "-♣-----" +
                "-♣--♦♦-" +
                "-------", PLAYER5);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#00B001-=#-=#00B-=#\n" +
                "-=#-=#-=#-=#-=#001-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#001-=#-=#-=#-=#-=#\n" +
                "-=#00B-=#-=#00100B-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER5);

        assertE("-------" +
                "-----♥-" +
                "-----♥-" +
                "-------" +
                "-----♦-" +
                "-----♦-" +
                "-------", PLAYER9);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#00B-=#\n" +
                "-=#-=#-=#-=#-=#001-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#001-=#\n" +
                "-=#-=#-=#-=#-=#00B-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER9);
    }

    private void goMultiple(int player) {
        hero(player, 1, 5).right();
        tickAll();

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#00B001-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", player);

        hero(player, 2, 5).right();
        tickAll();

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#00B002001-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", player);

        verify(player).event(Events.WIN(0));
        reset(player);
        verifyNoMoreInteractions(player);

        tickAll();
    }

    @Test
    public void shouldIfOneOfManyUsersWillLeaveAnotherOneGoOnThisFreePlace() {
        // given
        shouldIfThereAreMoreThan4PlayersThenCreateNewMultiple();

        // when
        destroy(PLAYER5);
        destroy(PLAYER9);
        destroy(PLAYER2);
        tickAll();

        // then
        assertE("-------" +
                "-♠--♥♥-" +
                "-♠-----" +
                "-------" +
                "-------" +
                "-♣♣----" +
                "-------", PLAYER1);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#00B-=#-=#00100B-=#\n" +
                "-=#001-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#00B001-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER1);

        assertE("-------" +
                "-♠♠----" +
                "-------" +
                "-------" +
                "-♣-----" +
                "-♣--♦♦-" +
                "-------", PLAYER5);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#00B001-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#001-=#-=#-=#-=#-=#\n" +
                "-=#00B-=#-=#00100B-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER5);

        assertE("-------" +
                "-------" +
                "-------" +
                "-------" +
                "-----♦-" +
                "-----♦-" +
                "-------", PLAYER9);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#001-=#\n" +
                "-=#-=#-=#-=#-=#00B-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER9);

        // when
        // another player11 registered
        createOneMorePlayer();
        tickAll();

        goMultiple(PLAYER11);

        // then
        // it will be in the first room
        assertE("-------" +
                "-♠--♥♥-" +
                "-♠-----" +
                "-------" +
                "-------" +
                "-♣♣--♦-" +
                "-------", PLAYER1);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#00B-=#-=#00100B-=#\n" +
                "-=#001-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#00B001-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER1);

        // when
        // another player12 registered
        createOneMorePlayer();
        tickAll();

        goMultiple(PLAYER12);

        // then
        // it will be in the second room
        assertE("-------" +
                "-♠♠--♥-" +
                "-------" +
                "-------" +
                "-♣-----" +
                "-♣--♦♦-" +
                "-------", PLAYER5);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#00B001-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#001-=#-=#-=#-=#-=#\n" +
                "-=#00B-=#-=#00100B-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER5);

        // when
        // another player13 registered
        createOneMorePlayer();
        tickAll();

        goMultiple(PLAYER13);

        // then
        // it will be in the third room
        assertE("-------" +
                "-----♥-" +
                "-------" +
                "-------" +
                "-----♦-" +
                "-----♦-" +
                "-------", PLAYER9);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#001-=#\n" +
                "-=#-=#-=#-=#-=#00B-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER9);

        // when
        // another player14 registered
        createOneMorePlayer();
        tickAll();

        goMultiple(PLAYER14);

        // then
        // it will be in the third room
        assertE("-------" +
                "-----♥-" +
                "-------" +
                "-------" +
                "-----♦-" +
                "-♣---♦-" +
                "-------", PLAYER9);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#001-=#\n" +
                "-=#00A-=#-=#-=#00B-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER9);

        // when
        // another player15 registered
        createOneMorePlayer();
        tickAll();

        goMultiple(PLAYER15);

        // then
        // it will be in the third room
        assertE("-------" +
                "-♠---♥-" +
                "-------" +
                "-------" +
                "-----♦-" +
                "-♣---♦-" +
                "-------", PLAYER9);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#001-=#\n" +
                "-=#00A-=#-=#-=#00B-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER9);

        // when
        // another player15 registered
        createOneMorePlayer();
        tickAll();

        goMultiple(PLAYER16);

        // then
        // it will be in the fourth room
        assertE("-------" +
                "-----♥-" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------", PLAYER16);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER16);
    }

    @Test
    public void shouldOnlyMultipleLevel() {
        // given
        givenFl(MULTIPLE_LEVEL);
        createPlayers(4);

        // then
        assertE("-------" +
                "-♠---♥-" +
                "-------" +
                "-------" +
                "-------" +
                "-♣---♦-" +
                "-------", PLAYER1);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER1);
    }

    @Test
    public void shouldNoWaitTillAllPlayersCollectedTogether_ifNoSpecialMode() {
        // given
        givenFl(MULTIPLE_LEVEL);
        gameFactory.setWaitingOthers(false);
        createPlayers(3);

        assertE("-------" +
                "-----♥-" +
                "-------" +
                "-------" +
                "-------" +
                "-♣---♦-" +
                "-------", PLAYER1);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER1);

        // when
        // try to go
        hero(PLAYER3, 1, 1).up();

        tickAll();

        // then
        // can do it
        assertE("-------" +
                "-----♥-" +
                "-------" +
                "-------" +
                "-♣-----" +
                "-♣---♦-" +
                "-------", PLAYER1);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#001-=#-=#-=#-=#-=#\n" +
                "-=#00B-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER1);
    }

    @Test
    public void shouldWaitTillAllPlayersCollectedTogether_ifSpecialMode() {
        // given
        givenFl(MULTIPLE_LEVEL);
        gameFactory.setWaitingOthers(true);
        createPlayers(3);

        assertE("-------" +
                "-----♥-" +
                "-------" +
                "-------" +
                "-------" +
                "-♣---♦-" +
                "-------", PLAYER1);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER1);

        // when
        // try to go
        hero(PLAYER3, 1, 1).up();

        tickAll();

        // then
        // cant do it
        assertE("-------" +
                "-----♥-" +
                "-------" +
                "-------" +
                "-------" +
                "-♣---♦-" +
                "-------", PLAYER1);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER1);

        // but find the attempt
        assertEquals("{'additionalData':{'lastAction':{'increase':[{'count':2,'region':{'x':1,'y':1}}]," +
                        "'movements':[{'count':1,'direction':'UP','region':{'x':1,'y':1}}]}}," +
                        "'coordinate':{'x':1,'y':1},'level':0,'singleBoardGame':true}",
                JsonUtils.clean(JsonUtils.toStringSorted(single(PLAYER3).getHero())));

        // when
        // but if register one more player
        createPlayers(1);

        assertE("-------" +
                "-♠---♥-" +
                "-------" +
                "-------" +
                "-------" +
                "-♣---♦-" +
                "-------", PLAYER1);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER1);

        // when
        // try to go
        hero(PLAYER3, 1, 1).up();

        tickAll();

        // then
        // can do it
        assertE("-------" +
                "-♠---♥-" +
                "-------" +
                "-------" +
                "-♣-----" +
                "-♣---♦-" +
                "-------", PLAYER1);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#001-=#-=#-=#-=#-=#\n" +
                "-=#00B-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER1);
    }
}
