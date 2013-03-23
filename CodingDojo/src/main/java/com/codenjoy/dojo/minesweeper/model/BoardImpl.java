package com.codenjoy.dojo.minesweeper.model;

import com.codenjoy.dojo.minesweeper.model.objects.*;
import com.codenjoy.dojo.minesweeper.services.MinesweeperEvents;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.Printer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BoardImpl implements Board {

    private List<CellImpl> cells;
    private int size;
    private Sapper sapper;
    private List<Mine> mines;
    private int turnCount = 0;
    private MinesGenerator minesGenerator;
    private EventListener listener;
    private boolean useDetector;
    private int maxScore;
    private int score;
    private int detectorCharge;
    private int minesCount;
    private Printer printer;

    public BoardImpl(int size, int minesCount, int detectorCharge,
                     MinesGenerator minesGenerator, EventListener listener) {
        if (size < 2) {
            throw new IllegalArgumentException();
        }
        if (minesCount > size * size - 1) {
            throw new IllegalArgumentException();
        }
        if (detectorCharge < minesCount) {
            throw new IllegalArgumentException();
        }
        this.size = size;
        printer = new MinesweeperPrinter(false, this);

        this.listener = listener; // TODO to use settings
        this.minesGenerator = minesGenerator;
        this.detectorCharge = detectorCharge;
        this.minesCount = minesCount;
    }

    private Sapper initializeSapper() {
        return new Sapper(1, 1);
    }

    private List<CellImpl> initializeBoardCells(int size) {
        List<CellImpl> result = new ArrayList<CellImpl>();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                result.add(new CellImpl(x, y));
            }
        }
        return result;
    }

    @Override
    public List<CellImpl> getFreeCells() {
        List<CellImpl> result = new LinkedList<CellImpl>();
        for (CellImpl cell : getCells()) {
            boolean isSapper = cell.equals(getSapper());
            boolean isMine = isMine(cell);
            if (!isSapper && !isMine) {
                result.add(cell);
            }
        }
        return result;
    }

    private boolean isMine(Cell cell) {
        boolean isMine = false;
        if (getMines() != null) {
            for (Mine mine : getMines()) {
                isMine |= cell.equals(mine);
            }
        }
        return isMine;
    }

    @Override
    public List<CellImpl> getCells() {
        return cells;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public Sapper getSapper() {
        return sapper;
    }

    @Override
    public List<Mine> getMines() {
        return mines;
    }

    @Override
    public int getMinesCount() {
        return getMines().size();
    }

    @Override
    public void sapperMoveTo(Direction direction) {
        if (isSapperCanMoveToDirection(direction)) {
            moveSapperAndFillFreeCell(direction);
            if (isSapperOnMine()) {
                sapper.die(true);
                fire(MinesweeperEvents.KILL_NO_MINE);
            }
            nextTurn();
        }
    }

    private void fire(MinesweeperEvents event) {
        if (listener != null) {
            listener.event(event.name());
        }
    }

    private void moveSapperAndFillFreeCell(Direction direction) {
        sapper.displaceMeByDelta(direction.getDeltaPosition());
    }

    private boolean isSapperCanMoveToDirection(Direction direction) {
        Cell result = getCellPossiblePosition(direction);
        return cells.contains(result);
    }

    private void nextTurn() {
        turnCount++;
    }

    @Override
    public boolean isSapperOnMine() {
        return getMines().contains(sapper);
    }

    @Override
    public Joystick getJoystick() {
        return new Joystick() {
            @Override
            public void down() {
                BoardImpl.this.act(Direction.DOWN);
            }

            @Override
            public void up() {
                BoardImpl.this.act(Direction.UP);
            }

            @Override
            public void left() {
                BoardImpl.this.act(Direction.LEFT);
            }

            @Override
            public void right() {
                BoardImpl.this.act(Direction.RIGHT);
            }

            @Override
            public void act() {
                useDetector = true;
            }
        };
    }

    private void act(Direction direction) {
        if (useDetector) {
            useMineDetectorToGivenDirection(direction);
            useDetector = false;
        } else {
            sapperMoveTo(direction);
        }
    }

    @Override
    public int getMaxScore() {
        return maxScore;
    }

    @Override
    public int getCurrentScore() {
        return score;
    }

    @Override
    public boolean isGameOver() {
        return sapper.isDead() || isEmptyDetectorButPresentMines() | isWin();
    }

    @Override
    public void newGame() {
        useDetector = false;
        maxScore = 0;
        score = 0;
        cells = initializeBoardCells(size);
        sapper = initializeSapper();
        sapper.iWantToHaveMineDetectorWithChargeNumber(detectorCharge);
        mines = minesGenerator.get(minesCount, this);
    }

    @Override
    public String getBoardAsString() {
        return printer.print();
    }

    @Override
    public Cell getCellPossiblePosition(Direction direction) {
        Cell result = sapper.clone();
        result.changeTo(direction.getDeltaPosition());
        return result;
    }

    @Override
    public Mine createMineOnPositionIfPossible(Cell cell) {
        Mine result = new Mine(cell);
        getMines().add(result);
        return result;
    }

    @Override
    public int getTurn() {
        return turnCount;
    }

    @Override
    public int getMinesNearSapper() {
        int result = 0;
        for (Direction direction : Direction.values()) {
            Cell sapperPossiblePosition = getCellPossiblePosition(direction);
            if (cells.contains(sapperPossiblePosition)
                    && getMines().contains(sapperPossiblePosition)) {
                result++;
            }
        }
        return result;
    }

    @Override
    public void useMineDetectorToGivenDirection(Direction direction) {
        Cell result = getCellPossiblePosition(direction);
        if (cells.contains(result)) {
            sapper.useMineDetector();
            if (getMines().contains(result)) {
                getMines().remove(result);
                increaseScore();
                fire(MinesweeperEvents.DESTROY_MINE);
            } else {
                fire(MinesweeperEvents.FORGET_CHARGE);
            }
            if (isEmptyDetectorButPresentMines()) {
                fire(MinesweeperEvents.NO_MORE_CHARGE);
            }
        }
    }

    private void increaseScore() {
        score++;
        maxScore = Math.max(score, maxScore);
    }

    @Override
    public boolean isEmptyDetectorButPresentMines() {
        return mines.size() != 0 && sapper.getMineDetectorCharge() == 0;
    }

    @Override
    public boolean isWin() {
        return mines.size() == 0 && !sapper.isDead();
    }

    @Override
    public void tick() {
        // do nothing
    }
}
