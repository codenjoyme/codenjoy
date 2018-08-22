package com.codenjoy.dojo.tetris.client;

import com.codenjoy.dojo.client.AbstractTextBoard;
import com.codenjoy.dojo.client.Solver;
import org.json.JSONObject;

public abstract class AbstractJsonSolver<T> implements Solver<AbstractTextBoard> {

    private AbstractTextBoard board;

    public abstract String getAnswer(JSONObject question);

    @Override
    public String get(AbstractTextBoard board) {
        this.board = board;
        if (board.isGameOver()) return "";

        JSONObject data = new JSONObject(board.getData());

        String answer = getAnswer(data);

        return answer;
    }

}