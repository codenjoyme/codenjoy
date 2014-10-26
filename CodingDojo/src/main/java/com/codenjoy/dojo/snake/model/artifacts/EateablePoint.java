package com.codenjoy.dojo.snake.model.artifacts;

import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.snake.model.Hero;

/**
 * User: oleksandr.baglai
 * Date: 10/2/12
 * Time: 2:48 AM
 */
public class EateablePoint extends PointImpl implements Element {
    private Runnable doItOnEat;

    public EateablePoint(int x, int y) {
        super(x, y);
    }

    @Override
    public void affect(Hero snake) {
        if (doItOnEat != null) {
            doItOnEat.run();
        }
    }

    public void onEat(Runnable runnable) {
        this.doItOnEat = runnable;
    }
}
