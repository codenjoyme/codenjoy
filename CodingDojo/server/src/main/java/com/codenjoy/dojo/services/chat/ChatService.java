package com.codenjoy.dojo.services.chat;

import java.util.List;

/**
 * User: sanja
 * Date: 23.09.13
 * Time: 23:35
 */
public interface ChatService {
    void chat(String playerName, String message);

    String getChatLog();

    List<ChatMessage> getMessages();

    void setMessages(List<ChatMessage> messages);
}
