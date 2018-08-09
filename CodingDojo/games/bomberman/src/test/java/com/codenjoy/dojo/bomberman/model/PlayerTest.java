package com.codenjoy.dojo.bomberman.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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


import com.codenjoy.dojo.bomberman.services.Events;
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

        player.event(Events.KILL_DESTROY_WALL);

        verify(listener).event(Events.KILL_DESTROY_WALL);
    }

    @Test
    public void shouldNotProcessEventWhenListenerNotNull() {
        GameSettings settings = mock(GameSettings.class);

        Player player = new Player(null);

        player.event(Events.KILL_DESTROY_WALL);
    }
}
