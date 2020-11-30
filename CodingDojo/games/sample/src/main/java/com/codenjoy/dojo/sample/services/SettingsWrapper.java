package com.codenjoy.dojo.sample.services;

/*-
 * #%L
 * expansion - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2020 Codenjoy
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


import com.codenjoy.dojo.sample.model.LevelImpl;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;

public final class SettingsWrapper {

    public static SettingsWrapper data;

    private final Parameter<Integer> winScore;
    private final Parameter<Integer> loosePenalty;
    private final Parameter<String> levelMap;

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

        winScore = settings.addEditBox("Win score").type(Integer.class).def(30);
        loosePenalty = settings.addEditBox("Loose penalty").type(Integer.class).def(100);
        levelMap = settings.addEditBox("Level map").multiline().type(String.class)
                .def("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
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

    public int loosePenalty() {
        return loosePenalty.getValue();
    }

    public int winScore() {
        return winScore.getValue();
    }

    public String levelMap() {
        return levelMap.getValue();
    }

    public int getSize() {
        return new LevelImpl(levelMap()).getSize(); // TODO а что если уровней несколько?
    }

    // setters for testing

    public SettingsWrapper loosePenalty(int value) {
        loosePenalty.update(value);
        return this;
    }

    public SettingsWrapper winScore(int value) {
        winScore.update(value);
        return this;
    }

    public SettingsWrapper levelMap(String value) {
        levelMap.update(value);
        return this;
    }

}
