package com.codenjoy.dojo.chess.model;

import com.codenjoy.dojo.chess.model.figures.*;
import com.codenjoy.dojo.services.LengthToXY;

import java.util.LinkedList;
import java.util.List;

public class LevelImpl implements Level {
    private final LengthToXY xy;

    private String map;

    public LevelImpl(String map) {
        this.map = map;
        xy = new LengthToXY(getSize());
    }

    @Override
    public int getSize() {
        return (int) Math.sqrt(map.length());
    }

    @Override
    public List<Figure> getFigures(boolean isWhite) {
        List<Figure> result = new LinkedList<Figure>();
        for (int index = 0; index < map.length(); index++) {
            if (isWhite) {
                if (map.charAt(index) == Elements.WHITE_FERZ.ch) result.add(new Ferz(xy.getXY(index), isWhite));
                if (map.charAt(index) == Elements.WHITE_KON.ch) result.add(new Kon(xy.getXY(index), isWhite));
                if (map.charAt(index) == Elements.WHITE_KOROL.ch) result.add(new Korol(xy.getXY(index), isWhite));
                if (map.charAt(index) == Elements.WHITE_LADIA.ch) result.add(new Ladia(xy.getXY(index), isWhite));
                if (map.charAt(index) == Elements.WHITE_PESHKA.ch) result.add(new Peshka(xy.getXY(index), isWhite));
                if (map.charAt(index) == Elements.WHITE_SLON.ch) result.add(new Slon(xy.getXY(index), isWhite));
            } else {
                if (map.charAt(index) == Elements.BLACK_FERZ.ch) result.add(new Ferz(xy.getXY(index), isWhite));
                if (map.charAt(index) == Elements.BLACK_KON.ch) result.add(new Kon(xy.getXY(index), isWhite));
                if (map.charAt(index) == Elements.BLACK_KOROL.ch) result.add(new Korol(xy.getXY(index), isWhite));
                if (map.charAt(index) == Elements.BLACK_LADIA.ch) result.add(new Ladia(xy.getXY(index), isWhite));
                if (map.charAt(index) == Elements.BLACK_PESHKA.ch) result.add(new Peshka(xy.getXY(index), isWhite));
                if (map.charAt(index) == Elements.BLACK_SLON.ch) result.add(new Slon(xy.getXY(index), isWhite));
            }
        }
        return result;
    }

    private char upper(char ch) {
        return ("" + ch).toUpperCase().charAt(0);
    }
}
