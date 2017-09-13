package com.epam.dojo.expansion.model.attack;

import com.epam.dojo.expansion.model.levels.items.HeroForces;

import java.util.LinkedList;
import java.util.List;

import static com.epam.dojo.expansion.services.SettingsWrapper.data;

/**
 * Created by Oleksandr_Baglai on 2017-09-12.
 */
public class DefenderHasAdvantageAttack implements Attack {

    @Override
    public boolean calculate(List<HeroForces> forces) {
        if (forces.size() <= 1) return false;

        HeroForces defender = forces.get(0);

        forces.remove(defender);
        // now we have only attackers in forces. So it is shorter on 1 element.
        forces.sort((f1, f2) -> Integer.compare(f2.getCount(), f1.getCount()));

        HeroForces maxAttacker1 = forces.get(0);
        int maxAttacker2 = 0;
        if (forces.size() >= 2) {
            maxAttacker2 = forces.get(1).getCount();
        }
        int allOtherAttackers = 0;
        if (forces.size() == 3) {
            allOtherAttackers = forces.get(2).getCount();
        }

        // please round this down; i.e. 5.7 -> 5; 5->5. I think just (int) cast should do this
        int temp1 = (int) Math.floor((maxAttacker1.getCount() + maxAttacker2 + allOtherAttackers) / A());
        if (temp1 <= defender.getCount()) {
            defender.leave(temp1, 0);
            setWinner(forces, defender);
        } else {
            // please round this down; i.e. 5.7 -> 5; 5->5. I think just (int) cast should do this
            double temp3 = (int) Math.floor((maxAttacker2 + maxAttacker2 + allOtherAttackers) / A());
            if (temp3 > defender.getCount()) {
                maxAttacker1.leave(maxAttacker2, 0);
            } else {
                //  please round this up; i.e. 5.1 -> 6; 5->5. I think (int)Math.ceil() should do this
                int temp2 = (int) Math.ceil(defender.getCount() * A());
                maxAttacker1.leave(temp2 - maxAttacker2 - allOtherAttackers, 0);
            }
            setWinner(forces, maxAttacker1);
        }

        return true;
    }

    private double A() {
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
