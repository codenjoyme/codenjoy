package com.codenjoy.dojo.hex.model;

import com.codenjoy.dojo.hex.services.HexEvents;
import com.codenjoy.dojo.sample.services.SampleEvents;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.Tickable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Player implements Tickable {

    private EventListener listener;
    private int maxScore;
    private int score;
    List<Hero> heroes;
    private Hero active;
    private boolean alive;
    private Field field;
    Hero newHero;
    private Elements element;

    public Player(EventListener listener, Field field) {
        this.listener = listener;
        this.field = field;
        this.alive = true;
        heroes = new LinkedList<Hero>();
        clearScore();
    }

    private void increaseScore() {
        score = score + 1;
        maxScore = Math.max(maxScore, score);
    }

    public int getMaxScore() {
        return maxScore;
    }

    public int getScore() {
        return score;
    }

    public void event(HexEvents event) {
        switch (event) {
            case LOOSE: gameOver(); break;
            case WIN: increaseScore(); break;
        }

        if (listener != null) {
            listener.event(event);
        }
    }

    private void gameOver() {
        score = 0;
    }

    public void clearScore() {
        score = 0;
        maxScore = 0;
    }

    public void newHero() {
        Point pt = field.getFreeRandom();
        Hero hero = new Hero(pt);
        hero.init(field);
        heroes.clear();
        heroes.add(hero);
        alive = true;
    }

    public void addHero(Hero newHero) {
        this.newHero = newHero;
    }

    public List<Hero> getHeroes() {
        return heroes;
    }

    public Joystick getJoystick() {
        return new Joystick() {
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
                int y = p[1];
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
                listener.event(HexEvents.LOOSE);
            }
            newHero = null;
        }
    }

    public void applyNew() {
        if (newHero != null && !heroes.contains(newHero)) {
            heroes.add(newHero);
            newHero = null;
            listener.event(HexEvents.WIN);
        }
    }

    @Override
    public void tick() {
        if (heroes.isEmpty() && newHero == null) {
            die();
            return;
        }
        for (Hero hero : heroes.toArray(new Hero[0])) {
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
}