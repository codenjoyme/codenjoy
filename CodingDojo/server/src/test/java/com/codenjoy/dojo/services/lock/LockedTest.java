package com.codenjoy.dojo.services.lock;

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


import com.codenjoy.dojo.services.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class LockedTest {

    public static final int WAIT = 200;
    private GameType gameType;
    private List<String> messages;

    @Before
    public void setup() {
        messages = new LinkedList<String>();

        gameType = mock(GameType.class);
        Game game = mock(Game.class);
        Joystick joystick = mock(Joystick.class);

        when(gameType.newGame(any(EventListener.class), any(PrinterFactory.class), any(String.class))).thenReturn(game);
        when(game.getJoystick()).thenReturn(joystick);

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                messages.add("joystick.act()");
                return null;
            }
        }).when(joystick).act();

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                messages.add("started game.tick()");
                sleep(WAIT);
                messages.add("finished game.tick()");
                return null;
            }
        }).when(game).tick();
    }

    @Test
    public void testRealObject() {
        final Game game = gameType.newGame(null, null, null);

        run(new Runnable() {
            @Override
            public void run() {
                game.tick();
            }
        });
        sleep(WAIT / 3);
        game.getJoystick().act();
        sleep(WAIT);

        assertEquals("[started game.tick(), joystick.act(), finished game.tick()]", messages.toString());
    }

    @Test
    public void testLockedObject() {
        final Game game = new LockedGameType(gameType).newGame(null, null, null);

        run(new Runnable() {
            @Override
            public void run() {
                game.tick();
            }
        });
        sleep(WAIT / 3);
        game.getJoystick().act();
        sleep(WAIT);

        assertEquals("[started game.tick(), finished game.tick(), joystick.act()]", messages.toString());
    }

    private void run(Runnable r) {
        (new Thread(r)).start();
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
