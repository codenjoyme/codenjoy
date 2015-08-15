package com.codenjoy.dojo.spacerace.model;

import com.codenjoy.dojo.profile.Profiler;
import com.codenjoy.dojo.spacerace.services.GameRunner;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.PrinterFactory;
import com.codenjoy.dojo.services.PrinterFactoryImpl;
import org.junit.Test;

import java.util.*;

import static org.mockito.Mockito.mock;

/**
 * Created by Sanja on 15.02.14.
 * 118 игр. Результат 0,2 сек.
 * Больше с текущим полем нельзя. Поскольку игроки создаются
 * в нижней части поля, а в одной ячейке - не больше 7 объектов,
 * то при 119 уже вылетает ошибка (ячейки переполняются).
 */
public class SpaceracePerformanceTest {

    @Test // TODO закончить как будет настроение :)
    public void test() {
        GameRunner sampleGame = new GameRunner();

        List<com.codenjoy.dojo.services.Game> games = new LinkedList();

        PrinterFactory factory = new PrinterFactoryImpl();
        long f = System.currentTimeMillis();

        for (int index = 0; index < 118; index++) {
            com.codenjoy.dojo.services.Game game = sampleGame.newGame(mock(EventListener.class), factory);
            games.add(game);
        }
        long s = System.currentTimeMillis();
        System.out.println(s-f);
        Profiler profiler = new Profiler();

        for (com.codenjoy.dojo.services.Game game : games) {
//            profiler.start();
            String boardAsString = game.getBoardAsString();
//            System.out.println(boardAsString);
//
//            profiler.done("getBoardAsString");
//            profiler.print();
        }
        long t = System.currentTimeMillis();
        System.out.println(t-f);
    }
}
