package com.codenjoy.dojo.spacerace.services;

import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

/**
 * Класс, который умеет подсчитывать очки за те или иные действия.
 * Обычно хочется, чтобы константы очков не были захардкоджены, потому используй объект {@see Settings} для их хранения.
 */
public class Scores implements PlayerScores {

    private final Parameter<Integer> destroyBombScore;
    private final Parameter<Integer> destroyStoneScore;
    private final Parameter<Integer> destroyEnemyScore;
    private final Parameter<Integer> loosePenalty;

    private volatile int score;

    public Scores(int startScore, Settings settings) {
        this.score = startScore;

        // вот тут мы на админке увидим два поля с подписями и возожностью редактировать значение по умолчанию
        destroyBombScore = settings.addEditBox("Destroy bomb score").type(Integer.class).def(30);
        destroyStoneScore = settings.addEditBox("Destroy stone score").type(Integer.class).def(10);
        destroyEnemyScore = settings.addEditBox("Destroy enemy score").type(Integer.class).def(500);
        loosePenalty = settings.addEditBox("Loose penalty").type(Integer.class).def(100);
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
        if (event.equals(Events.DESTROY_BOMB)) {
            score += destroyBombScore.getValue();
        } else if (event.equals(Events.DESTROY_STONE)) {
            score += destroyStoneScore.getValue();
        } else if (event.equals(Events.DESTROY_ENEMY)) {
            score += destroyEnemyScore.getValue();
        } else if (event.equals(Events.LOOSE)) {
            score -= loosePenalty.getValue();
        }
        score = Math.max(0, score);
    }
}
