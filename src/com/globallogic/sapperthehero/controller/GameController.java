package com.globallogic.sapperthehero.controller;

import com.globallogic.sapperthehero.game.Board;
import com.globallogic.sapperthehero.game.Direction;

import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: oleksii.morozov
 * Date: 10/16/12
 * Time: 3:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class GameController {
    private static final String PLEASE_INPUT_ANOTHER_POSITIVE_INTEGER_NUMBER = "Please, input another positive integer number.";
    private static final String PLEASE_INPUT_POSITIVE_INTEGER_NUMBER = "Please, input positive integer number:";
    private static final String ENTER_BOARD_SIZE = "Введите размеры поля:";
    private static final String ENTER_NUMBER_OF_MINES_ON_BOARD = "Введите количество мина на поле:";
    private static final String AFTER_EACH_COMMAND_PRESS_ENTER = "After each command press ENTER";
    private static final String ВЫБЕРИ_НАПРАВЛЕНИЕ_ДЛЯ_РАЗМИНИРОВАНИЯ_W_S_A_D = "Выбери направление для разминирования - w s a d:";
    private static final String ENTER_NUMBER_OF_DETECTOR_CHARGE = "Введите количество зарядов детектора";

    public void startNewGameWithCheats() {
        startNewGame(true);
    }

    public void startNewGame() {
        startNewGame(false);
    }

    private void startNewGame(boolean cheats) {
        Board board;
        while (true) {
            try {
                System.out.println(ENTER_BOARD_SIZE);
                int boardSize = readNumberFromConsole();
                System.out.println(ENTER_NUMBER_OF_MINES_ON_BOARD);
                int mineCount = readNumberFromConsole();
                System.out.println(ENTER_NUMBER_OF_DETECTOR_CHARGE);
                int detectorCharge = readNumberFromConsole();
                board = new Board(boardSize, mineCount, detectorCharge);
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        printControls();
        System.out.println(AFTER_EACH_COMMAND_PRESS_ENTER);
        BoardPrinter boardPrint = new BoardPrinter(cheats);
        while (true) {
            boardPrint.printBoard(board);
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
                    System.out.println(ВЫБЕРИ_НАПРАВЛЕНИЕ_ДЛЯ_РАЗМИНИРОВАНИЯ_W_S_A_D);
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
        System.out.println("Пояснения к игре: \n");
        System.out.println("Клавиши ввода:");
        System.out.println("w - up");
        System.out.println("s - down");
        System.out.println("a - left");
        System.out.println("d - right");
        System.out.println("r - use detector");
        System.out.println("q - end game");
        System.out.println("\n Обозначения на доске:");
        System.out.println("@ - сапер");
        System.out.println("# - стена поля");
        System.out.println(". - свободная клетка");
        System.out.println("* - мина");
    }

    private int readNumberFromConsole() {
        System.out.println(PLEASE_INPUT_POSITIVE_INTEGER_NUMBER);
        while (true) {
            try {
                int inputNumber = Integer.parseInt(new Scanner(System.in).nextLine());
                if (inputNumber < 1) {
                    throw new Exception();
                }
                return inputNumber;
            } catch (Exception e) {
                System.out.println(PLEASE_INPUT_ANOTHER_POSITIVE_INTEGER_NUMBER);
            }
        }
    }
}
