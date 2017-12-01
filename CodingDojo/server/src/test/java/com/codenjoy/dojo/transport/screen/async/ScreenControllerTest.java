package com.codenjoy.dojo.transport.screen.async;

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


import com.codenjoy.dojo.transport.screen.async.PlayerScreenUpdateRequest;
import com.codenjoy.dojo.transport.screen.async.RestScreenSender;
import com.codenjoy.dojo.transport.screen.async.ScreenController;
import org.fest.assertions.CollectionAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.*;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Collection;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ScreenControllerTest {
    @Mock
    private RestScreenSender screenSender;

    @Captor
    private ArgumentCaptor<PlayerScreenUpdateRequest> updateRequestCaptor;

    private ScreenController screenController;
    private MockHttpServletRequest request;

    @Before
    public void setUp() throws Exception {
        screenController = new ScreenController(screenSender);
        request = new MockHttpServletRequest30();
    }

    @Test
    public void shouldRequestUpdatesForPlayers() throws IOException, ServletException {
        request.addParameter("vasya", "vasya");

        screenController.handleRequest(request, new MockHttpServletResponse());

        verify(screenSender).scheduleUpdate(updateRequestCaptor.capture());
        assertUpdateRequestContainsPlayer("vasya");
    }

    @Test
    public void shouldRequestUpdatesForAllPlayersScreen() throws IOException, ServletException {
        request.addParameter("vasya", "vasya");
        request.addParameter("allPlayersScreen", "true");

        screenController.handleRequest(request, new MockHttpServletResponse());

        verify(screenSender).scheduleUpdate(updateRequestCaptor.capture());
        PlayerScreenUpdateRequest updateRequest = updateRequestCaptor.getValue();
        assertTrue(updateRequest.isForAllPlayers());
        assertNull(updateRequest.getPlayersToUpdate());
    }

    private CollectionAssert assertUpdateRequestContainsPlayer(String playerName) {
        return assertThat(updateRequestCaptor.getValue().getPlayersToUpdate()).contains(playerName);
    }

    private static class MockHttpServletRequest30 extends MockHttpServletRequest {

        @Override
        public Collection<Part> getParts() throws IOException, ServletException {
            return null;
        }

        @Override
        public Part getPart(String name) throws IOException, ServletException {
            return null;
        }

        @Override
        public AsyncContext startAsync() throws IllegalStateException {
            return null;
        }

        @Override
        public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
            return null;
        }

        @Override
        public boolean isAsyncStarted() {
            return false;
        }

        @Override
        public boolean isAsyncSupported() {
            return false;
        }

        @Override
        public AsyncContext getAsyncContext() {
            return null;
        }

        @Override
        public DispatcherType getDispatcherType() {
            return null;
        }
    }
}
