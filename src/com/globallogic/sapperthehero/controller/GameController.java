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
    private static final String ENTER_NUMBER_OF_MINES_ON_BOARD = "Введите количество мин на поле:";
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
                print(ENTER_BOARD_SIZE);
                int boardSize = readNumberFromConsole();
                print(ENTER_NUMBER_OF_MINES_ON_BOARD);
                int mineCount = readNumberFromConsole();
                print(ENTER_NUMBER_OF_DETECTOR_CHARGE);
                int detectorCharge = readNumberFromConsole();
                board = new Board(boardSize, mineCount, detectorCharge);
                break;
            } catch (Exception e) {
                print(e.getMessage());
            }
        }
        printControls();
        print(AFTER_EACH_COMMAND_PRESS_ENTER);
        while (true) {

            String toPrint = new BoardPresenter(cheats, board).print();
            print(toPrint);
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
                    print(ВЫБЕРИ_НАПРАВЛЕНИЕ_ДЛЯ_РАЗМИНИРОВАНИЯ_W_S_A_D);
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

    private void print(String toPrint) {
        System.out.println(toPrint);
    }

    private void printControls() {
        print("Пояснения к игре: \n");
        print("Клавиши ввода:");
        print("w - up");
        print("s - down");
        print("a - left");
        print("d - right");
        print("r - use detector");
        print("q - end game");
        print("\n Обозначения на доске:");
        print("@ - сапер");
        print("# - стена поля");
        print(". - свободная клетка");
        print("* - мина");
    }

    private int readNumberFromConsole() {
        print(PLEASE_INPUT_POSITIVE_INTEGER_NUMBER);
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
