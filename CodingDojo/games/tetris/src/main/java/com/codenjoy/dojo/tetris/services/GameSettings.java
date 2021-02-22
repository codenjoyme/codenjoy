package com.codenjoy.dojo.tetris.services;

import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.tetris.model.levels.LevelsFactory;
import com.codenjoy.dojo.tetris.model.levels.level.ProbabilityWithoutOverflownLevels;

import java.util.List;

public class GameSettings extends SettingsImpl {

    public static final String GAME_LEVELS = "Game Levels";
    public static final String GLASS_SIZE = "Glass Size";

    private Parameter<String> gameLevels;
    private Parameter<Integer> glassSize;

    public GameSettings() {
        gameLevels = addSelect(GAME_LEVELS, (List)levels()).type(String.class)
                .def(ProbabilityWithoutOverflownLevels.class.getSimpleName());

        glassSize = addEditBox(GLASS_SIZE).type(Integer.class).def(18);
    }

    private List<String> levels() {
        LevelsFactory factory = new LevelsFactory();
        return factory.allLevels();
    }

    public Parameter<String> gameLevels() {
        return gameLevels;
    }

    public Parameter<Integer> glassSize() {
        return glassSize;
    }
}
