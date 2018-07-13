package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import com.codenjoy.dojo.services.mocks.MockPlayerService;
import com.codenjoy.dojo.services.mocks.MockTimerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.reset;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 18:51
 */
@ContextConfiguration(classes = {
        GameServiceImpl.class,
        MockTimerService.class,
        MockPlayerService.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class GameServiceTest {

    @Autowired private GameServiceImpl gameService;
    @Autowired private TimerService timer;
    @Autowired private PlayerService players;

    @Before
    public void setup() {
        reset(timer, players);
    }

    @Test
    public void shouldGetGameNames() {
        assertEquals("[first, second]",
                gameService.getGameNames().toString());
    }

    @Test
    public void shouldGetSprites() {
        Map<String, List<String>> sprites = gameService.getSprites();
        assertEquals(
            "{" +
                "first=[none, wall, hero], " +
                "second=[none, red, green, blue]" +
            "}",
                sprites.toString());
    }

    @Test
    public void shouldGetPngForSprites() {
        Map<String, List<String>> sprites = gameService.getSprites();

        List<String> errors = new LinkedList<String>();
        for (Map.Entry<String, List<String>> entry : sprites.entrySet()) {
            for (String sprite : entry.getValue()) {
                File file = new File(String.format("target/test-classes/sprite/%s/%s.png", entry.getKey(), sprite));
                if (!file.exists()) {
                    errors.add("Файл не найден: " + file.getAbsolutePath());
                }
            }
        }

        assertTrue(errors.toString().replace(',', '\n'), errors.isEmpty());
    }

}
