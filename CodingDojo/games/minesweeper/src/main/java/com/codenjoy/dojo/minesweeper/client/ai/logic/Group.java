package com.codenjoy.dojo.minesweeper.client.ai.logic;

import java.util.*;

public class Group {

    private final List<Cell> list;
    private int value;
    private Integer[] comb;
    private StringBuilder[] combinations;
    private final Deque<StringBuilder> stack;

    public Group(List<Cell> cells, int value) {
        list = new ArrayList(cells);
        this.value = value;
        stack = new LinkedList();
    }

    public List<Cell> getList() {
        return list;
    }

    public int size() {
        return list.size();
    }

    public int getValue() {
        return value;
    }

    public boolean isCross(Group group) {
        Iterator i$ = group.list.iterator();

        Cell cell;
        do {
            if (!i$.hasNext()) {
                return false;
            }

            cell = (Cell) i$.next();
        } while (!list.contains(cell));

        return true;
    }

    public int getCombSize() {
        return comb.length;
    }

    public void setComb() {
        comb = Sequence6.getSequensed(value, size());
        List<StringBuilder> combStrings = new ArrayList(comb.length);
        Integer[] arr$ = comb;
        int len$ = arr$.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            Integer integer = arr$[i$];
            StringBuilder combSB = new StringBuilder(comb.length);
            String binary = Integer.toBinaryString(integer);
            int lastChar = list.size() - 1;
            int binaryLength = binary.length();

            for (int k = 0; k < list.size(); ++k) {
                if (lastChar - k >= binaryLength) {
                    combSB.append('0');
                } else {
                    combSB.append(binary.charAt(binaryLength - lastChar - 1 + k));
                }
            }

            combStrings.add(combSB);
        }

        combinations = new StringBuilder[combStrings.size()];
        combStrings.toArray(combinations);
    }

    public boolean checkCombination(int index) {
        StringBuilder comb = combinations[index];

        for (int i = 0; i < comb.length(); ++i) {
            char ch = comb.charAt(i);
            if (ch == '1' && list.get(i).isValued() || ch == '0' && list.get(i).isMine()) {
                return false;
            }
        }

        return true;
    }

    public boolean checkCells() {
        int mayBeValued = list.size() - value;
        int mayBeMined = value;
        int valued = 0;
        int mined = 0;
        Iterator i$ = list.iterator();

        while (i$.hasNext()) {
            Cell cell = (Cell) i$.next();
            if (cell.isValued()) {
                ++valued;
                if (valued > mayBeValued) {
                    return false;
                }
            } else if (cell.isMine()) {
                ++mined;
                if (mined > mayBeMined) {
                    return false;
                }
            }
        }

        return true;
    }

    public void storeCells() {
        StringBuilder combSB = new StringBuilder(list.size());
        Iterator i$ = list.iterator();

        while (i$.hasNext()) {
            Cell cell = (Cell) i$.next();
            if (cell.isMine()) {
                combSB.append('1');
            } else if (cell.isValued()) {
                combSB.append('0');
            } else if (cell.isUnknown()) {
                combSB.append('2');
            }
        }

        stack.push(combSB);
    }

    public void restoreCells() {
        StringBuilder combSB = stack.pop();

        for (int i = 0; i < combSB.length(); ++i) {
            if (combSB.charAt(i) == '1') {
                list.get(i).setMine();
            }

            if (combSB.charAt(i) == '0') {
                list.get(i).setValued();
            }

            if (combSB.charAt(i) == '2') {
                list.get(i).setUnknown();
            }
        }

    }

    public void setCellsComb(int index) {
        StringBuilder combSB = combinations[index];

        for (int i = 0; i < combSB.length(); ++i) {
            if (combinations[index].charAt(i) == '1') {
                list.get(i).setMine();
            } else {
                list.get(i).setValued();
            }
        }

    }

    public void subtraction(Group group) {
        Iterator i$ = group.list.iterator();

        while (i$.hasNext()) {
            Cell cell = (Cell) i$.next();
            list.remove(cell);
        }

        value -= group.value;
        if (value < 0) {
        }

    }

    public boolean contains(Group group) {
        Iterator i$ = group.list.iterator();

        Cell cell;
        do {
            if (!i$.hasNext()) {
                return true;
            }

            cell = (Cell) i$.next();
        } while (list.contains(cell));

        return false;
    }

    public Group getOverlap(Group group) {
        List<Cell> overlap = new ArrayList();
        Iterator i$ = group.list.iterator();

        while (i$.hasNext()) {
            Cell cell = (Cell) i$.next();
            if (list.contains(cell)) {
                overlap.add(cell);
            }
        }

        int mine = value - (list.size() - overlap.size());
        if (mine != group.value) {
            return null;
        } else {
            return new Group(overlap, mine);
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && getClass() == o.getClass()) {
            Group group = (Group) o;
            if (value != group.value) {
                return false;
            } else if (list.size() != group.list.size()) {
                return false;
            } else {
                return group.list.containsAll(list);
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = list.hashCode();
        result = 31 * result + value;
        return result;
    }
}
