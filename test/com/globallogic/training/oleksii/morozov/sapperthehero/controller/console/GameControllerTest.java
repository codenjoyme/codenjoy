package com.globallogic.training.oleksii.morozov.sapperthehero.controller.console;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import com.globallogic.training.oleksii.morozov.sapperthehero.controller.console.input.Reader;
import com.globallogic.training.oleksii.morozov.sapperthehero.controller.console.output.Printer;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.Board;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.objects.Mine;
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

	@Test
	public void shouldBoard_whenInitializeIt() {
		// given
		int boardSize = 3;
		int mineCount = 2;
		int detectorCharge = 6;
		initReaderWith(boardSize, mineCount, detectorCharge);

		// when
		Board board = gameController.getInitializedBoard();

		// then
		assertEquals(boardSize, board.getSize());
		assertEquals(mineCount, board.getMinesCount());
		assertEquals(detectorCharge, board.getSapper().getMineDetectorCharge());
	}

	private void initReaderWith(int boardSize, int mineCount, int detectorCharge) {
		when(input.read(ENTER_BOARD_SIZE)).thenReturn(boardSize);
		when(input.read(ENTER_NUMBER_OF_MINES_ON_BOARD)).thenReturn(mineCount);
		when(input.read(DETECTOR_CHARGE_COUNT)).thenReturn(detectorCharge);
	}

	@Test
	public void shouldPrintBoardInformation() {
		// when
		gameController.printBoardInformation();

		// then
		verify(printer).print(BOARD_INFORMATION);
	}

	@Test
	public void shouldGameIsNotOver_whenBoardCreated() {
		// when
		Board board = mock(Board.class);
		
		// then
		assertEquals(false, gameController.isGameOver(board));
	}
}
