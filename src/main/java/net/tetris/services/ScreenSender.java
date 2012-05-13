package net.tetris.services;

import net.tetris.web.controller.UpdateRequest;

import java.util.List;
import java.util.Map;

/**
 * User: serhiy.zelenin
 * Date: 5/13/12
 * Time: 10:45 PM
 */
public interface ScreenSender {
    void scheduleUpdate(UpdateRequest updateRequest);

    void sendUpdates(Map<Player, List<Plot>> playerScreens);
}
