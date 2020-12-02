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
import org.mockito.Mockito;

import java.util.*;

import static com.codenjoy.dojo.services.PlayerGames.*;
import static com.codenjoy.dojo.services.multiplayer.MultiplayerType.DISPOSABLE;
import static com.codenjoy.dojo.services.multiplayer.MultiplayerType.RELOAD_ALONE;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PlayerGamesTest extends AbstractPlayerGamesTest {

    private static final int NEVER = 0;
    public static final int ONCE = 1;

    private PlayerGame removed;

    @Test
    public void testRemove() {
        // given
        Player player = createPlayer();

        assertEquals(false, playerGames.isEmpty());
        assertEquals(1, playerGames.size());
        PlayerGame playerGame = playerGames.get(player.getId());
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
        PlayerGame playerGame = playerGames.get(player.getId());

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

        PlayerGame playerGame = playerGames.get(otherPlayer.getId());

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

        PlayerGame playerGame1 = playerGames.get(player.getId());
        GameField field1 = playerGame1.getGame().getField();

        PlayerGame playerGame2 = playerGames.get(player2.getId());
        GameField field2 = playerGame2.getGame().getField();

        PlayerGame playerGame3 = playerGames.get(player3.getId());
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
    public void shouldTickLazyJoystickWhenTick_skipNonActiveRooms() {
        // given
        MultiplayerType type = MultiplayerType.SINGLE;
        createPlayer("player1", "room1", "game1", type);
        createPlayer("player2", "room2", "game2", type); // paused
        createPlayer("player3", "room3", "game1", type);

        setActive("room2", false);

        lazyJoysticks.get(0).right();
        verifyNoMoreInteractions(joysticks.get(0));

        lazyJoysticks.get(1).up();
        verifyNoMoreInteractions(joysticks.get(1));

        lazyJoysticks.get(2).down();
        verifyNoMoreInteractions(joysticks.get(2));

        // when
        playerGames.tick();

        // then
        verify(joysticks.get(0)).right();
        verifyNoMoreInteractions(joysticks.get(1)); // because paused
        verify(joysticks.get(2)).down();
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
        resetAllFields();
        int index = 0;
        playerIsNotAlive(index);

        // when
        playerGames.tick();

        // then
        verifyNewGameCreated(0);
    }

    private void playerIsNotAlive(int index) {
        when(gamePlayers.get(index).isAlive()).thenReturn(false);
    }

    private void resetAllFields() {
        fields.forEach(Mockito::reset);
    }

    @Test
    public void shouldNewGame_whenGameOver_caseAnyMultiplayerType_skipNonActiveRooms() {
        // given
        MultiplayerType type = MultiplayerType.SINGLE;
        createPlayer("player1", "room1", "game1", type);
        createPlayer("player2", "room2", "game2", type); // paused
        createPlayer("player3", "room3", "game1", type);

        setActive("room2", false);

        resetAllFields();
        playerIsNotAlive(0);
        playerIsNotAlive(1);
        playerIsNotAlive(2);

        // when
        playerGames.tick();

        // then
        verifyNewGameCreated(0);
        verifyNewGameCreated(1, NEVER); // because paused
        verifyNewGameCreated(2);

        // when
        setActive("room2", true);
        resetAllFields();

        playerGames.tick();

        // then
        verifyNewGameCreated(0);
        verifyNewGameCreated(1); // because active
        verifyNewGameCreated(2);
    }

    @Test
    public void shouldTickFields_onlyDistinctFields() {
        // given
        createPlayer("player1", "room1", "game1", MultiplayerType.MULTIPLE); // field 0
        createPlayer("player2", "room1", "game1", MultiplayerType.MULTIPLE);

        createPlayer("player3", "room2", "game2", MultiplayerType.SINGLE); // field 1
        createPlayer("player4", "room2", "game2", MultiplayerType.SINGLE); // field 2

        resetAllFields();

        // when
        playerGames.tick();

        // then
        assertEquals(3, fields.size()); // only 3 fields created
        verifyFieldTicked(0);
        verifyFieldTicked(1);
        verifyFieldTicked(2);
    }

    @Test
    public void shouldTickFields_onlyStuffedRooms() {
        // given

        // field 0 not stuffed
        createPlayer("player1", "room1", "game1", MultiplayerType.TRIPLE);
        createPlayer("player2", "room1", "game1", MultiplayerType.TRIPLE);

        // field 1 stuffed
        createPlayer("player3", "room2", "game1", MultiplayerType.TRIPLE);
        createPlayer("player4", "room2", "game1", MultiplayerType.TRIPLE);
        createPlayer("player5", "room2", "game1", MultiplayerType.TRIPLE);

        resetAllFields();

        // when
        playerGames.tick();

        // then
        assertEquals(2, fields.size()); // only 2 fields created
        verifyFieldTicked(0, NEVER);
        verifyFieldTicked(1);
    }

    @Test
    public void shouldTickFields_skipNonActiveRooms() {
        // given
        MultiplayerType type = MultiplayerType.SINGLE;
        createPlayer("player1", "room1", "game1", type);
        createPlayer("player2", "room2", "game2", type); // paused
        createPlayer("player3", "room3", "game1", type);

        setActive("room2", false);

        resetAllFields();

        // when
        playerGames.tick();

        // then
        verifyFieldTicked(0);
        verifyFieldTicked(1, NEVER); // because paused
        verifyFieldTicked(2);

        // when
        setActive("room2", true);
        resetAllFields();

        playerGames.tick();

        // then
        verifyFieldTicked(0);
        verifyFieldTicked(1); // because active
        verifyFieldTicked(2);
    }

    private void verifyFieldTicked(int index) {
        verifyFieldTicked(index, ONCE);
    }

    private void verifyFieldTicked(int index, int times) {
        verify(fields.get(index), times(times)).quietTick();
    }

    private void verifyNewGameCreated(int index) {
        verifyNewGameCreated(index, ONCE);
    }

    private void verifyNewGameCreated(int index, int times) {
        verify(fields.get(index), times(times)).newGame(gamePlayers.get(index));
    }

    @Test
    public void shouldNewGame_whenGameOver_caseTrainingMultiplayerType() {
        // given
        createPlayer("player2", "room", "game2", MultiplayerType.TRAINING.apply(3));

        resetAllFields();
        playerIsNotAlive(0);

        // when
        playerGames.tick();

        // then
        verifyNewGameCreated(0);
    }

    @Test
    public void shouldNextLevel_whenGameOver_andIsWin_caseTrainingMultiplayerType() {
        // given
        Player player = createPlayer(MultiplayerType.TRAINING.apply(3));

        resetAllFields();
        assertEquals(1, fields.size());
        playerIsNotAlive(0);
        playerIsWin(0);

        // then
        assertProgress("player", "{'current':1,'passed':0,'total':3,'valid':true}");

        // when
        // win + gameOver > next level
        playerGames.tick();

        // then
        String progress1 = "{'current':2,'passed':1,'total':3,'valid':true}";
        assertProgress("player", progress1);
        verifyPlayerEventListenerLevelChanged("player", progress1);

        int newField = fields.size() - 1;
        assertEquals(2, fields.size());
        verify(fields.get(newField), times(ONCE)).newGame(gamePlayers.get(0));
        reset(fields.get(newField));

        // when
        // win + gameOver > next (last) level
        playerGames.tick();

        // then
        String progress2 = "{'current':3,'passed':2,'total':3,'valid':true}";
        assertProgress("player", progress2);
        verifyPlayerEventListenerLevelChanged("player", progress2);

        newField = fields.size() - 1;
        assertEquals(3, fields.size());
        verify(fields.get(newField), times(ONCE)).newGame(gamePlayers.get(0));
        reset(fields.get(newField));

        // when
        // win + gameOver > this is last level - no changes
        playerGames.tick();

        // then
        assertProgress("player", "{'current':3,'passed':2,'total':3,'valid':true}");
        verifyPlayerEventListenerLevelChanged("player", null);

        newField = fields.size() - 1;
        assertEquals(3, fields.size());
        verify(fields.get(newField), times(ONCE)).newGame(gamePlayers.get(0));
        reset(fields.get(newField));
    }

    private void playerIsWin(int index) {
        when(gamePlayers.get(index).isWin()).thenReturn(true);
    }

    @Test
    public void testChangeLevel_cantBecauseNotPassedLevel() {
        // given
        createPlayer(MultiplayerType.TRAINING.apply(2));

        resetAllFields();
        assertEquals(1, fields.size());
        when(gamePlayers.get(0).isAlive()).thenReturn(true);
        when(gamePlayers.get(0).isWin()).thenReturn(false);

        // then
        String same = "{'current':1,'passed':0,'total':2,'valid':true}";
        assertProgress("player", same);

        // when
        playerGames.changeLevel("player", 2);

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
        assertProgress("player", "{'current':3,'passed':2,'total':3,'valid':true}");

        // when
        // change decrease - create new field
        playerGames.changeLevel("player", 2);

        // then
        String progress = "{'current':2,'passed':2,'total':3,'valid':true}";
        assertProgress("player", progress);
        verifyPlayerEventListenerLevelChanged("player", progress);

        int newField = fields.size() - 1;
        assertEquals(4, fields.size());
        verify(fields.get(newField), times(ONCE)).newGame(gamePlayers.get(0));
    }

    @Test
    public void testSetLevel_caseNotPassedLevel() {
        // given
        createPlayer(MultiplayerType.TRAINING.apply(2));

        resetAllFields();
        assertEquals(1, fields.size());
        when(gamePlayers.get(0).isAlive()).thenReturn(true);
        when(gamePlayers.get(0).isWin()).thenReturn(false);

        // then
        String same = "{'current':1,'passed':0,'total':2,'valid':true}";
        assertProgress("player", same);

        // when
        // change increase - don't create new field
        playerGames.setLevel("player", new JSONObject("{'levelProgress':{'current':1,'lastPassed':1,'total':2}}"));

        // then
        assertProgress("player", "{'current':1,'passed':1,'total':2,'valid':true}");

        int newField = fields.size() - 1;
        assertEquals(2, fields.size());
        verify(fields.get(newField), times(ONCE)).newGame(gamePlayers.get(0));
    }

    @Test
    public void testSetLevel_casePassedLevel() {
        shouldNextLevel_whenGameOver_andIsWin_caseTrainingMultiplayerType();

        // given
        assertEquals(3, fields.size());
        when(gamePlayers.get(0).isAlive()).thenReturn(true);
        when(gamePlayers.get(0).isWin()).thenReturn(false);

        // then
        assertProgress("player", "{'current':3,'passed':2,'total':3,'valid':true}");

        // when
        // change decrease - create new field
        playerGames.setLevel("player", new JSONObject("{'levelProgress':{'current':2,'lastPassed':2,'total':3}}"));

        // then
        String progress = "{'current':2,'passed':2,'total':3,'valid':true}";
        assertProgress("player", progress);
        verifyPlayerEventListenerLevelChanged("player", progress);

        int newField = fields.size() - 1;
        assertEquals(4, fields.size());
        verify(fields.get(newField), times(ONCE)).newGame(gamePlayers.get(0));
    }

    @Test
    public void testSetLevel_caseNoLevelData() {
        shouldNextLevel_whenGameOver_andIsWin_caseTrainingMultiplayerType();

        // given
        assertEquals(3, fields.size());
        when(gamePlayers.get(0).isAlive()).thenReturn(true);
        when(gamePlayers.get(0).isWin()).thenReturn(false);

        // then
        assertProgress("player", "{'current':3,'passed':2,'total':3,'valid':true}");

        // when
        playerGames.setLevel("player", new JSONObject("{}"));

        // then
        String progress = "{'current':1,'passed':0,'total':3,'valid':true}";
        assertProgress("player", progress);
        verifyPlayerEventListenerLevelChanged("player", progress);

        int newField = fields.size() - 1;
        assertEquals(4, fields.size());
        verify(fields.get(newField), times(ONCE)).newGame(gamePlayers.get(0));
    }

    @Test
    public void testSetLevel_caseNullLevelData() {
        shouldNextLevel_whenGameOver_andIsWin_caseTrainingMultiplayerType();

        // given
        assertEquals(3, fields.size());
        when(gamePlayers.get(0).isAlive()).thenReturn(true);
        when(gamePlayers.get(0).isWin()).thenReturn(false);

        // then
        String same = "{'current':3,'passed':2,'total':3,'valid':true}";
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
        assertRooms("{1=[player3]}");
        assertEquals(2, fields.size());

        verify(fields.get(1), times(ONCE)).newGame(gamePlayers.get(2));
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
        assertRooms("{0=[player3]}");
        assertEquals(1, fields.size());
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
        assertRooms("{1=[player1, player2]}");
        assertEquals(2, fields.size());

        verifyNewGameCreated(1);
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
        assertRooms("{0=[player2], 1=[player1]}");
        assertEquals(2, fields.size());
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
        MultiplayerType type = MultiplayerType.TEAM.apply(3, DISPOSABLE);
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
        assertRooms("{1=[player1], " +
                "2=[player2, player3, player4], " +
                "3=[player5]}");
        assertEquals(4, fields.size());
    }

    @Test
    public void testReloadAll_withoutShuffle_notDisposable() {
        // TODO почему-то в этом тесте флаг игнорируется, то ли это условия такие, то ли бага
        // given
        MultiplayerType type = MultiplayerType.TEAM.apply(3, !DISPOSABLE);
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
        assertRooms("{1=[player1], " +
                "2=[player2, player3, player4], " +
                "3=[player5]}");
        assertEquals(4, fields.size());
    }

    @Test
    public void testReloadAll_forRoom() {
        // given
        MultiplayerType type = MultiplayerType.TRIPLE;
        Player player1 = createPlayer("player1", "room1", "game1", type);
        Player player2 = createPlayer("player2", "room1", "game1", type);
        Player player3 = createPlayer("player3", "room1", "game1", type);
        Player player4 = createPlayer("player4", "room1", "game1", type);

        Player player5 = createPlayer("player5", "room2", "game1", type);
        Player player6 = createPlayer("player6", "room2", "game1", type);

        assertRooms("{0=[player1, player2, player3], " +
                "1=[player4], " +
                "2=[player5, player6]}");

        // when
        playerGames.reloadAll(false, withRoom("room1"));

        // then
        assertRooms("{1=[player1, player2], " +
                "2=[player5, player6], " + // didn't touch because room2
                "3=[player3, player4]}");
        assertEquals(4, fields.size());
    }

    @Test
    public void testReloadAll_forGame() {
        // given
        MultiplayerType type = MultiplayerType.TRIPLE;
        Player player1 = createPlayer("player1", "room1", "game1", type);
        Player player2 = createPlayer("player2", "room1", "game1", type);
        Player player3 = createPlayer("player3", "room1", "game1", type);
        Player player4 = createPlayer("player4", "room1", "game1", type);

        Player player5 = createPlayer("player5", "room2", "game2", type);
        Player player6 = createPlayer("player6", "room2", "game2", type);

        assertRooms("{0=[player1, player2, player3], " +
                "1=[player4], " +
                "2=[player5, player6]}");

        // when
        playerGames.reloadAll(false, withType("game1"));

        // then
        assertRooms("{1=[player1, player2], " +
                "2=[player5, player6], " + // didn't touch because game2
                "3=[player3, player4]}");
        assertEquals(4, fields.size());
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

    private void verifyPlayerEventListenerLevelChanged(String playerId, String expected) {
        ArgumentCaptor<LevelProgress> captor = ArgumentCaptor.forClass(LevelProgress.class);
        Player player = playerGames.get(playerId).getPlayer();
        if (expected == null) {
            verifyNoMoreInteractions(player.getEventListener());
        } else {
            verify(player.getEventListener(), times(ONCE)).levelChanged(captor.capture());
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
                        "\"levelProgress\":{\"total\":3,\"current\":1,\"lastPassed\":0}}");
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
                        "\"levelProgress\":{\"total\":3,\"current\":1,\"lastPassed\":0}}");
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
        assertProgress("player2", "{'current':1,'passed':0,'total':3,'valid':true}");
    }

    @Test
    public void shouldNewPlayerGoToFirstLevel_forTraining() {
        // given
        createPlayer("player1", MultiplayerType.TRAINING.apply(3));
        createPlayer("player2", MultiplayerType.TRAINING.apply(3));

        // when
        playerGames.tick();

        // then
        assertProgress("player1", "{'current':1,'passed':0,'total':3,'valid':true}");
        assertProgress("player2", "{'current':1,'passed':0,'total':3,'valid':true}");
    }

    @Test
    public void shouldNewPlayerGoToFirstLevel_forSingle() {
        // given
        createPlayer("player1", MultiplayerType.SINGLE);
        createPlayer("player2", MultiplayerType.SINGLE);

        // when
        playerGames.tick();

        // then
        assertProgress("player1", "{'current':1,'passed':0,'total':1,'valid':true}");
        assertProgress("player2", "{'current':1,'passed':0,'total':1,'valid':true}");
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
        assertProgress("player1", "{'current':1,'passed':0,'total':3,'valid':true}");
        assertProgress("player2", "{'current':1,'passed':0,'total':5,'valid':true}");
    }

    @Test
    public void shouldRemovePlayerFromBoard_whenShouldLeaveIsTrue() {
        // given
        createPlayer("player1", MultiplayerType.TRIPLE);
        createPlayer("player2", MultiplayerType.TRIPLE);
        createPlayer("player3", MultiplayerType.TRIPLE);

        resetAllFields();
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
        verify(fields.get(newField), times(ONCE)).newGame(gamePlayers.get(2));

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

    @Test
    public void testChangeRoom_caseLevels_disposable_reloadAlone() {
        // given
        MultiplayerType type = MultiplayerType.LEVELS.apply(2, 3, DISPOSABLE, RELOAD_ALONE);
        createPlayer("player1", "room", "game", type);
        createPlayer("player2", "room", "game", type);
        createPlayer("player3", "room", "game", type);
        createPlayer("player4", "room", "game", type);
        createPlayer("player5", "room", "game", type);

        assertRooms("{0=[player1, player2], " +
                "1=[player3, player4], " +
                "2=[player5]}");

        assertRoomsNames("{room[1]=[[player1, player2], [player3, player4], [player5]]}");

        // when
        playerGames.changeRoom("player1", "otherRoom");
        playerGames.changeRoom("player2", "otherRoom");

        // then
        // player1 покинул комнату и попал в отдельный мир otherRoom
        // player2 при этом остался однин в своей комнате и был удален оттуда
        //         попав к player5 у которого комната еще была недоукомплектована
        // затем и player2 ушел в отдельный мир otherRoom
        //         чем вынудил последнего в комнате player5 отправиться в новую комнату
        //         такое себе лишнее телодвижение TODO подумать может можно красиво решить
        assertRooms("{1=[player3, player4], " +
                "3=[player1, player2], " +
                "4=[player5]}");

        assertRoomsNames("{otherRoom[1]=[[player1, player2]], " +
                "room[1]=[[player3, player4], [player5]]}");
    }

    @Test
    public void testChangeRoom_caseLevels_disposable_dontReloadAlone() {
        // given
        MultiplayerType type = MultiplayerType.LEVELS.apply(2, 3, DISPOSABLE, !RELOAD_ALONE);
        createPlayer("player1", "room", "game", type);
        createPlayer("player2", "room", "game", type);
        createPlayer("player3", "room", "game", type);
        createPlayer("player4", "room", "game", type);
        createPlayer("player5", "room", "game", type);

        assertRooms("{0=[player1, player2], " +
                "1=[player3, player4], " +
                "2=[player5]}");

        assertRoomsNames("{room[1]=[[player1, player2], [player3, player4], [player5]]}");

        // when
        playerGames.changeRoom("player1", "otherRoom");
        playerGames.changeRoom("player2", "otherRoom");

        // then
        // player1 покинул комнату и попал в отдельный мир otherRoom
        // player2 при этом остался однин в своей комнате, т.к. !RELOAD_ALONE
        // затем и player2 ушел в отдельный мир otherRoom
        assertRooms("{1=[player3, player4], " +
                "2=[player5], " +
                "3=[player1, player2]}");

        assertRoomsNames("{otherRoom[1]=[[player1, player2]], " +
                "room[1]=[[player3, player4], [player5]]}");
    }

    @Test
    public void testChangeRoom_caseLevels_notDisposable_twoLeave_reloadAlone() {
        // given
        MultiplayerType type = MultiplayerType.LEVELS.apply(2, 3, !DISPOSABLE, RELOAD_ALONE);
        createPlayer("player1", "room", "game", type);
        createPlayer("player2", "room", "game", type);
        createPlayer("player3", "room", "game", type);
        createPlayer("player4", "room", "game", type);
        createPlayer("player5", "room", "game", type);

        assertRooms("{0=[player1, player2], " +
                "1=[player3, player4], " +
                "2=[player5]}");

        assertRoomsNames("{room[1]=[[player1, player2], [player3, player4], [player5]]}");

        // when
        playerGames.changeRoom("player1", "otherRoom");
        playerGames.changeRoom("player2", "otherRoom");

        // then
        // player1 покинул комнату и попал в отдельный мир otherRoom
        // player2 при этом остался однин в своей комнате и был удален оттуда
        //         попав к player5 у которого комната еще была недоукомплектована
        // затем и player2 ушел в отдельный мир otherRoom
        //         чем вынудил последнего в комнате player5 отправиться в новую комнату
        //         такое себе лишнее телодвижение TODO подумать может можно красиво решить
        assertRooms("{1=[player3, player4], " +
                "3=[player1, player2], " +
                "4=[player5]}");

        assertRoomsNames("{otherRoom[1]=[[player1, player2]], " +
                "room[1]=[[player3, player4], [player5]]}");
    }

    @Test
    public void testChangeRoom_caseLevels_notDisposable_twoLeave_dontReloadAlone() {
        // given
        MultiplayerType type = MultiplayerType.LEVELS.apply(2, 3, !DISPOSABLE, !RELOAD_ALONE);
        createPlayer("player1", "room", "game", type);
        createPlayer("player2", "room", "game", type);
        createPlayer("player3", "room", "game", type);
        createPlayer("player4", "room", "game", type);
        createPlayer("player5", "room", "game", type);

        assertRooms("{0=[player1, player2], " +
                "1=[player3, player4], " +
                "2=[player5]}");

        assertRoomsNames("{room[1]=[[player1, player2], [player3, player4], [player5]]}");

        // when
        playerGames.changeRoom("player1", "otherRoom");
        playerGames.changeRoom("player2", "otherRoom");

        // then
        // player1 покинул комнату и попал в отдельный мир otherRoom
        // player2 при этом остался однин в своей комнате
        // затем и player2 ушел в отдельный мир otherRoom
        assertRooms("{1=[player3, player4], " +
                "2=[player5], " +
                "3=[player1, player2]}");

        assertRoomsNames("{otherRoom[1]=[[player1, player2]], " +
                "room[1]=[[player3, player4], [player5]]}");
    }

    @Test
    public void testChangeRoom_caseLevels_notDisposable_oneLeave_reloadAlone() {
        // given
        MultiplayerType type = MultiplayerType.LEVELS.apply(2, 3, !DISPOSABLE, RELOAD_ALONE);
        createPlayer("player1", "room", "game", type);
        createPlayer("player2", "room", "game", type);
        createPlayer("player3", "room", "game", type);
        createPlayer("player4", "room", "game", type);
        createPlayer("player5", "room", "game", type);

        assertRooms("{0=[player1, player2], " +
                "1=[player3, player4], " +
                "2=[player5]}");

        assertRoomsNames("{room[1]=[[player1, player2], [player3, player4], [player5]]}");

        // when
        playerGames.changeRoom("player1", "otherRoom");

        // then
        // player1 покинул комнату и попал в отдельный мир otherRoom
        // player2 при этом остался однин в своей комнате и был удален оттуда
        //         попав к player5 у которого комната еще была недоукомплектована
        assertRooms("{1=[player3, player4], " +
                "2=[player2, player5], " +
                "3=[player1]}");

        assertRoomsNames("{otherRoom[1]=[[player1]], " +
                "room[1]=[[player3, player4], [player5, player2]]}");
    }

    @Test
    public void testChangeRoom_caseLevels_notDisposable_oneLeave_dontReloadAlone() {
        // given
        MultiplayerType type = MultiplayerType.LEVELS.apply(2, 3, !DISPOSABLE, !RELOAD_ALONE);
        createPlayer("player1", "room", "game", type);
        createPlayer("player2", "room", "game", type);
        createPlayer("player3", "room", "game", type);
        createPlayer("player4", "room", "game", type);
        createPlayer("player5", "room", "game", type);

        assertRooms("{0=[player1, player2], " +
                "1=[player3, player4], " +
                "2=[player5]}");

        assertRoomsNames("{room[1]=[[player1, player2], [player3, player4], [player5]]}");

        // when
        playerGames.changeRoom("player1", "otherRoom");

        // then
        // player1 покинул комнату и попал в отдельный мир otherRoom
        // player2 при этом остался однин в своей комнате
        assertRooms("{0=[player2], " +
                "1=[player3, player4], " +
                "2=[player5], 3=[player1]}");

        assertRoomsNames("{otherRoom[1]=[[player1]], " +
                "room[1]=[[player3, player4], [player5]]}");
    }

    @Test
    public void testGoNextLevel_caseLevels_notDisposable_firstLeave_reloadAlone() {
        // given
        MultiplayerType type = MultiplayerType.LEVELS.apply(3, 3, !DISPOSABLE, RELOAD_ALONE);
        createPlayer("player1", "room", "game", type);
        createPlayer("player2", "room", "game", type);
        createPlayer("player3", "room", "game", type);
        createPlayer("player4", "room", "game", type);
        createPlayer("player5", "room", "game", type);

        assertRooms("{0=[player1, player2, player3], " +
                "1=[player4, player5]}");

        assertRoomsNames("{room[1]=[[player1, player2, player3], [player4, player5]]}");

        // when
        // player1 переходит на новый уровень
        // при этом остальные игроки остаются где были
        playerGames.setLevel("player1", new JSONObject("{'levelProgress':" +
                "{'current':2,'lastPassed':1,'total':3}}"));

        // then
        assertRooms("{0=[player2, player3], " +
                "1=[player4, player5], 2=[player1]}");

        assertRoomsNames("{room[1]=[[player2, player3], [player4, player5]], " +
                "room[2]=[[player1]]}");

        // when
        // player1 возвращается обратно
        // и так как уровень не DISPOSABLE то игрок вернулся назад
        // в первую свободную комнату (свою же)
        playerGames.changeLevel("player1", 1);

        // then
        assertRooms("{0=[player1, player2, player3], " +
                "1=[player4, player5]}");

        assertRoomsNames("{room[1]=[[player2, player3, player1], [player4, player5]]}");
    }

    @Test
    public void testGoNextLevel_caseLevels_notDisposable_firstLeave_dontReloadAlone() {
        // given
        MultiplayerType type = MultiplayerType.LEVELS.apply(3, 3, !DISPOSABLE, !RELOAD_ALONE);
        createPlayer("player1", "room", "game", type);
        createPlayer("player2", "room", "game", type);
        createPlayer("player3", "room", "game", type);
        createPlayer("player4", "room", "game", type);
        createPlayer("player5", "room", "game", type);

        assertRooms("{0=[player1, player2, player3], " +
                "1=[player4, player5]}");

        assertRoomsNames("{room[1]=[[player1, player2, player3], [player4, player5]]}");

        // when
        // player1 переходит на новый уровень
        // при этом остальные игроки остаются где были
        playerGames.setLevel("player1", new JSONObject("{'levelProgress':" +
                "{'current':2,'lastPassed':1,'total':3}}"));

        // then
        assertRooms("{0=[player2, player3], " +
                "1=[player4, player5], 2=[player1]}");

        assertRoomsNames("{room[1]=[[player2, player3], [player4, player5]], " +
                "room[2]=[[player1]]}");

        // when
        // player1 возвращается обратно
        // и так как уровень не DISPOSABLE то игрок вернулся назад
        // в первую свободную комнату (свою же)
        playerGames.changeLevel("player1", 1);

        // then
        assertRooms("{0=[player1, player2, player3], " +
                "1=[player4, player5]}");

        assertRoomsNames("{room[1]=[[player2, player3, player1], [player4, player5]]}");
    }

    @Test
    public void testGoNextLevel_caseLevels_notDisposable_lastLeave_reloadAlone() {
        // given
        MultiplayerType type = MultiplayerType.LEVELS.apply(3, 3, !DISPOSABLE, RELOAD_ALONE);
        createPlayer("player1", "room", "game", type);
        createPlayer("player2", "room", "game", type);
        createPlayer("player3", "room", "game", type);
        createPlayer("player4", "room", "game", type);
        createPlayer("player5", "room", "game", type);
        createPlayer("player6", "room", "game", type);

        assertRooms("{0=[player1, player2, player3], " +
                "1=[player4, player5, player6]}");

        assertRoomsNames("{room[1]=[[player4, player5, player6], " +
                "[player1, player2, player3]]}");

        // when
        // player6 переходит на новый уровень
        // при этом остальные остаются на месте
        playerGames.setLevel("player6", new JSONObject("{'levelProgress':" +
                "{'current':2,'lastPassed':1,'total':3}}"));

        // then
        assertRooms("{0=[player1, player2, player3], " +
                "1=[player4, player5], " +
                "2=[player6]}");

        assertRoomsNames("{room[1]=[[player1, player2, player3], [player4, player5]], " +
                "room[2]=[[player6]]}");

        // when
        // player6 возвращается обратно
        // и так как уровень не DISPOSABLE его старая комната может его принять назад
        playerGames.changeLevel("player6", 1);

        // then
        assertRooms("{0=[player1, player2, player3], " +
                "1=[player4, player5, player6]}");

        assertRoomsNames("{room[1]=[[player4, player5, player6], " +
                "[player1, player2, player3]]}");
    }

    @Test
    public void testGoNextLevel_caseLevels_notDisposable_lastLeave_dontReloadAlone() {
        // given
        MultiplayerType type = MultiplayerType.LEVELS.apply(3, 3, !DISPOSABLE, !RELOAD_ALONE);
        createPlayer("player1", "room", "game", type);
        createPlayer("player2", "room", "game", type);
        createPlayer("player3", "room", "game", type);
        createPlayer("player4", "room", "game", type);
        createPlayer("player5", "room", "game", type);
        createPlayer("player6", "room", "game", type);

        assertRooms("{0=[player1, player2, player3], " +
                "1=[player4, player5, player6]}");

        assertRoomsNames("{room[1]=[[player4, player5, player6], " +
                "[player1, player2, player3]]}");

        // when
        // player6 переходит на новый уровень
        // при этом остальные остаются на месте
        playerGames.setLevel("player6", new JSONObject("{'levelProgress':" +
                "{'current':2,'lastPassed':1,'total':3}}"));

        // then
        assertRooms("{0=[player1, player2, player3], " +
                "1=[player4, player5], " +
                "2=[player6]}");

        assertRoomsNames("{room[1]=[[player1, player2, player3], [player4, player5]], " +
                "room[2]=[[player6]]}");

        // when
        // player6 возвращается обратно
        // и так как уровень не DISPOSABLE его старая комната может его принять назад
        playerGames.changeLevel("player6", 1);

        // then
        assertRooms("{0=[player1, player2, player3], " +
                "1=[player4, player5, player6]}");

        assertRoomsNames("{room[1]=[[player4, player5, player6], " +
                "[player1, player2, player3]]}");
    }

    @Test
    public void testGoNextLevel_caseLevels_disposable_firstLeave_reloadAlone() {
        // given
        MultiplayerType type = MultiplayerType.LEVELS.apply(3, 3, DISPOSABLE, RELOAD_ALONE);
        createPlayer("player1", "room", "game", type);
        createPlayer("player2", "room", "game", type);
        createPlayer("player3", "room", "game", type);
        createPlayer("player4", "room", "game", type);
        createPlayer("player5", "room", "game", type);

        assertRooms("{0=[player1, player2, player3], " +
                "1=[player4, player5]}");

        assertRoomsNames("{room[1]=[[player1, player2, player3], [player4, player5]]}");

        // when
        // player1 переходит на новый уровень
        // при этом остальные игроки остаются где были
        playerGames.setLevel("player1", new JSONObject("{'levelProgress':" +
                "{'current':2,'lastPassed':1,'total':3}}"));

        // then
        assertRooms("{0=[player2, player3], " +
                "1=[player4, player5], 2=[player1]}");

        assertRoomsNames("{room[1]=[[player2, player3], [player4, player5]], " +
                "room[2]=[[player1]]}");

        // when
        // player1 возвращается обратно
        // и так как уровень DISPOSABLE то игрок попадает в новую комнату к player4 и player5
        playerGames.changeLevel("player1", 1);

        // then
        assertRooms("{0=[player2, player3], " +
                "1=[player1, player4, player5]}");

        assertRoomsNames("{room[1]=[[player4, player5, player1], [player2, player3]]}");
    }

    @Test
    public void testGoNextLevel_caseLevels_disposable_firstLeave_dontReloadAlone() {
        // given
        MultiplayerType type = MultiplayerType.LEVELS.apply(3, 3, DISPOSABLE, !RELOAD_ALONE);
        createPlayer("player1", "room", "game", type);
        createPlayer("player2", "room", "game", type);
        createPlayer("player3", "room", "game", type);
        createPlayer("player4", "room", "game", type);
        createPlayer("player5", "room", "game", type);

        assertRooms("{0=[player1, player2, player3], " +
                "1=[player4, player5]}");

        assertRoomsNames("{room[1]=[[player1, player2, player3], [player4, player5]]}");

        // when
        // player1 переходит на новый уровень
        // при этом остальные игроки остаются где были
        playerGames.setLevel("player1", new JSONObject("{'levelProgress':" +
                "{'current':2,'lastPassed':1,'total':3}}"));

        // then
        assertRooms("{0=[player2, player3], " +
                "1=[player4, player5], 2=[player1]}");

        assertRoomsNames("{room[1]=[[player2, player3], [player4, player5]], " +
                "room[2]=[[player1]]}");

        // when
        // player1 возвращается обратно
        // и так как уровень DISPOSABLE то игрок попадает в новую комнату к player4 и player5
        playerGames.changeLevel("player1", 1);

        // then
        assertRooms("{0=[player2, player3], " +
                "1=[player1, player4, player5]}");

        assertRoomsNames("{room[1]=[[player4, player5, player1], [player2, player3]]}");
    }

    @Test
    public void testGoNextLevel_caseLevels_disposable_lastLeave_reloadAlone() {
        // given
        MultiplayerType type = MultiplayerType.LEVELS.apply(3, 3, DISPOSABLE, RELOAD_ALONE);
        createPlayer("player1", "room", "game", type);
        createPlayer("player2", "room", "game", type);
        createPlayer("player3", "room", "game", type);
        createPlayer("player4", "room", "game", type);
        createPlayer("player5", "room", "game", type);
        createPlayer("player6", "room", "game", type);

        assertRooms("{0=[player1, player2, player3], " +
                "1=[player4, player5, player6]}");

        assertRoomsNames("{room[1]=[[player4, player5, player6], " +
                "[player1, player2, player3]]}");

        // when
        // player6 переходит на новый уровень
        // при этом остальные остаются на месте
        playerGames.setLevel("player6", new JSONObject("{'levelProgress':" +
                "{'current':2,'lastPassed':1,'total':3}}"));

        // then
        assertRooms("{0=[player1, player2, player3], " +
                "1=[player4, player5], " +
                "2=[player6]}");

        assertRoomsNames("{room[1]=[[player1, player2, player3], [player4, player5]], " +
                "room[2]=[[player6]]}");

        // when
        // player6 возвращается обратно
        // и так как уровень DISPOSABLE его старая комната не может его принять назад
        // и он отправляется в новую
        playerGames.changeLevel("player6", 1);

        // then
        assertRooms("{0=[player1, player2, player3], " +
                "1=[player4, player5], " +
                "3=[player6]}");

        assertRoomsNames("{room[1]=[[player6], " +
                "[player1, player2, player3], " +
                "[player4, player5]]}");
    }

    @Test
    public void testGoNextLevel_caseLevels_disposable_lastLeave_dontReloadAlone() {
        // given
        MultiplayerType type = MultiplayerType.LEVELS.apply(3, 3, DISPOSABLE, !RELOAD_ALONE);
        createPlayer("player1", "room", "game", type);
        createPlayer("player2", "room", "game", type);
        createPlayer("player3", "room", "game", type);
        createPlayer("player4", "room", "game", type);
        createPlayer("player5", "room", "game", type);
        createPlayer("player6", "room", "game", type);

        assertRooms("{0=[player1, player2, player3], " +
                "1=[player4, player5, player6]}");

        assertRoomsNames("{room[1]=[[player4, player5, player6], " +
                "[player1, player2, player3]]}");

        // when
        // player6 переходит на новый уровень
        // при этом остальные остаются на месте
        playerGames.setLevel("player6", new JSONObject("{'levelProgress':" +
                "{'current':2,'lastPassed':1,'total':3}}"));

        // then
        assertRooms("{0=[player1, player2, player3], " +
                "1=[player4, player5], " +
                "2=[player6]}");

        assertRoomsNames("{room[1]=[[player1, player2, player3], [player4, player5]], " +
                "room[2]=[[player6]]}");

        // when
        // player6 возвращается обратно
        // и так как уровень DISPOSABLE его старая комната не может его принять назад
        // и он отправляется в новую
        playerGames.changeLevel("player6", 1);

        // then
        assertRooms("{0=[player1, player2, player3], " +
                "1=[player4, player5], " +
                "3=[player6]}");

        assertRoomsNames("{room[1]=[[player6], " +
                "[player1, player2, player3], " +
                "[player4, player5]]}");
    }

    @Test
    public void testGetAll_withType() {
        // given
        MultiplayerType type = MultiplayerType.SINGLE;
        createPlayer("player1", "room", "game1", type);
        createPlayer("player2", "room", "game1", type);
        createPlayer("player3", "room", "game2", type);
        createPlayer("player4", "room", "game3", type);

        // when then
        assertPlayers("[player1, player2]", playerGames.getAll(withType("game1")));
        assertPlayers("[player3]", playerGames.getAll(withType("game2")));
        assertPlayers("[player4]", playerGames.getAll(withType("game3")));
    }

    @Test
    public void testGetAll_withAll() {
        // given
        MultiplayerType type = MultiplayerType.SINGLE;
        createPlayer("player1", "room", "game1", type);
        createPlayer("player2", "room", "game1", type);
        createPlayer("player3", "room", "game2", type);
        createPlayer("player4", "room", "game3", type);

        // when then
        assertPlayers("[player1, player2, player3, player4]", playerGames.getAll(withAll()));
    }

    @Test
    public void testGetAll_withRoom() {
        // given
        MultiplayerType type = MultiplayerType.SINGLE;
        createPlayer("player1", "room1", "game1", type);
        createPlayer("player2", "room1", "game1", type);
        createPlayer("player3", "room2", "game2", type);
        createPlayer("player4", "room2", "game2", type);

        // when then
        assertPlayers("[player1, player2]", playerGames.getAll(withRoom("room1")));
        assertPlayers("[player3, player4]", playerGames.getAll(withRoom("room2")));
    }

    @Test
    public void testGetAll_withActive() {
        // given
        MultiplayerType type = MultiplayerType.SINGLE;
        createPlayer("player1", "room1", "game1", type);
        createPlayer("player2", "room1", "game1", type);
        createPlayer("player3", "room2", "game2", type); // paused
        createPlayer("player4", "room2", "game2", type); // paused
        createPlayer("player5", "room3", "game1", type);

        setActive("room2", false);

        // when then
        assertPlayers("[player1, player2, player5]", playerGames.getAll(playerGames.withActive()));
        assertPlayers("[player1, player2, player5]", playerGames.active());
    }

    @Test
    public void testGetRooms() {
        // given
        MultiplayerType type = MultiplayerType.SINGLE;
        createPlayer("player1", "room1", "game1", type);
        createPlayer("player2", "room1", "game1", type);
        createPlayer("player3", "room2", "game2", type); // paused
        createPlayer("player4", "room2", "game2", type); // paused
        createPlayer("player5", "room3", "game1", type);

        setActive("room2", false);

        // when then
        assertEquals("[room1, room3]", playerGames.getRooms(ACTIVE).toString());
        assertEquals("[room1, room2, room3]", playerGames.getRooms(ALL).toString());
    }

    private void assertPlayers(String expected, List<PlayerGame> list) {
        assertEquals(expected,
                list.stream()
                        .map(it -> it.getPlayer().getId())
                        .collect(toList())
                        .toString());
    }
}