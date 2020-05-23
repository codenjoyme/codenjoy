package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.services.round.RoundSettingsWrapper;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.SimpleParameter;
import org.junit.Test;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

public class RoundBattleSingleTest extends AbstractSingleTest {

    private int minTicksForWin = 1;
    private int timePerRound = 10;
    private int timeForWinner = 2;
    private int timeBeforeStart = 5;
    private int roundsPerMatch = 3;

    @Override
    protected RoundSettingsWrapper getRoundSettings() {
        return new RoundSettingsWrapper() {

            @Override
            public Parameter<Integer> timeBeforeStart() {
                return v(timeBeforeStart);
            }

            @Override
            public Parameter<Integer> roundsPerMatch() {
                return v(roundsPerMatch);
            }

            @Override
            public Parameter<Integer> minTicksForWin() {
                return v(minTicksForWin);
            }

            @Override
            public Parameter<Integer> timePerRound() {
                return v(timePerRound);
            }

            @Override
            public Parameter<Integer> timeForWinner() {
                return v(timeForWinner);
            }

            @Override
            public Parameter<Boolean> roundsEnabled() {
                return new SimpleParameter<>(true);
            }
        };
    }

    // во время старта игры, когда не прошло timeBeforeStart тиков,
    // все игроки неактивны (видно их трупики)
    @Test
    public void shouldAllPlayersOnBoardIsInactive_whenStart() {
        playersPerRoom.update(3);

        givenBoard(3);

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ♣   \n" +
                "Ѡ♣   \n", game(0));

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ♣   \n" +
                "♣Ѡ   \n", game(1));

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " Ѡ   \n" +
                "♣♣   \n", game(2));
    }

    @Test
    public void shouldCountdownBeforeRound_whenTicksOnStart() {
        shouldAllPlayersOnBoardIsInactive_whenStart();

        verifyEvents(listener(0), "[]");
        verifyEvents(listener(1), "[]");
        verifyEvents(listener(2), "[]");

        tick();

        verifyEvents(listener(0), "[[....4....]]");
        verifyEvents(listener(1), "[[....4....]]");
        verifyEvents(listener(2), "[[....4....]]");

        tick();

        verifyEvents(listener(0), "[[...3...]]");
        verifyEvents(listener(1), "[[...3...]]");
        verifyEvents(listener(2), "[[...3...]]");

        tick();

        verifyEvents(listener(0), "[[..2..]]");
        verifyEvents(listener(1), "[[..2..]]");
        verifyEvents(listener(2), "[[..2..]]");

        tick();

        verifyEvents(listener(0), "[[.1.]]");
        verifyEvents(listener(1), "[[.1.]]");
        verifyEvents(listener(2), "[[.1.]]");
    }

    @Test
    public void shouldActiveAndCanMove_afterCountdown() {
        shouldCountdownBeforeRound_whenTicksOnStart();

        // пока еще не активны
        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ♣   \n" +
                "Ѡ♣   \n", game(0));

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ♣   \n" +
                "♣Ѡ   \n", game(1));

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " Ѡ   \n" +
                "♣♣   \n", game(2));

        // и я не могу ничего поделать с ними
        hero(0).up();
        hero(1).right();
        hero(2).up();

        tick();

        // после сообщения что раунд начался
        verifyEvents(listener(0), "[START_ROUND, [Round 1]]");
        verifyEvents(listener(1), "[START_ROUND, [Round 1]]");
        verifyEvents(listener(2), "[START_ROUND, [Round 1]]");

        // можно играть - игроки видны как активные
        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ♥   \n" +
                "☺♥   \n", game(0));

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ♥   \n" +
                "♥☺   \n", game(1));

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ☺   \n" +
                "♥♥   \n", game(2));

        // ... и когда я муваю героев, они откликаются
        hero(0).up();
        hero(1).right();
        hero(2).up();

        tick();

        asrtBrd("     \n" +
                "     \n" +
                " ♥   \n" +
                "☺    \n" +
                "  ♥  \n", game(0));

        asrtBrd("     \n" +
                "     \n" +
                " ♥   \n" +
                "♥    \n" +
                "  ☺  \n", game(1));

        asrtBrd("     \n" +
                "     \n" +
                " ☺   \n" +
                "♥    \n" +
                "  ♥  \n", game(2));
    }

}
