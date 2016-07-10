package net.tetris.dom;

import com.codenjoy.dojo.tetris.model.FigureQueue;
import com.codenjoy.dojo.tetris.model.Glass;

/**
 * @author serhiy.zelenin
 */
public class TetrisAdvancedGame extends TetrisGame implements TetrisJoystik, Cloneable {

    public TetrisAdvancedGame(FigureQueue queue, Glass glass) {
        super(queue, glass);
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
