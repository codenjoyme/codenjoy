package com.codenjoy.dojo.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * @author Igor_Petrov@epam.com
 * Created at 4/8/2019
 */
@Configuration
public class MVCConf implements WebMvcConfigurer {

    @Value("${mvc.cache-period}")
    private int cachePeriod;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/resources/**")
                .addResourceLocations("/resources/", "classpath:/resources/")
                .setCachePeriod(cachePeriod);
    }

    @Bean
    public ViewResolver internalResourceViewResolver() {
        return new InternalResourceViewResolver(){{
            setPrefix("/view/");
            setSuffix(".jsp");
            setRedirectHttp10Compatible(false);
        }};
    }
}
