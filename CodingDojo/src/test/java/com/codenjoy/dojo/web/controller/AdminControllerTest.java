package com.codenjoy.dojo.web.controller;

import com.codenjoy.dojo.services.TimerService;
import com.codenjoy.dojo.web.controller.AdminController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * User: apofig
 * Date: 9/20/12
 * Time: 2:21 PM
 */
@RunWith(MockitoJUnitRunner.class)
public class AdminControllerTest {
    public static final boolean GAME_PAUSED = false;
    public static final boolean GAME_ACTIVE = true;
    @Mock
    private TimerService timerService;

    private AdminController controller;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Before
    public void setUp() throws Exception {
        controller = new AdminController(timerService);
    }

    @Test
    public void shouldPauseGameWhenPauseGameAction() {
        when(timerService.isPaused()).thenReturn(GAME_PAUSED);

        String jsp = controller.pauseGame(model);

        assertEquals("admin", jsp);

        verify(model).addAttribute("paused", GAME_PAUSED);
        verify(timerService).pause();
    }

    @Test
    public void shouldResumeGameWhenResumeGameAction() {
        when(timerService.isPaused()).thenReturn(GAME_ACTIVE);

        String jsp = controller.resumeGame(model);

        assertEquals("admin", jsp);

        verify(model).addAttribute("paused", GAME_ACTIVE);
        verify(timerService).resume();
    }

    @Test
    public void shouldGetAdminPageWhenCallAdmin() {
        when(timerService.isPaused()).thenReturn(GAME_ACTIVE);

        String jsp = controller.getAdminPage(model);

        assertEquals("admin", jsp);

        verify(model).addAttribute("paused", GAME_ACTIVE);
    }
}
