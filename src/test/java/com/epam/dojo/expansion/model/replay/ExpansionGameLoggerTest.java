package com.epam.dojo.expansion.model.replay;

/*-
 * #%L
 * expansion - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2017 EPAM
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


import com.codenjoy.dojo.services.EventListener;
import com.epam.dojo.expansion.model.Expansion;
import com.epam.dojo.expansion.model.Field;
import com.epam.dojo.expansion.model.Player;
import com.epam.dojo.expansion.model.ProgressBar;
import com.epam.dojo.expansion.model.levels.Level;
import com.epam.dojo.expansion.model.levels.LevelImpl;
import com.epam.dojo.expansion.model.levels.items.Hero;
import com.epam.dojo.expansion.services.SettingsWrapper;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Created by Oleksandr_Baglai on 2017-09-21.
 */
public class ExpansionGameLoggerTest {

    @Before
    public void setup() {
        SettingsWrapper.setup().waitingOthers(false);
    }

    @Test
    public void shouldCallWhenRegisterPlayer() {
        // given
        GameLogger logger = mock(GameLogger.class);
        Expansion expansion = new Expansion(getLevels(), null, logger, Expansion.MULTIPLE);

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
        Expansion expansion = new Expansion(getLevels(), null, logger, Expansion.SINGLE);

        // when
        Player player = createPlayer();
        expansion.newGame(player);

        // then
        verifyNoMoreInteractions(logger);
    }

    @NotNull
    private List<Level> getLevels() {
        return Arrays.asList(new LevelImpl("name", "                ", 4));
    }

    @Test
    public void shouldCallStartWhenCreateGame() {
        // given
        GameLogger logger = mock(GameLogger.class);

        // when
        Expansion expansion = new Expansion(getLevels(), null, logger, Expansion.MULTIPLE);

        // then
        verify(logger).start(expansion);
    }

    @Test
    public void shouldNotCallStartWhenCreateGameForSingleGame() {
        // given
        GameLogger logger = mock(GameLogger.class);

        // when
        Expansion expansion = new Expansion(getLevels(), null, logger, Expansion.SINGLE);

        // then
        verifyNoMoreInteractions(logger);
    }

    @Test
    public void shouldCallLogWhenTick() {
        // given
        GameLogger logger = mock(GameLogger.class);
        Expansion expansion = new Expansion(getLevels(), null, logger, Expansion.MULTIPLE);
        expansion.loadLevel(0);
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
        Expansion expansion = new Expansion(getLevels(), null, logger, Expansion.SINGLE);
        expansion.loadLevel(0);
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
        return new Player(mock(EventListener.class), mock(ProgressBar.class)) {
            @Override
            public void newHero(Field field) {
                setHero(new Hero(){
                    @Override
                    public void tick() {
                        // do nothing
                    }
                });
            }

            @Override
            public String toString() {
                return "";
            }
        };
    }

}
