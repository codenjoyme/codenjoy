package com.codenjoy.dojo.config;

import com.codenjoy.dojo.services.GameServiceImpl;
import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.mocks.FirstGameType;
import com.codenjoy.dojo.services.mocks.SecondGameType;
import com.codenjoy.dojo.services.mocks.ThirdGameType;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.Collection;

@TestConfiguration
public class ThreeGamesConfiguration {
    @Bean("gameService")
    public GameServiceImpl gameService() {
        return new GameServiceImpl() {
            @Override
            public Collection<? extends Class<? extends GameType>> findInPackage(String packageName) {
                return Arrays.asList(
                        FirstGameType.class,
                        SecondGameType.class,
                        ThirdGameType.class
                );
            }
        };
    }
}
