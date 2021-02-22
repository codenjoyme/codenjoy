package com.codenjoy.dojo.spacerace.services;

import com.codenjoy.dojo.services.settings.EditBox;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.spacerace.model.Level;
import com.codenjoy.dojo.spacerace.model.LevelImpl;

public class GameSettings extends SettingsImpl {

    private Parameter<Integer> ticksToRecharge;
    private Parameter<Integer> bulletsCount;
    private Parameter<Integer> destroyStoneScore;
    private Parameter<Integer> destroyEnemyScore;
    private Parameter<Integer> loosePenalty;
    private Parameter<Integer> destroyBombScore;
    private Parameter<String> levelMap;

    public GameSettings() {
        ticksToRecharge = addEditBox("Ticks to recharge").type(Integer.class).def(30);
        bulletsCount = addEditBox("Bullets count").type(Integer.class).def(10);

        destroyBombScore = addEditBox("Destroy bomb score").type(Integer.class).def(30);
        destroyStoneScore = addEditBox("Destroy stone score").type(Integer.class).def(10);
        destroyEnemyScore = addEditBox("Destroy enemy score").type(Integer.class).def(500);
        loosePenalty = addEditBox("Loose penalty").type(Integer.class).def(100);

        levelMap = addEditBox("Level map").multiline().type(String.class).def(
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼              ☺             ☼");
    }

    // getters

    public int destroyBombScore() {
        return destroyBombScore.getValue();
    }

    public int destroyStoneScore() {
        return destroyStoneScore.getValue();
    }

    public int destroyEnemyScore() {
        return destroyEnemyScore.getValue();
    }

    public int loosePenalty() {
        return loosePenalty.getValue();
    }

    public Integer ticksToRecharge() {
        return ticksToRecharge.getValue();
    }

    public Integer bulletsCount() {
        return bulletsCount.getValue();
    }

    public Level level() {
        return new LevelImpl(levelMap.getValue());
    }

    // setters

    public GameSettings ticksToRecharge(int value) {
        ticksToRecharge.update(value);
        return this;
    }

    public GameSettings bulletsCount(int value) {
        bulletsCount.update(value);
        return this;
    }

    public GameSettings destroyStoneScore(int value) {
        destroyStoneScore.update(value);
        return this;
    }

    public GameSettings destroyEnemyScore(int value) {
        destroyEnemyScore.update(value);
        return this;
    }

    public GameSettings loosePenalty(int value) {
        loosePenalty.update(value);
        return this;
    }

    public GameSettings destroyBombScore(int value) {
        destroyBombScore.update(value);
        return this;
    }

    public GameSettings levelMap(String value) {
        levelMap.update(value);
        return this;
    }

}
