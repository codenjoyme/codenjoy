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

    boolean alreadyRegistered(String playerName);

    Player findPlayer(String playerName);

    void updatePlayer(Player player);

    void removeAll();

    Player findPlayerByIp(String ip);

    void removePlayer(String ip);

    int getBoardSize();    // TODO fixme

    void nextStepForAllGames();  // TODO fixme
}
