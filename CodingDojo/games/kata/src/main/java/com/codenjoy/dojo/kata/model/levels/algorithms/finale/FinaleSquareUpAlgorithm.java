package com.codenjoy.dojo.kata.model.levels.algorithms.finale;


import com.codenjoy.dojo.kata.model.levels.algorithms.QuestionGenerator;
import com.codenjoy.dojo.kata.model.levels.algorithms.SquareUpAlgorithm;

import java.util.List;

public class FinaleSquareUpAlgorithm extends SquareUpAlgorithm {

    @Override
    public List<String> getQuestions() {
        return new QuestionGenerator(10, 10).generate();
    }
}
