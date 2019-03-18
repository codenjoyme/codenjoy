package com.codenjoy.dojo.lemonade.client.ai;

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


import com.codenjoy.dojo.client.AbstractJsonSolver;
import com.codenjoy.dojo.client.AbstractTextBoard;
import com.codenjoy.dojo.client.AbstractTextSolver;
import com.codenjoy.dojo.lemonade.client.Board;
import com.codenjoy.dojo.services.Dice;
import org.json.JSONObject;

/**
 * User: your name
 * Это твой алгоритм AI для игры. Реализуй его на свое усмотрение.
 * Обрати внимание на {@see YourSolverTest} - там приготовлен тестовый
 * фреймворк для тебя.
 */
public class AISolver extends AbstractJsonSolver<Board> {

    private Dice dice;
    private AbstractTextBoard board;

    public AISolver(Dice dice) {
        this.dice = dice;
    }

    @Override
    public String getAnswer(Board board) {
        String a = toAnswerString(0,0,0);
        return a;
    }

    private String toAnswerString(int glasses, int signs, int priceCents) {
        JSONObject answer = new JSONObject();
        answer.put("glassesToMake", glasses);
        answer.put("signsToMake", signs);
        answer.put("priceCents", priceCents);
        return answer.toString();
    }
}
