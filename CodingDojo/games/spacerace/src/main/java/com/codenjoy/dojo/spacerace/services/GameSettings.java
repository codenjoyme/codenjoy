package com.codenjoy.dojo.spacerace.services;

import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SettingsReader;
import com.codenjoy.dojo.spacerace.model.Level;
import com.codenjoy.dojo.spacerace.model.LevelImpl;

import static com.codenjoy.dojo.spacerace.services.GameSettings.Keys.*;

public class GameSettings extends SettingsImpl implements SettingsReader<GameSettings> {

    public enum Keys implements Key {

        TICKS_TO_RECHARGE("Ticks to recharge"),
        BULLETS_COUNT("Bullets count"),
        DESTROY_BOMB_SCORE("Destroy bomb score"),
        DESTROY_STONE_SCORE("Destroy stone score"),
        DESTROY_ENEMY_SCORE("Destroy enemy score"),
        LOOSE_PENALTY("Loose penalty"),
        LEVEL_MAP("Level map");

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
        integer(TICKS_TO_RECHARGE, 30);
        integer(BULLETS_COUNT, 10);

        integer(DESTROY_BOMB_SCORE, 30);
        integer(DESTROY_STONE_SCORE, 10);
        integer(DESTROY_ENEMY_SCORE, 500);
        integer(LOOSE_PENALTY, 100);

        multiline(LEVEL_MAP,
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼              ☺             ☼");
    }

    public Level level() {
        return new LevelImpl(string(LEVEL_MAP));
    }

}
