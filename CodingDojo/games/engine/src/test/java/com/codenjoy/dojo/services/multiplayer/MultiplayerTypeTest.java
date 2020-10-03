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
import com.codenjoy.dojo.services.Progressive;
import com.codenjoy.dojo.services.nullobj.NullGame;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Delegate;
import org.json.JSONObject;
import org.junit.Test;

import static com.codenjoy.dojo.services.multiplayer.MultiplayerType.DISPOSABLE;
import static com.codenjoy.dojo.services.multiplayer.MultiplayerType.RELOAD_ALONE;
import static org.junit.Assert.*;

public class MultiplayerTypeTest {

    @Test
    public void twoTeamTypesShouldBeDifferent() {
        MultiplayerType team1 = MultiplayerType.TEAM.apply(1, !DISPOSABLE);
        MultiplayerType team2 = MultiplayerType.TEAM.apply(3, DISPOSABLE);

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

        assertEquals(false, type.isLevels());
        assertEquals(true, type.shouldReloadAlone());

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

        assertEquals(false, type.isLevels());
        assertEquals(true, type.shouldReloadAlone());

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

        assertEquals(false, type.isLevels());
        assertEquals(true, type.shouldReloadAlone());

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

        assertEquals(false, type.isLevels());
        assertEquals(true, type.shouldReloadAlone());

        assertEquals(false, type.isSingleplayer());
        assertEquals(true, type.isMultiplayer());

        assertEquals("quadro", type.getType());
        assertEquals(true, type.isDisposable());
    }

    @Test
    public void typeTeam() {
        MultiplayerType type = MultiplayerType.TEAM.apply(9, !DISPOSABLE);

        assertEquals(9, type.getRoomSize());
        assertEquals(1, type.getLevelsCount());

        assertEquals(false, type.isSingle());
        assertEquals(false, type.isTournament());
        assertEquals(false, type.isTriple());
        assertEquals(false, type.isQuadro());
        assertEquals(true, type.isTeam());
        assertEquals(false, type.isMultiple());

        assertEquals(false, type.isLevels());
        assertEquals(true, type.shouldReloadAlone());

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

        assertEquals(false, type.isLevels());
        assertEquals(true, type.shouldReloadAlone());

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

        assertEquals(true, type.isLevels());
        assertEquals(true, type.shouldReloadAlone());

        assertEquals(false, type.isSingleplayer());
        assertEquals(true, type.isMultiplayer());

        assertEquals("training", type.getType());
        assertEquals(false, type.isDisposable());
    }

    @Test
    public void typeLevels_disposable_reloadAlone() {
        MultiplayerType type = MultiplayerType.LEVELS.apply(3, 4, DISPOSABLE, RELOAD_ALONE);

        assertEquals(3, type.getRoomSize());
        assertEquals(4, type.getLevelsCount());

        assertEquals(false, type.isSingle());
        assertEquals(false, type.isTournament());
        assertEquals(false, type.isTriple());
        assertEquals(false, type.isQuadro());
        assertEquals(false, type.isTeam());
        assertEquals(false, type.isMultiple());
        assertEquals(false, type.isTraining());

        assertEquals(true, type.isLevels());
        assertEquals(true, type.shouldReloadAlone());

        assertEquals(false, type.isSingleplayer());
        assertEquals(true, type.isMultiplayer());

        assertEquals("levels", type.getType());
        assertEquals(true, type.isDisposable());
    }

    @Test
    public void typeLevels_notDisposable_dontReloadAlone() {
        MultiplayerType type = MultiplayerType.LEVELS.apply(5, 6, !DISPOSABLE, !RELOAD_ALONE);

        assertEquals(5, type.getRoomSize());
        assertEquals(6, type.getLevelsCount());

        assertEquals(false, type.isSingle());
        assertEquals(false, type.isTournament());
        assertEquals(false, type.isTriple());
        assertEquals(false, type.isQuadro());
        assertEquals(false, type.isTeam());
        assertEquals(false, type.isMultiple());
        assertEquals(false, type.isTraining());

        assertEquals(true, type.isLevels());
        assertEquals(false, type.shouldReloadAlone());

        assertEquals(false, type.isSingleplayer());
        assertEquals(true, type.isMultiplayer());

        assertEquals("levels", type.getType());
        assertEquals(false, type.isDisposable());
    }

    @Test
    public void typeSingleLevels() {
        MultiplayerType type = MultiplayerType.SINGLE_LEVELS.apply(5);

        assertEquals(1, type.getRoomSize());
        assertEquals(5, type.getLevelsCount());

        assertEquals(true, type.isSingle());
        assertEquals(false, type.isTournament());
        assertEquals(false, type.isTriple());
        assertEquals(false, type.isQuadro());
        assertEquals(false, type.isTeam());
        assertEquals(false, type.isMultiple());
        assertEquals(false, type.isTraining());

        assertEquals(true, type.isLevels());
        assertEquals(true, type.shouldReloadAlone());

        assertEquals(true, type.isSingleplayer());
        assertEquals(false, type.isMultiplayer());

        assertEquals("singlelevels", type.getType());
        assertEquals(true, type.isDisposable());
    }

    @Test
    public void typeMultipleLevels() {
        MultiplayerType type = MultiplayerType.MULTIPLE_LEVELS.apply(5, 7);

        assertEquals(5, type.getRoomSize());
        assertEquals(7, type.getLevelsCount());

        assertEquals(false, type.isSingle());
        assertEquals(false, type.isTournament());
        assertEquals(false, type.isTriple());
        assertEquals(false, type.isQuadro());
        assertEquals(false, type.isTeam());
        assertEquals(true, type.isMultiple());
        assertEquals(false, type.isTraining());

        assertEquals(true, type.isLevels());
        assertEquals(false, type.shouldReloadAlone());

        assertEquals(false, type.isSingleplayer());
        assertEquals(true, type.isMultiplayer());

        assertEquals("multiplelevels", type.getType());
        assertEquals(false, type.isDisposable());
    }

    @Test
    public void typeMultipleLevelsMultiroom() {
        MultiplayerType type = MultiplayerType.MULTIPLE_LEVELS_MULTIROOM.apply(5, 7);

        assertEquals(1, type.getRoomSize());
        assertEquals(1, type.getRoomSize(new LevelProgress(7, 1, 2))); // level 1
        assertEquals(1, type.getRoomSize(new LevelProgress(7, 6, 6))); // level N - 1
        assertEquals(5, type.getRoomSize(new LevelProgress(7, 7, 7))); // level N
        assertEquals(7, type.getLevelsCount());

        assertEquals(false, type.isSingle());
        assertEquals(false, type.isTournament());
        assertEquals(false, type.isTriple());
        assertEquals(false, type.isQuadro());
        assertEquals(false, type.isTeam());
        assertEquals(true, type.isMultiple());
        assertEquals(false, type.isTraining());

        assertEquals(true, type.isLevels());
        assertEquals(false, type.shouldReloadAlone());

        assertEquals(false, type.isSingleplayer());
        assertEquals(true, type.isMultiplayer());

        assertEquals("multiplelevelsmultiroom", type.getType());
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

    @Test
    public void loadProgress_forAnyType() {
        // given
        MultiplayerType type = MultiplayerType.SINGLE;
        Game game = new StubGame();
        JSONObject save = new JSONObject("{'any':'data'}");

        // when
        int roomSize = type.loadProgress(game, save);

        // then
        assertEquals(1, roomSize);
        assertEquals("{'current':1,'passed':0,'total':1,'valid':true}",
                game.getProgress().toString());
    }

    @Test
    public void loadProgress_forTrainingType_atFirstSingleLevel() {
        // given
        MultiplayerType type = MultiplayerType.TRAINING.apply(3);
        Game game = new StubGame();
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
        Game game = new StubGame();
        JSONObject save = new JSONObject("{'levelProgress':{'total':3,'current':3,'lastPassed':2}}");

        // when
        int roomSize = type.loadProgress(game, save);

        // then
        assertEquals(Integer.MAX_VALUE, roomSize);
        assertEquals("{'current':3,'passed':2,'total':3,'valid':true}",
                game.getProgress().toString());
    }

    private static class StubGame implements Game {

        @Delegate(excludes = Progressive.class)
        private Game game = NullGame.INSTANCE;

        @Setter
        @Getter
        private LevelProgress progress;

    }
}