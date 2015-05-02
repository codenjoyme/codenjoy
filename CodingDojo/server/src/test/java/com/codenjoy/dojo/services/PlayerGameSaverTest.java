package com.codenjoy.dojo.services;

import com.codenjoy.dojo.services.chat.ChatMessage;
import com.codenjoy.dojo.services.chat.ChatServiceImpl;
import com.codenjoy.dojo.services.chat.ChatServiceImplTest;
import org.apache.commons.lang.StringEscapeUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: oleksandr.baglai
 * Date: 11/15/12
 * Time: 2:07 AM
 */
public class PlayerGameSaverTest {

    private PlayerGameSaver saver;

    @Before
    public void removeAll() {
        saver = new PlayerGameSaver("target/saves.db" + new Random().nextInt());
    }

    @After
    public void cleanUp() {
        saver.removeDatabase();
    }

    @Test
    public void shouldWorks_saveLoadPlayerGame() {
        PlayerScores scores = getScores(10);
        Information info = getInfo("Some info");
        GameService gameService = getGameService(scores);
        Player player = new Player("vasia", "http://127.0.0.1:8888", PlayerTest.mockGameType("game"), scores, info, Protocol.HTTP);

        saver.saveGame(player);

        PlayerSave loaded = saver.loadGame("vasia");
        assertEqualsProperties(player, loaded);

        saver.delete("vasia");

        assertEquals("[]", saver.getSavedList().toString());
    }

    private GameType getGameType(PlayerScores scores) {
        GameType gameType = mock(GameType.class);
        when(gameType.getPlayerScores(anyInt())).thenReturn(scores);
        return gameType;
    }

    private GameService getGameService(PlayerScores scores) {
        GameService gameService = mock(GameService.class);
        GameType gameType = getGameType(scores);
        when(gameService.getGame(anyString())).thenReturn(gameType);
        return gameService;
    }

    private Information getInfo(String string) {
        Information info = mock(Information.class);
        when(info.getMessage()).thenReturn(string);
        return info;
    }

    private PlayerScores getScores(int value) {
        PlayerScores scores = mock(PlayerScores.class);
        when(scores.getScore()).thenReturn(value);
        return scores;
    }

    private void assertEqualsProperties(Player expected, PlayerSave actual) {
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getCallbackUrl(), actual.getCallbackUrl());
        assertEquals(expected.getScore(), actual.getScore());
    }

    @Test
    public void shouldWorks_saveLoadChat() {    // TODO проверить как русиш символы сохраняются
        ChatServiceImplTest.setNowDate(2013, 9, 25, 15, 3, 0);
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

        saver.saveChat(chat.getMessages());
        messages.clear();

        List<ChatMessage> chatMessages = saver.loadChat();
        chat.setMessages(chatMessages);

        assertEquals("apofig, НЛО прилетело и украло ваше сообщение\n" +
                        "[15:03] apofig: message6\n" +
                        "[15:03] apofig: message5\n" +
                        "[15:03] apofig: message4\n" +
                        "[15:03] apofig: message3\n" +
                        "[15:03] apofig: message2\n" +
                        "[15:03] apofig: message1\n",
                StringEscapeUtils.unescapeJava(chat.getChatLog()));
    }

    @Test
    public void shouldSaleOnlyLastMessages_saveLoadChat() {
        ChatServiceImplTest.setNowDate(2013, 9, 25, 15, 3, 0);
        ChatServiceImpl chat = new ChatServiceImpl();
        LinkedList<ChatMessage> messages = new LinkedList<ChatMessage>();
        chat.setMessages(messages);
        chat.chat("apofig", "message1");
        chat.chat("apofig", "message2");
        chat.chat("apofig", "message3");
        long expected = 1382702580000L;
        assertEquals(expected, messages.getLast().getTime().getTime());

        saver.saveChat(chat.getMessages());

        ChatServiceImplTest.setNowDate(2013, 9, 25, 15, 3, 1);
        chat.chat("apofig", "message4");
        chat.chat("apofig", "message5");
        chat.chat("apofig", "message6");
        chat.chat("apofig", "message7");
        assertEquals(expected + 1000, messages.getLast().getTime().getTime());

        saver.saveChat(chat.getMessages());

        List<ChatMessage> chatMessages = saver.loadChat();
        chat.setMessages(chatMessages);

        assertEquals("apofig, НЛО прилетело и украло ваше сообщение\n" +
                        "[15:03] apofig: message6\n" +
                        "[15:03] apofig: message5\n" +
                        "[15:03] apofig: message4\n" +
                        "[15:03] apofig: message3\n" +
                        "[15:03] apofig: message2\n" +
                        "[15:03] apofig: message1\n",
                StringEscapeUtils.unescapeJava(chat.getChatLog()));
    }

    @Test
    public void shouldWorks_getSavedList() {
        Player player1 = new Player("vasia", "http://127.0.0.1:8888", PlayerTest.mockGameType("game"), getScores(10), getInfo("Some other info"), Protocol.HTTP);
        Player player2 = new Player("katia", "http://127.0.0.3:7777", PlayerTest.mockGameType("game"), getScores(20), getInfo("Some info"), Protocol.WS);

        saver.saveGame(player1);
        saver.saveGame(player2);

        assertEquals("[katia, vasia]", saver.getSavedList().toString());
    }
}
