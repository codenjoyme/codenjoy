package com.epam.dojo.icancode.services;

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
        loosePenalty = settings.addEditBox("Loose penalty").type(Integer.class).def(10);
    }

    @Override
    public int clear() {
        score = 0;
        return 0;
    }

    @Override
    public int getScore() {
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
