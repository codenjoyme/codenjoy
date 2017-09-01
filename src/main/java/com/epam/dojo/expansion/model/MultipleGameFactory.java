package com.epam.dojo.expansion.model;

import com.codenjoy.dojo.services.RandomDice;
import com.epam.dojo.expansion.model.levels.Levels;
import com.epam.dojo.expansion.model.levels.LevelsFactory;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Oleksandr_Baglai on 2017-09-01.
 */
// TODO test me
public class MultipleGameFactory implements GameFactory {

    private List<Expansion> rooms = new LinkedList<>();

    private LevelsFactory singleFactory;
    private LevelsFactory multipleFactory;

    public MultipleGameFactory(LevelsFactory singleFactory,
                               LevelsFactory multipleFactory)
    {
        this.singleFactory = singleFactory;
        this.multipleFactory = multipleFactory;
    }

    @Override
    public Expansion get(boolean isMultiple) {
        if (isMultiple) {
            Expansion game = findFreeMultiple();
            if (game == null) {
                game = createNewMultiple();
            }
            return game;
        } else {
            return new Expansion(singleFactory.get(),
                    new RandomDice(), Expansion.SINGLE);
        }
    }

    private Expansion findFreeMultiple() {
        for (Expansion game : rooms) {
            if (game.isNotBusy()) {
                return game;
            }
        }
        return null;
    }

    @NotNull
    private Expansion createNewMultiple() {
        Expansion game = new Expansion(multipleFactory.get(),
                new RandomDice(), Expansion.MULTIPLE);
        rooms.add(game);
        return game;
    }
}
