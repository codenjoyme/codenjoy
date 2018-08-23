package com.codenjoy.dojo.services.multiplayer;

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

import static org.junit.Assert.*;

public class MultiplayerTypeTest {

    @Test
    public void twoTeamTypesShouldBeDifferent() {
        MultiplayerType team1 = MultiplayerType.TEAM.apply(1);
        MultiplayerType team2 = MultiplayerType.TEAM.apply(3);

        assertEquals(1, team1.getCount());
        assertEquals(3, team2.getCount());
    }

    @Test
    public void typeSingle() {
        MultiplayerType single = MultiplayerType.SINGLE;
        assertEquals(1, single.getCount());

        assertEquals(true, single.isSingle());
        assertEquals(false, single.isTournament());
        assertEquals(false, single.isTriple());
        assertEquals(false, single.isQuadro());
        assertEquals(false, single.isTeam());
        assertEquals(false, single.isMultiple());

        assertEquals(true, single.isSingleplayer());
        assertEquals(false, single.isMultiplayer());
    }

    @Test
    public void typeTournament() {
        MultiplayerType tournament = MultiplayerType.TOURNAMENT;
        assertEquals(2, tournament.getCount());

        assertEquals(false, tournament.isSingle());
        assertEquals(true, tournament.isTournament());
        assertEquals(false, tournament.isTriple());
        assertEquals(false, tournament.isQuadro());
        assertEquals(false, tournament.isTeam());
        assertEquals(false, tournament.isMultiple());

        assertEquals(false, tournament.isSingleplayer());
        assertEquals(true, tournament.isMultiplayer());
    }

    @Test
    public void typeTriple() {
        MultiplayerType triple = MultiplayerType.TRIPLE;
        assertEquals(3, triple.getCount());

        assertEquals(false, triple.isSingle());
        assertEquals(false, triple.isTournament());
        assertEquals(true, triple.isTriple());
        assertEquals(false, triple.isQuadro());
        assertEquals(false, triple.isTeam());
        assertEquals(false, triple.isMultiple());

        assertEquals(false, triple.isSingleplayer());
        assertEquals(true, triple.isMultiplayer());
    }

    @Test
    public void typeQuadro() {
        MultiplayerType quadro = MultiplayerType.QUADRO;
        assertEquals(4, quadro.getCount());

        assertEquals(false, quadro.isSingle());
        assertEquals(false, quadro.isTournament());
        assertEquals(false, quadro.isTriple());
        assertEquals(true, quadro.isQuadro());
        assertEquals(false, quadro.isTeam());
        assertEquals(false, quadro.isMultiple());

        assertEquals(false, quadro.isSingleplayer());
        assertEquals(true, quadro.isMultiplayer());
    }

    @Test
    public void typeTeam() {
        MultiplayerType team = MultiplayerType.TEAM.apply(9);
        assertEquals(9, team.getCount());

        assertEquals(false, team.isSingle());
        assertEquals(false, team.isTournament());
        assertEquals(false, team.isTriple());
        assertEquals(false, team.isQuadro());
        assertEquals(true, team.isTeam());
        assertEquals(false, team.isMultiple());

        assertEquals(false, team.isSingleplayer());
        assertEquals(true, team.isMultiplayer());
    }

    @Test
    public void typeMultiple() {
        MultiplayerType multiple = MultiplayerType.MULTIPLE;
        assertEquals(Integer.MAX_VALUE, multiple.getCount());

        assertEquals(false, multiple.isSingle());
        assertEquals(false, multiple.isTournament());
        assertEquals(false, multiple.isTriple());
        assertEquals(false, multiple.isQuadro());
        assertEquals(false, multiple.isTeam());
        assertEquals(true, multiple.isMultiple());

        assertEquals(false, multiple.isSingleplayer());
        assertEquals(true, multiple.isMultiplayer());
    }
}
