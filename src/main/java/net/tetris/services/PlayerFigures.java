package net.tetris.services;

import net.tetris.dom.Figure;
import net.tetris.dom.FigureQueue;
import net.tetris.dom.TetrisFigure;

public class PlayerFigures implements FigureQueue {

    @Override
    public Figure next() {
        return new TetrisFigure(0, 1, Figure.Type.I, "#", "#", "#", "#");
    }
}
