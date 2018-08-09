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

public class NextAlgorithmEvent {

    private double complexity;
    private double time;

    public NextAlgorithmEvent(int complexity, double time) {
        this.complexity = complexity;
        this.time = time;
    }

    public int getScore(Parameter<Integer> A, Parameter<Integer> B, Parameter<Integer> C) {
        double a = A.getValue();
        double b = B.getValue();
        double c = C.getValue() / 100D;
        if (time > b*complexity) {
            time = b*complexity;
        }
        return (int)(a * (((c - 1)*time + (b - c)*complexity)/(b - 1)));
    }

    @Override
    public String toString() {
        DecimalFormat format = new DecimalFormat("#.#");
        return "NextAlgorithm{" +
                "complexity=" + format.format(complexity) +
                ", time=" + format.format(time) +
                '}';
    }

}
