package com.codenjoy.dojo.a2048.model;

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


import com.codenjoy.dojo.a2048.model.generator.Generator;
import com.codenjoy.dojo.a2048.services.Events;
import com.codenjoy.dojo.a2048.services.GameSettings;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;


import java.util.Iterator;

public class A2048 implements Field {

    private Numbers numbers;
    private final int size;
    private Dice dice;
    private Player player;
    private Level level;

    private GameSettings settings;

    public A2048(Level level, Dice dice, GameSettings settings) {
        this.level = level;
        this.dice = dice;
        size = level.size();
        numbers = new Numbers(level.numbers(), size, level.breaks());
        this.settings = settings;
    }

    @Override
    public void clearScore() {
        hero().clear();
    }

    @Override
    public void newGame(Player player) {
        if (this.player != null) {
            numbers.clear();
        }
        this.player = player;
        player.newHero(this);
    }

    @Override
    public void remove(Player player) {
        // do nothing
    }

    @Override
    public void tick() {
        if (isGameOver()) {
            return;
        }

        if (numbers.isEmpty()) {
            hero().down();
        }

        if (hero().getDirection() != null) {
            numbers.move(hero().getDirection());

            generateNewNumber();
        }

        int sum = numbers.getSum();
        player.event(new Events(Events.Event.SUM, sum));

        if (isWin()) {
            player.event(new Events(Events.Event.WIN));
        } else if (isGameOver()) {
            player.event(new Events(Events.Event.GAME_OVER));
        }

        hero().clearDirection();
    }

    private Hero hero() {
        return player.getHero();
    }

    private void generateNewNumber() {
        settings.generator(dice).generate(numbers);
    }

    public int size() {
        return size;
    }

    public Numbers getNumbers() {
        return numbers;
    }

    @Override
    public boolean isGameOver() {
        if (hero().isClear()) return true;
        if (isWin()) return true;
        if (!numbers.isFull()) return false;
        return !numbers.canGo();
    }

    private boolean isWin() {
        return numbers.contains(Elements._4194304);
    }

    @Override
    public BoardReader reader() {
        return new BoardReader<Player>() {
            @Override
            public int size() {
                return A2048.this.size;
            }

            @Override
            public Iterable<? extends Point> elements(Player player) {
                return () -> new Iterator<Point>() {
                    private int x = 0;
                    private int y = 0;
                    private Numbers numb = A2048.this.numbers;
                    private int size = A2048.this.size;

                    @Override
                    public boolean hasNext() {
                        return y != size;
                    }

                    @Override
                    public Point next() {
                        Number number = numb.get(x, y);
                        x++;
                        if (x == size) {
                            x = 0;
                            y++;
                        }
                        return number;
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    @Override
    public GameSettings settings() {
        return settings;
    }
}
