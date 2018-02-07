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


import com.codenjoy.dojo.kata.model.levels.algorithms.QuestionGenerator;
import com.codenjoy.dojo.kata.model.levels.algorithms.Sequence1Algorithm;

import java.util.List;

public class FinaleSequence1Algorithm extends Sequence1Algorithm {

    @Override
    public List<String> getQuestions() {
        return new QuestionGenerator(29, 18).generate();
    }

    @Override
    public String description() {
        return "Continue the sequence 525, 354, 555, 657, 585, 960... \n" +
                "i.e f(1) = 525, f(2) = 354, ...\n" +
                "Hint: Look at the whole picture";
    }
}
