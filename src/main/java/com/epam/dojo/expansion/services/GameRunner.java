package com.epam.dojo.expansion.services;

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


import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.settings.Parameter;
import com.epam.dojo.expansion.model.*;
import com.epam.dojo.expansion.model.levels.Levels;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

/**
 * Генератор игор - реализация {@see GameType}
 * Обрати внимание на {@see GameRunner#SINGLE} - там реализовано переключение в режимы "все на одном поле"/"каждый на своем поле"
 */
public class GameRunner extends AbstractGameType implements GameType  {

    private static Logger logger = DLoggerFactory.getLogger(GameRunner.class);
    private Dice dice;

    private Parameter<Boolean> waitingOthers;
    private Parameter<Integer> boardSize;
    private List<Parameter<String>> levels = new LinkedList<>();
    private int size;

    private MultipleGameFactory gameFactory;
    private Ticker ticker;

    public GameRunner() {
        new Scores(0, settings);
        boardSize = settings.addEditBox("Board size").type(Integer.class).def(20);
        waitingOthers = settings.addEditBox("Waiting others").type(Boolean.class).def(false);

        levels.add(settings.addEditBox("Multiple level 1").type(String.class).def(Levels.MULTI_LEVEL1));
        levels.add(settings.addEditBox("Multiple level 2").type(String.class).def(Levels.MULTI_LEVEL2));
        levels.add(settings.addEditBox("Multiple level 3").type(String.class).def(Levels.MULTI_LEVEL3));

        ticker = new Ticker();
        dice = new RandomDice();
    }

    private void initGameFactory() {
        if (gameFactory == null || settings.changed()) {
            settings.changesReacted();

            size = boardSize.getValue();
            List<String> list = Arrays.asList(
                    levels.get(0).getValue(),
                    levels.get(1).getValue(),
                    levels.get(2).getValue()
            );
            gameFactory = new MultipleGameFactory(dice,
                    Levels.collectSingle(boardSize.getValue()),
                    Levels.collectMultiple(boardSize.getValue(),
                            list.toArray(new String[0]))
            );
            gameFactory.setWaitingOthers(waitingOthers.getValue());
        }
    }

    @Override
    public PlayerScores getPlayerScores(int score) {
        return new Scores(score, settings);
    }

    @Override
    public Game newGame(EventListener listener, PrinterFactory factory, String save) {
        boolean isTrainingMode = false; // TODO load from game_settings via GameDataController
        if (!isTrainingMode) {
            int total = Levels.collectSingle(boardSize.getValue()).get().size();
            save = "{'total':" + total + ",'current':0,'lastPassed':" + (total - 1) + ",'multiple':true}";
        }

        initGameFactory();

        if (logger.isDebugEnabled()) {
            logger.debug("Starting new game with save {}", save);
        }
        Game single = new Single(gameFactory, listener, factory, ticker, dice, save);
        single.newGame();
        return single;
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return v(size);
    }

    @Override
    public String name() {
        return "expansion";
    }

    @Override
    public Enum[] getPlots() {
        return Elements.values();
    }

    public void setDice(Dice dice) {
        this.dice = dice;
    }

}
