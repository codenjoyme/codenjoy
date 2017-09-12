package com.epam.dojo.expansion.model.attack;

import com.epam.dojo.expansion.model.levels.items.HeroForces;

import java.util.List;

/**
 * Created by Oleksandr_Baglai on 2017-09-12.
 */
public interface Attack {
    void calculate(List<HeroForces> forces);
}
