package com.codenjoy.dojo.sampletext.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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

import com.codenjoy.dojo.sampletext.model.Level;
import com.codenjoy.dojo.sampletext.model.LevelImpl;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SettingsReader;

import java.util.Arrays;
import java.util.List;

import static com.codenjoy.dojo.sampletext.services.GameSettings.Keys.*;

public class GameSettings extends SettingsImpl implements SettingsReader<GameSettings> {

    public enum Keys implements Key {

        WIN_SCORE("Win score"),
        LOOSE_PENALTY("Loose penalty"),
        QUESTIONS("Questions");

        private String key;

        Keys(String key) {
            this.key = key;
        }

        @Override
        public String key() {
            return key;
        }
    }

    @Override
    public List<Key> allKeys() {
        return Arrays.asList(Keys.values());
    }

    public GameSettings() {
        integer(WIN_SCORE, 30);
        integer(LOOSE_PENALTY, 100);
     
        multiline(QUESTIONS,
                "question1=answer1\n" +
                "question2=answer2\n" +
                "question3=answer3\n" +
                "question4=answer4\n" +
                "question5=answer5\n" +
                "question6=answer6\n" +
                "question7=answer7\n" +
                "question8=answer8\n" +
                "question9=answer9\n" +
                "question10=answer10\n" +
                "question11=answer11\n" +
                "question12=answer12\n" +
                "question13=answer13\n" +
                "question14=answer14\n" +
                "question15=answer15\n" +
                "question16=answer16\n" +
                "question17=answer17\n" +
                "question18=answer18\n" +
                "question19=answer19\n" +
                "question20=answer20");
    }

    public Level level() {
        return new LevelImpl(string(QUESTIONS).split("\n"));
    }
}
