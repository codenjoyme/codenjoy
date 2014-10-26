package com.codenjoy.dojo.bomberman.services;

import com.apofig.profiler.Profiler;
import com.codenjoy.dojo.bomberman.model.BombermanPrinter;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.Printer;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.mock;

/**
 * Created by Sanja on 26.10.2014.
 */
public class BombermanPerformanceTest {

    @Test
    public void test() {
        int boardSize = 200;
        int walls = 3000;
        int meatChoppers = 3000;
        int players = 1000;
        int ticks = 1;
Profiler p = new Profiler();
p.start();

        GameType bomberman = new BombermanGame();
        bomberman.getGameSettings().getParameter("Board size").type(Integer.class).update(boardSize);
        bomberman.getGameSettings().getParameter("Destroy wall count").type(Integer.class).update(walls);
        bomberman.getGameSettings().getParameter("Meat choppers count").type(Integer.class).update(meatChoppers);

        List<Game> games = new LinkedList<Game>();
        for (int i = 0; i < players; i++) {
            games.add(bomberman.newGame(mock(EventListener.class)));
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

        Printer.p.print();
    }
}
