package com.codenjoy.dojo.sokoban.services;

import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SettingsReader;
import com.codenjoy.dojo.sokoban.helper.TextIOHelper;
import com.codenjoy.dojo.sokoban.model.items.Level;
import com.codenjoy.dojo.sokoban.model.itemsImpl.LevelImpl;

import static com.codenjoy.dojo.sokoban.services.GameSettings.Keys.*;

public class GameSettings extends SettingsImpl implements SettingsReader<GameSettings> {

    public enum Keys implements Key {

        WIN_SCORE("Win score"),
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
        addEditBox(LEVEL_MAP.key()).multiline().type(String.class).def(loadLevelMap());

        addEditBox(WIN_SCORE.key()).type(Integer.class).def(30);
        addEditBox(LOOSE_PENALTY.key()).type(Integer.class).def(100);
    }

    private String loadLevelMap() {
        if (Storage.levels.size() > 0) {
            // TODO player has to be real, not PlayerFirst as dummy
            return TextIOHelper.getStringFromResourcesRtf(Storage.levels.get("PlayerFirst"));
        } else {
            return TextIOHelper.getStringFromResourcesRtf(1);
        }
    }

    public Level level() {
        return new LevelImpl(loadLevelMap());
    }
}
