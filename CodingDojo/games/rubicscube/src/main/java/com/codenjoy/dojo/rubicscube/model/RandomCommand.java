package com.codenjoy.dojo.rubicscube.model;

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

import java.util.Arrays;
import java.util.List;

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
