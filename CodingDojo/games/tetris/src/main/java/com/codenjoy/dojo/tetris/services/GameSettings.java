package com.codenjoy.dojo.tetris.services;

import com.codenjoy.dojo.services.settings.SelectBox;
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
        options(GAME_LEVELS, levels(),
                ProbabilityWithoutOverflownLevels.class.getSimpleName());
        integer(GLASS_SIZE, 18);
        integer(FIGURE_DROPPED_SCORE, 1);
        integer(ONE_LINE_REMOVED_SCORE, 10);
        integer(TWO_LINES_REMOVED_SCORE, 30);
        integer(THREE_LINES_REMOVED_SCORE, 50);
        integer(FOUR_LINES_REMOVED_SCORE, 100);
        integer(GLASS_OVERFLOWN_PENALTY, 10);
    }

    private List<String> levels() {
        LevelsFactory factory = new LevelsFactory();
        return factory.allLevels();
    }

}
