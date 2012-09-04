package net.tetris.services;

import net.tetris.dom.Figure;
import net.tetris.dom.GlassEventListener;

public class PlayerScores implements GlassEventListener {

    private volatile int score;

    @Override
    public void glassOverflown() {
        int delta = score - 100;
        score = delta;
    }

    @Override
    public void linesRemoved(int amount) {
        int delta = 0;
        switch (amount) {
            case 1:
                delta = 100;
                break;
            case 2:
                delta = 300;
                break;
            case 3:
                delta = 700;
                break;
            case 4:
                delta = 1500;
                break;
        }
        delta += score;
        score = delta;
    }

    @Override
    public void figureDropped(Figure figure) {
        int delta = score + 10;
        score = delta;
    }

    public int getScore() {
        return score;
    }
}
