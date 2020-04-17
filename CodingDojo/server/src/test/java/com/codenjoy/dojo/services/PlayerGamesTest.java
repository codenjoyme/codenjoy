package com.codenjoy.dojo.services;

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


import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.LevelProgress;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.nullobj.NullPlayerGame;
import com.codenjoy.dojo.services.printer.BoardReader;
import org.json.JSONObject;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import java.util.*;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PlayerGamesTest extends AbstractPlayerGamesTest {

    private PlayerGame removed;

    @Test
    public void testRemove() {
        // given
        Player player = createPlayer();

        assertEquals(false, playerGames.isEmpty());
        assertEquals(1, playerGames.size());
        PlayerGame playerGame = playerGames.get(player.getName());
        GameField field = playerGame.getGame().getField();
        playerGames.onRemove(pg -> removed = pg);

        // when
        playerGames.remove(player);

        // then
        assertEquals(true, playerGames.isEmpty());
        assertEquals(0, playerGames.size());

        verifyRemove(playerGame, field);
        assertEquals(removed, playerGame);
    }

    @Test
    public void testGet_notExists() {
        // when
        PlayerGame playerGame = playerGames.get("bla");

        // then
        assertEquals(NullPlayerGame.INSTANCE, playerGame);
    }

    @Test
    public void testGet_exists() {
        // given
        Player player = createPlayer("game");

        // when
        PlayerGame playerGame = playerGames.get(player.getName());

        // then
        assertEquals(player, playerGame.getPlayer());
        assertEquals(fields.get(0), playerGame.getGame().getField());
    }

    @Test
    public void testGetByIndex() {
        // given
        Player player = createPlayer();

        // when
        PlayerGame playerGame = playerGames.get(0);

        // then
        assertEquals(player, playerGame.getPlayer());
    }

    @Test
    public void testAdd() {
        // given
        Player player = createPlayer();

        // when
        Player otherPlayer = createPlayer();

        // then
        assertEquals(false, playerGames.isEmpty());
        assertEquals(2, playerGames.size());

        PlayerGame playerGame = playerGames.get(otherPlayer.getName());

        // TODO интересная бага, время от времени при запуске всех тестов parent проекта этот ассерт слетает потому что == не то же самое что equals. Интересный квест почему. Не критично, просто любопытно
        // System.out.println("==> " + (otherPlayer == playerGame.getPlayer()));
        // System.out.println("eq> " + (otherPlayer.equals(playerGame.getPlayer())));
        // assertSame(otherPlayer, playerGame.getPlayer());
        assertEquals(otherPlayer, playerGame.getPlayer());
    }

    @Test
    public void testIterator() {
        // given
        Player player = createPlayer();
        Player otherPlayer = createPlayer();

        // when
        Iterator<PlayerGame> iterator = playerGames.iterator();

        // then
        assertEquals(true,iterator.hasNext());
        assertEquals(player, iterator.next().getPlayer());

        assertEquals(true,iterator.hasNext());
        assertEquals(otherPlayer, iterator.next().getPlayer());

        assertEquals(false,iterator.hasNext());
    }

    @Test
    public void testPlayers() {
        // given
        Player player = createPlayer();
        Player otherPlayer = createPlayer();

        // when
        List<Player> players = playerGames.players();

        // then
        assertEquals(player, players.get(0));
        assertEquals(otherPlayer, players.get(1));
        assertEquals(2, players.size());
    }

    @Test
    public void testGetAll() {
        // given
        Player player = createPlayer();
        Player secondPlayer = createPlayer();
        Player thirdPlayer = createPlayer("game2");

        // when
        List<PlayerGame> result = playerGames.getAll("game");

        // then
        assertEquals(2, result.size());
        assertEquals(player, result.get(0).getPlayer());
        assertEquals(secondPlayer, result.get(1).getPlayer());

        // when
        List<PlayerGame> result2 = playerGames.getAll("game2");

        // then
        assertEquals(1, result2.size());
        assertEquals(thirdPlayer, result2.get(0).getPlayer());
    }

    @Test
    public void testGetAllPlayersByType() {
        // given
        Player player = createPlayer();
        Player secondPlayer = createPlayer();
        Player thirdPlayer = createPlayer("game2");

        // when
        List<Player> result = playerGames.getPlayers("game");

        // then
        assertEquals(2, result.size());
        assertEquals(player, result.get(0));
        assertEquals(secondPlayer, result.get(1));

        // when
        List<Player> result2 = playerGames.getPlayers("game2");

        // then
        assertEquals(1, result2.size());
        assertEquals(thirdPlayer, result2.get(0));
    }

    @Test
    public void testClear() {
        // given
        Player player = createPlayer();
        Player player2 = createPlayer();
        Player player3 = createPlayer();

        PlayerGame playerGame1 = playerGames.get(player.getName());
        GameField field1 = playerGame1.getGame().getField();

        PlayerGame playerGame2 = playerGames.get(player2.getName());
        GameField field2 = playerGame2.getGame().getField();

        PlayerGame playerGame3 = playerGames.get(player3.getName());
        GameField field3 = playerGame3.getGame().getField();

        assertEquals(3, playerGames.size());

        // when
        playerGames.clear();

        // then
        assertEquals(0, playerGames.size());

        verifyRemove(playerGame1, field1);
        verifyRemove(playerGame2, field2);
        verifyRemove(playerGame3, field3);
    }

    @Test
    public void testGetGameTypes() {
        // given
        Player player = createPlayer();
        Player player2 = createPlayer("game2");
        playerGames.add(player2, "room", null);

        // when
        List<GameType> gameTypes = playerGames.getGameTypes();

        // then
        assertEquals(2, gameTypes.size());
        assertEquals("game", gameTypes.get(0).name());
        assertEquals("game2", gameTypes.get(1).name());
    }

    @Test
    public void shouldTickLazyJoystickWhenTick() {
        // given
        Player player = createPlayer();
        lazyJoysticks.get(0).right();

        verifyNoMoreInteractions(joysticks.get(0));

        // when
        playerGames.tick();

        // then
        verify(joysticks.get(0)).right();
    }

    @Test
    public void shouldTickGameType() {
        // given
        createPlayer("game2");
        createPlayer("game3");
        createPlayer("game2"); // второй игрок к уже существующей game2

        // when
        playerGames.tick();

        // then
        InOrder order = inOrder(gameTypes.get(0), gameTypes.get(1), gameTypes.get(2));

        order.verify(gameTypes.get(0)).quietTick();
        order.verify(gameTypes.get(1)).quietTick();
        order.verify(gameTypes.get(2)).quietTick();
    }

    @Test
    public void testGetByGamePlayer() {
        // given
        Player player = createPlayer();

        // when
        PlayerGame playerGame = playerGames.get(gamePlayers.get(0));

        // then
        assertEquals(gamePlayers.get(0), playerGame.getGame().getPlayer());
        assertEquals(fields.get(0), playerGame.getField());
    }

    @Test
    public void shouldNewGame_whenGameOver_caseAnyMultiplayerType() {
        // given
        createPlayer();
        reset(fields.get(0));
        when(gamePlayers.get(0).isAlive()).thenReturn(false);

        // when
        playerGames.tick();

        // then
        verify(fields.get(0), times(1)).newGame(gamePlayers.get(0));
    }

    @Test
    public void shouldNewGame_whenGameOver_caseTrainingMultiplayerType() {
        // given
        createPlayer("player2", "room", "game2", MultiplayerType.TRAINING.apply(3));

        reset(fields.get(0));
        when(gamePlayers.get(0).isAlive()).thenReturn(false);

        // when
        playerGames.tick();

        // then
        verify(fields.get(0), times(1)).newGame(gamePlayers.get(0));
    }

    @Test
    public void shouldNextLevel_whenGameOver_andIsWin_caseTrainingMultiplayerType() {
        // given
        Player player = createPlayer(MultiplayerType.TRAINING.apply(2));

        reset(fields.get(0));
        assertEquals(1, fields.size());
        when(gamePlayers.get(0).isAlive()).thenReturn(false);
        when(gamePlayers.get(0).isWin()).thenReturn(true);

        // then
        assertProgress("player", "{'current':0,'passed':-1,'total':2,'valid':true}");

        // when
        // win + gameOver > next level
        playerGames.tick();

        // then
        String progress1 = "{'current':1,'passed':0,'total':2,'valid':true}";
        assertProgress("player", progress1);
        verifyPlayerEventListenerLevelChanged("player", progress1);

        int newField = fields.size() - 1;
        assertEquals(2, fields.size());
        verify(fields.get(newField), times(1)).newGame(gamePlayers.get(0));
        reset(fields.get(newField));

        // when
        // win + gameOver > next (last) level
        playerGames.tick();

        // then
        String progress2 = "{'current':2,'passed':1,'total':2,'valid':true}";
        assertProgress("player", progress2);
        verifyPlayerEventListenerLevelChanged("player", progress2);

        newField = fields.size() - 1;
        assertEquals(3, fields.size());
        verify(fields.get(newField), times(1)).newGame(gamePlayers.get(0));
        reset(fields.get(newField));

        // when
        // win + gameOver > this is last level - no changes
        playerGames.tick();

        // then
        assertProgress("player", "{'current':2,'passed':1,'total':2,'valid':true}");
        verifyPlayerEventListenerLevelChanged("player", null);

        newField = fields.size() - 1;
        assertEquals(3, fields.size());
        verify(fields.get(newField), times(1)).newGame(gamePlayers.get(0));
        reset(fields.get(newField));
    }

    @Test
    public void testChangeLevel_cantBecauseNotPassedLevel() {
        // given
        createPlayer(MultiplayerType.TRAINING.apply(2));

        reset(fields.get(0));
        assertEquals(1, fields.size());
        when(gamePlayers.get(0).isAlive()).thenReturn(true);
        when(gamePlayers.get(0).isWin()).thenReturn(false);

        // then
        String same = "{'current':0,'passed':-1,'total':2,'valid':true}";
        assertProgress("player", same);

        // when
        playerGames.changeLevel("player", 1);

        // then
        assertProgress("player", same);

        assertEquals(1, fields.size());
        verify(fields.get(0), never()).newGame(gamePlayers.get(0));
    }

    @Test
    public void testChangeLevel_canBecausePassedLevel() {
        shouldNextLevel_whenGameOver_andIsWin_caseTrainingMultiplayerType();

        // given
        assertEquals(3, fields.size());
        when(gamePlayers.get(0).isAlive()).thenReturn(true);
        when(gamePlayers.get(0).isWin()).thenReturn(false);

        // then
        assertProgress("player", "{'current':2,'passed':1,'total':2,'valid':true}");

        // when
        // change decrease - create new field
        playerGames.changeLevel("player", 1);

        // then
        String progress = "{'current':1,'passed':1,'total':2,'valid':true}";
        assertProgress("player", progress);
        verifyPlayerEventListenerLevelChanged("player", progress);

        int newField = fields.size() - 1;
        assertEquals(4, fields.size());
        verify(fields.get(newField), times(1)).newGame(gamePlayers.get(0));
    }

    @Test
    public void testSetLevel_caseNotPassedLevel() {
        // given
        createPlayer(MultiplayerType.TRAINING.apply(2));

        reset(fields.get(0));
        assertEquals(1, fields.size());
        when(gamePlayers.get(0).isAlive()).thenReturn(true);
        when(gamePlayers.get(0).isWin()).thenReturn(false);

        // then
        String same = "{'current':0,'passed':-1,'total':2,'valid':true}";
        assertProgress("player", same);

        // when
        // change increase - don't create new field
        playerGames.setLevel("player", new JSONObject("{'levelProgress':{'current':1,'lastPassed':1,'total':2}}"));

        // then
        assertProgress("player", "{'current':1,'passed':1,'total':2,'valid':true}");

        int newField = fields.size() - 1;
        assertEquals(2, fields.size());
        verify(fields.get(newField), times(1)).newGame(gamePlayers.get(0));
    }

    @Test
    public void testSetLevel_casePassedLevel() {
        shouldNextLevel_whenGameOver_andIsWin_caseTrainingMultiplayerType();

        // given
        assertEquals(3, fields.size());
        when(gamePlayers.get(0).isAlive()).thenReturn(true);
        when(gamePlayers.get(0).isWin()).thenReturn(false);

        // then
        assertProgress("player", "{'current':2,'passed':1,'total':2,'valid':true}");

        // when
        // change decrease - create new field
        playerGames.setLevel("player", new JSONObject("{'levelProgress':{'current':1,'lastPassed':1,'total':2}}"));

        // then
        String progress = "{'current':1,'passed':1,'total':2,'valid':true}";
        assertProgress("player", progress);
        verifyPlayerEventListenerLevelChanged("player", progress);

        int newField = fields.size() - 1;
        assertEquals(4, fields.size());
        verify(fields.get(newField), times(1)).newGame(gamePlayers.get(0));
    }

    @Test
    public void testSetLevel_caseNoLevelData() {
        shouldNextLevel_whenGameOver_andIsWin_caseTrainingMultiplayerType();

        // given
        assertEquals(3, fields.size());
        when(gamePlayers.get(0).isAlive()).thenReturn(true);
        when(gamePlayers.get(0).isWin()).thenReturn(false);

        // then
        assertProgress("player", "{'current':2,'passed':1,'total':2,'valid':true}");

        // when
        playerGames.setLevel("player", new JSONObject("{}"));

        // then
        String progress = "{'current':0,'passed':-1,'total':2,'valid':true}";
        assertProgress("player", progress);
        verifyPlayerEventListenerLevelChanged("player", progress);

        int newField = fields.size() - 1;
        assertEquals(4, fields.size());
        verify(fields.get(newField), times(1)).newGame(gamePlayers.get(0));
    }

    @Test
    public void testSetLevel_caseNullLevelData() {
        shouldNextLevel_whenGameOver_andIsWin_caseTrainingMultiplayerType();

        // given
        assertEquals(3, fields.size());
        when(gamePlayers.get(0).isAlive()).thenReturn(true);
        when(gamePlayers.get(0).isWin()).thenReturn(false);

        // then
        String same = "{'current':2,'passed':1,'total':2,'valid':true}";
        assertProgress("player", same);

        // when
        playerGames.setLevel("player", null);

        // then
        assertProgress("player", same);
        verifyPlayerEventListenerLevelChanged("player", null);

        int newField = fields.size() - 1;
        assertEquals(3, fields.size());
        verifyNoMoreInteractions(fields.get(newField));
    }

    @Test
    public void testResetAloneUsersField_whenRemove() {
        // given
        MultiplayerType type = MultiplayerType.MULTIPLE;
        Player player1 = createPlayer("player1", type);
        Player player2 = createPlayer("player2", type);
        Player player3 = createPlayer("player3", type);

        assertRooms("{0=[player1, player2, player3]}");

        // when
        playerGames.remove(player1);

        // then
        assertEquals(1, fields.size());

        // when
        playerGames.remove(player2);

        // then
        // created new field for player3
        assertEquals(2, fields.size());
        assertRooms("{1=[player3]}");

        verify(fields.get(1), times(1)).newGame(gamePlayers.get(2));
    }

    @Test
    public void testDontResetAloneUsersField_whenRemoveCurrent() {
        // given
        MultiplayerType type = MultiplayerType.MULTIPLE;
        Player player1 = createPlayer("player1", type);
        Player player2 = createPlayer("player2", type);
        Player player3 = createPlayer("player3", type);

        assertRooms("{0=[player1, player2, player3]}");

        // when
        playerGames.removeCurrent(player1);

        // then
        assertEquals(1, fields.size());

        // when
        playerGames.removeCurrent(player2);

        // then
        assertEquals(1, fields.size());
        assertRooms("{0=[player3]}");
    }

    @Test
    public void testResetAloneUsersField_whenReload() {
        // given
        MultiplayerType type = MultiplayerType.TOURNAMENT;
        Player player1 = createPlayer("player1", type);
        Player player2 = createPlayer("player2", type);

        assertRooms("{0=[player1, player2]}");

        // when
        Game game = playerGames.get(0).getGame();
        playerGames.reload(game, "room", game.getSave());

        // then
        // created new field for player3
        assertEquals(2, fields.size());
        assertRooms("{1=[player1, player2]}");

        verify(fields.get(1), times(1)).newGame(gamePlayers.get(1));
    }

    @Test
    public void testDontResetAloneUsersField_whenReloadCurrent() {
        // given
        MultiplayerType type = MultiplayerType.TOURNAMENT;
        Player player1 = createPlayer("player1", type);
        Player player2 = createPlayer("player2", type);

        assertRooms("{0=[player1, player2]}");

        // when
        PlayerGame playerGame = playerGames.get(0);
        playerGames.reloadCurrent(playerGame);

        // then
        assertEquals(2, fields.size());
        assertRooms("{0=[player2], 1=[player1]}");
    }

    @Test
    public void testReloadAll_withShuffle() {
        // given
        MultiplayerType type = MultiplayerType.TRIPLE;
        Player player1 = createPlayer("player1", type);
        Player player2 = createPlayer("player2", type);
        Player player3 = createPlayer("player3", type);
        Player player4 = createPlayer("player4", type);
        Player player5 = createPlayer("player5", type);

        assertRooms("{0=[player1, player2, player3], " +
                "1=[player4, player5]}");

        // when
        playerGames.reloadAll(true);

        // then
        Map<Integer, Collection<String>> rooms = getRooms();
        assertEquals("[player1, player2, player3, player4, player5]",
                rooms.values().stream()
                    .flatMap(Collection::stream)
                    .sorted()
                    .collect(toList())
                    .toString());
    }

    @Test
    public void testReloadAll_withoutShuffle_disposable() {
        // given
        MultiplayerType type = MultiplayerType.TEAM.apply(3, MultiplayerType.DISPOSABLE);
        Player player1 = createPlayer("player1", type);
        Player player2 = createPlayer("player2", type);
        Player player3 = createPlayer("player3", type);
        Player player4 = createPlayer("player4", type);
        Player player5 = createPlayer("player5", type);

        assertRooms("{0=[player1, player2, player3], " +
                "1=[player4, player5]}");

        // when
        playerGames.reloadAll(false);

        // then
        assertEquals(4, fields.size());
        assertRooms("{1=[player1], " +
                "2=[player2, player3, player4], " +
                "3=[player5]}");
    }

    @Test
    public void testReloadAll_withoutShuffle_notDisposable() {
        // TODO почему-то в этом тесте флаг игнорируется, то ли это условия такие, то ли бага
        // given
        MultiplayerType type = MultiplayerType.TEAM.apply(3, !MultiplayerType.DISPOSABLE);
        Player player1 = createPlayer("player1", type);
        Player player2 = createPlayer("player2", type);
        Player player3 = createPlayer("player3", type);
        Player player4 = createPlayer("player4", type);
        Player player5 = createPlayer("player5", type);

        assertRooms("{0=[player1, player2, player3], " +
                "1=[player4, player5]}");

        // when
        playerGames.reloadAll(false);

        // then
        assertEquals(4, fields.size());
        assertRooms("{1=[player1], " +
                "2=[player2, player3, player4], " +
                "3=[player5]}");
    }

    @Test
    public void testLoadLevelProgressFromSave_checkProgress() {
        // given when
        MultiplayerType type = MultiplayerType.TRAINING.apply(3);

        Player player1 = createPlayer("player1", type,
                new PlayerSave("{'levelProgress':{'total':3,'current':1,'lastPassed':0}}"));

        Player player2 = createPlayer("player2", type,
                new PlayerSave("{'levelProgress':{'total':3,'current':2,'lastPassed':1}}"));

        Player player3 = createPlayer("player3", type,
                new PlayerSave("{'levelProgress':{'total':3,'current':3,'lastPassed':2}}"));

        // then
        assertEquals(3, fields.size());
        assertProgress("player1", "{'current':1,'passed':0,'total':3,'valid':true}");
        assertProgress("player2", "{'current':2,'passed':1,'total':3,'valid':true}");
        assertProgress("player3", "{'current':3,'passed':2,'total':3,'valid':true}");
    }

    @Test
    public void testLoadLevelProgressFromSave_failBecauseBadFormat() {
        // given when
        try {
            createPlayer("player1", MultiplayerType.TRAINING.apply(3),
                    new PlayerSave("{'levelProgress':{'total':3,'current':50,'lastPassed':0}}"));
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Progress is invalid: {'current':50,'passed':0,'total':3,'valid':false}",
                    e.getMessage());
        }
    }

    @Test
    public void testLoadLevelProgressFromSave_allInMultiple() {
        // given when
        MultiplayerType type = MultiplayerType.TRAINING.apply(3);

        Player player1 = createPlayer("player1", type,
                new PlayerSave("{'levelProgress':{'total':3,'current':3,'lastPassed':2}}"));

        Player player2 = createPlayer("player2", type,
                new PlayerSave("{'levelProgress':{'total':3,'current':3,'lastPassed':2}}"));

        Player player3 = createPlayer("player3", type,
                new PlayerSave("{'levelProgress':{'total':3,'current':3,'lastPassed':2}}"));

        // then
        assertEquals(1, fields.size());
    }


    @Test
    public void testLoadFromSave_whenNullPlayerSave() {
        // given
        PlayerSave save = null;

        // when
        createPlayer("player1", MultiplayerType.SINGLE, save);

        // then
        verify(fields.get(0), never()).loadSave(anyObject());
    }

    @Test
    public void testLoadFromSave_whenNullSaveInPlayerSave() {
        // given
        String save = null;

        // when
        createPlayerFromSave("player1", save);

        // then
        verify(fields.get(0), never()).loadSave(anyObject());
    }

    @Test
    public void testLoadFromSave_whenEmptyStringSaveInPlayerSave() {
        // given
        String save = "";

        // when
        createPlayerFromSave("player1", save);

        // then
        verify(fields.get(0), never()).loadSave(anyObject());
    }

    @Test
    public void testLoadFromSave_whenNullStringSaveInPlayerSave() {
        // given
        String save = null;

        // when
        createPlayerFromSave("player1", save);

        // then
        verify(fields.get(0), never()).loadSave(anyObject());
    }

    @Test
    public void testLoadFromSave_whenEmptyJsonSaveInPlayerSave() {
        // given
        String save = "{}";

        // when
        createPlayerFromSave("player1", save);

        // then
        verify(fields.get(0), never()).loadSave(anyObject());
    }

    @Test
    public void testLoadFromSave_saveGoesToField() {
        // given
        String save = "{\"some\":\"data\"}";

        // when
        createPlayerFromSave("player1", save);

        // then
        verifyFiledLoadSave(0, "[{\"some\":\"data\"}]");
    }

    private void verifyFiledLoadSave(int field, String expected) {
        ArgumentCaptor<JSONObject> captor = ArgumentCaptor.forClass(JSONObject.class);
        verify(fields.get(field)).loadSave(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }

    private void verifyPlayerEventListenerLevelChanged(String playerName, String expected) {
        ArgumentCaptor<LevelProgress> captor = ArgumentCaptor.forClass(LevelProgress.class);
        Player player = playerGames.get(playerName).getPlayer();
        if (expected == null) {
            verifyNoMoreInteractions(player.getEventListener());
        } else {
            verify(player.getEventListener(), times(1)).levelChanged(captor.capture());
            assertEquals(expected, captor.getValue().toString());
        }
        reset(player.getEventListener());
    }

    @Test
    public void testLoadFromSave_saveGoesToField_anyLevelProgressWillRemove() {
        // given when
        String save = "{'levelProgress':{'total':3,'current':3,'lastPassed':2},'some':'data'}";

        createPlayerFromSave("player1", save);

        // then
        verifyFiledLoadSave(0, "[{\"some\":\"data\"}]");
    }

    private Player createPlayerFromSave(String name, String save) {
        return createPlayer(name, MultiplayerType.SINGLE, new PlayerSave(save));
    }

    @Test
    public void testGetGameSave_forTrainingMultiplayerType_caseNullFieldSave() {
        // given
        MultiplayerType type = MultiplayerType.TRAINING.apply(3);

        Player player = createPlayer("player", type,
                new PlayerSave("{'levelProgress':{'total':3,'current':3,'lastPassed':2}}"));

        when(fields.get(0).getSave()).thenReturn(null);

        // when then
        assertSave("player", "{\"levelProgress\":{\"total\":3,\"current\":3,\"lastPassed\":2}}");
    }

    @Test
    public void testGetGameSave_forTrainingMultiplayerType_caseNotNullFieldSave() {
        // given
        MultiplayerType type = MultiplayerType.TRAINING.apply(3);

        Player player = createPlayer("player", type,
                new PlayerSave("{'levelProgress':{'total':3,'current':3,'lastPassed':2}}"));

        when(fields.get(0).getSave()).thenReturn(new JSONObject("{'some':'data'}"));

        // when then
        assertSave("player", "{\"field\":{\"some\":\"data\"}," +
                        "\"levelProgress\":{\"total\":3,\"current\":3,\"lastPassed\":2}}");
    }

    @Test
    public void testGetGameSave_forOtherMultiplayerTypes() {
        // given
        MultiplayerType type = MultiplayerType.SINGLE;

        Player player = createPlayer("player", type,
                new PlayerSave("{'save':'data2'}"));

        when(fields.get(0).getSave()).thenReturn(new JSONObject("{'some':'data'}"));

        // when then
        assertSave("player", "{\"some\":\"data\"}");

    }

    private void assertSave(String playerName, String expected) {
        assertEquals(expected, playerGames.get(playerName).getGame().getSave().toString());
    }

    @Test
    public void testGetGameSave_forOtherMultiplayerTypes_caseIfNullSave() {
        // given
        MultiplayerType type = MultiplayerType.SINGLE;

        Player player = createPlayer("player", type,
                new PlayerSave("{'save':'data'}"));

        when(fields.get(0).getSave()).thenReturn(null);

        // when then
        assertSave("player", "{}");

    }

    @Test
    public void testGetBoardAsString_trainingMultiplayerType_caseJSON() {
        // given
        MultiplayerType type = MultiplayerType.TRAINING.apply(3);

        Player player = createPlayer("player", "room", "game", type, null,
                new JSONObject("{'board':'data'}"));

        when(fields.get(0).reader()).thenReturn(mock(BoardReader.class));

        // when then
        assertBoard("player",
                "{\"board\":\"data\"," +
                        "\"levelProgress\":{\"total\":3,\"current\":0,\"lastPassed\":-1}}");
    }

    private void assertBoard(String playerName, String expected) {
        assertEquals(expected,
                playerGames.get(playerName).getGame().getBoardAsString().toString());
    }

    @Test
    public void testGetBoardAsString_trainingMultiplayerType_caseString() {
        // given
        MultiplayerType type = MultiplayerType.TRAINING.apply(3);

        Player player = createPlayer("player", "room", "game", type, null,
                "board-data");

        when(fields.get(0).reader()).thenReturn(mock(BoardReader.class));

        // when then
        assertBoard("player",
                "{\"board\":\"board-data\"," +
                        "\"levelProgress\":{\"total\":3,\"current\":0,\"lastPassed\":-1}}");
    }

    @Test
    public void testGetBoardAsString_anyOtherMultiplayerType_caseString() {
        // given
        MultiplayerType type = MultiplayerType.SINGLE;

        Player player = createPlayer("player", "room", "game", type, null,
                "board");

        when(fields.get(0).reader()).thenReturn(mock(BoardReader.class));

        // when then
        assertBoard("player", "board");
    }

    @Test
    public void shouldNewPlayerGoToFirstLEvel_evenIfOtherOnMultiplayer_forTraining() {
        // given
        createPlayer("player1", MultiplayerType.TRAINING.apply(3),
                new PlayerSave("{'levelProgress':{'total':3,'current':3,'lastPassed':2}}"));

        // when
        createPlayer("player2", MultiplayerType.TRAINING.apply(3));
        playerGames.tick();

        // then
        assertProgress("player1", "{'current':3,'passed':2,'total':3,'valid':true}");
        assertProgress("player2", "{'current':0,'passed':-1,'total':3,'valid':true}");
    }

    @Test
    public void shouldNewPlayerGoToFirstLevel_forTraining() {
        // given
        createPlayer("player1", MultiplayerType.TRAINING.apply(3));
        createPlayer("player2", MultiplayerType.TRAINING.apply(3));

        // when
        playerGames.tick();

        // then
        assertProgress("player1", "{'current':0,'passed':-1,'total':3,'valid':true}");
        assertProgress("player2", "{'current':0,'passed':-1,'total':3,'valid':true}");
    }

    @Test
    public void shouldNewPlayerGoToFirstLevel_forSingle() {
        // given
        createPlayer("player1", MultiplayerType.SINGLE);
        createPlayer("player2", MultiplayerType.SINGLE);

        // when
        playerGames.tick();

        // then
        assertProgress("player1", "{'current':0,'passed':-1,'total':1,'valid':true}");
        assertProgress("player2", "{'current':0,'passed':-1,'total':1,'valid':true}");
    }

    private void assertProgress(String player, String expected) {
        assertEquals(expected,
                playerGames.get(player)
                        .getGame().getProgress().toString());
    }

    @Test
    public void whatIfTwoPlayersForDifferentTrainings() {
        // given
        // TODO обрати внимание, тут 3 и 5 - для одной игры, нельзя такого допускать
        createPlayer("player1", MultiplayerType.TRAINING.apply(3));
        createPlayer("player2", MultiplayerType.TRAINING.apply(5));

        // when
        playerGames.tick();

        // then
        assertProgress("player1", "{'current':0,'passed':-1,'total':3,'valid':true}");
        assertProgress("player2", "{'current':0,'passed':-1,'total':5,'valid':true}");
    }

    @Test
    public void shouldRemovePlayerFromBoard_whenShouldLeaveIsTrue() {
        // given
        createPlayer("player1", MultiplayerType.TRIPLE);
        createPlayer("player2", MultiplayerType.TRIPLE);
        createPlayer("player3", MultiplayerType.TRIPLE);

        reset(fields.get(0));
        assertEquals(1, fields.size());

        setPlayerStatus(0, true);  // still play on board
        setPlayerStatus(1, true);  // still play on board
        setPlayerStatus(2, false); // should leave + game over

        // when
        // shouldLeave + gameOver > remove player3 from board and crate on new board
        playerGames.tick();

        // then
        int newField = fields.size() - 1;
        assertEquals(2, fields.size());
        verify(fields.get(newField), times(1)).newGame(gamePlayers.get(2));

        assertEquals(3, playerGames.size());
        assertEquals(fields.get(0), playerGames.get("player1").getField());
        assertEquals(fields.get(0), playerGames.get("player2").getField());
        assertEquals(fields.get(1), playerGames.get("player3").getField());

        // when
        // add another player
        createPlayer("player4", MultiplayerType.TRIPLE);
        createPlayer("player5", MultiplayerType.TRIPLE);

        // then
        assertEquals(2, fields.size());
        assertEquals(5, playerGames.size());

        // это старая борда, там осталось 2 юзера, но она была заполенна в прошлом тремя и места там нет
        assertEquals(fields.get(0), playerGames.get("player1").getField());
        assertEquals(fields.get(0), playerGames.get("player2").getField());

        // а это новая и она наполняется новыми юзерами
        assertEquals(fields.get(1), playerGames.get("player3").getField());
        assertEquals(fields.get(1), playerGames.get("player4").getField());
        assertEquals(fields.get(1), playerGames.get("player5").getField());
    }


    @Test
    public void testChangeRoom_oneLeftTwoRemained() {
        // given
        MultiplayerType type = MultiplayerType.MULTIPLE;
        createPlayer("player1", "room", "game", type);
        createPlayer("player2", "room", "game", type);
        createPlayer("player3", "room", "game", type);

        assertRooms("{0=[player1, player2, player3]}");

        // when
        playerGames.changeRoom("player1", "otherRoom");

        // then
        assertRooms("{0=[player2, player3], " +
                "1=[player1]}");

        assertRoomsNames("{otherRoom=[[player1]], " +
                "room=[[player2, player3]]}");
    }

    @Test
    public void testChangeRoom_twoLeftOneRemained() {
        // given
        MultiplayerType type = MultiplayerType.MULTIPLE;
        createPlayer("player1", "room", "game", type);
        createPlayer("player2", "room", "game", type);
        createPlayer("player3", "room", "game", type);

        assertRooms("{0=[player1, player2, player3]}");

        // when
        playerGames.changeRoom("player1", "otherRoom");
        playerGames.changeRoom("player3", "otherRoom");

        // then
        assertRooms("{1=[player1, player3], " +
                "2=[player2]}"); // второй покинул alone комнату и зашел в новую

        assertRoomsNames("{otherRoom=[[player1, player3]], " +
                "room=[[player2]]}");
    }

    @Test
    public void testChangeRoom_nullRoomDoNothing() {
        // given
        MultiplayerType type = MultiplayerType.MULTIPLE;
        createPlayer("player1", "room", "game", type);
        createPlayer("player2", "room", "game", type);
        createPlayer("player3", "room", "game", type);

        assertRooms("{0=[player1, player2, player3]}");

        // when
        playerGames.changeRoom("player1", null);

        // then
        assertRooms("{0=[player1, player2, player3]}");

        assertRoomsNames("{room=[[player1, player2, player3]]}");
    }

    @Test
    public void testChangeRoom_keepSave() {
        // given
        MultiplayerType type = MultiplayerType.MULTIPLE;
        createPlayer("player1", "room", "game", type, new PlayerSave("{\"some\":\"data1\"}"));
        createPlayer("player2", "room", "game", type, new PlayerSave("{\"some\":\"data2\"}"));

        assertRooms("{0=[player1, player2]}");

        // when
        playerGames.changeRoom("player1", "otherRoom");

        // then
        assertRooms("{1=[player2], " +
                "2=[player1]}");

        assertSave("player1", "{\"some\":\"data1\"}");
        assertSave("player2", "{\"some\":\"data2\"}");
    }

    @Test
    public void testChangeRoom_caseTournament() {
        // given
        MultiplayerType type = MultiplayerType.TOURNAMENT;
        createPlayer("player1", "room", "game", type);
        createPlayer("player2", "room", "game", type);
        createPlayer("player3", "room", "game", type);
        createPlayer("player4", "room", "game", type);

        assertRooms("{0=[player1, player2], " +
                "1=[player3, player4]}");

        assertRoomsNames("{room=[[player1, player2], [player3, player4]]}");

        // when
        playerGames.changeRoom("player1", "otherRoom");
        playerGames.changeRoom("player2", "otherRoom");

        // then
        assertRooms("{1=[player3, player4], " +
                "3=[player1, player2]}");

        assertRoomsNames("{otherRoom=[[player1, player2]], " +
                "room=[[player3, player4]]}");
    }
}
