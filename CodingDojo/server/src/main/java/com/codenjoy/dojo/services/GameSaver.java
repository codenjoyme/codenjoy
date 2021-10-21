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
import java.util.Map;

public interface GameSaver {

    void saveGame(Player player, String save, long time);

    void updateGame(Player player, String save, long time);

    void updateGame(PlayerSave playerSave, String save, long time);

    PlayerSave loadGame(String id);

    List<String> getSavedList();

    void delete(String id);

    void updateScore(String playerId, String gameName, long score, long time);

    List<PlayerSave> loadAllSaves();

    Map<String, String> getEventsList();

    List<String> getUserGames(String id);

    String getRoomNameByPlayerId(String id);

    String getRepositoryURLByPlayerId(String id);

    }
