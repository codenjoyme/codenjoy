package com.codenjoy.dojo.services.chat;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component("chatService")
public class ChatServiceImpl implements ChatService {

    public static final String FLOOD_MESSAGE = "НЛО прилетело и украло ваше сообщение";
    public static final String MAX_LENGTH_MESSAGE = "НЛО прилетело и поело ваше длинное сообщение";

    public static int FLOOD_MESSAGES_COUNT = 6;
    public static int MAX_LENGTH = 200;
    public static int MESSAGES_COUNT = 160;

    private List<ChatMessage> messages = new LinkedList<ChatMessage>();

    public ChatServiceImpl() {
        chat("Codenjoy", "Теперь во время игры можно общаться!");
        chat("Codenjoy", "<span style=\"color:red\">Свои</span> <span style=\"color:green\">сообщения</span> <span style=\"color:blue\">можно</span> <span style=\"color:pink\">разукрасить</span>, если знаешь HTML/CSS.");
        chat("Codenjoy", "Каждое твое сообщение не должно быть более чем 200 символов в длинну.");
        chat("Codenjoy", "Иначе оно будет обрезаться обрезаться обрезаться обрезаться обрезаться обрезаться обрезаться обрезаться обрезаться обрезаться обрезаться обрезаться обрезаться обрезаться обрезаться обрезаться обрезаться обрезаться обрезаться обрезаться обрезаться");
        chat("Codenjoy", "Так же не стоит флудить - это некрасиво по отношению к окружающим. Подряд можно послать только 5 сообщений, а потом ждать ответа.");
        chat("Codenjoy", "7-е подряд сообщение - пропадает! Например ты никогда не узнаешь, что я тут написал дальше...");
        chat("Codenjoy", "Бла бла бла");
    }

    @Override
    public void chat(String playerName, String message) {
        if (isFlood(playerName)) {
            if (messages.isEmpty() || !lastFlood()) {
                messages.add(new ChatMessage(playerName + ", " + FLOOD_MESSAGE));
            }
        } else {
            if (message.length() > MAX_LENGTH) {
                messages.add(new ChatMessage(playerName + ", " + MAX_LENGTH_MESSAGE));
                message = message.substring(0, MAX_LENGTH) + "...";
            }
            add(playerName, message);
        }
    }

    private void add(String playerName, String message) {
        messages.add(new ChatMessage(playerName, message));
    }

    private boolean lastFlood() {
        return messages.get(messages.size() - 1).contains(FLOOD_MESSAGE);
    }

    private boolean isFlood(String playerName) {
        if (messages.isEmpty()) return false;

        int count = 0;
        int messagesProcessed = 0;
        for (int index = messages.size() - 1; index >= 0 && messagesProcessed != FLOOD_MESSAGES_COUNT; index--) {
            ChatMessage message = messages.get(index);
            if (!message.isSystem()) {
                messagesProcessed++;
            }
            if (!message.isSystem() && message.is(playerName))  {
                count++;
            }
        }
        return (count == FLOOD_MESSAGES_COUNT);
    }

    @Override
    public String getChatLog() {
        StringBuffer result = new StringBuffer();
        int count = 0;
        if (!messages.isEmpty()) {
            for (int index = messages.size() - 1; index >= 0; index--) {
                if (count++ >= MESSAGES_COUNT) break;
                ChatMessage message = messages.get(index);
                result.append(message).append("\n");
            }
        }
        return StringEscapeUtils.escapeJava(result.toString());
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }

    @Override
    public List<ChatMessage> getMessages() {
        return messages;
    }

}
