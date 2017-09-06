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


import com.codenjoy.dojo.services.QDirection;
import com.codenjoy.dojo.utils.JsonUtils;
import com.epam.dojo.expansion.model.levels.LevelsFactory;
import com.epam.dojo.expansion.services.Events;
import org.junit.Test;

import static com.codenjoy.dojo.services.PointImpl.pt;
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

    @Test
    public void shouldOnlyOnePlayerWins() {
        // given
        givenFl("╔═════┐" +
                "║4...1│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "║3...2│" +
                "└─────┘");
        gameFactory.setWaitingOthers(true);
        createPlayers(4);

        // when then
        atackFirstPlayer();
        atackSecondPlayer();
        attackThirdPlayerAndWin();
    }

    private void attackThirdPlayerAndWin() {
        hero(PLAYER3, 4, 1).right();
        tickAll();

        // move to another enemy
        for (int i = 0; i < 20; i++) {
            hero(PLAYER3, 5, 1).up();
            tickAll();

            hero(PLAYER3, 5, 2).up();
            tickAll();

            hero(PLAYER3, 5, 3).up();
            tickAll();
        }

        assertE("-------" +
                "-----♥-" +
                "-♣---♣-" +
                "-♣---♣-" +
                "-♣---♣-" +
                "-♣♣♣♣♣-" +
                "-------", PLAYER1);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#00A-=#\n" +
                "-=#00A-=#-=#-=#00K-=#\n" +
                "-=#014-=#-=#-=#014-=#\n" +
                "-=#014-=#-=#-=#014-=#\n" +
                "-=#01E01401400B00L-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER1);

        verifyNoMoreInteractions(PLAYER1);
        verifyNoMoreInteractions(PLAYER2);
        verifyNoMoreInteractions(PLAYER3);
        verifyNoMoreInteractions(PLAYER4);

        // kill this enemy
        hero(PLAYER3).move(new ForcesMoves(pt(5, 4), 10, QDirection.UP));
        tickAll();
        tickAll();

        verify(PLAYER1).event(Events.LOOSE());
        verifyNoMoreInteractions(PLAYER2);
        verify(PLAYER3).event(Events.WIN(1));
        verifyNoMoreInteractions(PLAYER4);

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

        tickAll();

        verifyNoMoreInteractions(PLAYER1);
        verifyNoMoreInteractions(PLAYER2);
        verifyNoMoreInteractions(PLAYER3);
        verifyNoMoreInteractions(PLAYER4);
    }

    private void atackSecondPlayer() {
        // move to another enemy
        for (int i = 0; i < 20; i++) {
            hero(PLAYER3, 1, 1).right();
            tickAll();

            hero(PLAYER3, 2, 1).right();
            tickAll();

            hero(PLAYER3, 3, 1).right();
            tickAll();
        }

        assertE("-------" +
                "-----♥-" +
                "-♣-----" +
                "-♣-----" +
                "-♣-----" +
                "-♣♣♣♣♦-" +
                "-------", PLAYER1);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#00A-=#\n" +
                "-=#00A-=#-=#-=#-=#-=#\n" +
                "-=#014-=#-=#-=#-=#-=#\n" +
                "-=#014-=#-=#-=#-=#-=#\n" +
                "-=#01E01401400K00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER1);

        verifyNoMoreInteractions(PLAYER1);
        verifyNoMoreInteractions(PLAYER2);
        verifyNoMoreInteractions(PLAYER3);
        verifyNoMoreInteractions(PLAYER4);

        // kill this enemy
        hero(PLAYER3).move(new ForcesMoves(pt(4, 1), 10, QDirection.RIGHT));
        tickAll();
        tickAll();

        verifyNoMoreInteractions(PLAYER1);
        verify(PLAYER2).event(Events.LOOSE());
        verifyNoMoreInteractions(PLAYER3);
        verifyNoMoreInteractions(PLAYER4);

        assertE("-------" +
                "-----♥-" +
                "-♣-----" +
                "-♣-----" +
                "-♣-----" +
                "-♣♣♣♣--" +
                "-------", PLAYER1);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#00A-=#\n" +
                "-=#00A-=#-=#-=#-=#-=#\n" +
                "-=#014-=#-=#-=#-=#-=#\n" +
                "-=#014-=#-=#-=#-=#-=#\n" +
                "-=#01E01401400A-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER1);
    }

    private void atackFirstPlayer() {
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

        // move to enemy
        for (int i = 0; i < 20; i++) {
            hero(PLAYER3, 1, 1).up();
            tickAll();

            hero(PLAYER3, 1, 2).up();
            tickAll();

            hero(PLAYER3, 1, 3).up();
            tickAll();
        }

        assertE("-------" +
                "-♠---♥-" +
                "-♣-----" +
                "-♣-----" +
                "-♣-----" +
                "-♣---♦-" +
                "-------", PLAYER1);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#-=#00A-=#\n" +
                "-=#00K-=#-=#-=#-=#-=#\n" +
                "-=#014-=#-=#-=#-=#-=#\n" +
                "-=#014-=#-=#-=#-=#-=#\n" +
                "-=#00U-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER1);

        verifyNoMoreInteractions(PLAYER1);
        verifyNoMoreInteractions(PLAYER2);
        verifyNoMoreInteractions(PLAYER3);
        verifyNoMoreInteractions(PLAYER4);

        // kill this enemy
        hero(PLAYER3).move(new ForcesMoves(pt(1, 4), 10, QDirection.UP));
        tickAll();
        tickAll();

        verifyNoMoreInteractions(PLAYER1);
        verifyNoMoreInteractions(PLAYER2);
        verifyNoMoreInteractions(PLAYER3);
        verify(PLAYER4).event(Events.LOOSE());

        assertE("-------" +
                "-----♥-" +
                "-♣-----" +
                "-♣-----" +
                "-♣-----" +
                "-♣---♦-" +
                "-------", PLAYER1);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#00A-=#\n" +
                "-=#00A-=#-=#-=#-=#-=#\n" +
                "-=#014-=#-=#-=#-=#-=#\n" +
                "-=#014-=#-=#-=#-=#-=#\n" +
                "-=#00U-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER1);
    }

    @Test
    public void shouldLosePlayersCanGetInfoAboutGame() {
        // given
        givenFl("╔═════┐" +
                "║4...1│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "║3...2│" +
                "└─────┘");
        gameFactory.setWaitingOthers(true);
        createPlayers(4);


        // when
        atackFirstPlayer();

        // and go to new base
        hero(PLAYER3, 1, 4).up();
        tickAll();

        assertE("-------" +
                "-♣---♥-" +
                "-♣-----" +
                "-♣-----" +
                "-♣-----" +
                "-♣---♦-" +
                "-------", PLAYER1);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#001-=#-=#-=#00A-=#\n" +
                "-=#00B-=#-=#-=#-=#-=#\n" +
                "-=#014-=#-=#-=#-=#-=#\n" +
                "-=#014-=#-=#-=#-=#-=#\n" +
                "-=#00U-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER1);

        // then
        assertBoardData(PLAYER1, "{'forces':'-=#-=#-=#-=#-=#-=#-=#-=#001-=#-=#-=#00A-=#-=#00B-=#-=#-=#-=#-=#-=#014-=#-=#-=#-=#-=#-=#014-=#-=#-=#-=#-=#-=#00U-=#-=#-=#00A-=#-=#-=#-=#-=#-=#-=#-=#','available':10,'layers':['╔═════┐║4...1│║.....│║.....│║.....│║3...2│└─────┘','--------♣---♥--♣------♣------♣------♣---♦--------'],'levelProgress':{'current':0,'lastPassed':0,'multiple':true,'scores':true,'total':0},'myBase':{'x':5,'y':5},'myColor':0,'offset':{'x':0,'y':0},'onlyMyName':false,'showName':true}");
        assertBoardData(PLAYER2, "{'forces':'-=#-=#-=#-=#-=#-=#-=#-=#001-=#-=#-=#00A-=#-=#00B-=#-=#-=#-=#-=#-=#014-=#-=#-=#-=#-=#-=#014-=#-=#-=#-=#-=#-=#00U-=#-=#-=#00A-=#-=#-=#-=#-=#-=#-=#-=#','available':10,'layers':['╔═════┐║4...1│║.....│║.....│║.....│║3...2│└─────┘','--------♣---♥--♣------♣------♣------♣---♦--------'],'levelProgress':{'current':0,'lastPassed':0,'multiple':true,'scores':true,'total':0},'myBase':{'x':5,'y':1},'myColor':1,'offset':{'x':0,'y':0},'onlyMyName':false,'showName':true}");
        assertBoardData(PLAYER3, "{'forces':'-=#-=#-=#-=#-=#-=#-=#-=#001-=#-=#-=#00A-=#-=#00B-=#-=#-=#-=#-=#-=#014-=#-=#-=#-=#-=#-=#014-=#-=#-=#-=#-=#-=#00U-=#-=#-=#00A-=#-=#-=#-=#-=#-=#-=#-=#','available':10,'layers':['╔═════┐║4...1│║.....│║.....│║.....│║3...2│└─────┘','--------♣---♥--♣------♣------♣------♣---♦--------'],'levelProgress':{'current':0,'lastPassed':0,'multiple':true,'scores':true,'total':0},'myBase':{'x':1,'y':1},'myColor':2,'offset':{'x':0,'y':0},'onlyMyName':false,'showName':true}");
        assertBoardData(PLAYER4, "{'forces':'-=#-=#-=#-=#-=#-=#-=#-=#001-=#-=#-=#00A-=#-=#00B-=#-=#-=#-=#-=#-=#014-=#-=#-=#-=#-=#-=#014-=#-=#-=#-=#-=#-=#00U-=#-=#-=#00A-=#-=#-=#-=#-=#-=#-=#-=#','available':10,'layers':['╔═════┐║4...1│║.....│║.....│║.....│║3...2│└─────┘','--------♣---♥--♣------♣------♣------♣---♦--------'],'levelProgress':{'current':0,'lastPassed':0,'multiple':true,'scores':true,'total':0},'myBase':{'x':1,'y':5},'myColor':3,'offset':{'x':0,'y':0},'onlyMyName':false,'showName':true}");
    }

    @Test
    public void shouldLosePlayersCanGetInfoAboutGameAfterRenew() {
        // given
        shouldOnlyOnePlayerWins();

        assertBoardData(PLAYER1, "{'forces':'-=#-=#-=#-=#-=#-=#-=#-=#00A-=#-=#-=#00A-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#00A-=#-=#-=#00A-=#-=#-=#-=#-=#-=#-=#-=#','available':10,'layers':['╔═════┐║4...1│║.....│║.....│║.....│║3...2│└─────┘','--------♠---♥-----------------------♣---♦--------'],'levelProgress':{'current':0,'lastPassed':0,'multiple':true,'scores':true,'total':0},'myBase':{'x':5,'y':5},'myColor':0,'offset':{'x':0,'y':0},'onlyMyName':false,'showName':true}");
        assertBoardData(PLAYER2, "{'forces':'-=#-=#-=#-=#-=#-=#-=#-=#00A-=#-=#-=#00A-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#00A-=#-=#-=#00A-=#-=#-=#-=#-=#-=#-=#-=#','available':10,'layers':['╔═════┐║4...1│║.....│║.....│║.....│║3...2│└─────┘','--------♠---♥-----------------------♣---♦--------'],'levelProgress':{'current':0,'lastPassed':0,'multiple':true,'scores':true,'total':0},'myBase':{'x':5,'y':1},'myColor':1,'offset':{'x':0,'y':0},'onlyMyName':false,'showName':true}");
        assertBoardData(PLAYER3, "{'forces':'-=#-=#-=#-=#-=#-=#-=#-=#00A-=#-=#-=#00A-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#00A-=#-=#-=#00A-=#-=#-=#-=#-=#-=#-=#-=#','available':10,'layers':['╔═════┐║4...1│║.....│║.....│║.....│║3...2│└─────┘','--------♠---♥-----------------------♣---♦--------'],'levelProgress':{'current':0,'lastPassed':0,'multiple':true,'scores':true,'total':0},'myBase':{'x':1,'y':1},'myColor':2,'offset':{'x':0,'y':0},'onlyMyName':false,'showName':true}");
        assertBoardData(PLAYER4, "{'forces':'-=#-=#-=#-=#-=#-=#-=#-=#00A-=#-=#-=#00A-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#00A-=#-=#-=#00A-=#-=#-=#-=#-=#-=#-=#-=#','available':10,'layers':['╔═════┐║4...1│║.....│║.....│║.....│║3...2│└─────┘','--------♠---♥-----------------------♣---♦--------'],'levelProgress':{'current':0,'lastPassed':0,'multiple':true,'scores':true,'total':0},'myBase':{'x':1,'y':5},'myColor':3,'offset':{'x':0,'y':0},'onlyMyName':false,'showName':true}");
    }

    @Test
    public void shouldSeveralForcesGoOnOneCell_case4Attackers() {
        // given
        givenFl("╔═══┐" +
                "║.1.│" +
                "║3.2│" +
                "║.4.│" +
                "└───┘");
        gameFactory.setWaitingOthers(false);
        createPlayers(4);

        assertE("-----" +
                "--♥--" +
                "-♣-♦-" +
                "--♠--" +
                "-----", PLAYER1);

        assertF("-=#-=#-=#-=#-=#\n" +
                "-=#-=#00A-=#-=#\n" +
                "-=#00A-=#00A-=#\n" +
                "-=#-=#00A-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n", PLAYER1);

        // when
        hero(PLAYER1, 2, 3).down();
        hero(PLAYER2, 3, 2).left();
        hero(PLAYER3, 1, 2).right();
        hero(PLAYER4, 2, 1).up();

        tickAll();

        // then
        assertE("-----" +
                "--♥--" +
                "-♣-♦-" +
                "--♠--" +
                "-----", PLAYER1);

        assertF("-=#-=#-=#-=#-=#\n" +
                "-=#-=#00B-=#-=#\n" +
                "-=#00B-=#00B-=#\n" +
                "-=#-=#00B-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n", PLAYER1);
    }

    @Test
    public void shouldSeveralForcesGoOnOneCell_case3Attackers() {
        // given
        givenFl("╔═══┐" +
                "║.1.│" +
                "║3.2│" +
                "║.4.│" +
                "└───┘");
        gameFactory.setWaitingOthers(false);
        createPlayers(4);

        assertE("-----" +
                "--♥--" +
                "-♣-♦-" +
                "--♠--" +
                "-----", PLAYER1);

        assertF("-=#-=#-=#-=#-=#\n" +
                "-=#-=#00A-=#-=#\n" +
                "-=#00A-=#00A-=#\n" +
                "-=#-=#00A-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n", PLAYER1);

        // when
        hero(PLAYER1, 2, 3).down();
        hero(PLAYER2, 3, 2).left();
        hero(PLAYER3, 1, 2).right();

        tickAll();

        // then
        assertE("-----" +
                "--♥--" +
                "-♣-♦-" +
                "--♠--" +
                "-----", PLAYER1);

        assertF("-=#-=#-=#-=#-=#\n" +
                "-=#-=#00B-=#-=#\n" +
                "-=#00B-=#00B-=#\n" +
                "-=#-=#00A-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n", PLAYER1);
    }

    @Test
    public void shouldSeveralForcesGoOnOneCell_goWhereSomeoneAlreadyIs_case4Attackers_oneWin() {
        // given
        givenFl("╔═══┐" +
                "║.1.│" +
                "║3.2│" +
                "║.4.│" +
                "└───┘");
        gameFactory.setWaitingOthers(false);
        createPlayers(4);

        hero(PLAYER1, 2, 3).down();
        tickAll();

        assertE("-----" +
                "--♥--" +
                "-♣♥♦-" +
                "--♠--" +
                "-----", PLAYER1);

        assertF("-=#-=#-=#-=#-=#\n" +
                "-=#-=#00B-=#-=#\n" +
                "-=#00A00100A-=#\n" +
                "-=#-=#00A-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n", PLAYER1);

        // when
        hero(PLAYER1, 2, 3).down();
        hero(PLAYER2, 3, 2).left();
        hero(PLAYER3, 1, 2).right();
        hero(PLAYER4, 2, 1).up();

        tickAll();

        // then
        assertE("-----" +
                "--♥--" +
                "-♣♥♦-" +
                "--♠--" +
                "-----", PLAYER1);

        assertF("-=#-=#-=#-=#-=#\n" +
                "-=#-=#00C-=#-=#\n" +
                "-=#00B00100B-=#\n" +
                "-=#-=#00B-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n", PLAYER1);
    }

    @Test
    public void shouldSeveralForcesGoOnOneCell_goWhereSomeoneAlreadyIs_case3Attackers_removeEachOther() {
        // given
        givenFl("╔═══┐" +
                "║.1.│" +
                "║3.2│" +
                "║.4.│" +
                "└───┘");
        gameFactory.setWaitingOthers(false);
        createPlayers(4);

        hero(PLAYER1, 2, 3).down();
        tickAll();

        assertE("-----" +
                "--♥--" +
                "-♣♥♦-" +
                "--♠--" +
                "-----", PLAYER1);

        assertF("-=#-=#-=#-=#-=#\n" +
                "-=#-=#00B-=#-=#\n" +
                "-=#00A00100A-=#\n" +
                "-=#-=#00A-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n", PLAYER1);

        // when
        hero(PLAYER2, 3, 2).left();
        hero(PLAYER3, 1, 2).right();
        hero(PLAYER4, 2, 1).up();

        tickAll();

        // then
        assertE("-----" +
                "--♥--" +
                "-♣-♦-" +
                "--♠--" +
                "-----", PLAYER1);

        assertF("-=#-=#-=#-=#-=#\n" +
                "-=#-=#00B-=#-=#\n" +
                "-=#00B-=#00B-=#\n" +
                "-=#-=#00B-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n", PLAYER1);
    }

    @Test
    public void shouldSeveralForcesGoOnOneCell_goWhereSomeoneAlreadyIs_case3Attackers_notEnoughForces() {
        // given
        givenFl("╔═══┐" +
                "║.1.│" +
                "║3.2│" +
                "║.4.│" +
                "└───┘");
        gameFactory.setWaitingOthers(false);
        createPlayers(4);

        hero(PLAYER1, 2, 3).down();
        tickAll();

        hero(PLAYER1, 2, 3).down();
        tickAll();

        assertE("-----" +
                "--♥--" +
                "-♣♥♦-" +
                "--♠--" +
                "-----", PLAYER1);

        assertF("-=#-=#-=#-=#-=#\n" +
                "-=#-=#00C-=#-=#\n" +
                "-=#00A00200A-=#\n" +
                "-=#-=#00A-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n", PLAYER1);

        // when
        hero(PLAYER2, 3, 2).left();
        hero(PLAYER3, 1, 2).right();
        hero(PLAYER4, 2, 1).up();

        tickAll();

        // then
        assertE("-----" +
                "--♥--" +
                "-♣♥♦-" +
                "--♠--" +
                "-----", PLAYER1);

        assertF("-=#-=#-=#-=#-=#\n" +
                "-=#-=#00C-=#-=#\n" +
                "-=#00B00100B-=#\n" +
                "-=#-=#00B-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n", PLAYER1);
    }

    @Test
    public void shouldAttackEachOther_stayOnBases() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.12│" +
                "║...│" +
                "└───┘");
        gameFactory.setWaitingOthers(false);
        createPlayers(2);

        assertE("-----" +
                "-----" +
                "--♥♦-" +
                "-----" +
                "-----", PLAYER1);

        assertF("-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n" +
                "-=#-=#00A00A-=#\n" +
                "-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n", PLAYER1);

        // when
        hero(PLAYER1).move(new ForcesMoves(pt(2, 2), 10, QDirection.RIGHT));
        hero(PLAYER2).move(new ForcesMoves(pt(3, 2), 10, QDirection.LEFT));
        tickAll();

        // then
        assertE("-----" +
                "-----" +
                "--♦♥-" +
                "-----" +
                "-----", PLAYER1);

        assertF("-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n" +
                "-=#-=#008008-=#\n" +
                "-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n", PLAYER1);
    }

    @Test
    public void shouldAttackEachOther_stayOnNeutralTerritories_exchange() {
        // given
        givenFl("╔════┐" +
                "║....│" +
                "║1..2│" +
                "║....│" +
                "║....│" +
                "└────┘");
        gameFactory.setWaitingOthers(false);
        createPlayers(2);

        assertE("------" +
                "------" +
                "-♥--♦-" +
                "------" +
                "------" +
                "------", PLAYER1);

        assertF("-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n", PLAYER1);

        // when
        hero(PLAYER1).move(new ForcesMoves(pt(1, 3), 9, QDirection.RIGHT));
        hero(PLAYER2).move(new ForcesMoves(pt(4, 3), 9, QDirection.LEFT));
        tickAll();

        // then
        assertE("------" +
                "------" +
                "-♥♥♦♦-" +
                "------" +
                "------" +
                "------", PLAYER1);

        assertF("-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#001009009001-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n", PLAYER1);

        // when
        hero(PLAYER1).move(new ForcesMoves(pt(2, 3), 8, QDirection.RIGHT));
        hero(PLAYER2).move(new ForcesMoves(pt(3, 3), 8, QDirection.LEFT));
        tickAll();


        // then
        assertE("------" +
                "------" +
                "-♥♦♥♦-" +
                "------" +
                "------" +
                "------", PLAYER1);

        assertF("-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#001007007001-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n", PLAYER1);

    }

    @Test
    public void shouldAttackEachOther_stayOnNeutralTerritories_attack() {
        // given
        givenFl("╔════┐" +
                "║....│" +
                "║1..2│" +
                "║....│" +
                "║....│" +
                "└────┘");
        gameFactory.setWaitingOthers(false);
        createPlayers(2);

        assertE("------" +
                "------" +
                "-♥--♦-" +
                "------" +
                "------" +
                "------", PLAYER1);

        assertF("-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n", PLAYER1);

        // when
        hero(PLAYER1).move(new ForcesMoves(pt(1, 3), 9, QDirection.RIGHT));
        hero(PLAYER2).move(new ForcesMoves(pt(4, 3), 9, QDirection.LEFT));
        tickAll();

        // then
        assertE("------" +
                "------" +
                "-♥♥♦♦-" +
                "------" +
                "------" +
                "------", PLAYER1);

        assertF("-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#001009009001-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n", PLAYER1);

        // when
        hero(PLAYER1).move(new ForcesMoves(pt(2, 3), 5, QDirection.RIGHT));
        hero(PLAYER2).move(new ForcesMoves(pt(3, 3), 5, QDirection.LEFT));
        tickAll();


        // then
        assertE("------" +
                "------" +
                "-♥♦♥♦-" +
                "------" +
                "------" +
                "------", PLAYER1);

        assertF("-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#001001001001-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n", PLAYER1);

    }

    @Test
    public void shouldFirstPlayerWinWithoutErrors() {
        // given
        givenFl("╔════┐" +
                "║....│" +
                "║1..2│" +
                "║....│" +
                "║....│" +
                "└────┘");
        gameFactory.setWaitingOthers(false);
        createPlayers(2);

        assertE("------" +
                "------" +
                "-♥--♦-" +
                "------" +
                "------" +
                "------", PLAYER1);

        assertF("-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n", PLAYER1);

        // when
        hero(PLAYER1).move(new ForcesMoves(pt(1, 3), 9, QDirection.RIGHT));
        tickAll();

        hero(PLAYER1).move(new ForcesMoves(pt(2, 3), 8, QDirection.RIGHT));
        tickAll();

        hero(PLAYER1).increaseAndMove(
                new Forces(pt(3, 3), 10),
                new ForcesMoves(pt(3, 3), 17, QDirection.RIGHT));
        tickAll();

        // then
        assertE("------" +
                "------" +
                "-♥♥♥♥-" +
                "------" +
                "------" +
                "------", PLAYER1);

        assertF("-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#001001001007-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n", PLAYER1);

        verifyNoMoreInteractions(PLAYER1);
        verifyNoMoreInteractions(PLAYER2);

        tickAll();

        // then
        assertE("------" +
                "------" +
                "-♥--♦-" +
                "------" +
                "------" +
                "------", PLAYER1);

        assertF("-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n", PLAYER1);

        verify(PLAYER1).event(Events.WIN(1));
        verify(PLAYER2).event(Events.LOOSE());

        // when
        tickAll();

        verifyNoMoreInteractions(PLAYER1);
        verifyNoMoreInteractions(PLAYER2);
    }

    @Test
    public void shouldResetOnePlayerAfterSeveralSteps() {
        // given
        givenFl("╔═════┐" +
                "║4...1│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "║3...2│" +
                "└─────┘");
        gameFactory.setWaitingOthers(true);
        createPlayers(4);

        hero(PLAYER1, 5, 5).down();
        hero(PLAYER2, 5, 1).up();
        hero(PLAYER3, 1, 1).up();
        hero(PLAYER4, 1, 5).down();

        tickAll();

        assertE("-------" +
                "-♠---♥-" +
                "-♠---♥-" +
                "-------" +
                "-♣---♦-" +
                "-♣---♦-" +
                "-------", PLAYER1);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#00B-=#-=#-=#00B-=#\n" +
                "-=#001-=#-=#-=#001-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#001-=#-=#-=#001-=#\n" +
                "-=#00B-=#-=#-=#00B-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER1);

        // when
        hero(PLAYER1).reset();

        // then
        assertE("-------" +
                "-♠---♥-" +
                "-♠---♥-" +
                "-------" +
                "-♣---♦-" +
                "-♣---♦-" +
                "-------", PLAYER1);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#00B-=#-=#-=#00B-=#\n" +
                "-=#001-=#-=#-=#001-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#001-=#-=#-=#001-=#\n" +
                "-=#00B-=#-=#-=#00B-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER1);

        tickAll();

        assertE("-------" +
                "-♠---♥-" +
                "-♠-----" +
                "-------" +
                "-♣---♦-" +
                "-♣---♦-" +
                "-------", PLAYER1);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#00B-=#-=#-=#00A-=#\n" +
                "-=#001-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#001-=#-=#-=#001-=#\n" +
                "-=#00B-=#-=#-=#00B-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER1);

        // when
        // after that all heroes can go
        hero(PLAYER1, 5, 5).left();
        hero(PLAYER2, 5, 1).left();
        hero(PLAYER3, 1, 1).right();
        hero(PLAYER4, 1, 5).right();

        tickAll();

        assertE("-------" +
                "-♠♠-♥♥-" +
                "-♠-----" +
                "-------" +
                "-♣---♦-" +
                "-♣♣-♦♦-" +
                "-------", PLAYER1);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#00C001-=#00100B-=#\n" +
                "-=#001-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#001-=#-=#-=#001-=#\n" +
                "-=#00C001-=#00100C-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER1);
    }

    @Test
    public void shouldCantResetAfterLoose() {
        // given
        shouldLosePlayersCanGetInfoAboutGame();

        assertE("-------" +
                "-♣---♥-" +
                "-♣-----" +
                "-♣-----" +
                "-♣-----" +
                "-♣---♦-" +
                "-------", PLAYER1);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#001-=#-=#-=#00A-=#\n" +
                "-=#00B-=#-=#-=#-=#-=#\n" +
                "-=#014-=#-=#-=#-=#-=#\n" +
                "-=#014-=#-=#-=#-=#-=#\n" +
                "-=#00U-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER1);

        // when
        hero(PLAYER4).reset();
        tickAll();
        tickAll();

        // then
        assertE("-------" +
                "-♣---♥-" +
                "-♣-----" +
                "-♣-----" +
                "-♣-----" +
                "-♣---♦-" +
                "-------", PLAYER1);

        assertF("-=#-=#-=#-=#-=#-=#-=#\n" +
                "-=#001-=#-=#-=#00A-=#\n" +
                "-=#00B-=#-=#-=#-=#-=#\n" +
                "-=#014-=#-=#-=#-=#-=#\n" +
                "-=#014-=#-=#-=#-=#-=#\n" +
                "-=#00U-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#-=#\n", PLAYER1);
    }

    @Test
    public void shouldGoldResetIfSomebodyKillMeOnCell() {
        // given
        givenFl("╔════┐" +
                "║1$$2│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");
        gameFactory.setWaitingOthers(false);
        createPlayers(2);

        // when
        hero(PLAYER1, 1, 4).right();
        tickAll();

        // then
        assertEquals(11, hero(PLAYER1).getForcesPerTick());
        assertEquals(10, hero(PLAYER2).getForcesPerTick());

        // when
        hero(PLAYER1, 2, 4).right();
        tickAll();

        // then
        assertEquals(12, hero(PLAYER1).getForcesPerTick());
        assertEquals(10, hero(PLAYER2).getForcesPerTick());


        assertE("------" +
                "-♥♥♥♦-" +
                "------" +
                "------" +
                "------" +
                "------", PLAYER1);

        assertF("-=#-=#-=#-=#-=#-=#\n" +
                "-=#00B00200100A-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n", PLAYER1);

        // when
        hero(PLAYER2, 4, 4).left();
        tickAll();

        assertE("------" +
                "-♥♥-♦-" +
                "------" +
                "------" +
                "------" +
                "------", PLAYER1);

        assertF("-=#-=#-=#-=#-=#-=#\n" +
                "-=#00B002-=#00B-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n", PLAYER1);

        assertEquals(11, hero(PLAYER1).getForcesPerTick());
        assertEquals(10, hero(PLAYER2).getForcesPerTick());

        // when
        hero(PLAYER2, 4, 4).left();
        tickAll();

        assertE("------" +
                "-♥♥♦♦-" +
                "------" +
                "------" +
                "------" +
                "------", PLAYER1);

        assertF("-=#-=#-=#-=#-=#-=#\n" +
                "-=#00B00200100C-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n", PLAYER1);

        assertEquals(11, hero(PLAYER1).getForcesPerTick());
        assertEquals(11, hero(PLAYER2).getForcesPerTick());

        // when
        hero(PLAYER2).increaseAndMove(new Forces(pt(3, 4), 10),
                new ForcesMoves(pt(3, 4), 5, QDirection.LEFT));
        tickAll();

        assertE("------" +
                "-♥♦♦♦-" +
                "------" +
                "------" +
                "------" +
                "------", PLAYER1);

        assertF("-=#-=#-=#-=#-=#-=#\n" +
                "-=#00B00300600C-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n", PLAYER1);

        assertEquals(10, hero(PLAYER1).getForcesPerTick());
        assertEquals(12, hero(PLAYER2).getForcesPerTick());

        // when
        hero(PLAYER1).move(new ForcesMoves(pt(1, 4), 3, QDirection.RIGHT));
        tickAll();

        assertE("------" +
                "-♥-♦♦-" +
                "------" +
                "------" +
                "------" +
                "------", PLAYER1);

        assertF("-=#-=#-=#-=#-=#-=#\n" +
                "-=#008-=#00600C-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n", PLAYER1);

        assertEquals(10, hero(PLAYER1).getForcesPerTick());
        assertEquals(11, hero(PLAYER2).getForcesPerTick());
    }

    @Test // TODO continue with this test
    public void shouldForcesPerTickIncreasedAlsoFromTerritoryOccupation() {
        // given
        givenFl("╔════┐" +
                "║1...│" +
                "║....│" +
                "║....│" +
                "║...2│" +
                "└────┘");
        gameFactory.setWaitingOthers(false);
        createPlayers(2);

        assertE("------" +
                "-♥----" +
                "------" +
                "------" +
                "----♦-" +
                "------", PLAYER1);

        assertF("-=#-=#-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#00A-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n", PLAYER1);

        assertEquals(10, hero(PLAYER1).getForcesPerTick());
        assertEquals(10, hero(PLAYER2).getForcesPerTick());

        // when
        hero(PLAYER1, 1, 4).right();
        hero(PLAYER2, 4, 1).left();
        tickAll();

        assertE("------" +
                "-♥♥---" +
                "------" +
                "------" +
                "---♦♦-" +
                "------", PLAYER1);

        assertF("-=#-=#-=#-=#-=#-=#\n" +
                "-=#00B001-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#00100B-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n", PLAYER1);

        assertEquals(10, hero(PLAYER1).getForcesPerTick());
        assertEquals(10, hero(PLAYER2).getForcesPerTick());
    }

    @Test
    public void shouldContinueAfterWinIfSinglePlayerOnMultipleGame() {
        // given
        givenFl("╔════┐" +
                "║1...│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");
        gameFactory.setWaitingOthers(false);
        createPlayers(1);

        assertF("-=#-=#-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n", PLAYER1);

        // when
        hero(PLAYER1).increaseAnd(
                new Forces(pt(1, 4), 100)
        ).move(
                new ForcesMoves(pt(1, 4), 5, QDirection.DOWN),
                new ForcesMoves(pt(1, 4), 9, QDirection.RIGHT_DOWN),
                new ForcesMoves(pt(1, 4), 5, QDirection.RIGHT)
        ).send();
        tickAll();

        // then
        assertE("------" +
                "-♥♥---" +
                "-♥♥---" +
                "------" +
                "------" +
                "------", PLAYER1);

        assertF("-=#-=#-=#-=#-=#-=#\n" +
                "-=#001005-=#-=#-=#\n" +
                "-=#005009-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n", PLAYER1);

        // when
        hero(PLAYER1).increaseAnd(
                new Forces(pt(1, 3), 3),
                new Forces(pt(1, 4), 1),
                new Forces(pt(2, 3), 3),
                new Forces(pt(2, 4), 3)
        ).move(
                new ForcesMoves(pt(2, 4), 3, QDirection.RIGHT),
                new ForcesMoves(pt(2, 4), 3, QDirection.RIGHT_DOWN),
                new ForcesMoves(pt(2, 3), 3, QDirection.RIGHT),
                new ForcesMoves(pt(2, 3), 3, QDirection.RIGHT_DOWN),
                new ForcesMoves(pt(2, 3), 3, QDirection.DOWN),
                new ForcesMoves(pt(1, 3), 3, QDirection.RIGHT_DOWN),
                new ForcesMoves(pt(1, 3), 3, QDirection.DOWN)
        ).send();
        tickAll();

        // then
        assertE("------" +
                "-♥♥♥--" +
                "-♥♥♥--" +
                "-♥♥♥--" +
                "------" +
                "------", PLAYER1);

        assertF("-=#-=#-=#-=#-=#-=#\n" +
                "-=#002002003-=#-=#\n" +
                "-=#002003006-=#-=#\n" +
                "-=#003006003-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n", PLAYER1);

        verifyNoMoreInteractions(PLAYER1);

        // when
        hero(PLAYER1).increaseAnd(
                new Forces(pt(1, 2), 1),
                new Forces(pt(1, 3), 1),
                new Forces(pt(1, 4), 1),
                new Forces(pt(2, 2), 1),
                new Forces(pt(2, 3), 1),
                new Forces(pt(2, 4), 1),
                new Forces(pt(3, 2), 1),
                new Forces(pt(3, 3), 1),
                new Forces(pt(3, 4), 1)
        ).move(
                new ForcesMoves(pt(3, 4), 1, QDirection.RIGHT),
                new ForcesMoves(pt(3, 4), 1, QDirection.RIGHT_DOWN),
                new ForcesMoves(pt(3, 3), 1, QDirection.RIGHT),
                new ForcesMoves(pt(3, 3), 1, QDirection.RIGHT_DOWN),
                new ForcesMoves(pt(3, 2), 1, QDirection.RIGHT_DOWN),
                new ForcesMoves(pt(2, 2), 1, QDirection.RIGHT_DOWN),
                new ForcesMoves(pt(2, 2), 1, QDirection.DOWN),
                new ForcesMoves(pt(1, 2), 1, QDirection.RIGHT_DOWN),
                new ForcesMoves(pt(1, 2), 1, QDirection.DOWN)
        ).send();
        tickAll();

        // then
        assertE("------" +
                "-♥♥♥♥-" +
                "-♥♥♥♥-" +
                "-♥♥♥♥-" +
                "-♥♥♥♥-" +
                "------", PLAYER1);

        assertF("-=#-=#-=#-=#-=#-=#\n" +
                "-=#003003002001-=#\n" +
                "-=#003004005002-=#\n" +
                "-=#002005003001-=#\n" +
                "-=#001002001001-=#\n" +
                "-=#-=#-=#-=#-=#-=#\n", PLAYER1);

        // when
        tickAll();

        // then
        verify(PLAYER1).event(Events.WIN(1));
    }

    @Test
    public void shouldContinueAfterWinIfSinglePlayerOnMultipleGame_caseWithOtherPassableCells() {
        // given
        givenFl("╔═══┐" +
                "║1$2│" +
                "└───┘" +
                "     " +
                "     ");
        gameFactory.setWaitingOthers(false);
        createPlayers(1);

        assertF("-=#-=#-=#-=#-=#\n" +
                "-=#00A-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n", PLAYER1);

        // when
        hero(PLAYER1).move(new ForcesMoves(pt(1, 3), 5, QDirection.RIGHT));
        tickAll();

        // then
        verifyNoMoreInteractions(PLAYER1);

        assertE("-----" +
                "-♥♥--" +
                "-----" +
                "-----" +
                "-----", PLAYER1);

        assertF("-=#-=#-=#-=#-=#\n" +
                "-=#005005-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n", PLAYER1);

        // when
        hero(PLAYER1).move(new ForcesMoves(pt(2, 3), 2, QDirection.RIGHT));
        tickAll();

        // then
        verifyNoMoreInteractions(PLAYER1);

        assertE("-----" +
                "-♥♥♥-" +
                "-----" +
                "-----" +
                "-----", PLAYER1);

        assertF("-=#-=#-=#-=#-=#\n" +
                "-=#005003002-=#\n" +
                "-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n" +
                "-=#-=#-=#-=#-=#\n", PLAYER1);

        verifyNoMoreInteractions(PLAYER1);

        // when
        tickAll();

        // then
        verify(PLAYER1).event(Events.WIN(1));
    }

}
