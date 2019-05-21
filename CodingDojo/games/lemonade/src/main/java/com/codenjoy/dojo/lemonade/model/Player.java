package com.codenjoy.dojo.lemonade.model;

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


import com.codenjoy.dojo.lemonade.services.EventArgs;
import com.codenjoy.dojo.lemonade.services.EventType;
import com.codenjoy.dojo.lemonade.services.ScoreMode;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Класс игрока. Тут кроме героя может подсчитываться очки.
 * Тут же ивенты передабтся лиснеру фреймворка.
 */
public class Player extends GamePlayer<Hero, GameField<Player>> {

    private Queue<SalesResult> history;
    private final GameSettings gameSettings;
    Hero hero;
    private long heroRandomSeed;

    public Player(EventListener listener, long heroRandomSeed, GameSettings gameSettings) {
        super(listener);
        this.gameSettings = gameSettings;
        this.heroRandomSeed = heroRandomSeed;
        history = new LinkedList<>();
    }

    public void clearScore() {
        if (history != null) {
            history.clear();
        }

        if (hero != null) {
            hero.init(null);
        }
    }

    public Hero getHero() {
        return hero;
    }

    public void newHero(GameField<Player> field) {
        hero = new Hero(heroRandomSeed, gameSettings, history);
        hero.init(field);
    }

    @Override
    public boolean isAlive() {
        return hero != null && hero.isAlive();
    }

    public JSONObject getNextQuestion() { // TODO test me
        return hero.getNextQuestion().toJson();
    }

    public JSONArray getHistoryJson() {
        JSONArray historyJson = new JSONArray();
        history.forEach(sr -> historyJson.put(sr.toJSONObject()));
        return historyJson;
    }

    public void checkAnswer() {
        hero.tick();
        SalesResult salesResult = hero.popSalesResult();

        // put to history and raise events if there is salesResult and no input errors
        if (salesResult != null && !salesResult.isInputError()) {
            int day = salesResult.getDay();
            boolean isLastDayAssetsGameMode = gameSettings.getScoreMode() == ScoreMode.LAST_DAY_ASSETS;
            if (isLastDayAssetsGameMode && day > gameSettings.getLimitDays()) {
                return;
            }

            history.add(salesResult);
            while (history.size() > 10)
                history.remove();
            if (salesResult.isBankrupt()) {
                event(new EventArgs(EventType.LOOSE,
                        salesResult.getProfit(),
                        salesResult.getAssetsAfter()));
            } else {
                // raise WIN event only on SUM_OF_PROFITS game mode OR on the last day in LAST_DAY_ASSETS game mode
                if (!isLastDayAssetsGameMode || day == gameSettings.getLimitDays()) {
                    event(new EventArgs(EventType.WIN,
                            salesResult.getProfit(),
                            salesResult.getAssetsAfter()));
                }
            }
        }
    }

    public void updateSeed(int newSeed) {
        heroRandomSeed = newSeed;
    }
}
