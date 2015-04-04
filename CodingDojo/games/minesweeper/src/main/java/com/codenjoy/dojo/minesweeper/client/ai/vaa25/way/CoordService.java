package com.codenjoy.dojo.minesweeper.client.ai.vaa25.way;

import java.util.Random;

/**
 * @author Alexander Vlasov
 */
public class CoordService {
    private int x, y;
    private int counter;
    private Coord nearest, nearest2;

    public CoordService(Coord coord) {
        x = coord.getX();
        y = coord.getY();
        reset();
        nearest = coord;
    }

    public static Coord getRandomCoord(int w, int h) {
        Random random = new Random();
        return new Coord(random.nextInt(w), random.nextInt(h));
    }

    /**
     * ������� �������� ���������� �� ��������, ��������� �� ����������� � ��������
     *
     * @param from �������� ��������� ����������
     * @param to   �������� �������� ����������
     * @return �������� ����������
     */
    public static Coord getNearest(Coord from, Coord to) {
        if (to.equals(from)) return null;
        int x1 = from.getX();
        int y1 = from.getY();
        int x2 = to.getX();
        int y2 = to.getY();
        int dx = x2 - x1;
        int dy = y2 - y1;
        double distance = Math.sqrt(dx * dx + dy * dy);
        dx = (int) Math.round(1.0 * dx / distance);
        dy = (int) Math.round(1.0 * dy / distance);
        Coord res = new Coord(x1 + dx, y1 + dy);
        if (res.equals(from)) System.out.println("nearest = from " + from);
        return res;
    }

    public static Coord getNearest2(Coord from, Coord to) {
        if (to.equals(from)) return null;
        Coord nearest = getNearest(from, to);
        int x1 = from.getX();
        int y1 = from.getY();
        int x2 = to.getX();
        int y2 = to.getY();
        double dx = x2 - x1;
        double dy = y2 - y1;
        double distance = Math.sqrt(dx * dx + dy * dy);

        dx = (1.0 * (dx / distance));
        dy = (1.0 * (dy / distance));
        double dxAbs = Math.abs(dx);
        double dyAbs = Math.abs(dy);
        if (dxAbs >= 0.5 && dyAbs >= 0.5) {
            if (dxAbs > dyAbs) dx = 0;
            else if (dxAbs < dyAbs) dy = 0;
        } else if (dxAbs >= 0.5 && dyAbs < 0.5) {
            dy = Math.signum(dy);
        } else if (dyAbs >= 0.5 && dxAbs < 0.5) {
            dx = Math.signum(dx);
        }

        int dxr = (int) Math.round(dx);
        int dyr = (int) Math.round(dy);

        return new Coord(x1 + dxr, y1 + dyr);
    }

    public void reset() {
        counter = 0;

    }

    public boolean hasNext() {
        return counter != 6;
    }

    /**
     * ���������� �������� ���������� �� �������� ���������
     *
     * @return
     */
    public Coord next() {
        if (hasNext()) {
            switch (counter++) {
                case 0:
                    return nearest;
                case 1:
                    return nearest2;
//                case 8:
//                    return getLeftUp();
//                case 9:
//                    return getRightUp();
//                case 6:
//                    return getLeftDown();
//                case 7:
//                    return getRightDown();
                case 4:
                    return getLeft();
                case 5:
                    return getUp();
                case 2:
                    return getRight();
                case 3:
                    return getDown();
            }
        }
        return null;
    }

    /**
     * ������� �������� ����������, ��������� �� ����������� � ��������
     *
     * @param to �������� ����������
     */
    public void setNearest(Coord to) {
        nearest2 = getNearest2(nearest, to);
        nearest = getNearest(nearest, to);

    }

    public Coord getLeftUp() {
        return new Coord(x - 1, y - 1);
    }

    public Coord getRightUp() {
        return new Coord(x + 1, y - 1);
    }

    public Coord getLeftDown() {
        return new Coord(x - 1, y + 1);
    }

    public Coord getRightDown() {
        return new Coord(x + 1, y + 1);
    }

    public Coord getLeft() {
        return new Coord(x - 1, y);
    }

    public Coord getUp() {
        return new Coord(x, y - 1);
    }

    public Coord getRight() {
        return new Coord(x + 1, y);
    }

    public Coord getDown() {
        return new Coord(x, y + 1);
    }

    public Coord getCenter() {
        return new Coord(x, y);
    }

    public int getCounter() {
        return counter;
    }

    @Override
    public String toString() {
        return "(" + x +
                ',' + y +
                ") counter=" + counter;
    }
}
