package com.codenjoy.dojo.transport.screen;

import java.util.Map;

/**
 * User: serhiy.zelenin
 * Date: 9/26/13
 * Time: 2:05 PM
 */
public interface ScreenSender {
    void sendUpdates(Map<Player, PlayerData> playerScreens);
}
