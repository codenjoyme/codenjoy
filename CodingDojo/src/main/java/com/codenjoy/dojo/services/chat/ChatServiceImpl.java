package com.codenjoy.dojo.services.chat;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * User: sanja
 * Date: 23.09.13
 * Time: 23:35
 */
@Component("chatService")
public class ChatServiceImpl implements ChatService {

    public static final String FLOOD_MESSAGE = "НЛО прилетело и украло ваше сообщение";
    public static final String MAX_LENGTH_MESSAGE = "НЛО прилетело и поело ваше длинное сообщение";

    public static int FLOOD_MESSAGES_COUNT = 6;
    public static int MAX_LENGTH = 200;
    public static int MESSAGES_COUNT = 160;

    List<ChatMessage> messages = new LinkedList<ChatMessage>();

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

    public class ChatReader {
        public List<ChatMessage.ChatMessageReader> getMessages() {
            List<ChatMessage.ChatMessageReader> result = new LinkedList<ChatMessage.ChatMessageReader>();
            for (ChatMessage message : messages) {
                result.add(message.new ChatMessageReader());
            }
            return result;
        }
    }

    public static class ChatBuilder {

        private List<ChatMessage> messages = new LinkedList<ChatMessage>();

        public void setMessages(List<ChatMessage.ChatMessageBuilder> mss) {
            for (ChatMessage.ChatMessageBuilder ms : mss) {
                messages.add(ms.getChatMessage());
            }
        }

        public List<ChatMessage> getMessages() {
            return messages;
        }
    }
}
