//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.codenjoy.dojo.minesweeper.client.ai.logic;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Cell {
    private BigInteger bigInteger = new BigInteger("0");
    private int x;
    private int y;
    private int value;
    private boolean valued = false;
    private boolean mine = false;
    private boolean unknown = true;
    private double possibility = -1.0D;
    private List<Cell> neighbours = new ArrayList();

    public BigInteger getBigInteger() {
        return this.bigInteger;
    }

    public void setBigInteger(BigInteger bigInteger) {
        this.bigInteger = bigInteger;
    }

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void addNeighbour(Cell cell) {
        this.neighbours.add(cell);
    }

    public double getPossibility() {
        return this.possibility;
    }

    public void setPossibility(double possibility) {
        this.possibility = possibility;
    }

    public boolean isValued() {
        return this.valued;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
        this.valued = true;
        this.unknown = false;
        this.mine = false;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public boolean isMine() {
        return this.mine;
    }

    public void setMine() {
        this.mine = true;
        this.unknown = false;
        this.valued = false;
    }

    public boolean isUnknown() {
        return this.unknown;
    }

    public void setValued() {
        this.unknown = false;
        this.mine = false;
        this.valued = true;
    }

    public void setUnknown() {
        this.unknown = true;
        this.mine = false;
        this.valued = false;
    }

    public ArrayList<Cell> getUnknownCells() {
        ArrayList<Cell> cells = new ArrayList();
        Iterator i$ = this.neighbours.iterator();

        while(i$.hasNext()) {
            Cell neighbour = (Cell)i$.next();
            if (neighbour.isUnknown()) {
                cells.add(neighbour);
            }
        }

        return cells;
    }

    public int countMinesAround() {
        int result = 0;
        Iterator i$ = this.neighbours.iterator();

        while(i$.hasNext()) {
            Cell neighbour = (Cell)i$.next();
            if (neighbour.isMine()) {
                ++result;
            }
        }

        return result;
    }

    public String toString() {
        String string = "(" + this.x + "," + this.y + ")=" + (this.mine ? "mine" : (this.unknown ? "unknown, " + this.getPossibility() + "%" : this.value));
        return string;
    }

    public String toStringShort() {
        String string;
        if (this.isMine()) {
            string = "*";
        } else if (this.isUnknown()) {
            string = "`";
        } else if (this.value == 0) {
            string = " ";
        } else {
            string = Integer.toString(this.value);
        }

        return string;
    }

    public Pair<Integer, Integer> getCoords() {
        return new Pair(this.x, this.y);
    }

    public boolean hasUnknownAround() {
        Iterator i$ = this.neighbours.iterator();

        Cell neighbour;
        do {
            if (!i$.hasNext()) {
                return false;
            }

            neighbour = (Cell)i$.next();
        } while(!neighbour.isUnknown());

        return true;
    }
}
