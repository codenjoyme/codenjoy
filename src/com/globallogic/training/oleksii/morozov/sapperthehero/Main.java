package com.globallogic.training.oleksii.morozov.sapperthehero;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.globallogic.training.oleksii.morozov.sapperthehero.controller.console.GameController;
import com.globallogic.training.oleksii.morozov.sapperthehero.controller.console.input.ConsoleReader;
import com.globallogic.training.oleksii.morozov.sapperthehero.controller.console.output.ConsolePrinter;

/**
 * User: oleksii.morozov Date: 10/16/12 Time: 3:33 PM
 */
public class Main {

	public static void main(String[] args) {
		new GameController(new ConsolePrinter(), new ConsoleReader())
				.startNewGame();
	}

}