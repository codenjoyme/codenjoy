package com.codenjoy.dojo.web.rest;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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
import com.codenjoy.dojo.services.GameServiceImpl;
import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.services.PlayerGames;
import com.codenjoy.dojo.services.grpc.handler.UpdateHandler;
import com.codenjoy.dojo.services.hash.Hash;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
// TODO сделать удобным вовыд и расскомментить
// import static com.codenjoy.dojo.stuff.SmartAssert.assertEquals;

@SpringBootTest(classes = CodenjoyContestApplication.class,
        properties = "spring.main.allow-bean-definition-overriding=true")
@RunWith(SpringRunner.class)
@ActiveProfiles(SQLiteProfile.NAME)
@Import(RestGameControllerTest.ContextConfiguration.class)
@ContextConfiguration(initializers = AbstractRestControllerTest.PropertyOverrideContextInitializer.class)
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RestDojoPlayerControllerTest extends AbstractRestControllerTest {

    @Autowired
    @MockBean
    private PlayerGames playerGames;
    @Autowired
    @MockBean
    private UpdateHandler updateHandler;
    @Autowired
    private RestDojoPlayerController restDojoPlayerController;

    @Override
    @Before
    public void setUp() {
        super.setUp();
        registration.register("1", "dummy@email.com",
                "Name", "Name", Hash.md5("password"), "{}",
                Collections.singleton("ROLE_USER"), "username", "slackEmail");

        Player player = new Player();
        player.setId("1");
        player.setGitHubUsername("username");
        gameSaver.saveGame(player, "{}", System.currentTimeMillis());
    }

    @Test
    public void shouldUpdateScore() {
        post(HttpStatus.OK.value(), "/rest/update/username/score", "60");
        assertEquals("60", gameSaver.loadGame("1").getScore());
        verify(updateHandler, times(1)).sendUpdate("username", 60);
    }


    @Test
    public void shouldUpdateGitHubUsername() {
        String expected = "1";
        assertEquals(expected, post("/rest/change/username/to/newusername"));
        assertEquals("newusername",
                registration.getUserById("1").get().getGitHubUsername());
    }

    @Test
    public void shouldNotUpdateGitHubUsername() {
        String expected = "0";
        assertEquals(expected, post("/rest/change/non-existing/to/username"));
    }


    @TestConfiguration
    public static class ContextConfiguration {
        @Bean("gameService")
        public GameServiceImpl gameService() {
            return AbstractRestControllerTest.gameService();
        }
    }
}
