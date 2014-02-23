package com.codenjoy.dojo.services.mocks;

import com.codenjoy.dojo.services.TimerService;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

/**
 * User: sanja
 * Date: 21.11.13
 * Time: 14:15
 */
public class MockTimerService {
    @Bean(name = "timerService")
    public TimerService gameSaver() throws Exception {
        return mock(TimerService.class);
    }
}
