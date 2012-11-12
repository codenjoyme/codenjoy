package com.globallogic.training.oleksii.morozov.sapperthehero.controller.console;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.globallogic.training.oleksii.morozov.sapperthehero.controller.console.input.Reader;
import com.globallogic.training.oleksii.morozov.sapperthehero.controller.console.output.Printer;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.Board;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.objects.Sapper;

@RunWith(MockitoJUnitRunner.class)
public class GameControllerTest {
	private static final String BOARD_INFORMATION = "Information:\n"
			+ "Controls:\n" + "w - up\n" + "s - down\n" + "a - left\n"
			+ "d - right\n" + "r - use detector\n" + "q - end game\n"
			+ "\nLegend:\n" + "@ - Sapper\n" + "# - wall\n" + ". - free cell\n"
			+ "* - mine\n" + "After each command press ENTER\n";
	private static final String ENTER_BOARD_SIZE = "Board size:";
	private static final String ENTER_NUMBER_OF_MINES_ON_BOARD = "Mines count:";
	private static final String DETECTOR_CHARGE_COUNT = "Detector charge count";
	private Reader input;
	private Printer printer;
	private GameController gameController;

	@Before
	public void setUp() {
		gameController = itinializeGameController();
	}

	private GameController itinializeGameController() {
		input = mock(Reader.class);
		printer = mock(Printer.class);
		return new GameController(printer, input);
	}

	@Test
	public void shouldGameController() {
		assertNotNull(gameController);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void shouldReadInitialBoardVariables_whenStartGame() {
		// given
		int boardSize = 3;
		int mineCount = 2;
		int detectorCharge = 6;

		// when
		when(input.read(ENTER_BOARD_SIZE)).thenReturn(boardSize);
		when(input.read(ENTER_NUMBER_OF_MINES_ON_BOARD)).thenReturn(mineCount);
		when(input.read(DETECTOR_CHARGE_COUNT)).thenReturn(detectorCharge);
		Integer[] expected = { boardSize, mineCount, detectorCharge };

		// then
		Integer[] actual = gameController.readInitialVariables();
		assertEquals(expected, actual);
	}

	@Test
	public void shouldBoard_whenInitializeBoardVariables() {
		// given
		int boardSize = 3;
		int mineCount = 2;
		int detectorCharge = 6;
		int[] variables = { boardSize, mineCount, detectorCharge };

		// when
		Board board = gameController.getInitializedBoard(variables);

		// then
		assertNotNull(board);
		assertEquals(boardSize, board.getSize());
		assertEquals(mineCount, board.getMinesCount());
		assertEquals(detectorCharge, board.getSapper().getMineDetectorCharge());
	}

	@Test
	public void shouldPrintBoardInformation() {
		// when
		gameController.printBoardInformation();

		// then
		verify(printer).print(BOARD_INFORMATION);
	}

	@Test
	public void shouldGameIsOver_whenDeadSapper() {
		// given
		boolean isWin = false;
		boolean sapperIsDead = true;
		boolean isEmptyDetectorButPresentMines = false;
		// when
		Board board = intializeGameIsOverState(isWin, sapperIsDead,
				isEmptyDetectorButPresentMines);

		// then
		assertTrue(gameController.isGameOver(board));
	}

	private Board intializeGameIsOverState(boolean isWin, boolean sapperIsDead,
			boolean isEmptyDetectorButPresentMines) {
		Board board = mock(Board.class);
		Sapper sapper = mock(Sapper.class);

		when(board.isWin()).thenReturn(isWin);
		when(sapper.isDead()).thenReturn(sapperIsDead);
		when(board.getSapper()).thenReturn(sapper);
		when(board.isEmptyDetectorButPresentMines()).thenReturn(
				isEmptyDetectorButPresentMines);
		return board;
	}

	@Test
	public void shouldGameIsOver_whenIsWin() {
		// given
		boolean isWin = true;
		boolean sapperIsDead = false;
		boolean isEmptyDetectorButPresentMines = false;
		// when
		Board board = intializeGameIsOverState(isWin, sapperIsDead,
				isEmptyDetectorButPresentMines);

		// then
		assertTrue(gameController.isGameOver(board));
	}

	@Test
	public void shouldGameIsOver_whenNoChargePresentMines() {
		// given
		boolean isWin = false;
		boolean sapperIsDead = false;
		boolean isEmptyDetectorButPresentMines = true;
		// when
		Board board = intializeGameIsOverState(isWin, sapperIsDead,
				isEmptyDetectorButPresentMines);

		// then
		assertTrue(gameController.isGameOver(board));
	}

	@Test
	public void shouldPrintBoard(){
		// given
		String toPrint = "" +
		        "# # # # # \n" +
		        "# . . * # \n" +
		        "# . @ * # \n" +
		        "# . . * # \n" +
		        "# # # # # \n" +
		        "mines on board: 3\n" +
		        "mines near supper: 3\n" +
		        "mine detector charge: 3";
		BoardPresenter boardPresenter = mock(BoardPresenter.class);
		// when
		when(boardPresenter.print()).thenReturn(toPrint);
		// then
		assertEquals(toPrint, gameController.present(boardPresenter));
	}
	
}
