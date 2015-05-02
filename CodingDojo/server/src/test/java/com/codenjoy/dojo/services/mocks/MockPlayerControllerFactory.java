package com.codenjoy.dojo.services.mocks;

import com.codenjoy.dojo.services.PlayerControllerFactory;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

public class MockPlayerControllerFactory {
    @Bean(name = "playerController")
    public PlayerControllerFactory bean() throws Exception {
        return mock(PlayerControllerFactory.class);
    }
}
