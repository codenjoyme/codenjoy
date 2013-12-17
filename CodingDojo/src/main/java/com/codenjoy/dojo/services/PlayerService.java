package com.codenjoy.dojo.services;

import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 3/9/13
 * Time: 4:23 PM
 */
public interface PlayerService extends Tickable {

    Player register(String name, String password, String callbackUrl);
    Player register(Player.PlayerBuilder builder); // TODO придумать куда его убрать
    boolean login(String name, String password);
    List<Player> getAll();
    void remove(String name);
    boolean contains(String name);
    Player get(String name);
    void update(Player player);
    void updateAll(List<PlayerInfo> players);
    void removeAll();
    Player getByIp(String ip);
    void removeByIp(String ip);
    String getByCode(String code);
    String getRandom();

    void cleanAllScores();

    Joystick getJoystick(String name); // TODO

}
