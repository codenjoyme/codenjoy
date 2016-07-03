package com.codenjoy.dojo.transport.screen;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.transport.MockAsyncContext;
import com.jayway.restassured.path.json.JsonPath;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static com.jayway.restassured.path.json.JsonPath.from;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RestScreenSenderTest {

    private RestScreenSender sender;
    private MockHttpServletResponse response;
    private MockAsyncContext asyncContext;
    private ScheduledExecutorService restSenderExecutorService = new ScheduledThreadPoolExecutor(10);

    @Before
    public void setUp() throws Exception {
        sender = new RestScreenSender(restSenderExecutorService, new FakePlayerDataSerializer());
        response = new MockHttpServletResponse();
        asyncContext = new MockAsyncContext(response);
    }

    @Test
    public void shouldSendUpdateWhenOnePlayerRequested() throws UnsupportedEncodingException {
        sender.scheduleUpdate(updateRequestFor("vasya"));

        sender.sendUpdates(screenFor("vasya", plot(1, 2, SomePlotColor.BLUE)).asMap());

        assertContainsPlayerCoordinates(response.getContentAsString(), "vasya", "blue", 1, 2);
    }

    @Test
    public void shouldCompleteResponseWhenExceptionOnWrite() throws IOException {
        response.setCharacterEncoding("NON_EXISTENT_ENCODING_FOR_IO_EXCEPTION");
        sender.scheduleUpdate(updateRequestFor("vasya"));

        sender.sendUpdates(screenFor("vasya", plot(1, 2, SomePlotColor.BLUE)).asMap());

        assertTrue(asyncContext.isComplete());
    }

    @Test
    public void shouldSendOtherUpdatesWhenExceptionOnWrite() throws UnsupportedEncodingException {
        MockHttpServletResponse exceptionResponse = new MockHttpServletResponse();
        exceptionResponse.setCharacterEncoding("NON_EXISTENT_ENCODING_FOR_IO_EXCEPTION");
        MockAsyncContext exceptionContext = new MockAsyncContext(exceptionResponse);
        sender.scheduleUpdate(new PlayerScreenUpdateRequest(exceptionContext, "exception"));
        sender.scheduleUpdate(updateRequestFor("petya"));


        sender.sendUpdates(
                screenFor("petya", plot(2, 3, SomePlotColor.BLUE))
                        .addScreenFor("exception", 123, plot(1, 3, SomePlotColor.BLUE)).asMap());

        assertContainsPlayerCoordinates(response.getContentAsString(), "petya", "blue", 2, 3);
        assertTrue(exceptionContext.isComplete());
    }

    @Test
    public void shouldRemoveRequestWhenProcessed() throws UnsupportedEncodingException {
        sender.scheduleUpdate(updateRequestFor("vasya"));
        sender.sendUpdates(screenFor("vasya", plot(1, 2, SomePlotColor.BLUE)).asMap());
        response.setWriterAccessAllowed(false);

        try {
            sender.sendUpdates(screenFor("vasya", plot(1, 2, SomePlotColor.BLUE)).asMap());
        } catch (Exception e) {
            fail("Should send only once");
        }
    }

    private void assertContainsPlayerCoordinates(String responseContent, String playerName, String color, int expectedX, int expectedY) {
        JsonPath jsonPath = from(responseContent);
        assertEquals(expectedX, jsonPath.getInt(playerName + ".plots." + color + "[0][0]"));
        assertEquals(expectedY, jsonPath.getInt(playerName + ".plots." + color + "[0][1]"));
    }

    @Test
    public void shouldSendToRequestedPlayersOnly() throws UnsupportedEncodingException {
        sender.scheduleUpdate(updateRequestFor("vasya"));

        sender.sendUpdates(
                screenFor("petya", plot(2, 3, SomePlotColor.BLUE))
                        .addScreenFor("vasya", 123, plot(1, 3, SomePlotColor.BLUE)).asMap());

        JsonPath jsonPath = from(response.getContentAsString());
        assertNull("Should contain only requested user screens", jsonPath.get("petya"));
    }

    @Test
    public void shouldIgnoreNonRequestedPlayerData() throws UnsupportedEncodingException {
        sender.scheduleUpdate(updateRequestFor("vasya"));

        sender.sendUpdates(
                screenFor("petya", plot(2, 3, SomePlotColor.BLUE)).asMap());

        assertTrue(asyncContext.isComplete());
    }

    @Test
    public void shouldSendUpdateForSeveralRequestedPlayers() throws UnsupportedEncodingException {
        sender.scheduleUpdate(updateRequestFor("vasya", "petya"));

        sender.sendUpdates(
                screenFor("petya", plot(3, 4, SomePlotColor.CYAN)).
                        addScreenFor("vasya", 123, plot(1, 2, SomePlotColor.RED)).asMap());

        assertContainsPlayerCoordinates(response.getContentAsString(), "vasya", "red", 1, 2);
        assertContainsPlayerCoordinates(response.getContentAsString(), "petya", "cyan", 3, 4);
    }

    @Test
    public void shouldSendUpdateForAllPlayersWhenRequested() throws UnsupportedEncodingException {
        sender.scheduleUpdate(new PlayerScreenUpdateRequest(asyncContext, true, null));

        sender.sendUpdates(
                screenFor("petya", plot(3, 4, SomePlotColor.CYAN)).
                        addScreenFor("vasya", 123, plot(1, 2, SomePlotColor.RED)).asMap());

        assertContainsPlayerCoordinates(response.getContentAsString(), "vasya", "red", 1, 2);
        assertContainsPlayerCoordinates(response.getContentAsString(), "petya", "cyan", 3, 4);
    }

    @Test
    public void shouldSendScores() throws UnsupportedEncodingException {
        sender.scheduleUpdate(updateRequestFor("vasya"));

        sender.sendUpdates(screenFor("vasya", 345).asMap());

        JsonPath jsonPath = from(response.getContentAsString());
        assertEquals(345, jsonPath.getInt("vasya.score"));
    }

    private SomePlot plot(int x, int y, SomePlotColor color) {
        return new SomePlot(x, y, color);
    }

    private PlayerScreenUpdateRequest updateRequestFor(String... playerName) {
        return new PlayerScreenUpdateRequest(asyncContext, false, new HashSet<String>(Arrays.asList(playerName)));
    }

    private Screen screenFor(String playerName, SomePlot... plots) {
        return new Screen(playerName, 123, plots);
    }

    private Screen screenFor(String playerName, int score, SomePlot... plots) {
        return new Screen(playerName, score, plots);
    }

    private class Screen {
        private Map<ScreenRecipient, ScreenData> map;

        public Screen(String playerName, int score, SomePlot... plots) {
            this.map = new HashMap<ScreenRecipient, ScreenData>();
            addScreenFor(playerName, score, plots);
        }

        public Screen addScreenFor(String playerName, int score, SomePlot... plots) {
            map.put(new FakePlayer(playerName),
                    new FakePlayerData(Arrays.asList(plots), score,
                            345, "", 7, "")); // dummy values
            return this;
        }

        public Map<ScreenRecipient, ScreenData> asMap() {
            return map;
        }

    }
}