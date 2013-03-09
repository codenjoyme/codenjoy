package com.codenjoy.dojo.snake.services;

import com.codenjoy.dojo.snake.model.ChangeLevelListener;
import com.codenjoy.dojo.snake.model.GameLevel;
import com.codenjoy.dojo.snake.model.middle.SnakeEventListener;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 11/13/12
 * Time: 12:40 AM
 */
public class InformationCollector implements SnakeEventListener, ChangeLevelListener, Information {
    private Deque<String> pool = new LinkedList<String>();
    private PlayerScores playerScores;
    private static final String LEVEL = "Level";

    public InformationCollector(PlayerScores playerScores) {
        this.playerScores = playerScores;
    }

    @Override
    public void snakeIsDead() {
        int before = playerScores.getScore();
        playerScores.snakeIsDead();
        add(before);
    }

    @Override
    public void snakeEatApple() {
        int before = playerScores.getScore();
        playerScores.snakeEatApple();
        add(before);
    }

    private void add(int before) {
        int delta = playerScores.getScore() - before;
        if (delta != 0) {
            pool.add(showSign(delta));
        }
    }

    @Override
    public void snakeEatStone() {
        int before = playerScores.getScore();
        playerScores.snakeEatStone();
        add(before);
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
        List<String> result = new LinkedList<String>();
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

    public void setInfo(String information) {
        pool.clear();
        pool.add(information);
    }
}
