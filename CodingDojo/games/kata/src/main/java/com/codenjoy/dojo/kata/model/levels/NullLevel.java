package com.codenjoy.dojo.kata.model.levels;

import java.util.Arrays;
import java.util.List;

/**
 * Created by indigo on 2017-03-04.
 */
public class NullLevel implements Level {

    @Override
    public List<String> getQuestions() {
        return Arrays.asList();
    }

    @Override
    public List<String> getAnswers() {
        return Arrays.asList();
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public int complexity() {
        return 0;
    }

    @Override
    public String description() {
        return "No more Levels. You win!";
    }
}
