package com.codenjoy.dojo.services.mocks;

import com.codenjoy.dojo.services.ActionLogger;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

/**
 * User: sanja
 * Date: 11.01.14
 * Time: 3:14
 */
public class MockActionLogger {
    @Bean(name = "actionLogger")
    public ActionLogger actionLogger() throws Exception {
        return mock(ActionLogger.class);
    }
}
