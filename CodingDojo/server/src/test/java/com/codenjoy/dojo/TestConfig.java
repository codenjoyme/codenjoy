package com.codenjoy.dojo;

import com.codenjoy.dojo.services.GameSaver;
import com.codenjoy.dojo.services.GameService;
import com.codenjoy.dojo.services.GameServiceImpl;
import com.codenjoy.dojo.services.SaveServiceImpl;
import com.codenjoy.dojo.services.dao.FeedbackSaver;
import com.codenjoy.dojo.services.dao.PlayerGameSaver;
import com.codenjoy.dojo.services.dao.SubscriptionSaver;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class TestConfig {

    @Bean
    @Primary
    public SubscriptionSaver subscriptionSaver() {
        return Mockito.mock(SubscriptionSaver.class);
    }

    @Bean
    @Primary
    public FeedbackSaver feedbackSaver() {
        return Mockito.mock(FeedbackSaver.class);
    }

    @Bean
    @Primary
    public SaveServiceImpl saveServiceImpl() {
        return Mockito.mock(SaveServiceImpl.class);
    }

    @Bean
    @Primary
    public GameService gameServiceImpl(){
        return new GameServiceImpl();
    }
}
