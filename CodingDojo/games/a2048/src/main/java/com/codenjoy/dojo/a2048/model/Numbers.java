package com.codenjoy.dojo.a2048.model;

import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

import java.lang.*;
import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;

public class Numbers {

    public static final int NONE = 0;
    public static final int BREAK = -1;
    private final int size;
    private int[][] data;
    private boolean[][] done;

    public Numbers(List<Number> numbers, int size, List<Number> aBreak) {
        this(size);

        for (Number number : numbers) {
            add(number);
        }
        for (Number number : aBreak) {
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
                if (data[x][y] != BREAK) {
                    data[x][y] = NONE;
                }
            }
        }
    }

    public boolean isEmpty() {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (isBusy(x, y) && data[x][y] != BREAK) return false;
            }
        }
        return true;
    }

    public void add(Number number) {
        data[number.getX()][number.getY()] = number.get();
    }

    public boolean contains(Point pt) {
        if (pt.getX() == -1 || pt.getY() == -1) return false;

        return isBusy(pt.getX(), pt.getY());
    }

    public Number get(int x, int y) {
        return new Number(data[x][y], x, y);
    }

    public boolean contains(Elements element) {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (data[x][y] == element.number()) return true;
            }
        }
        return false;
    }

    public boolean isBusy(int x, int y) {
        return data[x][y] != NONE;
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
                if (i == NONE) {
                    result.append('.');
                } else if (i == BREAK) {
                    result.append('x');
                } else {
                    result.append(String.valueOf(i));
                }
            }
            result.append('\n');
        }

        return result.toString();
    }

    private void merge(Mirror data) {
        for (int y = 0; y < size; y++) {
            for (int x = 0; x <= size - 1; x++) {
                if (data.get(x, y) == NONE || data.get(x, y) == BREAK) continue;

                for (int x2 = x - 1; x2 > -1; x2--) {
                    if (data.get(x2, y) == NONE) {
                        data.set(x2, y, data.get(x2 + 1, y));
                        data.set(x2 + 1, y, NONE);
                    } else if (done[x2][y]) {
                        break;
                    } else if (data.get(x2, y) == data.get(x2 + 1, y)) {
                        int val = 2 * data.get(x2 + 1, y);
                        data.set(x2, y, val);
                        data.set(x2 + 1, y, NONE);
                        setF(x2, y, true);
                        break;
                    } else {
                        setF(x2, y, true);
                        break;
                    }
                }
            }
        }
        clearFlags();
    }

    public boolean canGo(Mirror data) {
        for (int x = 0; x < size; x++) {
            int y1 = 0;

            while (y1 < size && data.get(x, y1) == NONE) {
                y1++;
            }

            int y2 = y1 + 1;
            while (y1 != y2 && y2 < size) {
                if (y1 == size - 1) break;

                while (y2 < size - 1 && data.get(x, y2) == NONE) {
                    y2++;
                }

                if (y2 == size) break;

                if (data.get(x, y1) == data.get(x, y2)) {
                    return true;
                }

                y1 = y2;
                y2 = y1 + 1;
            }
        }
        return false;
    }

    public boolean canGo() {
        return canGo(this.new YX()) || canGo(this.new XY());
    }

    public boolean isFull() {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (data[x][y] == NONE) return false;
            }
        }
        return true;
    }

    public int getSum() {
        int result = 0;
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                int val = data[x][y];
                if (val != NONE) {
                    result += val;
                }
            }
        }
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

    public void move(Direction direction) {
        switch (direction) {
            case RIGHT : merge(this.new _XY()); break;
            case UP    : merge(this.new Y_X()); break;
            case LEFT  : merge(this.new XY()); break;
            case DOWN  : merge(this.new YX()); break;
        }
    }

    public int size() {
        return size;
    }
}
