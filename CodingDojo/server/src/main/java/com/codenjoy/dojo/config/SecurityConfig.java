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

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
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

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                    .authorizeRequests()
                .antMatchers("/admin*")
                    .hasRole("ADMIN")
                .antMatchers("/*")
                    .permitAll()
// TODO закончить с headers
//                .and()
//                    .headers()
//                        .
//        <http auto-config="true" use-expressions="true">
//            <intercept-url pattern="/**" access="permitAll"/>
//            <headers>
//                <hsts max-age-seconds="31536000"/>
//                <content-type-options/>
//                <header name="Content-Security-Policy"
//                    value="default-src 'self';
//                    script-src 'self' 'unsafe-eval' 'unsafe-inline' http://www.google-analytics.com;
//                    img-src 'self' data: http://www.google-analytics.com;
//                    connect-src 'self' ws: wss: http: https:;
//                    font-src 'self';
//                    style-src 'self' 'unsafe-inline';"/>
//                <xss-protection enabled="true" block="false"/>
//                <cache-control/>
//            </headers>
//            <csrf disabled="true"/>
//        </http>

                .and()
                    .formLogin()
                        .loginProcessingUrl("/admin")
                        .failureForwardUrl("/denied");

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
