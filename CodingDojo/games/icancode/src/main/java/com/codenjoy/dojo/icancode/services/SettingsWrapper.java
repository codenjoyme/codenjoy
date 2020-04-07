package com.codenjoy.dojo.icancode.services;

/*-
 * #%L
 * expansion - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2017 EPAM
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


import com.codenjoy.dojo.icancode.services.levels.Level;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;

public final class SettingsWrapper {

    public static SettingsWrapper data;

    private final Parameter<Integer> winScore;
    private final Parameter<Integer> goldScore;
    private final Parameter<Integer> loosePenalty;
    private final Parameter<Boolean> isTrainingMode;
    private final Settings settings;

    public static SettingsWrapper setup(Settings settings) {
        return new SettingsWrapper(settings);
    }

    // for testing
    public static SettingsWrapper setup() {
        return setup(new SettingsImpl());
    }

    private SettingsWrapper(Settings settings) {
        data = this;
        this.settings = settings;

        winScore = settings.addEditBox("Win score").type(Integer.class).def(50);
        goldScore = settings.addEditBox("Gold score").type(Integer.class).def(10);
        loosePenalty = settings.addEditBox("Loose penalty").type(Integer.class).def(0);
        isTrainingMode = settings.addCheckBox("Is training mode").type(Boolean.class).def(true);

        Levels.setup();
    }

    public int goldScore() {
        return goldScore.getValue();
    }

    public int loosePenalty() {
        return loosePenalty.getValue();
    }

    public int winScore() {
        return winScore.getValue();
    }

    public boolean isTrainingMode() {
        return isTrainingMode.getValue();
    }

    public String levelMap(int index) {
        String prefix = levelPrefix(index);
        return settings.addEditBox(prefix + "map").type(String.class).getValue();
    }

    public int levelsCount() {
        return settings.addEditBox("levels.count").type(Integer.class).getValue();
    }

    public SettingsWrapper addLevel(int index, Level level) {
        settings.addEditBox("levels.count").type(Integer.class).def(0).update(index);

        String prefix = levelPrefix(index);
        settings.addEditBox(prefix + "map").multiline().type(String.class).def(level.map());
        settings.addEditBox(prefix + "help").multiline().type(String.class).def(level.help());
        settings.addEditBox(prefix + "defaultCode").multiline().type(String.class).def(level.defaultCode());
        settings.addEditBox(prefix + "refactoringCode").multiline().type(String.class).def(level.refactoringCode());
        settings.addEditBox(prefix + "winCode").multiline().type(String.class).def(level.winCode());
        settings.addEditBox(prefix + "autocomplete").multiline().type(String.class).def(level.autocomplete().replace("'", "\""));
        settings.addEditBox(prefix + "befungeCommands").multiline().type(String.class).def(String.join("\n", level.befungeCommands()));
        return this;
    }

    private String levelPrefix(int index) {
        return "level" + index + ".";
    }

    // setters for testing

    public SettingsWrapper loosePenalty(int value) {
        loosePenalty.update(value);
        return this;
    }

    public SettingsWrapper goldScore(int value) {
        goldScore.update(value);
        return this;
    }

    public SettingsWrapper winScore(int value) {
        winScore.update(value);
        return this;
    }

    public SettingsWrapper isTrainingMode(boolean value) {
        isTrainingMode.update(value);
        return this;
    }

}
