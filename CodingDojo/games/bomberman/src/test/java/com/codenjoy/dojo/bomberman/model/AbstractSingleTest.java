package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.bomberman.services.Events;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.multiplayer.Single;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.services.round.RoundSettingsWrapper;
import com.codenjoy.dojo.services.settings.Parameter;
import org.mockito.ArgumentCaptor;
import org.mockito.exceptions.verification.NeverWantedButInvoked;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public abstract class AbstractSingleTest {

    public static final int SIZE = 5;
    protected Walls walls;
    protected List<Hero> heroes = new LinkedList<>();
    protected List<Game> games = new LinkedList<>();
    private List<EventListener> listeners = new LinkedList<>();
    private List<Player> players = new LinkedList<>();
    protected GameSettings settings;
    protected Level level;
    protected Bomberman board;
    protected int bombsCount = 1;
    protected Parameter<Integer> playersPerRoom = v(Integer.MAX_VALUE);
    protected Dice meatDice = mock(Dice.class);
    protected Dice heroDice = mock(Dice.class);
    private PrinterFactory printerFactory = new PrinterFactoryImpl();

    {
        givenWalls();
    }

    public void givenBoard(int count) {
        settings = mock(GameSettings.class);

        level = mock(Level.class);
        when(level.bombsCount()).thenReturn(bombsCount);
        when(level.bombsPower()).thenReturn(1);

        when(settings.getBomberman(any(Level.class))).thenAnswer(inv -> {
            Hero hero = new Hero(level, heroDice);
            heroes.add(hero);
            return hero;
        });

        when(settings.getLevel()).thenReturn(level);
        when(settings.getBoardSize()).thenReturn(v(SIZE));
        when(settings.getWalls(any(Bomberman.class))).thenReturn(walls);
        when(settings.getRoundSettings()).thenReturn(getRoundSettings());
        when(settings.getPlayersPerRoom()).thenReturn(playersPerRoom);
        when(settings.killOtherHeroScore()).thenReturn(v(200));
        when(settings.killMeatChopperScore()).thenReturn(v(100));
        when(settings.killWallScore()).thenReturn(v(10));

        board = new Bomberman(settings);

        for (int i = 0; i < count; i++) {
            listeners.add(mock(EventListener.class));
            players.add(new Player(listener(i), getRoundSettings().roundsEnabled()));
            games.add(new Single(player(i), printerFactory));
        }

        games.forEach(g -> {
            g.on(board);
            g.newGame();
        });
    }

    protected void meatChopperAt(int x, int y) {
        dice(meatDice, x, y);
        Field temp = mock(Field.class);
        when(temp.size()).thenReturn(SIZE);
        MeatChoppers meatchoppers = new MeatChoppers(new WallsImpl(), temp, v(1), meatDice);
        meatchoppers.regenerate();
        walls = meatchoppers;
    }

    protected void asrtBrd(String board, Game game) {
        assertEquals(board, game.getBoardAsString());
    }

    protected void verifyEvents(EventListener events, String expected) {
        if (expected.equals("[]")) {
            try {
                verify(events, never()).event(any(Events.class));
            } catch (NeverWantedButInvoked e) {
                assertEquals(expected, getEvents(events));
            }
        } else {
            assertEquals(expected, getEvents(events));
        }
        reset(events);
    }

    protected String getEvents(EventListener events) {
        try {
            ArgumentCaptor<Events> captor = ArgumentCaptor.forClass(Events.class);
            verify(events, atLeast(1)).event(captor.capture());
            return captor.getAllValues().toString();
        } catch (WantedButNotInvoked e) {
            return "[]";
        } finally {
            reset(events);
        }
    }

    protected Hero hero(int index) {
        return heroes.get(index);
    }

    protected Game game(int index) {
        return games.get(index);
    }

    protected Player player(int index) {
        return players.get(index);
    }

    protected EventListener listener(int index) {
        return listeners.get(index);
    }

    protected void tick() {
        board.tick();
    }

    protected abstract RoundSettingsWrapper getRoundSettings();

    protected void dice(Dice dice, int... values) {
        reset(dice);
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int value : values) {
            when = when.thenReturn(value);
        }
    }

    protected void givenWalls(Wall... input) {
        walls = new WallsImpl();
        Arrays.asList(input).forEach(walls::add);
    }

    protected void newGame(int index) {
        board.newGame(player(index));
        heroes.set(index, heroes.remove(heroes.size() - 1));
    }

    protected void assertBoards(String expected, Integer... indexes) {
        assertAll(expected, indexes, index -> {
            Object actual = game(index).getBoardAsString();
            return String.format("game(%s)\n%s\n", index, actual);
        });
    }

    private void assertAll(String expected, Integer[] indexes,
                           Function<Integer, String> function)
    {
        indexes = range(indexes);

        String actual = "";
        for (int i = 0; i < indexes.length; i++) {
            actual += function.apply(indexes[i]);
        }

        assertEquals(expected, actual);
    }

    private Integer[] range(Integer[] indexes) {
        if (indexes.length == 0) {
            indexes = IntStream.range(0, games.size())
                    .boxed()
                    .collect(toList()).toArray(new Integer[0]);
        }
        return indexes;
    }

    protected void verifyAllEvents(String expected, Integer... indexes) {
        assertAll(expected, indexes, index -> {
            Object actual = getEvents(listener(index));
            return String.format("listener(%s) => %s\n", index, actual);
        });
    }
}
