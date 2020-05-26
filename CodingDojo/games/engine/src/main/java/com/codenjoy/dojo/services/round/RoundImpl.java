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

import com.codenjoy.dojo.services.settings.Parameter;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;

public class RoundImpl implements Round {

    private RoundGameField<RoundGamePlayer<RoundPlayerHero, RoundGameField>> field;

    private Parameter<Integer> roundsPerMatch;
    private Parameter<Integer> minTicksForWin;
    private Parameter<Integer> timeBeforeStart;
    private Parameter<Integer> timePerRound;
    private Parameter<Integer> timeForWinner;

    private Timer startTimer;
    private Timer roundTimer;
    private Timer winnerTimer;

    private int round;

    private Object winEvent;

    public RoundImpl(Parameter<Integer> roundsPerMatch, Parameter<Integer> minTicksForWin,
                     Parameter<Integer> timeBeforeStart, Parameter<Integer> timePerRound,
                     Parameter<Integer> timeForWinner)
    {
        this.roundsPerMatch = roundsPerMatch;
        this.minTicksForWin = minTicksForWin;
        this.timeBeforeStart = timeBeforeStart;
        this.timePerRound = timePerRound;
        this.timeForWinner = timeForWinner;

        clear();
    }

    public RoundImpl(RoundSettingsWrapper settings) {
        this(settings.roundsPerMatch(), settings.minTicksForWin(),
                settings.timeBeforeStart(), settings.timePerRound(),
                settings.timeForWinner());
    }

    @Override
    public void init(RoundGameField field, Object winEvent) {
        this.field = field;
        this.winEvent = winEvent;
    }

    @Override
    public boolean tick() {
        startTimer.tick(this::sendTimerStatus);
        roundTimer.tick(() -> {});
        winnerTimer.tick(() -> {});

        if (startTimer.justFinished()) {
            round++;
            field.start(round);
            roundTimer.start();
        }

        if (!startTimer.done()) {
            return true;
        }

        if (roundTimer.justFinished()) {
            rewardWinnersByTimeout();

            startTimer.start();
            return true;
        }

        if (isNoOneOnBoard() || winnerTimer.justFinished()) {
            if (isLastOnBoard()) {
                field.reset(getLast());
            }

            startTimer.start();
            return true;
        }

        if (!startTimer.unlimited() && winnerTimer.done() && isLastOnBoard()) {
            winnerTimer.start();
        }

        return false;
    }

    private boolean isNoOneOnBoard() {
        return field.aliveActive().size() == 0;
    }

    private boolean isLastOnBoard() {
        return field.aliveActive().size() == 1;
    }

    private RoundGamePlayer<RoundPlayerHero, RoundGameField> getLast() {
        return field.aliveActive().iterator().next();
    }

    private void rewardWinnersByTimeout() {
        if (field.aliveActive().isEmpty()) {
            return;
        }

        Integer max = field.aliveActive().stream()
                .map(p -> field.score(p))
                .max(Comparator.comparingInt(i1 -> i1))
                .orElse(Integer.MAX_VALUE);

        field.aliveActive().forEach(p -> {
            if (field.score(p) == max
                    && roundTimer.time() > minTicksForWin.getValue())
            {
                p.event(winEvent);
            } else {
                p.printMessage("Time is over");
            }
        });

        field.aliveActive().forEach(player -> field.reset(player));
    }

    private void sendTimerStatus() {
        String pad = StringUtils.leftPad("", startTimer.countdown(), '.');
        String message = pad + startTimer.countdown() + pad;
        field.print(message);
    }

    @Override
    public void rewardTheWinner() {
        if (isLastOnBoard()) {
            if (roundTimer.time() >= minTicksForWin.getValue()) {
                getLast().event(winEvent);
            }
        }
    }

    @Override
    public boolean isMatchOver() {
        // тут >= а не == потому что на админке можно поменять roundsPerMatch
        // в меньшую сторону и тут можно зациклится в противном случае
        return round >= roundsPerMatch.getValue();
    }

    @Override
    public void clear() {
        round = 0;

        startTimer = new Timer(timeBeforeStart);
        roundTimer = new Timer(timePerRound);
        winnerTimer = new Timer(timeForWinner);

        startTimer = startTimer.start();
        roundTimer = roundTimer.stop();
        winnerTimer = winnerTimer.stop();
    }

}
