package com.codenjoy.dojo.config;

import com.codenjoy.dojo.services.GameServiceImpl;
import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.mocks.FirstSemifinalGameType;
import com.codenjoy.dojo.services.mocks.SecondSemifinalGameType;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.Collection;

@TestConfiguration
public class SemifinalGamesConfiguration {
    @Bean("gameService")
    public GameServiceImpl gameService() {
        return new GameServiceImpl() {
            @Override
            public Collection<? extends Class<? extends GameType>> findInPackage(String packageName) {
                return Arrays.asList(
                        FirstSemifinalGameType.class,
                        SecondSemifinalGameType.class
                );
            }
        };
    }
}
