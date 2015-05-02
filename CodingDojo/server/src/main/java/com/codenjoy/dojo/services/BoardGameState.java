package com.codenjoy.dojo.services;

import com.codenjoy.dojo.transport.GameState;

public class BoardGameState implements GameState {

    private String board;

    public BoardGameState(String board) {
        this.board = board;
    }

    @Override
    public String asString() {
        return "board=" + board;
    }
}
