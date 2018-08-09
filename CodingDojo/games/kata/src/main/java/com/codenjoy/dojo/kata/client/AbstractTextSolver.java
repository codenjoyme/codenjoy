package com.codenjoy.dojo.kata.client;

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


import com.codenjoy.dojo.client.AbstractTextBoard;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.kata.model.Elements;
import com.codenjoy.dojo.utils.JsonUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * User: your name
 * Это твой алгоритм AI для игры. Реализуй его на свое усмотрение.
 * Обрати внимание на {@see YourSolverTest} - там приготовлен тестовый
 * фреймворк для тебя.
 */
public abstract class AbstractTextSolver<T> implements Solver<AbstractTextBoard> {

    private AbstractTextBoard board;
    protected JSONObject data;

    public abstract Strings getAnswers(int level, Strings questions);

    @Override
    public String get(AbstractTextBoard board) {
        this.board = board;
        if (board.isGameOver()) return "";

        data = new JSONObject(board.getData());
        JSONArray array = data.getJSONArray("questions");
        int level = data.getInt("level"); 

        List<String> questions = JsonUtils.getStrings(array);
        Strings answers = getAnswers(level, new Strings(questions));
        String answersString = answers.toString();

        if (answers.size() == 1) { // TODO подумать над этим
            String command = answers.iterator().next();
            if (Arrays.asList(Elements.START_NEXT_LEVEL, Elements.SKIP_THIS_LEVEL).contains(command)) {
                answersString = command;
            }
        }

        return String.format("message('%s')", answersString);
    }

}
