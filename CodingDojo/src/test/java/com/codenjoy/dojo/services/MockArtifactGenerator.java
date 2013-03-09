package com.codenjoy.dojo.services;

import com.codenjoy.dojo.snake.model.artifacts.ArtifactGenerator;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

/**
 * User: oleksandr.baglai
 * Date: 10/1/12
 * Time: 8:26 AM
 */
public class MockArtifactGenerator {

    @Bean(name = "artifactGenerator")
    public ArtifactGenerator artifactGenerator() {
        return mock(ArtifactGenerator.class);
    }
}
