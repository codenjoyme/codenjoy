package com.codenjoy.dojo.snake.web.controller;

import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.snake.services.PlayerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * User: apofig
 * Date: 9/20/12
 * Time: 3:38 PM
 */
@RunWith(MockitoJUnitRunner.class)
public class MainControllerTest {

    private MainPageController controller;
    @Mock
    private Model model;
    @Mock
    private PlayerService playerService;
    @Mock
    private HttpServletRequest request;

    @Before
    public void setUp() throws Exception {
        controller = new MainPageController(playerService);
    }

    @Test
    public void shouldGetMainPage() {
        when(request.getRemoteAddr()).thenReturn("IP");

        Player player = new Player();
        player.setName("player");
        when(playerService.findPlayerByIp("IP")).thenReturn(player);

        String jsp = controller.getMainPage(request, model);
        assertEquals("main", jsp);

        verify(model).addAttribute("ip", "IP");
        verify(model).addAttribute("user", "player");
    }

}
