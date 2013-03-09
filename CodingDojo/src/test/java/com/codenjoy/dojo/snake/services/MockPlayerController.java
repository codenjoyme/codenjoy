package com.codenjoy.dojo.snake.services;

import com.codenjoy.dojo.services.PlayerController;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

/**
 * User: serhiy.zelenin
 * Date: 5/13/12
 * Time: 10:46 PM
 */
//@Configuration
public class MockPlayerController {

    @Bean(name = "playerController")
    public PlayerController screenSender() throws Exception {
        return mock(PlayerController.class);
    }
}
