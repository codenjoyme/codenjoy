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
            chat.chat("apofig" + index, "message" + index);
        }

        String log = chat.getChatLog().getBoard();
        assertEquals(
                "apofig13: message13\\n" +
                "apofig12: message12\\n" +
                "apofig11: message11\\n" +
                "apofig10: message10\\n" +
                "apofig9: message9\\n" +
                "apofig8: message8\\n" +
                "apofig7: message7\\n" +
                "apofig6: message6\\n" +
                "apofig5: message5\\n" +
                "apofig4: message4\\n", log);
    }

    @Test
    public void shouldAntiFlood() {
        ChatServiceImpl chat = new ChatServiceImpl();

        chat.chat("apofig", "1");
        chat.chat("apofig", "2");
        chat.chat("apofig", "3");
        chat.chat("apofig", "4");
        chat.chat("apofig", "5");

        String log = chat.getChatLog().getBoard();
        assertEquals("Codenjoy: \\u041D\\u043B\\u043E \\u043F\\u0440\\u0438\\u043B\\u0435\\u0442\\u0435\\u043B\\u043E \\u0438 \\u0443\\u043A\\u0440\\u0430\\u043B\\u043E \\u0432\\u0430\\u0448\\u0435 \\u0441\\u043E\\u043E\\u0431\\u0449\\u0435\\u043D\\u0438\\u0435\\n" +
                        "apofig: 3\\n" +
                        "apofig: 2\\n" +
                        "apofig: 1\\n", log);

        chat.chat("zanefig", "1");
        chat.chat("apofig", "2");

        log = chat.getChatLog().getBoard();
        assertEquals("apofig: 2\\n" +
                "zanefig: 1\\n" +
                "Codenjoy: \\u041D\\u043B\\u043E \\u043F\\u0440\\u0438\\u043B\\u0435\\u0442\\u0435\\u043B\\u043E \\u0438 \\u0443\\u043A\\u0440\\u0430\\u043B\\u043E \\u0432\\u0430\\u0448\\u0435 \\u0441\\u043E\\u043E\\u0431\\u0449\\u0435\\u043D\\u0438\\u0435\\n" +
                "apofig: 3\\n" +
                "apofig: 2\\n" +
                "apofig: 1\\n", log);
    }
}
