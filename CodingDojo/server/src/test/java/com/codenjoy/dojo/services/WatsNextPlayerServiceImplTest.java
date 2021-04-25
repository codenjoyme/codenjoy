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
                0, "ACT, LEFT",
                "Board:\n" +
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼☺x ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n" +
                "Events:[]\n");
    }

    @Test
    public void shouldCatchEvents_whenGameFiredIt() {
        whatsNx("sample",
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼☺x ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n",
                0, "RIGHT",
                "Board:\n" +
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ X ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n" +
                "Events:[LOSE]\n");
    }

    @Test
    public void shouldMultiplayer() {
        whatsNx("sample",
                "☼☼☼☼☼\n" +
                "☼☺ ☺☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n",
                1, "ACT, LEFT",
                "Board:\n" +
                "☼☼☼☼☼\n" +
                "☼☻☺x☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n" +
                "Events:[]\n");
    }

    private void whatsNx(String room, String inputBoard, int playerIndex, String command, String expected) {
        String board = playerService.whatsNext(room, inputBoard, playerIndex, command);
        assertEquals(expected, board);
    }

}
