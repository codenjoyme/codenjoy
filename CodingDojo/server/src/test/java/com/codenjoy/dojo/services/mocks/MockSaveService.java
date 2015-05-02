package com.codenjoy.dojo.services.mocks;

import com.codenjoy.dojo.services.SaveService;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

public class MockSaveService {
    @Bean(name = "saveService")
    public SaveService bean() throws Exception {
        return mock(SaveService.class);
    }
}
