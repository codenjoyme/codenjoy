package com.globallogic.training.oleksii.morozov.sapperthehero.controller.console;

import com.globallogic.training.oleksii.morozov.sapperthehero.controller.console.input.Reader;
import com.globallogic.training.oleksii.morozov.sapperthehero.controller.console.output.Printer;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.Board;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.BoardImpl;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.minegenerator.RandomMinesGenerator;

/**
 * User: oleksii.morozov Date: 10/16/12 Time: 3:33 PM
 */
public class GameController {
	private static final String BOARD_INFORMATION = "Information:\n"
			+ "Controls:\n" + "w - up\n" + "s - down\n" + "a - left\n"
			+ "d - right\n" + "r - use detector\n" + "q - end game\n"
			+ "\nLegend:\n" + "@ - Sapper\n" + "# - wall\n" + ". - free cell\n"
			+ "* - mine\n" + "After each command press ENTER\n";
	private static final String ENTER_BOARD_SIZE = "Board size:";
	private static final String ENTER_NUMBER_OF_MINES_ON_BOARD = "Mines count:";
	private static final String DETECTOR_CHARGE_COUNT = "Detector charge count";

	private Board board;
	private Printer printer;
	private Reader input;

	public GameController(Printer printer, Reader input) {
		this.printer = printer;
		this.input = input;
		input.setPrinter(printer);
	}

	public Board getInitializedBoard() {
		while (true) {
			try {
				int boardSize = input.read(ENTER_BOARD_SIZE);
				int mineCount = input.read(ENTER_NUMBER_OF_MINES_ON_BOARD);
				int detectorCharge = input.read(DETECTOR_CHARGE_COUNT);

				return new BoardImpl(boardSize, mineCount, detectorCharge,
						new RandomMinesGenerator());
			} catch (IllegalArgumentException exception) {
				printer.print(exception.getMessage());
			}
		}
	}

	public void printBoardInformation() {
		printer.print(BOARD_INFORMATION);
	}

	public boolean isGameOver(Board board) {
		return false;
	}

	public void startNewGame() {
	}

}
