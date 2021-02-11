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


import java.util.List;

public interface PlayerService extends Tickable {

    Player register(String id, String ip, String roomName, String gameName);
    Player register(PlayerSave save);
    List<Player> getAll();
    List<Player> getAll(String gameName);
    void remove(String id);
    void update(Player player);
    boolean contains(String id);
    Player get(String id);
    void updateAll(List<PlayerInfo> players);
    void removeAll();
    void removeAll(String roomName);
    Player getRandom(String gameType);
    GameType getAnyGameWithPlayers();

    void cleanAllScores();
    void cleanAllScores(String roomName);
    void reloadAllRooms();
    void reloadAllRooms(String roomName);
    void loadSaveForAll(String gameName, String save);

    Joystick getJoystick(String id); // TODO Как-то тут этот метод не вяжется, но ладно пока пусть остается

    void closeRegistration();
    boolean isRegistrationOpened();
    void openRegistration();

    void reloadAI(String id);
}
