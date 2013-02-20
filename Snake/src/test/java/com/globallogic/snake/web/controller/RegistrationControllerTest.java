package com.globallogic.snake.web.controller;

import com.globallogic.snake.services.NullPlayer;
import com.globallogic.snake.services.Player;
import com.globallogic.snake.services.PlayerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * User: apofig
 * Date: 9/20/12
 * Time: 3:13 PM
 */
@RunWith(MockitoJUnitRunner.class)
public class RegistrationControllerTest {

    private RegistrationController controller;
    @Mock
    private Model model;
    @Mock
    private PlayerService playerService;
    @Mock
    private HttpServletRequest request;

    private ArgumentCaptor<Player> players = ArgumentCaptor.forClass(Player.class);

    @Before
    public void setUp() throws Exception {
        controller = new RegistrationController(playerService);
    }

    @Test
    public void shouldEnterUserIPAtRegistration() {
        when(request.getRemoteAddr()).thenReturn("IP");
        when(playerService.findPlayerByIp("IP")).thenReturn(new NullPlayer());

        String jsp = controller.openRegistrationForm(request, model);
        assertEquals("register", jsp);

        verify(model).addAttribute(eq("player"), players.capture());
        assertEquals("http://IP:8888", players.getValue().getCallbackUrl());
    }

    @Test
    public void shouldForbiddenRegistrationWhenUserAlreadyRegistered() {
        when(request.getRemoteAddr()).thenReturn("IP");
        Player player = new Player("vasia", "URL", null, null);
        when(playerService.findPlayerByIp("IP")).thenReturn(player);

        String jsp = controller.openRegistrationForm(request, model);
        assertEquals("already_registered", jsp);

        verify(model).addAttribute("user", "vasia");
        verify(model).addAttribute("url", "URL");
    }

    @Test
    public void shouldRemoveUserByIp() {
        when(request.getRemoteAddr()).thenReturn("IP");

        String jsp = controller.removeUserFromGame(request, model);
        assertEquals("redirect:/", jsp);

        verify(playerService).removePlayer("IP");
    }
}
