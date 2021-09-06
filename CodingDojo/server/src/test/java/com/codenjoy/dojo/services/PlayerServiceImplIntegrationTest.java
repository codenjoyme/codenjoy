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
import com.codenjoy.dojo.services.room.RoomService;
import com.codenjoy.dojo.services.semifinal.SemifinalService;
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

    private PlayerService service;

    private SemifinalService semifinal;
    private ConfigProperties config;
    private ActionLogger actionLogger;
    private AutoSaver autoSaver;
    private TimeService time;
    private Chat chat;
    private GameSaver saver;
    private GameService gameService;
    private Controller screenController;
    private RoomService roomService;
    private Controller playerController;
    private Deals deals;
    private Registration registration;
    private Map<String, GameType> gameTypes = new HashMap<>();
    private Map<String, WebSocketRunner> runners = new HashMap<>();

    @Before
    public void setup() {
        service = new PlayerServiceImpl() {
            {
                PlayerServiceImplIntegrationTest.this.deals
                        = this.deals = new Deals();

                PlayerServiceImplIntegrationTest.this.playerController
                        = this.playerController = mock(Controller.class);

                PlayerServiceImplIntegrationTest.this.screenController
                        = this.screenController = mock(Controller.class);

                PlayerServiceImplIntegrationTest.this.gameService
                        = this.gameService = mock(GameService.class);

                PlayerServiceImplIntegrationTest.this.roomService
                        = this.roomService = mock(RoomService.class);

                PlayerServiceImplIntegrationTest.this.autoSaver
                        = this.autoSaver = mock(AutoSaver.class);

                PlayerServiceImplIntegrationTest.this.saver
                        = this.saver = mock(GameSaver.class);

                PlayerServiceImplIntegrationTest.this.chat
                        = this.chat = mock(Chat.class);

                PlayerServiceImplIntegrationTest.this.time
                        = this.time = mock(TimeService.class);

                PlayerServiceImplIntegrationTest.this.actionLogger
                        = this.actionLogger = mock(ActionLogger.class);

                PlayerServiceImplIntegrationTest.this.registration
                        = this.registration = mock(Registration.class);

                PlayerServiceImplIntegrationTest.this.config
                        = this.config = new ConfigProperties();
                config.setRegistrationOpened(true);

                PlayerServiceImplIntegrationTest.this.semifinal
                        = this.semifinal = mock(SemifinalService.class);

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

        // по умолчанию все команаты будут активными и открытыми для регистрации
        when(gameService.exists(anyString())).thenReturn(true);
        when(roomService.isOpened(anyString())).thenReturn(true);

        when(chat.getLastMessageIds()).thenReturn(new HashMap<>());
        when(time.now()).thenReturn(123L);

        // первый плеер зарегался (у него сейвов нет)
        when(saver.loadGame(anyString())).thenReturn(PlayerSave.NULL);
        Player player1 = service.register("player1", "game1", "room1", "callback1");
        String expected = "[game1-super-ai, player1]";
        assertEquals(expected, service.getAll().toString());
        assertEquals(expected, service.getAllInRoom("room1").toString());
        assertEquals(true, runners.containsKey("game1-super-ai"));

        // потом еще двое подоспели на ту же игру
        Player player2 = service.register("player2", "game1", "room1", "callback2");
        Player player3 = service.register("player3", "game1", "room1", "callback2");
        verify(gameTypes.get("game1"), times(++ai1)).getAI();
        verify(gameTypes.get("game1"), times(ai1)).getBoard();
        expected = "[game1-super-ai, player1, player2, player3]";
        assertEquals(expected, service.getAll().toString());
        assertEquals(expected, service.getAllInRoom("room1").toString());

        // второй вышел
        service.remove("player2");
        expected = "[game1-super-ai, player1, player3]";
        assertEquals(expected, service.getAll().toString());
        assertEquals(expected, service.getAllInRoom("room1").toString());

        // и третий тоже
        service.remove("player3");
        expected = "[game1-super-ai, player1]";
        assertEquals(expected, service.getAll().toString());
        assertEquals(expected, service.getAllInRoom("room1").toString());

        // смотрим есть ли пользователи
        assertEquals(true, service.contains("player1"));
        assertEquals(false, service.contains("player2"));

        // потом зарегались на другую игру
        Player player4 = service.register("player4", "game2", "room2", "callback4");
        verify(gameTypes.get("game2"), times(++ai2)).getAI();
        verify(gameTypes.get("game2"), times(ai2)).getBoard();
        Player player5 = service.register("player5", "game2", "room2", "callback5");
        Player player6 = service.register("player6", "game1", "room1", "callback6");
        assertEquals("[game1-super-ai, player1, game2-super-ai, player4, player5, player6]",
                service.getAll().toString());
        expected = "[game1-super-ai, player1, player6]";
        assertEquals(expected, service.getAll("game1").toString());
        assertEquals(expected, service.getAllInRoom("room1").toString());
        expected = "[game2-super-ai, player4, player5]";
        assertEquals(expected, service.getAll("game2").toString());
        assertEquals(expected, service.getAllInRoom("room2").toString());

        // при этом у нас теперь два AI
        assertEquals(true, service.contains("game1-super-ai"));
        assertEquals(true, service.contains("game2-super-ai"));
        assertEquals(true, runners.containsKey("game1-super-ai"));
        assertEquals(true, runners.containsKey("game2-super-ai"));

        // взяли игру с плеерами
        assertEquals("room1", service.getAnyRoomWithPlayers());

        // и рендомных прееров
        assertEquals("game1-super-ai", service.getRandomInRoom("room1").toString());
        assertEquals("game2-super-ai", service.getRandomInRoom("room2").toString());

        // несложно понять что берется просто первый в очереди
        verifyNoMoreInteractions(runners.get("game2-super-ai"));
        service.remove("game2-super-ai");
        verify(runners.get("game2-super-ai"), times(1)).close();
        assertEquals("player4",
                service.getRandomInRoom("room2").toString());
        runners.remove("game2-super-ai");

        // закрыли регистрацию
        assertEquals(true, service.isRegistrationOpened());
        service.closeRegistration();
        assertEquals(false, service.isRegistrationOpened());
        Player player7 = service.register("player7", "game3", "room3", "callback7");
        assertEquals(false, service.contains("player7"));
        assertEquals("[]", service.getAll("game3").toString());
        assertEquals("[]", service.getAllInRoom("room3").toString());

        // открыли регистрацию
        service.openRegistration();
        assertEquals(true, service.isRegistrationOpened());
        player7 = service.register("player7", "game3", "room3", "callback7");
        verify(gameTypes.get("game3"), times(++ai3)).getAI();
        verify(gameTypes.get("game3"), times(ai3)).getBoard();
        assertEquals(true, service.contains("player7"));
        expected = "[game3-super-ai, player7]";
        assertEquals(expected, service.getAll("game3").toString());
        assertEquals(expected, service.getAllInRoom("room3").toString());
        assertEquals(true, runners.containsKey("game1-super-ai"));
        assertEquals(false, runners.containsKey("game2-super-ai"));
        assertEquals(true, runners.containsKey("game3-super-ai"));

        // загрузили AI вместо плеера
        service.reloadAI("player7");
        verify(gameTypes.get("game3"), times(++ai3)).getAI();
        verify(gameTypes.get("game3"), times(ai3)).getBoard();
        assertEquals("[game3-super-ai, player7]",
                service.getAll("game3").toString());
        expected = "[game3-super-ai, player7]";
        assertEquals(expected, service.getAll("game3").toString());
        assertEquals(expected, service.getAllInRoom("room3").toString());
        assertEquals(true, runners.containsKey("game1-super-ai"));
        assertEquals(false, runners.containsKey("game2-super-ai"));
        assertEquals(true, runners.containsKey("game3-super-ai"));
        assertEquals(true, runners.containsKey("player7"));

        // обновили описание ребят
        List<PlayerInfo> infos = service.getAll().stream().map(player -> new PlayerInfo(player.getId(),
                player.getCode(), player.getCallbackUrl() + "_updated", player.getGame())).collect(toList());
        service.updateAll(infos);
        assertEquals("[game1-super-ai, " +
                "player1, player4, player5, " +
                "player6, game3-super-ai, " +
                "player7]", service.getAll().toString());

        // зарегали существующего пользователя в другую игру
        expected = "[game1-super-ai, player1, player6]";
        assertEquals(expected, service.getAll("game1").toString());
        assertEquals(expected, service.getAllInRoom("room1").toString());
        expected = "[player4, player5]";
        assertEquals(expected, service.getAll("game2").toString());
        assertEquals(expected, service.getAllInRoom("room2").toString());
        player1 = service.register("player1", "game2", "room2", "callback1");
        expected = "[game1-super-ai, player6]";
        assertEquals(expected, service.getAll("game1").toString());
        assertEquals(expected, service.getAllInRoom("room1").toString());
        expected = "[player4, player5, game2-super-ai, player1]";
        // TODO какого фига сюда AI ломится? Там же есть ребята уже
        assertEquals(expected, service.getAll("game2").toString());
        assertEquals(expected, service.getAllInRoom("room2").toString());
        expected = "[game3-super-ai, player7]";
        assertEquals(expected, service.getAll("game3").toString());
        assertEquals(expected, service.getAllInRoom("room3").toString());

        // пользователь перешел в другую комнату той же игры
        player1 = service.register("player1", "game2", "room4", "callback1");
        expected = "[game1-super-ai, player6]";
        assertEquals(expected, service.getAll("game1").toString());
        assertEquals(expected, service.getAllInRoom("room1").toString());
        assertEquals("[player4, player5, game2-super-ai, player1]",
                service.getAll("game2").toString()); // тут отличие game2 != room2
        assertEquals("[player4, player5, game2-super-ai]",
                service.getAllInRoom("room2").toString()); // тут отличие game2 != room2
        expected = "[game3-super-ai, player7]";
        assertEquals(expected, service.getAll("game3").toString());
        assertEquals(expected, service.getAllInRoom("room3").toString());
        assertEquals("[]", service.getAll("game4").toString()); // нет такой игры
        assertEquals("[player1]", service.getAllInRoom("room4").toString());

        // удалили всех из комнаты 1
        service.removeAll("room2");
        expected = "[game1-super-ai, player6]";
        assertEquals(expected, service.getAll("game1").toString());
        assertEquals(expected, service.getAllInRoom("room1").toString());
        assertEquals("[player1]", service.getAll("game2").toString()); // тут отличие game2 != room2
        assertEquals("[]", service.getAllInRoom("room2").toString()); // тут отличие game2 != room2
        expected = "[game3-super-ai, player7]";
        assertEquals(expected, service.getAll("game3").toString());
        assertEquals(expected, service.getAllInRoom("room3").toString());
        assertEquals("[]", service.getAll("game4").toString()); // нет такой игры
        assertEquals("[player1]", service.getAllInRoom("room4").toString());

        // удалили всех нафиг
        service.removeAll();
        assertEquals("[]", service.getAll("game1").toString());
        assertEquals("[]", service.getAll("game2").toString());
        assertEquals("[]", service.getAll("game3").toString());
        verify(runners.get("game1-super-ai"), times(1)).close();
        verify(runners.get("game3-super-ai"), times(1)).close();
        verify(runners.get("player7"), times(1)).close();
        runners.clear();

        // грузим плеера из сейва
        player1 = service.register(new PlayerSave("player1", "callback1", "game1", "room1", 120, "{save:true}"));
        assertEquals("[player1]", service.getAll("game1").toString());
        assertEquals(0, runners.size());

        // а теперь AI из сейва
        verify(gameTypes.get("game1"), times(ai1)).getAI();
        verify(gameTypes.get("game1"), times(ai1)).getBoard();
        player1 = service.register(new PlayerSave("bot-super-ai", "callback", "game1", "room1", 120, "{save:true}"));
        assertEquals("[player1, bot-super-ai]", service.getAll("game1").toString());
        verify(gameTypes.get("game1"), times(++ai1)).getAI();
        verify(gameTypes.get("game1"), times(ai1)).getBoard();
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
        when(gameType.createPlayer(any(EventListener.class), anyInt(), anyString(), any()))
                .thenAnswer(inv -> mock(GamePlayer.class));
        when(gameType.getPrinterFactory()).thenReturn(mock(PrinterFactory.class));
        when(gameType.getAI()).thenReturn(AISolverStub.class);
        when(gameType.getBoard()).thenReturn(BoardStub.class);
        when(gameType.name()).thenReturn(name);

        gameTypes.put(name, gameType);

        return gameType;
    }
}
