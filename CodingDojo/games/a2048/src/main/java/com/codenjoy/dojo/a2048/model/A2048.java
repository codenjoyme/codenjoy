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


import com.codenjoy.dojo.a2048.model.generator.Factory;
import com.codenjoy.dojo.a2048.model.generator.Generator;
import com.codenjoy.dojo.a2048.services.Events;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;

public class A2048 implements Field {

    private Generator generator;
    private Numbers numbers;
    private final int size;
    private Dice dice;
    private Player player;
    private Level level;

    public A2048(Level level, Dice dice) {
        this.level = level;
        this.dice = dice;
        size = level.size();
        numbers = new Numbers(level.getNumbers(), level.size(), getBreak(level.getMode()));
        generator = Factory.get(level.getNewAdd(), dice);
    }

    private List<Number> getBreak(Mode mode) {
        LinkedList<Number> result = new LinkedList<Number>();
        if (mode == Mode.CLASSIC) return result;

        if (size < 5) return result;

        if (size == 5) {
            result.add(new Number(Numbers.BREAK, pt(0, 2)));
            result.add(new Number(Numbers.BREAK, pt(2, 0)));
            result.add(new Number(Numbers.BREAK, pt(4, 2)));
            result.add(new Number(Numbers.BREAK, pt(2, 4)));
        }

        if (size == 6) {
            result.add(new Number(Numbers.BREAK, pt(0, 2)));
            result.add(new Number(Numbers.BREAK, pt(0, 3)));
            result.add(new Number(Numbers.BREAK, pt(2, 0)));
            result.add(new Number(Numbers.BREAK, pt(3, 0)));
            result.add(new Number(Numbers.BREAK, pt(5, 2)));
            result.add(new Number(Numbers.BREAK, pt(5, 3)));
            result.add(new Number(Numbers.BREAK, pt(2, 5)));
            result.add(new Number(Numbers.BREAK, pt(3, 5)));
        }

        if (size == 7) {
            result.add(new Number(Numbers.BREAK, pt(0, 2)));
            result.add(new Number(Numbers.BREAK, pt(0, 3)));
            result.add(new Number(Numbers.BREAK, pt(1, 3)));
            result.add(new Number(Numbers.BREAK, pt(0, 4)));

            result.add(new Number(Numbers.BREAK, pt(2, 0)));
            result.add(new Number(Numbers.BREAK, pt(3, 0)));
            result.add(new Number(Numbers.BREAK, pt(3, 1)));
            result.add(new Number(Numbers.BREAK, pt(4, 0)));

            result.add(new Number(Numbers.BREAK, pt(6, 2)));
            result.add(new Number(Numbers.BREAK, pt(6, 3)));
            result.add(new Number(Numbers.BREAK, pt(5, 3)));
            result.add(new Number(Numbers.BREAK, pt(6, 4)));

            result.add(new Number(Numbers.BREAK, pt(2, 6)));
            result.add(new Number(Numbers.BREAK, pt(3, 6)));
            result.add(new Number(Numbers.BREAK, pt(3, 5)));
            result.add(new Number(Numbers.BREAK, pt(4, 6)));
        }

        if (size == 8) {
            result.add(new Number(Numbers.BREAK, pt(0, 3)));
            result.add(new Number(Numbers.BREAK, pt(0, 4)));
            result.add(new Number(Numbers.BREAK, pt(1, 3)));
            result.add(new Number(Numbers.BREAK, pt(1, 4)));

            result.add(new Number(Numbers.BREAK, pt(3, 0)));
            result.add(new Number(Numbers.BREAK, pt(4, 0)));
            result.add(new Number(Numbers.BREAK, pt(3, 1)));
            result.add(new Number(Numbers.BREAK, pt(4, 1)));

            result.add(new Number(Numbers.BREAK, pt(7, 3)));
            result.add(new Number(Numbers.BREAK, pt(7, 4)));
            result.add(new Number(Numbers.BREAK, pt(6, 3)));
            result.add(new Number(Numbers.BREAK, pt(6, 4)));

            result.add(new Number(Numbers.BREAK, pt(3, 7)));
            result.add(new Number(Numbers.BREAK, pt(4, 7)));
            result.add(new Number(Numbers.BREAK, pt(3, 6)));
            result.add(new Number(Numbers.BREAK, pt(4, 6)));
        }

        return result;
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
        generator.generate(numbers);
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
        return new BoardReader() {
            @Override
            public int size() {
                return A2048.this.size;
            }

            @Override
            public Iterable<? extends Point> elements() {
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
}
