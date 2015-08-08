package com.codenjoy.dojo.pong.services;

import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

/**
 * Класс, который умеет подсчитывать очки за те или иные действия.
 * Обычно хочется, чтобы константы очков не были захардкоджены, потому используй объект {@see Settings} для их хранения.
 */
public class Scores implements PlayerScores {

    private final Parameter<Integer> winScore;

    private volatile int score;

    public Scores(int startScore, Settings settings) {
        this.score = startScore;

        // вот тут мы на админке увидим два поля с подписями и возожностью редактировать значение по умолчанию
        winScore = settings.addEditBox("Win score").type(Integer.class).def(1);
    }

    @Override
    public int clear() {
        return score = 0;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public void event(Object event) {
        if (event.equals(Events.WIN)) {
            score += winScore.getValue();
        }
        score = Math.max(0, score);
    }
}
