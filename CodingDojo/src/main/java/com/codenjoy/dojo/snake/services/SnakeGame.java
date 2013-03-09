package com.codenjoy.dojo.snake.services;

import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.snake.model.BoardImpl;
import com.codenjoy.dojo.snake.model.Snake;
import com.codenjoy.dojo.snake.model.SnakeEvented;
import com.codenjoy.dojo.snake.model.SnakeFactory;
import com.codenjoy.dojo.snake.model.artifacts.BasicWalls;
import com.codenjoy.dojo.snake.model.artifacts.RandomArtifactGenerator;

/**
 * User: oleksandr.baglai
 * Date: 3/9/13
 * Time: 5:41 PM
 */
public class SnakeGame implements GameType {

    public static final int BOARD_SIZE = 15;

    @Override
    public PlayerScores getPlayerScores(int minScore) {
        return new SnakePlayerScores(minScore);
    }

    @Override
    public Game newGame(final EventListener listener) {
        BoardImpl board = new BoardImpl(new RandomArtifactGenerator(), new SnakeFactory() {
            @Override
            public Snake create(int x, int y) {
                return new SnakeEvented(listener, x, y);
            }
        }, new BasicWalls(BOARD_SIZE), BOARD_SIZE);

        return new SnakeBoardAdapter(board);
    }

    @Override
    public int getBoardSize() {
        return BOARD_SIZE;
    }

}
