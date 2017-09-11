package com.epam.dojo.expansion.model;

import com.codenjoy.dojo.services.Tickable;
import com.epam.dojo.expansion.model.levels.Level;
import com.epam.dojo.expansion.model.levels.items.Hero;

import java.util.List;

/**
 * Created by Oleksandr_Baglai on 2017-09-11.
 */
public interface PlayerBoard extends Tickable {
    int getViewSize();

    int size();

    Level getCurrentLevel();

    boolean isMultiple();

    int levelsCount();

    void newGame(Player player);

    void remove(Player player);

    void loadLevel(int level);

    List<Player> getPlayers();

    int getRoundTicks();

    Expansion.LogState lg();

    List<Hero> getHeroes();
}
