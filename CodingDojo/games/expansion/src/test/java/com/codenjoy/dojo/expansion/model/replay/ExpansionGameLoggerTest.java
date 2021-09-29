package com.codenjoy.dojo.expansion.model.replay;

/*-
 * #%L
 * expansion - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2020 Codenjoy
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


import com.codenjoy.dojo.expansion.model.Expansion;
import com.codenjoy.dojo.expansion.model.IField;
import com.codenjoy.dojo.expansion.model.Player;
import com.codenjoy.dojo.expansion.model.Ticker;
import com.codenjoy.dojo.expansion.model.levels.Level;
import com.codenjoy.dojo.expansion.model.levels.items.Hero;
import com.codenjoy.dojo.expansion.services.GameSettings;
import com.codenjoy.dojo.services.EventListener;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class ExpansionGameLoggerTest {

    private GameSettings settings;

    @Before
    public void setup() {
        settings = new GameSettings();
    }

    @Test
    public void shouldCallWhenRegisterPlayer() {
        // given
        GameLogger logger = mock(GameLogger.class);
        Expansion expansion = new Expansion(getLevel(), mock(Ticker.class),
                null, logger, Expansion.MULTIPLE, settings);

        // when
        Player player = createPlayer();
        expansion.newGame(player);

        // then
        verify(logger).register(player);
    }

    @Test
    public void shouldNotCallWhenRegisterPlayerForSingleGame() {
        // given
        GameLogger logger = mock(GameLogger.class);
        Expansion expansion = new Expansion(getLevel(), mock(Ticker.class),
                null, logger, Expansion.SINGLE, settings);

        // when
        Player player = createPlayer();
        expansion.newGame(player);

        // then
        verifyNoMoreInteractions(logger);
    }

    @NotNull
    private Level getLevel() {
        return new Level("name", "   1        2   ", 4);
    }

    @Test
    public void shouldCallStartWhenCreateGame() {
        // given
        GameLogger logger = mock(GameLogger.class);

        // when
        Expansion expansion = new Expansion(getLevel(), mock(Ticker.class),
                null, logger, Expansion.MULTIPLE, settings);

        // then
        verify(logger).start(expansion);
    }

    @Test
    public void shouldNotCallStartWhenCreateGameForSingleGame() {
        // given
        GameLogger logger = mock(GameLogger.class);

        // when
        Expansion expansion = new Expansion(getLevel(), mock(Ticker.class),
                null, logger, Expansion.SINGLE, settings);

        // then
        verifyNoMoreInteractions(logger);
    }

    @Test
    public void shouldCallLogWhenTick() {
        // given
        GameLogger logger = mock(GameLogger.class);
        Expansion expansion = new Expansion(getLevel(), mock(Ticker.class),
                null, logger, Expansion.MULTIPLE, settings);
        Player player = createPlayer();
        expansion.newGame(player);
        reset(logger);

        // when
        expansion.tick();

        // then
        verify(logger).logState();
    }

    @Test
    public void shouldNotCallLogWhenTickForSingleGame() {
        // given
        GameLogger logger = mock(GameLogger.class);
        Expansion expansion = new Expansion(getLevel(), mock(Ticker.class),
                null, logger, Expansion.SINGLE, settings);
        Player player = createPlayer();
        expansion.newGame(player);
        reset(logger);

        // when
        expansion.tick();

        // then
        verifyNoMoreInteractions(logger);
    }

    @NotNull
    private Player createPlayer() {
        return new Player(mock(EventListener.class), null, settings) {
            @Override
            public void newHero(IField field) {
                Hero hero = new Hero() {
                    @Override
                    public void tick() {
                        // do nothing
                    }
                };
                hero.init(field);
                setHero(hero);
            }

            @Override
            public String toString() {
                return "";
            }
        };
    }

}
