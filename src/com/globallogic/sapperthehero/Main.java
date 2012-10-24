package com.globallogic.sapperthehero;

import com.globallogic.sapperthehero.controller.GameController;
import com.globallogic.sapperthehero.controller.input.impl.ConsoleReader;
import com.globallogic.sapperthehero.controller.output.impl.ConsolePrinter;

/**
 * User: oleksii.morozov
 * Date: 10/16/12
 * Time: 3:33 PM
 */
public class Main {

    public static void main(String[] args) {
        new GameController(new ConsolePrinter(),
                new ConsoleReader()).startNewGame();
    }

}
