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
        addEditBox(TICKS_TO_RECHARGE.key()).type(Integer.class).def(30);
        addEditBox(BULLETS_COUNT.key()).type(Integer.class).def(10);

        addEditBox(DESTROY_BOMB_SCORE.key()).type(Integer.class).def(30);
        addEditBox(DESTROY_STONE_SCORE.key()).type(Integer.class).def(10);
        addEditBox(DESTROY_ENEMY_SCORE.key()).type(Integer.class).def(500);
        addEditBox(LOOSE_PENALTY.key()).type(Integer.class).def(100);

        addEditBox(LEVEL_MAP.key()).multiline().type(String.class).def(
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
