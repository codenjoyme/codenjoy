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

import com.codenjoy.dojo.web.controller.AdminController;
import com.codenjoy.dojo.web.controller.LoginController;
import com.codenjoy.dojo.web.controller.RegistrationController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author Igor_Petrov@epam.com
 * Created at 3/6/2019
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final int BEFORE_DEFAULT_SEC_CONFIG_PRECEDENCE = 99;
    private static final int PRE_BEFORE_DEFAULT_SEC_CONFIG_PRECEDENCE = 98;
    private static final int HIGHEST_SEC_CONFIG_PRECEDENCE = 97;
    private static final String USERNAME_FORM_PARAMETER = "email";
    private static final String PASSWORD_FORM_PARAMETER = "password";

    private static final String LOGIN_PROCESSING_URI = "/process_login";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.cors()
                .and()
                    .headers()
                        .httpStrictTransportSecurity().maxAgeInSeconds(31536000)
                    .and()
                        .contentTypeOptions()
                    .and()
                        .contentSecurityPolicy(
                                "default-src 'self';" +
                                "script-src 'self' 'unsafe-eval' 'unsafe-inline' http://www.google-analytics.com;" +
                                "img-src 'self' data: http://www.google-analytics.com;" +
                                "connect-src 'self' ws: wss: http: https:;" +
                                "font-src 'self';" +
                                "style-src 'self' 'unsafe-inline';")
                    .and()
                .and()
                    .csrf().disable();
        // @formatter:on
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Configuration
    @Order(BEFORE_DEFAULT_SEC_CONFIG_PRECEDENCE)
    public static class UserSecurityConf extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // @formatter:off
            http
                    .authorizeRequests()
                        .antMatchers(LoginController.ADMIN_URI, RegistrationController.URI + "*")
                            .permitAll()

                        .anyRequest()
                            .hasRole("USER")

                        .antMatchers(LOGIN_PROCESSING_URI)
                            .permitAll()
                    .and()
                        .formLogin()
                            .loginPage(LoginController.URI)
                                .loginProcessingUrl(LOGIN_PROCESSING_URI)
                                    .permitAll()
                                .usernameParameter(USERNAME_FORM_PARAMETER)
                                .passwordParameter(PASSWORD_FORM_PARAMETER)
                            .permitAll()
                    .and()
                    .csrf().disable();
            // @formatter:on
        }
    }

    @Configuration
    @Order(PRE_BEFORE_DEFAULT_SEC_CONFIG_PRECEDENCE)
    public static class AdminSecurityConf extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // @formatter:off
            http
                    .antMatcher(AdminController.URI + "*")
                        .authorizeRequests()
                            .anyRequest()
                                .hasRole("ADMIN")
                    .and()
                        .formLogin()
                            .loginPage(LoginController.ADMIN_URI)
                                .usernameParameter(USERNAME_FORM_PARAMETER)
                                .passwordParameter(PASSWORD_FORM_PARAMETER)
                            .permitAll();
            // @formatter:on
        }
    }

    @Configuration
    @Order(HIGHEST_SEC_CONFIG_PRECEDENCE)
    public static class WebSocketSecurityConfig extends WebSecurityConfigurerAdapter {

        @Value("${mvc.control-servlet-path}")
        private String controlWsURI;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // @formatter:off
            http
                    .antMatcher(controlWsURI + "*")
                        .authorizeRequests()
                            .anyRequest().permitAll();
            // @formatter:on
        }
    }

    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(
            new UrlBasedCorsConfigurationSource(){{
                registerCorsConfiguration("/**",
                    new CorsConfiguration(){{
                        setAllowCredentials(true);
                        addAllowedOrigin("*");
                        addAllowedHeader("*");
                        addAllowedMethod("*");
                    }});
            }});
    }
}
