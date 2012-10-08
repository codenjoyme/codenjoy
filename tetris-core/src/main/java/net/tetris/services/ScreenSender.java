package net.tetris.services;

import java.util.Map;

/**
 * User: serhiy.zelenin
 * Date: 5/13/12
 * Time: 10:45 PM
 */
public interface ScreenSender {
    void sendUpdates(Map<Player, PlayerData> playerScreens);
}
