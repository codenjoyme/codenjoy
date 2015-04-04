package com.codenjoy.dojo.minesweeper.client.ai.vaa25.way;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Vlasov Alexander
 * Date: 24.08.2014
 * Time: 23:51
 *
 * @author Alexander Vlasov
 */
public class Coord implements Serializable {
    private final int x, y;

    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coord coord = (Coord) o;

        if (x != coord.x) return false;
        if (y != coord.y) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return "(" +
                x +
                "," + y +
                ")";
    }
}
