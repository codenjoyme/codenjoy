package net.tetris.online.service;

import net.tetris.services.PlayerController;
import org.junit.After;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.io.IOException;

import static org.mockito.Mockito.mock;

/**
 * User: serhiy.zelenin
 * Date: 5/13/12
 * Time: 10:46 PM
 */
//@Configuration
public class MockServiceConfiguration {

    private ServiceConfigFixture fixture;

    @PostConstruct
    public void setUp() {
        fixture = new ServiceConfigFixture();
        fixture.setup();
    }

    @PreDestroy
    public void tearDown() throws IOException {
        fixture.tearDown();

    }

    @Bean(name = "configuration")
    public ServiceConfiguration screenSender() throws Exception {
        return fixture.getConfiguration();
    }
}
