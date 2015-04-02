package com.codenjoy.dojo.services;

import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 3/9/13
 * Time: 4:23 PM
 */
public interface PlayerService extends Tickable {

    Player register(String name, String callbackUrl, String gameName);
    Player register(PlayerSave save);
    List<Player> getAll();
    List<Player> getAll(String gameName);
    void remove(String name);
    boolean contains(String name);
    Player get(String name);
    void updateAll(List<PlayerInfo> players);
    void removeAll();
    Player getRandom(String gameType);
    GameType getAnyGameWithPlayers();

    void cleanAllScores();

    Joystick getJoystick(String name); // TODO Как-то тут этот метод не вяжется, но ладно пока пусть остается

    void closeRegistration();
    boolean isRegistrationOpened();
    void openRegistration();

}
