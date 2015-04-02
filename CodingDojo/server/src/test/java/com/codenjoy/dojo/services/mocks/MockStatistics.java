package com.codenjoy.dojo.services.mocks;

import com.codenjoy.dojo.services.Statistics;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

/**
 * Created by Sanja on 15.02.14.
 */
public class MockStatistics {
    @Bean(name = "statistics")
    public Statistics statistics() throws Exception {
        return mock(Statistics.class);
    }
}
