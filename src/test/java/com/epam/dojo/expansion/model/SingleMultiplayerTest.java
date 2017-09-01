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

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 4:47
 */
public class SingleMultiplayerTest extends AbstractSinglePlayersTest {

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

        assertE(PLAYER1,
                "-------" +
                "-♥-----" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        assertF(PLAYER1,
                "[[1,5]=10]");

        assertE(PLAYER2,
                "-------" +
                "-♥-----" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        assertF(PLAYER2,
                "[[1,5]=10]");

        // when
        // hero1 goes to multiple level
        hero(PLAYER1, 1, 5).right();
        tickAll();

        assertF(PLAYER1,
                "[[1,5]=11," +
                " [2,5]=1]");

        hero(PLAYER1, 2, 5).right();
        tickAll();

        assertF(PLAYER1,
                "[[1,5]=11," +
                " [2,5]=2," +
                " [3,5]=1]");

        verify(PLAYER1).event(Events.WIN(0));
        reset(PLAYER1);
        verifyNoMoreInteractions(PLAYER2);

        tickAll();

        // then
        // hero1 on their own start base
        assertE(PLAYER1,
                "-------" +
                "-----♥-" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        assertF(PLAYER1,
                "[[5,5]=10]");

        // for hero2 nothing will be changed
        assertE(PLAYER2,
                "-------" +
                "-♥-----" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        assertF(PLAYER2,
                "[[1,5]=10]");

        // when
        // hero2 goes to multiple level
        hero(PLAYER2, 1, 5).right();
        tickAll();

        assertF(PLAYER2,
                "[[1,5]=11," +
                " [2,5]=1]");

        hero(PLAYER2, 2, 5).right();
        tickAll();

        assertF(PLAYER2,
                "[[1,5]=11," +
                " [2,5]=2," +
                " [3,5]=1]");

        verify(PLAYER2).event(Events.WIN(0));
        reset(PLAYER2);
        verifyNoMoreInteractions(PLAYER2);

        tickAll();

        // then
        // hero2 on their own start base
        assertE(PLAYER2,
                "-------" +
                "-----♥-" +
                "-------" +
                "-------" +
                "-------" +
                "-----♦-" +
                "-------");

        assertF(PLAYER1,
                "[[5,5]=10," +
                " [5,1]=10]");

        // hero1 also sees this results
        assertE(PLAYER1,
                "-------" +
                "-----♥-" +
                "-------" +
                "-------" +
                "-------" +
                "-----♦-" +
                "-------");

        assertF(PLAYER1,
                "[[5,5]=10," +
                " [5,1]=10]");

        // when
        // another player3 register
        createOneMorePlayer();
        tickAll();

        // then
        // hero1-2 on multiple
        assertE(PLAYER1,
                "-------" +
                "-----♥-" +
                "-------" +
                "-------" +
                "-------" +
                "-----♦-" +
                "-------");

        assertF(PLAYER1,
                "[[5,5]=10," +
                " [5,1]=10]");

        assertE(PLAYER2,
                "-------" +
                "-----♥-" +
                "-------" +
                "-------" +
                "-------" +
                "-----♦-" +
                "-------");

        assertF(PLAYER2,
                "[[5,5]=10," +
                " [5,1]=10]");

        assertE(PLAYER3,
                "-------" +
                "-♥-----" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        assertF(PLAYER3,
                "[[1,5]=10]");

        // hero3 goes to multiple
        hero(PLAYER3, 1, 5).right();
        tickAll();

        assertF(PLAYER3,
                "[[1,5]=11," +
                " [2,5]=1]");

        hero(PLAYER3, 2, 5).right();
        tickAll();

        assertF(PLAYER3,
                "[[1,5]=11," +
                " [2,5]=2," +
                " [3,5]=1]");

        verify(PLAYER3).event(Events.WIN(0));
        reset(PLAYER3);
        verifyNoMoreInteractions(PLAYER3);

        tickAll();

        // then all they are on multiple
        assertE(PLAYER3,
                "-------" +
                "-----♥-" +
                "-------" +
                "-------" +
                "-------" +
                "-♣---♦-" +
                "-------");

        assertF(PLAYER3,
                "[[5,5]=10," +
                " [1,1]=10," +
                " [5,1]=10]");
    }
}
