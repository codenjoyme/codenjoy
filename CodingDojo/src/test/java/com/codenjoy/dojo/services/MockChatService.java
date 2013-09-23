package com.codenjoy.dojo.services;

import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

/**
 * User: sanja
 * Date: 23.09.13
 * Time: 23:50
 */
public class MockChatService {
    @Bean(name = "chatService")
    public ChatService chatService() throws Exception {
        return mock(ChatService.class);
    }
}
