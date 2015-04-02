package com.codenjoy.dojo.services.mocks;

import com.codenjoy.dojo.services.AutoSaver;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 20:21
 */
public class MockAutoSaver {
    @Bean(name = "autoSaver")
    public AutoSaver autoSaver() throws Exception {
        return mock(AutoSaver.class);
    }
}
