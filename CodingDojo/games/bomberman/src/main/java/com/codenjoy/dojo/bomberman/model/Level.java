package com.codenjoy.dojo.bomberman.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.Tickable;
import com.codenjoy.dojo.services.settings.Parameter;

import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 1:48 PM
 */
public interface Level extends Field, Tickable {

    Parameter<Integer> bombsCount(); //настройка для каждой карты своя
    Parameter<Integer> bombsPower(); //настройка для каждой карты своя
    Parameter<Integer> getSize();    //настройка для каждой карты своя

    List<Player> getPlayers();
    List<Player> getBotPlayers();
    List<Player> getNonBotPlayers();
    void prepareChoppers();
    void cleanupChoppers();

    List<PointImpl> getDestroyedWalls();
    List<Bomb> getDestroyedBombs();
}
