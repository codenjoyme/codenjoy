package com.codenjoy.dojo.reversi.model;


import com.codenjoy.dojo.reversi.model.items.Chip;
import com.codenjoy.dojo.services.Point;

import java.util.List;

public interface GetChip {

    Chip chip(Point point);

    boolean currentColor();

    List<Point> freeSpaces();
}
