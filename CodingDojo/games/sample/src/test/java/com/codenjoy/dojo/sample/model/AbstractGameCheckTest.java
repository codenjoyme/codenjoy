package com.codenjoy.dojo.sample.model;

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


import com.codenjoy.dojo.sample.services.GameSettings;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.utils.TestUtils;
import com.codenjoy.dojo.utils.events.EventsListenersAssert;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.ComparisonFailure;
import org.junit.Rule;
import org.junit.rules.TestName;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.mockito.Mockito.mock;

public abstract class AbstractGameCheckTest extends AbstractGameTest {

    @Rule
    public TestName test = new TestName();
    private List<String> messages;
    private int deep;
    private boolean delay;
    private String delayed;
    private boolean callRealAssert;

    @Before
    @Override
    public void setup() {
        messages = new LinkedList<>();
        deep = 0;
        delay = false;
        callRealAssert = false;

        addCall("before");
        super.setup();
        end();

        addCall(test.getMethodName());
    }

    @After
    @Override
    public void after() {
        end(); // end test

        addCall("after");
        super.after();
        end();

        checkFile();
    }

    protected String messages() {
        return messages.stream()
                .collect(joining("\n"));
    }

    protected void checkFile() {
        TestUtils.assertSmokeFile(this.getClass().getSimpleName()
                + "/" + test.getMethodName() +  ".txt", messages);
    }

    public void assertEquals(Object expected, Object actual) {
        if (callRealAssert) {
            super.assertEquals(expected, actual);
        }
    }

    private void addCall(String method, Object... parameters) {
        call(method, false, parameters);
    }

    private void appendCall(String method, Object... parameters) {
        call(method, true, parameters);
    }

    private void call(String method, boolean append, Object... parameters) {
        if (messages.isEmpty()) {
            append = false;
        }
        if (delay) {
            append = true;
        }
        if (!append) {
            deep++;
        }
        List<String> params = Arrays.stream(parameters)
                .map(param -> asString(param))
                .map(string -> string.replaceAll("\n$", ""))
                .collect(toList());
        boolean multiline = params.stream()
                .anyMatch(param -> param.contains("\n"));
        String data = params.stream()
                .collect(joining(multiline ? ",\n" : ", "));
        data = (data.contains("\n") ? "\n" : "") + data;
        data = data.replace("\n", "\n" + leftPad() + leftPad());
        String message = String.format("%s%s%s(%s)",
                (!append && deep == 1) ? "\n" : "",
                (!append) ? leftPad() : "",
                method,
                data);

        if (delay) {
            delayed = message;
            return;
        }

        if (!append) {
            messages.add(message);
        } else {
            append(message);
        }
    }

    private void append(String message) {
        int index = messages.size() - 1;
        messages.set(index, messages.get(index) + message);
    }

    private void appendResult(Object data) {
        append(String.format(" = %s", data.toString()));
    }

    private String asString(Object object) {
        if (object == null) {
            return "null";
        } else if (object.getClass().isArray()) {
            return arrayToString(object)
                    .replaceAll("^\\[", "")
                    .replaceAll("\\]$", "");
        } else {
            return object.toString();
        }
    }

    private String arrayToString(Object object) {
        Class<?> type = object.getClass().getComponentType();
        if (type.isPrimitive()) {
            if (boolean.class.isAssignableFrom(type)) {
                return Arrays.toString((boolean[]) object);
            }

            if (byte.class.isAssignableFrom(type)) {
                return Arrays.toString((byte[]) object);
            }

            if (char.class.isAssignableFrom(type)) {
                return Arrays.toString((char[]) object);
            }

            if (double.class.isAssignableFrom(type)) {
                return Arrays.toString((double[]) object);
            }

            if (float.class.isAssignableFrom(type)) {
                return Arrays.toString((float[]) object);
            }

            if (int.class.isAssignableFrom(type)) {
                return Arrays.toString((int[]) object);
            }

            if (long.class.isAssignableFrom(type)) {
                return Arrays.toString((long[]) object);
            }

            if (short.class.isAssignableFrom(type)) {
                return Arrays.toString((short[]) object);
            }
        }
        return Arrays.deepToString((Object[]) object);
    }

    private String leftPad() {
        return StringUtils.leftPad("", 4*(deep - 1), ' ');
    }

    private void end() {
        if (!delay) {
            deep--;
        }
    }

    private void delayOn() {
        delay = false;
    }

    private void delayOff() {
        delay = true;
    }

    @Override
    public void dice(int... ints) {
        addCall("dice", ints);

        super.dice(ints);

        end();
    }

    @Override
    public void givenFl(String... maps) {
        addCall("givenFl", maps);

        super.givenFl(maps);

        end();
    }

    @Override
    protected void givenPlayer(Hero hero) {
        addCall("givenPlayer", hero);

        super.givenPlayer(hero);

        end();
    }

    @Override
    public void tick() {
        addCall("tick");

        super.tick();

        end();
    }

    @Override
    public void assertF(String expected, int index) {
        Object actual = super.game(index).getBoardAsString();
        addCall("assertF", actual, index);

        if (callRealAssert) {
            super.assertF(expected, index);
        }

        end();
    }

    @Override
    public Game game(int index) {
//        log("game", index);

        // TODO add wrapper
        Game result = super.game(index);

//        logEnd();

        return result;
    }

    class PlayerWrapper extends Player {

        private final Player player;

        public PlayerWrapper(Player player) {
            super(null, mock(GameSettings.class)); // fake
            this.player = player;
        }

        @Override
        public boolean shouldLeave() {
            appendCall(".shouldLeave");
            boolean result = player.shouldLeave();
            appendResult(result);
            end();
            return result;
        }

        @Override
        public boolean wantToStay() {
            appendCall(".wantToStay");
            boolean result = player.wantToStay();
            appendResult(result);
            end();
            return result;
        }
    }

    @Override
    public Player player(int index) {
        addCall("player", index);

        return new PlayerWrapper(super.player(index));
    }

    class FieldWrapper extends Sample {

        private final Sample field;

        public FieldWrapper(Sample field) {
            super(null, null, field.settings()); // fake
            this.field = field;
        }

        @Override
        public void newGame(Player player) {
            delayOn();
            appendCall(".newGame", delayed());
            field.newGame(((PlayerWrapper)player).player);
            end();
        }

        @Override
        public void clearScore() {
            if (field == null) return; // check for fake

            delayOn();
            appendCall(".clearScore");
            field.clearScore();
            end();
        }
    }

    private String delayed() {
        String result = delayed;
        delayed = null;
        return result;
    }

    @Override
    public Sample field() {
        addCall("field");
        delayOff();

        return new FieldWrapper(super.field());
    }

    class SettingsWrapper extends GameSettings {

        private final GameSettings settings;

        public SettingsWrapper(GameSettings settings) {
            super();  // fake
            this.settings = settings;
        }

        @Override
        public GameSettings integer(Key key, int value) {
            if (settings == null) return this; // check for fake

            addCall("settings.integer", key, value);
            settings.integer(key, value);
            end();

            return this;
        }

        @Override
        public GameSettings string(Key key, String value) {
            if (settings == null) return this; // check for fake

            addCall("settings.string", key, value);
            settings.string(key, value);
            end();

            return this;
        }

        @Override
        public GameSettings bool(Key key, boolean value) {
            if (settings == null) return this; // check for fake

            addCall("settings.bool", key, value);
            settings.bool(key, value);
            end();

            return this;
        }
    }

    @Override
    public GameSettings settings() {
        return new SettingsWrapper(super.settings());
    }

    class HeroWrapper extends Hero {

        private final Hero hero;

        public HeroWrapper(Hero hero) {
            super(hero);
            this.hero = hero;
        }

        @Override
        public void up() {
            appendCall(".up");
            hero.up();
            end();
        }

        @Override
        public void down() {
            appendCall(".down");
            hero.down();
            end();
        }

        @Override
        public void left() {
            appendCall(".left");
            hero.left();
            end();
        }

        @Override
        public void right() {
            appendCall(".right");
            hero.right();
            end();
        }

        @Override
        public void act(int... p) {
            appendCall(".act", p);
            hero.act(p);
            end();
        }

        @Override
        public boolean isAlive() {
            appendCall(".isAlive");
            boolean result = hero.isAlive();
            appendResult(result);
            end();
            return result;
        }

        @Override
        public boolean isActive() {
            appendCall(".isActive");
            boolean result = hero.isActive();
            appendResult(result);
            end();
            return result;
        }

        @Override
        public int scores() {
            appendCall(".scores");
            int result = hero.scores();
            appendResult(result);
            end();
            return result;
        }

        @Override
        public void addScore(int added) {
            appendCall(".addScore", added);
            hero.addScore(added);
            end();
        }
    }

    @Override
    public Hero hero(int index) {
        addCall("hero", index);

        return new HeroWrapper(super.hero(index));
    }

    public class EventsWrapper extends EventsListenersAssert {

        private final EventsListenersAssert events;

        public EventsWrapper(EventsListenersAssert events) {
            super(null, null);    // fake
            this.events = events;
        }

        @Override
        public void verifyNoEvents(Integer... indexes) {
            appendCall(".verifyNoEvents", Arrays.asList(indexes));
            end();

            try {
                events.verifyNoEvents(indexes);
            } catch (ComparisonFailure failure) {
                verifyAllEvents(indexes);

                if (callRealAssert) {
                    throw failure;
                }
           }
        }

        private String verifyAllEvents(Integer[] indexes) {
            String actual = events.getEvents(indexes);
            appendCall(".verifyAllEvents", actual, Arrays.asList(indexes));
            end();

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
        addCall("events");

        return new EventsWrapper(super.events());
    }

}