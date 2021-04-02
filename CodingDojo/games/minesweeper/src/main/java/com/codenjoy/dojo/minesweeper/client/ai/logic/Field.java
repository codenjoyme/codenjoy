//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.codenjoy.dojo.minesweeper.client.ai.logic;

import com.codenjoy.dojo.minesweeper.client.ai.utils.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Field {
    private Point myCoord;
    public final int amount;
    public final int width;
    public final int height;
    private final com.codenjoy.dojo.minesweeper.client.ai.logic.Cell[][] field;
    private final List<com.codenjoy.dojo.minesweeper.client.ai.logic.Cell> cells;
    private PlayField playField;
    private List<com.codenjoy.dojo.minesweeper.client.ai.logic.Group> groups;
    private List<com.codenjoy.dojo.minesweeper.client.ai.logic.Island> islands;
    private List<com.codenjoy.dojo.minesweeper.client.ai.logic.Cell> toOpen;
    private List<com.codenjoy.dojo.minesweeper.client.ai.logic.Cell> toMark;
    double minPossibility;
    private boolean exploded;
    private int minesSetted;
    private int valued;

    public Field(PlayField playField) {
        this(playField.width, playField.height, playField.amount);
        this.playField = playField;
        this.scanPlayField();
    }

    public Field(int width, int height, int amount1) {
        this.groups = new ArrayList();
        this.toOpen = new ArrayList();
        this.toMark = new ArrayList();
        this.amount = amount1;
        this.width = width;
        this.height = height;
        this.cells = new LinkedList();
        this.islands = new ArrayList();
        this.field = new com.codenjoy.dojo.minesweeper.client.ai.logic.Cell[width][height];
        this.createCells();
        this.setCellsNeighbours();
    }

    public void setMyCoord(Point myCoord) {
        this.myCoord = myCoord;
    }

    private static void printStatistic(Map<Integer, Integer> whole, Map<Integer, Integer> real) {
        System.out.print("Theory  ");

        int i;
        for(i = 0; i < 101; ++i) {
            if (whole.containsKey(i)) {
                System.out.print(getNumber(i));
            }
        }

        System.out.println();
        System.out.print("Reality ");

        for(i = 0; i < 101; ++i) {
            if (whole.containsKey(i)) {
                int value = 0;
                if (real.containsKey(i)) {
                    value = (int)(100.0D * (double)(Integer)real.get(i) / (double)(Integer)whole.get(i));
                }

                System.out.print(getNumber(value));
            }
        }

        System.out.println();
        System.out.print("Clicked ");

        for(i = 0; i < 101; ++i) {
            if (whole.containsKey(i)) {
                System.out.print(getNumber((Integer)whole.get(i)));
            }
        }

        System.out.println();
    }

    private static String getNumber(int number) {
        if (number >= 1000) {
            return " " + number;
        } else if (number >= 100) {
            return "  " + number;
        } else if (number >= 10) {
            return "   " + number;
        } else {
            return number >= 0 ? "    " + number : null;
        }
    }

    private void createCells() {
        for(int x = 0; x < this.width; ++x) {
            for(int y = 0; y < this.height; ++y) {
                this.field[x][y] = new com.codenjoy.dojo.minesweeper.client.ai.logic.Cell(x, y);
                this.cells.add(this.field[x][y]);
            }
        }

    }

    private void setCellsNeighbours() {
        for(int x = 0; x < this.width; ++x) {
            for(int y = 0; y < this.height; ++y) {
                if (x > 0) {
                    this.field[x][y].addNeighbour(this.field[x - 1][y]);
                }

                if (y > 0) {
                    this.field[x][y].addNeighbour(this.field[x][y - 1]);
                }

                if (x > 0 && y > 0) {
                    this.field[x][y].addNeighbour(this.field[x - 1][y - 1]);
                }

                if (x < this.width - 1) {
                    this.field[x][y].addNeighbour(this.field[x + 1][y]);
                }

                if (y < this.height - 1) {
                    this.field[x][y].addNeighbour(this.field[x][y + 1]);
                }

                if (x < this.width - 1 && y < this.height - 1) {
                    this.field[x][y].addNeighbour(this.field[x + 1][y + 1]);
                }

                if (x > 0 && y < this.height - 1) {
                    this.field[x][y].addNeighbour(this.field[x - 1][y + 1]);
                }

                if (x < this.width - 1 && y > 0) {
                    this.field[x][y].addNeighbour(this.field[x + 1][y - 1]);
                }
            }
        }

    }

    private void scanPlayField() {
        this.minesSetted = 0;
        this.valued = 0;

        for(int x = 0; x < this.width; ++x) {
            for(int y = 0; y < this.height; ++y) {
                int value = this.playField.get(x, y);
                if (value != 10 && value != 12) {
                    if (value == 9) {
                        this.field[x][y].setUnknown();
                    } else if (value == 11) {
                        this.field[x][y].setMine();
                        ++this.minesSetted;
                    } else {
                        this.field[x][y].setValue(value);
                        ++this.valued;
                    }
                } else {
                    this.exploded = true;
                }
            }
        }

    }

    private void setGroups() {
        this.groups.clear();
        Iterator i$ = this.cells.iterator();

        while(i$.hasNext()) {
            com.codenjoy.dojo.minesweeper.client.ai.logic.Cell cell = (com.codenjoy.dojo.minesweeper.client.ai.logic.Cell)i$.next();
            if (cell.isValued() && cell.hasUnknownAround()) {
                this.groups.add(new com.codenjoy.dojo.minesweeper.client.ai.logic.Group(cell.getUnknownCells(), cell.getValue()));
            }
        }

    }

    private void optimizeIslands() {
        Iterator i$ = this.islands.iterator();

        while(i$.hasNext()) {
            com.codenjoy.dojo.minesweeper.client.ai.logic.Island island = (com.codenjoy.dojo.minesweeper.client.ai.logic.Island)i$.next();
            island.optimize();
        }

    }

    private void divideGroupsToIslands(List<com.codenjoy.dojo.minesweeper.client.ai.logic.Group> groups) {
        this.islands.clear();
        Iterator i$ = groups.iterator();

        while(i$.hasNext()) {
            com.codenjoy.dojo.minesweeper.client.ai.logic.Group group = (Group)i$.next();
            boolean added = false;
            com.codenjoy.dojo.minesweeper.client.ai.logic.Island addedTo = null;

            for(int i = 0; i < this.islands.size(); ++i) {
                com.codenjoy.dojo.minesweeper.client.ai.logic.Island currentIsland = (com.codenjoy.dojo.minesweeper.client.ai.logic.Island)this.islands.get(i);
                if (currentIsland.isCross(group)) {
                    if (!added) {
                        currentIsland.add(group);
                        added = true;
                        addedTo = currentIsland;
                    } else {
                        addedTo.add(currentIsland);
                        this.islands.remove(i);
                    }
                }
            }

            if (!added) {
                this.islands.add(new com.codenjoy.dojo.minesweeper.client.ai.logic.Island(group));
            }
        }

    }

    public void play(String user) {
        this.islands.clear();
        this.setGroups();
        this.divideGroupsToIslands(this.groups);
        this.optimizeIslands();
        this.determineMarkOpenIndefinite();
        this.filterReachableCells(this.toMark);
        this.filterReachableCells(this.toOpen);
        if (!this.hasDecision()) {
            if ("vaa25@yandex.ru".equals(user)) {
                Iterator i$ = this.islands.iterator();

                while(i$.hasNext()) {
                    com.codenjoy.dojo.minesweeper.client.ai.logic.Island island = (com.codenjoy.dojo.minesweeper.client.ai.logic.Island)i$.next();
                    island.resolve();
                }

                List<com.codenjoy.dojo.minesweeper.client.ai.logic.Cell> deepCells = this.getDeepCells();
                this.setPossibility(deepCells, 100.0D);
                List<com.codenjoy.dojo.minesweeper.client.ai.logic.Cell> minPosCells = this.getMinPosCells();
                this.minPossibility = minPosCells.size() == 0 ? 100.0D : ((com.codenjoy.dojo.minesweeper.client.ai.logic.Cell)minPosCells.get(0)).getPossibility();
                this.toOpen.addAll(minPosCells);
            } else {
                this.toOpen = this.getUnknownCells();
                this.filterReachableCells(this.toOpen);
            }
        }

    }

    private void filterReachableCells(List<com.codenjoy.dojo.minesweeper.client.ai.logic.Cell> cells) {
        for(int i = 0; i < cells.size(); ++i) {
            if (!this.isReachableCell((com.codenjoy.dojo.minesweeper.client.ai.logic.Cell)cells.get(i))) {
                cells.remove(i--);
            }
        }

    }

    private boolean isReachableCell(com.codenjoy.dojo.minesweeper.client.ai.logic.Cell cell) {
        int x = cell.getX();
        int y = cell.getY();
        if (x > 0 && !this.field[x - 1][y].isUnknown()) {
            return true;
        } else if (y > 0 && !this.field[x][y - 1].isUnknown()) {
            return true;
        } else if (x < this.width - 1 && !this.field[x + 1][y].isUnknown()) {
            return true;
        } else {
            return y < this.height - 1 && !this.field[x][y + 1].isUnknown();
        }
    }

    public Point[] getToOpen() {
        Point[] result = new Point[this.toOpen.size()];

        for(int i = 0; i < this.toOpen.size(); ++i) {
            result[i] = new Point(((com.codenjoy.dojo.minesweeper.client.ai.logic.Cell)this.toOpen.get(i)).getX(), ((com.codenjoy.dojo.minesweeper.client.ai.logic.Cell)this.toOpen.get(i)).getY());
        }

        return result;
    }

    public Point[] getToMark() {
        Point[] result = new Point[this.toMark.size()];

        for(int i = 0; i < this.toMark.size(); ++i) {
            result[i] = new Point(((com.codenjoy.dojo.minesweeper.client.ai.logic.Cell)this.toMark.get(i)).getX(), ((com.codenjoy.dojo.minesweeper.client.ai.logic.Cell)this.toMark.get(i)).getY());
        }

        return result;
    }

    private List<com.codenjoy.dojo.minesweeper.client.ai.logic.Cell> getMinPosCells() {
        List<com.codenjoy.dojo.minesweeper.client.ai.logic.Cell> result = new ArrayList();
        double min = 100.0D;
        Iterator i$ = this.cells.iterator();

        while(true) {
            com.codenjoy.dojo.minesweeper.client.ai.logic.Cell cell;
            do {
                do {
                    do {
                        if (!i$.hasNext()) {
                            return result;
                        }

                        cell = (com.codenjoy.dojo.minesweeper.client.ai.logic.Cell)i$.next();
                    } while(!cell.isUnknown());
                } while(!this.isReachableCell(cell));
            } while((Integer)cell.getCoords().getKey() == this.myCoord.getX() && (Integer)cell.getCoords().getValue() == this.myCoord.getY());

            if (cell.getPossibility() == min) {
                result.add(cell);
            } else if (cell.getPossibility() < min) {
                min = cell.getPossibility();
                result.clear();
                result.add(cell);
            }
        }
    }

    private void setPossibility(List<com.codenjoy.dojo.minesweeper.client.ai.logic.Cell> list, double possibility) {
        Iterator i$ = list.iterator();

        while(i$.hasNext()) {
            com.codenjoy.dojo.minesweeper.client.ai.logic.Cell cell = (com.codenjoy.dojo.minesweeper.client.ai.logic.Cell)i$.next();
            cell.setPossibility(possibility);
        }

    }

    private com.codenjoy.dojo.minesweeper.client.ai.logic.Cell getOneOf(List<com.codenjoy.dojo.minesweeper.client.ai.logic.Cell> list) {
        Random random = new Random();
        return (com.codenjoy.dojo.minesweeper.client.ai.logic.Cell)list.get(random.nextInt(list.size()));
    }

    private com.codenjoy.dojo.minesweeper.client.ai.logic.Cell getOneOf(List<com.codenjoy.dojo.minesweeper.client.ai.logic.Cell> list1, List<com.codenjoy.dojo.minesweeper.client.ai.logic.Cell> list2) {
        List<com.codenjoy.dojo.minesweeper.client.ai.logic.Cell> list = new ArrayList(list1);
        list.addAll(list2);
        return this.getOneOf(list);
    }

    private int countUnknownCells() {
        int res = 0;
        Iterator i$ = this.cells.iterator();

        while(i$.hasNext()) {
            com.codenjoy.dojo.minesweeper.client.ai.logic.Cell cell = (com.codenjoy.dojo.minesweeper.client.ai.logic.Cell)i$.next();
            if (cell.isUnknown()) {
                ++res;
            }
        }

        return res;
    }

    private List<com.codenjoy.dojo.minesweeper.client.ai.logic.Cell> getUnknownCells() {
        List<com.codenjoy.dojo.minesweeper.client.ai.logic.Cell> res = new ArrayList();
        Iterator i$ = this.cells.iterator();

        while(i$.hasNext()) {
            com.codenjoy.dojo.minesweeper.client.ai.logic.Cell cell = (com.codenjoy.dojo.minesweeper.client.ai.logic.Cell)i$.next();
            if (cell.isUnknown()) {
                res.add(cell);
            }
        }

        return res;
    }

    private int getDeepCellsAmount() {
        int u = this.countUnknownCells();

        com.codenjoy.dojo.minesweeper.client.ai.logic.Island island;
        for(Iterator i$ = this.islands.iterator(); i$.hasNext(); u -= island.size()) {
            island = (com.codenjoy.dojo.minesweeper.client.ai.logic.Island)i$.next();
        }

        return u;
    }

    private List<com.codenjoy.dojo.minesweeper.client.ai.logic.Cell> getDeepCells() {
        List<com.codenjoy.dojo.minesweeper.client.ai.logic.Cell> unknown = this.getUnknownCells();
        Iterator i$ = this.islands.iterator();

        while(i$.hasNext()) {
            com.codenjoy.dojo.minesweeper.client.ai.logic.Island island = (com.codenjoy.dojo.minesweeper.client.ai.logic.Island)i$.next();
            unknown.removeAll(island.getIndefiniteCells());
        }

        return unknown;
    }

    private void determineMarkOpenIndefinite() {
        Iterator i$ = this.islands.iterator();

        while(i$.hasNext()) {
            com.codenjoy.dojo.minesweeper.client.ai.logic.Island island = (Island)i$.next();
            this.toOpen.addAll(island.getToOpen());
            this.toMark.addAll(island.getToMark());
        }

    }

    private boolean hasDecision() {
        return this.toMark.size() > 0 || this.toOpen.size() > 0;
    }

    public void print() {
        System.out.println(this.fieldToString());
    }

    public String fieldToString() {
        StringBuilder result = new StringBuilder("0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9\n");

        for(int y = 0; y < this.height; ++y) {
            for(int x = 0; x < this.width; ++x) {
                com.codenjoy.dojo.minesweeper.client.ai.logic.Cell cell = this.field[x][y];
                if (cell.isMine()) {
                    result.append("* ");
                } else if (cell.isUnknown()) {
                    result.append("  ");
                } else {
                    result.append(cell.getValue()).append(" ");
                }
            }

            result.append(y).append('\n');
        }

        result.append("0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9\n");
        result.append('\n');
        return result.toString();
    }

    public String posToString() {
        StringBuilder result = new StringBuilder("0  1  2  3  4  5  6  7  8  9  10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29\n");

        for(int y = 0; y < this.height; ++y) {
            for(int x = 0; x < this.width; ++x) {
                com.codenjoy.dojo.minesweeper.client.ai.logic.Cell cell = this.field[x][y];
                if (cell.isMine()) {
                    result.append("*  ");
                } else if (cell.isUnknown()) {
                    long pos = Math.round(cell.getPossibility());
                    result.append(pos).append(pos > 9L ? " " : "  ");
                } else {
                    result.append(cell.getValue()).append(") ");
                }
            }

            result.append(y).append('\n');
        }

        result.append('\n');
        result.append("0  1  2  3  4  5  6  7  8  9  10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29\n");
        return result.toString();
    }

    public void printPossibilities() {
        System.out.println(this.posToString());
    }

    public void clear() {
        Iterator i$ = this.cells.iterator();

        while(i$.hasNext()) {
            com.codenjoy.dojo.minesweeper.client.ai.logic.Cell cell = (com.codenjoy.dojo.minesweeper.client.ai.logic.Cell)i$.next();
            cell.setUnknown();
        }

    }

    public void clear(int x, int y) {
        this.field[x][y].setUnknown();
    }

    public void set(int x, int y, int value) {
        if (value == 9) {
            this.field[x][y].setMine();
        }

        if (value < 9 && value >= 0) {
            this.field[x][y].setValue(value);
        }

    }

    public Point[] getMarked() {
        Point[] points = new Point[this.toMark.size()];

        for(int i = 0; i < this.toMark.size(); ++i) {
            points[i] = new Point(((com.codenjoy.dojo.minesweeper.client.ai.logic.Cell)this.toMark.get(i)).getX(), ((com.codenjoy.dojo.minesweeper.client.ai.logic.Cell)this.toMark.get(i)).getY());
        }

        return points;
    }

    public Point[] getOpen() {
        Point[] points = new Point[this.toOpen.size()];

        for(int i = 0; i < this.toOpen.size(); ++i) {
            points[i] = new Point(((com.codenjoy.dojo.minesweeper.client.ai.logic.Cell)this.toOpen.get(i)).getX(), ((Cell)this.toOpen.get(i)).getY());
        }

        return points;
    }

    public double getMinPossibility() {
        return this.minPossibility;
    }
}
