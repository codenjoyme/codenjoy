package com.codenjoy.dojo.minesweeper.model;

import com.codenjoy.dojo.services.PrinterFactoryImpl;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

public class RandomMinesGeneratorTest {

    @Test
    public void shouldMinesRandomPlacedOnBoard() {
        for (int index = 0; index < 100; index++) {
            List<Mine> mines = generate();
            List<Mine> minesAnother = generate();

            assertTrue(!mines.equals(minesAnother));
        }
    }

    @Test
    public void hasTwoMinesAtSamePlace() {
        for (int index = 0; index < 100; index++) {
            List<Mine> mines = generate();
            for (int i = 0; i < mines.size() - 1; i++) {
                Mine first = mines.get(i);
                for (int j = i + 1; j < mines.size(); j++) {
                    Mine second = mines.get(j);
                    if (first.getX() == second.getX() && first.getY() == second.getY()) {
                        System.out.println(Arrays.toString(mines.toArray()));
                        System.out.println("[" + first.getX() + "," + first.getY() + "] repeats");
                        fail();
                    }
                }
            }
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
