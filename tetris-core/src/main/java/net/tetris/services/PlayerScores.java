package net.tetris.services;

import com.codenjoy.dojo.tetris.model.Figure;
import net.tetris.dom.*;

public class PlayerScores implements GlassEventListener, ChangeLevelListener {

    public static final int FIGURE_DROPPED_SCORE = 1;
    public static final int ONE_LINE_REMOVED_SCORE = 10;
    public static final int TWO_LINES_REMOVED_SCORE = 30;
    public static final int THREE_LINES_REMOVED_SCORE = 50;
    public static final int FOUR_LINES_REMOVED_SCORE = 100;
    public static final int GLASS_OVERFLOWN_PENALTY = - ONE_LINE_REMOVED_SCORE;

    private volatile int score;
    private GameLevel level;

    public PlayerScores(int startScore) {
        this.score = startScore;
    }

    @Override
    public void glassOverflown() {
        int openCount = level.getFigureTypesToOpenCount();
        score += GLASS_OVERFLOWN_PENALTY * openCount;
        score = Math.max(0, score);
    }

    @Override
    public void linesRemoved(int amount) {
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
    public void levelChanged(int levelNumber, GameLevel level) {
        this.level = level;
    }
}
