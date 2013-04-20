package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.services.EventListener;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * User: sanja
 * Date: 20.04.13
 * Time: 13:32
 */
public class PlayerTest {
    @Test
    public void shouldProcessEventWhenListenerIsNotNull() {
        GameSettings settings = mock(GameSettings.class);
        EventListener listener = mock(EventListener.class);

        Player player = new Player();
        player.init(settings, listener);

        player.event("event");

        verify(listener).event("event");
    }

    @Test
    public void shouldNotProcessEventWhenListenerNotNull() {
        GameSettings settings = mock(GameSettings.class);

        Player player = new Player();
        player.init(settings, null);

        player.event("event");
    }
}
