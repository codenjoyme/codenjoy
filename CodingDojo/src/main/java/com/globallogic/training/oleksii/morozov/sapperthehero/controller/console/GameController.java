package com.globallogic.training.oleksii.morozov.sapperthehero.controller.console;

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
            + "d - right\n" + "r - use detector\n"
            + "\nLegend:\n" + "@ - Sapper\n" + "# - wall\n" + ". - free cell\n"
            + "* - mine\n" + "After each command press ENTER\n";

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
        board = new BoardImpl(10, 10, 10, new RandomMinesGenerator());
        printer.print(BOARD_INFORMATION);

        while (!board.isGameOver()) {
            printer.print(new BoardPresenter(board).print());
            consoleCommandHandler(input.readCharacter());
        }
        printEndGameMessage();
    }

    private void printEndGameMessage() {
        printer.print(board.isWin() ? "I win" :
                (board.isSapperOnMine() ? "Ops, mine..." :
                        (board.isEmptyDetectorButPresentMines() ? "Ops, I have no charge, but mines present..." : "")));
    }


    private Direction handleDirectionCommand(char command) {
        if (command == 'w' || command == 'ц') {
            return Direction.UP;
        } else if (command == 'a' || command == 'ф') {
            return Direction.LEFT;
        } else if (command == 'd' || command == 'в') {
            return Direction.RIGHT;
        } else if (command == 's' || command == 'ы') {
            return Direction.DOWN;
        }
        return null;
    }

    public void consoleCommandHandler(char command) {
        if (command == 'r' || command == 'к') {
            printer.print(CHOOSE_DIRECTION_MINE_DETECTOR);
            board.useMineDetectorToGivenDirection(handleDirectionCommand(input.readCharacter()));
        } else  {
            board.sapperMoveTo(handleDirectionCommand(command));
        }
    }
}
