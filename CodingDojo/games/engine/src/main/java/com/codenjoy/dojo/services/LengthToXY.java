package com.codenjoy.dojo.services;

public class LengthToXY { // TODO есть точно такой же в com.codenjoy.dojo.client; только вертикально зеркальный
    private int size;

    public LengthToXY(int size) {
        this.size = size;
    }

    public Point getXY(int length) {
        if (length == -1) {
            return null;
        }
        return new PointImpl(length % size, size - 1 - length / size);
    }

    public int getLength(int x, int y) {
        return (size - 1 - y) * size + x;
    }

    public static class Map { // TODO test me
        private final LengthToXY xy;
        private char[] map;

        public Map(String map) {
            this.map = map.toCharArray();
            xy = new LengthToXY(getSize());
        }

        public Map(int size) {
            map = new char[size*size];
            xy = new LengthToXY(getSize());
        }

        public int getSize() {
            return (int) Math.sqrt(map.length);
        }

        public char getAt(int x, int y) {
            int length = xy.getLength(x, y);
            return map[length];
        }

        public char setAt(int x, int y, char ch) {
            int length = xy.getLength(x, y);
            char old = map[length];
            map[length] = ch;
            return old;
        }

        public boolean isOutOf(int x, int y) {
            return new PointImpl(x, y).isOutOf(getSize());
        }

        public String getMap() {
            return new String(map);
        }
    }
}