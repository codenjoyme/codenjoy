package com.codenjoy.dojo.services;

import com.codenjoy.dojo.services.chat.ChatMessage;
import com.codenjoy.dojo.services.chat.ChatService;
import com.codenjoy.dojo.services.chat.ChatServiceImpl;
import com.codenjoy.dojo.services.chat.ChatServiceImplTest;
import org.apache.commons.lang.StringEscapeUtils;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: oleksandr.baglai
 * Date: 11/15/12
 * Time: 2:07 AM
 */
public class PlayerGameSaverTest {

    @Test
    public void shouldWorksSaveLoadPlayerGame() {
        PlayerScores scores = mock(PlayerScores.class);
        when(scores.getScore()).thenReturn(10);

        Information info = mock(Information.class);
        when(info.getMessage()).thenReturn("Some info");

        GameType gameType = mock(GameType.class);
        when(gameType.getPlayerScores(anyInt())).thenReturn(scores);

        Player player = new Player("vasia", "http://127.0.0.1:8888", scores, info, Protocol.HTTP);

        GameSaver saver = new PlayerGameSaver();

        saver.saveGame(player);

        Player loaded = saver.loadGame("vasia").getPlayer(gameType);
        assertEqualsProperties(player, loaded);

        saver.delete("vasia");
    }

    private void assertEqualsProperties(Player expected, Player actual) {
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getCallbackUrl(), actual.getCallbackUrl());
        assertEquals(expected.getScore(), actual.getScore());
        assertEquals(expected.getCurrentLevel(), actual.getCurrentLevel());

        String newInfo = ", Level " + (expected.getCurrentLevel() + 1);
        assertEquals(expected.getMessage(), actual.getMessage().replaceAll(newInfo, ""));
    }

    @Test
    public void shouldWorksSaveLoadChat() {    // TODO проверить как русиш символы сохраняются
        ChatServiceImplTest.setNowDate(2013, 9, 25, 3, 3, 0);
        ChatServiceImpl chat = new ChatServiceImpl();
        LinkedList<ChatMessage> messages = new LinkedList<ChatMessage>();
        chat.setMessages(messages);
        chat.chat("apofig", "message1");
        chat.chat("apofig", "message2");
        chat.chat("apofig", "message3");
        chat.chat("apofig", "message4");
        chat.chat("apofig", "message5");
        chat.chat("apofig", "message6");
        chat.chat("apofig", "message7");

        GameSaver saver = new PlayerGameSaver();

        saver.saveChat(chat);
        messages.clear();

        saver.loadChat(chat);

        assertEquals("apofig, НЛО прилетело и украло ваше сообщение\n" +
                "[03:03] apofig: message6\n" +
                "[03:03] apofig: message5\n" +
                "[03:03] apofig: message4\n" +
                "[03:03] apofig: message3\n" +
                "[03:03] apofig: message2\n" +
                "[03:03] apofig: message1\n",
                StringEscapeUtils.unescapeJava(chat.getChatLog().getBoard()));
    }
}
