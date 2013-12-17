package com.codenjoy.dojo.services;

import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

/**
 * User: oleksandr.baglai
 * Date: 3/9/13
 * Time: 5:40 PM
 */
public interface GameType {

    PlayerScores getPlayerScores(int score);

    Game newGame(EventListener listener);

    Parameter<Integer> getBoardSize();

    String gameName();

    Object[] getPlots();

    Settings getGameSettings();

    boolean isSingleBoardGame();
}
