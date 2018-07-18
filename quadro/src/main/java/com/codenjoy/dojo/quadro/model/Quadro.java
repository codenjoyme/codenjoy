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
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.printer.BoardReader;

import java.util.*;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static java.util.stream.Collectors.toList;

/**
 * О! Это самое сердце игры - борда, на которой все происходит.
 * Если какой-то из жителей борды вдруг захочет узнать что-то у нее, то лучше ему дать интефейс {@see Field}
 * Борда реализует интерфейс {@see Tickable} чтобы быть уведомленной о каждом тике игры. Обрати внимание на {Quadro#tick()}
 */
public class Quadro implements Field {

    private Map<Point, Chip> chips;
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
            if (++gameOver > 15) {
                chips.clear();
                gameOver = 0;
            } else return;
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
            if (++missedActs > 9)
                win(!yellowPlayerAct);
        }

        if (chips.size() == size * size)
            draw();

//        for (Player player : players) {
//            Hero hero = player.getHero();
//            if (!hero.isAlive()) {
//                player.event(Events.LOOSE);
//            }
//        }
    }

    public int getSize() {
        return size;
    }

    @Override
    public boolean isGameStarted() {
        return players.size() == MultiplayerType.TOURNAMENT.getCount();
    }

    @Override
    public boolean isFree(int x, int y) {
        Point pt = pt(x, y);

        return !chips.containsKey(pt);
    }

    @Override
    public void setChip(boolean color, int x) {
        int y = 0;

        while (chips.containsKey(pt(x, y))) {
            y++;
        }

        if (y >= size) return;

        Point pt = pt(x, y);
        if (!chips.containsKey(pt)) {
            chips.put(pt, new Chip(color, x, y));
        }

        chipMoved = true;
        checkWin(pt, color);
    }

    private void checkWin(Point pt, boolean color) {
        int verticalCounter = 1,
                horizontalCounter = 1,
                diagonal1Counter = 1, // ⭧⭩
                diagonal2Counter = 1; // ⭦⭨
        boolean directionTopToDownActive = true,
                directionLeftToRightActive = true,
                directionRightToLeftActive = true,
                directionTopRightToBottomLeftActive = true,
                directionBottomLeftToTopRightActive = true,
                directionTopLeftToBottomRightActive = true,
                directionBottomRightToTopLeftActive = true;

        for (int i = 1; i < 4; i++) {
            if (chips.get(pt(pt.getX(), pt.getY() - i)) == null
                    || chips.get(pt(pt.getX(), pt.getY() - i)).getColor() != color)
                directionTopToDownActive = false;
            if (chips.get(pt(pt.getX() + i, pt.getY())) == null
                    || chips.get(pt(pt.getX() + i, pt.getY())).getColor() != color)
                directionLeftToRightActive = false;
            if (chips.get(pt(pt.getX() - i, pt.getY())) == null
                    || chips.get(pt(pt.getX() - i, pt.getY())).getColor() != color)
                directionRightToLeftActive = false;
            if (chips.get(pt(pt.getX() - i, pt.getY() - i)) == null
                    || chips.get(pt(pt.getX() - i, pt.getY() - i)).getColor() != color)
                directionTopRightToBottomLeftActive = false;
            if (chips.get(pt(pt.getX() + i, pt.getY() + i)) == null
                    || chips.get(pt(pt.getX() + i, pt.getY() + i)).getColor() != color)
                directionBottomLeftToTopRightActive = false;
            if (chips.get(pt(pt.getX() + i, pt.getY() - i)) == null
                    || chips.get(pt(pt.getX() + i, pt.getY() - i)).getColor() != color)
                directionTopLeftToBottomRightActive = false;
            if (chips.get(pt(pt.getX() - i, pt.getY() + i)) == null
                    || chips.get(pt(pt.getX() - i, pt.getY() + i)).getColor() != color)
                directionBottomRightToTopLeftActive = false;

            if (directionTopToDownActive) verticalCounter++;
            if (directionLeftToRightActive) horizontalCounter++;
            if (directionRightToLeftActive) horizontalCounter++;
            if (directionTopRightToBottomLeftActive) diagonal1Counter++;
            if (directionBottomLeftToTopRightActive) diagonal1Counter++;
            if (directionTopLeftToBottomRightActive) diagonal2Counter++;
            if (directionBottomRightToTopLeftActive) diagonal2Counter++;
        }

        if (verticalCounter >= 4
                || horizontalCounter >= 4
                || diagonal1Counter >= 4
                || diagonal2Counter >= 4)
            win(color);
    }

    private void draw() {
        gameOver = 1;
        players.get(0).event(Events.DRAW);
        players.get(1).event(Events.DRAW);
    }

    private void win(boolean color) {
        gameOver = 1;
        (color ? players.get(0) : players.get(1)).event(Events.WIN);
        (!color ? players.get(0) : players.get(1)).event(Events.LOOSE);
    }

    @Override
    public boolean getFreeColor() {
        return players.size() == 1 || !players.get(0).getHero().getColor();
    }

    @Override
    public Hero currentPlayer() {
        return yellowPlayerAct ? players.get(0).getHero() : players.get(1).getHero();
    }

    public List<Hero> getHeroes() {
        return players.stream()
                .map(Player::getHero)
                .collect(toList());
    }

    @Override
    public void newGame(Player player) {
        if (players.size() == 2)
            throw new IllegalStateException("Too many players: " + players.size());

        if (!players.contains(player))
            players.add(player);
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
                return new ArrayList<>(chips.values());
            }
        };
    }
}
