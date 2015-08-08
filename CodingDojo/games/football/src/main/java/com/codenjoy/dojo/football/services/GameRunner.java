package com.codenjoy.dojo.football.services;

import java.util.ArrayList;
import java.util.List;

import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.football.client.ai.DefaultSolver;
import com.codenjoy.dojo.football.model.*;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

/**
 * Генератор игор - реализация {@see GameType}
 * Обрати внимание на {@see GameRunner#SINGLE} - там реализовано переключение в режимы "все на одном поле"/"каждый на своем поле"
 */
public class GameRunner implements GameType {

	private static final String NUM_OF_PLAYERS = "Num of players";
	private static final String IS_NEED_AI_PARAMETR = "Is need AI";
	public final static boolean SINGLE = true;
    private final Settings settings;
    private final Level level;
    private Football game;
    private List<Football> games;

    private final Parameter<Integer> needAI;
    private final Parameter<Integer> numberOfPlayers;

    public GameRunner() {
    	games = new ArrayList<Football>();
        settings = new SettingsImpl();
        numberOfPlayers = settings.addEditBox(NUM_OF_PLAYERS).type(Integer.class).def(2);
        needAI = settings.addEditBox(IS_NEED_AI_PARAMETR).type(Integer.class).def(1);
        new Scores(0, settings);
        level = new LevelImpl(
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼┴┴┴┴┴┴┴☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼               ∙              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼                              ☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼┬┬┬┬┬┬┬☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼");
    }

    private Football newGame() {
        return new Football(level, new RandomDice());
    }

    @Override
    public PlayerScores getPlayerScores(int score) {
        return new Scores(score, settings);
    }

    @Override
    public Game newGame(EventListener listener, PrinterFactory factory) {
        if (!SINGLE || game == null) {
            game = newGame();
        } else if (game != null) {
        	if (game.getPlayersCount() >= numberOfPlayers.getValue() ) {
        		games.add(game);
        		game = newGame();
        	}
        }

        Game game = new Single(this.game, listener, factory);
        game.newGame();
        return game;
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return v(level.getSize());
    }

    @Override
    public String name() {
        return "football";
    }

    @SuppressWarnings("rawtypes")
	@Override
    public Enum[] getPlots() {
        return Elements.values();
    }

    @Override
    public Settings getSettings() {
        return settings;
    }

    @Override
    public boolean isSingleBoard() {
        return SINGLE;
    }

    @Override
    public void newAI(String aiName) {
    	if (needAI.getValue() == 1) {
    		DefaultSolver.start(aiName, WebSocketRunner.Host.REMOTE);
    	}
    }
}
