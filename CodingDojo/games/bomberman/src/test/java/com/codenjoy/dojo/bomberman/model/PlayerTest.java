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
import com.codenjoy.dojo.services.round.RoundSettingsWrapper;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.SimpleParameter;
import org.junit.Before;
import org.junit.Test;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

/**
 * User: sanja
 * Date: 20.04.13
 * Time: 13:32
 */
public class PlayerTest {

    private EventListener listener;
    private Field field;
    private GameSettings settings;

    @Before
    public void setup() {
        settings = mock(GameSettings.class);
        when(settings.killWallScore()).thenReturn(v(10));
        when(settings.catchPerkScore()).thenReturn(v(5));
        when(settings.getLevel()).thenReturn(mock(Level.class));
        when(settings.getHero(any(Level.class))).thenReturn(mock(Hero.class));
        when(settings.getRoundSettings()).thenReturn(new RoundSettingsWrapper() {
            @Override
            public Parameter<Boolean> roundsEnabled() {
                return new SimpleParameter<>(false);
            }
        });

        field = mock(Field.class);
        when(field.settings()).thenReturn(settings);

        listener = mock(EventListener.class);
    }

    @Test
    public void shouldProcessEventWhenListenerIsNotNull() {
        Parameter<Boolean> roundsEnabled = new SimpleParameter<>(false);
        Player player = new Player(listener, roundsEnabled);
        player.newHero(field);

        player.event(Events.KILL_DESTROY_WALL);

        verify(listener).event(Events.KILL_DESTROY_WALL);
    }

    @Test
    public void shouldNotProcessEventWhenListenerNotNull() {
        Parameter<Boolean> roundsEnabled = new SimpleParameter<>(false);
        Player player = new Player(null, roundsEnabled);
        player.newHero(field);

        player.event(Events.KILL_DESTROY_WALL);

        verify(listener, never()).event(Events.KILL_DESTROY_WALL);
    }
}
