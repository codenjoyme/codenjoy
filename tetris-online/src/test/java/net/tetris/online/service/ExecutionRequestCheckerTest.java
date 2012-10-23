package net.tetris.online.service;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;

import static org.apache.commons.io.FileUtils.readFileToString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * User: serhiy.zelenin
 * Date: 10/10/12
 * Time: 8:22 PM
 */
@RunWith(MockitoJUnitRunner.class)
public class ExecutionRequestCheckerTest {

    private File homeFolder;
    @Mock
    private ServiceConfiguration configuration;
    @Mock
    private GameExecutorService executorService;

    private ExecutionRequestChecker checker;

    @Captor
    private ArgumentCaptor<String> userCaptor;

    @Captor
    private ArgumentCaptor<File> appCaptor;
    private File archiveFolder;
    private ServiceConfigFixture fixture;

    @Before
    public void setUp() {
        fixture = new ServiceConfigFixture();
        fixture.setupConfiguration(configuration);
        homeFolder = fixture.getHomeFolder();
        archiveFolder = fixture.getArchiveFolder();
        checker = new ExecutionRequestChecker(configuration, executorService);
    }

    @After
    public void tearDown() throws IOException {
        fixture.tearDown();
    }

    @Test
    public void shouldExecuteWhenOneRequest() throws IOException {
        File warFile = createAppfile("vasya.war");

        checker.run();

        verify(executorService).runGame(userCaptor.capture(), appCaptor.capture());
        assertExecutedApp("vasya", warFile);
    }

    private void assertExecutedApp(String expectedUser, File warFile) {
        assertEquals(expectedUser, userCaptor.getValue());
        assertEquals(warFile, appCaptor.getValue());
    }

    private File createAppfile(String fileName) throws IOException {
        return createAppfile(fileName, "some war content");
    }

    private File createAppfile(String fileName, String content) throws IOException {
        File warFile = new File(homeFolder, fileName);
        FileUtils.writeStringToFile(warFile, content);
        return warFile;
    }

    @Test
    public void shouldArchiveExecutedApps() throws IOException {
        File warFile = createAppfile("vasya.war", "bla-bla");

        checker.run();

        assertEquals("bla-bla", readFileToString(new File(archiveFolder, "vasya.war")));
    }

    @Test
    public void shouldNotFailWhenException() throws IOException {
        createAppfile("petya.war", "something");
        doThrow(new RuntimeException()).when(executorService).runGame(anyString(), any(File.class));

        checker.run();

        assertEquals("something", readFileToString(new File(archiveFolder, "petya.war")));
    }

    @Test
    public void shouldExecuteOnce() throws IOException {
        File warFile = createAppfile("vasya.war");
        checker.run();

        checker.run();

        verify(executorService, times(1)).runGame(userCaptor.capture(), appCaptor.capture());
        assertExecutedApp("vasya", warFile);
    }
}
