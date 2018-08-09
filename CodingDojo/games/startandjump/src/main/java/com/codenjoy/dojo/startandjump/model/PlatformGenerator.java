package com.codenjoy.dojo.startandjump.model;

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

import java.util.LinkedList;
import java.util.List;

public class PlatformGenerator {
    private final Dice dice;
    private final int size;
    private int newPlatformY;
    private int newPlatformLengthLeft;
    private int maxPlatformLength;
    private int previousY;
    boolean finishedGenerationPlatform;

    public PlatformGenerator(Dice dice, int size, int maxPlatformLength) {
        this.dice = dice;
        this.size = size;
        this.maxPlatformLength = maxPlatformLength;
        this.previousY = 2;
    }

    public List<Platform> generateRandomPlatforms() {
        List<Platform> result = new LinkedList<>();
        if (finishedGenerationPlatform) {
            //LOL just for mocking
            dice.next(123);
            dice.next(123);
            dice.next(123);
            finishedGenerationPlatform = false;
        } else {
            if (newPlatformLengthLeft == 0) {
                int newY = dice.next(size-3)+1;
                int maxPlatformY = previousY + 2;
//            int minPlatformY = previousY - 2;
                if (newY > maxPlatformY) newPlatformY = maxPlatformY;
//            else if (newY < minPlatformY) newPlatformY = minPlatformY;
                else {
                    newPlatformY = newY;
                }
                newPlatformLengthLeft = dice.next(maxPlatformLength+1);
            }

            if (newPlatformLengthLeft != 0) {
                result.add(new Platform(size, newPlatformY));
                previousY = newPlatformY;
                newPlatformLengthLeft--;
                if (newPlatformLengthLeft == 0) {
                    finishedGenerationPlatform = true;
                } else {
                    finishedGenerationPlatform = false;
                }
            }
        }

        return result;
    }

    void setPreviousY(int previousY) {
        this.previousY = previousY;
    }
}
