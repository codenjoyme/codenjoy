package com.codenjoy.dojo.snakebattle.model;

import com.codenjoy.dojo.snakebattle.services.GameSettings;

import static com.codenjoy.dojo.services.round.RoundSettings.Keys.*;
import static com.codenjoy.dojo.services.round.RoundSettings.Keys.TIME_FOR_WINNER;
import static com.codenjoy.dojo.snakebattle.services.GameSettings.Keys.*;

public class TestGameSettings extends GameSettings {

    public TestGameSettings() {
        bool(ROUNDS_ENABLED, true)
            .integer(ROUNDS_PER_MATCH, 5)
            .integer(MIN_TICKS_FOR_WIN, 2)
            .integer(TIME_BEFORE_START, 0)
            .integer(TIME_PER_ROUND, 300)
            .integer(TIME_FOR_WINNER, 1)
            .integer(FLYING_COUNT, 10)
            .integer(FURY_COUNT, 10)
            .integer(STONE_REDUCED, 3);
    }
}
