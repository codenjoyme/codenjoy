package com.codenjoy.dojo.config;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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

import com.codenjoy.dojo.services.JarPathResourceResolver;
import com.codenjoy.dojo.services.TimerService;
import com.codenjoy.dojo.transport.auth.AuthenticationService;
import com.codenjoy.dojo.transport.chat.ChatWebSocketServlet;
import com.codenjoy.dojo.transport.control.ControlWebSocketServlet;
import com.codenjoy.dojo.transport.screen.ScreenWebSocketServlet;
import com.codenjoy.dojo.transport.ws.PlayerTransport;
import com.codenjoy.dojo.web.controller.advice.SystemControllerAdvice;
import lombok.SneakyThrows;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.CacheControl;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.servlet.ServletContext;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Configuration
public class MVCConf implements WebMvcConfigurer {

    public static final String RESOURCES_URI = "/resources/**";

    @Value("${mvc.cache-period}")
    private int cachePeriod;

    @Value("${server.servlet.context-path}")
    private String servletContextRoot;

    @Value("${plugins.resources}")
    private String pluginsResources;

    @Value("${plugins.static}")
    private String pluginsStatic;

    @Autowired
    private TimerService timer;

    @Autowired
    private PlayerTransport controlPlayerTransport;

    @Autowired
    private PlayerTransport screenPlayerTransport;

    @Autowired
    private PlayerTransport chatPlayerTransport;

    @Autowired
    private AuthenticationService secureAuthenticationService;

    @Autowired
    private AuthenticationService defaultAuthenticationService;

    @Autowired
    private SystemControllerAdvice controllerAdvice;

    @Bean
    public ResourceHttpRequestHandler resourceHttpRequestHandler(ServletContext servletContext) {
        return new ResourceHttpRequestHandler() {{
            setCacheControl(getCache());
            setLocations(locations(servletContext));
            setResourceResolvers(Arrays.asList(new JarPathResourceResolver()));
        }};
    }

    @SneakyThrows
    private List<Resource> locations(ServletContext servletContext) {
        List<Resource> result = new LinkedList<>();

        addLocationsForDebug(result);

        // production code
        result.add(new ServletContextResource(servletContext, "/resources/"));
        result.add(new ClassPathResource("classpath:/resources/"));
        result.add(new ClassPathResource("classpath*:**/resources/"));
        result.add(new UrlResource("file:" + pluginsStatic));
        result.add(new UrlResource("file:" + pluginsResources));

        return result;
    }

    @SneakyThrows
    private List<Resource> addLocationsForDebug(List<Resource> result) {
        if (!controllerAdvice.isDebugEnabled()) {
            return result;
        }

        // only for testing, so you can get the resources from the src/target folder
        result.add(new UrlResource("file:../games/*/src/main/**"));
        result.add(new UrlResource("file:src/main/**"));
        result.add(new UrlResource("file:target/classes/**"));

        return result;
    }

    private CacheControl getCache() {
        if (cachePeriod == -1) {
            return CacheControl.noCache();
        }

        return CacheControl.maxAge(cachePeriod, TimeUnit.SECONDS)
                .noTransform()
                .mustRevalidate();
    }

    @Bean
    public SimpleUrlHandlerMapping sampleServletMapping(){
        return new SimpleUrlHandlerMapping(){{
            setOrder(Integer.MAX_VALUE - 2);
            setMappings(new Properties(){{
                put(RESOURCES_URI, "resourceHttpRequestHandler");
            }});
        }};
    }

    @Bean
    public ViewResolver internalResourceViewResolver() {
        return new InternalResourceViewResolver(){{
            setPrefix("/view/");
            setSuffix(".jsp");
            setRedirectHttp10Compatible(false);
        }};
    }

    @Bean
    public ServletRegistrationBean wsControlServlet(@Value("${mvc.servlet-path.control}") String path) {
        WebSocketServlet servlet = new ControlWebSocketServlet(timer, controlPlayerTransport, secureAuthenticationService);

        return new ServletRegistrationBean<>(servlet, path){{
            setLoadOnStartup(100);
            setName("wsControlServlet");
        }};
    }

    @Bean
    public ServletRegistrationBean wsScreenServlet(@Value("${mvc.servlet-path.screen}") String path) {
        ScreenWebSocketServlet servlet = new ScreenWebSocketServlet(screenPlayerTransport, defaultAuthenticationService);

        return new ServletRegistrationBean<>(servlet, path){{
            setLoadOnStartup(100);
            setName("wsScreenServlet");
        }};
    }

    @Bean
    public ServletRegistrationBean wsChatServlet(@Value("${mvc.servlet-path.chat}") String path) {
        ChatWebSocketServlet servlet = new ChatWebSocketServlet(chatPlayerTransport, secureAuthenticationService);

        return new ServletRegistrationBean<>(servlet, path){{
            setLoadOnStartup(100);
            setName("wsChatServlet");
        }};
    }
}
