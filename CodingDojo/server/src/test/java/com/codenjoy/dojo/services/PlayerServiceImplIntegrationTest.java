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

import com.codenjoy.dojo.client.*;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.controller.Controller;
import com.codenjoy.dojo.services.dao.ActionLogger;
import com.codenjoy.dojo.services.dao.Chat;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.mocks.AISolverStub;
import com.codenjoy.dojo.services.mocks.BoardStub;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class PlayerServiceImplIntegrationTest {

    private static final String FIRST_PLAYER_ID = "player1";
    private static final String SECOND_PLAYER_ID = "player2";
    private static final String THIRD_PLAYER_ID = "player3";
    private static final String FOURTH_PLAYER_ID = "player4";
    private static final String FIFTH_PLAYER_ID = "player5";
    private static final String SIXTH_PLAYER_ID = "player6";
    private static final String SEVENTH_PLAYER_ID = "player7";

    private static final String FIRST_GAME = "game1";
    private static final String SECOND_GAME = "game2";
    private static final String THIRD_GAME = "game3";
    private static final String FOURTH_GAME = "game4";

    private static final String FIRST_ROOM = "room1";
    private static final String SECOND_ROOM = "room2";
    private static final String THIRD_ROOM = "room3";
    private static final String FOURTH_ROOM = "room4";

    private static final String FIRST_IP = "callback1";
    private static final String SECOND_IP = "callback2";
    private static final String THIRD_IP = "callback3";
    private static final String FOURTH_IP = "callback4";
    private static final String FIFTH_IP = "callback5";
    private static final String SIXTH_IP = "callback6";
    private static final String SEVENTH_IP = "callback7";

    private static final String REPOSITORY = "repository";
    private static final String SLACK_EMAIL = "slackEmail";

    private PlayerService service;

    private Semifinal semifinal;
    private ConfigProperties config;
    private ActionLogger actionLogger;
    private AutoSaver autoSaver;
    private Chat chat;
    private GameSaver saver;
    private GameService gameService;
    private Controller screenController;
    private Controller playerController;
    private PlayerGames playerGames;
    private Registration registration;
    private Map<String, GameType> gameTypes = new HashMap<>();
    private Map<String, WebSocketRunner> runners = new HashMap<>();

    @Before
    public void setup() {
        service = new PlayerServiceImpl() {
            {
                PlayerServiceImplIntegrationTest.this.playerGames
                        = this.playerGames = new PlayerGames();

                PlayerServiceImplIntegrationTest.this.playerController
                        = this.playerController = mock(Controller.class);

                PlayerServiceImplIntegrationTest.this.screenController
                        = this.screenController = mock(Controller.class);

                PlayerServiceImplIntegrationTest.this.gameService
                        = this.gameService = mock(GameService.class);

                PlayerServiceImplIntegrationTest.this.autoSaver
                        = this.autoSaver = mock(AutoSaver.class);

                PlayerServiceImplIntegrationTest.this.saver
                        = this.saver = mock(GameSaver.class);

                PlayerServiceImplIntegrationTest.this.chat
                        = this.chat = mock(Chat.class);

                PlayerServiceImplIntegrationTest.this.actionLogger
                        = this.actionLogger = mock(ActionLogger.class);

                PlayerServiceImplIntegrationTest.this.registration
                        = this.registration = mock(Registration.class);

                PlayerServiceImplIntegrationTest.this.config
                        = this.config = new ConfigProperties();
                config.setRegistrationOpened(true);

                PlayerServiceImplIntegrationTest.this.semifinal
                        = this.semifinal = mock(Semifinal.class);

                this.isAiNeeded = true;
            }

            @Override
            protected WebSocketRunner runAI(String id, String code, Solver solver, ClientBoard board) {
                WebSocketRunner runner = mock(WebSocketRunner.class);
                doAnswer(inv -> {
                    return null; // for debug
                }).when(runner).close();
                runners.put(id, runner);
                return runner;
            }

        };
    }

    @Test
    public void test() {
        int ai1 = 0;
        int ai2 = 0;
        int ai3 = 0;
        when(gameService.getGameType(anyString())).thenAnswer(
                inv -> getOrCreateGameType(inv.getArgument(0))
        );
        when(gameService.getGameType(anyString(), anyString())).thenAnswer(
                inv -> getOrCreateGameType(inv.getArgument(0))
        );
        when(gameService.exists(anyString())).thenReturn(true);

        when(chat.getLastMessageIds()).thenReturn(new HashMap<>());

        // первый плеер зарегался (у него сейвов нет)
        when(saver.loadGame(anyString())).thenReturn(PlayerSave.NULL);
        Player player1 = service.register(FIRST_PLAYER_ID, FIRST_GAME, FIRST_ROOM, FIRST_IP, REPOSITORY, SLACK_EMAIL);
        String expected = "[game1-super-ai, player1]";
        assertEquals(expected, service.getAll().toString());
        assertEquals(expected, service.getAllInRoom(FIRST_ROOM).toString());
        assertEquals(true, runners.containsKey("game1-super-ai"));

        // потом еще двое подоспели на ту же игру
        Player player2 = service.register(SECOND_PLAYER_ID, FIRST_GAME, FIRST_ROOM, SECOND_IP, REPOSITORY, SLACK_EMAIL);
        Player player3 = service.register(THIRD_PLAYER_ID, FIRST_GAME, FIRST_ROOM, SECOND_IP, REPOSITORY, SLACK_EMAIL);
        verify(gameTypes.get(FIRST_GAME), times(++ai1)).getAI();
        verify(gameTypes.get(FIRST_GAME), times(ai1)).getBoard();
        expected = "[game1-super-ai, player1, player2, player3]";
        assertEquals(expected, service.getAll().toString());
        assertEquals(expected, service.getAllInRoom(FIRST_ROOM).toString());

        // второй вышел
        service.remove(SECOND_PLAYER_ID);
        expected = "[game1-super-ai, player1, player3]";
        assertEquals(expected, service.getAll().toString());
        assertEquals(expected, service.getAllInRoom(FIRST_ROOM).toString());

        // и третий тоже
        service.remove(THIRD_PLAYER_ID);
        expected = "[game1-super-ai, player1]";
        assertEquals(expected, service.getAll().toString());
        assertEquals(expected, service.getAllInRoom(FIRST_ROOM).toString());

        // смотрим есть ли пользователи
        assertEquals(true, service.contains(FIRST_PLAYER_ID));
        assertEquals(false, service.contains(SECOND_PLAYER_ID));

        // потом зарегались на другую игру
        Player player4 = service.register(FOURTH_PLAYER_ID, SECOND_GAME, SECOND_ROOM, FOURTH_IP, REPOSITORY, SLACK_EMAIL);
        verify(gameTypes.get(SECOND_GAME), times(++ai2)).getAI();
        verify(gameTypes.get(SECOND_GAME), times(ai2)).getBoard();
        Player player5 = service.register(FIFTH_PLAYER_ID, SECOND_GAME, SECOND_ROOM, FIFTH_IP, REPOSITORY, SLACK_EMAIL);
        Player player6 = service.register(SIXTH_PLAYER_ID, FIRST_GAME, FIRST_ROOM, SIXTH_IP, REPOSITORY, SLACK_EMAIL);
        assertEquals("[game1-super-ai, player1, game2-super-ai, player4, player5, player6]",
                service.getAll().toString());
        expected = "[game1-super-ai, player1, player6]";
        assertEquals(expected, service.getAll(FIRST_GAME).toString());
        assertEquals(expected, service.getAllInRoom(FIRST_ROOM).toString());
        expected = "[game2-super-ai, player4, player5]";
        assertEquals(expected, service.getAll(SECOND_GAME).toString());
        assertEquals(expected, service.getAllInRoom(SECOND_ROOM).toString());

        // при этом у нас теперь два AI
        assertEquals(true, service.contains("game1-super-ai"));
        assertEquals(true, service.contains("game2-super-ai"));
        assertEquals(true, runners.containsKey("game1-super-ai"));
        assertEquals(true, runners.containsKey("game2-super-ai"));

        // взяли игру с плеерами
        assertEquals(FIRST_GAME, service.getAnyGameWithPlayers().name());

        // и рендомных прееров
        assertEquals("game1-super-ai", service.getRandom(FIRST_GAME).toString());
        assertEquals("game2-super-ai", service.getRandom(SECOND_GAME).toString());

        // несложно понять что берется просто первый в очереди
        verifyNoMoreInteractions(runners.get("game2-super-ai"));
        service.remove("game2-super-ai");
        verify(runners.get("game2-super-ai"), times(1)).close();
        assertEquals(FOURTH_PLAYER_ID,
                service.getRandom(SECOND_GAME).toString());
        runners.remove("game2-super-ai");

        // закрыли регистрацию
        assertEquals(true, service.isRegistrationOpened());
        service.closeRegistration();
        assertEquals(false, service.isRegistrationOpened());
        Player player7 = service.register(SEVENTH_PLAYER_ID, THIRD_GAME, THIRD_ROOM, SEVENTH_IP, REPOSITORY, SLACK_EMAIL);
        assertEquals(false, service.contains(SEVENTH_PLAYER_ID));
        assertEquals("[]", service.getAll(THIRD_GAME).toString());
        assertEquals("[]", service.getAllInRoom(THIRD_ROOM).toString());

        // открыли регистрацию
        service.openRegistration();
        assertEquals(true, service.isRegistrationOpened());
        player7 = service.register(SEVENTH_PLAYER_ID, THIRD_GAME, THIRD_ROOM, SEVENTH_IP, REPOSITORY, SLACK_EMAIL);
        verify(gameTypes.get(THIRD_GAME), times(++ai3)).getAI();
        verify(gameTypes.get(THIRD_GAME), times(ai3)).getBoard();
        assertEquals(true, service.contains(SEVENTH_PLAYER_ID));
        expected = "[game3-super-ai, player7]";
        assertEquals(expected, service.getAll(THIRD_GAME).toString());
        assertEquals(expected, service.getAllInRoom(THIRD_ROOM).toString());
        assertEquals(true, runners.containsKey("game1-super-ai"));
        assertEquals(false, runners.containsKey("game2-super-ai"));
        assertEquals(true, runners.containsKey("game3-super-ai"));

        // загрузили AI вместо плеера
        service.reloadAI(SEVENTH_PLAYER_ID);
        verify(gameTypes.get(THIRD_GAME), times(++ai3)).getAI();
        verify(gameTypes.get(THIRD_GAME), times(ai3)).getBoard();
        assertEquals("[game3-super-ai, player7]",
                service.getAll("game3").toString());
        expected = "[game3-super-ai, player7]";
        assertEquals(expected, service.getAll(THIRD_GAME).toString());
        assertEquals(expected, service.getAllInRoom(THIRD_ROOM).toString());
        assertEquals(true, runners.containsKey("game1-super-ai"));
        assertEquals(false, runners.containsKey("game2-super-ai"));
        assertEquals(true, runners.containsKey("game3-super-ai"));
        assertEquals(true, runners.containsKey(SEVENTH_PLAYER_ID));

        // обновили описание ребят
        List<PlayerInfo> infos = service.getAll().stream().map(player -> new PlayerInfo(player.getId() + "_updated",
                player.getCode(), player.getCallbackUrl(), player.getGame())).collect(toList());
        service.updateAll(infos);
        assertEquals("[game1-super-ai_updated, " +
                "player1_updated, player4_updated, player5_updated, " +
                "player6_updated, game3-super-ai_updated, " +
                "player7_updated]", service.getAll().toString());

        // зарегали существующего пользователя в другую игру
        expected = "[game1-super-ai_updated, player1_updated, player6_updated]";
        assertEquals(expected, service.getAll(FIRST_GAME).toString());
        assertEquals(expected, service.getAllInRoom(FIRST_ROOM).toString());
        expected = "[player4_updated, player5_updated]";
        assertEquals(expected, service.getAll(SECOND_GAME).toString());
        assertEquals(expected, service.getAllInRoom(SECOND_ROOM).toString());
        player1 = service.register("player1_updated", SECOND_GAME, SECOND_ROOM, FIRST_IP, REPOSITORY, SLACK_EMAIL);
        expected = "[game1-super-ai_updated, player6_updated]";
        assertEquals(expected, service.getAll(FIRST_GAME).toString());
        assertEquals(expected, service.getAllInRoom(FIRST_ROOM).toString());
        expected = "[player4_updated, player5_updated, game2-super-ai, player1_updated]";
        // TODO какого фига сюда AI ломится? Там же есть ребята уже
        assertEquals(expected, service.getAll(SECOND_GAME).toString());
        assertEquals(expected, service.getAllInRoom(SECOND_ROOM).toString());
        expected = "[game3-super-ai_updated, player7_updated]";
        assertEquals(expected, service.getAll(THIRD_GAME).toString());
        assertEquals(expected, service.getAllInRoom(THIRD_ROOM).toString());

        // пользователь перешел в другую комнату той же игры
        player1 = service.register("player1_updated", SECOND_GAME, FOURTH_ROOM, FIRST_IP, REPOSITORY, SLACK_EMAIL);
        expected = "[game1-super-ai_updated, player6_updated]";
        assertEquals(expected, service.getAll(FIRST_GAME).toString());
        assertEquals(expected, service.getAllInRoom(FIRST_ROOM).toString());
        assertEquals("[player4_updated, player5_updated, game2-super-ai, player1_updated]",
                service.getAll(SECOND_GAME).toString()); // тут отличие game2 != room2
        assertEquals("[player4_updated, player5_updated, game2-super-ai]",
                service.getAllInRoom(SECOND_ROOM).toString()); // тут отличие game2 != room2
        expected = "[game3-super-ai_updated, player7_updated]";
        assertEquals(expected, service.getAll(THIRD_GAME).toString());
        assertEquals(expected, service.getAllInRoom(THIRD_ROOM).toString());
        assertEquals("[]", service.getAll(FOURTH_GAME).toString()); // нет такой игры
        assertEquals("[player1_updated]", service.getAllInRoom(FOURTH_ROOM).toString());


        // удалили всех нафиг
        service.removeAll();
        assertEquals("[]", service.getAll(FIRST_GAME).toString());
        assertEquals("[]", service.getAll(SECOND_GAME).toString());
        assertEquals("[]", service.getAll(THIRD_GAME).toString());
        verify(runners.get("game1-super-ai"), times(1)).close();
        verify(runners.get("game3-super-ai"), times(1)).close();
        verify(runners.get(SEVENTH_PLAYER_ID), times(1)).close();
        runners.clear();

        // грузим плеера из сейва
        player1 = service.register(new PlayerSave(FIRST_PLAYER_ID, FIFTH_IP, FIRST_GAME, FIRST_ROOM, 120, "{save:true}", REPOSITORY));
        assertEquals("[player1]", service.getAll(FIRST_GAME).toString());
        assertEquals(0, runners.size());

        // а теперь AI из сейва
        verify(gameTypes.get(FIRST_GAME), times(ai1)).getAI();
        verify(gameTypes.get(FIRST_GAME), times(ai1)).getBoard();
        player1 = service.register(new PlayerSave("bot-super-ai", "callback", FIRST_GAME, FIRST_ROOM, 120, "{save:true}", REPOSITORY));
        assertEquals("[player1, bot-super-ai]", service.getAll(FIRST_GAME).toString());
        verify(gameTypes.get(FIRST_GAME), times(++ai1)).getAI();
        verify(gameTypes.get(FIRST_GAME), times(ai1)).getBoard();
        assertEquals(true, runners.containsKey("bot-super-ai"));
    }

    private GameType getOrCreateGameType(String name) {
        if (gameTypes.containsKey(name)) {
            return gameTypes.get(name);
        }

        GameType gameType = mock(GameType.class);
        when(gameType.getMultiplayerType(any())).thenReturn(MultiplayerType.SINGLE);
        when(gameType.createGame(anyInt(), any())).thenAnswer(inv -> {
            GameField field = mock(GameField.class);
            when(field.reader()).thenAnswer(inv2 -> mock(BoardReader.class));
            return field;
        });
        when(gameType.createPlayer(any(EventListener.class), anyString(), any()))
                .thenAnswer(inv -> mock(GamePlayer.class));
        when(gameType.getPrinterFactory()).thenReturn(mock(PrinterFactory.class));
        when(gameType.getAI()).thenReturn(AISolverStub.class);
        when(gameType.getBoard()).thenReturn(BoardStub.class);
        when(gameType.name()).thenReturn(name);

        gameTypes.put(name, gameType);

        return gameType;
    }
}
