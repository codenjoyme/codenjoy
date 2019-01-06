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


import com.codenjoy.dojo.client.Closeable;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.nullobj.NullPlayerGame;
import com.codenjoy.dojo.services.printer.BoardReader;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class PlayerGamesTest {

    private PlayerGames playerGames;
    private List<GameType> gameTypes = new LinkedList<>();
    private Map<Player, Closeable> ais = new HashMap<>();
    private List<Joystick> joysticks = new LinkedList<>();
    private List<Joystick> lazyJoysticks = new LinkedList<>();
    private List<GamePlayer> gamePlayers = new LinkedList<>();
    private List<GameField> fields = new LinkedList<>();

    @Before
    public void setUp() throws Exception {
        playerGames = new PlayerGames();
    }

    private PlayerGame removed;

    @Test
    public void testRemove() throws Exception {
        // given
        Player player = createPlayer();

        assertFalse(playerGames.isEmpty());
        assertEquals(1, playerGames.size());
        PlayerGame playerGame = playerGames.get(player.getName());
        GameField field = playerGame.getGame().getField();
        playerGames.onRemove(pg -> removed = pg);

        // when
        playerGames.remove(player);

        // then
        assertTrue(playerGames.isEmpty());
        assertEquals(0, playerGames.size());

        verifyRemove(playerGame, field);
        assertSame(removed, playerGame);
    }

    @Test
    public void testGet() throws Exception {
        // given
        Player player = createPlayer();

        assertSame(NullPlayerGame.INSTANCE, playerGames.get("bla"));

        // when
        PlayerGame playerGame = playerGames.get(player.getName());

        // then
        assertSame(player, playerGame.getPlayer());
//        assertSame(game, playerGame.getGame());
    }

    @Test
    public void testGetByIndex() throws Exception {
        // given
        Player player = createPlayer();

        // when
        PlayerGame playerGame = playerGames.get(0);

        // then
        assertSame(player, playerGame.getPlayer());
    }

    @Test
    public void testAdd() throws Exception {
        // given
        Player player = createPlayer();

        // when
        Player otherPlayer = createPlayer();

        // then
        assertFalse(playerGames.isEmpty());
        assertEquals(2, playerGames.size());

        PlayerGame playerGame = playerGames.get(otherPlayer.getName());

        assertSame(otherPlayer, playerGame.getPlayer());
    }

    @Test
    public void testIterator() throws Exception {
        // given
        Player player = createPlayer();
        Player otherPlayer = createPlayer();

        // when
        Iterator<PlayerGame> iterator = playerGames.iterator();

        // then
        assertTrue(iterator.hasNext());
        assertSame(player, iterator.next().getPlayer());

        assertTrue(iterator.hasNext());
        assertSame(otherPlayer, iterator.next().getPlayer());

        assertFalse(iterator.hasNext());
    }

    private Player createPlayer(String game) {
        return createPlayer(game, "player" + Calendar.getInstance().getTimeInMillis(),
                MultiplayerType.SINGLE);
    }

    private Player createPlayer(String gameName, String name, MultiplayerType type) {
        return createPlayer(gameName, name, type, null);
    }

    private Player createPlayer(String gameName, String name, MultiplayerType type, PlayerSave save) {
        return createPlayer(gameName, name, type, save, "board");
    }

    private Player createPlayer(String gameName, String name, MultiplayerType type, PlayerSave save, Object board) {
        GameService gameService = mock(GameService.class);
        GameType gameType = mock(GameType.class);
        gameTypes.add(gameType);
        PlayerScores scores = mock(PlayerScores.class);
        when(gameType.getPlayerScores(anyInt())).thenReturn(scores);
        when(gameType.name()).thenReturn(gameName);
        when(gameService.getGame(anyString())).thenReturn(gameType);

        Player player = new Player(name, "url", gameType, scores, mock(Information.class));
        Closeable ai = mock(Closeable.class);
        ais.put(player, ai);
        player.setAI(ai);

        playerGames.onAdd(pg -> lazyJoysticks.add(pg.getJoystick()));

        TestUtils.Env env =
                TestUtils.getPlayerGame(
                        playerGames,
                        player,
                        inv -> {
                            GameField field = mock(GameField.class);
                            fields.add(field);
                            return field;
                        },
                        type,
                        save,
                        parameters -> board
                );

        joysticks.add(env.joystick);
        gamePlayers.add(env.gamePlayer);

        return player;
    }

    @Test
    public void testPlayers() throws Exception {
        // given
        Player player = createPlayer();
        Player otherPlayer = createPlayer();

        // when
        List<Player> players = playerGames.players();

        // then
        assertSame(player, players.get(0));
        assertSame(otherPlayer, players.get(1));
        assertEquals(2, players.size());
    }

    @Test
    public void testGetAll() throws Exception {
        // given
        Player player = createPlayer();
        Player secondPlayer = createPlayer();
        Player thirdPlayer = createPlayer("game2");

        // when
        List<PlayerGame> result = playerGames.getAll("game");

        // then
        assertEquals(2, result.size());
        assertSame(player, result.get(0).getPlayer());
        assertSame(secondPlayer, result.get(1).getPlayer());

        // when
        List<PlayerGame> result2 = playerGames.getAll("game2");

        // then
        assertEquals(1, result2.size());
        assertSame(thirdPlayer, result2.get(0).getPlayer());
    }

    @Test
    public void testGetAllPlayersByType() throws Exception {
        // given
        Player player = createPlayer();
        Player secondPlayer = createPlayer();
        Player thirdPlayer = createPlayer("game2");

        // when
        List<Player> result = playerGames.getPlayers("game");

        // then
        assertEquals(2, result.size());
        assertSame(player, result.get(0));
        assertSame(secondPlayer, result.get(1));

        // when
        List<Player> result2 = playerGames.getPlayers("game2");

        // then
        assertEquals(1, result2.size());
        assertSame(thirdPlayer, result2.get(0));
    }

    private Player createPlayer() {
        return createPlayer("game");
    }

    @Test
    public void testClear() throws Exception {
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

    private void verifyRemove(PlayerGame playerGame, GameField field) {
        verify(field).remove(playerGame.getGame().getPlayer());
        verify(ais.get(playerGame.getPlayer())).close();
    }

    @Test
    public void testGetGameTypes() {
        // given
        Player player = createPlayer();
        Player player2 = createPlayer("game2");
        playerGames.add(player2, null);

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
        assertSame(gamePlayers.get(0), playerGame.getGame().getPlayer());
        assertSame(fields.get(0), playerGame.getField());
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
        createPlayer("game2", "player2", MultiplayerType.TRAINING.apply(3));

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
        createPlayer("game2", "player2",
                MultiplayerType.TRAINING.apply(2));

        reset(fields.get(0));
        assertEquals(1, fields.size());
        when(gamePlayers.get(0).isAlive()).thenReturn(false);
        when(gamePlayers.get(0).isWin()).thenReturn(true);

        // then
        assertEquals("{'current':0,'passed':-1,'total':2,'valid':true}",
                playerGames.get("player2")
                        .getGame().getProgress().toString());

        // when
        // win + gameOver > next level
        playerGames.tick();

        // then
        assertEquals("{'current':1,'passed':0,'total':2,'valid':true}",
                playerGames.get("player2")
                        .getGame().getProgress().toString());

        int newField = fields.size() - 1;
        assertEquals(2, fields.size());
        verify(fields.get(newField), times(1)).newGame(gamePlayers.get(0));
        reset(fields.get(newField));

        // when
        // win + gameOver > next (last) level
        playerGames.tick();

        // then
        assertEquals("{'current':2,'passed':1,'total':2,'valid':true}",
                playerGames.get("player2")
                        .getGame().getProgress().toString());

        newField = fields.size() - 1;
        assertEquals(3, fields.size());
        verify(fields.get(newField), times(1)).newGame(gamePlayers.get(0));
        reset(fields.get(newField));

        // when
        // win + gameOver > this is last level - no changes
        playerGames.tick();

        // then
        assertEquals("{'current':2,'passed':1,'total':2,'valid':true}",
                playerGames.get("player2")
                        .getGame().getProgress().toString());

        newField = fields.size() - 1;
        assertEquals(3, fields.size());
        verify(fields.get(newField), times(1)).newGame(gamePlayers.get(0));
        reset(fields.get(newField));
    }

    @Test
    public void testChangeLevel_cantBecauseNotPassedLevel() {
        // given
        createPlayer("game2", "player2",
                MultiplayerType.TRAINING.apply(2));

        reset(fields.get(0));
        assertEquals(1, fields.size());
        when(gamePlayers.get(0).isAlive()).thenReturn(true);
        when(gamePlayers.get(0).isWin()).thenReturn(false);

        // then
        String same = "{'current':0,'passed':-1,'total':2,'valid':true}";
        assertEquals(same,
                playerGames.get("player2")
                        .getGame().getProgress().toString());

        // when
        playerGames.changeLevel("player2", 1);

        // then
        assertEquals(same,
                playerGames.get("player2")
                        .getGame().getProgress().toString());

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
        assertEquals("{'current':2,'passed':1,'total':2,'valid':true}",
                playerGames.get("player2")
                        .getGame().getProgress().toString());

        // when
        // change decrease - create new field
        playerGames.changeLevel("player2", 1);

        // then
        assertEquals("{'current':1,'passed':1,'total':2,'valid':true}",
                playerGames.get("player2")
                        .getGame().getProgress().toString());

        int newField = fields.size() - 1;
        assertEquals(4, fields.size());
        verify(fields.get(newField), times(1)).newGame(gamePlayers.get(0));
    }

    @Test
    public void testSetLevel_caseNotPassedLevel() {
        // given
        createPlayer("game2", "player2",
                MultiplayerType.TRAINING.apply(2));

        reset(fields.get(0));
        assertEquals(1, fields.size());
        when(gamePlayers.get(0).isAlive()).thenReturn(true);
        when(gamePlayers.get(0).isWin()).thenReturn(false);

        // then
        String same = "{'current':0,'passed':-1,'total':2,'valid':true}";
        assertEquals(same,
                playerGames.get("player2")
                        .getGame().getProgress().toString());

        // when
        // change increase - don't create new field
        playerGames.setLevel("player2", new JSONObject("{'levelProgress':{'current':1,'lastPassed':1,'total':2}}"));

        // then
        assertEquals("{'current':1,'passed':1,'total':2,'valid':true}",
                playerGames.get("player2")
                        .getGame().getProgress().toString());

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
        assertEquals("{'current':2,'passed':1,'total':2,'valid':true}",
                playerGames.get("player2")
                        .getGame().getProgress().toString());

        // when
        // change decrease - create new field
        playerGames.setLevel("player2", new JSONObject("{'levelProgress':{'current':1,'lastPassed':1,'total':2}}"));

        // then
        assertEquals("{'current':1,'passed':1,'total':2,'valid':true}",
                playerGames.get("player2")
                        .getGame().getProgress().toString());

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
        assertEquals("{'current':2,'passed':1,'total':2,'valid':true}",
                playerGames.get("player2")
                        .getGame().getProgress().toString());

        // when
        playerGames.setLevel("player2", new JSONObject("{}"));

        // then
        assertEquals("{'current':0,'passed':-1,'total':2,'valid':true}",
                playerGames.get("player2")
                        .getGame().getProgress().toString());

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
        assertEquals("{'current':2,'passed':1,'total':2,'valid':true}",
                playerGames.get("player2")
                        .getGame().getProgress().toString());

        // when
        playerGames.setLevel("player2", null);

        // then
        assertEquals("{'current':2,'passed':1,'total':2,'valid':true}",
                playerGames.get("player2")
                        .getGame().getProgress().toString());

        int newField = fields.size() - 1;
        assertEquals(3, fields.size());
        verifyNoMoreInteractions(fields.get(newField));
    }

    @Test
    public void testResetAloneUsersField() {
        // given
        MultiplayerType type = MultiplayerType.MULTIPLE;
        Player player1 = createPlayer("game", "player1", type);
        Player player2 = createPlayer("game", "player2", type);
        Player player3 = createPlayer("game", "player3", type);

        GameField field3 = playerGames.get("player3").getGame().getField();

        assertEquals(1, fields.size());

        // when
        playerGames.remove(player1);

        // then
        assertEquals(1, fields.size());

        // when
        playerGames.remove(player2);

        // then
        // created new field for player3
        assertEquals(2, fields.size());
        GameField newField3 = playerGames.get("player3").getGame().getField();
        assertNotSame(field3, newField3);

        verify(fields.get(1), times(1)).newGame(gamePlayers.get(2));
    }

    @Test
    public void testLoadLevelProgressFromSave_checkProgress() {
        // given when
        MultiplayerType type = MultiplayerType.TRAINING.apply(3);

        Player player1 = createPlayer("game", "player1", type,
                new PlayerSave("{'levelProgress':{'total':3,'current':1,'lastPassed':0}}"));

        Player player2 = createPlayer("game", "player2", type,
                new PlayerSave("{'levelProgress':{'total':3,'current':2,'lastPassed':1}}"));

        Player player3 = createPlayer("game", "player3", type,
                new PlayerSave("{'levelProgress':{'total':3,'current':3,'lastPassed':2}}"));

        // then
        assertEquals(3, fields.size());

        assertEquals("{'current':1,'passed':0,'total':3,'valid':true}",
                playerGames.get("player1").getGame().getProgress().toString());

        assertEquals("{'current':2,'passed':1,'total':3,'valid':true}",
                playerGames.get("player2").getGame().getProgress().toString());

        assertEquals("{'current':3,'passed':2,'total':3,'valid':true}",
                playerGames.get("player3").getGame().getProgress().toString());
    }

    @Test
    public void testLoadLevelProgressFromSave_failBecauseBadFormat() {
        // given when
        try {
            createPlayer("game", "player1", MultiplayerType.TRAINING.apply(3),
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

        Player player1 = createPlayer("game", "player1", type,
                new PlayerSave("{'levelProgress':{'total':3,'current':3,'lastPassed':2}}"));

        Player player2 = createPlayer("game", "player2", type,
                new PlayerSave("{'levelProgress':{'total':3,'current':3,'lastPassed':2}}"));

        Player player3 = createPlayer("game", "player3", type,
                new PlayerSave("{'levelProgress':{'total':3,'current':3,'lastPassed':2}}"));

        // then
        assertEquals(1, fields.size());
    }


    @Test
    public void testLoadFromSave_whenNullPlayerSave() {
        // given when
        PlayerSave save = null;

        MultiplayerType type = MultiplayerType.SINGLE;
        Player player = createPlayer("game", "player1", type, save);

        // then
        verify(fields.get(0), never()).loadSave(anyObject());
    }

    @Test
    public void testLoadFromSave_whenNullSaveInPlayerSave() {
        // given when
        String stringSave = null;
        PlayerSave save = new PlayerSave(stringSave);

        MultiplayerType type = MultiplayerType.SINGLE;
        Player player = createPlayer("game", "player1", type, save);

        // then
        verify(fields.get(0), never()).loadSave(anyObject());
    }

    @Test
    public void testLoadFromSave_whenEmptyStringSaveInPlayerSave() {
        // given when
        String stringSave = "";
        PlayerSave save = new PlayerSave(stringSave);

        MultiplayerType type = MultiplayerType.SINGLE;
        Player player = createPlayer("game", "player1", type, save);

        // then
        verify(fields.get(0), never()).loadSave(anyObject());
    }

    @Test
    public void testLoadFromSave_whenNullStringSaveInPlayerSave() {
        // given when
        String stringSave = "null";
        PlayerSave save = new PlayerSave(stringSave);

        MultiplayerType type = MultiplayerType.SINGLE;
        Player player = createPlayer("game", "player1", type, save);

        // then
        verify(fields.get(0), never()).loadSave(anyObject());
    }

    @Test
    public void testLoadFromSave_whenEmptyJsonSaveInPlayerSave() {
        // given when
        String stringSave = "{}";
        PlayerSave save = new PlayerSave(stringSave);

        MultiplayerType type = MultiplayerType.SINGLE;
        Player player = createPlayer("game", "player1", type, save);

        // then
        verify(fields.get(0), never()).loadSave(anyObject());
    }

    @Test
    public void testLoadFromSave_saveGoesToField() {
        // given when
        String stringSave = "{\"some\":\"data\"}";
        PlayerSave save = new PlayerSave(stringSave);

        MultiplayerType type = MultiplayerType.SINGLE;
        Player player = createPlayer("game", "player1", type, save);

        // then
        ArgumentCaptor<JSONObject> captor = ArgumentCaptor.forClass(JSONObject.class);
        verify(fields.get(0)).loadSave(captor.capture());
        assertEquals("[{\"some\":\"data\"}]", captor.getAllValues().toString());
    }

    @Test
    public void testLoadFromSave_saveGoesToField_anyLevelProgressWillRemove() {
        // given when
        String stringSave = "{'levelProgress':{'total':3,'current':3,'lastPassed':2},'some':'data'}";
        PlayerSave save = new PlayerSave(stringSave);

        MultiplayerType type = MultiplayerType.SINGLE;
        Player player = createPlayer("game", "player1", type, save);

        // then
        ArgumentCaptor<JSONObject> captor = ArgumentCaptor.forClass(JSONObject.class);
        verify(fields.get(0)).loadSave(captor.capture());
        assertEquals("[{\"some\":\"data\"}]", captor.getAllValues().toString());
    }

    @Test
    public void testGetGameSave_forTrainingMultiplayerType_caseNullFieldSave() {
        // given
        MultiplayerType type = MultiplayerType.TRAINING.apply(3);

        Player player = createPlayer("game", "player", type,
                new PlayerSave("{'levelProgress':{'total':3,'current':3,'lastPassed':2}}"));

        when(fields.get(0).getSave()).thenReturn(null);

        // when then
        assertEquals("{\"levelProgress\":{\"total\":3,\"current\":3,\"lastPassed\":2}}",
                playerGames.get("player").getGame().getSave().toString());
    }

    @Test
    public void testGetGameSave_forTrainingMultiplayerType_caseNotNullFieldSave() {
        // given
        MultiplayerType type = MultiplayerType.TRAINING.apply(3);

        Player player = createPlayer("game", "player", type,
                new PlayerSave("{'levelProgress':{'total':3,'current':3,'lastPassed':2}}"));

        when(fields.get(0).getSave()).thenReturn(new JSONObject("{'some':'data'}"));

        // when then
        assertEquals("{\"field\":{\"some\":\"data\"}," +
                        "\"levelProgress\":{\"total\":3,\"current\":3,\"lastPassed\":2}}",
                playerGames.get("player").getGame().getSave().toString());
    }

    @Test
    public void testGetGameSave_forOtherMultiplayerTypes() {
        // given
        MultiplayerType type = MultiplayerType.SINGLE;

        Player player = createPlayer("game", "player", type,
                new PlayerSave("{'save':'data2'}"));

        when(fields.get(0).getSave()).thenReturn(new JSONObject("{'some':'data'}"));

        // when then
        assertEquals("{\"some\":\"data\"}",
                playerGames.get("player").getGame().getSave().toString());

    }

    @Test
    public void testGetGameSave_forOtherMultiplayerTypes_caseIfNullSave() {
        // given
        MultiplayerType type = MultiplayerType.SINGLE;

        Player player = createPlayer("game", "player", type,
                new PlayerSave("{'save':'data'}"));

        when(fields.get(0).getSave()).thenReturn(null);

        // when then
        assertEquals("{}",
                playerGames.get("player").getGame().getSave().toString());

    }

    @Test
    public void testGetBoardAsString_trainingMultiplayerType_caseJSON() {
        // given
        MultiplayerType type = MultiplayerType.TRAINING.apply(3);

        Player player = createPlayer("game", "player", type, null,
                new JSONObject("{'board':'data'}"));

        when(fields.get(0).reader()).thenReturn(mock(BoardReader.class));

        // when then
        assertEquals("{\"board\":\"data\"," +
                        "\"levelProgress\":{\"total\":3,\"current\":0,\"lastPassed\":-1}}",
                playerGames.get("player").getGame().getBoardAsString().toString());
    }

    @Test
    public void testGetBoardAsString_trainingMultiplayerType_caseString() {
        // given
        MultiplayerType type = MultiplayerType.TRAINING.apply(3);

        Player player = createPlayer("game", "player", type, null,
                "board-data");

        when(fields.get(0).reader()).thenReturn(mock(BoardReader.class));

        // when then
        assertEquals("{\"board\":\"board-data\"," +
                        "\"levelProgress\":{\"total\":3,\"current\":0,\"lastPassed\":-1}}",
                playerGames.get("player").getGame().getBoardAsString().toString());
    }

    @Test
    public void testGetBoardAsString_anyOtherMultiplayerType_caseString() {
        // given
        MultiplayerType type = MultiplayerType.SINGLE;

        Player player = createPlayer("game", "player", type, null,
                "board");

        when(fields.get(0).reader()).thenReturn(mock(BoardReader.class));

        // when then
        assertEquals("board",
                playerGames.get("player").getGame().getBoardAsString().toString());
    }

    @Test
    public void shouldNewPlayerGoToFirstLEvel_evenIfOtherOnMultiplayer_forTraining() {
        // given
        createPlayer("game", "player1", MultiplayerType.TRAINING.apply(3),
                new PlayerSave("{'levelProgress':{'total':3,'current':3,'lastPassed':2}}"));

        // when
        createPlayer("game", "player2", MultiplayerType.TRAINING.apply(3));
        playerGames.tick();

        // then
        assertEquals("{'current':3,'passed':2,'total':3,'valid':true}",
                playerGames.get("player1")
                        .getGame().getProgress().toString());

        assertEquals("{'current':0,'passed':-1,'total':3,'valid':true}",
                playerGames.get("player2")
                        .getGame().getProgress().toString());
    }

    @Test
    public void shouldNewPlayerGoToFirstLevel_forTraining() {
        // given
        createPlayer("game", "player1", MultiplayerType.TRAINING.apply(3));
        createPlayer("game", "player2", MultiplayerType.TRAINING.apply(3));

        // when
        playerGames.tick();

        // then
        assertEquals("{'current':0,'passed':-1,'total':3,'valid':true}",
                playerGames.get("player1")
                        .getGame().getProgress().toString());

        assertEquals("{'current':0,'passed':-1,'total':3,'valid':true}",
                playerGames.get("player2")
                        .getGame().getProgress().toString());
    }

    @Test
    public void shouldNewPlayerGoToFirstLevel_forSingle() {
        // given
        createPlayer("game", "player1", MultiplayerType.SINGLE);
        createPlayer("game", "player2", MultiplayerType.SINGLE);

        // when
        playerGames.tick();

        // then
        assertEquals("{'current':0,'passed':-1,'total':1,'valid':true}",
                playerGames.get("player1")
                        .getGame().getProgress().toString());

        assertEquals("{'current':0,'passed':-1,'total':1,'valid':true}",
                playerGames.get("player2")
                        .getGame().getProgress().toString());
    }

    @Test
    public void whatIfTwoPlayersForDifferentTrainings() {
        // given
        // TODO обрати внимание, тут 3 и 5 - для ожной игры, нельзя такого допускать
        createPlayer("game", "player1", MultiplayerType.TRAINING.apply(3));
        createPlayer("game", "player2", MultiplayerType.TRAINING.apply(5));

        // when
        playerGames.tick();

        // then
        assertEquals("{'current':0,'passed':-1,'total':3,'valid':true}",
                playerGames.get("player1")
                        .getGame().getProgress().toString());

        assertEquals("{'current':0,'passed':-1,'total':5,'valid':true}",
                playerGames.get("player2")
                        .getGame().getProgress().toString());
    }

    @Test
    public void shouldRemovePlayerFromBoard_whenShouldLeaveIsTrue() {
        // given
        createPlayer("game", "player1", MultiplayerType.TRIPLE);
        createPlayer("game", "player2", MultiplayerType.TRIPLE);
        createPlayer("game", "player3", MultiplayerType.TRIPLE);

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
        assertSame(fields.get(0), playerGames.get("player1").getField());
        assertSame(fields.get(0), playerGames.get("player2").getField());
        assertSame(fields.get(1), playerGames.get("player3").getField());

        // when
        // add another player
        createPlayer("game", "player4", MultiplayerType.TRIPLE);
        createPlayer("game", "player5", MultiplayerType.TRIPLE);

        // then
        assertEquals(2, fields.size());
        assertEquals(5, playerGames.size());

        // это старая борда, там осталось 2 юзера, но она была заполенна в прошлом тремя и места там нет
        assertSame(fields.get(0), playerGames.get("player1").getField());
        assertSame(fields.get(0), playerGames.get("player2").getField());

        // а это новая и она наполняется новыми юзерами
        assertSame(fields.get(1), playerGames.get("player3").getField());
        assertSame(fields.get(1), playerGames.get("player4").getField());
        assertSame(fields.get(1), playerGames.get("player5").getField());
    }

    private void setPlayerStatus(int index, boolean stillPlay) {
        when(gamePlayers.get(index).isAlive()).thenReturn(stillPlay);
        when(gamePlayers.get(index).isWin()).thenReturn(!stillPlay);
        when(gamePlayers.get(index).shouldLeave()).thenReturn(!stillPlay);
    }
}
