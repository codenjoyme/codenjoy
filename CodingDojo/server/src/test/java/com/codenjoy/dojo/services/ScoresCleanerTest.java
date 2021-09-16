package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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

import com.codenjoy.dojo.config.meta.SQLiteProfile;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.mocks.FakePlayerScores;
import com.codenjoy.dojo.services.mocks.FirstGameType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles(SQLiteProfile.NAME)
public class ScoresCleanerTest {

    @Autowired
    PlayerService playerService;

    @Autowired
    SaveService saver;

    @Autowired
    Deals deals;

    @Autowired
    ScoresCleaner scoresCleaner;

    @SpyBean
    Registration registration;

    @Before
    public void init() {
        saver.removeAllSaves();
        playerService.removeAll();
    }

    private void createPlayer(String id, String room, Object score) {
        Player player = new Player(id);
        player.setGameType(new FirstGameType());
        player.setScores(new FakePlayerScores(score));
        player.setReadableName(id + "_name");
        when(registration.getNameById(anyString()))
                .thenReturn(player.getReadableName());
        deals.add(player, room, new PlayerSave("{}"));
        saver.save(id);
    }

    private void assertActiveScores(String expected) {
        List<Object> activeScores = deals.all().stream()
                .map(Deal::getPlayer)
                .map(Player::getScore)
                .collect(Collectors.toList());
        String actual = activeScores.toString();

        assertEquals(expected, actual);
    }

    private void assertSavedScores(String expected) {
        List<Object> savedScores = saver.getSaves().stream()
                .map(Player::getScore)
                .collect(Collectors.toList());
        String actual = savedScores.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void cleanAllScores() {
        // given
        createPlayer("player1", "first", 100);
        createPlayer("player2", "second", 50);

        // when
        scoresCleaner.cleanAllScores();

        // then
        assertActiveScores("[0, 0]");
        assertSavedScores("[0, 0]");
    }

    @Test
    public void cleanAllScoresForRoom() {
        // given
        createPlayer("player1", "first", 100);
        createPlayer("player2", "second", 50);

        // when
        scoresCleaner.cleanAllScores("first");

        // then
        assertActiveScores("[0, 50]");
        assertActiveScores("[0, 50]");
    }

    @Test
    public void cleanScoresForDeal() {
        // given
        createPlayer("player1", "first", 100);
        createPlayer("player2", "second", 50);

        // when
        scoresCleaner.cleanScores("player2");

        // then
        assertActiveScores("[100, 0]");
        assertSavedScores("[100, 0]");
    }
}
