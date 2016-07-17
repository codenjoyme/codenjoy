package net.tetris.services;

import com.codenjoy.dojo.tetris.model.MockLevels;
import org.springframework.context.annotation.Bean;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
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
