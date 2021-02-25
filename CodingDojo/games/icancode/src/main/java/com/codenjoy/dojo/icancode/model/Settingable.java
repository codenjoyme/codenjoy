package com.codenjoy.dojo.icancode.model;

import com.codenjoy.dojo.icancode.services.GameSettings;

public interface Settingable {

    GameSettings settings();

    void setSettings(GameSettings settings);
}
