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


import com.codenjoy.dojo.bomberman.model.perks.PerkOnBoard;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.round.RoundGameField;

import java.util.List;

public interface Field extends RoundGameField<Player> {  // TODO применить тут ISP (все ли методы должны быть паблик?)

    boolean FOR_HERO = true;

    Dice dice();

    int size();

    List<Hero> heroes(boolean activeAliveOnly);

    List<Bomb> bombs();

    List<Bomb> bombs(Hero hero);

    Walls walls();

    boolean isBarrier(Point pt, boolean isForHero);

    void remove(Player player);

    List<Blast> blasts();

    void drop(Bomb bomb);

    void remove(Bomb bomb);

    void remove(Wall wall);

    GameSettings settings();

    List<PerkOnBoard> perks();

    PerkOnBoard pickPerk(Point pt);
}
