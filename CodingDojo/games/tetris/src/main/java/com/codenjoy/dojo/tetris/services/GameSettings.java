package com.codenjoy.dojo.tetris.services;

import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SettingsReader;
import com.codenjoy.dojo.tetris.model.levels.LevelsFactory;
import com.codenjoy.dojo.tetris.model.levels.level.ProbabilityWithoutOverflownLevels;

import java.util.List;

import static com.codenjoy.dojo.tetris.services.GameSettings.Keys.*;

public class GameSettings extends SettingsImpl implements SettingsReader<GameSettings> {

    public enum Keys implements Key {

        GAME_LEVELS("Game Levels"),
        GLASS_SIZE("Glass Size"),
        FIGURE_DROPPED_SCORE("Figure dropped score score"),
        ONE_LINE_REMOVED_SCORE("One line removed score"),
        TWO_LINES_REMOVED_SCORE("Two lines removed score"),
        THREE_LINES_REMOVED_SCORE("Three lines removed score"),
        FOUR_LINES_REMOVED_SCORE("Four lines removed score"),
        GLASS_OVERFLOWN_PENALTY("Glass overflown penalty");

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

        addEditBox(FIGURE_DROPPED_SCORE.key()).type(Integer.class).def(1);
        addEditBox(ONE_LINE_REMOVED_SCORE.key()).type(Integer.class).def(10);
        addEditBox(TWO_LINES_REMOVED_SCORE.key()).type(Integer.class).def(30);
        addEditBox(THREE_LINES_REMOVED_SCORE.key()).type(Integer.class).def(50);
        addEditBox(FOUR_LINES_REMOVED_SCORE.key()).type(Integer.class).def(100);
        addEditBox(GLASS_OVERFLOWN_PENALTY.key()).type(Integer.class).def(10);
    }

    private List<String> levels() {
        LevelsFactory factory = new LevelsFactory();
        return factory.allLevels();
    }

}
