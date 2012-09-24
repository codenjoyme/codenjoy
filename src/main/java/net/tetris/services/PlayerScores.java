package net.tetris.services;

import net.tetris.dom.Figure;
import net.tetris.dom.GlassEventListener;
import net.tetris.dom.Levels;

public class PlayerScores implements GlassEventListener {

    public static final int GLASS_OVERFLOWN_PENALTY = -500;
    public static final int ONE_LINE_REMOVED_SCORE = 100;
    public static final int TWO_LINES_REMOVED_SCORE = 300;
    public static final int THREE_LINES_REMOVED_SCORE = 700;
    public static final int FOUR_LINES_REMOVED_SCORE = 1500;
    public static final int FIGURE_DROPPED_SCORE = 10;

    private volatile int score;
    private Levels levels;

    public PlayerScores(Levels levels, int startScore) {
        this.levels = levels;
        this.score = startScore;
    }

    @Override
    public void glassOverflown() {
        int delta = score + GLASS_OVERFLOWN_PENALTY;
        score = delta;
    }

    @Override
    public void linesRemoved(int total, int amount) {
        int delta = 0;
        int currentLevel = levels.getCurrentLevel() + 1;
        switch (amount) {
            case 1:
                delta = ONE_LINE_REMOVED_SCORE * currentLevel;
                break;
            case 2:
                delta = TWO_LINES_REMOVED_SCORE * currentLevel;
                break;
            case 3:
                delta = THREE_LINES_REMOVED_SCORE * currentLevel;
                break;
            case 4:
                delta = FOUR_LINES_REMOVED_SCORE * currentLevel;
                break;
        }
        delta += score;
        score = delta;
    }

    @Override
    public void figureDropped(Figure figure) {
        int delta = score + FIGURE_DROPPED_SCORE;
        score = delta;
    }

    public int getScore() {
        return score;
    }
}
