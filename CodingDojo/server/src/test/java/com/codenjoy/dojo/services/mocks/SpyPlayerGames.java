package com.codenjoy.dojo.services.mocks;

import com.codenjoy.dojo.services.PlayerGames;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.spy;

public class SpyPlayerGames {
    @Bean(name = "playerGames")
    public PlayerGames bean() throws Exception {
        return spy(new PlayerGames());
    }
}
