package com.codenjoy.dojo.reversi.model;

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


import com.codenjoy.dojo.reversi.model.items.Break;
import com.codenjoy.dojo.reversi.model.items.Chip;
import com.codenjoy.dojo.reversi.services.Events;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.printer.BoardReader;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static java.util.stream.Collectors.toList;

public class Reversi implements Field {

    private Flipper flipper;
    private List<Chip> chips;
    private List<Break> breaks;
    private List<Player> players;
    private final int size;
    private Level level;
    private Dice dice;
    private boolean current;

    public Reversi(Level level, Dice dice) {
        flipper = new Flipper(this);
        this.level = level;
        this.dice = dice;
        size = level.size();
        players = new LinkedList<>();
        resetField(level);
        whoFirst();
    }

    private void whoFirst() {
        if (flipper.cantFlip(current)) {
            nextTurn();
            if (flipper.cantFlip(current) && flipper.cantFlip(!current)) {
                nextTurn();
            }
        }
    }

    private void resetField(Level level) {
        chips = level.chips(this);
        breaks = level.breaks(this);
        current = level.currentColor();
        if (isGameOver()) {
            throw new IllegalArgumentException("Изначально патовая ситуация");
        }
    }

    @Override
    public void tick() {
        if (isGameOver()) {
            resetField(level);
            return;
        }

        getHeroes().forEach(Hero::tick);

        if (stop()) {
            return;
        }

        if (isGameOver()) {
            whoWin();
        }
        nextTurn();
    }

    private boolean isGameOver() {
        long countWhite = chips(true).size();
        long countBlack = chips(false).size();
        return isCompletelyFilled()
                || flipper.cantFlip(true) && flipper.cantFlip(false)
                || countBlack == 0
                || countWhite == 0;
    }

    private void nextTurn() {
        current = !current;
        if (flipper.cantFlip(current) && !flipper.cantFlip(!current)) {
            current = !current;
        }
    }

    private List<Chip> chips(boolean color) {
        return chips.stream()
                .filter(chip -> chip.color() == color)
                .collect(toList());
    }

    @Override
    public List<Point> freeSpaces() {
        List<Point> result = new LinkedList<>();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Point pt = pt(x, y);
                if (chip(pt) == Chip.NULL
                        && !isBreak(pt.getX(), pt.getY()))
                {
                    result.add(pt);
                }
            }
        }
        return result;
    }

    private boolean isCompletelyFilled() {
        return chips.size() == size*size;
    }

    private void whoWin() {
        long countWhite = chips(true).size();
        long countBlack = chips(false).size();

        if (countWhite == countBlack) {
            whitePlayer().event(Events.WIN());
            blackPlayer().event(Events.WIN());
        } else if (countBlack < countWhite) {
            whitePlayer().event(Events.WIN());
            blackPlayer().event(Events.LOOSE());
        } else if (countBlack > countWhite) {
            whitePlayer().event(Events.LOOSE());
            blackPlayer().event(Events.WIN());
        }
    }

    private Player whitePlayer() {
        return player(true);
    }

    private Player blackPlayer() {
        return player(false);
    }

    private Player player(boolean color) {
        return players.stream().filter(player -> player.color() == color).findFirst().get();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean freeColor() {
        if (players.isEmpty()) {
            return true;
        }

        if (players.size() == 1) {
            Hero hero = players.get(0).hero;
            if (hero == null) {
                return true;
            } else {
                return !hero.color();
            }
        }

        if (players.size() == 2) {
            Hero hero1 = players.get(0).hero;
            Hero hero2 = players.get(1).hero;
            if (hero1 == null && hero2 == null) {
                return true;
            } else if (hero1 == null) {
                 return !hero2.color();
            } else if (hero2 == null) {
                return !hero1.color();
            } else{
                throw new IllegalArgumentException("Все цвета заняты!");
            }
        }

        throw new IllegalArgumentException("На поле больше двух игроков: " + players.size());
    }

    @Override
    public boolean currentColor() {
        return current;
    }

    @Override
    public boolean stop() {
        return players.size() != MultiplayerType.TOURNAMENT.getRoomSize();
    }

    @Override
    public boolean isBreak(int x, int y) {
        return breaks.contains(pt(x, y));
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
            int count = flipper.flip(chip);
            if (count > 0){
                player(color).event(Events.FLIP(count));
                chips.add(chip);
            }
        }
    }

    @Override
    public Chip chip(Point chip) {
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
        if (players.size() == 2) {
            throw new IllegalArgumentException("Нельзя добавить игрока - поле занято!");
        }

        if (!players.contains(player)) {
            players.add(player);
        }
        player.newHero(this);
    }

    @Override
    public void remove(Player player) {
        players.remove(player);
        player.hero = null;
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
                return new LinkedList<Point>(){{
                    addAll(Reversi.this.chips);
                    addAll(Reversi.this.breaks);
                }};
            }
        };
    }
}
