package com.epam.dojo.expansion.model.levels;

import com.codenjoy.dojo.services.Dice;
import com.epam.dojo.expansion.model.Expansion;
import com.epam.dojo.expansion.model.GameFactory;
import com.epam.dojo.expansion.model.interfaces.ILevel;
import com.epam.dojo.expansion.model.levels.LevelsFactory;

import java.util.List;

/**
 * Created by Oleksandr_Baglai on 2017-09-01.
 */
public class OneMultipleGameFactory implements GameFactory {


    private Dice dice;
    private LevelsFactory singleFactory;
    private LevelsFactory multipleFactory;

    private Expansion multiple;
    private Expansion single;

    public OneMultipleGameFactory(Dice dice,
                                  LevelsFactory singleFactory,
                                  LevelsFactory multipleFactory) {
        this.dice = dice;
        this.singleFactory = singleFactory;
        this.multipleFactory = multipleFactory;
    }

    @Override
    public Expansion get(boolean isMultiple) {
        return isMultiple ? multiple() : single();
    }

    private Expansion multiple() {
        if (multiple == null) {
            multiple = new Expansion(multipleFactory.get(), dice, Expansion.MULTIPLE);
        }
        return multiple;
    }

    private Expansion single() {
        return new Expansion(singleFactory.get(), dice, Expansion.SINGLE);
    }
}
