package com.codenjoy.dojo.icancode.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.client.ClientBoard;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.layeredview.PrinterData;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.icancode.model.Elements;
import com.codenjoy.dojo.icancode.model.ICanCode;
import com.codenjoy.dojo.icancode.model.Player;
import com.codenjoy.dojo.icancode.model.interfaces.ILevel;
import org.json.JSONObject;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

public class GameRunner extends AbstractGameType implements GameType  {

    private Parameter<Integer> isTrainingMode;

    public GameRunner() {
        setupSettings();
    }

    @SuppressWarnings("unchecked")
    private void setupSettings() {
        new Scores(0, settings);
        isTrainingMode = settings
                .addEditBox("Is training mode")
                .type(Integer.class).def(1);
    }
    
    @Override
    public PlayerScores getPlayerScores(Object score) {
        return new Scores((Integer)score, settings);
    }

    @Override
    public GameField createGame(int levelNumber) {
        boolean isSingle = levelNumber < getMultiplayerType().getLevelsCount();
        if (isSingle) {
            ILevel levels = loadLevel(levelNumber);
            return new ICanCode(levels,
                    getDice(),
                    ICanCode.SINGLE);
        } else {
            return new ICanCode(Levels.getMultiple(),
                    getDice(),
                    ICanCode.MULTIPLE);
        }
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return v(Levels.size());
    }

    @Override
    public String name() {
        return "icancode";
    }

    @Override
    public MultiplayerType getMultiplayerType() {
        return MultiplayerType.TRAINING.apply(Levels.getSingleMaps().size());
    }

    public ILevel loadLevel(int level) {
        return Levels.loadLevel(level);
    }

    @Override
    public Enum[] getPlots() {
        return Elements.values();
    }

    @Override
    public Class<? extends Solver> getAI() {
        return null;
    }

    @Override
    public Class<? extends ClientBoard> getBoard() {
        return null;
    }

    @Override
    public GamePlayer createPlayer(EventListener listener, String playerName) {
        if (isTrainingMode.getValue() == 0) { // TODO найти как это загрузить
//            int total = Levels.collectSingle().size();
//            save = "{'total':" + total + ",'current':0,'lastPassed':" + (total - 1) + ",'multiple':true}";
        }
        return new Player(listener);
    }

    @Override
    public PrinterFactory getPrinterFactory() {
        return PrinterFactory.get((BoardReader reader, Player player) -> {
            PrinterData data = player.getPrinter().print();

            JSONObject result = new JSONObject();
            result.put("layers", data.getLayers());
            // do not change 'offset' key - canvases working
            result.put("offset", toJson(data.getOffset()));
            result.put("heroPosition", toJson(player.getHeroOffset(data.getOffset())));
            result.put("levelFinished", player.isLevelFinished());
            result.put("showName", true);
            return result;
        });
    }

    private JSONObject toJson(Point point) {
        JSONObject result = new JSONObject();
        result.put("x", point.getX());
        result.put("y", point.getY());
        return result;
    }

}
