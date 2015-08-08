package com.codenjoy.dojo.startandjump.model;

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
