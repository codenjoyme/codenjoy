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
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.excitebike.model.items.springboard.SpringboardElementType.*;

public class Springboard extends PointImpl implements Shiftable {
    private int x0;
    private List<SpringboardElement> riseElements = new LinkedList<>();
    private List<SpringboardElement> betweenElements = new LinkedList<>();
    private List<SpringboardElement> descentElements = new LinkedList<>();

    public Springboard(int x, int linesNumber, int width) {
        super(x + (width < 2 ? 2 : width), linesNumber);
        x0 = x;

        riseElements.addAll(getRiseLine(linesNumber));

        for (int i = x0 + 1; i < getX() - 1; i++) {
            betweenElements.addAll(generateSpringBoardLine(SPRINGBOARD_NONE, SPRINGBOARD_NONE, SPRINGBOARD_DARK, i, linesNumber));
        }

        descentElements.addAll(getDescentLine(linesNumber));
    }

    private List<SpringboardElement> getRiseLine(int linesNumber) {
        return generateSpringBoardLine(SPRINGBOARD_LEFT_UP, SPRINGBOARD_DARK, SPRINGBOARD_LEFT_DOWN, x0, linesNumber);
    }

    private List<SpringboardElement> getDescentLine(int linesNumber) {
        return generateSpringBoardLine(SPRINGBOARD_RIGHT_UP, SPRINGBOARD_LIGHT, SPRINGBOARD_RIGHT_DOWN, getX() - 1, linesNumber);
    }

    private List<SpringboardElement> generateSpringBoardLine(SpringboardElementType up, SpringboardElementType middle, SpringboardElementType down, int x, int lines) {
        List<SpringboardElement> line = new ArrayList<>();
        line.add(new SpringboardElement(x, 1, down));

        int y = 2;
        for (; y < lines - 1; y++) {
            line.add(new SpringboardElement(x, y, middle));
        }

        line.add(new SpringboardElement(x, y, up));
        return line;
    }

    @Override
    public void shift() {
        Shiftable.super.shift();
        riseElements.forEach(Shiftable::shift);
        betweenElements.forEach(Shiftable::shift);
        descentElements.forEach(Shiftable::shift);
    }

    public List<SpringboardElement> getElements() {
        return new LinkedList<SpringboardElement>() {
            {
                addAll(riseElements);
                addAll(betweenElements);
                addAll(descentElements);
            }
        };
    }

    public boolean isOnRise(Point point) {
        return riseElements.contains(point);
    }

    public boolean isOnSpringBoardTop(Point point) {
        return betweenElements.contains(point);
    }

    public boolean isOnDescent(Point point) {
        return descentElements.contains(point);
    }

}
