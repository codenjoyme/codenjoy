package com.globallogic.sapperthehero.game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Board {
    public static final String ВВЕДИТЕ_РАЗМЕРЫ_ПОЛЯ_БОЛЬШЕ_1 = "\n!!!!!!!!Введите размеры поля больше 1!!!!!!! \n";
    public static final String КОЛИЧЕСТВО_МИН_ДОЛЖНО_БЫТЬ_МЕНЬШЕ_ВСЕХ_КЛЕТОК_НА_ПОЛЕ_ТО_ЕСТЬ = "\n!!!!!!!!Количество мин должно быть меньше всех клеток на поле, то есть ";
    public static final String Я_ПОДОРВАЛСЯ_НА_МИНЕ_КОНЕЦ_ИГРЫ = "я подорвался на мине... конец игры...";
    public static final String ЗАКОНЧИЛИСЬ_ЗАРЯДЫ_У_ДЕТЕКТОРА_И_ОСТАЛИСЬ_МИНЫ_НА_ПОЛЕ_КОНЕЦ_ИГРЫ = "закончились заряды у детектора и остались мины на поле. конец игры...";
    public static final String Я_РАЗМИРИРОВАЛ_ПОСЛЕДНЮЮ_МИНУ_Я_ВЫИГРАЛ = "я размирировал последнюю мину. Я выиграл!";
    public static final String КОЛИЧЕСТВО_ЗАРЯДОВ_ДЕТЕКТОРА_ДОЛЖНО_БЫТЬ_БОЛЬШЕ_КОЛИЧЕСТВА_МИН_НА_ПОЛЕ = "\n!!!!!!Количество зарядов детектора должно быть больше количества мин на поле!!!!\n";

    private List<Cell> boardCells;
    private int boardSize;
    private Sapper sapper;
    private List<Mine> mines;
    private int turnCount = 0;

    public Board(int boardSize, int minesCount, int detectorCharge) {
        if (boardSize < 2) {
            throw new IllegalArgumentException(ВВЕДИТЕ_РАЗМЕРЫ_ПОЛЯ_БОЛЬШЕ_1);
        }
        if (minesCount > boardSize * boardSize - 1) {
            throw new IllegalArgumentException(КОЛИЧЕСТВО_МИН_ДОЛЖНО_БЫТЬ_МЕНЬШЕ_ВСЕХ_КЛЕТОК_НА_ПОЛЕ_ТО_ЕСТЬ + boardSize * boardSize + "x!!!!!!!!\n");
        }
        if (minesCount > boardSize * boardSize - 1) {
            throw new IllegalArgumentException(КОЛИЧЕСТВО_МИН_ДОЛЖНО_БЫТЬ_МЕНЬШЕ_ВСЕХ_КЛЕТОК_НА_ПОЛЕ_ТО_ЕСТЬ + boardSize * boardSize + "x!!!!!!!!\n");
        }
        if (detectorCharge < minesCount) {
            throw new IllegalArgumentException(КОЛИЧЕСТВО_ЗАРЯДОВ_ДЕТЕКТОРА_ДОЛЖНО_БЫТЬ_БОЛЬШЕ_КОЛИЧЕСТВА_МИН_НА_ПОЛЕ);
        }
        this.boardSize = boardSize;
        this.boardCells = initializeBoardCells(boardSize);
        this.sapper = initializeSapper();
        sapper.iWantToHaveMineDetectorWithChargeNumber(detectorCharge);
        this.mines = generateRandomPlacedMines(minesCount);
    }


    private Sapper initializeSapper() {
        return new Sapper(1, 1);
    }

    private List<Cell> initializeBoardCells(int boardSize) {
        List<Cell> result = new ArrayList<Cell>();
        for (int xPosition = 0; xPosition < boardSize; xPosition++) {
            for (int yPosition = 0; yPosition < boardSize; yPosition++) {
                result.add(new Cell(xPosition, yPosition));
            }
        }
        return result;
    }

    public List<Cell> getFreeCells() {
        List<Cell> result = new LinkedList<Cell>();
        for (Cell cell : boardCells) {
            boolean isSapper = cell.equals(sapper);
            boolean isMine = false;
            if (getMines() != null) {
                for (Mine mine : getMines()) {
                    isMine |= cell.equals(mine);
                }
            }
            if (!isSapper && !isMine) {
                result.add(cell);
            }
        }
        return result;
    }

    public List<Cell> getBoardCells() {
        return boardCells;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public Sapper getSapper() {
        return sapper;
    }

    public List<Mine> getMines() {
        return mines;
    }

    public int getMinesCount() {
        return getMines().size();
    }

    private List<Mine> generateRandomPlacedMines(int minesCount) {
        List<Mine> result = new ArrayList<Mine>();
        for (int index = 0; index < minesCount; index++) {
            result.add(new Mine(getRandomFreeCellOnBoard()));
        }
        return result;
    }

    private Cell getRandomFreeCellOnBoard() {
        if (!getFreeCells().isEmpty()) {
            int indexRandomFreePositionAtTile = new Random().nextInt(getFreeCells().size());
            Cell result = getFreeCells().get(indexRandomFreePositionAtTile);
            return result;
        }
        return null;
    }


    public void sapperMoveTo(Direction direction) {
        if (isSapperCanMoveToDirection(direction)) {
            moveSapperAndFillFreeCell(direction);
            if (isSapperOnMine()) {
                sapper.die(true);
                if (sapper.isDead()) {
                    System.out.println(Я_ПОДОРВАЛСЯ_НА_МИНЕ_КОНЕЦ_ИГРЫ);
                }
            }

            nextTurn();
        }
    }

    private void moveSapperAndFillFreeCell(Direction direction) {
        sapper.displaceMeByDelta(direction.getDeltaPosition());
    }

    private boolean isSapperCanMoveToDirection(Direction direction) {
        Cell result = getSapperPossiblePosition(direction);
        return boardCells.contains(result);
    }

    private void nextTurn() {
        turnCount++;
    }

    public boolean isSapperOnMine() {
        return getMines().contains(sapper);
    }

    public boolean isGameOver() {
        return sapper.isDead();
    }

    public Cell getSapperPossiblePosition(Direction direction) {
        Cell result = sapper.clone();
        result.changeMyCoordinate(direction.getDeltaPosition());
        return result;
    }

    public Mine createMineOnPositionIfPossible(Cell cell) {
        Mine result = new Mine(cell);
        getMines().add(result);
        return result;
    }

    public int getTurn() {
        return turnCount;
    }

    public int getMinesNearSapper() {
        int result = 0;
        for (Direction direction : Direction.values()) {
            Cell sapperPossiblePosition = getSapperPossiblePosition(direction);
            if (boardCells.contains(sapperPossiblePosition) && getMines().contains(sapperPossiblePosition)) {
                result++;
            }
        }
        return result;
    }

    public void useMineDetectorToGivenDirection(Direction direction) {
        Cell result = getSapperPossiblePosition(direction);
        if (boardCells.contains(result)) {
            sapper.useMineDetector();
            if (getMines().contains(result)) {
                destroyMine(result);
            }
            if (getMinesCount() != 0 && sapper.getMineDetectorCharge() == 0) {
                System.out.println(ЗАКОНЧИЛИСЬ_ЗАРЯДЫ_У_ДЕТЕКТОРА_И_ОСТАЛИСЬ_МИНЫ_НА_ПОЛЕ_КОНЕЦ_ИГРЫ);
            }
            if (getMinesCount() == 0 && !sapper.isDead()) {
                System.out.println(Я_РАЗМИРИРОВАЛ_ПОСЛЕДНЮЮ_МИНУ_Я_ВЫИГРАЛ);
            }

        }
    }

    private void destroyMine(Cell possibleMine) {
        getMines().remove(possibleMine);
    }

}
