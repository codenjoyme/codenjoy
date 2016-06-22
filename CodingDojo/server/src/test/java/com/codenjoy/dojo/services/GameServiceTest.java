package com.codenjoy.dojo.services;

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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
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
        assertEquals("[second, first]",
                gameService.getGameNames().toString());
    }

    @Test
    public void shouldGetSprites() {
        Map<String, List<String>> sprites = gameService.getSprites();
        assertEquals(
            "{second=[none, red, green, blue], " +
                    "first=[none, wall, hero]}",
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
