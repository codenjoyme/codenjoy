package net.tetris.services;

import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

/**
 * User: serhiy.zelenin
 * Date: 5/13/12
 * Time: 10:46 PM
 */
//@Configuration
public class MockPlayerService {

    @Bean(name = "playerService")
    public PlayerService playerService() throws Exception {
        return mock(PlayerService.class);
    }
}
