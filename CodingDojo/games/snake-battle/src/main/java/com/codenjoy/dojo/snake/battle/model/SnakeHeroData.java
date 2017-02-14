package com.codenjoy.dojo.snake.battle.model;

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


import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.hero.HeroData;
import com.codenjoy.dojo.snake.battle.model.hero.Hero;

/**
 * @author K.ilya
 *         Date: 05.12.2016
 *         Time: 18:56
 */
public class SnakeHeroData implements HeroData {

    private Hero hero;

    SnakeHeroData(Hero hero) {
        this.hero = hero;
    }

    @Override
    public Point getCoordinate() {
        return hero.getHead();
    }

    @Override
    public boolean isSingleBoardGame() {
        return false;
    }

    @Override
    public Object getAdditionalData() {
        return null;
    }
}
