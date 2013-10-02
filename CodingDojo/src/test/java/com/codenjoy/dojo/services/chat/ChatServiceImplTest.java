package com.codenjoy.dojo.services.chat;

import com.codenjoy.dojo.services.chat.ChatMessage;
import com.codenjoy.dojo.services.chat.ChatServiceImpl;
import com.codenjoy.dojo.services.chat.DateCalendar;
import org.apache.commons.lang.StringEscapeUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

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
        setNowDate(2012, 12, 12, 13, 0, 0);
        chat = new ChatServiceImpl();
        chat.messages.clear();
    }

    @Test
    public void shouldInitMessage() {
        chat = new ChatServiceImpl();
        assertLog(
                "Codenjoy, НЛО прилетело и украло ваше сообщение\n" +
                "[13:00] Codenjoy: 7-е подряд сообщение - пропадает! Например ты никогда не узнаешь, что я тут написал дальше...\n" +
                "[13:00] Codenjoy: Так же не стоит флудить - это некрасиво по отношению к окружающим. Подряд можно послать только 5 сообщений, а потом ждать ответа.\n" +
                "[13:00] Codenjoy: Иначе оно будет обрезаться обрезаться обрезаться обрезаться обрезаться обрезаться обрезаться обрезаться обрезаться обрезаться обрезаться обрезаться обрезаться обрезаться обрезаться обрезаться обрезать...\n" +
                "Codenjoy, НЛО прилетело и поело ваше длинное сообщение\n" +
                "[13:00] Codenjoy: Каждое твое сообщение не должно быть более чем 200 символов в длинну.\n" +
                "[13:00] Codenjoy: <span style=\"color:red\">Свои</span> <span style=\"color:green\">сообщения</span> <span style=\"color:blue\">можно</span> <span style=\"color:pink\">разукрасить</span>, если знаешь HTML/CSS.\n" +
                "[13:00] Codenjoy: Теперь во время игры можно общаться!\n");
    }

    @Test
    public void shouldEscape() {
        chat.chat("apofig", "message1");
        chat.chat("apofig", "сообщение2");
        chat.chat("apofig", "☺3");

        assertLog("[13:00] apofig: ☺3\n" +
                "[13:00] apofig: сообщение2\n" +
                "[13:00] apofig: message1\n");
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

        assertLog("[13:00] apofig13: message13\n" +
                "[13:00] apofig12: message12\n" +
                "[13:00] apofig11: message11\n" +
                "[13:00] apofig10: message10\n" +
                "[13:00] apofig9: message9\n" +
                "[13:00] apofig8: message8\n" +
                "[13:00] apofig7: message7\n" +
                "[13:00] apofig6: message6\n" +
                "[13:00] apofig5: message5\n" +
                "[13:00] apofig4: message4\n");

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
                "[13:00] apofig: 6\n" +
                "[13:00] apofig: 5\n" +
                "[13:00] apofig: 4\n" +
                "[13:00] apofig: 3\n" +
                "[13:00] apofig: 2\n" +
                "[13:00] apofig: 1\n");

        chat.chat("zanefig", "1");
        chat.chat("apofig", "2");

        assertLog("[13:00] apofig: 2\n" +
                "[13:00] zanefig: 1\n" +
                "apofig, " + ChatServiceImpl.FLOOD_MESSAGE + "\n" +
                "[13:00] apofig: 6\n" +
                "[13:00] apofig: 5\n" +
                "[13:00] apofig: 4\n" +
                "[13:00] apofig: 3\n" +
                "[13:00] apofig: 2\n" +
                "[13:00] apofig: 1\n");


    }

    @Test
    public void shouldCutLongMessage() {
        int old = ChatServiceImpl.MAX_LENGTH;
        ChatServiceImpl.MAX_LENGTH = 20;

        chat.chat("apofig", "first message");
        chat.chat("apofig", "123456789012345678901234567890");

        assertLog("[13:00] apofig: 12345678901234567890...\n" +
                "apofig, " + ChatServiceImpl.MAX_LENGTH_MESSAGE + "\n" +
                "[13:00] apofig: first message\n");

        ChatServiceImpl.MAX_LENGTH = old;
    }

    @Test
    public void shouldSeveralDaysAgoMessage() {
        DateCalendar old = ChatMessage.calendar;

        setNowDate(2012, 12, 12, 13, 0, 0);
        chat.chat("apofig", "first message");

        setNowDate(2012, 12, 16, 13, 0, 0);
        chat.chat("apofig", "second message");

        setNowDate(2012, 12, 18, 13, 0, 0);
        assertLog("[2 days ago, 13:00] apofig: second message\n" +
                "[6 days ago, 13:00] apofig: first message\n");

        ChatMessage.calendar = old;
    }

    public static void setNowDate(final int year, final int month, final int date,
                            final int hour, final int minute, final int second) {
        ChatMessage.calendar = new DateCalendar() {
            @Override
            public Date now() {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DATE, date);
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, second);
                return calendar.getTime();
            }
        };
    }

    @Test
    public void shouldAntiFloodWorksIgnoreSystemMessages() {
        int old = ChatServiceImpl.MAX_LENGTH;
        ChatServiceImpl.MAX_LENGTH = 3;

        chat.chat("apofig", "1111");
        chat.chat("apofig", "2222");
        chat.chat("apofig", "3333");
        chat.chat("apofig", "4444");
        chat.chat("apofig", "5555");
        chat.chat("apofig", "6666");
        chat.chat("apofig", "7777");
        chat.chat("apofig", "8888");

        assertLog("apofig, " + ChatServiceImpl.FLOOD_MESSAGE + "\n" +
                "[13:00] apofig: 666...\n" +
                "apofig, " + ChatServiceImpl.MAX_LENGTH_MESSAGE + "\n" +
                "[13:00] apofig: 555...\n" +
                "apofig, " + ChatServiceImpl.MAX_LENGTH_MESSAGE + "\n" +
                "[13:00] apofig: 444...\n" +
                "apofig, " + ChatServiceImpl.MAX_LENGTH_MESSAGE + "\n" +
                "[13:00] apofig: 333...\n" +
                "apofig, " + ChatServiceImpl.MAX_LENGTH_MESSAGE + "\n" +
                "[13:00] apofig: 222...\n" +
                "apofig, " + ChatServiceImpl.MAX_LENGTH_MESSAGE + "\n" +
                "[13:00] apofig: 111...\n" +
                "apofig, " + ChatServiceImpl.MAX_LENGTH_MESSAGE + "\n");

        ChatServiceImpl.MAX_LENGTH = old;
    }

    @Test
    public void shouldAntiFloodOnlyForOneUser() {
        chat.chat("apofig", "1");
        chat.chat("apofig", "2");
        chat.chat("apofig", "3");
        chat.chat("apofig", "4");
        chat.chat("apofig", "5");
        chat.chat("apofig", "6");
        chat.chat("apofig", "7");
        chat.chat("apofig", "8");

        assertLog("apofig, " + ChatServiceImpl.FLOOD_MESSAGE + "\n" +
                "[13:00] apofig: 6\n" +
                "[13:00] apofig: 5\n" +
                "[13:00] apofig: 4\n" +
                "[13:00] apofig: 3\n" +
                "[13:00] apofig: 2\n" +
                "[13:00] apofig: 1\n");

        chat.chat("zanefig", "1");
        chat.chat("zanefig", "2");
        chat.chat("zanefig", "3");
        chat.chat("zanefig", "4");
        chat.chat("zanefig", "5");
        chat.chat("zanefig", "6");
        chat.chat("zanefig", "7");
        chat.chat("zanefig", "8");

        assertLog("zanefig, НЛО прилетело и украло ваше сообщение\n" +
                "[13:00] zanefig: 6\n" +
                "[13:00] zanefig: 5\n" +
                "[13:00] zanefig: 4\n" +
                "[13:00] zanefig: 3\n" +
                "[13:00] zanefig: 2\n" +
                "[13:00] zanefig: 1\n" +
                "apofig, НЛО прилетело и украло ваше сообщение\n" +
                "[13:00] apofig: 6\n" +
                "[13:00] apofig: 5\n" +
                "[13:00] apofig: 4\n" +
                "[13:00] apofig: 3\n" +
                "[13:00] apofig: 2\n" +
                "[13:00] apofig: 1\n");


    }
}
