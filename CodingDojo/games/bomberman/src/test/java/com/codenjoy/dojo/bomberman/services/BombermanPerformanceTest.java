package com.codenjoy.dojo.bomberman.services;

import com.codenjoy.dojo.profile.Profiler;
import com.codenjoy.dojo.services.*;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class BombermanPerformanceTest {

    @Test
    public void test() {
        int boardSize = 100;
        int walls = 600;
        int meatChoppers = 100;
        int players = 100;
        int ticks = 1;

        Profiler p = new Profiler();
        p.start();

        GameType bomberman = new GameRunner();
        bomberman.getSettings().getParameter("Board size").type(Integer.class).update(boardSize);
        bomberman.getSettings().getParameter("Destroy wall count").type(Integer.class).update(walls);
        bomberman.getSettings().getParameter("Meat choppers count").type(Integer.class).update(meatChoppers);

        PrinterFactory factory = new PrinterFactoryImpl();

        List<com.codenjoy.dojo.services.Game> games = new LinkedList<com.codenjoy.dojo.services.Game>();
        for (int i = 0; i < players; i++) {
            games.add(bomberman.newGame(mock(EventListener.class), factory));
        }

        p.done("creation");


        for (int i = 0; i < ticks; i++) {
            games.get(0).tick();
            p.done("tick");

            for (int j = 0; j < games.size(); j++) {
                games.get(j).getBoardAsString();
            }
            p.done("print");
        }

        p.print();

        assertLess(p.get("creation"), 1000);
        assertLess(p.get("print"), 400);
        assertLess(p.get("tick"), 400);

    }

    private void assertLess(long actual, int expected) {
        assertTrue(actual + " > " + expected, actual < expected);
    }
}
