package com.codenjoy.dojo.transport.screen;

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

/**
 * User: serhiy.zelenin
 * Date: 9/26/13
 * Time: 3:02 PM
 */
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
