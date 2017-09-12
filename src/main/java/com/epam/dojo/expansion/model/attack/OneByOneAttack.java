package com.epam.dojo.expansion.model.attack;

import com.epam.dojo.expansion.model.levels.items.HeroForces;

import java.util.List;

/**
 * Created by Oleksandr_Baglai on 2017-09-12.
 */
public class OneByOneAttack implements Attack {

    @Override
    public void calculate(List<HeroForces> forces) {
        while (forces.size() > 1) {
            int min = Integer.MAX_VALUE;
            for (HeroForces force : forces) {
                min = Math.min(min, force.getCount());
            }

            for (HeroForces force : forces) {
                force.leave(min, 0);
            }

            for (HeroForces force : forces.toArray(new HeroForces[0])) {
                if (force.getCount() == 0) {
                    forces.remove(force);
                    force.removeFromCell();
                }
            }
        }
    }
}
