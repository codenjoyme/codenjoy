package com.codenjoy.dojo.services.chat;

public interface ChatPost {

    void postFieldFor(String recipientId, String text, String room);

    void postField(String text, String room);
}
