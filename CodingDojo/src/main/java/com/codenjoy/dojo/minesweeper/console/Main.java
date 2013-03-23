package com.codenjoy.dojo.minesweeper.console;

import com.codenjoy.dojo.services.ConsoleImpl;
import com.codenjoy.dojo.minesweeper.model.Board;
import com.codenjoy.dojo.minesweeper.model.BoardImpl;
import com.codenjoy.dojo.minesweeper.model.RandomMinesGenerator;

/**
 * User: oleksii.morozov Date: 10/16/12 Time: 3:33 PM
 */
public class Main {

    public static void main(String[] args) {
        Board board = new BoardImpl(10, 10, 10, new RandomMinesGenerator());
        new GameController(new ConsoleImpl(), board).startNewGame();
    }

}