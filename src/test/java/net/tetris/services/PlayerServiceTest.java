package net.tetris.services;


import net.tetris.dom.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static net.tetris.dom.TestUtils.HEIGHT;
import static net.tetris.dom.TestUtils.assertContainsPlot;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ContextConfiguration(classes = {PlayerService.class,
        MockScreenSenderConfiguration.class, MockPlayerController.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class PlayerServiceTest {
    private ArgumentCaptor<Map> screenSendCaptor;
    private ArgumentCaptor<Player> playerCaptor;
    private ArgumentCaptor<Integer> xCaptor;
    private ArgumentCaptor<Integer> yCaptor;
    private ArgumentCaptor<Figure.Type> figureCaptor;
    private ArgumentCaptor<List> plotsCaptor;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private ScreenSender screenSender;

    @Autowired
    private PlayerController playerController;

    @Before
    @SuppressWarnings("all")
    public void setUp() throws IOException {
        screenSendCaptor = ArgumentCaptor.forClass(Map.class);
        playerCaptor = ArgumentCaptor.forClass(Player.class);
        xCaptor = ArgumentCaptor.forClass(Integer.class);
        yCaptor = ArgumentCaptor.forClass(Integer.class);
        figureCaptor = ArgumentCaptor.forClass(Figure.Type.class);
        plotsCaptor = ArgumentCaptor.forClass(List.class);

        playerService.clear();
        Mockito.reset(playerController, screenSender);
    }

    @Test
    public void shouldSendCoordinatesToPlayerBoard() throws IOException {
        Player vasya = playerService.addNewPlayer("vasya", "http://localhost:1234");

        playerService.nextStepForAllGames();

        assertSentToPlayers(vasya);
        List<Plot> plots = getPlotsFor(vasya);
        assertContainsPlot(4, 19, plots);
    }

    @Test
    public void shouldReturnPlotsOfCurrentAndDroppedFigures() {
        Player vasya = playerService.addNewPlayer("vasya", "http://localhost:1234");
        forceDropFigureInGlass(3, HEIGHT, new TetrisFigure());

        playerService.nextStepForAllGames();

        assertSentToPlayers(vasya);
        List<Plot> plots = getPlotsFor(vasya);
        assertContainsPlot(3, 0, PlotColor.BLUE, plots);
    }

    @Test
    public void shouldRequestControlFromAllPlayers() throws IOException {
        Player vasya = playerService.addNewPlayer("vasya", "http://vasya:1234");
        Player petya = playerService.addNewPlayer("petya", "http://petya:1234");

        playerService.nextStepForAllGames();

        assertSentToPlayers(vasya, petya);
        verify(playerController, times(2)).requestControl(playerCaptor.capture(), figureCaptor.capture(),
                xCaptor.capture(), yCaptor.capture(), Matchers.<Joystick>any(), plotsCaptor.capture());

        assertHostsCaptured("http://vasya:1234", "http://petya:1234");
    }

    @Test
    public void shouldRequestControlFromAllPlayersWithGlassState() throws IOException {
        playerService.addNewPlayer("vasya", "http://vasya:1234");
        forceDropFigureInGlass(0, HEIGHT, new TetrisFigure());

        playerService.nextStepForAllGames();

        verify(playerController).requestControl(playerCaptor.capture(), figureCaptor.capture(),
                xCaptor.capture(), yCaptor.capture(), Matchers.<Joystick>any(), plotsCaptor.capture());
        List<Plot> sentPlots = plotsCaptor.getValue();
        assertEquals(1, sentPlots.size());
        assertContainsPlot(0, 0, PlotColor.BLUE, sentPlots);
    }

    private List<Plot> getPlotsFor(Player vasya) {
        Map<Player, PlayerData> value = screenSendCaptor.getValue();
        return value.get(vasya).getPlots();
    }

    private void assertSentToPlayers(Player ... players) {
        verify(screenSender).sendUpdates(screenSendCaptor.capture());
        Map sentScreens = screenSendCaptor.getValue();
        assertEquals(players.length, sentScreens.size());
        for (Player player : players) {
            assertTrue(sentScreens.containsKey(player));
        }
    }

    private void forceDropFigureInGlass(int x, int y, TetrisFigure point) {
        Glass glass = playerService.getGlasses().get(0);
        glass.drop(point, x, y);
    }

    private void assertHostsCaptured(String ... hostUrls) {
        assertEquals(hostUrls.length, playerCaptor.getAllValues().size());
        for (int i = 0; i < hostUrls.length; i++) {
            String hostUrl = hostUrls[i];
            assertEquals(hostUrl, playerCaptor.getAllValues().get(i).getCallbackUrl());
        }
    }

}
