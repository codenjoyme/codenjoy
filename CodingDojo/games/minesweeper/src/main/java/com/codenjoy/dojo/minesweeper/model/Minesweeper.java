package com.codenjoy.dojo.minesweeper.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.minesweeper.services.Events;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.QDirection;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.settings.Parameter;

import java.util.*;

public class Minesweeper implements Field {

    private List<Point> cells;
    private List<Mine> mines;
    private List<Mine> removedMines;
    private int turnCount = 0;
    private MinesGenerator minesGenerator;
    private int maxScore;
    private int score;
    private List<Wall> walls = new LinkedList<>();

    private Parameter<Integer> size;  // TODO это пооубирать отсюда, если изменяются настройки, надо пересобрать все игры
    private Parameter<Integer> detectorCharge;
    private Parameter<Integer> minesCount;

    private List<Flag> flags;
    private Map<Point, Integer> walkAt;
    private Integer currentSize;
    private Player player;

    public Minesweeper(Parameter<Integer> size, Parameter<Integer> minesCount, Parameter<Integer> detectorCharge,
                       MinesGenerator minesGenerator) {
        this.size = size;
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
    
    private List<Point> initializeBoardCells() {
        List<Point> result = new ArrayList<>();
        for (int x = 1; x < size.getValue() - 1; x++) {
            for (int y = 1; y < size.getValue() - 1; y++) {
                result.add(new Cell(x, y, this));
            }
        }
        return result;
    }

    @Override
    public List<Point> getFreeCells() {
        List<Point> result = new LinkedList<>();
        for (Point cell : getCells()) {
            boolean isSapper = cell.equals(sapper());
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
                player.getHero().die();
                openAllBoard();
                player.event(Events.KILL_ON_MINE);
            } else {
                if (cleaned) {
                    player.event(Events.CLEAN_BOARD);
                }
            }
            nextTurn();
        }
    }

    private boolean moveSapperAndFillFreeCell(Direction direction) {
        walkAt.put(sapper().copy(), getMinesNearSapper());
        sapper().change(direction);

        boolean wasHere = walkAt.containsKey(sapper());
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
        return getMines().contains(sapper());
    }

    @Override
    public Sapper sapper() {
        return player.getHero();
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
        return pt.equals(sapper());
    }

    @Override
    public int minesNear(Point pt) {
        Integer count = walkAt.get(pt);
        if (count == null) {
            return -1;
        }
        return count;
    }

    @Override
    public BoardReader reader() {
        return new BoardReader() {
            private int size = Minesweeper.this.size();

            @Override
            public int size() {
                return size;
            }

            @Override
            public Iterable<? extends Point> elements() {
                return new LinkedList<Point>() {{
                    add(Minesweeper.this.sapper());
                    addAll(Minesweeper.this.getMines());
                    addAll(Minesweeper.this.removedMines);
                    addAll(Minesweeper.this.getFlags());
                    addAll(Minesweeper.this.getCells());
                    addAll(Minesweeper.this.getWalls());
                }};
            }
        };
    }



    @Override
    public void newGame(Player player) {
        validate();
        this.player = player;
        flags = new LinkedList<>();
        walkAt = new HashMap<>();
        maxScore = 0;
        score = 0;
        cells = initializeBoardCells();
        player.newHero(this);
        sapper().iWantToHaveMineDetectorWithChargeNumber(detectorCharge.getValue());
        mines = minesGenerator.get(minesCount.getValue(), this);
        removedMines = new LinkedList<>();
        tick();
    }

    @Override
    public void remove(Player player) {
        this.player = null;
    }

    @Override
    public Point getCellPossiblePosition(Direction direction) {
        return direction.change(sapper().copy());
    }

    @Override
    public Mine createMineOnPositionIfPossible(Point cell) {
        Mine result = new Mine(cell);
        result.init(this);
        getMines().add(result);
        return result;
    }

    @Override
    public int getTurn() {
        return turnCount;
    }

    @Override
    public boolean isGameOver() {
        return !sapper().isAlive();
    }

    @Override
    public int getMinesNearSapper() {
        return getMinesNear(sapper());
    }

    private int getMinesNear(Point position) {
        int result = 0;
        for (QDirection direction : QDirection.values()) {
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
            if (sapper().isEmptyCharge()) {
                return;
            }

            if (flags.contains(result)) {
                return;
            }

            sapper().tryToUseDetector(new DetectorAction() {
                @Override
                public void used() {
                    flags.add(new Flag(result));
                    if (getMines().contains(result)) {
                        removeMine(result);
                    } else {
                        player.event(Events.FORGET_CHARGE);
                    }
                }
            });

            if (isEmptyDetectorButPresentMines()) {
                openAllBoard();
                player.event(Events.NO_MORE_CHARGE);
            }
        }
    }

    private void removeMine(Point result) {
        Mine mine = new Mine(result);
        mine.init(this);
        removedMines.add(mine);
        getMines().remove(result);
        increaseScore();
        recalculateWalkMap();
        player.event(Events.DESTROY_MINE);
        if (getMines().isEmpty()) {
            openAllBoard();
            player.event(Events.WIN);
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
        return getMines().size() != 0 && sapper().isEmptyCharge();
    }

    @Override
    public boolean isWin() {
        return getMines().size() == 0 && !sapper().isDead();
    }

    @Override
    public void tick() {
        if (currentSize != size.getValue()) {  // TODO потестить это
            currentSize = size.getValue();
            newGame(player);
            return;
        }

        sapper().tick();
    }

    public List<Wall> getWalls() {
        return walls;
    }

    public List<Flag> getFlags() {
        return flags;
    }
}
