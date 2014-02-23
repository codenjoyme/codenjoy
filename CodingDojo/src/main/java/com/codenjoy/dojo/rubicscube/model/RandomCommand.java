package com.codenjoy.dojo.rubicscube.model;

import com.codenjoy.dojo.services.Dice;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Sanja on 22.02.14.
 */
public class RandomCommand {
    private Dice dice;
    private List<String> parts = Arrays.asList(
            "B", "B2", "B'", "D", "D2", "D'",
            "F", "F2", "F'", "L", "L2", "L'",
            "R", "R2", "R'", "U", "U2", "U'");

    public RandomCommand(Dice dice) {
        this.dice = dice;
    }

    public String next() {
        int count = dice.next(100);

        String result = "";
        for (int i = 0; i < count; i++) {
            int command = dice.next(parts.size());

            result += parts.get(command);
        }
        return result;
    }
}
