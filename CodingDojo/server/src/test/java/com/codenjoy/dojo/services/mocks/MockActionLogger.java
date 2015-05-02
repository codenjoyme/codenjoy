package com.codenjoy.dojo.services.mocks;

import com.codenjoy.dojo.services.dao.ActionLogger;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

public class MockActionLogger {
    @Bean(name = "actionLogger")
    public ActionLogger bean() throws Exception {
        return mock(ActionLogger.class);
    }
}
