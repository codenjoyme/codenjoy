package com.codenjoy.dojo.services;

import com.codenjoy.dojo.client.Closeable;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.printer.BoardReader;
import org.junit.Before;

import java.util.*;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Oleksandr_Baglai on 2019-10-12.
 */
public class AbstractPlayerGamesTest {

    protected PlayerGames playerGames;
    protected List<GameType> gameTypes = new LinkedList<>();
    protected Map<Player, Closeable> ais = new HashMap<>();
    protected List<Joystick> joysticks = new LinkedList<>();
    protected List<Joystick> lazyJoysticks = new LinkedList<>();
    protected List<GamePlayer> gamePlayers = new LinkedList<>();
    protected List<GameField> fields = new LinkedList<>();

    @Before
    public void setUp() {
        playerGames = new PlayerGames();
    }

    protected Player createPlayer() {
        return createPlayer("game");
    }

    protected Player createPlayer(String gameName) {
        return createPlayer(gameName, "player" + Calendar.getInstance().getTimeInMillis(),
                MultiplayerType.SINGLE);
    }

    protected Player createPlayer(String gameName, String name, MultiplayerType type) {
        return createPlayer(gameName, name, type, null);
    }

    protected Player createPlayer(String gameName, String name, MultiplayerType type, PlayerSave save) {
        return createPlayer(gameName, name, type, save, "board");
    }

    protected void verifyRemove(PlayerGame playerGame, GameField field) {
        verify(field).remove(playerGame.getGame().getPlayer());
        verify(ais.get(playerGame.getPlayer())).close();
    }

    protected Player createPlayerWithScore(int score) {
        Player player = createPlayer();
        when(player.getScores().getScore()).thenReturn(score);
        return player;
    }

    protected void setPlayerStatus(int index, boolean stillPlay) {
        when(gamePlayers.get(index).isAlive()).thenReturn(stillPlay);
        when(gamePlayers.get(index).isWin()).thenReturn(!stillPlay);
        when(gamePlayers.get(index).shouldLeave()).thenReturn(!stillPlay);
    }

    protected Player createPlayer(String gameName, String name, MultiplayerType type, PlayerSave save, Object board) {
        GameService gameService = mock(GameService.class);
        GameType gameType = mock(GameType.class);
        gameTypes.add(gameType);
        PlayerScores scores = mock(PlayerScores.class);
        when(gameType.getPlayerScores(anyInt())).thenReturn(scores);
        when(gameType.name()).thenReturn(gameName);
        when(gameService.getGame(anyString())).thenReturn(gameType);

        Player player = new Player(name, "url", gameType, scores, mock(Information.class));
        player.setEventListener(mock(InformationCollector.class));
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
                            when(field.reader()).thenReturn(mock(BoardReader.class));
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
}
