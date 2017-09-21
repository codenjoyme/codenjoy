package com.epam.dojo.expansion.model.replay;

import com.epam.dojo.expansion.model.Expansion;
import com.epam.dojo.expansion.model.Player;

/**
 * Created by Oleksandr_Baglai on 2017-09-21.
 */
public interface GameLogger {
    void start(Expansion expansion);

    void register(Player player);

    void logState();
}
