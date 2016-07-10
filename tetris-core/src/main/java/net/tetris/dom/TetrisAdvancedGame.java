package net.tetris.dom;

import com.codenjoy.dojo.tetris.model.Figure;
import com.codenjoy.dojo.tetris.model.FigureQueue;

import java.util.List;

/**
 * @author serhiy.zelenin
 */
public class TetrisAdvancedGame extends TetrisGame implements TetrisJoystik, Cloneable {

    public TetrisAdvancedGame(FigureQueue queue, Glass glass) {
        super(glass, queue);
        takeFigure();
    }

    @Override
    public void left(int delta) {
        moveHorizontallyIfAccepted(x - delta < currentFigure.getLeft() ? currentFigure.getLeft() : x - delta);
    }

    @Override
    public void right(int delta) {
        moveHorizontallyIfAccepted(x + delta > 9 - currentFigure.getRight() ? 9 - currentFigure.getRight() : x + delta);
    }


}
