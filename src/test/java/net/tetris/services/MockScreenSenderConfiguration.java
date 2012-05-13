package net.tetris.services;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

/**
 * User: serhiy.zelenin
 * Date: 5/13/12
 * Time: 10:46 PM
 */
@Configuration
public class MockScreenSenderConfiguration {

    @Bean(name = "screenSender")
    public ScreenSender screenSender() {
        return mock(ScreenSender.class);
    }
}
