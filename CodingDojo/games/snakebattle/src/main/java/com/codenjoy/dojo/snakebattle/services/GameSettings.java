package com.codenjoy.dojo.snakebattle.services;

import com.codenjoy.dojo.services.round.RoundSettings;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.snakebattle.model.level.Level;
import com.codenjoy.dojo.snakebattle.model.level.LevelImpl;

public class GameSettings extends SettingsImpl implements RoundSettings {

    private Parameter<Integer> minTicksForWin;
    private Parameter<Integer> roundsPerMatch;
    private Parameter<Integer> timeBeforeStart;
    private Parameter<Integer> timeForWinner;
    private Parameter<Integer> timePerRound;
    private Parameter<Boolean> roundsEnabled;
    private Parameter<Integer> flyingCount;
    private Parameter<Integer> furyCount;
    private Parameter<Integer> playersPerRoom;
    private Parameter<Integer> stoneReduced;
    private Parameter<Integer> winScore;
    private Parameter<Integer> appleScore;
    private Parameter<Integer> goldScore;
    private Parameter<Integer> diePenalty;
    private Parameter<Integer> stoneScore;
    private Parameter<Integer> eatScore;
    private Parameter<String> levelMap;

    public GameSettings() {
        roundsEnabled = addCheckBox("[Game][Rounds] Enabled").type(Boolean.class).def(true);
        timePerRound = addEditBox("[Rounds] Time per Round").type(Integer.class).def(300);
        timeForWinner = addEditBox("[Rounds] Time for Winner").type(Integer.class).def(1);
        timeBeforeStart = addEditBox("[Rounds] Time before start Round").type(Integer.class).def(5);
        roundsPerMatch = addEditBox("[Rounds] Rounds per Match").type(Integer.class).def(3);
        minTicksForWin = addEditBox("[Rounds] Min ticks for win").type(Integer.class).def(40);

        playersPerRoom = addEditBox("Players per Room").type(Integer.class).def(5);
        flyingCount = addEditBox("Flying count").type(Integer.class).def(10);
        furyCount = addEditBox("Fury count").type(Integer.class).def(10);
        stoneReduced = addEditBox("Stone reduced value").type(Integer.class).def(3);

        winScore = addEditBox("Win score").type(Integer.class).def(50);
        appleScore = addEditBox("Apple score").type(Integer.class).def(1);
        goldScore = addEditBox("Gold score").type(Integer.class).def(10);
        diePenalty = addEditBox("Die penalty").type(Integer.class).def(0);
        stoneScore = addEditBox("Stone score").type(Integer.class).def(5);
        eatScore = addEditBox("Eat enemy score").type(Integer.class).def(10);

        levelMap = addEditBox("Level map").type(String.class).multiline().def(
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼☼         ○                 ☼" +
                "☼#                           ☼" +
                "☼☼  ○   ☼#         ○         ☼" +
                "☼☼                      ○    ☼" +
                "☼# ○         ●               ☼" +
                "☼☼                ☼#        %☼" +
                "☼☼      ☼☼☼        ☼  ☼      ☼" +
                "☼#      ☼      ○   ☼  ☼      ☼" +
                "☼☼      ☼○         ☼  ☼      ☼" +
                "☼☼      ☼☼☼               ●  ☼" +
                "☼#              ☼#           ☼" +
                "☼☼○                         $☼" +
                "☼☼    ●              ☼       ☼" +
                "☼#             ○             ☼" +
                "☼☼                           ☼" +
                "☼☼   ○             ☼#        ☼" +
                "☼#       ☼☼ ☼                ☼" +
                "☼☼          ☼     ●     ○    ☼" +
                "☼☼       ☼☼ ☼                ☼" +
                "☼#          ☼               @☼" +
                "☼☼         ☼#                ☼" +
                "☼☼           ○               ☼" +
                "☼#                  ☼☼☼      ☼" +
                "☼☼                           ☼" +
                "☼☼      ○        ☼☼☼#    ○   ☼" +
                "☼#                           ☼" +
                "☼☼     ╘►        ○           ☼" +
                "☼☼                           ☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼");
    }

    // getters

    @Override
    public Parameter<Integer> timeBeforeStart() {
        return timeBeforeStart;
    }

    @Override
    public Parameter<Integer> roundsPerMatch() {
        return roundsPerMatch;
    }

    @Override
    public Parameter<Integer> minTicksForWin() {
        return minTicksForWin;
    }

    @Override
    public Parameter<Integer> timePerRound() {
        return timePerRound;
    }

    @Override
    public Parameter<Integer> timeForWinner() {
        return timeForWinner;
    }

    @Override
    public Parameter<Boolean> roundsEnabled() {
        return roundsEnabled;
    }

    public Parameter<Boolean> roundsEnabledValue() {
        return roundsEnabled;
    }

    public Parameter<Integer> flyingCount() {
        return flyingCount;
    }

    public Parameter<Integer> furyCount() {
        return furyCount;
    }

    public Parameter<Integer> playersPerRoom() {
        return playersPerRoom;
    }

    public Parameter<Integer> stoneReduced() {
        return stoneReduced;
    }

    public Level level() {
        return new LevelImpl(levelMap.getValue());
    }

    public Parameter<Integer> winScore() {
        return winScore;
    }

    public Parameter<Integer> appleScore() {
        return appleScore;
    }

    public Parameter<Integer> goldScore() {
        return goldScore;
    }

    public Parameter<Integer> diePenalty() {
        return diePenalty;
    }

    public Parameter<Integer> stoneScore() {
        return stoneScore;
    }

    public Parameter<Integer> eatScore() {
        return eatScore;
    }

    // setters

    public GameSettings minTicksForWin(int value) {
        minTicksForWin.update(value);
        return this;
    }

    public GameSettings roundsPerMatch(int value) {
        roundsPerMatch.update(value);
        return this;
    }

    public GameSettings timeBeforeStart(int value) {
        timeBeforeStart.update(value);
        return this;
    }

    public GameSettings timeForWinner(int value) {
        timeForWinner.update(value);
        return this;
    }

    public GameSettings timePerRound(int value) {
        timePerRound.update(value);
        return this;
    }

    public GameSettings roundsEnabled(boolean value) {
        roundsEnabled.update(value);
        return this;
    }

    public GameSettings flyingCount(int value) {
        flyingCount.update(value);
        return this;
    }

    public GameSettings furyCount(int value) {
        furyCount.update(value);
        return this;
    }

    public GameSettings playersPerRoom(int value) {
        playersPerRoom.update(value);
        return this;
    }

    public GameSettings stoneReduced(int value) {
        stoneReduced.update(value);
        return this;
    }

    public GameSettings winScore(int value) {
        winScore.update(value);
        return this;
    }

    public GameSettings appleScore(int value) {
        appleScore.update(value);
        return this;
    }

    public GameSettings goldScore(int value) {
        goldScore.update(value);
        return this;
    }

    public GameSettings diePenalty(int value) {
        diePenalty.update(value);
        return this;
    }

    public GameSettings stoneScore(int value) {
        stoneScore.update(value);
        return this;
    }

    public GameSettings eatScore(int value) {
        eatScore.update(value);
        return this;
    }

    public GameSettings levelMap(String value) {
        levelMap.update(value);
        return this;
    }

}
