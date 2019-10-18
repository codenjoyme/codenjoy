package com.codenjoy.dojo.icancode.services;

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
    private final Parameter<Integer> loosePenalty;
    private final Parameter<Integer> goldScore;

    private volatile int score;

    public Scores(int startScore, Settings settings) {
        this.score = startScore;

        // вот тут мы на админке увидим два поля с подписями и возожностью редактировать значение по умолчанию
        winScore = settings.addEditBox("Win score").type(Integer.class).def(50);
        goldScore = settings.addEditBox("Gold score").type(Integer.class).def(10);
        loosePenalty = settings.addEditBox("Loose penalty").type(Integer.class).def(0);
    }

    @Override
    public int clear() {
        score = 0;
        return 0;
    }

    @Override
    public void update(Object score) {
        this.score = Integer.valueOf(score.toString());
    }

    @Override
    public Object getScore() {
        return score;
    }

    @Override
    public void event(Object input) {
        Events events = (Events)input;

        if (events.getType() == Events.Type.WIN) {
            if (!events.isMultiple()) {
                score += winScore.getValue(); // TODO test me
            }
            score += goldScore.getValue()*events.getGoldCount();
        } else if (events.getType() == Events.Type.LOOSE) {
            score -= loosePenalty.getValue();
        }
        score = Math.max(0, score);
    }
}
