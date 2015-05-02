package com.codenjoy.dojo.services.mocks;

import com.codenjoy.dojo.services.AutoSaver;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

public class MockAutoSaver {
    @Bean(name = "autoSaver")
    public AutoSaver bean() throws Exception {
        return mock(AutoSaver.class);
    }
}
