package com.codenjoy.dojo.services;

import com.codenjoy.dojo.services.settings.Settings;

import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 3/9/13
 * Time: 4:23 PM
 */
public interface PlayerService {

    Player addNewPlayer(String name, String password, String callbackUrl);

    List<Player> getPlayers();

    void gameOverPlayerByName(String name);

    void loadPlayerGame(String name);

    void savePlayerGame(String name);

    boolean alreadyRegistered(String playerName);

    Player findPlayer(String playerName);

    void updatePlayer(Player player);

    void updatePlayers(List<PlayerInfo> players);

    void removeAll();

    Player findPlayerByIp(String ip);

    void removePlayerByIp(String ip);

    List<PlayerInfo> getPlayersGames();

    int getBoardSize();    // TODO fixme

    void nextStepForAllGames();  // TODO fixme

    String getGameType();

    void saveAllGames();

    void loadAllGames();

    Protocol getProtocol();

    Joystick getJoystick(String playerName);

    boolean isSingleBoardGame();

    void cleanAllScores();

    Settings getGameSettings();

    void selectGame(String name);

    List<String> getGames();

    void removePlayerSaveByName(String playerName);

    void removeAllPlayerSaves();

    String getPlayerByCode(String code);

    String getRandomPlayerName();

    boolean login(String name, String password);
}
