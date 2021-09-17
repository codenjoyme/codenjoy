package com.codenjoy.dojo.services.chat;

import com.codenjoy.dojo.CodenjoyContestApplication;
import com.codenjoy.dojo.config.TestSqliteDBLocations;
import com.codenjoy.dojo.config.meta.SQLiteProfile;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.helper.LoginHelper;
import com.codenjoy.dojo.stuff.SmartAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static com.codenjoy.dojo.stuff.SmartAssert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CodenjoyContestApplication.class)
@ActiveProfiles(SQLiteProfile.NAME)
@ContextConfiguration(initializers = TestSqliteDBLocations.class)
public class PlayersCacheTest {

    @SpyBean
    private Registration registration;

    @Autowired
    private PlayersCache cache;

    @Autowired
    private LoginHelper login;

    @Before
    public void setup() {
        login.removeAll();
    }

    @After
    public void after() {
        SmartAssert.checkResult();
    }

    @Test
    public void shouldGetName_firstTime() {
        // given
        login.register("player", "ip", "room", "first");
        reset(registration);
        cache.removeAll();

        // when
        String name = cache.name("player");

        // then
        assertEquals("player-name", name);

        verify(registration, times(1)).getNameById("player");
    }

    @Test
    public void shouldGetName_secondTime() {
        // given
        shouldGetName_firstTime();
        reset(registration);

        when(registration.getNameById(anyString())).thenReturn("new_name");

        // when
        String name = cache.name("player");

        // then
        assertEquals("player-name", name);

        verify(registration, never()).getNameById("player");
    }

    @Test
    public void shouldGetName_notRegisteredUser() {
        // given
        login.register("player-ai", "ip", "room", "first");
        registration.remove("player-ai"); // no registration recprds
        reset(registration);
        cache.removeAll();

        // when
        String name = cache.name("player-ai");

        // then
        assertEquals("player[player-ai]", name);

        verify(registration, times(1)).getNameById("player-ai");
    }
}