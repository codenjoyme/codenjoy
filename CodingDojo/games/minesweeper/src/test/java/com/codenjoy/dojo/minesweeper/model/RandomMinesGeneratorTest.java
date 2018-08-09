package com.codenjoy.dojo.minesweeper.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.RandomDice;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

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
        return new RandomMinesGenerator(new RandomDice()).get(10, new MockBoard());
    }

    private class MockBoard extends Minesweeper {
        public MockBoard() {
            super(v(16), v(0), v(1), new MinesGenerator() {
                public List<Mine> get(int count, Field board) {
                    return Arrays.asList();
                }
            });
            newGame(new Player(mock(EventListener.class)));
        }
    }
}
