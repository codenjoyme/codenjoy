package com.codenjoy.dojo.hex.services;

import com.codenjoy.dojo.sample.model.*;
import com.codenjoy.dojo.sample.model.Sample;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

public class HexGame implements GameType {

    public final static boolean SINGLE = true;
    private final Settings settings;
    private final Level level;
    private Sample game;

    public HexGame() {
        settings = new SettingsImpl();
        new HexPlayerScores(0, settings);
        level = new LevelImpl(
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼          $                 ☼" +
                "☼                            ☼" +
                "☼   $              $         ☼" +
                "☼                       $    ☼" +
                "☼  $                         ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼              $             ☼" +
                "☼        $                   ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼ $                         $☼" +
                "☼                            ☼" +
                "☼              $             ☼" +
                "☼                            ☼" +
                "☼    $                       ☼" +
                "☼                            ☼" +
                "☼                       $    ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼            $               ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼       $                $   ☼" +
                "☼                            ☼" +
                "☼       ☺        $           ☼" +
                "☼                            ☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼");
    }

    private Sample newGame() {
        return new Sample(level, new RandomDice());
    }

    @Override
    public PlayerScores getPlayerScores(int score) {
        return new HexPlayerScores(score, settings);
    }

    @Override
    public Game newGame(EventListener listener) {
        if (!SINGLE || game == null) {
            game = newGame();
        }

        Game game = new SingleSample(this.game, listener);
        game.newGame();
        return game;
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return v(level.getSize());
    }

    @Override
    public String gameName() {
        return "sample";
    }

    @Override
    public Enum[] getPlots() {
        return Elements.values();
    }

    @Override
    public Settings getGameSettings() {
        return settings;
    }

    @Override
    public boolean isSingleBoardGame() {
        return SINGLE;
    }
}
