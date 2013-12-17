package com.codenjoy.dojo.loderunner.services;

import com.codenjoy.dojo.loderunner.model.Elements;
import com.codenjoy.dojo.loderunner.model.Level;
import com.codenjoy.dojo.loderunner.model.LevelImpl;
import com.codenjoy.dojo.loderunner.model.Loderunner;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

/**
 * User: oleksandr.baglai
 * Date: 8/17/13
 * Time: 7:47 PM
 */
public class LoderunnerGame implements GameType {

    public final static boolean SINGLE = true;
    private final Settings settings;
    private final Level level;

    public LoderunnerGame() {
        settings = new SettingsImpl();
        new LoderunnerPlayerScores(0, settings);  // TODO сеттринги разделены по разным классам, продумать архитектуру
        level = new LevelImpl(
                "☼☼☼☼☼☼☼☼☼☼" +
                "☼        ☼" +
                "☼        ☼" +
                "☼        ☼" +
                "☼        ☼" +
                "☼        ☼" +
                "☼        ☼" +
                "☼ ◄      ☼" +
                "☼########☼" +
                "☼☼☼☼☼☼☼☼☼☼");
    }

    @Override
    public PlayerScores getPlayerScores(int score) {
        return new LoderunnerPlayerScores(score, settings);
    }

    @Override
    public Game newGame(EventListener listener) {
        return new Loderunner(level);
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return v(level.getSize());
    }

    @Override
    public String gameName() {
        return "loderunner";
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
