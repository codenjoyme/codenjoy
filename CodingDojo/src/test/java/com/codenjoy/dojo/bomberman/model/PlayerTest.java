package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.bomberman.services.BombermanEvents;
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

        Player player = new Player(listener);

        player.event(BombermanEvents.KILL_DESTROY_WALL);

        verify(listener).event(BombermanEvents.KILL_DESTROY_WALL);
    }

    @Test
    public void shouldNotProcessEventWhenListenerNotNull() {
        GameSettings settings = mock(GameSettings.class);

        Player player = new Player(null);

        player.event(BombermanEvents.KILL_DESTROY_WALL);
    }
}
