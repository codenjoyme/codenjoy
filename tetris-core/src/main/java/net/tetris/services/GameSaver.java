package net.tetris.services;

import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 11/19/12
 * Time: 4:09 AM
 */
public interface GameSaver {
    void saveGame(Player player);

    Player.PlayerBuilder loadGame(String userName);

    List<String> getSavedList();

    void delete(String name);
}
