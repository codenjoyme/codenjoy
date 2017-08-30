package com.epam.dojo.expansion.model.items;

import com.codenjoy.dojo.services.Point;
import com.epam.dojo.expansion.model.Elements;
import com.epam.dojo.expansion.model.Forces;
import com.epam.dojo.expansion.model.Player;

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
        super(Elements.ROBO);
        this.hero = hero;
        this.count = count;
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        if (player.getHero() == hero || Arrays.asList(alsoAtPoint).contains(player.getHero())) {
            return Elements.ROBO;
        } else {
            return Elements.ROBO_OTHER;
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
}
