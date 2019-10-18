package com.codenjoy.dojo.lemonade.services;

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


import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

/**
 * Класс, который умеет подсчитывать очки за те или иные действия.
 * Обычно хочется, чтобы константы очков не были захардкоджены, потому используй объект {@see Settings} для их хранения.
 */
public class Scores implements PlayerScores {

    private final Parameter<Integer> winScore;
    private final Parameter<Integer> bankruptPenalty;
    private final Parameter<Integer> limitDays;

    private volatile int score;

    public Scores(int startScore, Settings settings) {
        this.score = startScore;

        // вот тут мы на админке увидим два поля с подписями и возожностью редактировать значение по умолчанию
        winScore = settings.addEditBox("Win score").type(Integer.class).def(30);
        bankruptPenalty = settings.addEditBox("Bankrupt penalty").type(Integer.class).def(100);
        limitDays = settings.addEditBox("Limit days").type(Integer.class).def(30);
    }

    @Override
    public int clear() {
        return score = 0;
    }

    @Override
    public Integer getScore() {
        return score;
    }

    @Override
    public void event(Object event) {
        EventArgs eventArgs = (EventArgs) event;
        ScoreMode mode = getScoreMode();
        switch (eventArgs.type) {
            case WIN:
                if (mode == ScoreMode.SUM_OF_PROFITS)
                    score += toScore(eventArgs.profit);
                if (mode == ScoreMode.LAST_DAY_ASSETS)
                    score = Math.max(score, toScore(eventArgs.assetsAfter));
                break;
            case LOOSE:
                if (mode == ScoreMode.SUM_OF_PROFITS)
                    score -= bankruptPenalty.getValue();
                break;
        }
        score = Math.max(0, score);
    }

    private int toScore(double value) {
        return (int) (100 * value);
    }

    private ScoreMode getScoreMode() {
        return limitDays.getValue() > 0
                ? ScoreMode.LAST_DAY_ASSETS
                : ScoreMode.SUM_OF_PROFITS;
    }

    @Override
    public void update(Object score) {
        this.score = Integer.valueOf(score.toString());
    }

}
