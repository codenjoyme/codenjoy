package com.globallogic.snake.model.artifacts;

import com.globallogic.snake.model.Snake;

/**
 * User: oleksandr.baglai
 * Date: 10/2/12
 * Time: 2:48 AM
 */
public class EateablePoint extends Point implements Element {
    private Runnable doItOnEat;

    public EateablePoint(int x, int y) {
        super(x, y);
    }

    @Override
    public void affect(Snake snake) {
        if (doItOnEat != null) {
            doItOnEat.run();
        }
    }

    public void onEat(Runnable runnable) {
        this.doItOnEat = runnable;
    }
}
