package com.codenjoy.dojo.bomberman.services;

import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.GameType;
import org.junit.Test;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.mock;

/**
 * Created by Sanja on 26.10.2014.
 */
public class BombermanPerformanceTest {

    @Test
    public void test() {
        int boardSize = 100;
        int walls = 3000;
        int meatChoppers = 3000;
        int players = 1000;
        int ticks = 2;

long time = Calendar.getInstance().getTime().getTime();

        GameType bomberman = new BombermanGame();
        bomberman.getGameSettings().getParameter("Board size").type(Integer.class).update(boardSize);
        bomberman.getGameSettings().getParameter("Destroy wall count").type(Integer.class).update(walls);
        bomberman.getGameSettings().getParameter("Meat choppers count").type(Integer.class).update(meatChoppers);

        List<Game> games = new LinkedList<Game>();
        for (int i = 0; i < players; i++) {
            games.add(bomberman.newGame(mock(EventListener.class)));
        }

System.out.println("Creation: " + (Calendar.getInstance().getTime().getTime() - time) + "ms");

        for (int i = 0; i < ticks; i++) {
time = Calendar.getInstance().getTime().getTime();
            games.get(0).tick();
            for (int j = 0; j < games.size(); j++) {
                games.get(j).getBoardAsString();
            }
System.out.println("Tick: " + (Calendar.getInstance().getTime().getTime() - time) + "ms");
        }

    }
}
