package com.codenjoy.dojo.startandjump.services;

import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.startandjump.model.Level;
import com.codenjoy.dojo.startandjump.model.LevelImpl;

public class GameSettings extends SettingsImpl {

    private Parameter<Integer> winScore;
    private Parameter<Integer> losePenalty;
    private Parameter<String> levelMap;

    public GameSettings() {
        winScore = addEditBox("Win score").type(Integer.class).def(30);
        losePenalty = addEditBox("Lose penalty").type(Integer.class).def(100);
        levelMap = addEditBox("Level map").multiline().type(String.class).def(
                "####################" +
                " =                  " +
                " =                  " +
                " =                  " +
                " =                  " +
                " =                  " +
                " =                  " +
                " =                  " +
                " =                  " +
                " =                  " +
                " =                  " +
                " =                  " +
                " =                  " +
                " =                  " +
                " =                  " +
                "                    " +
                "☺            ==  ===" +
                " =        =         " +
                " =  ==== = ==       " +
                "####################");
    }

    // getters

    public int winScore() {
        return winScore.getValue();
    }

    public int losePenalty() {
        return losePenalty.getValue();
    }

    public String levelMap() {
        return levelMap.getValue();
    }

    public Level level() {
        return new LevelImpl(levelMap());
    }

    // setters

    public GameSettings winScore(int input) {
        winScore.update(input);
        return this;
    }

    public GameSettings losePenalty(int input) {
        losePenalty.update(input);
        return this;
    }

    public GameSettings levelMap(String input) {
        levelMap.update(input);
        return this;
    }

}
