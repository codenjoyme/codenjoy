package com.codenjoy.dojo.services.mocks;

import com.codenjoy.dojo.services.Statistics;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.net.URL;

import static org.mockito.Mockito.mock;

/**
 * Created by oleksandr.baglai on 23.06.2016.
 */
public class MockPropertyPlaceholderConfigurer {
    @Bean(name = "propertyPlaceholderConfigurer")
    public PropertyPlaceholderConfigurer bean() throws Exception {
        PropertyPlaceholderConfigurer configurer = new PropertyPlaceholderConfigurer();
        configurer.setLocation(new ClassPathResource("/com/codenjoy/dojo/server/codenjoy.properties"));
        return configurer;
    }
}
