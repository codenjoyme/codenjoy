package com.codenjoy.dojo.services;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * User: sanja
 * Date: 23.09.13
 * Time: 23:39
 */
public class ChatServiceImplTest {

    @Test
    public void shouldEscape() {
        ChatServiceImpl chat = new ChatServiceImpl();

        chat.chat("apofig", "message1");
        chat.chat("apofig", "сообщение2");
        chat.chat("apofig", "☺3");

        String log = chat.getChatLog().getBoard();
        assertEquals(
                "apofig: \\u263A3\\n" +
                "apofig: \\u0441\\u043E\\u043E\\u0431\\u0449\\u0435\\u043D\\u0438\\u04352\\n" +
                "apofig: message1\\n", log);
    }

    @Test
    public void shouldOnly100Messages() {
        ChatServiceImpl.MAX = 10;
        ChatServiceImpl chat = new ChatServiceImpl();

        for (int index = 1; index <= 13; index ++) {
            chat.chat("apofig", "message" + index);
        }

        String log = chat.getChatLog().getBoard();
        assertEquals(
                "apofig: message13\\n" +
                "apofig: message12\\n" +
                "apofig: message11\\n" +
                "apofig: message10\\n" +
                "apofig: message9\\n" +
                "apofig: message8\\n" +
                "apofig: message7\\n" +
                "apofig: message6\\n" +
                "apofig: message5\\n" +
                "apofig: message4\\n", log);
    }
}
