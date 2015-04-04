package com.codenjoy.dojo.chess.services;

import com.codenjoy.dojo.chess.model.Chess;
import com.codenjoy.dojo.chess.model.Elements;
import com.codenjoy.dojo.chess.model.LevelImpl;
import com.codenjoy.dojo.chess.model.SingleChess;
import com.codenjoy.dojo.chess.model.figures.Level;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

public class ChessGame implements GameType {

    public final static boolean SINGLE = true;
    private final Settings settings;
    private final Level level;
    private Chess game;

    public ChessGame() {
        settings = new SettingsImpl();
        new ChessPlayerScores(0, settings);
        level = new LevelImpl(
                "tksfaskt" +
                "pppppppp" +
                "        " +
                "        " +
                "        " +
                "        " +
                "PPPPPPPP" +
                "TKSFASKT");
    }

    private Chess newGame() {
        return new Chess(level, new RandomDice());
    }

    @Override
    public PlayerScores getPlayerScores(int score) {
        return new ChessPlayerScores(score, settings);
    }

    @Override
    public Game newGame(EventListener listener, PrinterFactory factory) {
        if (!SINGLE || game == null) {
            game = newGame();
        }

        Game game = new SingleChess(this.game, listener, factory);
        game.newGame();
        return game;
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return v(level.getSize());
    }

    @Override
    public String name() {
        return "sample";
    }

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
        // TODO implement me
//        try {
//            WebSocketRunner.run(
//                    WebSocketRunner.Host.LOCAL,
//                    aiName,
//                    new ApofigDirectionSolver(new RandomDice()),
//                    new Board());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
