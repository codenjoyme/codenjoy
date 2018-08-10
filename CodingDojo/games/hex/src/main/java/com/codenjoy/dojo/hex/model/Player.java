package com.codenjoy.dojo.hex.model;

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


import com.codenjoy.dojo.hex.services.Event;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.Tickable;
import com.codenjoy.dojo.services.joystick.DirectionActJoystick;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static com.codenjoy.dojo.services.PointImpl.pt;

public class Player extends GamePlayer<Hero, Field> implements Tickable {

    private Field field;
    private Hero active;
    private boolean alive;
    private Heroes heroes;
    private Hero newHero;
    private Elements element;
    private int loose;
    private int win;

    public Player(EventListener listener) {
        super(listener);
        alive = false;
        heroes = new Heroes();
    }

    public Heroes getHeroes() {
        return heroes;
    }

    @Override
    public Hero getHero() {
        return newHero;
    }

    @Override
    public void newHero(Field field) {
        this.field = field;
        Point pt = field.getFreeRandom();
        Hero hero = new Hero(pt, element);
        hero.init(field);
        heroes.clear();
        heroes.add(hero, this);
        alive = true;
    }

    public void addHero(Hero newHero) {
        this.newHero = newHero;
    }

    @Override
    public Joystick getJoystick() {
        return new DirectionActJoystick() {
            @Override
            public void down() {
                if (active == null || !alive) return;
                active.down();
            }

            @Override
            public void up() {
                if (active == null || !alive) return;
                active.up();
            }

            @Override
            public void left() {
                if (active == null || !alive) return;
                active.left();
            }

            @Override
            public void right() {
                if (active == null || !alive) return;
                active.right();
            }

            @Override
            public void act(int... p) {
                if (!alive) return;
                int x = p[0]; // TODO validation
                int y = field.size() - 1 - p[1];
                boolean jump = p.length == 3 && p[2] == 1;

                Hero hero = field.getHero(x, y);
                if (hero != null && heroes.contains(hero)) {
                    active = hero;
                    active.isJump(jump);
                    if (jump) {
                        addHero(hero);
                    }
                } else {
                    active = null;
                }
            }
        };
    }

    public boolean isAlive() {
        return alive;
    }

    public void remove(Hero hero) {
        if (newHero == hero) {
            boolean remove = heroes.remove(hero);
            if (remove) {
                loose(1);
            }
            newHero = null;
        }
    }

    public void applyNew() {
        if (newHero != null && !heroes.contains(newHero)) {
            heroes.add(newHero, this);
            newHero = null;
            win(1);
        }
    }

    @Override
    public void tick() {
        if (heroes.isEmpty() && newHero == null) {
            die();
            return;
        }
        for (Hero hero : heroes) {
            hero.tick();
        }
        if (newHero != null) {
            newHero.tick();
        }

        active = null;
    }

    public void die() {
        alive = false;
        heroes.clear();
        newHero = null;
    }

    public boolean itsMine(Hero hero) {
        return heroes.contains(hero) || hero == newHero;
    }

    public Elements getElement() {
        return element;
    }

    public void setElement(Elements element) {
        this.element = element;
    }

    public void loose(int count) {
        loose += count;
    }

    public void win(int count) {
        win += count;
    }

    public void fireEvents() {
        if (loose > 0) {
            listener.event(new Event(Event.EventEnum.LOOSE, loose));
            loose = 0;
        }

        if (win > 0) {
            listener.event(new Event(Event.EventEnum.WIN, win));
            win = 0;
        }
    }

    public Hero getActive() {
        return active;
    }

    public Hero getNewHero() {
        return newHero;
    }
}
