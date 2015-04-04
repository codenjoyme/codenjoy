package com.codenjoy.dojo.minesweeper.client.ai.vaa25.logic;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vlasov Alexander
 * Date: 04.09.13
 * Time: 18:08
 * To change this template use File | Settings | File Templates.
 */

/**
 * Ячейка на поле
 */
public class Cell {
    private BigInteger bigInteger = new BigInteger("0");
    private int x, y, value;
    /**
     * ячейка открыта и имеет цифру
     */
    private boolean valued;
    /**
     * ячейка отмечена бомбой
     */
    private boolean mine;
    /**
     * ячейка не открыта
     */
    private boolean unknown;
    /**
     * общая вероятность
     */
    private double possibility = -1;
    private List<Cell> neighbours;

    public BigInteger getBigInteger() {
        return bigInteger;
    }

    public void setBigInteger(BigInteger bigInteger) {
        this.bigInteger = bigInteger;
    }

    public Cell(int x, int y) {
        unknown = true;
        mine = false;
        valued = false;
        neighbours = new ArrayList<Cell>();
        this.x = x;
        this.y = y;
    }

    /**
     * Добавляет заданную ячейку к списку соседних
     *
     * @param cell заданная ячейка
     */
    public void addNeighbour(Cell cell) {
        neighbours.add(cell);
    }

    public double getPossibility() {
        return possibility;
    }

    public void setPossibility(double possibility) {
        this.possibility = possibility;
    }

    public boolean isValued() {
        return valued;
    }

    public int getValue() {
        return value;
    }

    /**
     * Открывает ячейку, устанавливая в нее заданное число окружающих мин
     *
     * @param value число окружающих ячейку мин
     */
    public void setValue(int value) {
        this.value = value;
        valued = true;
        unknown = false;
        mine = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isMine() {
        return mine;
    }

    public void setMine() {
        this.mine = true;
        unknown = false;
        valued = false;
    }

    public boolean isUnknown() {
        return unknown;
    }

    public void setValued() {
        unknown = false;
        mine = false;
        valued = true;
    }

    public void setUnknown() {
        this.unknown = true;
        mine = false;
        valued = false;
    }

    public ArrayList<Cell> getUnknownCells() {
        ArrayList<Cell> cells = new ArrayList<Cell>();
        for (Cell neighbour : neighbours) if (neighbour.isUnknown()) cells.add(neighbour);
        return cells;
    }


    /**
     * Подсчитывает количество известных мин, окружающих эту ячейку
     *
     * @return количество известных мин, окружающих эту ячейку
     */
    public int countMinesAround() {
        int result = 0;
        for (Cell neighbour : neighbours) if (neighbour.isMine()) result++;
        return result;
    }

    @Override
    public String toString() {
        String string = "(" + x + "," + y + ")=" + (mine ? "mine" : unknown ? ("unknown, " + getPossibility() + "%") : value);
        return string;
    }

    public String toStringShort() {
        String string;
        if (isMine()) string = "*";
        else if (isUnknown()) string = "`";
        else if (value == 0) string = " ";
        else string = Integer.toString(value);
        return string;
    }


    public Pair<Integer, Integer> getCoords() {
        return new Pair<Integer, Integer>(x, y);
    }


    public boolean hasUnknownAround() {
        for (Cell neighbour : neighbours) if (neighbour.isUnknown()) return true;
        return false;
    }
}
