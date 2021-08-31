package com.codenjoy.dojo.loderunner.model.levels;

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


import com.codenjoy.dojo.loderunner.model.Hero;
import com.codenjoy.dojo.loderunner.model.items.*;
import com.codenjoy.dojo.loderunner.model.items.enemy.Enemy;
import com.codenjoy.dojo.loderunner.model.items.enemy.EnemyAI;

import java.util.List;

public interface Level {
    int getSize();

    List<Brick> getBricks();

    List<Border> getBorders();

    List<Hero> getHeroes();

    List<YellowGold> getYellowGold();

    List<GreenGold> getGreenGold();

    List<RedGold> getRedGold();

    List<Ladder> getLadder();

    List<Pipe> getPipe();

    List<Enemy> getEnemies();

    List<Pill> getPills();

    List<Portal> getPortals();

    EnemyAI getAi();
}
