package com.codenjoy.dojo.sample.model.experimental;

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
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.utils.TestUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractGameCheckTest extends AbstractGameTest {

    @Rule
    public TestName name = new TestName();
    private List<String> messages;
    private int deep;

    @Before
    @Override
    public void setup() {
        messages = new LinkedList<>();
        deep = 0;
        addCall("setup");

        super.setup();

        logEnd();
    }

    @After
    @Override
    public void after() {
        super.after();
        TestUtils.assertSmokeFile("GameTest/" + name.getMethodName() +  ".txt", messages);
    }

    private void addCall(String method, Object... parameters) {
        log(method, false, parameters);
    }

    private void appendCall(String method, Object... parameters) {
        log(method, true, parameters);
    }

    private void log(String method, boolean append, Object... parameters) {
        if (messages.isEmpty()) {
            append = false;
        }
        if (!append) {
            deep++;
        }
        String data = Arrays.stream(parameters)
                .map(param -> asString(param))
                .map(string -> string.replaceAll("\n$", ""))
                .collect(Collectors.joining("\n"));
        data = (data.contains("\n") ? "\n" : "") + data;
        String message = String.format("%s%s%s(%s)",
                (!append && deep == 1) ? "\n" : "",
                (!append) ? deep() : "",
                method,
                data);

        if (!append) {
            messages.add(message);
        } else {
            int index = messages.size() - 1;
            messages.set(index, messages.get(index) + message);
        }
    }

    private void logAdd(Object data) {
        messages.add(String.format(" = %s", data.toString()));
    }

    private String asString(Object object) {
        if (object.getClass().isArray()) {
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

    private String deep() {
        return StringUtils.leftPad("", 4*(deep - 1), ' ');
    }

    private void logEnd() {
        deep--;
    }

    @Override
    public void dice(int... ints) {
        addCall("dice", ints);

        super.dice(ints);

        logEnd();
    }

    @Override
    public void givenFl(String... maps) {
        addCall("givenFl", maps);

        super.givenFl(maps);

        logEnd();
    }

    @Override
    protected void givenPlayer(Hero hero) {
        addCall("givenPlayer", hero);

        super.givenPlayer(hero);

        logEnd();
    }

    @Override
    public void tick() {
        addCall("tick");

        super.tick();

        logEnd();
    }

    // getters & asserts

    @Override
    public void assertF(String expected, int index) {
        Object actual = super.game(index).getBoardAsString();
        addCall("assertF", actual, index);

        super.assertF(expected, index);

        logEnd();
    }

    @Override
    public Game game(int index) {
//        log("game", index);

        Game result = super.game(index);

//        logEnd();

        return result;
    }

    @Override
    public Player player(int index) {
        addCall("player", index);

        Player result = super.player(index);

        logEnd();

        return result;
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
            logEnd();
        }

        @Override
        public void down() {
            appendCall(".down");
            hero.down();
            logEnd();
        }

        @Override
        public void left() {
            appendCall(".left");
            hero.left();
            logEnd();
        }

        @Override
        public void right() {
            appendCall(".right");
            hero.right();
            logEnd();
        }

        @Override
        public void act(int... p) {
            appendCall(".act", p);
            hero.act(p);
            logEnd();
        }

        @Override
        public boolean isAlive() {
            appendCall(".isAlive");
            boolean result = hero.isAlive();
            logAdd(result);
            logEnd();
            return result;
        }

        @Override
        public int scores() {
            appendCall(".scores");
            int result = hero.scores();
            logAdd(result);
            logEnd();
            return result;
        }
    }

    @Override
    public Hero hero(int index) {
        addCall("hero", index);

        Hero result = new HeroWrapper(super.hero(index));

        return result;
    }

}