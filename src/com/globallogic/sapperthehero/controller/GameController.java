package com.globallogic.sapperthehero.controller;

import com.globallogic.sapperthehero.controller.input.Reader;
import com.globallogic.sapperthehero.controller.output.Printer;
import com.globallogic.sapperthehero.game.Board;
import com.globallogic.sapperthehero.game.Direction;
import com.globallogic.sapperthehero.game.impl.RandomMinesGenerator;

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
    private static final String CHOSE_DIRECTION_TO_DESTROING_MINE = "Выбери направление для разминирования - w s a d:";
    private static final String ENTER_NUMBER_OF_DETECTOR_CHARGE = "Введите количество зарядов детектора";
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
                    printer.print(CHOSE_DIRECTION_TO_DESTROING_MINE);
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
