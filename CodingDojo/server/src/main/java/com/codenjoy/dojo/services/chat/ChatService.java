package com.codenjoy.dojo.services.chat;

import java.util.List;

public interface ChatService {
    void chat(String playerName, String message);

    String getChatLog();

    List<ChatMessage> getMessages();

    void setMessages(List<ChatMessage> messages);
}
