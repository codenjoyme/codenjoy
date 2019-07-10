package com.codenjoy.dojo.excitebike.model.items.springboard;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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

import com.codenjoy.dojo.excitebike.model.items.Shiftable;
import com.codenjoy.dojo.services.printer.CharElements;
import com.google.common.collect.Lists;

import java.util.*;

import static com.codenjoy.dojo.excitebike.model.items.springboard.SpringboardElementType.*;

public class SpringboardGenerator {
    private int x0;
    private int x;
    private Map<SpringboardElementType, List<Shiftable>> elements = new EnumMap<>(SpringboardElementType.class);

    public SpringboardGenerator(int x0, int linesNumber, int width) {
        this.x0 = x0;
        this.x = x0 + (width < 2 ? 2 : width);

        generateRiseLine(linesNumber);

        for (int i = x0 + 1; i < x - 1; i++) {
            generateSpringBoardStep(SPRINGBOARD_NONE, SPRINGBOARD_NONE, SPRINGBOARD_DARK, i, linesNumber);
        }
        generateDescentLine(linesNumber);
    }

    private void generateRiseLine(int linesNumber) {
        generateSpringBoardStep(SPRINGBOARD_LEFT_UP, SPRINGBOARD_DARK, SPRINGBOARD_LEFT_DOWN, x0, linesNumber);
    }

    private void generateDescentLine(int linesNumber) {
        generateSpringBoardStep(SPRINGBOARD_RIGHT_UP, SPRINGBOARD_LIGHT, SPRINGBOARD_RIGHT_DOWN, x - 1, linesNumber);
    }

    private void generateSpringBoardStep(SpringboardElementType up, SpringboardElementType middle, SpringboardElementType down, int x, int lines) {
        mergeNewElementToElementsMap(down, x , 1);

//        elements.get(down).add(new SpringboardElement(x, 1, down));

        int y = 2;
        for (; y < lines - 1; y++) {
            mergeNewElementToElementsMap(middle, x , y);
//            elements.get(middle).add(new SpringboardElement(x, y, middle));
        }
        mergeNewElementToElementsMap(up, x, y);

//        elements.get(up).add(new SpringboardElement(x, y, up));
    }

    private void mergeNewElementToElementsMap(SpringboardElementType type, int x, int y) {
        elements.merge(type,
                new LinkedList<>(Lists.newArrayList(new SpringboardElement(x, y, type))),
                (shiftables, shiftables2) -> {
                    shiftables.addAll(shiftables2);
                    return shiftables;
                });
    }

//
//    public List<SpringboardElement> getNextStep() {
//        return elements.isEmpty() ? Collections.emptyList() : elements.remove(0);
//    }

    public Map<? extends CharElements, List<Shiftable>> getElements() {
        return elements;
    }
}
