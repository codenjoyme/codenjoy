package com.codenjoy.dojo.services;

import com.codenjoy.dojo.services.playerdata.PlotsBuilder;

/**
 * User: oleksandr.baglai
 * Date: 3/9/13
 * Time: 5:40 PM
 */
public interface GameType {

    PlayerScores getPlayerScores(int score);

    Game newGame(EventListener informationCollector);

    int getBoardSize();

    String gameName();

    Object[] getPlots();
}
