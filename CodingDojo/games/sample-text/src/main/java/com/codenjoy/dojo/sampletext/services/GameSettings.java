package com.codenjoy.dojo.sampletext.services;

import com.codenjoy.dojo.sampletext.model.Level;
import com.codenjoy.dojo.sampletext.model.LevelImpl;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SettingsReader;

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

    public GameSettings() {
        addEditBox(WIN_SCORE.key()).type(Integer.class).def(30);
        addEditBox(LOOSE_PENALTY.key()).type(Integer.class).def(100);
     
        addEditBox(QUESTIONS.key()).multiline().type(String.class).def(
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
