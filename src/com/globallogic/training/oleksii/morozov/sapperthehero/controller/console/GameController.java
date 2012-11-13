package com.globallogic.training.oleksii.morozov.sapperthehero.controller.console;

import java.util.Arrays;

import com.globallogic.training.oleksii.morozov.sapperthehero.controller.console.input.Reader;
import com.globallogic.training.oleksii.morozov.sapperthehero.controller.console.output.Printer;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.Board;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.BoardImpl;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.minegenerator.RandomMinesGenerator;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.objects.Direction;

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
	private static final Character[] AVAILABLE_CONSOLE_COMMANDS = { 'w', 's', 'a', 'd',
			'r', 'q' };
	private Printer printer;
	private Reader input;

	public GameController(Printer printer, Reader input) {
		this.printer = printer;
		this.input = input;
		input.setPrinter(printer);
	}

	public Integer[] readInitialVariables() {
		Integer[] result = { input.readNumber(ENTER_BOARD_SIZE),
				input.readNumber(ENTER_NUMBER_OF_MINES_ON_BOARD),
				input.readNumber(DETECTOR_CHARGE_COUNT) };
		return result;
	}

	public Board getInitializedBoard(int[] initilVariables) {
		return new BoardImpl(initilVariables[0], initilVariables[1],
				initilVariables[2], new RandomMinesGenerator());
	}

	public void printBoardInformation() {
		printer.print(BOARD_INFORMATION);
	}

	public boolean isGameOver(Board board) {
		return board.getSapper().isDead() | board.isWin()
				| board.isEmptyDetectorButPresentMines();
	}

	public void startNewGame() {
	}

	public String getBoardPresentation(BoardPresenter boardPresenter) {
		return "";
	}

	public void printBoard(String boardAsString) {
		printer.print(boardAsString);
	}

	public void doConsoleCommandUp(Board board, char command) {
		if (command == 'w') {
			board.sapperMoveTo(Direction.UP);
		}
	}

	public void doConsoleCommandDown(Board board, char command) {
		if (command == 's') {
			board.sapperMoveTo(Direction.DOWN);
		}
	}

	public void doConsoleCommandLeft(Board board, char command) {
		if (command == 'a') {
			board.sapperMoveTo(Direction.LEFT);
		}
	}

	public void doConsoleCommandRight(Board board, char command) {
		if (command == 'd') {
			board.sapperMoveTo(Direction.RIGHT);
		}
	}

	public char readConsoleCommand() {
		char result = input.readCharacter();
		if (!Arrays.asList(AVAILABLE_CONSOLE_COMMANDS).contains(result)){
			throw new IllegalConsoleCommandException();
		}
		return result;
	}

	public void printMessageWhileUseMineDetector(char command) {
		if (command == 'r') {
			printer.print("Choose direction mine detector.");
		}
	}

	public void doEndGameCommand(SystemExitWrapper systemExitWrapper, char command) {
		if (command == 'q') {
			systemExitWrapper.exit();
		}
	}

	
}
