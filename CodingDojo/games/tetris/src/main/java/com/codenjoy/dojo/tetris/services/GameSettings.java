package com.codenjoy.dojo.tetris.services;

import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SettingsReader;
import com.codenjoy.dojo.tetris.model.levels.LevelsFactory;
import com.codenjoy.dojo.tetris.model.levels.level.ProbabilityWithoutOverflownLevels;

import java.util.List;

import static com.codenjoy.dojo.tetris.services.GameSettings.Keys.GAME_LEVELS;
import static com.codenjoy.dojo.tetris.services.GameSettings.Keys.GLASS_SIZE;

public class GameSettings extends SettingsImpl implements SettingsReader<GameSettings> {

    public enum Keys implements Key {

        GAME_LEVELS("Game Levels"),
        GLASS_SIZE("Glass Size");

        private String key;

        Keys(String key) {
            this.key = key;
        }

        @Override
        public String key() {
            return key;
        }
    }

    public GameSettings() {
        addSelect(GAME_LEVELS.key(), (List)levels()).type(String.class)
                .def(ProbabilityWithoutOverflownLevels.class.getSimpleName());

        addEditBox(GLASS_SIZE.key()).type(Integer.class).def(18);
    }

    private List<String> levels() {
        LevelsFactory factory = new LevelsFactory();
        return factory.allLevels();
    }

}
