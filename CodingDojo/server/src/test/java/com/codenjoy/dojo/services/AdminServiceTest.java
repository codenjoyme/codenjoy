package com.codenjoy.dojo.services;

import com.codenjoy.dojo.CodenjoyContestApplication;
import com.codenjoy.dojo.config.meta.SQLiteProfile;
import com.codenjoy.dojo.services.incativity.InactivitySettingsImpl;
import com.codenjoy.dojo.services.mocks.FirstInactivityGameType;
import com.codenjoy.dojo.services.mocks.SecondSemifinalGameType;
import com.codenjoy.dojo.services.room.RoomService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static com.codenjoy.dojo.utils.TestUtils.split;
import static java.util.stream.Collectors.joining;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CodenjoyContestApplication.class)
@RunWith(SpringRunner.class)
@ActiveProfiles(SQLiteProfile.NAME)
public class AdminServiceTest {

    @SpyBean
    private TimeService timeService;

    @SpyBean
    private PlayerService playerService;

    @SpyBean
    private RoomService roomService;

    @MockBean
    private SaveService saveService;

    @Autowired
    private AdminService adminService;

    @Test
    public void shouldUpdateSettings_whenUpdateInactivity() {
        // given
        roomService.create("room", new FirstInactivityGameType());

        assertSettings("First[Parameter 1=15, \n" +
                "Parameter 2=true, \n" +
                "[Inactivity] Kick inactive players=false, \n" +
                "[Inactivity] Inactivity timeout ticks=300]");

        // when
        adminService.updateInactivity("room",
                new InactivitySettingsImpl()
                        .setInactivityTimeout(123)
                        .setKickEnabled(true));

        // then
        assertSettings("First[Parameter 1=15, \n" +
                        "Parameter 2=true, \n" +
                        "[Inactivity] Kick inactive players=true, \n" +
                        "[Inactivity] Inactivity timeout ticks=123]");
    }

    private void assertSettings(String expected) {
        assertEquals(expected,
                split(roomService.settings("room"), ", \n"));
    }

    @Test
    public void shouldResetLastResponse_whenUpdateInactivity() {
        // given
        roomService.create("room1", new FirstInactivityGameType());
        roomService.create("room2", new FirstInactivityGameType());
        roomService.create("room3", new SecondSemifinalGameType());

        AtomicLong time = new AtomicLong(1000L);
        when(timeService.now()).thenAnswer(inv -> time.getAndIncrement());

        List<Player> players = new LinkedList<>();
        players.add(playerService.register("player1", "first", "room1", "ip1"));
        players.add(playerService.register("player2", "first", "room1", "ip2"));
        players.add(playerService.register("player3", "first", "room2", "ip3"));   // another room
        players.add(playerService.register("player4", "second", "room3", "ip4"));  // another game

        assertPlayersLastResponse(playerService.getAll(),
                "[player1: 1000], [player2: 1001], [player3: 1002], [player4: 1003]");

        // when
        adminService.updateInactivity("room1",
                new InactivitySettingsImpl()
                        .setInactivityTimeout(123)
                        .setKickEnabled(true));

        // then
        assertPlayersLastResponse(playerService.getAll(),
                "[player1: 1004], [player2: 1005], [player3: 1002], [player4: 1003]");
    }

    public static void assertPlayersLastResponse(List<Player> players, String expected) {
        assertEquals(expected,
                players.stream()
                        .map(player -> String.format("[%s: %s]", player.getId(), player.getLastResponse()))
                        .collect(joining(", ")));
    }
}