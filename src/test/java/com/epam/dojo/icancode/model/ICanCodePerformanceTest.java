package com.epam.dojo.icancode.model;

import com.codenjoy.dojo.profile.Profiler;
import com.codenjoy.dojo.services.Game;
import com.epam.dojo.icancode.services.GameRunner;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.PrinterFactory;
import com.codenjoy.dojo.services.PrinterFactoryImpl;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.mock;

/**
 * Created by Sanja on 15.02.14.
 */
public class ICanCodePerformanceTest {

    @Test // TODO закончить как будет настроение :)
    public void test() {
        GameRunner iCanCode = new GameRunner();

        List<com.codenjoy.dojo.services.Game> games = new LinkedList<com.codenjoy.dojo.services.Game>();

        PrinterFactory factory = new PrinterFactoryImpl();
        for (int index = 0; index < 50; index++) {
            Game game = iCanCode.newGame(mock(EventListener.class), factory);
            games.add(game);
        }

        Profiler profiler = new Profiler();

        profiler.start();
        for (Game game : games) {
            String boardAsString = game.getBoardAsString();
            profiler.done("getBoardAsString");
        }
        profiler.print();
    }
}
