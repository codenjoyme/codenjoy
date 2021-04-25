package com.codenjoy.dojo.services;

import com.codenjoy.dojo.CodenjoyContestApplication;
import com.codenjoy.dojo.config.meta.SQLiteProfile;
import com.codenjoy.dojo.sample.services.GameRunner;
import com.codenjoy.dojo.web.rest.AbstractRestControllerTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertEquals;

@SpringBootTest(classes = CodenjoyContestApplication.class,
        properties = "spring.main.allow-bean-definition-overriding=true")
@RunWith(SpringRunner.class)
@ActiveProfiles(SQLiteProfile.NAME)
@Import(WatsNextPlayerServiceImplTest.ContextConfiguration.class)
@ContextConfiguration(initializers = AbstractRestControllerTest.PropertyOverrideContextInitializer.class)
@WebAppConfiguration
public class WatsNextPlayerServiceImplTest extends AbstractRestControllerTest {

    @TestConfiguration
    public static class ContextConfiguration {
        @Bean("gameService")
        public GameServiceImpl gameService() {
            return AbstractRestControllerTest.gameService(GameRunner.class);
        }
    }

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void shouldHeroMove_whenSendCommand() {
        whatsNx("sample",
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ☺ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n",

                "(0)->[ACT, LEFT]",

                "+--------------------\n" +
                "|       tick 1       \n" +
                "+--------------------\n" +
                "|\n" +
                "| (1) Board:\n" +
                "| (1) ☼☼☼☼☼\n" +
                "| (1) ☼   ☼\n" +
                "| (1) ☼☺x ☼\n" +
                "| (1) ☼   ☼\n" +
                "| (1) ☼☼☼☼☼\n" +
                "| (1) Events:[]\n" +
                "|\n" +
                "+--------------------\n");
    }

    @Test
    public void shouldCatchEvents_whenGameFiredIt() {
        whatsNx("sample",
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼☺x ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n",

                "(0)->[RIGHT]",

                "+--------------------\n" +
                "|       tick 1       \n" +
                "+--------------------\n" +
                "|\n" +
                "| (1) Board:\n" +
                "| (1) ☼☼☼☼☼\n" +
                "| (1) ☼   ☼\n" +
                "| (1) ☼ X ☼\n" +
                "| (1) ☼   ☼\n" +
                "| (1) ☼☼☼☼☼\n" +
                "| (1) Events:[LOSE]\n" +
                "|\n" +
                "+--------------------\n");
    }

    @Test
    public void shouldMultiplayer_onlyOnePlayerGo() {
        whatsNx("sample",
                "☼☼☼☼☼\n" +
                "☼☺ ☺☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n",

                "(1)->[ACT, LEFT]",

                "+--------------------\n" +
                "|       tick 1       \n" +
                "+--------------------\n" +
                "|\n" +
                "| (1) Board:\n" +
                "| (1) ☼☼☼☼☼\n" +
                "| (1) ☼☺☻x☼\n" +
                "| (1) ☼   ☼\n" +
                "| (1) ☼   ☼\n" +
                "| (1) ☼☼☼☼☼\n" +
                "| (1) Events:[]\n" +
                "|\n" +
                "| (2) Board:\n" +
                "| (2) ☼☼☼☼☼\n" +
                "| (2) ☼☻☺x☼\n" +
                "| (2) ☼   ☼\n" +
                "| (2) ☼   ☼\n" +
                "| (2) ☼☼☼☼☼\n" +
                "| (2) Events:[]\n" +
                "|\n" +
                "+--------------------\n");
    }

    @Test(timeout = 30000)
    public void shouldMethodIsFast() {
        // about 13 sec
        for (int i = 0; i < 100000; i++) {
            whatsNx("sample",
                    "☼☼☼☼☼\n" +
                    "☼☺ ☺☼\n" +
                    "☼   ☼\n" +
                    "☼☺ ☺☼\n" +
                    "☼☼☼☼☼\n",

                    "(3)->[ACT, LEFT]",

                    "+--------------------\n" +
                    "|       tick 1       \n" +
                    "+--------------------\n" +
                    "|\n" +
                    "| (1) Board:\n" +
                    "| (1) ☼☼☼☼☼\n" +
                    "| (1) ☼☺ ☻☼\n" +
                    "| (1) ☼   ☼\n" +
                    "| (1) ☼☻☻x☼\n" +
                    "| (1) ☼☼☼☼☼\n" +
                    "| (1) Events:[]\n" +
                    "|\n" +
                    "| (2) Board:\n" +
                    "| (2) ☼☼☼☼☼\n" +
                    "| (2) ☼☻ ☺☼\n" +
                    "| (2) ☼   ☼\n" +
                    "| (2) ☼☻☻x☼\n" +
                    "| (2) ☼☼☼☼☼\n" +
                    "| (2) Events:[]\n" +
                    "|\n" +
                    "| (3) Board:\n" +
                    "| (3) ☼☼☼☼☼\n" +
                    "| (3) ☼☻ ☻☼\n" +
                    "| (3) ☼   ☼\n" +
                    "| (3) ☼☺☻x☼\n" +
                    "| (3) ☼☼☼☼☼\n" +
                    "| (3) Events:[]\n" +
                    "|\n" +
                    "| (4) Board:\n" +
                    "| (4) ☼☼☼☼☼\n" +
                    "| (4) ☼☻ ☻☼\n" +
                    "| (4) ☼   ☼\n" +
                    "| (4) ☼☻☺x☼\n" +
                    "| (4) ☼☼☼☼☼\n" +
                    "| (4) Events:[]\n" +
                    "|\n" +
                    "+--------------------\n");
        }
    }

    @Test
    public void shouldMultiplayer_everyHeroCanGo() {
        whatsNx("sample",
                "☼☼☼☼☼\n" +
                "☼☺  ☼\n" +
                "☼   ☼\n" +
                "☼  ☺☼\n" +
                "☼☼☼☼☼\n",

                "(1)->[ACT, UP]&(0)->[ACT, DOWN]",

                "+--------------------\n" +
                "|       tick 1       \n" +
                "+--------------------\n" +
                "|\n" +
                "| (1) Board:\n" +
                "| (1) ☼☼☼☼☼\n" +
                "| (1) ☼x  ☼\n" +
                "| (1) ☼☺ ☻☼\n" +
                "| (1) ☼  x☼\n" +
                "| (1) ☼☼☼☼☼\n" +
                "| (1) Events:[]\n" +
                "|\n" +
                "| (2) Board:\n" +
                "| (2) ☼☼☼☼☼\n" +
                "| (2) ☼x  ☼\n" +
                "| (2) ☼☻ ☺☼\n" +
                "| (2) ☼  x☼\n" +
                "| (2) ☼☼☼☼☼\n" +
                "| (2) Events:[]\n" +
                "|\n" +
                "+--------------------\n");
    }

    @Test
    public void shouldSeveralTicksOnMultiplayer() {
        whatsNx("sample",
                "☼☼☼☼☼\n" +
                "☼☺  ☼\n" +
                "☼   ☼\n" +
                "☼  ☺☼\n" +
                "☼☼☼☼☼\n",

                "(1)->[ACT, UP]&(0)->[ACT, DOWN];" +
                "(0)->[DOWN]&(1)->[UP];",

                "+--------------------\n" +
                "|       tick 1       \n" +
                "+--------------------\n" +
                "|\n" +
                "| (1) Board:\n" +
                "| (1) ☼☼☼☼☼\n" +
                "| (1) ☼x  ☼\n" +
                "| (1) ☼☺ ☻☼\n" +
                "| (1) ☼  x☼\n" +
                "| (1) ☼☼☼☼☼\n" +
                "| (1) Events:[]\n" +
                "|\n" +
                "| (2) Board:\n" +
                "| (2) ☼☼☼☼☼\n" +
                "| (2) ☼x  ☼\n" +
                "| (2) ☼☻ ☺☼\n" +
                "| (2) ☼  x☼\n" +
                "| (2) ☼☼☼☼☼\n" +
                "| (2) Events:[]\n" +
                "|\n" +
                "+--------------------\n" +
                "|       tick 2       \n" +
                "+--------------------\n" +
                "|\n" +
                "| (1) Board:\n" +
                "| (1) ☼☼☼☼☼\n" +
                "| (1) ☼x ☻☼\n" +
                "| (1) ☼   ☼\n" +
                "| (1) ☼☺ x☼\n" +
                "| (1) ☼☼☼☼☼\n" +
                "| (1) Events:[]\n" +
                "|\n" +
                "| (2) Board:\n" +
                "| (2) ☼☼☼☼☼\n" +
                "| (2) ☼x ☺☼\n" +
                "| (2) ☼   ☼\n" +
                "| (2) ☼☻ x☼\n" +
                "| (2) ☼☼☼☼☼\n" +
                "| (2) Events:[]\n" +
                "|\n" +
                "+--------------------\n");
    }

    private void whatsNx(String room, String actions, String command, String expected) {
        String board = playerService.whatsNext(room, actions, command);
        assertEquals(expected, board);
    }

}
