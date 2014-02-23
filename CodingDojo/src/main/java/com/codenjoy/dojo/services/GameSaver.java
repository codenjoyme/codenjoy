package com.codenjoy.dojo.services;

import com.codenjoy.dojo.services.chat.ChatService;

import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 3/22/13
 * Time: 10:55 PM
 */
public interface GameSaver {
    void saveGame(Player player);

    Player.PlayerBuilder loadGame(String userName);

    List<String> getSavedList();

    void delete(String name);

    void saveChat(ChatService chatService);

    void loadChat(ChatService chatService);
}
