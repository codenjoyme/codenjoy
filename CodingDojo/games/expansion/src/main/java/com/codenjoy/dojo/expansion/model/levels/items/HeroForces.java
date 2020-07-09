package com.codenjoy.dojo.expansion.model.levels.items;

/*-
 * #%L
 * expansion - it's a dojo-like platform from developers to developers.
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


import com.codenjoy.dojo.expansion.model.Elements;
import com.codenjoy.dojo.expansion.model.Forces;
import com.codenjoy.dojo.expansion.model.Player;

/**
 * Created by Oleksandr_Baglai on 2017-08-29.
 */
public class HeroForces extends FieldItem {

    public static final HeroForces EMPTY = new HeroForces();
    private Hero hero;
    protected int count;
    private int increase;

    protected HeroForces() {
        super(Elements.FORCE1);
    }

    public HeroForces(Hero hero) {
        this(hero, 0);
    }

    public HeroForces(Hero hero, int count) {
        super(getElement(hero));
        this.hero = hero;
        this.count = count;
    }

    private static Elements getElement(Hero hero) {
        return hero.getBase().element();
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        return Elements.getForce(hero.getBase().index());
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

    public int leave(int count, int countToStay) {
        if (this.count == 0) {
            return 0;
        }
        if (this.count - countToStay < count) {
            count = this.count - countToStay;
        }
        this.count -= count;
        if (this.count < 0) {
            this.count = 0;
            System.out.println("Hero leave negative count on cell!");
        }
        if (this.count == 0) {
            removeFromCell();
        }
        return count;
    }

    public void move() {
        count += increase;
        increase = 0;
    }

    public void startMove(int increase) {
        this.increase += increase;
    }

    public void setWin() {
        hero.setWin();
    }

    public void pickUpGold(Gold gold) {
        hero.pickUpGold(gold);
    }

    public void forgotGold(Gold gold) {
        hero.forgotGold(gold);
    }

    public boolean ownGold(Gold gold) {
        return hero.ownGold(gold);
    }
}
