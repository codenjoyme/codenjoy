package com.codenjoy.dojo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author Igor_Petrov@epam.com
 * Created at 4/8/2019
 */
@SpringBootApplication
@ServletComponentScan
public class BalancerApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(BalancerApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(BalancerApplication.class, args);
    }
}
