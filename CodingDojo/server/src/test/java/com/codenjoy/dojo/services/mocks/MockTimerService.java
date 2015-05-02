package com.codenjoy.dojo.services.mocks;

import com.codenjoy.dojo.services.TimerService;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

public class MockTimerService {
    @Bean(name = "timerService")
    public TimerService bean() throws Exception {
        return mock(TimerService.class);
    }
}
