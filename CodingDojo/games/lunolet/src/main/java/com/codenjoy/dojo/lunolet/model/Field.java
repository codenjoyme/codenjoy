package com.codenjoy.dojo.lunolet.model;

import com.codenjoy.dojo.services.multiplayer.GameField;

public interface Field extends GameField<Player> {
    LevelManager getLevels();
}
