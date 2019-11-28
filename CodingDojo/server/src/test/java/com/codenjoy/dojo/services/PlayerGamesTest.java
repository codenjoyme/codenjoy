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
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.nullobj.NullPlayerGame;
import com.codenjoy.dojo.services.printer.BoardReader;
import org.json.JSONObject;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
        assertEquals(true,playerGames.isEmpty());
        assertEquals(0, playerGames.size());

        verifyRemove(playerGame, field);
        assertEquals(removed, playerGame);
    }

    @Test
    public void testGet() {
        // given
        Player player = createPlayer();

        assertEquals(NullPlayerGame.INSTANCE, playerGames.get("bla"));

        // when
        PlayerGame playerGame = playerGames.get(player.getName());

        // then
        assertEquals(player, playerGame.getPlayer());
//        assertEquals(game, playerGame.getGame());
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
    public void testResetAloneUsersField_whenRemove() {
        // given
        MultiplayerType type = MultiplayerType.MULTIPLE;
        Player player1 = createPlayer("game", "player1", type);
        Player player2 = createPlayer("game", "player2", type);
        Player player3 = createPlayer("game", "player3", type);

        assertR("{0=[player1, player2, player3]}");

        // when
        playerGames.remove(player1);

        // then
        assertEquals(1, fields.size());

        // when
        playerGames.remove(player2);

        // then
        // created new field for player3
        assertEquals(2, fields.size());
        assertR("{1=[player3]}");

        verify(fields.get(1), times(1)).newGame(gamePlayers.get(2));
    }

    @Test
    public void testDontResetAloneUsersField_whenRemoveCurrent() {
        // given
        MultiplayerType type = MultiplayerType.MULTIPLE;
        Player player1 = createPlayer("game", "player1", type);
        Player player2 = createPlayer("game", "player2", type);
        Player player3 = createPlayer("game", "player3", type);

        assertR("{0=[player1, player2, player3]}");

        // when
        playerGames.removeCurrent(player1);

        // then
        assertEquals(1, fields.size());

        // when
        playerGames.removeCurrent(player2);

        // then
        assertEquals(1, fields.size());
        assertR("{0=[player3]}");
    }

    @Test
    public void testResetAloneUsersField_whenReload() {
        // given
        MultiplayerType type = MultiplayerType.TOURNAMENT;
        Player player1 = createPlayer("game", "player1", type);
        Player player2 = createPlayer("game", "player2", type);

        assertR("{0=[player1, player2]}");

        // when
        Game game = playerGames.get(0).getGame();
        playerGames.reload(game, game.getSave());

        // then
        // created new field for player3
        assertEquals(2, fields.size());
        assertR("{1=[player1, player2]}");

        verify(fields.get(1), times(1)).newGame(gamePlayers.get(1));
    }

    @Test
    public void testDontResetAloneUsersField_whenReloadCurrent() {
        // given
        MultiplayerType type = MultiplayerType.TOURNAMENT;
        Player player1 = createPlayer("game", "player1", type);
        Player player2 = createPlayer("game", "player2", type);

        assertR("{0=[player1, player2]}");

        // when
        PlayerGame playerGame = playerGames.get(0);
        playerGames.reloadCurrent(playerGame);

        // then
        assertEquals(2, fields.size());
        assertR("{0=[player2], 1=[player1]}");
    }

    @Test
    public void testReloadAll_withShuffle() {
        // given
        MultiplayerType type = MultiplayerType.TRIPLE;
        Player player1 = createPlayer("game", "player1", type);
        Player player2 = createPlayer("game", "player2", type);
        Player player3 = createPlayer("game", "player3", type);
        Player player4 = createPlayer("game", "player4", type);
        Player player5 = createPlayer("game", "player5", type);

        assertR("{0=[player1, player2, player3], " +
                "1=[player4, player5]}");

        // when
        playerGames.reloadAll(true);

        // then
        Map<Integer, List<String>> rooms = getRooms();
        assertEquals("[player1, player2, player3, player4, player5]",
                rooms.values().stream()
                    .flatMap(List::stream)
                    .sorted()
                    .collect(toList())
                    .toString());
    }

    @Test
    public void testReloadAll_withoutShuffle_disposable() {
        // given
        MultiplayerType type = MultiplayerType.TEAM.apply(3, MultiplayerType.DISPOSABLE);
        Player player1 = createPlayer("game", "player1", type);
        Player player2 = createPlayer("game", "player2", type);
        Player player3 = createPlayer("game", "player3", type);
        Player player4 = createPlayer("game", "player4", type);
        Player player5 = createPlayer("game", "player5", type);

        assertR("{0=[player1, player2, player3], " +
                "1=[player4, player5]}");

        // when
        playerGames.reloadAll(false);

        // then
        assertEquals(4, fields.size());
        assertR("{1=[player1], " +
                "2=[player2, player3, player4], " +
                "3=[player5]}");
    }

    @Test
    public void testReloadAll_withoutShuffle_notDisposable() {
        // TODO почему-то в этом тесте флаг игнорируется, то ли это условия такие, то ли бага
        // given
        MultiplayerType type = MultiplayerType.TEAM.apply(3, !MultiplayerType.DISPOSABLE);
        Player player1 = createPlayer("game", "player1", type);
        Player player2 = createPlayer("game", "player2", type);
        Player player3 = createPlayer("game", "player3", type);
        Player player4 = createPlayer("game", "player4", type);
        Player player5 = createPlayer("game", "player5", type);

        assertR("{0=[player1, player2, player3], " +
                "1=[player4, player5]}");

        // when
        playerGames.reloadAll(false);

        // then
        assertEquals(4, fields.size());
        assertR("{1=[player1], " +
                "2=[player2, player3, player4], " +
                "3=[player5]}");
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
        assertEquals(fields.get(0), playerGames.get("player1").getField());
        assertEquals(fields.get(0), playerGames.get("player2").getField());
        assertEquals(fields.get(1), playerGames.get("player3").getField());

        // when
        // add another player
        createPlayer("game", "player4", MultiplayerType.TRIPLE);
        createPlayer("game", "player5", MultiplayerType.TRIPLE);

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


}
