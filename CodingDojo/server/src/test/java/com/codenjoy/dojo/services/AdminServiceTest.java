package com.codenjoy.dojo.services;

import com.codenjoy.dojo.CodenjoyContestApplication;
import com.codenjoy.dojo.config.meta.SQLiteProfile;
import com.codenjoy.dojo.services.incativity.InactivitySettingsImpl;
import com.codenjoy.dojo.services.mocks.FirstInactivityGameType;
import com.codenjoy.dojo.services.room.RoomService;
import com.codenjoy.dojo.services.settings.Settings;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static com.codenjoy.dojo.utils.TestUtils.split;
import static org.junit.Assert.*;

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
}