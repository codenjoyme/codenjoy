package com.codenjoy.dojo.kata.model.levels.algorithms;

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


import com.codenjoy.dojo.kata.model.levels.AlgorithmLevelImpl;
import org.apache.commons.lang3.StringUtils;

public class Sequence2Algorithm extends AlgorithmLevelImpl {

    @Override
    public String get(int n) {
        Integer res = 971;

        for(int i = 1; i < n; i++) {
            String s = res.toString();
            String s2 = StringUtils.leftPad(s, 3, '0');
            res = res - Integer.parseInt(s2.substring(0, 2));
        }

        return res.toString();
    }

    @Override
    public String description() {
        return "Continue the sequence 971, 874, 787, 709, 639...\n" +
                "i.e f(1) = 971, f(2) = 874, ...\n" +
                "Hint: Use subtraction";
    }

    @Override
    public int complexity() {
        return 50;
    }

    @Override
    public String author() {
        return "Alexey.Shcheglov (Alexey_Shcheglov@epam.com)\n" +
                "http://nazva.net/735";
    }
}
