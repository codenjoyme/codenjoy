package com.codenjoy.dojo.expansion.model;

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


import com.codenjoy.dojo.expansion.model.levels.items.Hero;
import com.codenjoy.dojo.expansion.services.GameRunner;
import com.codenjoy.dojo.expansion.services.SettingsWrapper;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.multiplayer.Single;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.utils.TestUtils;
import org.json.JSONObject;
import org.junit.Before;
import org.mockito.stubbing.OngoingStubbing;

import java.util.LinkedList;
import java.util.function.BiConsumer;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Sanja on 15.02.14.
 */
public class AbstractGameRunnerTest {

    public static final int LEVEL1 = 0;
    public static final int LEVEL2 = 1;
    public static final int LEVEL3 = 2;
    public static final int LEVEL4 = 3;

    protected LinkedList<Game> games;
    private GameRunner gameRunner;
    private PrinterFactoryImpl factory;
    private Dice dice;
    private EventListener listener;
    private Settings settings;
    private Expansion current;

    @Before
    public void setup() {
        dice = mock(Dice.class);
        listener = mock(EventListener.class);

        gameRunner = new GameRunner();
        gameRunner.setDice(dice);

        games = new LinkedList<>();
        factory = new PrinterFactoryImpl();

        settings = gameRunner.getSettings();
        SettingsWrapper.data
                .waitingOthers(false)
                .singleTrainingMode(false)
                .shufflePlayers(false);
    }

    protected void givenFl(String... boards) {
        SettingsWrapper.multi(boards);
    }

    protected JSONObject board(int player) {
        return (JSONObject)game(player).getBoardAsString();
    }

    protected Game game(int player) {
        if (player >= games.size()) {
            fail("There is no PLAYER" + (player + 1));
        }
        Game game = games.get(player);
        if (game == null) {
            fail("You just destroyed PLAYER" + (player + 1));
        }
        return game;
    }

    protected void createNewGame(int room) {
        gotoFreeRoom(room);
        createNewGame();
    }

    protected int getRound(int player) {
        return ((JSONObject) game(player).getBoardAsString()).getInt("round");
    }

    protected void createNewGame() {
        if (current == null || current.freeBases() == 0) {
            current = (Expansion) gameRunner.createGame(0);
        }
        Player player = (Player) gameRunner.createPlayer(listener, "");
        Single game = new Single(player, gameRunner.getPrinterFactory());
        game.on(current);
        game.newGame();
        games.add(game);
    }

    protected void gotoFreeRoom(int... levelOfRoom) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int i : levelOfRoom) {
            when = when.thenReturn(i);
        }
    }

    protected void assertE(String expected, int index) {
        Single single = (Single)game(index);
        assertEquals(expected,
                TestUtils.injectN(getLayer(single, 1)));
    }

    protected void assertL(String expected, int index) {
        Single single = (Single)game(index);
        assertEquals(expected,
                TestUtils.injectN(getLayer(single, 0)));
    }

    protected void assertF(String expected, int index) {
        Single single = (Single)game(index);
        assertEquals(expected,
                TestUtils.injectNN(getLayer(single, 2)));
    }

    private String getLayer(Single single, int layer) {
        return ((JSONObject) single.getBoardAsString()).getJSONArray("layers").getString(layer);
    }

    protected void tickAll() {
        for (Game game : games) {
            if (game != null) {
                game.getField().tick();
            }
        }
        gameRunner.tick(); // this codenjoy server will do after all game ticks
    }

    protected void doit(int times, Runnable whatToDo) {
        for (int i = 0; i < times; i++) {
            whatToDo.run();
            tickAll();
        }
    }

    protected void destroy(int player) {
        Game game = games.get(player);
        game.getField().remove(game.getPlayer());
        games.set(player, null);
    }

    protected Joystick goTimes(int player, Point pt, int times) {
        Hero hero = (Hero) joystick(player);
        BiConsumer<Hero, QDirection> command =
                (h, d) -> {
                    // increase and go to
                    h.increaseAndMove(new Forces(pt, 1), new ForcesMoves(pt, 1, d));
                    // change point so next turn from new place
                    pt.change(d);
                };

        return new Joystick() {
            @Override
            public void down() {
                doit(times, () -> command.accept(hero, QDirection.DOWN));
            }

            @Override
            public void up() {
                doit(times, () -> command.accept(hero, QDirection.UP));
            }

            @Override
            public void left() {
                doit(times, () -> command.accept(hero, QDirection.LEFT));
            }

            @Override
            public void right() {
                doit(times, () -> command.accept(hero, QDirection.RIGHT));
            }

            @Override
            public void act(int... p) {
                // do nothing
            }

            @Override
            public void message(String command) {
                // do nothing
            }
        };
    }

    private Joystick joystick(int player) {
        return game(player).getJoystick();
    }

    protected void givenLevels() {
        givenFl("╔════┐" +
                "║1..2│" +
                "║....│" +
                "║....│" +
                "║4..3│" +
                "└────┘",
                "╔════┐" +
                "║..1.│" +
                "║4...│" +
                "║...2│" +
                "║.3..│" +
                "└────┘",
                "╔════┐" +
                "║.1..│" +
                "║...2│" +
                "║4...│" +
                "║..3.│" +
                "└────┘",
                "╔════┐" +
                "║....│" +
                "║.12.│" +
                "║.43.│" +
                "║....│" +
                "└────┘");
    }
}
