package net.tetris.services;

import net.tetris.dom.*;

public class PlayerScores implements GlassEventListener, ChangeLevelListener {

    public static final int GLASS_OVERFLOWN_PENALTY = -500;
    public static final int ONE_LINE_REMOVED_SCORE = 100;
    public static final int TWO_LINES_REMOVED_SCORE = 300;
    public static final int THREE_LINES_REMOVED_SCORE = 700;
    public static final int FOUR_LINES_REMOVED_SCORE = 1500;
    public static final int FIGURE_DROPPED_SCORE = 10;

    private volatile int score;
    private GameLevel level;

    public PlayerScores(int startScore) {
        this.score = startScore;
    }

    @Override
    public void glassOverflown() {
        score += GLASS_OVERFLOWN_PENALTY;
    }

    @Override
    public void linesRemoved(int total, int amount) {
        int delta = 0;
        int openCount = level.getFigureTypesToOpenCount();
        switch (amount) {
            case 1:
                delta = ONE_LINE_REMOVED_SCORE * openCount;
                break;
            case 2:
                delta = TWO_LINES_REMOVED_SCORE * openCount;
                break;
            case 3:
                delta = THREE_LINES_REMOVED_SCORE * openCount;
                break;
            case 4:
                delta = FOUR_LINES_REMOVED_SCORE * openCount;
                break;
        }
        delta += score;
        score = delta;
    }

    @Override
    public void figureDropped(Figure figure) {
        score += FIGURE_DROPPED_SCORE;
    }

    public int getScore() {
        return score;
    }

    @Override
    public void levelChanged(GameLevel level) {
        this.level = level;
    }
}
