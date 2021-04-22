package com.codenjoy.dojo.loderunner.model;

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


import com.codenjoy.dojo.loderunner.model.Pill.PillType;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.round.RoundGameField;

import java.util.List;
import java.util.Optional;

public interface Field extends RoundGameField<Player> {

    boolean isBarrier(Point pt);

    boolean tryToDrill(Hero hero, Point pt);

    boolean isPit(Point pt);

    Optional<Point> getFreeRandom();

    boolean isLadder(Point pt);

    boolean isPipe(Point pt);

    boolean isFree(Point pt);

    boolean isFullBrick(Point pt);

    boolean isHeroAt(Point pt);

    boolean isBrick(Point pt);

    boolean isEnemyAt(Point pt);

    void leaveGold(Point pt, Class<? extends Point> clazz);

    void leavePill(Point pt, PillType pill);

    void leavePortal(Point pt);

    boolean under(Point pt, PillType pill);

    int size();

    boolean isBorder(Point pt);

    List<Hero> getHeroes();

    void suicide(Hero hero);

    List<Brick> bricks();
}
