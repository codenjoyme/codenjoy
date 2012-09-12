package net.tetris.services;

import net.tetris.dom.Figure;
import net.tetris.dom.GlassEventListener;
import net.tetris.dom.Levels;

public class PlayerScores implements GlassEventListener {

    private volatile int score;
    private Levels levels;

    public PlayerScores(Levels levels) {
        this.levels = levels;
    }

    public PlayerScores() {
    }

    @Override
    public void glassOverflown() {
        int delta = score - 500;
        score = delta;
    }

    @Override
    public void linesRemoved(int amount) {
        int delta = 0;
        int currentLevel = levels.getCurrentLevel() + 1;
        switch (amount) {
            case 1:
                delta = 100 * currentLevel;
                break;
            case 2:
                delta = 300 * currentLevel;
                break;
            case 3:
                delta = 700 * currentLevel;
                break;
            case 4:
                delta = 1500 * currentLevel;
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
