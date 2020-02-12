package com.codenjoy.dojo.client;

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


import com.codenjoy.dojo.services.printer.CharElements;
import com.codenjoy.dojo.services.Point;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

import static com.codenjoy.dojo.services.PointImpl.pt;

public abstract class AbstractLayeredBoard<E extends CharElements> implements ClientBoard {
    protected int size;
    protected char[][][] field;
    protected JSONObject source;
    protected List<String> layersString = new LinkedList<>();

    public ClientBoard forString(String boardString) {
        if (boardString.contains("layer")) {
            source = new JSONObject(boardString);
            JSONArray layers = source.getJSONArray("layers");
            return forString(layers.toList().toArray(new String[0]));
        } else {
            return forString(new String[]{boardString});
        }
    }

    public ClientBoard forString(String... layers) {
        layersString.clear();
        layersString.addAll(Arrays.asList(layers));

        String board = layers[0].replaceAll("\n", "");
        size = (int) Math.sqrt(board.length());
        field = new char[layers.length][size][size];

        for (int i = 0; i < layers.length; ++i) {
            board = layers[i].replaceAll("\n", "");

            char[] temp = board.toCharArray();
            for (int y = 0; y < size; y++) {
                int dy = y * size;
                for (int x = 0; x < size; x++) {
                    field[i][inversionX(x)][inversionY(y)] = temp[dy + x];
                }
            }
        }

        return this;
    }

    protected int inversionX(int x) {
        return x;
    }

    protected int inversionY(int y) {
        return y;
    }

    public abstract E valueOf(char ch);

    public int size() {
        return size;
    }

    public static Set<Point> removeDuplicates(Collection<Point> all) {
        return new TreeSet<>(all);
    }

    /**
     * @param numLayer Layer number (from 0).
     * @param elements List of elements that we try to find.
     * @return All positions of element specified.
     */
    protected List<Point> get(int numLayer, E... elements) {
        List<Point> result = new LinkedList<>();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                for (E element : elements) {
                    E value = valueOf(field(numLayer, x, y));
                    if ((value == null && element == null)
                            || (value != null && value.equals(element)))
                    {
                        result.add(pt(x, y));
                    }
                }
            }
        }
        return result;
    }

    /**
     * Says if at given position (X, Y) at given layer has given element.
     *
     * @param numLayer Layer number (from 0).
     * @param x        X coordinate.
     * @param y        Y coordinate.
     * @param element  Elements that we try to detect on this point.
     * @return true is element was found.
     */
    protected boolean isAt(int numLayer, int x, int y, E element) {
        if (pt(x, y).isOutOf(size)) {
            return false;
        }
        return getAt(numLayer, x, y).equals(element);
    }

    /**
     * @param numLayer Layer number (from 0).
     * @param x        X coordinate.
     * @param y        Y coordinate.
     * @return Returns element at position specified.
     */
    protected E getAt(int numLayer, int x, int y) {
        return valueOf(field(numLayer, x, y));
    }
    
    protected char field(int numLayer, int x, int y) {
        return field[numLayer][x][y];
    }
    
    protected String boardAsString(int numLayer) {
        StringBuilder result = new StringBuilder();
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                result.append(field(numLayer, inversionX(x), inversionY(y)));
            }
            result.append("\n");
        }
        return result.toString();
    }

    protected int countLayers() {
        return field.length;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Board:");
        for (int i = 0; i < countLayers(); i++) {
            result.append("\n").append(boardAsString(i));
        }
        return result.toString();
    }

    /**
     * Says if at given position (X, Y) at given layer has given elements.
     *
     * @param numLayer Layer number (from 0).
     * @param x        X coordinate.
     * @param y        Y coordinate.
     * @param elements List of elements that we try to detect on this point.
     * @return true is any of this elements was found.
     */
    protected boolean isAt(int numLayer, int x, int y, E... elements) {
        for (E element : elements) {
            if (isAt(numLayer, x, y, element)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Says if near (at left, right, down, up,
     * left-down, left-up, right-down, right-up)
     * given position (X, Y) at given layer exists given element.
     *
     * @param numLayer Layer number (from 0).
     * @param x        X coordinate.
     * @param y        Y coordinate.
     * @param element  Element that we try to detect on near point.
     * @return true is element was found.
     */
    protected boolean isNear(int numLayer, int x, int y, E element) {
        if (pt(x, y).isOutOf(size)) {
            return false;
        }
		return countNear(numLayer, x, y, element) > 0;
    }


    /**
     * @param numLayer Layer number (from 0).
     * @param x        X coordinate.
     * @param y        Y coordinate.
     * @param element  Element that we try to detect on near point.
     * @return Returns count of elements with type specified near
     * (at left, right, down, up,
     * left-down, left-up, right-down, right-up) {x,y} point.
     */
    protected int countNear(int numLayer, int x, int y, E element) {
        if (pt(x, y).isOutOf(size)) {
            return 0;
        }
        return (int) getNear(numLayer, x, y).stream()
                .filter( it -> it.equals(element))
                .count();
    }

    /**
     * @param numLayer Layer number (from 0).
     * @param x        X coordinate.
     * @param y        Y coordinate.
     * @return All elements around
     * (at left, right, down, up,
     * left-down, left-up, right-down, right-up) position.
     */
    protected List<E> getNear(int numLayer, int x, int y) {
        List<E> result = new LinkedList<E>();

        int radius = 1;
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                if (pt(x + dx, y + dy).isOutOf(size)) {
                    continue;
                }
                if (dx == 0 && dy == 0) {
                    continue;
                }
                if (withoutCorners() && (dx != 0 && dy != 0)) {
                    continue;
                }
                result.add(getAt(numLayer, x + dx, y + dy));
            }
        }

        return result;
    }

    protected boolean withoutCorners() {
        return false;
    }

    public boolean isOutOfField(int x, int y) {
        return pt(x, y).isOutOf(size);
    }

    protected void set(int numLayer, int x, int y, char ch) {
        field[numLayer][x][y] = ch;
    }

    protected char[][] getField(int numLayer) {
        return field[numLayer];
    }

    public List<String> getLayersString() {
        return layersString;
    }

    public void setSource(JSONObject source) {
        this.source = source;
    }
}
