package com.codenjoy.dojo.services.multiplayer;

import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.services.PlayerGame;

/**
 * Интерфейс сервиса, реализующий одно- и многопользовательские варианты игры.
 */
public interface MultiplayerService {
    PlayerGame playerWantsToPlay(GameType gameType, Player player);
}
