package com.globallogic.training.oleksii.morozov.sapperthehero.controller;

import com.globallogic.training.oleksii.morozov.sapperthehero.controller.input.Reader;
import com.globallogic.training.oleksii.morozov.sapperthehero.controller.output.Printer;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.Board;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.Direction;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.minegenerator.impl.RandomMinesGenerator;

import java.util.Scanner;

/**
 * User: oleksii.morozov
 * Date: 10/16/12
 * Time: 3:33 PM
 */
public class GameController {
    private static final String PLEASE_INPUT_ANOTHER_POSITIVE_INTEGER_NUMBER = "Please, input another positive integer number.";
    private static final String PLEASE_INPUT_POSITIVE_INTEGER_NUMBER = "Please, input positive integer number:";
    private static final String ENTER_BOARD_SIZE = "Введите размеры поля:";
    private static final String ENTER_NUMBER_OF_MINES_ON_BOARD = "Введите количество мин на поле:";
    private static final String AFTER_EACH_COMMAND_PRESS_ENTER = "After each command press ENTER";
    private static final String SELECT_DIRECTION = "Выбери направление для разминирования - w s a d:";
    private static final String ENTER_NUMBER_OF_DETECTOR_CHARGE = "Введите количество зарядов детектора";

    private static final String GOT_MINE = "я подорвался на мине... конец игры...";
    private static final String EMPTY_CHARGE = "закончились заряды у детектора и остались мины на поле. конец игры...";
    private static final String NO_MORE_MINES = "я размирировал последнюю мину. Я выиграл!";

    private Board board;
    private Printer printer;
    private Reader input;

    public GameController(Printer printer, Reader input) {
        this.printer = printer;
        this.input = input;
        input.setPrinter(printer);
    }

    public void startNewGame() {
        while (true) {
            try {
                int boardSize = input.read(ENTER_BOARD_SIZE);
                int mineCount = input.read(ENTER_NUMBER_OF_MINES_ON_BOARD);
                int detectorCharge = input.read(ENTER_NUMBER_OF_DETECTOR_CHARGE);

                board = new Board(boardSize, mineCount, detectorCharge,
                        new RandomMinesGenerator());
                break;
            } catch (IllegalArgumentException exception) {
                printer.print(exception.getMessage());
            }
        }
        printControls();
        printer.print(AFTER_EACH_COMMAND_PRESS_ENTER);
        while (true) {
            if (board.getSapper().isDead()) {
                printer.print(GOT_MINE);
                System.exit(0);
            }
            if (board.isEmptyDetectorButPresentMines()) {
                printer.print(EMPTY_CHARGE);
                System.exit(0);
            }
            if (board.isWin()) {
                printer.print(NO_MORE_MINES);
                System.exit(0);
            }
            String toPrint = new BoardPresenter(board).print();
            printer.print(toPrint);
            Scanner scanner = new Scanner(System.in);
            String inputStream = scanner.nextLine();
            if (inputStream.equals("w")) {
                board.sapperMoveTo(Direction.UP);
            } else if (inputStream.equals("s")) {
                board.sapperMoveTo(Direction.DOWN);
            } else if (inputStream.equals("a")) {
                board.sapperMoveTo(Direction.LEFT);
            } else if (inputStream.equals("d")) {
                board.sapperMoveTo(Direction.RIGHT);
            } else if (inputStream.equals("r")) {
                while (true) {
                    printer.print(SELECT_DIRECTION);
                    String checkMineDirection = scanner.nextLine();
                    if (checkMineDirection.equals("w")) {
                        board.useMineDetectorToGivenDirection(Direction.UP);
                    } else if (checkMineDirection.equals("s")) {
                        board.useMineDetectorToGivenDirection(Direction.DOWN);
                    } else if (checkMineDirection.equals("a")) {
                        board.useMineDetectorToGivenDirection(Direction.LEFT);
                    } else if (checkMineDirection.equals("d")) {
                        board.useMineDetectorToGivenDirection(Direction.RIGHT);
                    } else {
                        continue;
                    }
                    break;
                }
            } else if (inputStream.equals("q")) {
                System.exit(0);
            }
        }
    }

    private void printControls() {
        printer.print("Пояснения к игре: \n");
        printer.print("Клавиши ввода:");
        printer.print("w - up");
        printer.print("s - down");
        printer.print("a - left");
        printer.print("d - right");
        printer.print("r - use detector");
        printer.print("q - end game");
        printer.print("\n Обозначения на доске:");
        printer.print("@ - сапер");
        printer.print("# - стена поля");
        printer.print(". - свободная клетка");
        printer.print("* - мина");
    }

}
