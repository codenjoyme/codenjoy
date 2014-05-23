package com.codenjoy.dojo.a2048.model;

import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;

/**
 * Created by Sanja on 21.05.14.
 */
public class Numbers {

    public static final int NONE = -1;
    private final int size;
    private int[][] data;
    private boolean[][] done;

    public Numbers(List<Number> numbers, int size) {
        this(size);

        for (Number number : numbers) {
            add(number);
        }
    }

    public Numbers(int size) {
        this.size = size;
        data = new int[size][size];
        clearFlags();
        clear();
    }

    public void clear() {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                data[x][y] = NONE;
            }
        }
    }

    public boolean isEmpty() {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (data[x][y] != NONE) return false;
            }
        }
        return true;
    }

    public void add(Number number) {
        data[number.getX()][number.getY()] = number.get();
    }

    public boolean contains(Point pt) {
        if (pt.getX() == -1 || pt.getY() == -1) return false;

        return data[pt.getX()][pt.getY()] != NONE;
    }

    public Number get(Point pt) {
        return new Number(data[pt.getX()][pt.getY()], pt);
    }

    public boolean contains(Elements element) {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (data[x][y] == element.number()) return true;
            }
        }
        return false;
    }

    public void addRandom(Dice dice, int count) {
        for (int i = 0; i < count; i++) {
            Point pt = getFreeRandom(dice);
            if (!pt.itsMe(NO_SPACE)) {
                add(new Number(2, pt));
            }
        }
    }

    public static final Point NO_SPACE = pt(-1, -1);

    public Point getFreeRandom(Dice dice) {
        int rndX = 0;
        int rndY = 0;
        int c = 0;
        do {
            rndX = dice.next(size);
            rndY = dice.next(size);
        } while (rndX != -1 && rndY != -1 && data[rndX][rndY] != NONE && c++ < 100);

        if (c >= 100) {
            return NO_SPACE;
        }

        return pt(rndX, rndY);
    }

    public void remove(Point pt) {
        data[pt.getX()][pt.getY()] = NONE;
    }

    @Override
    public String toString() {
        StringBuffer result = new StringBuffer();

        for (int y = size - 1; y >= 0; y--) {
            for (int x = 0; x < size; x++) {
                int i = data[x][y];
                if (i != -1) {
                    result.append(String.valueOf(i));
                } else {
                    result.append('.');
                }
            }
            result.append('\n');
        }

        return result.toString();
    }

    private List<Integer> merge(Mirror data, int v1, int v2, int v3, int v4) {
        List<Integer> result = new LinkedList<Integer>();

        for (int y = 0; y < size; y++) {
            for (int x = v2 + v1; v1*(x - v2 - v3) <= v4; x += v1) {
                if (data.get(x, y) == NONE) continue;

                int x2 = x - v1;
                while (Math.abs(x2 - v2) != 0) {
                    if (data.get(x2, y) == NONE) {
                        data.set(x2, y, data.get(x2 + v1, y));
                        data.set(x2 + v1, y, NONE);
                    } else if (done[x2][y]) {
                        break;
                    } else if (data.get(x2, y) == data.get(x2 + v1, y)) {
                        int val = 2 * data.get(x2 + v1, y);
                        result.add(val);
                        data.set(x2, y, val);
                        data.set(x2 + v1, y, NONE);
                        setF(x2, y, true);
                        break;
                    } else {
                        setF(x2, y, true);
                        break;
                    }
                    x2 -= v1;
                }
            }
        }
        clearFlags();
        return result;
    }

    interface Mirror {
        int get(int x, int y);
        void set(int x, int y, int val);
    }

    class XY implements Mirror {
        @Override
        public int get(int x, int y) {
            return data[x][y];
        }

        @Override
        public void set(int x, int y, int val) {
            data[x][y] = val;
        }
    }

    class _XY implements Mirror {
        @Override
        public int get(int x, int y) {
            return data[size - 1 - x][y];
        }

        @Override
        public void set(int x, int y, int val) {
            data[size - 1 - x][y] = val;
        }
    }

    class Y_X implements Mirror {
        @Override
        public int get(int x, int y) {
            return data[y][size - 1 - x];
        }

        @Override
        public void set(int x, int y, int val) {
            data[y][size - 1 - x] = val;
        }
    }

    class YX implements Mirror {
        @Override
        public int get(int x, int y) {
            return data[y][x];
        }

        @Override
        public void set(int x, int y, int val) {
            data[y][x] = val;
        }
    }

    private void setF(int x, int y, boolean val) {
        done[x][y] = val;
    }

    private void clearFlags() {
        done = new boolean[size][size];
    }

    public List<Integer> move(Direction direction) {
        switch (direction) {
            case RIGHT : return merge(this.new _XY(),  1, -1, size, 0);
            case UP    : return merge(this.new Y_X(), 1, -1, size, 0);
            case LEFT  : return merge(this.new XY(), 1, -1, size, 0);
            case DOWN  : return merge(this.new YX(), 1, -1, size, 0);
        }
        return new LinkedList<Integer>();
    }

}
