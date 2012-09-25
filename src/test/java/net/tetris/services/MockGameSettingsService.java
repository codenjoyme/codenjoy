package net.tetris.services;

import net.tetris.dom.GlassEvent;
import net.tetris.dom.Levels;
import org.springframework.context.annotation.Bean;

import java.util.List;

import static net.tetris.dom.Figure.Type.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

/**
 * User: oleksandr.baglai
 * Date: 9/24/12
 * Time: 6:37 PM
 */
public class MockGameSettingsService {

    @Bean(name = "gameSettingsService")
    public GameSettings screenSender() throws Exception {
        return spy(new MockGameSettings());
    }

    class MockGameSettings implements GameSettings   {

        @Override
        public Levels getGameLevels() {
            return new Levels(new FigureTypesLevel(
                    new GlassEvent<>(GlassEvent.Type.LINES_REMOVED, 4), I));
        }

        @Override
        public void setGameLevels(String levelSettings) {
        }

        @Override
        public String getCurrentGameLevels() {
            return null;
        }

        @Override
        public List<String> getGameLevelsList() {
            return null;
        }
    }
}
