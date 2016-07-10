package com.codenjoy.dojo.tetris.model;

import com.codenjoy.dojo.tetris.model.Figure;

import java.util.List;

public interface FigureQueue {
    Figure next();

    List<Figure.Type> getFutureFigures();
}
