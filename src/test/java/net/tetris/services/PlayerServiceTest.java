package net.tetris.services;


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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

//@ContextConfiguration(locations = {"classpath:/net/tetris/applicationContext.xml"})
@ContextConfiguration(classes = {PlayerService.class, MockScreenSenderConfiguration.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class PlayerServiceTest {
    private ArgumentCaptor<Map> screenSendCaptor;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private ScreenSender screenSender;

    @Before
    @SuppressWarnings("all")
    public void setUp() throws IOException {
        screenSendCaptor = ArgumentCaptor.forClass(Map.class);
    }

    @Test
    public void shouldSendCoordinatesToPlayerBoard() {
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


}
