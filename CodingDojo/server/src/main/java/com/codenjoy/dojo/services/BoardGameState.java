package com.codenjoy.dojo.services;

import com.codenjoy.dojo.transport.GameState;

/**
 * User: sanja
 * Date: 30.05.13
 * Time: 18:26
 */
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
