package com.codenjoy.dojo.snake.services;

import com.codenjoy.dojo.services.ScreenSender;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

/**
 * User: serhiy.zelenin
 * Date: 5/13/12
 * Time: 10:46 PM
 */
public class MockScreenSenderConfiguration {

    @Bean(name = "screenSender")
    public ScreenSender screenSender() {
        return mock(ScreenSender.class);
    }
}
