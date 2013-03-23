package com.globallogic.training.oleksii.morozov.sapperthehero;

import com.codenjoy.dojo.services.ConsoleImpl;
import com.globallogic.training.oleksii.morozov.sapperthehero.controller.console.GameController;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.Board;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.BoardImpl;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.minegenerator.RandomMinesGenerator;

/**
 * User: oleksii.morozov Date: 10/16/12 Time: 3:33 PM
 */
public class Main {

    public static void main(String[] args) {
        Board board = new BoardImpl(10, 10, 10, new RandomMinesGenerator());
        new GameController(new ConsoleImpl(), board).startNewGame();
    }

}