package com.codenjoy.dojo.services.lock;

import com.codenjoy.dojo.services.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.LinkedList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
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

        when(gameType.newGame(any(EventListener.class), any(PrinterFactory.class))).thenReturn(game);
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
        final Game game = gameType.newGame(null, null);

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
        final Game game = new LockedGameType(gameType).newGame(null, null);

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
