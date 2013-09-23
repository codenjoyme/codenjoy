package com.codenjoy.dojo.services;

import com.codenjoy.dojo.services.playerdata.PlayerData;
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

    private static final String FLOOD_MESSSAGE = "Нло прилетело и украло ваше сообщение";
    public static final int FLOOD_MESSAGES_COUNT = 3;
    public static int MAX = 160;

    private List<String> messages = new LinkedList<String>();

    @Override
    public void chat(String playerName, String message) {
        if (isFlood(playerName)) {
            if (messages.isEmpty() || !lastFlood()) {
                add("Codenjoy", FLOOD_MESSSAGE);
            }
        } else {
            add(playerName, message);
        }
    }

    private void add(String playerName, String message) {
        messages.add(playerName + ": " + message + "\n");
    }

    private boolean lastFlood() {
        return messages.get(messages.size() - 1).contains(FLOOD_MESSSAGE);
    }

    private boolean isFlood(String playerName) {
        if (messages.size() < FLOOD_MESSAGES_COUNT) return false;
        if (lastFlood()) {
            if (messages.get(messages.size() - 2).startsWith(playerName)) {
                return true;
            }
        }

        int count = 0;
        for (int index = 3; index > 0; index--) {
            if (messages.get(messages.size() - index).startsWith(playerName))  {
                count++;
            }
        }
        return (count == 3);
    }

    @Override
    public PlayerData getChatLog() {
        StringBuffer result = new StringBuffer();
        int count = 0;
        if (!messages.isEmpty()) {
            for (int index = messages.size() - 1; index >= 0; index--) {
                if (count++ >= MAX) break;
                String message = messages.get(index);
                result.append(message);
            }
        }
        return new PlayerData(StringEscapeUtils.escapeJava(result.toString()));
    }

}
