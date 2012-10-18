package com.globallogic.sapperthehero.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {
    public static final String ВВЕДИТЕ_РАЗМЕРЫ_ПОЛЯ_БОЛЬШЕ_1 = "\n!!!!!!!!Введите размеры поля больше 1!!!!!!! \n";
    public static final String КОЛИЧЕСТВО_МИН_ДОЛЖНО_БЫТЬ_МЕНЬШЕ_ВСЕХ_КЛЕТОК_НА_ПОЛЕ_ТО_ЕСТЬ = "\n!!!!!!!!Количество мин должно быть меньше всех клеток на поле, то есть ";
    public static final String Я_ПОДОРВАЛСЯ_НА_МИНЕ_КОНЕЦ_ИГРЫ = "я подорвался на мине... конец игры...";
    public static final String ЗАКОНЧИЛИСЬ_ЗАРЯДЫ_У_ДЕТЕКТОРА_И_ОСТАЛИСЬ_МИНЫ_НА_ПОЛЕ_КОНЕЦ_ИГРЫ = "закончились заряды у детектора и остались мины на поле. конец игры...";
    public static final String Я_РАЗМИРИРОВАЛ_ПОСЛЕДНЮЮ_МИНУ_Я_ВЫИГРАЛ = "я размирировал последнюю мину. Я выиграл!";
    public static final String КОЛИЧЕСТВО_ЗАРЯДОВ_ДЕТЕКТОРА_ДОЛЖНО_БЫТЬ_БОЛЬШЕ_КОЛИЧЕСТВА_МИН_НА_ПОЛЕ = "\n!!!!!!Количество зарядов детектора должно быть больше количества мин на поле!!!!\n";
    private List<Cell> freeCells;
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
        this.freeCells = initializeBoardCells(boardSize);
        this.boardCells = initializeBoardCells(boardSize);
        this.sapper = initializeSapper();
        sapper.iWantToHaveMineDetectorWithChargeNumber(detectorCharge);
        this.mines = generateRandomPlacedMines(minesCount);
    }


    private Sapper initializeSapper() {
        Sapper sapperTemporary = new Sapper(1, 1);
        removeFreeCell(sapperTemporary);
        return sapperTemporary;
    }

    private List<Cell> initializeBoardCells(int boardSize) {
        List<Cell> cells = new ArrayList<Cell>();
        for (int xPosition = 0; xPosition < boardSize; xPosition++) {
            for (int yPosition = 0; yPosition < boardSize; yPosition++) {
                cells.add(new Cell(xPosition, yPosition));
            }
        }
        return cells;
    }

    public List<Cell> getFreeCells() {
        return freeCells;
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
        return mines.size();
    }

    private List<Mine> generateRandomPlacedMines(int minesCount) {
        List<Mine> minesTemporary = new ArrayList<Mine>();
        for (int index = 0; index < minesCount; index++) {
            minesTemporary.add(new Mine(getRandomFreeCellOnBoard()));
        }
        return minesTemporary;
    }

    private Cell getRandomFreeCellOnBoard() {
        if (!freeCells.isEmpty()) {
            int indexRandomFreePositionAtTile = new Random().nextInt(freeCells.size());
            Cell randomFreeCellOnBoard = freeCells.get(indexRandomFreePositionAtTile);
            removeFreeCell(randomFreeCellOnBoard);
            return randomFreeCellOnBoard;
        }
        return null;
    }

    private void removeFreeCell(Cell cell) {
        freeCells.remove(cell);
    }

    private void addFreeCell(Cell cell) {
        freeCells.add(new Cell(cell));
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
        addFreeCell(sapper);
        sapper.displaceMeByDelta(direction.getDeltaPosition());
        removeFreeCell(sapper);
    }

    private boolean isSapperCanMoveToDirection(Direction direction) {
        Cell sapperPossiblePosition = getSapperPossiblePosition(direction);
        return boardCells.contains(sapperPossiblePosition);
    }

    private void nextTurn() {
        turnCount++;
    }

    public boolean isSapperOnMine() {
        return mines.contains(sapper);
    }

    public boolean isGameOver() {
        return sapper.isDead();
    }

    public Cell getSapperPossiblePosition(Direction direction) {
        Cell temporarySupperPosition = sapper.clone();
        temporarySupperPosition.changeMyCoordinate(direction.getDeltaPosition());
        return temporarySupperPosition;
    }

    public Mine createMineOnPositionIfPossible(Cell cell) {
        Mine mine = new Mine(cell);
        removeFreeCell(mine);
        mines.add(mine);
        return mine;
    }

    public int getTurn() {
        return turnCount;
    }

    public int getMinesNearSapper() {
        int minesNearSapper = 0;
        for (Direction direction : Direction.values()) {
            Cell sapperPossiblePosition = getSapperPossiblePosition(direction);
            if (boardCells.contains(sapperPossiblePosition) && mines.contains(sapperPossiblePosition)) {
                minesNearSapper++;
            }
        }
        return minesNearSapper;
    }

    public void useMineDetectorToGivenDirection(Direction direction) {
        Cell possibleMine = getSapperPossiblePosition(direction);
        if (boardCells.contains(possibleMine)) {
            sapper.useMineDetector();
            if (mines.contains(possibleMine)) {
                destroyMine(possibleMine);
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
        mines.remove(possibleMine);
        addFreeCell(possibleMine);
    }
}
