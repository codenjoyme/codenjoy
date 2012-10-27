package net.tetris.online.service;

import net.tetris.services.PlayerController;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

/**
 * User: serhiy.zelenin
 * Date: 5/13/12
 * Time: 10:46 PM
 */
//@Configuration
public class MockReplayPlayerController {

    @Bean(name = "replayPlayerController")
    public PlayerController screenSender() throws Exception {
        return mock(PlayerController.class);
    }
}
