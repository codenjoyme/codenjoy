package com.codenjoy.dojo.services.mocks;

import com.codenjoy.dojo.services.GameSaver;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

/**
 * User: oleksandr.baglai
 * Date: 3/22/13
 * Time: 11:37 PM
 */
public class MockGameSaver {

    @Bean(name = "gameSaver")
    public GameSaver gameSaver() throws Exception {
        return mock(GameSaver.class);
    }
}
