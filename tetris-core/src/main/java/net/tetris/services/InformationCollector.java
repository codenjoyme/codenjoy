package net.tetris.services;

import net.tetris.dom.ChangeLevelListener;
import net.tetris.dom.Figure;
import net.tetris.dom.GameLevel;
import net.tetris.dom.GlassEventListener;

import java.util.*;

/**
 * User: oleksandr.baglai
 * Date: 11/13/12
 * Time: 12:40 AM
 */
public class InformationCollector implements GlassEventListener, ChangeLevelListener, Information {
    private Deque<String> pool = new LinkedList<String>();
    private PlayerScores playerScores;
    private static final String LEVEL = "Level";

    public InformationCollector(PlayerScores playerScores) {
        this.playerScores = playerScores;
    }

    @Override
    public void glassOverflown() {
        int before = playerScores.getScore();
        playerScores.glassOverflown();
        pool.add(showSign(playerScores.getScore() - before));
    }

    @Override
    public void linesRemoved(int amount) {
        int before = playerScores.getScore();
        playerScores.linesRemoved(amount);
        pool.add(showSign(playerScores.getScore() - before));
    }

    @Override
    public void figureDropped(Figure figure) {
        int before = playerScores.getScore();
        playerScores.figureDropped(figure);
        pool.add(showSign(playerScores.getScore() - before));
    }

    private String showSign(int integer) {
        if (integer > 0) {
            return "+" + integer;
        } else {
            return "" + integer;
        }
    }

    @Override
    public String getMessage() {
        List<String> result = new LinkedList<>();
        String message;
        do {
            message = infoAboutLevelChangedMustBeLast(pool.pollFirst());
            if (message != null) {
                result.add(message);
            }
        } while (message != null);

        if (result.isEmpty()) {
            return null;
        }
        return result.toString().replace("[", "").replace("]", "");
    }

    private String infoAboutLevelChangedMustBeLast(String message) {
        if (message != null && message.contains(LEVEL)) {
            pool.add(message);
            return pool.pollFirst();
        } else {
            return message;
        }
    }

    @Override
    public void levelChanged(int levelNumber, GameLevel level) {
        playerScores.levelChanged(levelNumber, level);
        pool.add(LEVEL + " " + (levelNumber + 1));
    }
}
