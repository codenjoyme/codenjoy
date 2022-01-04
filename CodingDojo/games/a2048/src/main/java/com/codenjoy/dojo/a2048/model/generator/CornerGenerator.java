package com.codenjoy.dojo.a2048.model.generator;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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


import com.codenjoy.dojo.a2048.model.*;
import com.codenjoy.dojo.a2048.model.Number;
import com.codenjoy.dojo.services.Dice;
import com.google.common.base.Supplier;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

public class CornerGenerator implements Generator {

    private Dice dice;
    private int count;

    public CornerGenerator(Dice dice, int count) {
        this.dice = dice;
        this.count = count;
    }

    @Override
    public void generate(Numbers numbers) {
        int last = numbers.size() - 1;

        List<Supplier<Boolean>> corners = new LinkedList<>();
        corners.add(() -> checkAndSet(numbers, 0, 0));
        corners.add(() -> checkAndSet(numbers, 0, last));
        corners.add(() -> checkAndSet(numbers, last, 0));
        corners.add(() -> checkAndSet(numbers, last, last));

        // TODO щось таке насерив, але працює
        IntStream.range(0, count)
                .forEach(i -> {
                    while (!corners.isEmpty()){
                        int index = dice.next(corners.size());
                        if (corners.remove(index).get()) break;
                    }
                });

    }

    private boolean checkAndSet(Numbers numbers, int x, int y) {
        boolean added = !numbers.isBusy(x, y);
        if (added) {
            numbers.add(new Number(2, x, y));
        }
        return added;
    }
}
