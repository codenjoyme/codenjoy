package com.codenjoy.dojo.icancode.model.items;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2018 EPAM
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

import com.codenjoy.dojo.services.Tickable;
import com.codenjoy.dojo.icancode.model.Elements;
import com.codenjoy.dojo.icancode.model.Hero;
import com.codenjoy.dojo.icancode.model.Player;

public class HeroItem extends FieldItem implements Tickable {

    private Hero hero;

    public HeroItem(Elements element) {
        super(element);
    }

    public Hero getHero() {
        return hero;
    }

    public void init(Hero hero) {
        this.hero = hero;
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        return hero.state(player, alsoAtPoint);
    }

    @Override
    public void tick() {
        hero.tick();
    }

    public void fixLayer() {
        hero.fixLayer();
    }
}
