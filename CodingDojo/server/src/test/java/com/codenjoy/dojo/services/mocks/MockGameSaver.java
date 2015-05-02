package com.codenjoy.dojo.services.mocks;

import com.codenjoy.dojo.services.GameSaver;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

public class MockGameSaver {
    @Bean(name = "playerGameSaver")
    public GameSaver bean() throws Exception {
        return mock(GameSaver.class);
    }
}
