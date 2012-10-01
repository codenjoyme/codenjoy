package com.globallogic.snake.services;

import com.globallogic.snake.services.Player;
import com.globallogic.snake.services.playerdata.PlayerData;
import com.globallogic.snake.web.controller.UpdateRequest;

import java.util.Map;

/**
 * User: oleksandr.baglai
 * Date: 10/1/12
 * Time: 6:50 AM
 */
public interface ScreenSender {
    void scheduleUpdate(UpdateRequest updateRequest);

    void sendUpdates(Map<Player, PlayerData> playerScreens);
}
