package com.codenjoy.dojo.services.mocks;

import com.codenjoy.dojo.services.SaveService;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 20:33
 */
public class MockSaveService {
    @Bean(name = "saveService")
    public SaveService saveService() throws Exception {
        return mock(SaveService.class);
    }
}
