package com.codenjoy.dojo.icancode.model.items;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2020 Codenjoy
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

import com.codenjoy.dojo.icancode.model.FieldItem;
import com.codenjoy.dojo.services.Tickable;
import com.codenjoy.dojo.icancode.model.Elements;
import com.codenjoy.dojo.icancode.model.Hero;
import com.codenjoy.dojo.icancode.model.Player;

import static com.codenjoy.dojo.icancode.model.Elements.Layers.LAYER3;

public class HeroItem extends FieldItem implements Tickable {

    protected Hero hero;

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

    @Override
    public int layer() {
        if (hero != null && hero.isFlying()) {
            return LAYER3;
        }

        return super.layer();
    }
}
