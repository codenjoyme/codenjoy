package com.codenjoy.dojo.kata.model.levels;

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

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public abstract class AlgorithmLevelImpl extends QuestionAnswerLevelImpl implements Algorithm {

    public static final int MAX_QUESTION_FOR_ONE_INT_ARGUMENT = 25;

    public AlgorithmLevelImpl(String... input) {
        if (input.length == 0) {
            questions = getQuestions();
            if (questions.isEmpty()) {
                questions = new LinkedList<>();
                for (int index = 1; index <= MAX_QUESTION_FOR_ONE_INT_ARGUMENT; index++) {
                    questions.add(String.valueOf(index));
                }
            }
        } else {
            questions = Arrays.asList(input);
        }
        prepareAnswers();
    }

    private void prepareAnswers() {
        answers = new LinkedList<>();
        for (String question : questions) {
            answers.add(get(question));
        }
    }

    @Override
    public List<String> getQuestions() {
        return questions;
    }

    public String get(String input) {
        if (input.contains(", ")) {
            String[] inputs = input.split(", ");
            int[] ints = new int[inputs.length];
            try {
                for (int index = 0; index < inputs.length; index++) {
                    ints[index] = Integer.parseInt(inputs[index]);
                }
                return get(ints);
            } catch (NumberFormatException e) {
                return get(inputs);
            }
        }

        try {
            int number = Integer.parseInt(input);
            return get(number);
        } catch (NumberFormatException e) {
            // do nothing - in this case this method will be overloaded
            throw new IllegalStateException("You should override one of 'get' methods");
        }
    }

    public String get(int input) {
        return null;
    }

    public String get(int... input) {
        return null;
    }

    public String get(String... input) {
        return null;
    }

}
