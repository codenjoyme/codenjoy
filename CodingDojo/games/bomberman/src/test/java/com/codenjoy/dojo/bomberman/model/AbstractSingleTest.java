package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.multiplayer.Single;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.services.round.RoundSettingsWrapper;
import org.mockito.stubbing.OngoingStubbing;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class AbstractSingleTest {

    public static final int SIZE = 5;
    protected Walls walls = emptyWalls();
    private List<Hero> heroes = new LinkedList<>();
    private List<Game> games = new LinkedList<>();
    private List<EventListener> listeners = new LinkedList<>();
    protected GameSettings settings;
    protected Level level;
    private Bomberman board;
    protected int bombsCount = 1;
    protected Dice meatDice;
    protected Dice heroDice;
    private PrinterFactory printerFactory = new PrinterFactoryImpl();

    public void givenBoard() {
        settings = mock(GameSettings.class);

        level = mock(Level.class);
        when(level.bombsCount()).thenReturn(bombsCount);
        when(level.bombsPower()).thenReturn(1);

        heroDice = mock(Dice.class);

        dice(heroDice,  0, 0);
        heroes.add(new Hero(level, heroDice));

        dice(heroDice,  0, 0);
        heroes.add(new Hero(level, heroDice));

        OngoingStubbing<Hero> when = when(settings.getBomberman(any(Level.class)));
        for (Hero h : heroes) {
            when = when.thenReturn(h);
        }

        when(settings.getLevel()).thenReturn(level);
        when(settings.getBoardSize()).thenReturn(v(SIZE));
        when(settings.getWalls(any(Bomberman.class))).thenReturn(walls);
        RoundSettingsWrapper roundSettings = getRoundSettings();
        when(settings.getRoundSettings()).thenReturn(roundSettings);
        when(settings.killOtherBombermanScore()).thenReturn(v(200));
        when(settings.killMeatChopperScore()).thenReturn(v(100));
        when(settings.killWallScore()).thenReturn(v(10));

        board = new Bomberman(settings);

        listeners.add(mock(EventListener.class));
        listeners.add(mock(EventListener.class));

        games.add(new Single(new Player(listener(0), roundSettings.roundsEnabled()), printerFactory));
        games.add(new Single(new Player(listener(1), roundSettings.roundsEnabled()), printerFactory));

        games.forEach(g -> {
            g.on(board);
            g.newGame();
        });
    }

    protected Hero hero(int index) {
        return heroes.get(index);
    }

    protected Game game(int index) {
        return games.get(index);
    }

    protected EventListener listener(int index) {
        return listeners.get(index);
    }

    protected void tick() {
        board.tick();
    }

    protected abstract RoundSettingsWrapper getRoundSettings();

    protected void dice(Dice dice, int... values) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int value : values) {
            when = when.thenReturn(value);
        }
    }

    private Walls emptyWalls() {
        Walls walls = mock(WallsImpl.class);
        when(walls.iterator()).thenReturn(new LinkedList<Wall>().iterator());
        return walls;
    }

}
