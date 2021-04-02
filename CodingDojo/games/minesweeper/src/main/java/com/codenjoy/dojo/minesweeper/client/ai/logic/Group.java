

package com.codenjoy.dojo.minesweeper.client.ai.logic;

import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Group {

    private List<Cell> list;
    private int value;
    private Integer[] comb;
    private StringBuilder[] combinations;
    private Deque<StringBuilder> stack;

    public Group(List<Cell> cells, int value) {
        this.list = new ArrayList(cells);
        this.value = value;
        this.stack = new LinkedList();
    }

    public List<Cell> getList() {
        return this.list;
    }

    public int size() {
        return this.list.size();
    }

    public int getValue() {
        return this.value;
    }

    public boolean isCross(Group group) {
        Iterator i$ = group.list.iterator();

        Cell cell;
        do {
            if (!i$.hasNext()) {
                return false;
            }

            cell = (Cell)i$.next();
        } while(!this.list.contains(cell));

        return true;
    }

    public int getCombSize() {
        return this.comb.length;
    }

    public void setComb() {
        this.comb = Sequence6.getSequensed(this.value, this.size());
        List<StringBuilder> combStrings = new ArrayList(this.comb.length);
        Integer[] arr$ = this.comb;
        int len$ = arr$.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Integer integer = arr$[i$];
            StringBuilder combSB = new StringBuilder(this.comb.length);
            String binary = Integer.toBinaryString(integer);
            int lastChar = this.list.size() - 1;
            int binaryLength = binary.length();

            for(int k = 0; k < this.list.size(); ++k) {
                if (lastChar - k >= binaryLength) {
                    combSB.append('0');
                } else {
                    combSB.append(binary.charAt(binaryLength - lastChar - 1 + k));
                }
            }

            combStrings.add(combSB);
        }

        this.combinations = new StringBuilder[combStrings.size()];
        combStrings.toArray(this.combinations);
    }

    public boolean checkCombination(int index) {
        StringBuilder comb = this.combinations[index];

        for(int i = 0; i < comb.length(); ++i) {
            char ch = comb.charAt(i);
            if (ch == '1' && ((Cell)this.list.get(i)).isValued() || ch == '0' && ((Cell)this.list.get(i)).isMine()) {
                return false;
            }
        }

        return true;
    }

    public boolean checkCells() {
        int mayBeValued = this.list.size() - this.value;
        int mayBeMined = this.value;
        int valued = 0;
        int mined = 0;
        Iterator i$ = this.list.iterator();

        while(i$.hasNext()) {
            Cell cell = (Cell)i$.next();
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
        StringBuilder combSB = new StringBuilder(this.list.size());
        Iterator i$ = this.list.iterator();

        while(i$.hasNext()) {
            Cell cell = (Cell)i$.next();
            if (cell.isMine()) {
                combSB.append('1');
            } else if (cell.isValued()) {
                combSB.append('0');
            } else if (cell.isUnknown()) {
                combSB.append('2');
            }
        }

        this.stack.push(combSB);
    }

    public void restoreCells() {
        StringBuilder combSB = this.stack.pop();

        for(int i = 0; i < combSB.length(); ++i) {
            if (combSB.charAt(i) == '1') {
                this.list.get(i).setMine();
            }

            if (combSB.charAt(i) == '0') {
                this.list.get(i).setValued();
            }

            if (combSB.charAt(i) == '2') {
                this.list.get(i).setUnknown();
            }
        }

    }

    public void setCellsComb(int index) {
        StringBuilder combSB = this.combinations[index];

        for(int i = 0; i < combSB.length(); ++i) {
            if (this.combinations[index].charAt(i) == '1') {
                this.list.get(i).setMine();
            } else {
                this.list.get(i).setValued();
            }
        }

    }

    public void subtraction(Group group) {
        Iterator i$ = group.list.iterator();

        while(i$.hasNext()) {
            Cell cell = (Cell)i$.next();
            this.list.remove(cell);
        }

        this.value -= group.value;
        if (this.value < 0) {
        }

    }

    public boolean contains(Group group) {
        Iterator i$ = group.list.iterator();

        Cell cell;
        do {
            if (!i$.hasNext()) {
                return true;
            }

            cell = (Cell)i$.next();
        } while(this.list.contains(cell));

        return false;
    }

    public Group getOverlap(Group group) {
        List<Cell> overlap = new ArrayList();
        Iterator i$ = group.list.iterator();

        while(i$.hasNext()) {
            Cell cell = (Cell)i$.next();
            if (this.list.contains(cell)) {
                overlap.add(cell);
            }
        }

        int mine = this.value - (this.list.size() - overlap.size());
        if (mine != group.value) {
            return null;
        } else {
            return new Group(overlap, mine);
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Group group = (Group)o;
            if (this.value != group.value) {
                return false;
            } else if (this.list.size() != group.list.size()) {
                return false;
            } else {
                return group.list.containsAll(this.list);
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.list.hashCode();
        result = 31 * result + this.value;
        return result;
    }

    public String toString() {
        StringBuilder res = (new StringBuilder("mines=")).append(this.value).append(' ');
        Iterator iterator = this.list.iterator();

        while(iterator.hasNext()) {
            Cell cell = (Cell)iterator.next();
            res.append(" (").append(cell.getX()).append(',').append(cell.getY()).append(")");
        }

        return res.toString();
    }

}
