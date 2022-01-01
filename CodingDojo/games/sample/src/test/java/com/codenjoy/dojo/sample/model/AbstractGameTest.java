package com.codenjoy.dojo.sample.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2022 Codenjoy
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
import com.codenjoy.dojo.sample.services.Event;
import com.codenjoy.dojo.sample.services.GameSettings;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.multiplayer.TriFunction;
import com.codenjoy.dojo.utils.gametest.AbstractBaseGameTest;
import org.junit.After;
import org.junit.Before;

import java.util.function.BiFunction;
import java.util.function.Function;

import static com.codenjoy.dojo.utils.TestUtils.asArray;

public abstract class AbstractGameTest
        extends AbstractBaseGameTest<Player, Sample, GameSettings, Level, Hero> {

    @Before
    public void setup() {
        super.setup();
    }

    @After
    public void after() {
        super.after();
    }

    @Override
    protected void setupHeroesDice() {
        dice(asArray(level().heroes()));
    }

    @Override
    protected GameSettings setupSettings() {
        return new TestGameSettings();
    }

    @Override
    protected Function<String, Level> createLevel() {
        return Level::new;
    }

    @Override
    protected BiFunction<EventListener, GameSettings, Player> createPlayer() {
        return Player::new;
    }

    @Override
    protected TriFunction<Dice, Level, GameSettings, Sample> createField() {
        return Sample::new;
    }

    @Override
    protected Class<?> eventClass() {
        return Event.class;
    }

    // other methods
}