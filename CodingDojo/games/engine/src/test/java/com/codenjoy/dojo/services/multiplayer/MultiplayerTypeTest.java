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


import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.nullobj.NullGame;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultiplayerTypeTest {

    @Test
    public void twoTeamTypesShouldBeDifferent() {
        MultiplayerType team1 = MultiplayerType.TEAM.apply(1, !MultiplayerType.DISPOSABLE);
        MultiplayerType team2 = MultiplayerType.TEAM.apply(3, MultiplayerType.DISPOSABLE);

        assertEquals(1, team1.getRoomSize());
        assertEquals(false, team1.isDisposable());

        assertEquals(3, team2.getRoomSize());
        assertEquals(true, team2.isDisposable());
    }

    @Test
    public void typeSingle() {
        MultiplayerType type = MultiplayerType.SINGLE;

        assertEquals(1, type.getRoomSize());
        assertEquals(1, type.getLevelsCount());

        assertEquals(true, type.isSingle());
        assertEquals(false, type.isTournament());
        assertEquals(false, type.isTriple());
        assertEquals(false, type.isQuadro());
        assertEquals(false, type.isTeam());
        assertEquals(false, type.isMultiple());

        assertEquals(true, type.isSingleplayer());
        assertEquals(false, type.isMultiplayer());

        assertEquals("single", type.getType());
        assertEquals(true, type.isDisposable());
    }

    @Test
    public void typeTournament() {
        MultiplayerType type = MultiplayerType.TOURNAMENT;

        assertEquals(2, type.getRoomSize());
        assertEquals(1, type.getLevelsCount());

        assertEquals(false, type.isSingle());
        assertEquals(true, type.isTournament());
        assertEquals(false, type.isTriple());
        assertEquals(false, type.isQuadro());
        assertEquals(false, type.isTeam());
        assertEquals(false, type.isMultiple());

        assertEquals(false, type.isSingleplayer());
        assertEquals(true, type.isMultiplayer());

        assertEquals("tournament", type.getType());
        assertEquals(true, type.isDisposable());
    }

    @Test
    public void typeTriple() {
        MultiplayerType type = MultiplayerType.TRIPLE;

        assertEquals(3, type.getRoomSize());
        assertEquals(1, type.getLevelsCount());

        assertEquals(false, type.isSingle());
        assertEquals(false, type.isTournament());
        assertEquals(true, type.isTriple());
        assertEquals(false, type.isQuadro());
        assertEquals(false, type.isTeam());
        assertEquals(false, type.isMultiple());

        assertEquals(false, type.isSingleplayer());
        assertEquals(true, type.isMultiplayer());

        assertEquals("triple", type.getType());
        assertEquals(true, type.isDisposable());
    }

    @Test
    public void typeQuadro() {
        MultiplayerType type = MultiplayerType.QUADRO;

        assertEquals(4, type.getRoomSize());
        assertEquals(1, type.getLevelsCount());

        assertEquals(false, type.isSingle());
        assertEquals(false, type.isTournament());
        assertEquals(false, type.isTriple());
        assertEquals(true, type.isQuadro());
        assertEquals(false, type.isTeam());
        assertEquals(false, type.isMultiple());

        assertEquals(false, type.isSingleplayer());
        assertEquals(true, type.isMultiplayer());

        assertEquals("quadro", type.getType());
        assertEquals(true, type.isDisposable());
    }

    @Test
    public void typeTeam() {
        MultiplayerType type = MultiplayerType.TEAM.apply(9, !MultiplayerType.DISPOSABLE);

        assertEquals(9, type.getRoomSize());
        assertEquals(1, type.getLevelsCount());

        assertEquals(false, type.isSingle());
        assertEquals(false, type.isTournament());
        assertEquals(false, type.isTriple());
        assertEquals(false, type.isQuadro());
        assertEquals(true, type.isTeam());
        assertEquals(false, type.isMultiple());

        assertEquals(false, type.isSingleplayer());
        assertEquals(true, type.isMultiplayer());

        assertEquals("team", type.getType());
        assertEquals(false, type.isDisposable());
    }

    @Test
    public void typeMultiple() {
        MultiplayerType type = MultiplayerType.MULTIPLE;

        assertEquals(Integer.MAX_VALUE, type.getRoomSize());
        assertEquals(1, type.getLevelsCount());

        assertEquals(false, type.isSingle());
        assertEquals(false, type.isTournament());
        assertEquals(false, type.isTriple());
        assertEquals(false, type.isQuadro());
        assertEquals(false, type.isTeam());
        assertEquals(true, type.isMultiple());

        assertEquals(false, type.isSingleplayer());
        assertEquals(true, type.isMultiplayer());

        assertEquals("multiple", type.getType());
        assertEquals(false, type.isDisposable());
    }

    @Test
    public void typeTraining() {
        MultiplayerType type = MultiplayerType.TRAINING.apply(3);

        assertEquals(1, type.getRoomSize());
        assertEquals(3, type.getLevelsCount());

        assertEquals(false, type.isSingle());
        assertEquals(false, type.isTournament());
        assertEquals(false, type.isTriple());
        assertEquals(false, type.isQuadro());
        assertEquals(false, type.isTeam());
        assertEquals(false, type.isMultiple());
        assertEquals(true, type.isTraining());

        assertEquals(false, type.isSingleplayer());
        assertEquals(true, type.isMultiplayer());

        assertEquals("training", type.getType());
        assertEquals(false, type.isDisposable());
    }

    @Test
    public void typeTraining_lastLevelIsMultiple() {
        MultiplayerType type = MultiplayerType.TRAINING.apply(3);

        assertEquals(1, type.getRoomSize());

        assertEquals(1, type.getRoomSize(new LevelProgress(3, 1, 2)));
        assertEquals(1, type.getRoomSize(new LevelProgress(3, 2, 2)));
        assertEquals(Integer.MAX_VALUE, type.getRoomSize(new LevelProgress(3, 3, 3)));
    }

    public class AGame extends NullGame {

        private LevelProgress progress;

        @Override
        public void setProgress(LevelProgress progress) {
            this.progress = progress;
        }

        @Override
        public LevelProgress getProgress() {
            return progress;
        }
    }

    @Test
    public void loadProgress_forAnyType() {
        // given
        MultiplayerType type = MultiplayerType.SINGLE;
        Game game = new AGame();
        JSONObject save = new JSONObject("{'any':'data'}");

        // when
        int roomSize = type.loadProgress(game, save);

        // then
        assertEquals(1, roomSize);
        assertEquals("{'current':0,'passed':-1,'total':1,'valid':true}",
                game.getProgress().toString());
    }

    @Test
    public void loadProgress_forTrainingType_atFirstSingleLevel() {
        // given
        MultiplayerType type = MultiplayerType.TRAINING.apply(3);
        Game game = new AGame();
        JSONObject save = new JSONObject("{'levelProgress':{'total':3,'current':1,'lastPassed':0}}");

        // when
        int roomSize = type.loadProgress(game, save);

        // then
        assertEquals(1, roomSize);
        assertEquals("{'current':1,'passed':0,'total':3,'valid':true}",
                game.getProgress().toString());
    }

    @Test
    public void loadProgress_forTrainingType_atLastMultipleLevel() {
        // given
        MultiplayerType type = MultiplayerType.TRAINING.apply(3);
        Game game = new AGame();
        JSONObject save = new JSONObject("{'levelProgress':{'total':3,'current':3,'lastPassed':2}}");

        // when
        int roomSize = type.loadProgress(game, save);

        // then
        assertEquals(Integer.MAX_VALUE, roomSize);
        assertEquals("{'current':3,'passed':2,'total':3,'valid':true}",
                game.getProgress().toString());
    }
}