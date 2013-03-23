package com.globallogic.training.oleksii.morozov.sapperthehero.controller.console;

import com.globallogic.training.oleksii.morozov.sapperthehero.controller.console.input.Reader;
import com.globallogic.training.oleksii.morozov.sapperthehero.controller.console.output.Printer;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.Board;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.BoardImpl;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.minegenerator.RandomMinesGenerator;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.objects.Direction;

import java.util.Arrays;

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
    private static final Character[] AVAILABLE_CONSOLE_COMMANDS = {'w', 's', 'a', 'd',
            'r', 'q'};
    public static final String REENTER_DATA = "Reenter data";
    public static final String CHOOSE_DIRECTION_MINE_DETECTOR = "Choose direction mine detector.";

    private Printer printer;
    private Reader input;
    private Board board;

    public GameController(Printer printer, Reader input) {
        this.printer = printer;
        this.input = input;
        input.setPrinter(printer);
    }

    public void startNewGame() {
        board = getInitializedBoard(readInitialVariables());
        printBoardInformation();
        while (!isGameOver(board)) {
            printBoard(getBoardPresentation(new BoardPresenter(board)));
            consoleCommandHandler(board, readConsoleCommand());
        }
        printEndGameMessage();
    }

    private void printEndGameMessage() {
        printer.print(board.isWin() ? "I win" :
                (board.isSapperOnMine() ? "Ops, mine..." :
                        (board.isEmptyDetectorButPresentMines() ? "Ops, I have no charge, but mines present..." : "")));
    }

    Integer[] readInitialVariables() {
        Integer[] result = new Integer[]{input.readNumber(ENTER_BOARD_SIZE),
                input.readNumber(ENTER_NUMBER_OF_MINES_ON_BOARD),
                input.readNumber(DETECTOR_CHARGE_COUNT)};
        return result;
    }

    Board getInitializedBoard(Integer[] initialVariables) {
        try {
            return new BoardImpl(initialVariables[0], initialVariables[1],
                    initialVariables[2], new RandomMinesGenerator());
        } catch (IllegalArgumentException exception) {
            printer.print(REENTER_DATA);
            return getInitializedBoard(readInitialVariables());
        }
    }

    void printBoardInformation() {
        printer.print(BOARD_INFORMATION);
    }

    boolean isGameOver(Board board) {
        return board.getSapper().isDead() | board.isWin()
                | board.isEmptyDetectorButPresentMines();
    }


    String getBoardPresentation(BoardPresenter boardPresenter) {
        return boardPresenter.print();
    }

    void printBoard(String boardAsString) {
        printer.print(boardAsString);
    }

    void doSapperMovementCommand(Board board, char command) {
        board.sapperMoveTo(handleDirectionCommand(command));
    }

    private Direction handleDirectionCommand(char command) {
        switch (command) {
            case 'w': {
                return Direction.UP;
            }
            case 's': {
                return Direction.DOWN;
            }
            case 'a': {
                return Direction.LEFT;
            }
            case 'd': {
                return Direction.RIGHT;
            }
        }
        return null;
    }

    char readConsoleCommand() {
        char result = input.readCharacter();
        if (!Arrays.asList(AVAILABLE_CONSOLE_COMMANDS).contains(result)) {
            throw new IllegalConsoleCommandException();
        }
        return result;
    }

    void printMessageWhileUseMineDetector() {
        printer.print(CHOOSE_DIRECTION_MINE_DETECTOR);
    }

    void doEndGameCommand(SystemExitWrapper systemExitWrapper) {
        systemExitWrapper.exit();
    }

    public void clearOfMinesTo(Board board, char command) {
        board.useMineDetectorToGivenDirection(handleDirectionCommand(command));
    }

    public void consoleCommandHandler(Board board, char command) {
        if (command == 'r') {
            printMessageWhileUseMineDetector();
            clearOfMinesTo(board, readConsoleCommand());
        } else if (command == 'w' || command == 's' || command == 'a' || command == 'd') {
            doSapperMovementCommand(board, command);
        } else if (command == 'q') {
            doEndGameCommand(new SystemExitWrapperImpl());
        }
    }
}
