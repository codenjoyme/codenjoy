package com.codenjoy.dojo.services.multiplayer;

import com.codenjoy.dojo.services.*;

/**
 * Интерфейс сервиса, реализующий одно- и многопользовательские варианты игры.
 */
public interface MultiplayerService {
    /**
     * Добавляеь игрока к существующей игре или создает новую в
     * соответствии с правилами указанными в этой игре.
     * @param gameType тип игры
     * @param player игрок
     * @param save если игрок загружается из save тут будут данные
     * @return игрок связанный с соответствующей игрой
     */
    PlayerGame playerWantsToPlay(GameType gameType, Player player, String save,
                                 PlayerController playerController,
                                 PlayerController screenController);
}
