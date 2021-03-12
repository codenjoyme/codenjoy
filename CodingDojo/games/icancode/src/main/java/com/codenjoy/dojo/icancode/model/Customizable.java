package com.codenjoy.dojo.icancode.model;

import com.codenjoy.dojo.icancode.services.GameSettings;

public interface Customizable {

    GameSettings settings();

    void init(GameSettings settings);
}
