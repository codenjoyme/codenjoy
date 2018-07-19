package com.codenjoy.dojo.quadro.model;

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


import com.codenjoy.dojo.quadro.model.items.Chip;
import com.codenjoy.dojo.quadro.services.Events;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.QDirection;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.printer.BoardReader;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static com.codenjoy.dojo.services.QDirection.*;
import static java.util.stream.Collectors.toList;

public class Quadro implements Field {

    public static final int TIMEOUT_TICKS = 15;
    public static final int CHIPS_LENGTH_TO_WIN = 4;

    private List<Chip> chips;
    private List<Player> players;
    private final int size;
    private boolean yellowPlayerAct = true;
    private boolean chipMoved;
    private int missedActs = 0;
    private int gameOver = 0;
    private Dice dice;

    public Quadro(Level level, Dice dice) {
        this.dice = dice;
        size = level.getSize();
        players = new LinkedList<>();
        chips = level.getChips();
    }

    @Override
    public void tick() {
        if (gameOver > 0) {
            if (++gameOver > TIMEOUT_TICKS) {
                chips.clear();
                gameOver = 0;
            } else {
                return;
            }
        }

        chipMoved = false;
        for (Player player : players) {
            Hero hero = player.getHero();
            hero.tick();
        }

        if (chipMoved) {
            yellowPlayerAct = !yellowPlayerAct;
            missedActs = 0;
        } else {
            if (++missedActs > 9) {
                win(!yellowPlayerAct);
            }
        }

        if (chips.size() == size * size) {
            draw();
        }
    }

    public int getSize() {
        return size;
    }

    @Override
    public boolean isGameStarted() {
        return players.size() == MultiplayerType.TOURNAMENT.getCount();
    }

    @Override
    public boolean isMyTurn(Hero hero) {
        return hero.equals(currentHero());
    }

    @Override
    public void setChip(boolean color, int x) {
        int y = 0;

        while (chips.contains(pt(x, y))) {
            y++;
        }

        if (y >= size) {
            return;
        }

        Point pt = pt(x, y);
        if (!chips.contains(pt)) {
            chips.add(new Chip(color, x, y));
        }

        chipMoved = true;
        checkWin(pt, color);
    }

    private void checkWin(Point pt, boolean color) {
        int verticalCounter = 1;
        for (int length = 1; length < CHIPS_LENGTH_TO_WIN; length++) {
            if (to(DOWN, pt, color, length)) {
                verticalCounter++;
            }
        }

        int horizontalCounter = 1;
        for (int length = 1; length < CHIPS_LENGTH_TO_WIN; length++) {
            if (to(RIGHT, pt, color, length)) {
                horizontalCounter++;
            }
        }

        for (int length = 1; length < CHIPS_LENGTH_TO_WIN; length++) {
            if (to(LEFT, pt, color, length)) {
                horizontalCounter++;
            }
        }

        int diagonal1Counter = 1;
        for (int length = 1; length < CHIPS_LENGTH_TO_WIN; length++) {
            if (to(LEFT_DOWN, pt, color, length)) {
                diagonal1Counter++;
            }
        }

        for (int length = 1; length < CHIPS_LENGTH_TO_WIN; length++) {
            if (to(RIGHT_UP, pt, color, length)) {
                diagonal1Counter++; // TODO не хватает кейза на этот случай если поменять diagonal1Counter и следующий  diagonal2Counter местами
            }
        }

        int diagonal2Counter = 1;
        for (int length = 1; length < CHIPS_LENGTH_TO_WIN; length++) {
            if (to(RIGHT_DOWN, pt, color, length)) {
                diagonal2Counter++;
            }
        }

        for (int length = 1; length < CHIPS_LENGTH_TO_WIN; length++) {
            if (to(LEFT_UP, pt, color, length)) {
                diagonal2Counter++;
            }
        }

        if (verticalCounter >= CHIPS_LENGTH_TO_WIN
                || horizontalCounter >= CHIPS_LENGTH_TO_WIN
                || diagonal1Counter >= CHIPS_LENGTH_TO_WIN
                || diagonal2Counter >= CHIPS_LENGTH_TO_WIN)
        {
            win(color);
        }
    }

    private boolean to(QDirection where, Point pt, boolean color, int length) {
        for (int i = 0; i < length; i++) {
            pt = where.change(pt);
        }
        return chip(pt).itsMyColor(color);
    }

    private Chip chip(Point pt) {
        return chips.stream()
                .filter(Predicate.isEqual(pt))
                .findFirst()
                .orElse(Chip.NULL);
    }

    private void draw() {
        gameOver = 1;
        players.get(0).event(Events.DRAW);
        players.get(1).event(Events.DRAW);
    }

    private void win(boolean color) {
        gameOver = 1;

        if (color) {
            players.get(0).event(Events.WIN);
            players.get(1).event(Events.LOOSE);
        } else {
            players.get(0).event(Events.LOOSE);
            players.get(1).event(Events.WIN);
        }
    }

    @Override
    public boolean getFreeColor() {
        return players.size() == 1
                || !players.get(0).getHero().getColor();
    }

    private Hero currentHero() {
        return yellowPlayerAct
                ? players.get(0).getHero()
                : players.get(1).getHero();
    }

    public List<Hero> getHeroes() {
        return players.stream()
                .map(Player::getHero)
                .collect(toList());
    }

    @Override
    public void newGame(Player player) {
        if (players.size() == 2) {
            throw new IllegalStateException("Too many players: "
                    + players.size());
        }

        if (!players.contains(player)) {
            players.add(player);
        }
        player.newHero(this);
    }

    @Override
    public void remove(Player player) {
        players.remove(player);
    }

    @Override
    public BoardReader reader() {
        return new BoardReader() {
            private int size = Quadro.this.size;

            @Override
            public int size() {
                return size;
            }

            @Override
            public Iterable<? extends Point> elements() {
                return new ArrayList<>(chips);
            }
        };
    }
}
