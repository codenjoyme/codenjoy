package com.codenjoy.dojo.snakebattle.model.board.round;

import com.codenjoy.dojo.services.Tickable;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;
import com.codenjoy.dojo.services.settings.Parameter;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;
import java.util.List;

public class Round {

    private RoundField<GamePlayer<PlayerHero, GameField>> field;

    private Parameter<Integer> roundsPerMatch;
    private Parameter<Integer> minTicksForWin;

    private Timer startTimer;
    private Timer roundTimer;
    private Timer winnerTimer;

    private int round;

    private Object winEvent;

    public Round(Parameter<Integer> roundsPerMatch, Parameter<Integer> minTicksForWin,
                 Parameter<Integer> timeBeforeStart, Parameter<Integer> timePerRound,
                 Parameter<Integer> timeForWinner)
    {
        this.roundsPerMatch = roundsPerMatch;
        this.minTicksForWin = minTicksForWin;

        round = 0;

        startTimer = new Timer(timeBeforeStart);
        roundTimer = new Timer(timePerRound);
        winnerTimer = new Timer(timeForWinner);

        startTimer = startTimer.start();
        roundTimer = roundTimer.stop();
        winnerTimer = winnerTimer.stop();
    }

    public void init(RoundField<GamePlayer<PlayerHero, GameField>> field, Object winEvent) {
        this.field = field;
        this.winEvent = winEvent;
    }

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

    private GamePlayer<PlayerHero, GameField> getLast() {
        return field.aliveActive().iterator().next();
    }

    private void rewardWinnersByTimeout() {
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

    public void rewardTheWinner() {
        if (isLastOnBoard()) {
            if (roundTimer.time() >= minTicksForWin.getValue()) {
                getLast().event(winEvent);
            }
        }
    }

    public boolean isMatchOver() {
        // тут >= а не == потому что на админке можно поменять roundsPerMatch
        // в меньшую сторону и тут можно зациклится в противном случае
        return round >= roundsPerMatch.getValue();
    }

    public void clear() {
        round = 0;
    }

}
