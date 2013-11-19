package com.codenjoy.dojo.snake.services;

import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.snake.model.*;
import com.codenjoy.dojo.snake.model.artifacts.BasicWalls;
import com.codenjoy.dojo.snake.model.artifacts.RandomArtifactGenerator;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

/**
 * User: oleksandr.baglai
 * Date: 3/9/13
 * Time: 5:41 PM
 */
public class SnakeGame implements GameType {

    public static final int BOARD_SIZE = 15;

    @Override
    public PlayerScores getPlayerScores(int score) {
        return new SnakePlayerScores(score);
    }

    @Override
    public Game newGame(final EventListener listener) {
        return new BoardImpl(new RandomArtifactGenerator(), new SnakeFactory() {
            @Override
            public Snake create(int x, int y) {
                return new SnakeEvented(listener, x, y);
            }
        }, new BasicWalls(BOARD_SIZE), BOARD_SIZE);
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return v(BOARD_SIZE);
    }

    @Override
    public String gameName() {
        return "snake";
    }

    @Override
    public Object[] getPlots() {
        return PlotColor.values();
    }

    @Override
    public Settings getSettings() {
        return new Settings() {
            @Override
            public List<Parameter<?>> getParameters() {
                return new LinkedList<Parameter<?>>();
            }

            @Override
            public Parameter<?> addEditBox(String name) {
                return null;
            }

            @Override
            public Parameter<?> addSelect(String name, List<Object> strings) {
                return null;
            }

            @Override
            public Parameter<Boolean> addCheckBox(String name) {
                return null;
            }

            @Override
            public Parameter<?> getParameter(String name) {
                return null;
            }
        };
    }

    @Override
    public boolean isSingleBoardGame() {
        return false;
    }
}
