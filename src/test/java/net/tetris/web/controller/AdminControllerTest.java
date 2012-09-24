package net.tetris.web.controller;

import net.tetris.services.GameSettingsService;
import net.tetris.services.Player;
import net.tetris.services.TimerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.fest.util.Collections.list;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
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
    @Mock
    private GameSettingsService gameSettingsService;

    private AdminController controller;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Before
    public void setUp() throws Exception {
        controller = new AdminController(timerService, gameSettingsService);
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

    @Test
    public void shouldSaveSettingsOnServiceWhenSaveSettings() {
        String jsp = controller.saveSettings(adminSettings("level"), bindingResult, model);

        shouldGetAdminPageWhenCallAdmin();

        verify(gameSettingsService).setGameLevels("level");
    }

    private AdminSettings adminSettings(String levels) {
        AdminSettings result = new AdminSettings();
        result.setSelectedLevels(levels);
        return result;
    }

    @Test
    public void shouldPrepareSettingsWhenCallAdmin() {
        List<String> list = list("qwe", "asd");
        when(gameSettingsService.getGameLevelsList()).thenReturn(list);
        when(gameSettingsService.getGameLevels()).thenReturn("zxc");

        String jsp = controller.getAdminPage(model);

        verify(model).addAttribute("levelsList", list);
        ArgumentCaptor<AdminSettings> captor = ArgumentCaptor.forClass(AdminSettings.class);
        verify(model).addAttribute(eq("adminSettings"), captor.capture());
        assertEquals("zxc", captor.getValue().getSelectedLevels());
    }
}
