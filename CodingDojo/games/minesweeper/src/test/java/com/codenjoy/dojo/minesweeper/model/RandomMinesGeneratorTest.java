package com.codenjoy.dojo.minesweeper.model;

import com.codenjoy.dojo.services.Point;
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
    public void hasOnlyOneMineAtSamePlace() {
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

    @Test
    public void hasNoMinesInSafeArea() {
        for (int index = 0; index < 100; index++) {
            List<Mine> mines = generate();
            for (int i = 0; i < mines.size(); i++) {
                if (isInSafeArea(mines.get(i))) {
                    System.out.println(Arrays.toString(mines.toArray()));
                    System.out.println("[" + mines.get(i).getX() + "," + mines.get(i).getY() + "] is in safe area");

                    fail();
                }
            }
        }
    }

    private boolean isInSafeArea(Point point) {
        return point.getX() >= RandomMinesGenerator.SAFE_AREA_X_0 && point.getX() <= RandomMinesGenerator.SAFE_AREA_X_1
                && point.getY() >= RandomMinesGenerator.SAFE_AREA_Y_0 && point.getY() <= RandomMinesGenerator.SAFE_AREA_Y_1;
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
