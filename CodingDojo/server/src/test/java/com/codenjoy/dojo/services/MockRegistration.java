package com.codenjoy.dojo.services;

import com.codenjoy.dojo.services.dao.Registration;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

public class MockRegistration {
    @Bean(name = "registration")
    public Registration registration() throws Exception {
        return mock(Registration.class);
    }
}
