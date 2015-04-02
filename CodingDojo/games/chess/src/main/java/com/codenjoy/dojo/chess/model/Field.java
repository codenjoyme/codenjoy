package com.codenjoy.dojo.chess.model;

import com.codenjoy.dojo.chess.model.figures.Figure;

import java.util.List;

public interface Field {

    List<Figure> getFigures(boolean white);
}
