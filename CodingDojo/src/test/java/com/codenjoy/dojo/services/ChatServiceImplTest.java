package com.codenjoy.dojo.services;

import org.apache.commons.lang.StringEscapeUtils;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * User: sanja
 * Date: 23.09.13
 * Time: 23:39
 */
public class ChatServiceImplTest {

    private ChatServiceImpl chat;

    @Before
    public void setup() {
        chat = new ChatServiceImpl();
        chat.messages.clear();
    }

    @Test
    public void shouldInitMessage() {
        chat = new ChatServiceImpl();
        assertLog(
                "Codenjoy, НЛО прилетело и украло ваше сообщение\n" +
                "Codenjoy: Так же не стоит флудить - это некрасиво по отношению к окружающим. Подряд можно послать только 5 сообщений, а потом ждать ответа.\n" +
                "Codenjoy: Иначе оно будет обрезаться обрезаться обрезаться обрезаться обрезаться обрезаться обрезаться обрезаться обрезаться обрезаться обрезаться обрезаться обрезаться обрезаться обрезаться обрезаться обрезать...\n" +
                "Codenjoy, НЛО прилетело и поело ваше длинное сообщение\n" +
                "Codenjoy: Каждое твое сообщение не должно быть более чем 200 символов в длинну.\n" +
                "Codenjoy: <span style=\"color:red\">Свои</span> <span style=\"color:green\">сообщения</span> <span style=\"color:blue\">можно</span> <span style=\"color:pink\">разукрасить</span>, если знаешь HTML/CSS.\n" +
                "Codenjoy: Теперь во время игры можно общаться!\n");
    }

    @Test
    public void shouldEscape() {
        chat.chat("apofig", "message1");
        chat.chat("apofig", "сообщение2");
        chat.chat("apofig", "☺3");

        assertLog("apofig: ☺3\n" +
                "apofig: сообщение2\n" +
                "apofig: message1\n");
    }

    private void assertLog(String expected) {
        String log = StringEscapeUtils.unescapeJava(chat.getChatLog().getBoard());
        assertEquals(expected, log);
    }

    @Test
    public void shouldOnly100Messages() {
        int old = ChatServiceImpl.MESSAGES_COUNT;
        ChatServiceImpl.MESSAGES_COUNT = 10;

        for (int index = 1; index <= 13; index++) {
            chat.chat("apofig" + index, "message" + index);
        }

        assertLog("apofig13: message13\n" +
                "apofig12: message12\n" +
                "apofig11: message11\n" +
                "apofig10: message10\n" +
                "apofig9: message9\n" +
                "apofig8: message8\n" +
                "apofig7: message7\n" +
                "apofig6: message6\n" +
                "apofig5: message5\n" +
                "apofig4: message4\n");

        ChatServiceImpl.MESSAGES_COUNT = old;
    }

    @Test
    public void shouldAntiFlood() {
        chat.chat("apofig", "1");
        chat.chat("apofig", "2");
        chat.chat("apofig", "3");
        chat.chat("apofig", "4");
        chat.chat("apofig", "5");
        chat.chat("apofig", "6");
        chat.chat("apofig", "7");
        chat.chat("apofig", "8");

        assertLog("apofig, " + ChatServiceImpl.FLOOD_MESSAGE + "\n" +
                "apofig: 6\n" +
                "apofig: 5\n" +
                "apofig: 4\n" +
                "apofig: 3\n" +
                "apofig: 2\n" +
                "apofig: 1\n");

        chat.chat("zanefig", "1");
        chat.chat("apofig", "2");

        assertLog("apofig: 2\n" +
                "zanefig: 1\n" +
                "apofig, " + ChatServiceImpl.FLOOD_MESSAGE + "\n" +
                "apofig: 6\n" +
                "apofig: 5\n" +
                "apofig: 4\n" +
                "apofig: 3\n" +
                "apofig: 2\n" +
                "apofig: 1\n");
    }

    @Test
    public void shouldCutLongMessage() {
        int old = ChatServiceImpl.MAX_LENGTH;
        ChatServiceImpl.MAX_LENGTH = 20;

        chat.chat("apofig", "first message");
        chat.chat("apofig", "123456789012345678901234567890");

        assertLog("apofig: 12345678901234567890...\n" +
                "apofig, " + ChatServiceImpl.MAX_LENGTH_MESSAGE + "\n" +
                "apofig: first message\n");

        ChatServiceImpl.MAX_LENGTH = old;
    }
}
