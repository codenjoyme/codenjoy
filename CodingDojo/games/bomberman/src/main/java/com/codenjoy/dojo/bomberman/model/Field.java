package com.codenjoy.dojo.bomberman.model;

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


import com.codenjoy.dojo.services.multiplayer.GameField;

import java.util.List;

public interface Field extends GameField<Player> {  // TODO применить тут ISP (все ли методы должны быть паблик?)
    int size();

    List<Hero> getBombermans();

    List<Bomb> getBombs();

    List<Bomb> getBombs(Hero bomberman);

    Walls getWalls();

    boolean isBarrier(int x, int y, boolean isWithMeatChopper);

    void remove(Player player);

    List<Blast> getBlasts();

    void drop(Bomb bomb);

    void removeBomb(Bomb bomb);

    GameSettings getSettings();
}
