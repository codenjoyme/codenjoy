package com.codenjoy.dojo.kata.model.levels.algorithms.finale;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2017 Codenjoy
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


import com.codenjoy.dojo.kata.model.levels.algorithms.SumSquareDifferenceAlgorithm;

import java.util.Arrays;
import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 2/25/13
 * Time: 8:12 PM
 *
 * @author http://projecteuler.net/problem=6
 */
public class FinaleSumSquareDifferenceAlgorithm extends SumSquareDifferenceAlgorithm {

    @Override
    public List<String> getQuestions() {
        return Arrays.asList(
                "10",
                "20",
                "48",
                "32",
                "12",
                "4",
                "41",
                "39"
        );
    }
}
