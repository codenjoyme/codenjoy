package com.codenjoy.dojo.snakebattle.model.board;

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


import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.round.RoundGameField;
import com.codenjoy.dojo.snakebattle.model.Player;
import com.codenjoy.dojo.snakebattle.model.hero.Hero;

import java.util.Optional;

public interface Field extends RoundGameField<Player> {

    boolean isBarrier(Point p);

    Optional<Point> freeRandom();

    boolean isApple(Point p);

    boolean isStone(Point p);

    boolean isFlyingPill(Point p);

    boolean isFuryPill(Point p);

    boolean isGold(Point p);

    void setApple(Point p);

    boolean setStone(Point p);

    void setFlyingPill(Point p);

    void setFuryPill(Point p);

    void setGold(Point p);

    Hero enemyEatenWith(Hero h);
}
