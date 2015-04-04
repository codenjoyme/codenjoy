package com.codenjoy.dojo.minesweeper.client.ai.vaa25.logic;


import com.codenjoy.dojo.minesweeper.client.ai.vaa25.logic.possibility.MinesAndCombinationAmountsOfIsland;
import com.codenjoy.dojo.minesweeper.client.ai.vaa25.logic.possibility.v2.IslandMinesCombs;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vlasov Alexander
 * Date: 01.08.2014
 * Time: 1:48
 * To change this template use File | Settings | File Templates.
 *
 * @author Alexander Vlasov
 */
public class Island {
    private List<Group> list = new ArrayList<Group>();
    private List<Cell> toOpen = new ArrayList<Cell>();
    private List<Cell> toMark = new ArrayList<Cell>();
    private List<Group> indefinite = new ArrayList<Group>();
    private int amountCells;
    private Deque<StringBuilder> stack = new LinkedList<StringBuilder>();
    private List<Cell> indefiniteCells;
    private double[] possibilities;
    private Integer[] countOfMines;
    private List<Cell> minPossibilities; // ячейки с минимальной вероятностью нахождения мины
    private MinesAndCombinationAmountsOfIsland mx0; //< количество мин, количество вариантов с таким количеством мин >
    private IslandMinesCombs islandMinesCombs;
    private double minPossibility;

    public Island(Group group) {
        add(group);
    }

    public void add(Group group) {
        list.add(group);
    }

    public void add(Island island) {
        list.addAll(island.list);
    }

    public boolean isCross(Group group) {
        for (Group group1 : list) {
            if (group.isCross(group1)) return true;
        }
        return false;
    }

    public void optimize() {
        boolean repeat;
        doFractionZeroGroupsInList();
        do {
            repeat = false;
            for (int i = 0; i < list.size() - 1; i++) {
                Group first = list.get(i);
                if (first.getValue() < 0) {
                    list.remove(i--);
                }
                for (int j = i + 1; j < list.size(); j++) {
                    Group second = list.get(j);
                    if (first != second && first.isCross(second)) {
                        if (first.equals(second)) {
                            list.remove(j--);
                            continue;
                        }
                        if (first.contains(second)) {
                            first.subtraction(second);
                            repeat = true;
                        } else if (second.contains(first)) {
                            second.subtraction(first);
                            repeat = true;
                        } else {
                            Group overlap = first.getOverlap(second);
                            if (overlap == null) {
                                overlap = second.getOverlap(first);
                                if (overlap != null) {
                                    second.subtraction(first);
                                    repeat = true;
                                    add(overlap);
                                }
                            } else {
                                first.subtraction(second);
                                repeat = true;
                                add(overlap);
                            }
                        }
                    }
                }
            }
        } while (repeat);
        determine();
        amountCells = size();
    }

    private void doFractionZeroGroupsInList() {
        for (int i = 0; i < list.size() - 1; i++) {
            Group first = list.get(i);
            if (first.size() > 1 && first.getValue() == 0) {
                for (Cell cell : first.getList()) {
                    List<Cell> cells = new ArrayList<Cell>(1);
                    cells.add(cell);
                    list.add(new Group(cells, 0));
                }
                list.remove(i--);
            }
        }
    }

    private void setIndefiniteCells() {
        indefiniteCells = new ArrayList<Cell>();
        for (Group group : indefinite) {
            for (Cell cell : group.getList()) {
                if (!indefiniteCells.contains(cell)) indefiniteCells.add(cell);
            }
        }
        amountCells = indefiniteCells.size() + toOpen.size() + toMark.size();
    }

    public int size() {
        return amountCells;
    }

    private void determine() {
        for (Group group : list) {
            if (group.getValue() == 0) {
                for (Cell cell : group.getList()) {
                    toOpen.add(cell);
                }
            } else if (group.size() == group.getValue()) {
                for (Cell cell : group.getList()) {
                    toMark.add(cell);
                }
            } else indefinite.add(group);
        }
        setIndefiniteCells();
    }

    public void resolveAlone() {
        for (Group group : indefinite) {
            group.setComb();
        }
        ListIterator<Group> iterator = indefinite.listIterator();
        makeTree(iterator);
        computePossibilities();

    }

    private void setPossibilities(int amountCombs) {
        for (int i = 0; i < countOfMines.length; i++) {
            indefiniteCells.get(i).setPossibility(100.0 * countOfMines[i] / amountCombs);
        }
    }

    private void makeTree(ListIterator<Group> iterator) {
        if (iterator.hasNext()) {
            Group group = iterator.next();
            int combAmount = group.getCombSize();
            for (int i = 0; i < combAmount; i++) {
                if (group.checkCombination(i)) {
                    group.storeCells();
                    group.setCellsComb(i);
                    if (checkIslandComb()) {
                        makeTree(iterator);
                    }
                    group.restoreCells();
                    if (iterator.hasPrevious()) iterator.previous();
                }
            }
        } else {
            storeComb();
        }
    }

    private boolean checkIslandComb() {
        for (Group group : list) {
            if (!group.checkCells()) {
                return false;
            }
        }
        return true;
    }

    private void storeComb() {
        StringBuilder combSB = new StringBuilder(list.size());
        for (Cell cell : indefiniteCells) {
            if (cell.isMine()) combSB.append(Group.MINE);
            if (cell.isValued()) combSB.append(Group.VALUED);
            if (cell.isUnknown()) combSB.append(Group.UNKNOWN);
        }
        stack.push(combSB);
    }

    private void computePossibilities() {
        int amountOfComb = stack.size();
        countOfMines = new Integer[indefiniteCells.size()];
        possibilities = new double[indefiniteCells.size()];
        mx0 = new MinesAndCombinationAmountsOfIsland();
        mx0.setIndefiniteCells(indefiniteCells);
        islandMinesCombs = new IslandMinesCombs(indefiniteCells);
        for (int i = 0; i < countOfMines.length; i++) {
            countOfMines[i] = 0;
        }
        for (int i = 0; i < amountOfComb; i++) {
            StringBuilder combSB = stack.pop();
            int[] mxCountOfMines = new int[indefiniteCells.size()];
            int mines = 0;
            for (int j = 0; j < indefiniteCells.size(); j++) {
                if (combSB.charAt(j) == Group.MINE) {
                    countOfMines[j]++;
                    mxCountOfMines[j]++;
                    mines++;
                }
            }
            mx0.inc(mines);
            mx0.addMxCountOfMines(mines, mxCountOfMines);
            islandMinesCombs.incCombsByMines(mines);
            islandMinesCombs.addArrayCombs(mines, mxCountOfMines);
        }
        setPossibilities(amountOfComb);
    }

    //    Getters

    public List<Cell> getIndefiniteCells() {
        return indefiniteCells;
    }

    public MinesAndCombinationAmountsOfIsland getMx0() {
        return mx0;
    }

    public IslandMinesCombs getIslandMinesCombs() {
        return islandMinesCombs;
    }

    public List<Cell> getToOpen() {
        return toOpen;
    }

    public List<Cell> getToMark() {
        return toMark;
    }

    @Override
    public String toString() {
        return "size=" + list.size();
    }
}
