package com.codenjoy.dojo.kata.model;

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


import com.codenjoy.dojo.kata.model.levels.LevelsPool;
import com.codenjoy.dojo.kata.services.events.NextAlgorithmEvent;
import com.codenjoy.dojo.kata.services.events.PassTestEvent;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public class Player extends GamePlayer<Hero, Field> {

    private static Logger logger = LoggerFactory.getLogger(Player.class);

    private LevelsPool level;
    private List<QuestionAnswers> history;
    Hero hero;
    private Timer timer = new Timer();

    public Player(EventListener listener, LevelsPool level) {
        super(listener);
        this.history = new LinkedList<>();
        this.level = level;
        level.firstLevel();
    }

    public int getLevel() {
        return level.getLevelIndex();
    }

    @Override
    public String toString() {
        return "{" + Integer.toHexString(this.hashCode()) + "}";
    }

    public void event(Object event) {
        logger.info("Player '{}' on Level '{}:{}' fired event '{}'",
                this.toString(),
                level.getLevelIndex(), level.getQuestionIndex(),
                event);

        super.event(event);
    }

    public void clearScore() {
        if (history != null) {
            history.clear();
            level.firstLevel();
        }
    }

    public Hero getHero() {
        return hero;
    }

    public void newHero(Field field) {
        hero = new Hero();
        hero.init(field);
    }

    @Override
    public boolean isAlive() {
        return hero != null && hero.isAlive();
    }

    public List<String> getQuestions() {
        return level.getQuestions();
    }

    public List<QuestionAnswer> getLastHistory() {
        if (history.isEmpty()) {
            return null;
        }
        return history.get(history.size() - 1).getQuestionAnswers();
    }

    public List<QuestionAnswers> getHistory() {
        List<QuestionAnswers> result = new LinkedList<>();
        result.addAll(history);
        return result;
    }

    public void checkAnswer() {
        hero.tick();

        if (hero.wantsSkipLevel()) {
            hero.clearFlags();
            level.waitNext();
            return;
        }

        if (hero.wantsNextLevel()) {
            hero.clearFlags();
            if (level.isWaitNext()) {
                timer.start();
                level.nextLevel();
                logNextAttempt();
            }
            return;
        }

        List<String> actualAnswers = hero.popAnswers();
        if (actualAnswers.isEmpty()) {
            return;
        }

        logNextAttempt();

        if (level.isLastQuestion()) {
            return;
        }

        List<String> questions = level.getQuestions();
        List<String> expectedAnswers = level.getAnswers();
        boolean isWin = true;
        for (int index = 0; index < questions.size(); index++) {
            String question = questions.get(index);
            String expectedAnswer = expectedAnswers.get(index);
            String actualAnswer = "???";
            if (index < actualAnswers.size()) {
                actualAnswer = actualAnswers.get(index);
            }

            if (expectedAnswer.equals(actualAnswer)) {
                logSuccess(question, actualAnswer);
            } else {
                logFailure(question, actualAnswer);
                isWin = false;
            }
        }

        if (isWin) {
            event(new PassTestEvent(level.getComplexity(), level.getTotalQuestions()));
            boolean levelFinished = level.isLevelFinished();
            if (levelFinished) {
                event(new NextAlgorithmEvent(level.getComplexity(), timer.end()));
                level.waitNext();
            } else {
                level.nextQuestion();
            }
        } else {
            // do nothing, gamification is a positive thing
        }
    }

    private void logSuccess(String question, String answer) {
        log(question, answer, true);
    }

    private void logFailure(String question, String answer) {
        log(question, answer, false);
    }

    private void logNextAttempt() {
        history.add(new QuestionAnswers());
    }

    private void log(String question, String answer, boolean valid) {
        QuestionAnswer qa = new QuestionAnswer(question, answer);
        qa.setValid(valid);
        history.get(history.size() - 1).add(qa);
    }

    public String getDescription() {
        return level.getDescription();
    }

    public String getNextQuestion() {
        List<String> questions = getQuestions();
        return (questions.isEmpty()) ? null : questions.get(questions.size() - 1);
    }
}
