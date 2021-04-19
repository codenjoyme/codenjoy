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

public interface GameService {

    List<String> getGames();

    List<String> getRooms();

    List<String> getOnlyGames();

    Map<String, List<String>> getSpritesNames();
    
    Map<String, List<String>> getSpritesValues();

    Map<String, List<String>> getSprites();

    GameType getGameType(String game);

    // TODO из названия как-то неочевидно, что создается рума если ее не существовало раннее
    GameType getGameType(String game, String room);

    String getDefaultRoom();

    boolean exists(String game);

    String getDefaultProgress(GameType gameType);
}
