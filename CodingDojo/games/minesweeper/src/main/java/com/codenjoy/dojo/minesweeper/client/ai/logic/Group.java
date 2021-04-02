package com.codenjoy.dojo.minesweeper.client.ai.logic;

import java.util.*;

public class Group {

    private List<Cell> list;
    private int value;
    private Integer[] comb;
    private StringBuilder[] combinations;
    private Deque<StringBuilder> stack;

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
        return group.list.stream()
                .anyMatch(list::contains);
    }

    public int getCombSize() {
        return comb.length;
    }

    public void setComb() {
        comb = Sequence.get(value, size());
        List<StringBuilder> combStrings = new ArrayList(comb.length);
        for (int i = 0; i < comb.length; ++i) {
            Integer integer = comb[i];
            StringBuilder comb = new StringBuilder(this.comb.length);
            String binary = Integer.toBinaryString(integer);
            int lastChar = list.size() - 1;
            int binaryLength = binary.length();

            for (int k = 0; k < list.size(); ++k) {
                if (lastChar - k >= binaryLength) {
                    comb.append('0');
                } else {
                    comb.append(binary.charAt(binaryLength - lastChar - 1 + k));
                }
            }

            combStrings.add(comb);
        }

        combinations = new StringBuilder[combStrings.size()];
        combStrings.toArray(combinations);
    }

    public boolean checkCombination(int index) {
        StringBuilder comb = combinations[index];
        for (int i = 0; i < comb.length(); ++i) {
            char ch = comb.charAt(i);
            if (ch == '1' && list.get(i).isValued()
                    || ch == '0' && list.get(i).isMine())
            {
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

        for (Cell cell : list) {
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
        StringBuilder comb = new StringBuilder(list.size());
        for (Cell cell : list) {
            if (cell.isMine()) {
                comb.append('1');
            } else if (cell.isValued()) {
                comb.append('0');
            } else if (cell.isUnknown()) {
                comb.append('2');
            }
        }
        stack.push(comb);
    }

    public void restoreCells() {
        StringBuilder comb = stack.pop();
        for (int i = 0; i < comb.length(); ++i) {
            if (comb.charAt(i) == '1') {
                list.get(i).setMine();
            }

            if (comb.charAt(i) == '0') {
                list.get(i).setValued();
            }

            if (comb.charAt(i) == '2') {
                list.get(i).setUnknown();
            }
        }
    }

    public void setCellsComb(int index) {
        StringBuilder comb = combinations[index];
        for (int i = 0; i < comb.length(); ++i) {
            if (comb.charAt(i) == '1') {
                list.get(i).setMine();
            } else {
                list.get(i).setValued();
            }
        }
    }

    public void subtraction(Group group) {
        list.removeAll(group.list);
        value -= group.value;
    }

    public boolean contains(Group group) {
        return list.containsAll(group.list);
    }

    public Group getOverlap(Group group) {
        List<Cell> overlap = new ArrayList();

        for (Cell cell : group.list) {
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
        }

        if (o != null && getClass() == o.getClass()) {
            Group group = (Group) o;
            if (value != group.value) {
                return false;
            }

            if (list.size() != group.list.size()) {
                return false;
            }

            return group.list.containsAll(list);
        }

        return false;
    }

    public int hashCode() {
        return 31 * list.hashCode() + value;
    }
}
