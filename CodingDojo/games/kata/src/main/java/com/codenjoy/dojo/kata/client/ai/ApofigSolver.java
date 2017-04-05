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
import com.codenjoy.dojo.kata.services.Elements;
import com.codenjoy.dojo.kata.services.GameRunner;

import java.util.List;

/**
 * User: your name
 * Это твой алгоритм AI для игры. Реализуй его на свое усмотрение.
 * Обрати внимание на {@see YourSolverTest} - там приготовлен тестовый
 * фреймворк для тебя.
 */
public class ApofigSolver extends AbstractTextSolver {

    private AbstractTextBoard board;
    private List<Level> levels = LevelsLoader.getAlgorithms();

    @Override
    public Strings getAnswers(int level, Strings questions) {
        if (!questions.iterator().hasNext()) {
            return new Strings(Elements.START_NEXT_LEVEL);
        }
        Algorithm algorithm = getAlgorithm(level);

        Strings answers = new Strings();
        for (String question : questions) {
//            if (question.equals("2")) {
//                answers.add("qwe");
//            } else
            answers.add(algorithm.get(question));
        }
        return answers;
    }

    private Algorithm getAlgorithm(int level) {
        if (level >= levels.size()) {
            return new NullAlgorithm();
        }
        return (Algorithm) levels.get(level);
    }

    public static void main(String[] args) {
//        startLocal();
        start(WebSocketRunner.DEFAULT_USER, WebSocketRunner.Host.LOCAL);
    }

    private static void startLocal() {
        LocalGameRunner.TIMEOUT = 1000;
        LocalGameRunner.run(new GameRunner(),
                new ApofigSolver(),
                new Board());
    }

    public static void start(String name, WebSocketRunner.Host host) {
        WebSocketRunner.run(host,
                name,
                new ApofigSolver(),
                new Board());
    }
}
