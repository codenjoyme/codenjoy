package com.codenjoy.dojo.fifteen.services;

import com.codenjoy.dojo.fifteen.model.Bonus;
import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

/**
 * Класс, который умеет подсчитывать очки за те или иные действия.
 * Обычно хочется, чтобы константы очков не были захардкоджены, потому используй объект {@see Settings} для их хранения.
 */
public class Scores implements PlayerScores {
    private final Parameter<Integer> bonusScore;
    private final Parameter<Integer> winScore;
    private final Parameter<Integer> loosePenalty;

    private volatile int score;

    public Scores(int startScore, Settings settings) {
        this.score = startScore;

        // вот тут мы на админке увидим два поля с подписями и возожностью редактировать значение по умолчанию
        winScore = settings.addEditBox("Win score").type(Integer.class).def(30);
        loosePenalty = settings.addEditBox("Loose penalty").type(Integer.class).def(100);
        bonusScore = settings.addEditBox("Bonus score").type(Integer.class).def(100);
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
        if (event instanceof Bonus) {
            Bonus bonus = (Bonus) event;
            score += bonusScore.getValue() * bonus.getNumber() / bonus.getMoveCount();
        } else if (event.equals(Events.WIN)) {
            score += winScore.getValue();
        }

        score = Math.max(0, score);
    }
}
