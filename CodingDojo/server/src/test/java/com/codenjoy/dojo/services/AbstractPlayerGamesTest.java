package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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
import com.codenjoy.dojo.services.multiplayer.GameRoom;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.room.RoomService;
import com.codenjoy.dojo.services.settings.Settings;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;

import java.util.*;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AbstractPlayerGamesTest {

    protected PlayerGames playerGames;
    protected List<GameType> gameTypes = new LinkedList<>();
    protected Map<Player, Closeable> ais = new HashMap<>();
    protected List<Joystick> joysticks = new LinkedList<>();
    protected List<Player> players = new LinkedList<>();
    protected List<Joystick> lazyJoysticks = new LinkedList<>();
    protected List<GamePlayer> gamePlayers = new LinkedList<>();
    protected List<GameField> fields = new LinkedList<>();
    protected RoomService roomService;
    protected GameField fieldSaves = mock(GameField.class);

    @Before
    public void setUp() {
        playerGames = new PlayerGames();
        roomService = playerGames.roomService = new RoomService();
        when(fieldSaves.getSave()).thenReturn(null);
    }

    protected Player createPlayer() {
        return createPlayer("room", "game");
    }

    protected Player createPlayer(String room, String game) {
        return createPlayer("player" + (players.size() + 1),
                room, game,
                MultiplayerType.SINGLE);
    }

    protected Player createPlayer(MultiplayerType type) {
        return createPlayer("player", "room", "game", type);
    }

    protected Player createPlayer(String name, MultiplayerType type) {
        return createPlayer(name, "room", "game", type);
    }

    protected Player createPlayer(String name, String room, String game, MultiplayerType type) {
        return createPlayer(name, room, game, type, null);
    }

    protected Player createPlayer(String name, MultiplayerType type, PlayerSave save) {
        return createPlayer(name, "room", "game", type, save);
    }

    protected Player createPlayer(String name, String room, String game, MultiplayerType type, PlayerSave save) {
        // TODO распутать клубок, тут для одинаковых room должны быть и game тоже одинаковые, иначе идея может быть нарушена тестами
        return createPlayer(name, game, room, type, save, "board");
    }

    protected void verifyRemove(PlayerGame playerGame, GameField field) {
        verify(field).remove(playerGame.getGame().getPlayer());
        verify(ais.get(playerGame.getPlayer())).close();
    }

    protected Player createPlayerWithScore(int score, String playerName, MultiplayerType type) {
        return createPlayerWithScore(score, playerName, "room", type);
    }

    protected Player createPlayerWithScore(int score, String playerName, String room, MultiplayerType type) {
        Player player = createPlayer(playerName, room, "game " + room, type);
        setScore(score, player);
        return player;
    }

    private void setScore(int score, Player player) {
        when(player.getScores().getScore()).thenReturn(score);
    }

    protected Player createPlayerWithScore(int score) {
        Player player = createPlayer();
        setScore(score, player);
        return player;
    }

    protected void givenActive(String room, boolean active) {
        roomService.setActive(room, active);
    }

    protected void givenRegistrationOpened(String room, boolean opened) {
        roomService.setOpened(room, opened);
    }

    protected Player createPlayerWithScore(int score, String room) {
        Player player = createPlayer(room, "game");
        setScore(score, player);
        return player;
    }

    protected void setPlayerStatus(int index, boolean stillPlay) {
        when(gamePlayers.get(index).isAlive()).thenReturn(stillPlay);
        when(gamePlayers.get(index).isWin()).thenReturn(!stillPlay);
        when(gamePlayers.get(index).shouldLeave()).thenReturn(!stillPlay);
    }

    protected Player createPlayer(String id, String game,
                                  String room, MultiplayerType type,
                                  PlayerSave save, Object board)
    {
        GameType gameType = getGameType(game, room);
        Player player = new Player(id, "url", gameType, gameType.getPlayerScores(0, null), mock(Information.class));
        players.add(player);
        player.setEventListener(mock(InformationCollector.class));
        Closeable ai = mock(Closeable.class);
        ais.put(player, ai);
        player.setAi(ai);

        playerGames.onAdd(pg -> lazyJoysticks.add(pg.getJoystick()));

        TestUtils.Env env =
                TestUtils.getPlayerGame(
                        playerGames,
                        player,
                        room,
                        inv -> {
                            GameField field = mock(GameField.class);
                            when(field.reader()).thenReturn(mock(BoardReader.class));
                            JSONObject value = fieldSaves.getSave();
                            when(field.getSave()).thenReturn(value);
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

    // так как у нас есть комнаты, то для одной и той же room мы берем tameType из кеша roomService
    private GameType getGameType(String game, String room) {
        if (roomService.exists(room)) {
            return roomService.gameType(room);
        } else {
            GameType basicType = createGameType(game, room);
            return roomService.create(room, basicType);
        }
    }

    // при каждой новой room должен дергаться этот метод
    private GameType createGameType(String game, String room) {
        GameType gameType = mock(GameType.class);
        doReturn(settings(room)).when(gameType).getSettings();
        when(gameType.getPlayerScores(anyInt(), any())).thenAnswer(inv -> mock(PlayerScores.class));
        when(gameType.name()).thenReturn(game);
        GameService gameService = mock(GameService.class);
        when(gameService.getGameType(anyString())).thenReturn(gameType);
        when(gameService.getGameType(anyString(), anyString())).thenReturn(gameType);
        when(gameService.exists(anyString())).thenReturn(true);
        gameTypes.add(gameType);
        return gameType;
    }

    protected Settings settings(String room) {
        return mock(Settings.class);
    }

    private JSONObject getSaveJson(PlayerSave save) {
        if (save == null || save.getSave() == null) {
            return null;
        }

        try {
            return new JSONObject(save.getSave());
        } catch (JSONException e) {
            return null;
        }
    }

    public void assertRooms(String expected) {
        assertEquals(expected, getRooms().toString());
    }

    public Map<Integer, Collection<String>> getRooms() {
        Multimap<Integer, String> result = TreeMultimap.create();

        for (PlayerGame playerGame : playerGames) {
            int index = fields.indexOf(playerGame.getField());
            String name = playerGame.getPlayer().getId();

            result.get(index).add(name);
        }
        return result.asMap();
    }

    public void assertRoomsNames(String expected) {
        assertEquals(expected, getRoomNames().toString());
    }

    public Map<String, Collection<List<String>>> getRoomNames() {
        Multimap<String, List<String>> result = HashMultimap.create();

        playerGames.rooms().forEach(
                (key, value) -> result.get(key).add(players(value)));

        return result.asMap();
    }

    private List<String> players(GameRoom room) {
        return room.players().stream()
                .map(this::name)
                .collect(toList());
    }

    private String name(GamePlayer player) {
        return playerGames.get(gamePlayers.indexOf(player)).getPlayer().getId();
    }

}
