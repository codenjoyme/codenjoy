package com.epam.dojo.icancode.services;

/*-
 * #%L
 * iCanCode - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 EPAM
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
import com.codenjoy.dojo.services.AbstractGameType;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.layeredview.LayeredViewPrinter;
import com.codenjoy.dojo.services.printer.layeredview.PrinterData;
import com.codenjoy.dojo.services.settings.Parameter;
import com.epam.dojo.icancode.model.Elements;
import com.epam.dojo.icancode.model.ICanCode;
import com.epam.dojo.icancode.model.Player;
import com.epam.dojo.icancode.model.interfaces.ILevel;
import org.json.JSONObject;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;
import static com.epam.dojo.icancode.services.Levels.getSingleMaps;

public class GameRunner extends AbstractGameType implements GameType  {

    private Parameter<Integer> isTrainingMode;

    public GameRunner() {
        setupSettings();
    }

    private void setupSettings() {
        new Scores(0, settings);
        isTrainingMode = settings.addEditBox("Is training mode").type(Integer.class).def(1);
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
        return MultiplayerType.TRAINING.apply(getSingleMaps().size());
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
            // TODO тут ой как некрасиво, при каждой прорисовке создается принтер
            // TODO да и само по себе это layered как-то сложно вышло
            LayeredViewPrinter printer = new LayeredViewPrinter(
                    reader.size(),
                    () -> player.getField().layeredReader(),
                    () -> player,
                    Levels.size(),
                    2);

            PrinterData data = printer.print();

            JSONObject result = new JSONObject();
            result.put("layers", data.getLayers());
            result.put("offset", data.getOffset());
//            JSONObject progress = player.printProgress(); // TODO это подсчитать на UI
//            result.put("showName", true);
//            result.put("onlyMyName", !progress.getBoolean("multiple"));
            return result;
        });
    }

}
