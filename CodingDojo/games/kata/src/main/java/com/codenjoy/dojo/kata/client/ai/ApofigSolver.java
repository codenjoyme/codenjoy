package com.codenjoy.dojo.kata.client.ai;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import com.codenjoy.dojo.client.AbstractTextBoard;
import com.codenjoy.dojo.client.LocalGameRunner;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.kata.client.AbstractTextSolver;
import com.codenjoy.dojo.kata.client.Board;
import com.codenjoy.dojo.kata.client.Strings;
import com.codenjoy.dojo.kata.model.levels.*;
import com.codenjoy.dojo.kata.services.GameRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.RandomDice;

import java.util.List;

/**
 * User: your name
 * Это твой алгоритм AI для игры. Реализуй его на свое усмотрение.
 * Обрати внимание на {@see YourSolverTest} - там приготовлен тестовый
 * фреймворк для тебя.
 */
public class ApofigSolver extends AbstractTextSolver {

    private Dice dice;
    private AbstractTextBoard board;
    private List<Level> levels = LevelsLoader.getAlgorithms();

    public ApofigSolver(Dice dice) {
        this.dice = dice;
    }

    @Override
    public Strings getAnswers(Strings questions) {
        Algorithm algorithm = getAlgorithm(data.getString("description"));

        Strings answers = new Strings();
        for (String question : questions) {
//            if (question.equals("2")) {
//                answers.add("qwe");
//            } else
            answers.add(algorithm.get(question));
        }
        return answers;
    }

    private Algorithm getAlgorithm(String description) {
        String algorithmName = description.split(":")[0];
        for (Level level : levels) {
            if (level.getClass().getSimpleName().contains(algorithmName)) {
                return (Algorithm) level;
            } else if (algorithmName.contains(NullLevel.class.getSimpleName())) {
                return new NullAlgorithm();
            }
        }
        throw new RuntimeException("Not found algorithm for description: " + description);
    }

    public static void main(String[] args) {
//        LocalGameRunner.TIMEOUT = 10;
//        LocalGameRunner.run(new GameRunner(),
//                new ApofigSolver(new RandomDice()),
//                new Board());
        start(WebSocketRunner.DEFAULT_USER, WebSocketRunner.Host.LOCAL);
    }

    public static void start(String name, WebSocketRunner.Host host) {
        WebSocketRunner.run(host,
                name,
                new ApofigSolver(new RandomDice()),
                new Board());
    }
}
