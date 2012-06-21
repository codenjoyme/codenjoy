package net.tetris.services;


import net.tetris.dom.Figure;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.intThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

//@ContextConfiguration(locations = {"classpath:/net/tetris/applicationContext.xml"})
@ContextConfiguration(classes = {PlayerService.class,
        MockScreenSenderConfiguration.class, MockPlayerController.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class PlayerServiceTest {
    private ArgumentCaptor<Map> screenSendCaptor;
    private ArgumentCaptor<Player> playerCaptor;
    private ArgumentCaptor<Integer> xCaptor;
    private ArgumentCaptor<Integer> yCaptor;
    private ArgumentCaptor<Figure.Type> figureCaptor;

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
        playerService.clear();
        Mockito.reset(playerController, screenSender);
    }

    @Test
    public void shouldSendCoordinatesToPlayerBoard() throws IOException {
        Player vasya = playerService.addNewPlayer("vasya", "http://localhost:1234");

        playerService.nextStepForAllGames();

        verify(screenSender).sendUpdates(screenSendCaptor.capture());
        Map<Player, List<Plot>> value = screenSendCaptor.getValue();
        assertEquals(1, value.size());
        List<Plot> plots = value.get(vasya);
        assertEquals(PlotColor.CYAN, plots.get(0).getColor());
        assertEquals(4, plots.get(0).getX());
        assertEquals(19, plots.get(0).getY());
    }

    @Test
    public void shouldRequestControlFromAllPlayers() throws IOException {
        playerService.addNewPlayer("vasya", "http://vasya:1234");
        playerService.addNewPlayer("petya", "http://petya:1234");

        playerService.nextStepForAllGames();

        verify(playerController, times(2)).requestControl(playerCaptor.capture(), figureCaptor.capture(),
                xCaptor.capture(), yCaptor.capture());

        assertHostsCaptured("http://vasya:1234", "http://petya:1234");
    }

    private void assertHostsCaptured(String ... hostUrls) {
        assertEquals(hostUrls.length, playerCaptor.getAllValues().size());
        for (int i = 0; i < hostUrls.length; i++) {
            String hostUrl = hostUrls[i];
            assertEquals(hostUrl, playerCaptor.getAllValues().get(i).getCallbackUrl());
        }
    }

}
