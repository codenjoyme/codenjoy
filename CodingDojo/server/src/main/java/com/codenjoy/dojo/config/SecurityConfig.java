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

import com.codenjoy.dojo.config.meta.NonSSOProfile;
import com.codenjoy.dojo.config.meta.SSOProfile;
import com.codenjoy.dojo.config.oauth2.OAuth2MappingUserService;
import com.codenjoy.dojo.web.controller.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.annotation.PostConstruct;

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
    private static final String ADMIN_LOGIN_PROCESSING_URI = "/process_admin_login";
    private static final String LOGOUT_PROCESSING_URI = "/process_logout";

    public static final String[] UNAUTHORIZED_URIS = {
            LoginController.ADMIN_URI,
            RegistrationController.URI + "*",
            LOGIN_PROCESSING_URI,
            ADMIN_LOGIN_PROCESSING_URI,
            MVCConf.RESOURCES_URI,
            ErrorController.URI,
            GameDataController.URI + "/**",
    };

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

    @NonSSOProfile
    @Configuration
    @Order(BEFORE_DEFAULT_SEC_CONFIG_PRECEDENCE)
    @Slf4j
    public static class FormLoginSecurityConf extends WebSecurityConfigurerAdapter {

        @PostConstruct
        void info() {
            log.warn("Running server with form-based authentication");
        }

        @Autowired
        private AuthenticationSuccessHandler authenticationSuccessHandler;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // @formatter:off
            http
                    .authorizeRequests()
                        .antMatchers(UNAUTHORIZED_URIS)
                            .permitAll()

                        .anyRequest()
                            .hasRole("USER")

                    .and()
                        .formLogin()
                            .loginPage(LoginController.URI)
                                .loginProcessingUrl(LOGIN_PROCESSING_URI)
                                    .permitAll()
                                .usernameParameter(USERNAME_FORM_PARAMETER)
                                .passwordParameter(PASSWORD_FORM_PARAMETER)
                                .successHandler(authenticationSuccessHandler)
                                .failureUrl(LoginController.URI + "?failed=true")
                            .permitAll()
                    .and()
                        .logout()
                            .logoutUrl(LOGOUT_PROCESSING_URI)
                            .logoutRequestMatcher(new AntPathRequestMatcher(LOGOUT_PROCESSING_URI))
                            .invalidateHttpSession(true)
                    .and()
                    .csrf().disable();
            // @formatter:on
        }
    }

    @SSOProfile
    @Configuration
    @Order(BEFORE_DEFAULT_SEC_CONFIG_PRECEDENCE)
    @Slf4j
    public static class SSOUserSecurityConf extends WebSecurityConfigurerAdapter {

        @Autowired
        private OAuth2MappingUserService oAuth2MappingUserService;

        @PostConstruct
        void info() {
            log.warn("Running server with OAuth2 authorization");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // @formatter:off
            http
                    .authorizeRequests()
                    .antMatchers(UNAUTHORIZED_URIS)
                            .permitAll()

                        .anyRequest()
                            .hasRole("USER")

                    .and()
                        .oauth2Login()
                            .userInfoEndpoint()
                                .userService(oAuth2MappingUserService)
                        .and()
                    .and()
                        .logout()
                            .logoutUrl(LOGOUT_PROCESSING_URI)
                            .logoutRequestMatcher(new AntPathRequestMatcher(LOGOUT_PROCESSING_URI))
                            .invalidateHttpSession(true)
                    .and()
                    .csrf().disable();
            // @formatter:on
        }
    }

    @SSOProfile
    @Configuration
    public static class ResourceServerSSOConf extends ResourceServerConfigurerAdapter {

        @Override
        public void configure(ResourceServerSecurityConfigurer config) {
            config.tokenServices(tokenServices());
        }

        @Bean
        public TokenStore tokenStore() {
            return new JwtTokenStore(accessTokenConverter());
        }

        @Bean
        public JwtAccessTokenConverter accessTokenConverter() {
            JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
            converter.setSigningKey("123");
            return converter;
        }

        @Bean
        @Primary
        public DefaultTokenServices tokenServices() {
            DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
            defaultTokenServices.setTokenStore(tokenStore());
            return defaultTokenServices;
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
                            .permitAll()
                            .defaultSuccessUrl(AdminController.URI)
                                .permitAll()
                    .and()
                        .logout()
                            .logoutUrl(LOGOUT_PROCESSING_URI)
                            .logoutRequestMatcher(new AntPathRequestMatcher(LOGOUT_PROCESSING_URI))
                            .invalidateHttpSession(true)
                    .and()
                        .exceptionHandling()
                            .accessDeniedHandler((request, response, accessDeniedException) ->
                                    response.sendRedirect(request.getContextPath()
                                            + "/error?message=Page access is restricted"));
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
