package com.codenjoy.dojo.services.round;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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

import com.codenjoy.dojo.services.Tickable;

import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public abstract class RoundField<P extends RoundGamePlayer<? extends RoundPlayerHero, ? extends RoundGameField>> implements RoundGameField<P>, Tickable {

    private Round round;
    private List<P> inactive;

    private Object startRoundEvent;
    private Object looseEvent;

    public RoundField(Round round, Object startRoundEvent, Object winEvent, Object looseEvent) {
        this.round = round;
        round.init(this, winEvent);

        this.startRoundEvent = startRoundEvent;
        this.looseEvent = looseEvent;

        inactive = new LinkedList<>();
    }

    protected abstract List<P> players();

    protected abstract void tickField();

    protected abstract void cleanStuff();

    protected abstract void setNewObjects();

    @Override
    public void tick() {
        cleanStuff();

        boolean skip = round.tick();
        if (skip) {
            return;
        }

        tickField();

        rewardTheWinnerIfNeeded(this::setNewObjects);
    }

    @Override
    public List<P> aliveActive() {
        return players().stream()
                .filter(p -> p.isAlive() && p.isActive())
                .collect(toList());
    }

    @Override
    public void start(int round) {
        players().forEach(p -> p.start(round, startRoundEvent));
    }

    @Override
    public void print(String message) {
        players().forEach(player -> player.printMessage(message));
    }

    @Override
    public int score(P player) {
        return player.getHero().scores();
    }

    @Override
    public void oneMoreDead(P player) {
        if (round instanceof NullRound) {
            player.die(false, looseEvent);
        } else {
            player.die(round.isMatchOver(), looseEvent);
            inactive.add(player);
        }
    }

    @Override
    public void reset(P player) {
        if (round.isMatchOver()) {
            player.getHero().setAlive(false);
            player.leaveBoard();
        } else {
            newGame(player);
        }
    }

    public void rewardTheWinnerIfNeeded(Runnable runnable) {
        if (inactive.isEmpty()) {
            return;
        }

        inactive.clear();
        round.rewardTheWinner();
        runnable.run();
    }

    @Override
    public void newGame(P player) {
        if (inactive.contains(player)) {
            inactive.remove(player);
        }
    }

    @Override
    public void clearScore() {
        round.clear();
        inactive.clear();

        players().forEach(p -> newGame(p));
    }

    public void remove(P player) {
        if (players().contains(player)) {
            players().remove(player);

            // кто уходит из игры не лишает коллег очков за победу
            // но только если он был жив к этому моменту
            if (player.getHero().isActiveAndAlive()) {
                player.getHero().die();
                rewardTheWinnerIfNeeded(() -> {});
            }
        }
    }

}
