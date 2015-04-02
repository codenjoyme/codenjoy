package com.codenjoy.dojo.minesweeper.model;

import com.codenjoy.dojo.services.PrinterFactoryImpl;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;
import static junit.framework.Assert.assertTrue;

public class RandomMinesGeneratorTest {

    @Test
    public void shouldMinesRandomPlacedOnBoard() {
        for (int index = 0; index < 100; index++) {
            List<Mine> mines = generate();
            List<Mine> minesAnother = generate();

            assertTrue(!mines.equals(minesAnother));
        }
    }


    private List<Mine> generate() {
        return new RandomMinesGenerator().get(10, new MockBoard());
    }

    private class MockBoard extends Minesweeper {
        public MockBoard() {
            super(v(16), v(0), v(1), new MinesGenerator() {
                public List<Mine> get(int count, Field board) {
                    return Arrays.asList();
                }
            }, null, new PrinterFactoryImpl());
            newGame();
        }
    }
}
