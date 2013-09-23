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
public class ChatServiceImpl implements ChatService {  // TODO потесть меня
    public static int MAX = 160;
    private List<String> messages = new LinkedList<String>();

    @Override
    public void chat(String playerName, String message) {
        messages.add(playerName + ": " + message + "\n");
    }

    @Override
    public PlayerData getChatLog() {
        StringBuffer result = new StringBuffer();
        int count = 0;
        if (!messages.isEmpty()) {
            for (int index = messages.size() - 1; index >= 0; index--) {
                if (count++ >= MAX) break;
                String message = messages.get(index);
                result.insert(0, message);
            }
        }
        return new PlayerData(StringEscapeUtils.escapeJava(result.toString()));
    }
}
