package com.codenjoy.dojo.services;

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


import com.codenjoy.dojo.CodenjoyContestApplication;
import com.codenjoy.dojo.config.meta.SQLiteProfile;
import com.codenjoy.dojo.services.mocks.FirstGameType;
import com.codenjoy.dojo.services.mocks.SecondGameType;
import com.codenjoy.dojo.services.nullobj.NullGameType;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.*;
import static org.mockito.Mockito.reset;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 18:51
 */
@SpringBootTest(classes = CodenjoyContestApplication.class)
@RunWith(SpringRunner.class)
@ActiveProfiles(SQLiteProfile.NAME)
public class GameServiceTest {

    @SuppressWarnings("unchecked")
    private static final Set<Class<? extends GameType>> GAME_TEST_FILTER =
            Sets.newHashSet(NullGameType.class, AbstractGameType.class);

    @MockBean
    private TimerService timer;

    @MockBean
    private PlayerService players;

    @Autowired
    private GameServiceImpl gameService;

    @Autowired
    private ApplicationContext appCtx;

    private Map<String, GameType> loadedGameTypes;

    private List<String> activeGameNames = new ArrayList<>();

    private Map<String, List<String>> expectedPlots;

    /**
     * FIXME: Логика здесь дублирует логику GameServiceImpl, и требуется для того, чтобы успешно проходили тесты при запуске сборки
     *  с maven профилями других игр. Иначе в скриптах придется отдельно запускать сборку для прогона тестов (без профилей игр),
     *  и только затем сборку с -DskipTests в профиле необходимой для ивента игры.
     */
    @Before
    public void setup() {
        reset(timer, players);

        loadedGameTypes = gameService.findInPackage("com.codenjoy.dojo")
                .stream()
                .filter(gameClass -> !GAME_TEST_FILTER.contains(gameClass))
                .map(gameService::loadGameType)
                .peek(game -> activeGameNames.add(game.name()))
                .collect(Collectors.toMap(GameType::name, Function.identity()));

        expectedPlots = loadedGameTypes.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        e -> Arrays.stream(e.getValue().getPlots())
                                .map(Enum::name)
                                .map(String::toLowerCase)
                                .collect(Collectors.toList())));
    }

    @Test
    public void shouldGetGameNames() {
        assertThat("Games list must contain 'first', 'second' games, and" +
                " games loaded by active maven profiles", gameService.getGameNames(),
                containsInAnyOrder(activeGameNames.toArray()));
    }

    @Test
    public void shouldGetSprites() {
        Map<String, List<String>> sprites = gameService.getSprites();
        assertEquals(expectedPlots, sprites);
    }

    @Test
    public void shouldGetGame() {
        assertEquals(FirstGameType.class,
                gameService.getGame("first").getClass());

        assertEquals(SecondGameType.class,
                gameService.getGame("second").getClass());

        assertEquals(NullGameType.class,
                gameService.getGame("not-exists").getClass());
    }

    @Test
    public void shouldGetPngForSprites() {
        Map<String, List<String>> sprites = gameService.getSprites();

        List<String> errors = new LinkedList<>();
        for (Map.Entry<String, List<String>> entry : sprites.entrySet()) {
            for (String sprite : entry.getValue()) {
                String spriteUri = String.format("/%s/%s.png", entry.getKey(), sprite);
                File file = new File("target/test-classes/sprite" + spriteUri);
                if (!file.exists() && !appCtx.getResource("/sprite" + spriteUri).exists()) {
                    errors.add("Файл не найден: " + file.getAbsolutePath());
                }
            }
        }

        assertTrue(errors.toString().replace(',', '\n'), errors.isEmpty());
    }

}
