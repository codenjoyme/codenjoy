package com.codenjoy.dojo.kata.services.events;

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


import com.codenjoy.dojo.services.settings.Parameter;

import java.text.DecimalFormat;

public class PassTestEvent {
    private double complexity;
    private double testCount;

    public PassTestEvent(int complexity, int testCount) {
        this.complexity = complexity;
        this.testCount = testCount;
    }

    public int getScore(Parameter<Integer> A, Parameter<Integer> D) {
        double a = A.getValue();
        double d = D.getValue();
        double perTest = complexity * a * d / (100 * testCount);
        if (perTest < 1) {
            return 1;
        }
        return (int)(perTest);
    }

    @Override
    public String toString() {
        DecimalFormat format = new DecimalFormat("#.#");
        return "PassTest{" +
                "complexity=" + format.format(complexity) +
                ", testCount=" + format.format(testCount) +
                '}';
    }

}
