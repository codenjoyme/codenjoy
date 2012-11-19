package net.tetris.services;

import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

/**
 * User: oleksandr.baglai
 * Date: 11/19/12
 * Time: 4:11 AM
 */
public class MockGameSaver {

    @Bean(name = "playerGameSaver")
    public GameSaver playerGameSaver() throws Exception {
        return mock(GameSaver.class);
    }

}
