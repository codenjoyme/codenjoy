package net.tetris.services;

import net.tetris.dom.FigureQueue;
import net.tetris.dom.GlassEvent;
import net.tetris.dom.Levels;
import net.tetris.services.levels.MockLevels;
import org.springframework.context.annotation.Bean;

import java.util.List;

import static net.tetris.dom.Figure.Type.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * User: oleksandr.baglai
 * Date: 9/24/12
 * Time: 6:37 PM
 */
public class MockGameSettingsService {

    @Bean(name = "gameSettingsService")
    public GameSettings gameSettings() throws Exception {
        GameSettings mock = mock(GameSettings.class);
        when(mock.getCurrentGameLevels()).thenReturn(MockLevels.class.getSimpleName());
        return mock;
    }
}
