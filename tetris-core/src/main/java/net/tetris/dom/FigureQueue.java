package net.tetris.dom;

import java.util.List;

public interface FigureQueue {
    Figure next();

    List<Figure.Type> getFutureFigures();
}
