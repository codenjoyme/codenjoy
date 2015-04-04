package com.codenjoy.dojo.minesweeper.client.ai.vaa25.way;

/**
 * ��������������� ����� ��� PathFinder. ����������� ������ ���������
 *
 * @author Alexander Vlasov
 */
public class WayCoord {
    private Coord coord;
    private WayCoord prev;

    public WayCoord(Coord coord, WayCoord prev) {
        this.coord = coord;
        this.prev = prev;
    }

    public Coord getCoord() {
        return coord;
    }

    public WayCoord getPrev() {
        return prev;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WayCoord wayCoord = (WayCoord) o;

        if (!coord.equals(wayCoord.coord)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return coord.hashCode();
    }

    @Override
    public String toString() {
        return coord.toString();
    }
}
