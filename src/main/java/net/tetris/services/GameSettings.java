package net.tetris.services;

import net.tetris.dom.Levels;

import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 9/24/12
 * Time: 6:41 PM
 */
public interface GameSettings {

    Levels getGameLevels(PlayerFigures queue);

    void setGameLevels(String levelSettings);

    String getGameLevels();

    List<String> getGameLevelsList();
}
