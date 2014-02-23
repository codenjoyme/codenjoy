package com.codenjoy.dojo.services.mocks;

import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

/**
 * User: serhiy.zelenin
 * Date: 5/13/12
 * Time: 10:46 PM
 */
public class MockScreenSenderConfiguration {

    @Bean(name = "screenSender")
    public com.codenjoy.dojo.transport.screen.ScreenSender screenSender() {
        return mock(com.codenjoy.dojo.transport.screen.ScreenSender.class);
    }
}
