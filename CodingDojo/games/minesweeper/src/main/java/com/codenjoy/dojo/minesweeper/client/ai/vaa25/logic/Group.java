package com.codenjoy.dojo.minesweeper.client.ai.vaa25.logic;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vlasov Alexander
 * Date: 01.08.2014
 * Time: 1:43
 * To change this template use File | Settings | File Templates.
 *
 * @author Alexander Vlasov
 */
public class Group {
    public static final char MINE = '1';
    public static final char VALUED = '0';
    public static final char UNKNOWN = '2';
    private List<Cell> list;
    private int value;
    private Integer[] comb;
    private StringBuilder[] combinations;
    private Deque<StringBuilder> stack;

    /**
     * Создает группу неизвестных ячеек с известным количеством мин в них
     *
     * @param cells неизвестные ячейки
     * @param value количество мин в них
     */
    public Group(List<Cell> cells, int value) {
        list = new ArrayList<Cell>(cells);
        this.value = value;
        stack = new LinkedList<StringBuilder>();
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
        for (Cell cell : group.list) {
            if (list.contains(cell)) return true;
        }
        return false;
    }

    public int getCombSize() {
        return comb.length;
    }

    public void setComb() {
        comb = Sequence6.getSequensed(value, size());
        List<StringBuilder> combStrings = new ArrayList<StringBuilder>(comb.length);
        for (Integer integer : comb) {
            StringBuilder combSB = new StringBuilder(comb.length);
            String binary = Integer.toBinaryString(integer);
            int lastChar = list.size() - 1;
            int binaryLength = binary.length();
            for (int k = 0; k < list.size(); k++) {
                if (lastChar - k >= binaryLength) combSB.append(VALUED);
                else combSB.append(binary.charAt(binaryLength - lastChar - 1 + k));

            }
            combStrings.add(combSB);
        }
        combinations = new StringBuilder[combStrings.size()];
        combStrings.toArray(combinations);
    }

    public boolean checkCombination(int index) {
        StringBuilder comb = combinations[index];
        for (int i = 0; i < comb.length(); i++) {
            char ch = comb.charAt(i);
            if ((ch == MINE && list.get(i).isValued()) || (ch == VALUED && list.get(i).isMine())) return false;
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
                valued++;
                if (valued > mayBeValued) return false;
            } else if (cell.isMine()) {
                mined++;
                if (mined > mayBeMined) return false;
            }
        }
        return true;
    }

    public void storeCells() {
        StringBuilder combSB = new StringBuilder(list.size());
        for (Cell cell : list) {
            if (cell.isMine()) combSB.append(MINE);
            else if (cell.isValued()) combSB.append(VALUED);
            else if (cell.isUnknown()) combSB.append(UNKNOWN);
        }
        stack.push(combSB);
    }

    public void restoreCells() {
        StringBuilder combSB = stack.pop();
        for (int i = 0; i < combSB.length(); i++) {
            if (combSB.charAt(i) == MINE) list.get(i).setMine();
            if (combSB.charAt(i) == VALUED) list.get(i).setValued();
            if (combSB.charAt(i) == UNKNOWN) list.get(i).setUnknown();
        }

    }

    public void setCellsComb(int index) {

        StringBuilder combSB = combinations[index];
        for (int i = 0; i < combSB.length(); i++) {
            if (combinations[index].charAt(i) == MINE) list.get(i).setMine();
            else list.get(i).setValued();
        }
    }

    public void subtraction(Group group) {
        for (Cell cell : group.list) list.remove(cell);
        value -= group.value;
        if (value < 0) {
//            System.out.println("//Here is bad data");
        }
    }

    public boolean contains(Group group) {
        for (Cell cell : group.list) if (!list.contains(cell)) return false;
        return true;
    }

    /**
     * Формирует группу, являющейся общей для текущей и заданной
     *
     * @param group заданная группа
     * @return
     */
    public Group getOverlap(Group group) {
        List<Cell> overlap = new ArrayList<Cell>();
        for (Cell cell : group.list)
            if (list.contains(cell))
                overlap.add(cell);
        int mine = value - (list.size() - overlap.size());
        if (mine != group.value) return null;

        return new Group(overlap, mine);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Group group = (Group) o;

        if (value != group.value) return false;
        if (list.size() != (group.list.size())) return false;
        if (!group.list.containsAll(list)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = list.hashCode();
        result = 31 * result + value;
        return result;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("mines=").append(value).append(' ');
        Iterator<Cell> iterator = list.iterator();
        while (iterator.hasNext()) {
            Cell cell = iterator.next();
            res.append(" (").append(cell.getX()).append(',').append(cell.getY()).append(")");
        }
        return res.toString();
    }

    public void setUnknown() {
        for (Cell cell : list) {
            cell.setUnknown();
        }
    }
}
