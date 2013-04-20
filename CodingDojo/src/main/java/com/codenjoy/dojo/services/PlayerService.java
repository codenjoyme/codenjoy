package com.codenjoy.dojo.services;

import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 3/9/13
 * Time: 4:23 PM
 */
public interface PlayerService {

    Player addNewPlayer(final String name, final String callbackUrl);

    List<Player> getPlayers();

    void removePlayerByName(String name);

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
}
