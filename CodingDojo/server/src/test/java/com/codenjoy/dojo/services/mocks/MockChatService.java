package com.codenjoy.dojo.services.mocks;

import com.codenjoy.dojo.services.chat.ChatService;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

public class MockChatService {
    @Bean(name = "chatService")
    public ChatService bean() throws Exception {
        return mock(ChatService.class);
    }
}
