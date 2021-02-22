package com.codenjoy.dojo.sokoban.services;

import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.sokoban.helper.TextIOHelper;
import com.codenjoy.dojo.sokoban.model.items.Level;
import com.codenjoy.dojo.sokoban.model.itemsImpl.LevelImpl;
import org.apache.commons.lang3.StringUtils;

public class GameSettings extends SettingsImpl {

    private String levelMap;

    protected String levelMap() {
        if (!StringUtils.isEmpty(levelMap)) {
            return levelMap;
        } else if (Storage.levels.size() > 0) {
            // TODO player has to be real, not PlayerFirst as dummy
            return TextIOHelper.getStringFromResourcesRtf(Storage.levels.get("PlayerFirst"));
        } else {
            return TextIOHelper.getStringFromResourcesRtf(1);
        }
    }

    // getters

    public Level level() {
        return new LevelImpl(levelMap());
    }

    // setters

    public GameSettings levelMap(String value) {
        levelMap = value;
        return this;
    }
}
