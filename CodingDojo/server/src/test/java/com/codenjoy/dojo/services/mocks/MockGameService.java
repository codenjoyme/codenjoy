package com.codenjoy.dojo.services.mocks;

import com.codenjoy.dojo.services.GameService;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

public class MockGameService {
    @Bean(name = "gameService")
    public GameService bean() throws Exception {
        return mock(GameService.class);
    }
}
