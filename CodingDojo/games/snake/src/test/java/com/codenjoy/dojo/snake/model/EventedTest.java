package com.codenjoy.dojo.snake.model;

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


import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.snake.model.Hero;
import com.codenjoy.dojo.snake.model.Evented;
import com.codenjoy.dojo.snake.services.Events;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

/**
 * User: oleksandr.baglai
 * Date: 10/1/12
 * Time: 3:30 AM
 */
@RunWith(MockitoJUnitRunner.class)
public class EventedTest {

    @Mock
    private EventListener listener;
    private Hero snake;

    @Before
    public void setup() {
        snake = new Evented(listener, 0, 0);
    }

    @Test
    public void shouldListenerHearWhenSnakeEatApple() {
        snake.grow();

        verify(listener).event(Events.EAT_APPLE);
    }

    @Test
    public void shouldListenerHearWhenSnakeEatStone() {
        snake.eatStone();

        verify(listener).event(Events.EAT_STONE);
    }

    @Test
    public void shouldListenerHearWhenSnakeIsDead() {
        snake.killMe();

        verify(listener).event(Events.KILL);
    }
}
