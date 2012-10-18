package com.globallogic.sapperthehero;

import com.globallogic.sapperthehero.controller.GameController;
import com.globallogic.sapperthehero.input.ConsoleReader;
import com.globallogic.sapperthehero.output.ConsolePrinter;

/**
 * Created with IntelliJ IDEA.
 * User: oleksii.morozov
 * Date: 10/16/12
 * Time: 3:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static void main(String[] args) {
        new GameController(new ConsolePrinter(), new ConsoleReader()).startNewGame();
    }

}
