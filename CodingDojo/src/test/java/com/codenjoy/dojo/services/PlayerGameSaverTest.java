package com.codenjoy.dojo.services;

import com.codenjoy.dojo.services.chat.ChatMessage;
import com.codenjoy.dojo.services.chat.ChatServiceImpl;
import com.codenjoy.dojo.services.chat.ChatServiceImplTest;
import org.apache.commons.lang.StringEscapeUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;

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
        cleanUpResources();
    }

    @After
    public void cleanUp() {
        cleanUpResources();
    }

    @Test
    public void shouldWorks_saveLoadPlayerGame() {
        PlayerScores scores = getScores(10);
        Information info = getInfo("Some info");
        GameService gameService = getGameService(scores);
        Player player = new Player("vasia", "password", "http://127.0.0.1:8888", "game", scores, info, Protocol.HTTP);

        saver.saveGame(player);

        Player loaded = saver.loadGame("vasia").getPlayer(gameService);
        assertEqualsProperties(player, loaded);

        saver.delete("vasia");

        assertEquals("[]", saver.getSavedList().toString());
    }

    private void cleanUpResources() {
        saver = new PlayerGameSaver();

        for (String string : saver.getSavedList()) {
            saver.delete(string);
        }

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

    private void assertEqualsProperties(Player expected, Player actual) {
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getCallbackUrl(), actual.getCallbackUrl());
        assertEquals(expected.getScore(), actual.getScore());
        assertEquals(expected.getCurrentLevel(), actual.getCurrentLevel());

        String newInfo = ", Level " + (expected.getCurrentLevel() + 1);
        assertEquals(expected.getMessage(), actual.getMessage().replaceAll(newInfo, ""));
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

        saver.saveChat(chat);
        messages.clear();

        saver.loadChat(chat);

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
        Player player1 = new Player("vasia", "password", "http://127.0.0.1:8888", "game", getScores(10), getInfo("Some other info"), Protocol.HTTP);
        Player player2 = new Player("katia", "qweqwe", "http://127.0.0.3:7777", "game", getScores(20), getInfo("Some info"), Protocol.WS);

        saver.saveGame(player1);
        saver.saveGame(player2);

        assertEquals("[katia, vasia]", saver.getSavedList().toString());
    }
}
