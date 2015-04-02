package com.codenjoy.dojo.sample.model;

import com.codenjoy.dojo.profile.Profiler;
import com.codenjoy.dojo.sample.services.SampleGame;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.PrinterFactory;
import com.codenjoy.dojo.services.PrinterFactoryImpl;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.mock;

/**
 * Created by Sanja on 15.02.14.
 */
public class SamplePerormanceTest {

    @Test // TODO закончить как будет настроение :)
    public void test() {
        SampleGame sampleGame = new SampleGame();

        List<Game> games = new LinkedList<Game>();

        PrinterFactory factory = new PrinterFactoryImpl();
        for (int index = 0; index < 50; index++) {
            Game game = sampleGame.newGame(mock(EventListener.class), factory);
            games.add(game);
        }

        Profiler profiler = new Profiler();

        for (Game game : games) {
            profiler.start();

            String boardAsString = game.getBoardAsString();

            profiler.done("getBoardAsString");
            profiler.print();
        }
    }
}
