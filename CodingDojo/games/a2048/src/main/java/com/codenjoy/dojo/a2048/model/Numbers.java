package com.codenjoy.dojo.a2048.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

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

    public Numbers(int[][] numbers) {
        this.size = numbers.length;
        this.data = numbers;

        clearFlags();
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

    public void add(int x, int y, int number) {
        data[x][y] = number;
    }

    public void add(Number number) {
        data[number.getX()][number.getY()] = number.get();
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
            for (int y = 0; y < size; y++) {
                if (data.get(x, y) == NONE) {
                    return true;
                }
            }

            for (int y1 = 0; y1 < size - 1; y1++) {
                int y2 = y1 + 1;

                int n1 = data.get(x, y1);
                int n2 = data.get(x, y2);

                if (n1 != BREAK && n1 == n2) {
                    return true;
                }
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

    public int freeSpace() {
        int result = 0;
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                int val = data[x][y];
                if (val == NONE) {
                    result++;
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
