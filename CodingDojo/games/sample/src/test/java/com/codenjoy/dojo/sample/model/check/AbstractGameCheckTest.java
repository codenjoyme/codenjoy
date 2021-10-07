package com.codenjoy.dojo.sample.model.check;

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


import com.codenjoy.dojo.sample.model.AbstractGameTest;
import com.codenjoy.dojo.sample.model.Hero;
import com.codenjoy.dojo.sample.model.Player;
import com.codenjoy.dojo.sample.model.Sample;
import com.codenjoy.dojo.sample.services.GameSettings;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.utils.TestUtils;
import com.codenjoy.dojo.utils.check.WrapperManager;
import com.codenjoy.dojo.utils.events.EventsListenersAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

import java.util.Arrays;

public abstract class AbstractGameCheckTest extends AbstractGameTest {

    @Rule
    public TestName test = new TestName();
    protected WrapperManager manager;
    private boolean callRealAssert;

    @Before
    @Override
    public void setup() {
        callRealAssert = true;
        manager = new WrapperManager();

        manager.addCall("before");
        super.setup();
        manager.end();

        manager.addCall(test.getMethodName());
    }

    @After
    @Override
    public void after() {
        manager.end(); // end test

        manager.addCall("after");
        super.after();
        manager.end();

        checkFile();
    }

    protected void checkFile() {
        TestUtils.assertSmokeFile(this.getClass().getSimpleName()
                + "/" + test.getMethodName() +  ".data", manager.messagesList());
    }

    public void assertEquals(Object expected, Object actual) {
        if (callRealAssert) {
            super.assertEquals(expected, actual);
        }
    }

    public class EventsWrapper extends EventsListenersAssert {

        private final EventsListenersAssert events;

        public EventsWrapper(EventsListenersAssert events) {
            super(null, null);    // fake
            this.events = events;
        }

        private String verifyAllEvents(Integer[] indexes) {
            String actual = events.getEvents(indexes);
            manager.appendCall(".verifyAllEvents", actual, Arrays.asList(indexes));
            manager.end();
            return actual;
        }

        @Override
        public void verifyAllEvents(String expected, Integer... indexes) {
            String actual = verifyAllEvents(indexes);
            if (callRealAssert) {
                assertEquals(expected, actual);
            }
        }
    }

    @Override
    public EventsListenersAssert events() {
        manager.addCall("events");
        return new EventsWrapper(super.events());
    }

    @Override
    public void dice(int... ints) {
        manager.addCall("dice", ints);
        super.dice(ints);
        manager.end();
    }

    @Override
    public void givenFl(String... maps) {
        manager.addCall("givenFl", maps);
        super.givenFl(maps);
        manager.end();
    }

    @Override
    protected void givenPlayer(Hero hero) {
        manager.addCall("givenPlayer", hero);
        super.givenPlayer(hero);
        manager.end();
    }

    @Override
    public void tick() {
        manager.addCall("tick");
        super.tick();
        manager.end();
    }

    @Override
    public void assertF(String expected, int index) {
        Object actual = super.game(index).getBoardAsString();
        manager.addCall("assertF", actual, index);
        if (callRealAssert) {
            super.assertF(expected, index);
        }
        manager.end();
    }

    @Override
    public Game game(int index) {
        Game result = manager.objectSpy(super.game(index),
                "[-]getPlayer",
                "[-]getBoardAsString");
        manager.caller("game", result, index);
        return result;
    }

    @Override
    public Player player(int index) {
        Player result = manager.objectSpy(super.player(index),
                "[-]getHero",
                "[-R]inTeam",
                "wantToStay",  // TODO подумать как сделать включение всех, минус какие-то
                "shouldLeave");
        manager.caller("player", result, index);
        return result;
    }

    @Override
    public Sample field() {
        Sample result = manager.objectSpy(super.field(),
                "newGame",
                "clearScore");
        manager.caller("field", result);
        manager.pending(true);
        return result;
    }

    @Override
    public GameSettings settings() {
        GameSettings result = manager.objectSpy(super.settings(),
                "[-R]SettingsReader:integer",
                "[-R]SettingsReader:string",
                "[-R]SettingsReader:bool");
        manager.caller("settings", result);
        return result;
    }

    @Override
    public Hero hero(int index) {
        Hero result = manager.objectSpy(super.hero(index),
                "[-]itsMe");
        manager.caller("hero", result, index);
        return result;
    }

}