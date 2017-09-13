package com.epam.dojo.expansion.model.attack;

import com.epam.dojo.expansion.model.levels.items.HeroForces;

import java.util.List;

import static com.epam.dojo.expansion.services.SettingsWrapper.data;

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
            setWinner(forces, defender);
            return true;
        }

        temp = roundDown((max2 + max2 + other) / advantage());
        if (temp > defender.getCount()) {
            max1.leave(max2, 0);
            setWinner(forces, max1);
            return true;
        }

        temp = roundUp(defender.getCount() * advantage());
        max1.leave(temp - max2 - other, 0);
        setWinner(forces, max1);
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

    private void setWinner(List<HeroForces> forces, HeroForces winner) {
        for (HeroForces force : forces.toArray(new HeroForces[0])) {
            if (forces == winner) continue;
            remove(forces, force);
        }

        if (winner.getCount() == 0) {
            remove(forces, winner);
        } else {
            forces.add(winner);
        }
    }

    private void remove(List<HeroForces> forces, HeroForces force) {
        forces.remove(force);
        force.removeFromCell();
    }
}
