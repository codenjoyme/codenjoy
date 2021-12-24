package com.codenjoy.dojo.snake;

import com.codenjoy.dojo.services.event.ScoresImpl;
import com.codenjoy.dojo.snake.services.GameSettings;

import static com.codenjoy.dojo.snake.services.GameSettings.Keys.*;

public class TestGameSettings extends GameSettings {

    public TestGameSettings() {
        ScoresImpl.setup(this, ScoresImpl.CUMULATIVELY);

        integer(BOARD_SIZE, 15);
        integer(GAME_OVER_PENALTY, -50);
        integer(START_SNAKE_LENGTH, 2);
        integer(EAT_STONE_PENALTY, -10);
        integer(EAT_STONE_DECREASE, 10);
    }
}
