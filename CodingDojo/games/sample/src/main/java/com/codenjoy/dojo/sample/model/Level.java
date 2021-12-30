package com.codenjoy.dojo.sample.model;

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


import com.codenjoy.dojo.sample.model.items.Bomb;
import com.codenjoy.dojo.sample.model.items.Gold;
import com.codenjoy.dojo.sample.model.items.Wall;
import com.codenjoy.dojo.services.field.AbstractLevel;
import com.codenjoy.dojo.services.field.PointField;

import java.util.List;

import static com.codenjoy.dojo.games.sample.Element.*;

/**
 * Полезный класс для получения объектов на поле из текстового вида.
 *
 * Обрати внимание на множество методов поиска find Наследуемых
 * от {@link AbstractLevel}.
 */
public class Level extends AbstractLevel {

    public Level(String map) {
        super(map);
    }

    public List<Hero> heroes() {
        return find(Hero::new, HERO);
    }

    public List<Gold> gold() {
        return find(Gold::new, GOLD);
    }

    public List<Bomb> bombs() {
        return find(pt -> new Bomb(pt, null), BOMB);
    }

    public List<Wall> walls() {
        return find(Wall::new, WALL);
    }

    /**
     * @param field {@link PointField} который будет основным
     *         хранилищем объектов на поле.
     */
    @Override
    protected void fill(PointField field) {
        field.addAll(walls());
        field.addAll(gold());
        field.addAll(bombs());
    }
}