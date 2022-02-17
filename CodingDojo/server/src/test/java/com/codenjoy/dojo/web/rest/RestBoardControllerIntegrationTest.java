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
import com.codenjoy.dojo.services.hash.Hash;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
// TODO сделать удобным вовыд и расскомментить
// import static com.codenjoy.dojo.stuff.SmartAssert.assertEquals;

@SpringBootTest(classes = CodenjoyContestApplication.class,
        properties = "spring.main.allow-bean-definition-overriding=true")
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = {SQLiteProfile.NAME,"test"})
@Import(RestGameControllerTest.ContextConfiguration.class)
@ContextConfiguration(initializers = AbstractRestControllerTest.PropertyOverrideContextInitializer.class)
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class RestBoardControllerIntegrationTest extends AbstractRestControllerTest {

    @Autowired
    private RestBoardController restBoardController;

    @Override
    @Before
    public void setUp() {
        super.setUp();
        registration.register("1", "dummy@email.com",
                "Name", "nickName", Hash.md5("password"), "{}",
                Collections.singleton("ROLE_USER"), "ghusername", "slackEmail");
        Player player = new Player();
        player.setId("1");
        player.setRoom("kata");
        player.setScore(2);
        player.setGame("game");
        player.setReadableName("Readable Name");
        gameSaver.saveGame(player, "{}", System.currentTimeMillis());
    }

    @Test
    public void shouldGetPlayersScoresForGame() {
        String expected = "[{'id':'1','name':'nickName','score':2}]";
        assertEquals(expected, quote(get("/rest/room/kata/scores")));
    }

    @Test
    public void shouldGetEmptyScoresForGame() {
        String expected = "[]";
        assertEquals(expected, get("/rest/room/non-existing/scores"));
    }

    @TestConfiguration
    public static class ContextConfiguration {
        @Bean("gameService")
        public GameServiceImpl gameService() {
            return AbstractRestControllerTest.gameService();
        }
    }
}