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

    private Hero hero;
    private int count;

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

    public void decrease(int count) {
        this.count -= count;
        if (this.count < 0) {
            throw new IllegalStateException("Forces is negative!");
        }
    }

    public void increase(int count) {
        this.count += count;
    }

    public void setWin() {
        hero.setWin();
    }
}
