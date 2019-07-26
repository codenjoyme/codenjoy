package com.codenjoy.dojo.excitebike.model;

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


import com.codenjoy.dojo.excitebike.model.items.Bike;

import java.util.List;
import java.util.Optional;

public interface GameField extends com.codenjoy.dojo.services.multiplayer.GameField<Player> {

    int size();

    boolean isFence(int x, int y);

    boolean isInhibitor(int x, int y);

    boolean isAccelerator(int x, int y);

    boolean isObstacle(int x, int y);

    boolean isUpLineChanger(int x, int y);

    boolean isDownLineChanger(int x, int y);

    boolean isSpringboardDarkElement(int x, int y);

    boolean isSpringboardLightElement(int x, int y);

    boolean isSpringboardLeftDownElement(int x, int y);

    boolean isSpringboardRightDownElement(int x, int y);

    boolean isSpringboardTopElement(int x, int y);

    Optional<Bike> getEnemyBike(int x, int y, Player player);

    Bike getNewFreeBike();

    Player getPlayerOfBike(Bike bike);
}
