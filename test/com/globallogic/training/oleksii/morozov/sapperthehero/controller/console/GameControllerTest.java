package com.globallogic.training.oleksii.morozov.sapperthehero.controller.console;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.globallogic.training.oleksii.morozov.sapperthehero.controller.console.input.Reader;
import com.globallogic.training.oleksii.morozov.sapperthehero.controller.console.output.Printer;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.Board;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.objects.Direction;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.objects.Sapper;

@RunWith(MockitoJUnitRunner.class)
public class GameControllerTest {

	private static final String CHOOSE_DIRECTION_MINE_DETECTOR = "Choose direction mine detector.";
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

	@Test
	public void shouldReadInitialBoardVariables_whenStartGame() {
		// given
		int boardSize = 3;
		int mineCount = 2;
		int detectorCharge = 6;

		// when
		when(input.readNumber(ENTER_BOARD_SIZE)).thenReturn(boardSize);
		when(input.readNumber(ENTER_NUMBER_OF_MINES_ON_BOARD)).thenReturn(
				mineCount);
		when(input.readNumber(DETECTOR_CHARGE_COUNT))
				.thenReturn(detectorCharge);
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
	public void shouldBoardPresentation() {
		// given
		BoardPresenter boardPresenter = mock(BoardPresenter.class);
		// when
		when(boardPresenter.print()).thenReturn("");
		// then
		assertEquals("", gameController.getBoardPresentation(boardPresenter));
	}

	@Test
	public void shouldPrintBoardPresentation() {
		// given
		String boardAsString = "";
		// when
		doNothing().when(printer).print("");
		gameController.printBoard(boardAsString);
		// then
		verify(printer).print("");
	}

	@Test
	public void shouldMoveSapperUp_whenReadAppropriateCommand() {
		// given
		Board board = mock(Board.class);
		// when
		doNothing().when(board).sapperMoveTo(Direction.UP);
		gameController.doConsoleCommandUp(board, 'w');
		// then
		verify(board).sapperMoveTo(Direction.UP);
	}

	@Test
	public void shouldMoveSapperDown_whenReadAppropriateCommand() {
		// given
		Board board = mock(Board.class);
		// when
		doNothing().when(board).sapperMoveTo(Direction.DOWN);
		gameController.doConsoleCommandDown(board, 's');
		// then
		verify(board).sapperMoveTo(Direction.DOWN);
	}

	@Test
	public void shouldMoveSapperLeft_whenReadAppropriateCommand() {
		// given
		Board board = mock(Board.class);
		// when
		doNothing().when(board).sapperMoveTo(Direction.LEFT);
		gameController.doConsoleCommandLeft(board, 'a');
		// then
		verify(board).sapperMoveTo(Direction.LEFT);
	}

	@Test
	public void shouldMoveSapperRight_whenReadAppropriateCommand() {
		// given
		Board board = mock(Board.class);
		// when
		doNothing().when(board).sapperMoveTo(Direction.RIGHT);
		gameController.doConsoleCommandRight(board, 'd');
		// then
		verify(board).sapperMoveTo(Direction.RIGHT);
	}

	@Test(expected = IllegalConsoleCommandException.class)
	public void shouldException_whenReadIllegalConsoleCommand() {
		// when
		when(input.readCharacter()).thenReturn('c');
		gameController.readConsoleCommand();
	}

	@Test
	public void shouldReadConsoleCommand_whenCommandIsCorrect() {
		// when
		when(input.readCharacter()).thenReturn('d');
		// then
		assertEquals('d', gameController.readConsoleCommand());
	}

	@Test
	public void shouldMessageChooseDirection_whenReadPrepareMineDetectorCommand() {
		// given
		doNothing().when(printer).print(CHOOSE_DIRECTION_MINE_DETECTOR);
		// when
		gameController.printMessageWhileUseMineDetector('r');
		// then
		verify(printer).print(CHOOSE_DIRECTION_MINE_DETECTOR);
	}

	@Test
	public void shouldEndGame_whenReadAppropriateCommand() {
		// given
		SystemExitWrapper system = mock(SystemExitWrapper.class);
		doNothing().when(system).exit();
		// when
		gameController.doEndGameCommand(system, 'q');
		// then
		verify(system).exit();
	}
}
