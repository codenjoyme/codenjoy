package com.codenjoy.dojo.minesweeper.client.ai.vaa25.logic;


import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vlasov Alexander
 * Date: 03.08.2014
 * Time: 10:41
 * To change this template use File | Settings | File Templates.
 *
 * @author Alexander Vlasov
 */
public class Field {
    private Point myCoord;
    public final int amount;
    public final int width, height;
    private final Cell[][] field;
    private final List<Cell> cells;
    private PlayField playField;
    private List<Group> groups = new ArrayList<Group>();
    private List<Island> islands;
    private List<Cell> toOpen = new ArrayList<Cell>();
    private List<Cell> toMark = new ArrayList<Cell>();
    double minPossibility;
    private boolean exploded;
    private int minesSetted;
    private int valued;

    public Field(PlayField playField) {
        this(playField.width, playField.height, playField.amount);
        this.playField = playField;
        scanPlayField();
    }

    public Field(int width, int height, int amount1) {
        this.amount = amount1;
        this.width = width;
        this.height = height;
        cells = new LinkedList<Cell>();
        islands = new ArrayList<Island>();
        field = new Cell[width][height];
        createCells();
        setCellsNeighbours();

    }

    public void setMyCoord(Point myCoord) {
        this.myCoord = myCoord;
    }

    private static void printStatistic(Map<Integer, Integer> whole, Map<Integer, Integer> real) {
        System.out.print("Theory  ");
        for (int i = 0; i < 101; i++) {
            if (whole.containsKey(i)) {
                System.out.print(getNumber(i));
            }
        }
        System.out.println();
        System.out.print("Reality ");
        for (int i = 0; i < 101; i++) {
            if (whole.containsKey(i)) {
                int value = 0;
                if (real.containsKey(i)) {
                    value = (int) (100.0 * real.get(i) / whole.get(i));
                }
                System.out.print(getNumber(value));
            }
        }
        System.out.println();
        System.out.print("Clicked ");
        for (int i = 0; i < 101; i++) {
            if (whole.containsKey(i)) {
                System.out.print(getNumber(whole.get(i)));
            }
        }
        System.out.println();
    }

    private static String getNumber(int number) {
        if (number >= 1000) return " " + number;
        if (number >= 100) return "  " + number;
        if (number >= 10) return "   " + number;
        if (number >= 0) return "    " + number;
        return null;
    }

    private void createCells() {
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++) {
                field[x][y] = new Cell(x, y);
                cells.add(field[x][y]);
            }

    }

    private void setCellsNeighbours() {
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++) {
                if (x > 0) field[x][y].addNeighbour(field[x - 1][y]);
                if (y > 0) field[x][y].addNeighbour(field[x][y - 1]);
                if (x > 0 && y > 0) field[x][y].addNeighbour(field[x - 1][y - 1]);
                if (x < width - 1) field[x][y].addNeighbour(field[x + 1][y]);
                if (y < height - 1) field[x][y].addNeighbour(field[x][y + 1]);
                if (x < width - 1 && y < height - 1) field[x][y].addNeighbour(field[x + 1][y + 1]);
                if (x > 0 && y < height - 1) field[x][y].addNeighbour(field[x - 1][y + 1]);
                if (x < width - 1 && y > 0) field[x][y].addNeighbour(field[x + 1][y - 1]);
            }

    }

    private void scanPlayField() {
        minesSetted = 0;
        valued = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int value = playField.get(x, y);
                if (value == PlayField.BOMB || value == PlayField.EXPLODE) exploded = true;
                else if (value == PlayField.UNOPENED) field[x][y].setUnknown();
                else if (value == PlayField.FLAG) {
                    field[x][y].setMine();
                    minesSetted++;
                } else {
                    field[x][y].setValue(value);
                    valued++;
                }
            }
        }
    }

    private boolean isWon() {
        for (Cell cell : cells) {
            if (cell.isUnknown()) return false;
        }
        return true;
    }

    private void setGroups() {
        groups.clear();
        for (Cell cell : cells) {
            if (cell.isValued() && cell.hasUnknownAround()) {
//                groups.add(new Group(cell.getUnknownCells(),cell.getValue()-cell.countMinesAround()));
                groups.add(new Group(cell.getUnknownCells(), cell.getValue()));
            }
        }
    }

    private void optimizeIslands() {
        for (Island island : islands) {
            island.optimize();
        }
    }

    private void divideGroupsToIslands(List<Group> groups) {
        islands.clear();
        for (Group group : groups) {
            boolean added = false;
            Island addedTo = null;
            for (int i = 0; i < islands.size(); i++) {
                Island currentIsland = islands.get(i);
                if (currentIsland.isCross(group)) {
                    if (!added) {
                        currentIsland.add(group);
                        added = true;
                        addedTo = currentIsland;
                    } else {
                        addedTo.add(currentIsland);
                        islands.remove(i);
                    }
                }
            }
            if (!added) {
                islands.add(new Island(group));
            }
        }

    }

    public void play() {

        islands.clear();
        setGroups();
        divideGroupsToIslands(groups);
        optimizeIslands();
        determineMarkOpenIndefinite();
        if (!hasDecision()) {
            if (isWon()) System.out.println("won");
            for (Island island : islands) {
                island.resolveAlone();
            }
            List<Cell> deepCells = getDeepCells();
            setPossibility(deepCells, 100);
            List<Cell> minPosCells = getMinPosCells();
            minPossibility = minPosCells.size() == 0 ? 100 : minPosCells.get(0).getPossibility();
            Cell toClick = getOneOf(minPosCells);
//            if (minPossibility > 99.99) {
//                toMark.addAll(minPosCells);
//            }else{
            toOpen.addAll(minPosCells);
//            }
        }
    }

    public Point[] getToOpen() {
        Point[] result = new Point[toOpen.size()];
        for (int i = 0; i < toOpen.size(); i++) {
            result[i] = new PointImpl(toOpen.get(i).getX(), toOpen.get(i).getY());
        }
        return result;
    }

    public Point[] getToMark() {
        Point[] result = new Point[toMark.size()];
        for (int i = 0; i < toMark.size(); i++) {
            result[i] = new PointImpl(toMark.get(i).getX(), toMark.get(i).getY());
        }
        return result;
    }

    private List<Cell> getMinPosCells() {
        List<Cell> result = new ArrayList<Cell>();
        double min = 100;
        for (Cell cell : cells) {
            if (cell.isUnknown()
                    && (cell.getCoords().getKey() != myCoord.getX()
                    || cell.getCoords().getValue() != myCoord.getY())) {
                if (cell.getPossibility() == min) {
                    result.add(cell);
                } else if (cell.getPossibility() < min) {
                    min = cell.getPossibility();
                    result.clear();
                    result.add(cell);
                }
            }
        }
        return result;
    }

    private void setPossibility(List<Cell> list, double possibility) {
        for (Cell cell : list) {
            cell.setPossibility(possibility);
        }
    }

    private List<Cell> igrunFilter(List<Cell> list) {
        List<Cell> result = new ArrayList<Cell>();
        for (Cell cell : list) {
            Pair<Integer, Integer> coord = cell.getCoords();
            int x = (int) coord.getKey();
            int y = (int) coord.getValue();
            if ((y > 0 && !field[x][y - 1].isMine()) && (y < height - 1 && !field[x][y + 1].isMine())) {
                result.add(cell);
            }
        }
        if (result.size() > 0) {
            return result;
        } else {
            return list;
        }
    }

    private Cell getOneOf(List<Cell> list) {
        list = igrunFilter(list);
        Random random = new Random();
        return list.get(random.nextInt(list.size()));
    }

    private Cell getOneOf(List<Cell> list1, List<Cell> list2) {
        List<Cell> list = new ArrayList<Cell>(list1);
        list.addAll(list2);
        return getOneOf(list);
    }

    private int countUnknownCells() {
        int res = 0;
        for (Cell cell : cells) {
            if (cell.isUnknown()) res++;
        }
        return res;
    }

    private List<Cell> getUnknownCells() {
        List<Cell> res = new ArrayList<Cell>();
        for (Cell cell : cells) {
            if (cell.isUnknown()) res.add(cell);
        }
        return res;
    }

    private int getDeepCellsAmount() {
        int u = countUnknownCells();
        for (Island island : islands) {
            u -= island.size();
        }
        return u;
    }

    private List<Cell> getDeepCells() {
        List<Cell> unknown = getUnknownCells();
        for (Island island : islands) {
            unknown.removeAll(island.getIndefiniteCells());
        }
        return unknown;
    }

    private void determineMarkOpenIndefinite() {
        for (Island island : islands) {
            toOpen.addAll(island.getToOpen());
            toMark.addAll(island.getToMark());
        }
    }

    private boolean hasDecision() {
        return /*toMark.size()>0||*/toOpen.size() > 0;
    }

    public void print() {
        System.out.println(fieldToString());
    }

    public String fieldToString() {
        StringBuilder result = new StringBuilder("0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9\n");
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Cell cell = field[x][y];
                if (cell.isMine()) result.append("* ");
                else if (cell.isUnknown()) result.append("  ");
                else result.append(cell.getValue()).append(" ");
            }
            result.append(y).append('\n');
        }
        result.append("0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9\n");
        result.append('\n');
        return result.toString();
    }

    public String posToString() {
        StringBuilder result = new StringBuilder("0  1  2  3  4  5  6  7  8  9  10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29\n");
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Cell cell = field[x][y];
                if (cell.isMine()) result.append("*  ");
                else if (cell.isUnknown()) {
                    long pos = Math.round(cell.getPossibility());
                    result.append(pos).append(pos > 9 ? " " : "  ");
                } else result.append(cell.getValue()).append(") ");
            }
            result.append(y).append('\n');

        }
        result.append('\n');
        result.append("0  1  2  3  4  5  6  7  8  9  10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29\n");
        return result.toString();
    }

    public void printPossibilities() {
        System.out.println(posToString());
    }

    /**
     * Устанавливает статус всех ячеек "неизвестный"
     */
    public void clear() {
        for (Cell cell : cells) {
            cell.setUnknown();
        }
    }

    /**
     * Устанавливает статус ячейки "неизвестный"
     *
     * @param x координата Х
     * @param y координата У
     */
    public void clear(int x, int y) {
        field[x][y].setUnknown();
    }

    /**
     * Устанавливает статус или значение заданной ячейки в зависимости от value
     *
     * @param x     координата Х
     * @param y     координата У
     * @param value 0-8 - значение открытой ячейки
     *              9 - ячейка с миной
     */
    public void set(int x, int y, int value) {
        if (value == 9) field[x][y].setMine();
        if (value < 9 && value >= 0) field[x][y].setValue(value);
    }

    /**
     * Возвращает массив новых помеченных ячеек.
     *
     * @return
     */
    public Point[] getMarked() {
        Point[] points = new Point[toMark.size()];
        for (int i = 0; i < toMark.size(); i++) {
            points[i] = new PointImpl(toMark.get(i).getX(), toMark.get(i).getY());
        }
        return points;
    }

    /**
     * Возвращает массив рекомендуемых ходов.
     *
     * @return координаты рекомендуемых ходов, или null, если все ячейки уже открыты
     */
    public Point[] getOpen() {
        Point[] points = new Point[toOpen.size()];
        for (int i = 0; i < toOpen.size(); i++) {
            points[i] = new PointImpl(toOpen.get(i).getX(), toOpen.get(i).getY());
        }
        return points;
    }


    public double getMinPossibility() {
        return minPossibility;
    }
}
