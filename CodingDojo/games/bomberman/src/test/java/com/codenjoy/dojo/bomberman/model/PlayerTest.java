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


import com.codenjoy.dojo.bomberman.TestGameSettings;
import com.codenjoy.dojo.bomberman.services.Events;
import com.codenjoy.dojo.bomberman.services.GameSettings;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Optional;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static org.mockito.Mockito.*;

public class PlayerTest {

    private EventListener listener;
    private Field field;
    private GameSettings settings;
    private Dice dice;

    @Before
    public void setup() {
        settings = spy(new TestGameSettings());
        dice = mock(Dice.class);

        when(settings.getLevel()).thenReturn(mock(Level.class));
        when(settings.getHero(any(Level.class)))
                .thenReturn(mock(Hero.class));

        field = mock(Field.class);
        when(field.settings()).thenReturn(settings);
        when(field.freeRandom()).thenReturn(Optional.of(pt(0, 0)));

        listener = mock(EventListener.class);
    }

    protected void dice(int... values) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int value : values) {
            when = when.thenReturn(value);
        }
    }

    @Test
    public void shouldProcessEventWhenListenerIsNotNull() {
        Player player = new Player(listener, settings);
        dice(0, 0);
        player.newHero(field);

        player.event(Events.KILL_DESTROY_WALL);

        verify(listener).event(Events.KILL_DESTROY_WALL);
    }

    @Test
    public void shouldNotProcessEventWhenListenerNotNull() {
        Player player = new Player(null, settings);
        dice(0, 0);
        player.newHero(field);

        player.event(Events.KILL_DESTROY_WALL);

        verify(listener, never()).event(Events.KILL_DESTROY_WALL);
    }
}
