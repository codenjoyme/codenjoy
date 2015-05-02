package com.codenjoy.dojo.services.mocks;

import com.codenjoy.dojo.transport.screen.ScreenSender;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

public class MockScreenSenderConfiguration {
    @Bean(name = "screenSender")
    public ScreenSender bean() {
        return mock(ScreenSender.class);
    }
}
