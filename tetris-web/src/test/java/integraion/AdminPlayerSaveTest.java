package integraion;

import integraion.SpringMockerJettyRunner;
import net.tetris.services.PlayerService;
import net.tetris.services.PlayerGameSaver;
import net.tetris.services.PlayerInfo;
import org.junit.*;
import org.mockito.Mockito;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * User: serhiy.zelenin
 * Date: 7/2/12
 * Time: 6:35 PM
 */
public class AdminPlayerSaveTest  {
    private static WebDriver driver;
    private static String url;
    private static SpringMockerJettyRunner runner;
    private static PlayerService<Object> playerService;

    private static final boolean HAS = true;
    private static final boolean HAS_NOT = !HAS;

    @Before
    public void setUp() throws Exception {
        clearAllGameFiles();
        Mockito.reset(playerService);
    }

    @BeforeClass
    public static void startServer() throws Exception {
        runner = new SpringMockerJettyRunner("src/main/webapp", "tetris-web/src/main/webapp");
        runner.mockBean("playerService");
        int port = runner.start();
        playerService = runner.getBean("playerService");

        url = "http://localhost:" + port + "/admin31415";
        System.out.println(url);

        driver = new HtmlUnitDriver(true);
    }

    private void clearAllGameFiles() {
        new PlayerGameSaver().delete(null);
    }

    @AfterClass
    public static void stop() throws Exception {
        runner.stop();
    }

    @Test
    public void shouldNoUsersTableWhenNoUsers() throws IOException, InterruptedException {
        atAdminPage();
        assertUserRecordsCount(0);
    }

    @Test
    public void shouldAllUsersListWhenSomeUsers() throws IOException, InterruptedException {
        PlayerInfo player1 = createActiveUser("vasia");
        PlayerInfo player2 = createActiveUser("petia");
        PlayerInfo player3 = createActiveUser("katia");
        when(playerService.getPlayersGames()).thenReturn(Arrays.asList(player1, player2, player3));

        atAdminPage();

        assertUserRecordsCount(3);
        assertUserRecordsIs(player1, player2, player3);
    }

    @Test
    public void shouldMixedListWhenActiveAndOrSavedUsers()  {
        PlayerInfo active = createActiveUser("vasia");
        PlayerInfo saved = createSavedUser("petia");
        PlayerInfo savedActive = createSavedActiveUser("kolia");
        when(playerService.getPlayersGames()).thenReturn(Arrays.asList(active, saved, savedActive));

        atAdminPage();

        assertHasSaveLink(HAS, active, savedActive);
        assertHasSaveLink(HAS_NOT, saved);

        assertHasGameOverLink(HAS, active, savedActive);
        assertHasGameOverLink(HAS_NOT, saved);

        assertHasLoadLink(HAS, savedActive, saved);
        assertHasLoadLink(HAS_NOT, active);
    }

    private void assertHasLoadLink(boolean hasLink, PlayerInfo... players) {
        assertHasLink(hasLink, "Load", "load", players);
    }

    private void assertHasLink(boolean hasLink, String linkName, String link, PlayerInfo... players) {
        List<String> hrefs = collectValuesOf("//a[text()='" + linkName + "']", "href");

        for (PlayerInfo player : players) {
            assertEquals(String.format("%s link '%s' for player '%s'.", (hasLink)?"Expected":"Unexpected", linkName, player.getName()),
                    hasLink,
                    hrefs.toString().contains("/admin31415?" + link + "=" + player.getName()));
        }
    }

    private void assertHasGameOverLink(boolean hasLink, PlayerInfo... players) {
        assertHasLink(hasLink, "GameOver", "remove", players);
    }

    private void assertHasSaveLink(boolean hasLink, PlayerInfo... players) {
        assertHasLink(hasLink, "Save", "save", players);
    }

    private void assertUserRecordsIs(PlayerInfo... players) {
        List<String> names = collectValuesOf("//input[contains(@id,'name')]", "value");

        Iterator<PlayerInfo> info = Arrays.asList(players).iterator();
        for (String name : names) {
            assertEquals(info.next().getName(), name);
        }

        List<String> urls = collectValuesOf("//input[contains(@id,'callbackUrl')]", "value");

        info = Arrays.asList(players).iterator();
        for (String name : urls) {
            assertEquals(info.next().getCallbackUrl(), name);
        }
    }

    private List<String> collectValuesOf(String selector, String attributeName) {
        List<WebElement> elements = driver.findElements(By.xpath(selector));
        List<String> result = new LinkedList<String>();

        for (WebElement element: elements) {
            result.add(element.getAttribute(attributeName));
        }

        return result;
    }

    private PlayerInfo createSavedActiveUser(String name) {
        PlayerInfo user = createActiveUser(name);
        user.setSaved(true);
        return user;
    }

    private PlayerInfo createSavedUser(String name) {
        return new PlayerInfo(name, true);
    }

    private PlayerInfo createActiveUser(String name) {
        return new PlayerInfo(name, "http://" + name + ":8080");
    }

    private void atAdminPage() {
        driver.get(url);
        assertEquals("Admin page", driver.getTitle());
    }

    private void assertUserRecordsCount(int expected) {
        int trCount = driver.findElements(By.cssSelector("#savePlayersGame tr")).size();
        int headTrCount = 2;
        int count = (trCount == 0) ? 0 : (trCount - headTrCount);
        assertEquals(expected, count);
    }

}