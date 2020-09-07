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

/**
 * Created by Oleksandr_Baglai on 2017-09-12.
 */
public class OneByOneAttack implements Attack {

    @Override
    public boolean calculate(List<HeroForces> forces) {
        if (forces.size() <= 1) return false;

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

        return true;
    }

    private void remove(List<HeroForces> forces, HeroForces force) {
        forces.remove(force);
        force.removeFromCell();
    }
}
