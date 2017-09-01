package com.epam.dojo.expansion.model;

import com.codenjoy.dojo.services.Dice;
import com.epam.dojo.expansion.model.levels.LevelsFactory;

/**
 * Created by Oleksandr_Baglai on 2017-09-01.
 */
public class StubGamesGameFactory implements GameFactory {

    private Expansion multiple;
    private Expansion single;

    public StubGamesGameFactory(Expansion single, Expansion multiple) {
        this.single = single;
        this.multiple = multiple;
    }

    @Override
    public Expansion get(boolean isMultiple) {
        return isMultiple ? multiple : single;
    }
}
