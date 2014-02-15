package com.codenjoy.dojo.services.mocks;

import com.codenjoy.dojo.services.PlayerGames;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

/**
 * Created by Sanja on 15.02.14.
 */
public class SpyPlayerGames {
    @Bean(name = "playerGames")
    public PlayerGames playerGames() throws Exception {
        return spy(new PlayerGames());
    }
}
