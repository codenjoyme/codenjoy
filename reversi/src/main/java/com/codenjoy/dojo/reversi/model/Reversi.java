package com.codenjoy.dojo.reversi.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import com.codenjoy.dojo.reversi.model.items.Chip;
import com.codenjoy.dojo.reversi.services.Events;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.QDirection;
import com.codenjoy.dojo.services.printer.BoardReader;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static com.codenjoy.dojo.services.QDirection.*;
import static java.util.stream.Collectors.toList;

public class Reversi implements Field {

    private List<Chip> chips;
    private List<Player> players;
    private final int size;
    private Dice dice;
    private boolean currentColor;

    public Reversi(Level level, Dice dice) {
        this.dice = dice;
        size = level.size();
        players = new LinkedList<>();
        chips = level.chips(this);
        currentColor = level.currentColor();
    }

    @Override
    public void tick() {
        for (Player player : players) {
            Hero hero = player.getHero();

            hero.tick();
        }

        if (chips.size() == size*size){
            long countWhite = chips.stream().filter(Chip::isWhite).count();
            long countBlack = chips.stream().filter(Chip::isBlack).count();
            if (countWhite == countBlack) {
                // TODO
                // whitePlayer().event(Events.WIN);
                // blackPlayer().event(Events.WIN);
            } else if (countBlack < countWhite) {
                whitePlayer().event(Events.WIN);
                blackPlayer().event(Events.LOOSE);
            } else if (countBlack > countWhite) {
                // TODO
                // whitePlayer().event(Events.LOOSE);
                // blackPlayer().event(Events.WIN);
            }
        }

        currentColor = !currentColor;
    }

    private Player whitePlayer() {
        return players.stream().filter(Player::isWhite).findFirst().get();
    }

    private Player blackPlayer() {
        return players.stream().filter(Player::isBlack).findFirst().get();
    }

    public int size() {
        return size;
    }

    @Override
    public boolean getFreeColor() {
        return players.size() == 1;
    }

    @Override
    public boolean currentColor() {
        return currentColor;
    }

    @Override
    public boolean isFree(int x, int y) {
        Point pt = pt(x, y);

        return !chips.contains(pt);
    }

    @Override
    public void setChip(boolean color, int x, int y) {
        Point pt = pt(x, y);
        if (!chips.contains(pt)) {
            Chip chip = new Chip(color, pt, this);
            if (flipFromChip(chip)){
                chips.add(chip);
            }
        }
    }

    private boolean flipFromChip(Chip current) {
        boolean result = false;
        for (QDirection direction : directions()){
            result |= current.flip(direction);
        }
        return result;
    }

    private List<QDirection> directions() {
        return Arrays.asList(LEFT, LEFT_UP, UP, RIGHT_UP,
            RIGHT, RIGHT_DOWN, DOWN, LEFT_DOWN);
    }

    public Chip getChip(Point chip) {
        return chips.stream()
                .filter(Predicate.isEqual(chip))
                .findFirst()
                .orElse(Chip.NULL);
    }

    public List<Hero> getHeroes() {
        return players.stream()
                .map(Player::getHero)
                .collect(toList());
    }

    @Override
    public void newGame(Player player) {
        if (!players.contains(player)) {
            players.add(player);
        }
        player.newHero(this);
    }

    @Override
    public void remove(Player player) {
        players.remove(player);
    }

    public List<Chip> getChips() {
        return chips;
    }

    @Override
    public BoardReader reader() {
        return new BoardReader() {
            private int size = Reversi.this.size;

            @Override
            public int size() {
                return size;
            }

            @Override
            public Iterable<? extends Point> elements() {
                return Reversi.this.getChips();
            }
        };
    }
}
