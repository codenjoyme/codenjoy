package com.codenjoy.dojo.fifteen.model;

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


import com.codenjoy.dojo.games.fifteen.Element;
import com.codenjoy.dojo.services.Dice;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.games.fifteen.Element.HERO;

public class Randomizer {

    private static final String TEMPLATE =
            "******" +
            "*%s*" +
            "*%s*" +
            "*%s*" +
            "*%s*" +
            "******";

    private Dice dice;

    public String getRandomMap(Dice dice) {
        this.dice = dice;

        String random = getRandomElements();

        return String.format(TEMPLATE,
                random.substring(0, 4),
                random.substring(4, 8),
                random.substring(8, 12),
                random.substring(12, 16));
    }

    private String getRandomElements() {
        StringBuilder result = new StringBuilder();

        List<Element> random = getRandomList();

        for (int i = 0; i < random.size(); i++) {
            result.append(random.get(i));
        }

        return result.toString();
    }

    private List<Element> getRandomList() {
        List<Element> result = new LinkedList<>();
        result.addAll(Arrays.asList(DigitHandler.DIGITS));
        result.add(HERO);

        do {
            shuffle(result);
        } while (!canBeSolved(result));

        return result;
    }

    private void shuffle(List<Element> result) {
        for (int i = result.size(); i > 1; i--)
            Collections.swap(result, i - 1, dice.next(i));
    }

    public boolean canBeSolved(List<Element> result) {
        int sum = 0;
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i) == HERO) {
                sum += 1 + i / (int) Math.sqrt(result.size());
                continue;
            }
            for (int j = i + 1; j < result.size(); j++) {
                if (result.get(j) == HERO) {
                    continue;
                }
                if (result.get(j).ch() < result.get(i).ch()) {
                    sum++;
                }
            }
        }
        return (sum & 1) == 0;
    }
}
