package com.codenjoy.dojo.services;

import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

/**
 * Created by indigo on 12.03.2015.
 */
public class MockRegistration {
    @Bean(name = "registration")
    public Registration registration() throws Exception {
        return mock(Registration.class);
    }
}
