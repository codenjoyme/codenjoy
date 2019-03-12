package com.codenjoy.dojo.config;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.codenjoy.dojo.services.TimerService;
import com.codenjoy.dojo.transport.auth.AuthenticationService;
import com.codenjoy.dojo.transport.control.ControlWebSocketServlet;
import com.codenjoy.dojo.transport.screen.ws.ScreenWebSocketServlet;
import com.codenjoy.dojo.transport.ws.PlayerTransport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * @author Igor_Petrov@epam.com
 * Created at 3/6/2019
 */
@Configuration
public class MVCConf implements WebMvcConfigurer {

    @Value("${mvc.cache-period}")
    private int cachePeriod;

    @Autowired
    private TimerService timer;

    @Autowired
    private PlayerTransport controlPlayerTransport;

    @Autowired
    private PlayerTransport screenPlayerTransport;

    @Autowired
    private AuthenticationService secureAuthenticationService;

    @Autowired
    private AuthenticationService defaultAuthenticationService;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/resources/**")
                    .addResourceLocations("/resources/", "classpath:/resources/")
                    .setCachePeriod(cachePeriod);

//        registry
//                .addResourceHandler("/favicon.ico")
//                    .addResourceLocations("/resources/favicon.ico")
//                    .setCachePeriod(cachePeriod);

    }

    @Bean
    public ViewResolver internalResourceViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/view/");
        viewResolver.setSuffix(".jsp");
        viewResolver.setRedirectHttp10Compatible(false);
        return viewResolver;
    }

    @Bean
    public ServletRegistrationBean wsControlServlet(@Value("${mvc.control-servlet-path}") String path) {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean<>(new ControlWebSocketServlet(timer, controlPlayerTransport, secureAuthenticationService), path);
        servletRegistrationBean.setLoadOnStartup(100);
        servletRegistrationBean.setName("wsControlServlet");
        return servletRegistrationBean;
    }

    @Bean
    public ServletRegistrationBean wsScreenServlet(@Value("${mvc.screen-servlet-path}") String path) {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean<>(new ScreenWebSocketServlet(screenPlayerTransport, defaultAuthenticationService), path);
        servletRegistrationBean.setLoadOnStartup(100);
        servletRegistrationBean.setName("wsScreenServlet");
        return servletRegistrationBean;
    }
}
