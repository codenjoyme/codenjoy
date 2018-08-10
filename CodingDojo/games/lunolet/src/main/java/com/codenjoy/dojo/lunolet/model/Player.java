package com.codenjoy.dojo.lunolet.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2018 Codenjoy
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


import com.codenjoy.dojo.lunolet.services.Events;
import com.codenjoy.dojo.services.EventListener;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

public class Player {

    private EventListener listener;
    private int maxScore;
    private int score;
    private LevelManager levelManager;
    private Hero hero;
    private List<Point2D.Double> crashes;

    public Player(EventListener listener) {
        this.listener = listener;
        crashes = new LinkedList<Point2D.Double>();
        clearScore();
    }

    public Hero getHero() {
        return hero;
    }

    public List<Point2D.Double> getCrashes() {
        return crashes;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public int getScore() {
        return score;
    }

    public void clearScore() {
        score = 0;
    }

    public void resetLevels() {
        if (levelManager != null) {
            levelManager.reset();
        }
        if (hero != null) {
            hero.init(levelManager);
        }
        crashes.clear();
    }

    private void increaseScore() {
        score = score + 10;
        maxScore = Math.max(maxScore, score);
    }

    public void newHero(LevelManager levelManager) {
        hero = new Hero(this);
        this.levelManager = levelManager;
        hero.init(levelManager);
    }

    public void event(Events event) {
        if (event == Events.LANDED) {
            increaseScore();
            levelManager.levelUp();
            crashes.clear();
        } else if (event == Events.CRASHED) {
            addCrash(hero.getVesselStatus().getPoint());
        }

        if (listener != null) {
            listener.event(event);
        }
    }

    private void addCrash(Point2D.Double ptCrash) {
        for (int i = 0; i < crashes.size(); i++) {
            Point2D.Double pt = crashes.get(i);
            if (Math.abs(pt.x - ptCrash.x) < 1e-3 && Math.abs(pt.y - ptCrash.y) < 1e-3)
                return;  // already have the point
        }
        crashes.add(ptCrash);
        if (crashes.size() > 100)
            crashes.remove(0);
    }
}
