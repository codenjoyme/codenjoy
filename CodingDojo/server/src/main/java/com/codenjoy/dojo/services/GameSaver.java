package com.codenjoy.dojo.services;

import com.codenjoy.dojo.services.chat.ChatMessage;

import java.util.List;

public interface GameSaver {
    void saveGame(Player player);
    PlayerSave loadGame(String name);
    List<String> getSavedList();
    void delete(String name);

    void saveChat(List<ChatMessage> messages);
    List<ChatMessage> loadChat();
}
