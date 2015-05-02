package com.codenjoy.dojo.services.mocks;

import com.codenjoy.dojo.services.PlayerService;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

public class MockPlayerService {
    @Bean(name = "playerService")
    public PlayerService bean() throws Exception {
        return mock(PlayerService.class);
    }
}
