package com.globallogic.training.oleksii.morozov.sapperthehero.controller.console;

import com.globallogic.training.oleksii.morozov.sapperthehero.controller.console.input.Reader;
import com.globallogic.training.oleksii.morozov.sapperthehero.controller.console.output.Printer;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.Board;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.BoardImpl;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.minegenerator.RandomMinesGenerator;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.objects.Direction;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.objects.Sapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GameControllerTest {

//    private static final String CHOOSE_DIRECTION_MINE_DETECTOR = "Choose direction mine detector.";
//    private static final String BOARD_INFORMATION = "Information:\n"
//            + "Controls:\n" + "w - up\n" + "s - down\n" + "a - left\n"
//            + "d - right\n" + "r - use detector\n" + "q - end game\n"
//            + "\nLegend:\n" + "@ - Sapper\n" + "# - wall\n" + ". - free cell\n"
//            + "* - mine\n" + "After each command press ENTER\n";
//    private static final String ENTER_BOARD_SIZE = "Board size:";
//    private static final String ENTER_NUMBER_OF_MINES_ON_BOARD = "Mines count:";
//    private static final String DETECTOR_CHARGE_COUNT = "Detector charge count";
//
//    private Reader input;
//    private Printer printer;
//    private GameController gameController;
//
//    @Before
//    public void setUp() {
//        gameController = initializeGameController();
//    }
//
//    private GameController initializeGameController() {
//        input = mock(Reader.class);
//        printer = mock(Printer.class);
//        return new GameController(printer, input);
//    }
//
//    @Test
//    public void shouldGameController() {
//        assertNotNull(gameController);
//    }
//
//    @Test
//    public void shouldReadInitialBoardVariables_whenStartGame() {
//        // given
//        int boardSize = 3;
//        int mineCount = 2;
//        int detectorCharge = 6;
//
//        // when
//        when(input.readNumber(ENTER_BOARD_SIZE)).thenReturn(boardSize);
//        when(input.readNumber(ENTER_NUMBER_OF_MINES_ON_BOARD)).thenReturn(
//                mineCount);
//        when(input.readNumber(DETECTOR_CHARGE_COUNT))
//                .thenReturn(detectorCharge);
//        Integer[] expected = {boardSize, mineCount, detectorCharge};
//
//        // then
//        Integer[] actual = gameController.readInitialVariables();
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void shouldBoard_whenInitializeBoardVariables() {
//        // given
//        int boardSize = 3;
//        int mineCount = 2;
//        int detectorCharge = 6;
//        Integer[] variables = {boardSize, mineCount, detectorCharge};
//
//        // when
//        Board board = new BoardImpl((int) variables, 0, 0, new RandomMinesGenerator());
//
//        // then
//        assertNotNull(board);
//        assertEquals(boardSize, board.getSize());
//        assertEquals(mineCount, board.getMinesCount());
//        assertEquals(detectorCharge, board.getSapper().getMineDetectorCharge());
//    }
//
//    @Test
//    public void shouldPrintBoardInformation() {
//        // when
//        gameController.printer.print(GameController.BOARD_INFORMATION);
//
//        // then
//        verify(printer).print(BOARD_INFORMATION);
//    }
//
//    @Test
//    public void shouldGameIsOver_whenDeadSapper() {
//        // given
//        boolean isWin = false;
//        boolean sapperIsDead = true;
//        boolean isEmptyDetectorButPresentMines = false;
//        // when
//        Board board = initializeGameIsOverState(isWin, sapperIsDead,
//                isEmptyDetectorButPresentMines);
//
//        // then
//        assertTrue(board.isGameOver());
//    }
//
//    private Board initializeGameIsOverState(boolean isWin, boolean sapperIsDead,
//                                            boolean isEmptyDetectorButPresentMines) {
//        Board board = mock(Board.class);
//        Sapper sapper = mock(Sapper.class);
//
//        when(board.isWin()).thenReturn(isWin);
//        when(sapper.isDead()).thenReturn(sapperIsDead);
//        when(board.getSapper()).thenReturn(sapper);
//        when(board.isEmptyDetectorButPresentMines()).thenReturn(
//                isEmptyDetectorButPresentMines);
//        return board;
//    }
//
//    @Test
//    public void shouldGameIsOver_whenIsWin() {
//        // given
//        boolean isWin = true;
//        boolean sapperIsDead = false;
//        boolean isEmptyDetectorButPresentMines = false;
//        // when
//        Board board = initializeGameIsOverState(isWin, sapperIsDead,
//                isEmptyDetectorButPresentMines);
//
//        // then
//        assertTrue(board.isGameOver());
//    }
//
//    @Test
//    public void shouldGameIsOver_whenNoChargePresentMines() {
//        // given
//        boolean isWin = false;
//        boolean sapperIsDead = false;
//        boolean isEmptyDetectorButPresentMines = true;
//        // when
//        Board board = initializeGameIsOverState(isWin, sapperIsDead,
//                isEmptyDetectorButPresentMines);
//
//        // then
//        assertTrue(board.isGameOver());
//    }
//
//    @Test
//    public void shouldBoardPresentation() {
//        // given
//        BoardPresenter boardPresenter = mock(BoardPresenter.class);
//        // when
//        when(boardPresenter.print()).thenReturn("");
//        // then
//        assertEquals("", boardPresenter.print());
//    }
//
//    @Test
//    public void shouldPrintBoardPresentation() {
//        // given
//        String boardAsString = "";
//        // when
//        doNothing().when(printer).print("");
//        gameController.printer.print(boardAsString);
//        // then
//        verify(printer).print("");
//    }
//
//    @Test
//    public void shouldMoveSapper_whenReadAppropriateCommand() {
//        // given
//        Board board = mock(Board.class);
//        // when
//        doNothing().when(board).sapperMoveTo(Direction.UP);
//        board.sapperMoveTo(gameController.handleDirectionCommand('w'));
//        // then
//        verify(board).sapperMoveTo(Direction.UP);
//    }
//
//    @Test
//    public void shouldMoveSapperDown_whenReadAppropriateCommand() {
//        // given
//        Board board = mock(Board.class);
//        // when
//        doNothing().when(board).sapperMoveTo(Direction.DOWN);
//        board.sapperMoveTo(gameController.handleDirectionCommand('s'));
//        // then
//        verify(board).sapperMoveTo(Direction.DOWN);
//    }
//
//    @Test
//    public void shouldMoveSapperLeft_whenReadAppropriateCommand() {
//        // given
//        Board board = mock(Board.class);
//        // when
//        doNothing().when(board).sapperMoveTo(Direction.LEFT);
//        board.sapperMoveTo(gameController.handleDirectionCommand('a'));
//        // then
//        verify(board).sapperMoveTo(Direction.LEFT);
//    }
//
//    @Test
//    public void shouldMoveSapperRight_whenReadAppropriateCommand() {
//        // given
//        Board board = mock(Board.class);
//        // when
//        doNothing().when(board).sapperMoveTo(Direction.RIGHT);
//        board.sapperMoveTo(gameController.handleDirectionCommand('d'));
//        // then
//        verify(board).sapperMoveTo(Direction.RIGHT);
//    }
//
//    @Test
//    public void shouldException_whenReadIllegalConsoleCommand() {
//        // when
//        when(input.readCharacter()).thenReturn('c');
//        gameController.readConsoleCommand();
//    }
//
//    @Test
//    public void shouldReadConsoleCommand_whenCommandIsCorrect() {
//        // when
//        when(input.readCharacter()).thenReturn('d');
//        // then
//        assertEquals('d', gameController.readConsoleCommand());
//    }
//
//    @Test
//    public void shouldMessageChooseDirection_whenReadPrepareMineDetectorCommand() {
//        // given
//        doNothing().when(printer).print(CHOOSE_DIRECTION_MINE_DETECTOR);
//        // when
//        gameController.printer.print(GameController.CHOOSE_DIRECTION_MINE_DETECTOR);
//        // then
//        verify(printer).print(CHOOSE_DIRECTION_MINE_DETECTOR);
//    }
//
//    @Test
//    public void shouldEndGame_whenReadAppropriateCommand() {
//        // given
//        SystemExitWrapper system = mock(SystemExitWrapper.class);
//        doNothing().when(system).exit();
//        // when
//        system.exit();
//        // then
//        verify(system).exit();
//    }
//
//    @Test
//    public void shouldClearOfMines_whenReadDirectionCommand() {
//        //  given
//        Board board = mock(Board.class);
//        // when
//        doNothing().when(board).useMineDetectorToGivenDirection(Direction.RIGHT);
//        board.useMineDetectorToGivenDirection(gameController.handleDirectionCommand('d'));
//        // then
//        verify(board).useMineDetectorToGivenDirection(Direction.RIGHT);
//    }
//
//    @Test
//    public void shouldConsoleCommandHandler_whenReadDirectionCommand() {
//        //  given
//        Board board = mock(Board.class);
//        // when
//        when(input.readCharacter()).thenReturn('r');
//        gameController.consoleCommandHandler(board, 'r');
//        // then
//        verify(board).useMineDetectorToGivenDirection(null);
//        verify(input).readCharacter();
//        verify(printer).print("Choose direction mine detector.");
//    }

}
