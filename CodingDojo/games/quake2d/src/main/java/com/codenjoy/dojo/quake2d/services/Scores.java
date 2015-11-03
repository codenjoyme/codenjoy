package com.codenjoy.dojo.quake2d.services;

import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

/**
 * Класс, который умеет подсчитывать очки за те или иные действия.
 * Обычно хочется, чтобы константы очков не были захардкоджены, потому используй объект {@see Settings} для их хранения.
 */
public class Scores implements PlayerScores {

    private final Parameter<Integer> killScore;
    private final Parameter<Integer> injureScore;

    private volatile int score;

    public Scores(int startScore, Settings settings) {
        this.score = startScore;

        // вот тут мы на админке увидим два поля с подписями и возожностью редактировать значение по умолчанию
        killScore = settings.addEditBox("Kill score").type(Integer.class).def(30);
        injureScore = settings.addEditBox("Injure score").type(Integer.class).def(10);
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
        if (event.equals(Events.KILL)) {
            score += killScore.getValue();
        } else if (event.equals(Events.INJURE)) {
            score += injureScore.getValue();
        }
        score = Math.max(0, score);
    }
}
