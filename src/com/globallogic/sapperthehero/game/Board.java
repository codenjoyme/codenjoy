package com.globallogic.sapperthehero.game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Board {
    public static final String ВВЕДИТЕ_РАЗМЕРЫ_ПОЛЯ_БОЛЬШЕ_1 = "\n!!!!!!!!Введите размеры поля больше 1!!!!!!! \n";
    public static final String КОЛИЧЕСТВО_МИН_ДОЛЖНО_БЫТЬ_МЕНЬШЕ_ВСЕХ_КЛЕТОК_НА_ПОЛЕ_ТО_ЕСТЬ = "\n!!!!!!!!Количество мин должно быть меньше всех клеток на поле, то есть ";
    public static final String Я_ПОДОРВАЛСЯ_НА_МИНЕ_КОНЕЦ_ИГРЫ = "я подорвался на мине... конец игры...";
    public static final String ЗАКОНЧИЛИСЬ_ЗАРЯДЫ_У_ДЕТЕКТОРА_И_ОСТАЛИСЬ_МИНЫ_НА_ПОЛЕ_КОНЕЦ_ИГРЫ = "закончились заряды у детектора и остались мины на поле. конец игры...";
    public static final String Я_РАЗМИРИРОВАЛ_ПОСЛЕДНЮЮ_МИНУ_Я_ВЫИГРАЛ = "я размирировал последнюю мину. Я выиграл!";
    public static final String КОЛИЧЕСТВО_ЗАРЯДОВ_ДЕТЕКТОРА_ДОЛЖНО_БЫТЬ_БОЛЬШЕ_КОЛИЧЕСТВА_МИН_НА_ПОЛЕ = "\n!!!!!!Количество зарядов детектора должно быть больше количества мин на поле!!!!\n";

    private List<Cell> cells;
    private int size;
    private Sapper sapper;
    private List<Mine> mines;
    private int turnCount = 0;
    private MinesGenerator minesGenerator;

    public Board(int size, int minesCount, int detectorCharge, MinesGenerator minesGenerator) {
        if (size < 2) {
            throw new IllegalArgumentException(ВВЕДИТЕ_РАЗМЕРЫ_ПОЛЯ_БОЛЬШЕ_1);
        }
        if (minesCount > size * size - 1) {
            throw new IllegalArgumentException(КОЛИЧЕСТВО_МИН_ДОЛЖНО_БЫТЬ_МЕНЬШЕ_ВСЕХ_КЛЕТОК_НА_ПОЛЕ_ТО_ЕСТЬ + size * size + "x!!!!!!!!\n");
        }
        if (minesCount > size * size - 1) {
            throw new IllegalArgumentException(КОЛИЧЕСТВО_МИН_ДОЛЖНО_БЫТЬ_МЕНЬШЕ_ВСЕХ_КЛЕТОК_НА_ПОЛЕ_ТО_ЕСТЬ + size * size + "x!!!!!!!!\n");
        }
        if (detectorCharge < minesCount) {
            throw new IllegalArgumentException(КОЛИЧЕСТВО_ЗАРЯДОВ_ДЕТЕКТОРА_ДОЛЖНО_БЫТЬ_БОЛЬШЕ_КОЛИЧЕСТВА_МИН_НА_ПОЛЕ);
        }
        this.minesGenerator = minesGenerator;
        this.size = size;
        this.cells = initializeBoardCells(size);
        this.sapper = initializeSapper();
        sapper.iWantToHaveMineDetectorWithChargeNumber(detectorCharge);
        this.mines = this.minesGenerator.get(minesCount, this);
    }


    private Sapper initializeSapper() {
        return new Sapper(1, 1);
    }

    private List<Cell> initializeBoardCells(int size) {
        List<Cell> result = new ArrayList<Cell>();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                result.add(new Cell(x, y));
            }
        }
        return result;
    }

    public List<Cell> getFreeCells() {
        List<Cell> result = new LinkedList<Cell>();
        for (Cell cell : getCells()) {
            boolean isSapper = cell.equals(getSapper());
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

    public List<Cell> getCells() {
        return cells;
    }

    public int getSize() {
        return size;
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
        return cells.contains(result);
    }

    private void nextTurn() {
        turnCount++;
    }

    public boolean isSapperOnMine() {
        return getMines().contains(sapper);
    }

    // TODO протестить
    public boolean isGameOver() {
        return sapper.isDead() || isEmptyDetectorButPresentMines();
    }

    private boolean isEmptyDetectorButPresentMines() {
        return getMinesCount() != 0 && sapper.getMineDetectorCharge() == 0;
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
            if (cells.contains(sapperPossiblePosition) && getMines().contains(sapperPossiblePosition)) {
                result++;
            }
        }
        return result;
    }

    public void useMineDetectorToGivenDirection(Direction direction) {
        Cell result = getSapperPossiblePosition(direction);
        if (cells.contains(result)) {
            sapper.useMineDetector();
            if (getMines().contains(result)) {
                destroyMine(result);
            }
        }
    }

    //TODO протестить
    private boolean isWin() {
        return getMinesCount() == 0 && !sapper.isDead();
    }

    private void destroyMine(Cell possibleMine) {
        getMines().remove(possibleMine);
    }

}
