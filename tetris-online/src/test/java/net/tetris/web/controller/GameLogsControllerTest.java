package net.tetris.web.controller;

import com.jayway.restassured.path.json.JsonPath;
import net.tetris.online.service.GameLogFile;
import net.tetris.online.service.SecurityFilter;
import net.tetris.online.service.ServiceConfigFixture;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.jayway.restassured.path.json.JsonPath.from;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * User: serhiy.zelenin
 * Date: 10/29/12
 * Time: 8:35 PM
 */
public class GameLogsControllerTest {

    private ServiceConfigFixture fixture;
    private GameLogsController controller;
    private MockHttpServletRequest request;

    @Before
    public void setUp() {
        fixture = new ServiceConfigFixture();
        fixture.setup();
        controller = new GameLogsController(fixture.getConfiguration());
        controller.setTimestampFormat("yyyy-dd-MM HHmmss");
        request = new MockHttpServletRequest();
    }

    @After
    public void tearDown() throws IOException {
        fixture.tearDown();
    }

    @Test
    public void shouldBeEmptyArrayWhenNotLoggedUser() {
        String json = controller.gameLogs(request);

        assertEmptyDataArray(json);
    }

    private void assertEmptyDataArray(String json) {
        JsonPath jsonPath = from(json);
        assertTrue(jsonPath.getList("aaData").isEmpty());
    }

    @Test
    public void shouldSendGameLogEntry() {
        GameLogFile logFile = fixture.createLogFile("testUser", "2012-10-11 112233");
        logFile.log("something", "something");
        loginUser("testUser");

        String content = controller.gameLogs(request);

        JsonPath jsonPath = from(content);
        List<List> dataList = jsonPath.getList("aaData");
        assertEquals("2012-10-11 112233", dataList.get(0).get(0));
    }

    @Test
    public void shouldIgnoreNonLogFiles() throws IOException {
        loginUser("testUser");
        File testUserLogsDir = new File(fixture.getConfiguration().getLogsDir(), "testUser");
        testUserLogsDir.mkdirs();
        FileUtils.writeStringToFile(new File(testUserLogsDir, "scores.txt"), "bla-bla");

        String content = controller.gameLogs(request);

        assertEmptyDataArray(content);
    }

    private void loginUser(String playerName) {
        request.setAttribute(SecurityFilter.LOGGED_USER, playerName);
    }

    @Test
    public void shouldReturnEmptyListWhenLogDirNotExists(){
        loginUser("testUser");

        assertEmptyDataArray(controller.gameLogs(request));
    }
}
