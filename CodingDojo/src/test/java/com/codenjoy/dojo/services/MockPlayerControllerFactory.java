package com.codenjoy.dojo.services;

import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

/**
 * User: serhiy.zelenin
 * Date: 5/13/12
 * Time: 10:46 PM
 */
//@Configuration
public class MockPlayerControllerFactory {

    @Bean(name = "playerController")
    public PlayerControllerFactory screenSender() throws Exception {
        return mock(PlayerControllerFactory.class);
    }
}
