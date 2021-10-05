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


import com.codenjoy.dojo.sample.TestGameSettings;
import com.codenjoy.dojo.sample.model.*;
import com.codenjoy.dojo.sample.services.GameSettings;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.multiplayer.LevelProgress;
import com.codenjoy.dojo.services.multiplayer.Single;
import com.codenjoy.dojo.utils.TestUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.*;
import org.junit.rules.TestName;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        log("setup");

        super.setup();

        logEnd();
    }

    @After
    @Override
    public void after() {
        super.after();
        TestUtils.assertSmokeFile("GameTest/" + name.getMethodName() +  ".txt", messages);
    }

    private void log(String method, Object... parameters) {
        deep++;
        messages.add(String.format("%s%s%s(%s)",
                (deep == 1) ? "\n" : "",
                deep(),
                method,
                Arrays.deepToString(parameters)
                        .replaceAll("^\\[", "")
                        .replaceAll("\\]$", "")));
    }

    private String deep() {
        return StringUtils.leftPad("", 4*(deep - 1), ' ');
    }

    private void logEnd() {
        deep--;
    }

    @Override
    public void dice(int... ints) {
        log("dice", ints);

        super.dice(ints);

        logEnd();
    }

    @Override
    public void givenFl(String... maps) {
        log("givenFl", maps);

        super.givenFl(maps);

        logEnd();
    }

    @Override
    protected void givenPlayer(Hero hero) {
        log("givenPlayer", hero);

        super.givenPlayer(hero);

        logEnd();
    }

    @Override
    public void tick() {
        log("tick");

        super.tick();

        logEnd();
    }

    // getters & asserts

    @Override
    public void assertF(String expected, int index) {
        log("assertF", expected, index);

        super.assertF(expected, index);

        logEnd();
    }

    @Override
    public Game game(int index) {
        log("game", index);

        Game result = super.game(index);

        logEnd();

        return result;
    }

    @Override
    public Player player(int index) {
        log("player", index);

        Player result = super.player(index);

        logEnd();

        return result;
    }

    @Override
    public Hero hero(int index) {
        log("hero", index);

        Hero result = super.hero(index);

        logEnd();

        return result;
    }

}