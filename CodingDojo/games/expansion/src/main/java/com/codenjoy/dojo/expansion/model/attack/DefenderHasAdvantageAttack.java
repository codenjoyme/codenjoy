package com.codenjoy.dojo.expansion.model.attack;

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


import com.codenjoy.dojo.expansion.model.levels.items.HeroForces;

import java.util.List;

import static com.codenjoy.dojo.expansion.services.SettingsWrapper.data;

/**
 * Created by Oleksandr_Baglai on 2017-09-12.
 */
public class DefenderHasAdvantageAttack implements Attack {

    @Override
    public boolean calculate(List<HeroForces> forces) {
        if (forces.size() <= 1) return false;

        HeroForces defender = forces.remove(0);
        // now we have only attackers in forces. So it is shorter on 1 element.
        forces.sort((f1, f2) -> Integer.compare(f2.getCount(), f1.getCount()));

        HeroForces max1 = forces.get(0);
        int max2 = 0;
        if (forces.size() >= 2) {
            max2 = forces.get(1).getCount();
        }
        int other = 0;
        if (forces.size() == 3) {
            other = forces.get(2).getCount();
        }

        int temp = roundDown((max1.getCount() + max2 + other) / advantage());
        if (temp <= defender.getCount()) {
            defender.leave(temp, 0);
            setWinner(forces, defender, defender);
            return true;
        }

        temp = roundDown((max2 + max2 + other) / advantage());
        if (temp >= defender.getCount()) {
            max1.leave(max2, 0);
            setWinner(forces, max1, defender);
            return true;
        }

        temp = roundUp(defender.getCount() * advantage());
        max1.leave(temp - max2 - other, 0);
        setWinner(forces, max1, defender);
        return true;
    }

    /**
     * round this up; i.e. 5.1 -> 6; 5->5
     */
    private int roundUp(double a) {
        return (int) Math.ceil(a);
    }

    /**
     * round this down; i.e. 5.7 -> 5; 5->5
     */
    private int roundDown(double a) {
        return (int) Math.floor(a);
    }

    private double advantage() {
        return data.defenderAdvantage();
    }

    private void setWinner(List<HeroForces> forces, HeroForces winner, HeroForces defender) {
        for (HeroForces force : forces.toArray(new HeroForces[0])) {
            if (force == winner) {
                continue;
            }
            remove(forces, force);
        }

        if (winner != defender) {
            remove(forces, defender);
        } else {
            forces.add(defender);
        }

        if (winner.getCount() == 0) {
            remove(forces, winner);
        }
    }

    private void remove(List<HeroForces> forces, HeroForces force) {
        forces.remove(force);
        force.removeFromCell();
    }
}
