package com.codenjoy.dojo.minesweeper.model;

import com.codenjoy.dojo.minesweeper.services.MinesweeperEvents;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.settings.Parameter;

import java.util.*;

public class Minesweeper implements Field {

    private List<Point> cells;
    private Sapper sapper;
    private List<Mine> mines;
    private List<Mine> removedMines;
    private int turnCount = 0;
    private MinesGenerator minesGenerator;
    private EventListener listener;
    private boolean useDetector;
    private int maxScore;
    private int score;
    private List<Wall> walls = new LinkedList<Wall>();

    private Parameter<Integer> size;  // TODO это пооубирать отсюда, если изменяются настройки, надо пересобрать все игры
    private Parameter<Integer> detectorCharge;
    private Parameter<Integer> minesCount;

    private Printer printer;
    private List<Flag> flags;
    private Map<Point, Integer> walkAt;
    private Direction nextStep;
    private Integer currentSize;

    public Minesweeper(Parameter<Integer> size, Parameter<Integer> minesCount, Parameter<Integer> detectorCharge,
                       MinesGenerator minesGenerator, EventListener listener) {
        this.size = size;
        this.listener = listener; // TODO to use settings
        this.minesGenerator = minesGenerator;
        this.detectorCharge = detectorCharge;
        this.minesCount = minesCount;
        buildWalls();
    }

    private void buildWalls() {
        for (int i = 0; i < size(); i++) {
            walls.add(new Wall(0, i));
            walls.add(new Wall(size() - 1, i));

            walls.add(new Wall(i, 0));
            walls.add(new Wall(i, size() - 1));
        }
    }

    private void validate() {
        if (size.getValue() < 5) {
            size.update(5);
        }

        while (minesCount.getValue() > ((size.getValue() - 1) * (size.getValue() - 1) - 1)) {
            minesCount.update(minesCount.getValue() / 2);
        }

        if (detectorCharge.getValue() < minesCount.getValue()) {
            detectorCharge.update(minesCount.getValue());
        }
    }

    protected Sapper initializeSapper() {
        Sapper s = new Sapper(1, 1);
        s.setBoard(this);
        return s;
    }

    private List<Point> initializeBoardCells() {
        List<Point> result = new ArrayList<Point>();
        for (int x = 1; x < size.getValue() - 1; x++) {
            for (int y = 1; y < size.getValue() - 1; y++) {
                result.add(new Cell(x, y, this));
            }
        }
        return result;
    }

    @Override
    public List<Point> getFreeCells() {
        List<Point> result = new LinkedList<Point>();
        for (Point cell : getCells()) {
            boolean isSapper = cell.equals(getSapper());
            boolean isBoard = cell.getX() == 0 || cell.getY() == 0 || cell.getX() == size.getValue() - 1 || cell.getY() == size.getValue() - 1;  // TODO test me
            boolean isMine = isMine(cell);
            if (!isSapper && !isMine && !isBoard) {
                result.add(cell);
            }
        }
        return result;
    }

    @Override
    public List<Point> getCells() {
        return cells;
    }

    @Override
    public int size() {
        return size.getValue();
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
            boolean cleaned = moveSapperAndFillFreeCell(direction);
            if (isSapperOnMine()) {
                sapper.die();
                openAllBoard();
                fire(MinesweeperEvents.KILL_ON_MINE);
            } else {
                if (cleaned) {
                    fire(MinesweeperEvents.CLEAN_BOARD);
                }
            }
            nextTurn();
        }
    }

    private void fire(MinesweeperEvents event) {
        if (listener != null) {
            listener.event(event);
        }
    }

    private boolean moveSapperAndFillFreeCell(Direction direction) {
        walkAt.put(sapper.copy(), getMinesNearSapper());
        direction.change(sapper);

        boolean wasHere = walkAt.containsKey(sapper.copy());
        return !wasHere;
    }

    private boolean isSapperCanMoveToDirection(Direction direction) {
        Point cell = getCellPossiblePosition(direction);
        return cells.contains(cell);
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
                nextStep = Direction.DOWN;
            }

            @Override
            public void up() {
                nextStep = Direction.UP;
            }

            @Override
            public void left() {
                nextStep = Direction.LEFT;
            }

            @Override
            public void right() {
                nextStep = Direction.RIGHT;
            }

            @Override
            public void act(int... p) {
                useDetector = true;
            }
        };
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
            return sapper.isDead() || isEmptyDetectorButPresentMines() || isWin();
        }

    @Override
    public boolean isMine(Point pt) {
        if (getMines() == null) return false;
        return getMines().contains(pt) || (isGameOver() && removedMines.contains(pt));
    }

    @Override
    public boolean walkAt(Point pt) {
        return walkAt.containsKey(pt);
    }

    @Override
    public boolean isFlag(Point pt) {
        return flags.contains(pt);
    }

    @Override
    public boolean isSapper(Point pt) {
        return pt.equals(getSapper());
    }

    @Override
    public int minesNear(Point pt) {
        Integer count = walkAt.get(pt);
        if (count == null) {
            return -1;
        }
        return count;
    }

    public BoardReader reader() {
        return new BoardReader() {
            private int size = Minesweeper.this.size();

            @Override
            public int size() {
                return size;
            }

            @Override
            public Iterable<? extends Point> elements() {
                List<Point> result = new LinkedList<Point>();
                result.add(Minesweeper.this.getSapper());
                result.addAll(Minesweeper.this.getMines());
                result.addAll(Minesweeper.this.removedMines);
                result.addAll(Minesweeper.this.getFlags());
                result.addAll(Minesweeper.this.getCells());
                result.addAll(Minesweeper.this.getWalls());
                return result;
            }
        };
    }

    @Override
    public void newGame() {
        validate();
        flags = new LinkedList<Flag>();
        walkAt = new HashMap<Point, Integer>();
        printer = Printer.getFullFor(this.reader(), null, Elements.NONE);
        useDetector = false;
        maxScore = 0;
        score = 0;
        cells = initializeBoardCells();
        sapper = initializeSapper();
        sapper.iWantToHaveMineDetectorWithChargeNumber(detectorCharge.getValue());
        mines = minesGenerator.get(minesCount.getValue(), this);
        removedMines = new LinkedList<Mine>();
        tick();
    }

    @Override
    public String getBoardAsString() {
        return printer.toString();
    }

    @Override
    public void destroy() {
        // do nothing
    }

    @Override
    public void clearScore() {  // TODO test me
        maxScore = 0;
        score = 0;
    }

    @Override
    public Point getHero() {
        return sapper;
    }

    @Override
    public Point getCellPossiblePosition(Direction direction) {
        return direction.change(sapper.copy());
    }

    @Override
    public Mine createMineOnPositionIfPossible(Point cell) {
        Mine result = new Mine(cell);
        result.setBoard(this);
        getMines().add(result);
        return result;
    }

    @Override
    public int getTurn() {
        return turnCount;
    }

    @Override
    public int getMinesNearSapper() {
        return getMinesNear(sapper);
    }

    private int getMinesNear(Point position) {
        int result = 0;
        for (Direction direction : Direction.values()) {
            Point newPosition = direction.change(position.copy());
            if (cells.contains(newPosition) && getMines().contains(newPosition)) {
                result++;
            }
        }
        return result;
    }

    @Override
    public void useMineDetectorToGivenDirection(Direction direction) {
        final Point result = getCellPossiblePosition(direction);
        if (cells.contains(result)) {
            if (sapper.isEmptyCharge()) {
                return;
            }

            if (flags.contains(result)) {
                return;
            }

            sapper.tryToUseDetector(new DetectorAction() {
                @Override
                public void used() {
                    flags.add(new Flag(result));
                    if (getMines().contains(result)) {
                        removeMine(result);
                    } else {
                        fire(MinesweeperEvents.FORGET_CHARGE);
                    }
                }
            });

            if (isEmptyDetectorButPresentMines()) {
                openAllBoard();
                fire(MinesweeperEvents.NO_MORE_CHARGE);
            }
        }
    }

    private void removeMine(Point result) {
        Mine mine = new Mine(result);
        mine.setBoard(this);
        removedMines.add(mine);
        getMines().remove(result);
        increaseScore();
        recalculateWalkMap();
        fire(MinesweeperEvents.DESTROY_MINE);
        if (getMines().isEmpty()) {
            openAllBoard();
            fire(MinesweeperEvents.WIN);
        }
    }

    private void openAllBoard() {
        walkAt.clear();

        for (Point cell : getCells())  {
            walkAt.put(cell, getMinesNear(cell));
        }
    }

    private void recalculateWalkMap() {
        for (Map.Entry<Point, Integer> entry : walkAt.entrySet()) {
            entry.setValue(getMinesNear(entry.getKey()));
        }
    }

    private void increaseScore() {
        score++;
        maxScore = Math.max(score, maxScore);
    }

    @Override
    public boolean isEmptyDetectorButPresentMines() {
        return getMines().size() != 0 && sapper.isEmptyCharge();
    }

    @Override
    public boolean isWin() {
        return getMines().size() == 0 && !sapper.isDead();
    }

    @Override
    public void tick() {
        if (currentSize != size.getValue()) {  // TODO потестить это
            currentSize = size.getValue();
            newGame();
            return;
        }

        if (nextStep == null) {
            return;
        }

        if (useDetector) {
            useMineDetectorToGivenDirection(nextStep);
            useDetector = false;
        } else {
            sapperMoveTo(nextStep);
        }

        nextStep = null;
    }

    public List<Wall> getWalls() {
        return walls;
    }

    public List<Flag> getFlags() {
        return flags;
    }
}
