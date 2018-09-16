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

import java.util.*;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static com.codenjoy.dojo.services.QDirection.*;
import static java.util.stream.Collectors.toList;

public class Quadro implements Field {

    static final int TIMEOUT_TICKS = 15;
    private static final int CHIPS_LENGTH_TO_WIN = 4;

    private ChipSet chips;
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
        if (!isGameStarted()) return;

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
                missedActs = 0; // TODO test me
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
        return players.size() == MultiplayerType.TOURNAMENT.getRoomSize();
    }

    @Override
    public boolean isMyTurn(Hero hero) {
        return hero == currentHero();
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

            Chip chip = new Chip(color, x, y);
            chips.put(chip, chip);

        chipMoved = true;
        checkWin(chip);
    }

    private void checkWin(Chip from) {
        if (getCount(DOWN, from) >= CHIPS_LENGTH_TO_WIN
                || getCount(RIGHT, from) >= CHIPS_LENGTH_TO_WIN
                || getCount(LEFT_DOWN, from) >= CHIPS_LENGTH_TO_WIN
                || getCount(RIGHT_DOWN, from) >= CHIPS_LENGTH_TO_WIN)
            win(from.getColor());
    }

    private int getCount(QDirection direction, Chip from) {
        return getCountHalf(direction, from)
                + getCountHalf(direction.inverted(), from)
                + 1;
    }

    private int getCountHalf(QDirection direction, Chip from) {
        int result = 0;
        Point current = from;
        for (int length = 0; length < CHIPS_LENGTH_TO_WIN - 1; length++) {
            current = direction.change(current);
            Chip currentChip = chip(current);
            if (currentChip != null
                    && currentChip.itsMyColor(from.getColor())) {
                result++;
            }
            else break;
        }
        return result;
    }

    private Chip chip(Point pt) {
        return chips.get(pt);
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

    List<Hero> getHeroes() {
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
                return new ArrayList<>(chips.chips());
            }
        };
    }
}
