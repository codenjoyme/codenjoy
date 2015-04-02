package com.codenjoy.dojo.services.mocks;

import com.codenjoy.dojo.services.PlayerService;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 20:12
 */
public class MockPlayerService {
    @Bean(name = "playerService")
    public PlayerService playerService() throws Exception {
        return mock(PlayerService.class);
    }
}
