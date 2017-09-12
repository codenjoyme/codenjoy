package com.epam.dojo.expansion.model.attack;

import com.epam.dojo.expansion.model.levels.items.HeroForces;

import java.util.List;

/**
 * Created by Oleksandr_Baglai on 2017-09-12.
 */
public class OneByOneAttack implements Attack {

    @Override
    public void calculate(List<HeroForces> forces) {
        if (forces.size() <= 1) return;

        forces.sort((f1, f2) -> Integer.compare(f2.getCount(), f1.getCount()));
        HeroForces max = forces.get(0);
        HeroForces next = forces.get(1);
        max.leave(next.getCount(), 0);
        for (HeroForces force : forces.subList(1, forces.size()).toArray(new HeroForces[0])) {
            remove(forces, force);
        }
        if (max.getCount() == 0) {
            remove(forces, max);
        }
    }

    private void remove(List<HeroForces> forces, HeroForces force) {
        forces.remove(force);
        force.removeFromCell();
    }
}
