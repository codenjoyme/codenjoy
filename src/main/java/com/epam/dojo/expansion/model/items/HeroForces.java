package com.epam.dojo.expansion.model.items;

/*-
 * #%L
 * expansion - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2017 EPAM
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


import com.epam.dojo.expansion.model.Elements;
import com.epam.dojo.expansion.model.Forces;
import com.epam.dojo.expansion.model.Player;
import com.epam.dojo.expansion.model.interfaces.ICell;

import java.util.Arrays;

/**
 * Created by Oleksandr_Baglai on 2017-08-29.
 */
public class HeroForces extends FieldItem {

    public static final HeroForces EMPTY = new HeroForces(null);
    private Hero hero;
    private int count;
    private int increase;

    public HeroForces(Hero hero) {
        this(hero, 0);
    }

    public HeroForces(Hero hero, int count) {
        super(Elements.FORCE1);
        this.hero = hero;
        this.count = count;
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        if (player.getHero() == hero || Arrays.asList(alsoAtPoint).contains(player.getHero())) {
            return Elements.MY_FORCE;
        } else {
            ICell base = hero.getBase();
            HeroForces forces = base.getItem(HeroForces.class);
            return forces.element;
        }
    }

    public int getCount() {
        return count;
    }

    public Forces getForces() {
        return new Forces(this.getCell(), count);
    }

    public boolean itsMe(Hero hero) {
        return hero == this.hero;
    }

    public int decrease(int count) {
        if (this.count == 0) {
            return 0;
        }
        if (this.count - 1 < count) {
            count = this.count - 1;
        }
        this.count -= count;
        if (this.count < 0) {
            throw new IllegalStateException("Forces is negative!");
        }
        return count;
    }

    public void tryIncrease(int count) {
        this.increase = count;
    }

    public void increase() {
        count += increase;
    }

    public void setWin() {
        hero.setWin();
    }

    public void pickUpGold() {
        hero.pickUpGold();
    }
}
