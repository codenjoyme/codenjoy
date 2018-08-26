package com.codenjoy.dojo.services.nullobj;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.printer.BoardReader;

import java.util.LinkedList;

public class NullBoardReader implements BoardReader {

    public static final BoardReader INSTANCE = new NullBoardReader();

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Iterable<? extends Point> elements() {
        return new LinkedList<>();
    }
}
