package com.codenjoy.dojo.crossyroad.model;

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


import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;

import java.util.*;

public class CarGenerator {
    private final Dice dice;
    private final int size;
    private int newPlatformY;
    private int newPlatformLengthLeft;
    private int maxCarNumber;
    private int previousY;
    boolean finishedGenerationPlatform;
    Direction newPlatformDirection;

    public CarGenerator(Dice dice, int size, int maxCarNumber) {
        this.dice = dice;
        this.size = size;
        this.maxCarNumber = maxCarNumber;
        this.previousY = 16;
    }

    public List<Car> generateRandomPlatforms(List<Stone> stones, List<Car> cars) {
        List<Car> result = new LinkedList<>();
        if (finishedGenerationPlatform) {
            //LOL just for mocking
            dice.next(123);
            dice.next(123);
            dice.next(123);
            finishedGenerationPlatform = false;
        } else {
            if (newPlatformLengthLeft == 0) {
                int dir = dice.next(10);
                if (dir >= 5)
                    newPlatformDirection = Direction.RIGHT;
                else
                    newPlatformDirection = Direction.LEFT;
                List<Integer> freeLines = new LinkedList<>();
                for (int i = previousY; i < size-1; i++) {
                    freeLines.add(i);
                }
                List<Integer> st = new LinkedList<>();
                for (Stone s : stones) {
                    st.add(s.getY());
                }
                for (Car p : cars) {
                    st.add(p.getY());
                }
                if (st.size() != 0) freeLines.removeAll(st);
                if (freeLines.size() == 0) return result;
                int index = dice.next(freeLines.size() + 1);
                newPlatformY = freeLines.get(index);
                newPlatformLengthLeft = dice.next(maxCarNumber + 1);
            }

            for (int i = 0; i < newPlatformLengthLeft; i++) {
                if (newPlatformDirection == Direction.LEFT)
                    result.add(new Car(size - dice.next(size - 1), newPlatformY, newPlatformDirection));
                else
                    result.add(new Car(dice.next(size - 1), newPlatformY, newPlatformDirection));
                finishedGenerationPlatform = true;
            }
            newPlatformLengthLeft = 0;
        }

        return result;
    }

    void setPreviousY(int previousY) {
        this.previousY = previousY;
    }
}
