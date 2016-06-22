package com.codenjoy.dojo.services.playerdata;

import com.codenjoy.dojo.transport.screen.ScreenData;

/**
 * Created by oleksandr.baglai on 04.11.2015.
 */
public class ChatLog implements ScreenData {

    private String messages;

    public ChatLog(String messages) {
        this.messages = messages;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "ChatLog:" + messages;
    }
}
