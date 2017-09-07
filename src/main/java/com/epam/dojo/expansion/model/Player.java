package com.epam.dojo.expansion.model;

/*-
 * #%L
 * iCanCode - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 EPAM
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


import com.codenjoy.dojo.services.DLoggerFactory;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.utils.JsonUtils;
import com.epam.dojo.expansion.model.levels.items.Hero;
import com.epam.dojo.expansion.services.Events;
import org.json.JSONObject;
import org.slf4j.Logger;

public class Player {

    private static Logger logger = DLoggerFactory.getLogger(Player.class);

    private EventListener listener;
    Hero hero;
    private ProgressBar progressBar;

    public Player(EventListener listener, ProgressBar progressBar) {
        this.listener = listener;
        this.progressBar = progressBar;
        progressBar.setPlayer(this);
    }

    public int getMaxScore() {
        return 0;
    }

    public int getScore() {
        return 0;
    }

    public void event(Events event) {
        if (logger.isDebugEnabled()) {
            logger.debug("Player {} fired event {}", lg.id(), event);
        }

        if (listener != null && progressBar.enableWinScore()) {
            listener.event(event);
        }
    }

    public Hero getHero() {
        return hero;
    }

    public void newHero(Field field) {
        if (hero == null) {
            hero = new Hero();
        }

        hero.setField(field);
    }

    public void tick() {
        progressBar.checkLevel();
        hero.tick();
    }

    public void setNextLevel() {
        progressBar.setNextLevel();
    }

    public int getForcesColor() {
        return hero.getBase().element().getIndex();
    }

    public Game getGame() {
        return progressBar.getGameOwner();
    }

    public Point getBasePosition() {
        return hero.getBasePosition();
    }

    public JSONObject getCurrentAction() {
        return hero.getCurrentAction();
    }

    public int getForcesPerTick() {
        return hero.getForcesPerTick();
    }

    public void destroyHero() {
        if (hero != null) {
            hero.destroy();
        }
    }

    public class LogState {
        public JSONObject json() {
            return new JSONObject(){{
                put("id", id());
                put("hero", hero.lg.json());
                put("progressBar", progressBar.lg.json());
            }};
        }

        public String id() {
            return "P@" + Integer.toHexString(Player.this.hashCode());
        }
    }

    public LogState lg = new LogState();

    @Override
    public String toString() {
        return JsonUtils.toStringSorted(lg.json());
    }
}