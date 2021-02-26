package com.codenjoy.dojo.services;

import com.codenjoy.dojo.client.ClientBoard;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.printer.CharElements;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

public class RoomGameType implements GameType {

    private final GameType type;
    private final Settings settings;

    public RoomGameType(GameType type) {
        this.type = type;
        this.settings = type.getSettings();
    }

    @Override
    public Settings getSettings() {
        return settings;
    }

    @Override
    public String toString() {
        return "RoomGameType{" +
                "type=" + type +
                ", settings=" + settings +
                '}';
    }

    @Override
    public PlayerScores getPlayerScores(Object score, Settings settings) {
        return type.getPlayerScores(score, this.settings);
    }

    @Override
    public GameField createGame(int levelNumber, Settings settings) {
        return type.createGame(levelNumber, this.settings);
    }

    @Override
    public Parameter<Integer> getBoardSize(Settings settings) {
        return type.getBoardSize(this.settings);
    }

    @Override
    public String name() {
        return type.name();
    }

    @Override
    public CharElements[] getPlots() {
        return type.getPlots();
    }

    @Override
    public Class<? extends Solver> getAI() {
        return type.getAI();
    }

    @Override
    public Class<? extends ClientBoard> getBoard() {
        return type.getBoard();
    }

    @Override
    public String getVersion() {
        return type.getVersion();
    }

    @Override
    public MultiplayerType getMultiplayerType(Settings settings) {
        return type.getMultiplayerType(this.settings);
    }

    @Override
    public GamePlayer createPlayer(EventListener listener, String playerId, Settings settings) {
        return type.createPlayer(listener, playerId, this.settings);
    }

    @Override
    public Dice getDice() {
        return type.getDice();
    }

    @Override
    public PrinterFactory getPrinterFactory() {
        return type.getPrinterFactory();
    }

    @Override
    public void tick() {
        type.tick();
    }
}