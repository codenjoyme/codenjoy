package net.tetris.dom;

import net.tetris.services.Plot;

import java.util.List;

public interface Console {
    void startGame();

    void figureAt(Figure figure, int x, int y);

    void drawGlass(Glass glass);

    void showChangesToPlayer();

    List<Plot> getPlots();
}
