package com.codenjoy.dojo.services.mocks;

import com.codenjoy.dojo.services.Statistics;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

public class MockStatistics {
    @Bean(name = "statistics")
    public Statistics bean() throws Exception {
        return mock(Statistics.class);
    }
}
