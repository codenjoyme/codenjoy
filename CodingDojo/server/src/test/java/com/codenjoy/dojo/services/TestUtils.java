package com.codenjoy.dojo.services;

import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.printer.Printer;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import org.mockito.stubbing.Answer;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestUtils {

    private static int index = 0;

    public static class Env {
        public PlayerGame playerGame;
        public Joystick joystick;
        public GamePlayer gamePlayer;
        public GameType gameType;
        public PrinterFactory printerFactory;
    }

    public static Env getPlayerGame(PlayerGames playerGames, Player player, Answer<Object> answerCreateGame) {
        Joystick joystick = mock(Joystick.class);
        GamePlayer gamePlayer = mock(GamePlayer.class);
        when(gamePlayer.getJoystick()).thenReturn(joystick);

        GameType gameType = player.getGameType();
        if (gameType == null) {
            gameType = mock(GameType.class);
            if (player.getClass().getSimpleName().contains("Mock")) {
                when(player.getGameType()).thenReturn(gameType);
            } else {
                player.setGameType(gameType);
            }
        }

        when(gameType.getMultiplayerType()).thenReturn(MultiplayerType.SINGLE);
        when(gameType.createGame()).thenAnswer(answerCreateGame);
        PrinterFactory printerFactory = mock(PrinterFactory.class);
        when(gameType.getPrinterFactory()).thenReturn(printerFactory);
        Answer<Object> answerPrinter =
                inv -> (Printer<String>) parameters -> "board" + ++index;
        when(printerFactory.getPrinter(any(BoardReader.class), any()))
                .thenAnswer(answerPrinter);
        when(gameType.createPlayer(any(EventListener.class), anyString(), anyString()))
                .thenAnswer(inv -> gamePlayer);

        PlayerGame playerGame = playerGames.add(player, null);
        Env result = new Env();
        result.gamePlayer = gamePlayer;
        result.gameType = gameType;
        result.joystick = joystick;
        result.playerGame = playerGame;
        result.printerFactory = printerFactory;
        return result;
    }
}
