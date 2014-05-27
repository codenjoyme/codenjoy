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
    public List<Figure> getFigures() {
        List<Figure> result = new LinkedList<Figure>();
        for (int index = 0; index < map.length(); index++) {
            if (map.charAt(index) == Elements.FERZ.ch) result.add(new Ferz(xy.getXY(index)));
            if (map.charAt(index) == Elements.KON.ch) result.add(new Kon(xy.getXY(index)));
            if (map.charAt(index) == Elements.KOROL.ch) result.add(new Korol(xy.getXY(index)));
            if (map.charAt(index) == Elements.LADIA.ch) result.add(new Ladia(xy.getXY(index)));
            if (map.charAt(index) == Elements.PESHKA.ch) result.add(new Peshka(xy.getXY(index)));
            if (map.charAt(index) == Elements.SLON.ch) result.add(new Slon(xy.getXY(index)));
        }
        return result;
    }
}
