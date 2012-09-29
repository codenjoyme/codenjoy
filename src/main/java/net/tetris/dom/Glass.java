package net.tetris.dom;

import net.tetris.services.Plot;

import java.util.List;

public interface Glass {
    boolean accept(Figure figure, int x, int y);

    void drop(Figure figure, int x, int y);

    void empty();

    void figureAt(Figure figure, int x, int y);

    List<Plot> getDroppedPlots();

    List<Plot> getCurrentFigurePlots();
}
