package com.codenjoy.dojo.services.mocks;

import com.codenjoy.dojo.snake.model.artifacts.ArtifactGenerator;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

public class MockArtifactGenerator {
    @Bean(name = "artifactGenerator")
    public ArtifactGenerator bean() {
        return mock(ArtifactGenerator.class);
    }
}
