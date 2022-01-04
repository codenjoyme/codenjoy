package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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
import com.codenjoy.dojo.services.multiplayer.Sweeper;
import com.codenjoy.dojo.services.nullobj.NullDeal;
import com.codenjoy.dojo.services.printer.BoardReader;
import org.json.JSONObject;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.codenjoy.dojo.services.Deals.*;
import static com.codenjoy.dojo.services.multiplayer.GamePlayer.DEFAULT_TEAM_ID;
import static com.codenjoy.dojo.services.multiplayer.MultiplayerType.DISPOSABLE;
import static com.codenjoy.dojo.services.multiplayer.MultiplayerType.RELOAD_ALONE;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class DealsTest extends AbstractDealsTest {

    private static final int NEVER = 0;
    public static final int ONCE = 1;

    private Deal removed;

    @Test
    public void testRemove() {
        // given
        Player player = createPlayer();

        assertEquals(false, deals.isEmpty());
        assertEquals(1, deals.size());
        Deal deal = deals.get(player.getId());
        GameField field = deal.getGame().getField();
        deals.onRemove(otherDeal -> removed = otherDeal);

        // when
        deals.remove(player.getId(), Sweeper.off());

        // then
        assertEquals(true, deals.isEmpty());
        assertEquals(0, deals.size());

        verifyRemove(deal, field);
        assertEquals(removed, deal);
    }

    @Test
    public void testGet_notExists() {
        // when
        Deal deal = deals.get("bla");

        // then
        assertEquals(NullDeal.INSTANCE, deal);
    }

    @Test
    public void testGet_exists() {
        // given
        Player player = createPlayer();

        // when
        Deal deal = deals.get(player.getId());

        // then
        assertEquals(player, deal.getPlayer());
        assertEquals(fields.get(0), deal.getGame().getField());
    }

    @Test
    public void testGetByIndex() {
        // given
        Player player = createPlayer();

        // when
        Deal deal = deals.get(0);

        // then
        assertEquals(player, deal.getPlayer());
    }

    @Test
    public void testAdd() {
        // given
        Player player = createPlayer();

        // when
        Player otherPlayer = createPlayer();

        // then
        assertEquals(false, deals.isEmpty());
        assertEquals(2, deals.size());

        Deal deal = deals.get(otherPlayer.getId());

        // TODO интересная бага, время от времени при запуске всех тестов parent проекта этот ассерт слетает потому что == не то же самое что equals. Интересный квест почему. Не критично, просто любопытно
        // System.out.println("==> " + (otherPlayer == deal.getPlayer()));
        // System.out.println("eq> " + (otherPlayer.equals(deal.getPlayer())));
        // assertSame(otherPlayer, deal.getPlayer());
        assertEquals(otherPlayer, deal.getPlayer());
    }

    @Test
    public void testIterator() {
        // given
        Player player = createPlayer();
        Player otherPlayer = createPlayer();

        // when
        Iterator<Deal> iterator = deals.iterator();

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
        List<Player> players = deals.players();

        // then
        assertEquals(player, players.get(0));
        assertEquals(otherPlayer, players.get(1));
        assertEquals(2, players.size());
    }

    @Test
    public void testGetAllPlayersByType() {
        // given
        Player player = createPlayer("room", "game");
        Player secondPlayer = createPlayer("room", "game");
        Player thirdPlayer = createPlayer("room2", "game2");

        // when
        List<Player> result = deals.getPlayersByGame("game");

        // then
        assertEquals(2, result.size());
        assertEquals(player, result.get(0));
        assertEquals(secondPlayer, result.get(1));

        // when
        List<Player> result2 = deals.getPlayersByGame("game2");

        // then
        assertEquals(1, result2.size());
        assertEquals(thirdPlayer, result2.get(0));
    }

    @Test
    public void testGetAllPlayersByRoom() {
        // given
        Player player = createPlayer("room1", "game");
        Player secondPlayer = createPlayer("room1", "game");
        Player thirdPlayer = createPlayer("room2", "game2");

        // when
        List<Player> result = deals.getPlayersByRoom("room1");

        // then
        assertEquals(2, result.size());
        assertEquals(player, result.get(0));
        assertEquals(secondPlayer, result.get(1));

        // when
        List<Player> result2 = deals.getPlayersByRoom("room2");

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

        Deal deal1 = deals.get(player.getId());
        GameField field1 = deal1.getGame().getField();

        Deal deal2 = deals.get(player2.getId());
        GameField field2 = deal2.getGame().getField();

        Deal deal3 = deals.get(player3.getId());
        GameField field3 = deal3.getGame().getField();

        assertEquals(3, deals.size());

        // when
        deals.clear();

        // then
        assertEquals(0, deals.size());

        verifyRemove(deal1, field1);
        verifyRemove(deal2, field2);
        verifyRemove(deal3, field3);
    }

    @Test
    public void testGetGameTypes() {
        // given
        Player player = createPlayer("room", "game");
        Player player2 = createPlayer("room2", "game2");
        deals.add(player2, "room2", null);

        // when
        List<GameType> gameTypes = deals.getGameTypes();

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
        deals.tick();

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

        givenActive("room2", false);

        lazyJoysticks.get(0).right();
        verifyNoMoreInteractions(joysticks.get(0));

        lazyJoysticks.get(1).up();
        verifyNoMoreInteractions(joysticks.get(1));

        lazyJoysticks.get(2).down();
        verifyNoMoreInteractions(joysticks.get(2));

        // when
        deals.tick();

        // then
        verify(joysticks.get(0)).right();
        verifyNoMoreInteractions(joysticks.get(1)); // because paused
        verify(joysticks.get(2)).down();
    }

    @Test
    public void shouldTickGameType() {
        // given
        createPlayer("room2", "game2");
        createPlayer("room3", "game3");
        createPlayer("room2", "game2"); // второй игрок к уже существующей room2/game2

        // when
        deals.tick();

        // then
        assertEquals(2, gameTypes.size());
        InOrder order = inOrder(gameTypes.get(0), gameTypes.get(1));

        order.verify(gameTypes.get(0)).quietTick();
        order.verify(gameTypes.get(1)).quietTick();
    }

    @Test
    public void shouldNewGame_whenGameOver_caseAnyMultiplayerType() {
        // given
        createPlayer();
        resetAllFields();
        int index = 0;
        playerIsNotAlive(index);

        // when
        deals.tick();

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

        givenActive("room2", false);

        resetAllFields();
        playerIsNotAlive(0);
        playerIsNotAlive(1);
        playerIsNotAlive(2);

        // when
        deals.tick();

        // then
        verifyNewGameCreated(0);
        verifyNewGameCreated(1, NEVER); // because paused
        verifyNewGameCreated(2);

        // when
        givenActive("room2", true);
        resetAllFields();

        deals.tick();

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
        deals.tick();

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
        deals.tick();

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

        givenActive("room2", false);

        resetAllFields();

        // when
        deals.tick();

        // then
        verifyFieldTicked(0);
        verifyFieldTicked(1, NEVER); // because paused
        verifyFieldTicked(2);

        // when
        givenActive("room2", true);
        resetAllFields();

        deals.tick();

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
        deals.tick();

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
        deals.tick();

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
        deals.tick();

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
        deals.tick();

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
        deals.changeLevel("player", 2);

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
        deals.changeLevel("player", 2);

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
        deals.setLevel("player", new JSONObject("{'levelProgress':{'current':1,'lastPassed':1,'total':2}}"));

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
        deals.setLevel("player", new JSONObject("{'levelProgress':{'current':2,'lastPassed':2,'total':3}}"));

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
        deals.setLevel("player", new JSONObject("{}"));

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
        deals.setLevel("player", null);

        // then
        assertProgress("player", same);
        verifyPlayerEventListenerLevelChanged("player", null);

        int newField = fields.size() - 1;
        assertEquals(3, fields.size());
        verifyNoMoreInteractions(fields.get(newField));
    }

    @Test
    public void testResetAloneUsersField_whenRemove_multiple() {
        // given
        MultiplayerType type = MultiplayerType.MULTIPLE;
        Player player1 = createPlayer("player1", type);
        Player player2 = createPlayer("player2", type);
        Player player3 = createPlayer("player3", type);

        assertRooms("{0=[player1, player2, player3]}");

        // when
        deals.remove(player1.getId(), Sweeper.on().lastAlone());

        // then
        assertEquals(1, fields.size());

        // when
        deals.remove(player2.getId(), Sweeper.on().lastAlone());

        // then
        // still same field for player3, because MULTIPLE is !REMOVE_ALONE
        assertRooms("{0=[player3]}");
        assertEquals(1, fields.size());
    }

    @Test
    public void testDontResetAloneUsersField_whenRemoveCurrent_multiple() {
        // given
        MultiplayerType type = MultiplayerType.MULTIPLE;
        Player player1 = createPlayer("player1", type);
        Player player2 = createPlayer("player2", type);
        Player player3 = createPlayer("player3", type);

        assertRooms("{0=[player1, player2, player3]}");

        // when
        deals.remove(player1.getId(), Sweeper.off());

        // then
        assertEquals(1, fields.size());

        // when
        deals.remove(player2.getId(), Sweeper.off());

        // then
        assertRooms("{0=[player3]}");
        assertEquals(1, fields.size());
    }

    @Test
    public void testResetAloneUsersField_whenRemove_triple() {
        // given
        MultiplayerType type = MultiplayerType.TRIPLE;
        Player player1 = createPlayer("player1", type);
        Player player2 = createPlayer("player2", type);
        Player player3 = createPlayer("player3", type);

        assertRooms("{0=[player1, player2, player3]}");

        // when
        deals.remove(player1.getId(), Sweeper.on().lastAlone());

        // then
        assertEquals(1, fields.size());

        // when
        deals.remove(player2.getId(), Sweeper.on().lastAlone());

        // then
        // new field for player3, because TRIPLE is REMOVE_ALONE
        assertRooms("{1=[player3]}");
        assertEquals(2, fields.size());
    }

    @Test
    public void testDontResetAloneUsersField_whenRemoveCurrent_triple() {
        // given
        MultiplayerType type = MultiplayerType.TRIPLE;
        Player player1 = createPlayer("player1", type);
        Player player2 = createPlayer("player2", type);
        Player player3 = createPlayer("player3", type);

        assertRooms("{0=[player1, player2, player3]}");

        // when
        deals.remove(player1.getId(), Sweeper.off());

        // then
        assertEquals(1, fields.size());

        // when
        deals.remove(player2.getId(), Sweeper.off());

        // then
        // same field because Sweeper.off()
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
        deals.reload(deals.get("player1"), Sweeper.on().lastAlone());

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
        deals.reload(deals.get("player1"), Sweeper.off());

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
        deals.reloadAll(true);

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
        deals.reloadAll(false);

        // then
        assertRooms("{2=[player1, player2, player3], " +
                "3=[player4, player5]}");
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
        deals.reloadAll(false);

        // then
        assertRooms("{2=[player1, player2, player3], " +
                "3=[player4, player5]}");
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
        deals.reloadAll(false, withRoom("room1"));

        // then
        assertRooms("{2=[player5, player6], " + // didn't touch because room2
                "3=[player1, player2, player3], " +
                "4=[player4]}");
        assertEquals(5, fields.size());
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
        deals.reloadAll(false, withType("game1"));

        // then
        assertRooms("{2=[player5, player6], " +  // didn't touch because game2
                "3=[player1, player2, player3], " +
                "4=[player4]}");
        assertEquals(5, fields.size());
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
        verify(fields.get(0), never()).loadSave(any());
    }

    @Test
    public void testLoadFromSave_whenNullSaveInPlayerSave() {
        // given
        String save = null;

        // when
        createPlayerFromSave("player1", save);

        // then
        verify(fields.get(0), never()).loadSave(any());
    }

    @Test
    public void testLoadFromSave_whenEmptyStringSaveInPlayerSave() {
        // given
        String save = "";

        // when
        createPlayerFromSave("player1", save);

        // then
        verify(fields.get(0), never()).loadSave(any());
    }

    @Test
    public void testLoadFromSave_whenNullStringSaveInPlayerSave() {
        // given
        String save = null;

        // when
        createPlayerFromSave("player1", save);

        // then
        verify(fields.get(0), never()).loadSave(any());
    }

    @Test
    public void testLoadFromSave_whenEmptyJsonSaveInPlayerSave() {
        // given
        String save = "{}";

        // when
        createPlayerFromSave("player1", save);

        // then
        verify(fields.get(0), never()).loadSave(any());
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
        Player player = deals.get(playerId).getPlayer();
        if (expected == null) {
            verifyNoMoreInteractions(player.getInfo());
        } else {
            verify(player.getInfo(), times(ONCE)).levelChanged(captor.capture());
            assertEquals(expected, captor.getValue().toString());
        }
        reset(player.getInfo());
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
        assertEquals(expected, deals.get(playerName).getGame().getSave().toString());
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
                deals.get(playerName).getGame().getBoardAsString().toString());
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
                "{\"layers\":[\"board-data\"]," +
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
        deals.tick();

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
        deals.tick();

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
        deals.tick();

        // then
        assertProgress("player1", "{'current':1,'passed':0,'total':1,'valid':true}");
        assertProgress("player2", "{'current':1,'passed':0,'total':1,'valid':true}");
    }

    private void assertProgress(String player, String expected) {
        assertEquals(expected,
                deals.get(player)
                        .getGame().getProgress().toString());
    }

    @Test
    public void whatIfTwoPlayersForDifferentTrainings() {
        // given
        // TODO обрати внимание, тут 3 и 5 - для одной игры, нельзя такого допускать
        createPlayer("player1", MultiplayerType.TRAINING.apply(3));
        createPlayer("player2", MultiplayerType.TRAINING.apply(5));

        // when
        deals.tick();

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
        deals.tick();

        // then
        int newField = fields.size() - 1;
        assertEquals(2, fields.size());
        verify(fields.get(newField), times(ONCE)).newGame(gamePlayers.get(2));

        assertEquals(3, deals.size());
        assertEquals(fields.get(0), deals.get("player1").getField());
        assertEquals(fields.get(0), deals.get("player2").getField());
        assertEquals(fields.get(1), deals.get("player3").getField());

        // when
        // add another player
        createPlayer("player4", MultiplayerType.TRIPLE);
        createPlayer("player5", MultiplayerType.TRIPLE);

        // then
        assertEquals(2, fields.size());
        assertEquals(5, deals.size());

        // это старая борда, там осталось 2 юзера, но она была заполенна в прошлом тремя и места там нет
        assertEquals(fields.get(0), deals.get("player1").getField());
        assertEquals(fields.get(0), deals.get("player2").getField());

        // а это новая и она наполняется новыми юзерами
        assertEquals(fields.get(1), deals.get("player3").getField());
        assertEquals(fields.get(1), deals.get("player4").getField());
        assertEquals(fields.get(1), deals.get("player5").getField());
    }

    @Test
    public void testSetTeamId() {
        // given
        MultiplayerType type = MultiplayerType.MULTIPLE;
        createPlayer("player1", "room", "game", type);
        createPlayer("player2", "room", "game", type);
        createPlayer("player3", "room", "game", type);

        assertTeams("{0=[player1, player2, player3]}");

        // when
        deals.setTeam("player1", 1);
        deals.setTeam("player2", 2);

        // then
        assertTeams("{0=[player3], 1=[player1], 2=[player2]}");

        // when
        deals.setTeam("player1", 2);

        // then
        assertTeams("{0=[player3], 2=[player1, player2]}");

        // when
        deals.setTeam("player3", 2);

        // then
        assertTeams("{2=[player1, player2, player3]}");
    }

    @Test
    public void testChangeRoom_doNothing_whenTryToChangeGame() {
        // given
        MultiplayerType type = MultiplayerType.MULTIPLE;
        createPlayer("player1", "room", "game", type);
        createPlayer("player2", "room", "game", type);
        createPlayer("player3", "room", "game", type);

        assertRooms("{0=[player1, player2, player3]}");

        // when
        String changed = "otherGame";
        deals.changeRoom("player1", changed, "otherRoom");

        // then
        assertRooms("{0=[player1, player2, player3]}");

        assertRoomsNames("{room=[[player1, player2, player3]]}");
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
        deals.changeRoom("player1", "game", "otherRoom");

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
        deals.changeRoom("player1", "game", "otherRoom");
        deals.changeRoom("player3", "game", "otherRoom");

        // then
        assertRooms("{0=[player2], " +   // второй остался в cвоей MULTIPLE потому что она !RELOAD_ALONE
                "1=[player1, player3]}");

        assertRoomsNames("{otherRoom=[[player1, player3]], " +
                "room=[[player2]]}");
    }

    @Test
    public void testChangeRoom_removeMultipleRoomAfterLastPlayer() {
        // given
        MultiplayerType type = MultiplayerType.MULTIPLE;
        createPlayer("player1", "room", "game", type);

        assertRooms("{0=[player1]}");

        // when
        // поки даем текущую комнату room
        deals.changeRoom("player1", "game", "otherRoom");

        // then
        assertRooms("{1=[player1]}");
        assertRoomsNames("{otherRoom=[[player1]]}");

        // when
        // вернулись назад в room
        deals.changeRoom("player1", "game", "room");

        // then
        assertRooms("{2=[player1]}"); // но комната обновилась
        assertRoomsNames("{room=[[player1]]}");

    }

    @Test
    public void testChangeRoom_doNothing_whenNullParameters() {
        // given
        MultiplayerType type = MultiplayerType.MULTIPLE;
        createPlayer("player1", "room", "game", type);
        createPlayer("player2", "room", "game", type);
        createPlayer("player3", "room", "game", type);

        assertRooms("{0=[player1, player2, player3]}");

        // when
        deals.changeRoom("player1", "game", null);
        deals.changeRoom("player1", "game", "");
        deals.changeRoom("player1", null, "otherRoom");
        deals.changeRoom("player1", "", "otherRoom");

        // then
        assertRooms("{0=[player1, player2, player3]}");

        assertRoomsNames("{room=[[player1, player2, player3]]}");
    }

    @Test
    public void testChangeRoom_keepSave() {
        // given
        MultiplayerType type = MultiplayerType.MULTIPLE;

        when(fieldSaves.getSave())
                .thenReturn(new JSONObject("{\"fiedData\":1}"),
                        new JSONObject("{\"fiedData\":2}"),
                        new JSONObject("{\"fiedData\":3}"),
                        new JSONObject("{\"fiedData\":4}"));

        createPlayer("player1", "room", "game", type, new PlayerSave("{\"some\":\"data1\"}"));
        createPlayer("player2", "room", "game", type, new PlayerSave("{\"some\":\"data2\"}"));

        // вначале два плеера в одной комнате
        assertRooms("{0=[player1, player2]}");

        assertSave("player1", "{\"fiedData\":1}");
        assertSave("player2", "{\"fiedData\":1}");

        // when
        // потом один покидает комнату и переходит в другую
        deals.changeRoom("player1", "game", "otherRoom");

        // then
        // второй игрок остается в своей старой MULTIPLE комнате, потому что она !REMOVE_ALONE
        // а тот, кто самовольно перешел так же размещается в своей новой комнате
        assertRooms("{0=[player2], 1=[player1]}");

        assertSave("player1", "{\"fiedData\":2}");
        assertSave("player2", "{\"fiedData\":1}");
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
        deals.changeRoom("player1", "game", "otherRoom");
        deals.changeRoom("player2", "game", "otherRoom");

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
        deals.changeRoom("player1", "game", "otherRoom");
        deals.changeRoom("player2", "game", "otherRoom");

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
        deals.changeRoom("player1", "game", "otherRoom");
        deals.changeRoom("player2", "game", "otherRoom");

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
        deals.changeRoom("player1", "game", "otherRoom");
        deals.changeRoom("player2", "game", "otherRoom");

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
        deals.changeRoom("player1", "game", "otherRoom");
        deals.changeRoom("player2", "game", "otherRoom");

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
        deals.changeRoom("player1", "game", "otherRoom");

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
        deals.changeRoom("player1", "game", "otherRoom");

        // then
        // player1 покинул комнату и попал в отдельный мир otherRoom
        // player2 при этом остался однин в своей комнате
        assertRooms("{0=[player2], " +
                "1=[player3, player4], " +
                "2=[player5], 3=[player1]}");

        assertRoomsNames("{otherRoom[1]=[[player1]], " +
                "room[1]=[[player2], [player3, player4], [player5]]}");
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
        deals.setLevel("player1", new JSONObject("{'levelProgress':" +
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
        deals.changeLevel("player1", 1);

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
        deals.setLevel("player1", new JSONObject("{'levelProgress':" +
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
        deals.changeLevel("player1", 1);

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
        deals.setLevel("player6", new JSONObject("{'levelProgress':" +
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
        deals.changeLevel("player6", 1);

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
        deals.setLevel("player6", new JSONObject("{'levelProgress':" +
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
        deals.changeLevel("player6", 1);

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
        deals.setLevel("player1", new JSONObject("{'levelProgress':" +
                "{'current':2,'lastPassed':1,'total':3}}"));

        // then
        assertRooms("{0=[player2, player3], " +
                "1=[player4, player5], 2=[player1]}");

        assertRoomsNames("{room[1]=[[player2, player3], [player4, player5]], " +
                "room[2]=[[player1]]}");

        // when
        // player1 возвращается обратно
        // и так как уровень DISPOSABLE то игрок попадает в новую комнату к player4 и player5
        deals.changeLevel("player1", 1);

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
        deals.setLevel("player1", new JSONObject("{'levelProgress':" +
                "{'current':2,'lastPassed':1,'total':3}}"));

        // then
        assertRooms("{0=[player2, player3], " +
                "1=[player4, player5], 2=[player1]}");

        assertRoomsNames("{room[1]=[[player2, player3], [player4, player5]], " +
                "room[2]=[[player1]]}");

        // when
        // player1 возвращается обратно
        // и так как уровень DISPOSABLE то игрок попадает в новую комнату к player4 и player5
        deals.changeLevel("player1", 1);

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
        deals.setLevel("player6", new JSONObject("{'levelProgress':" +
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
        deals.changeLevel("player6", 1);

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
        deals.setLevel("player6", new JSONObject("{'levelProgress':" +
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
        deals.changeLevel("player6", 1);

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
        createPlayer("player1", "room1", "game1", type);
        createPlayer("player2", "room1", "game1", type);
        createPlayer("player3", "room2", "game2", type);
        createPlayer("player4", "room3", "game3", type);

        // when then
        assertPlayers("[player1, player2]", deals.getAll(withType("game1")));
        assertPlayers("[player3]", deals.getAll(withType("game2")));
        assertPlayers("[player4]", deals.getAll(withType("game3")));
    }

    @Test
    public void testGetAll_withAll() {
        // given
        MultiplayerType type = MultiplayerType.SINGLE;
        createPlayer("player1", "room1", "game1", type);
        createPlayer("player2", "room1", "game1", type);
        createPlayer("player3", "room2", "game2", type);
        createPlayer("player4", "room3", "game3", type);

        // when then
        assertPlayers("[player1, player2, player3, player4]", deals.getAll(withAll()));
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
        assertPlayers("[player1, player2]", deals.getAll(withRoom("room1")));
        assertPlayers("[player3, player4]", deals.getAll(withRoom("room2")));
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

        givenActive("room2", false);

        // when then
        assertPlayers("[player1, player2, player5]", deals.getAll(deals.withActive()));
        assertPlayers("[player1, player2, player5]", deals.active());
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

        givenActive("room2", false);

        // when then
        assertEquals("[room1, room3]", deals.getRooms(ACTIVE).toString());
        assertEquals("[room1, room2, room3]", deals.getRooms(ALL).toString());
    }

    @Test
    public void testAdd_gamePlayerGetTeamIdFromSave() {
        // given
        Player player = createPlayer();
        deals.remove(player.getId(), Sweeper.off());

        int teamId = 3;
        PlayerSave playerSave = new PlayerSave("player", teamId, "url", "game", "room", 0, "{}");

        // when
        deals.add(player, "room", playerSave);

        // then
        assertEquals(1, deals.all().size());
        assertEquals(teamId, deals.all().iterator().next().getTeamId());
    }

    @Test
    public void testAdd_gamePlayerDefaultTeamIdBecauseNullSave() {
        // given
        Player player = createPlayer();
        deals.remove(player.getId(), Sweeper.off());

        // when
        deals.add(player, "room", PlayerSave.NULL);

        // then
        assertEquals(1, deals.all().size());
        assertEquals(DEFAULT_TEAM_ID, deals.all().iterator().next().getTeamId());
    }

    @Test
    public void testAdd_gamePlayerDefaultTeamIdBecauseNoSave() {
        // given
        Player player = createPlayer();
        deals.remove(player.getId(), Sweeper.off());

        // when
        deals.add(player, "room", null);

        // then
        assertEquals(1, deals.all().size());
        assertEquals(DEFAULT_TEAM_ID, deals.all().iterator().next().getTeamId());
    }
}