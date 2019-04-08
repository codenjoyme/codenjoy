package com.codenjoy.dojo.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * @author Igor_Petrov@epam.com
 * Created at 4/8/2019
 */
@EnableWebSecurity
public class SecurityConf extends WebSecurityConfigurerAdapter {

    @Value("${admin.login}")
    private String adminLogin;

    @Value("${admin.password}")
    private String adminPassword;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        User.UserBuilder userBuilder = User.builder()
                .passwordEncoder(pwd -> passwordEncoder().encode(pwd));
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(userBuilder.username(adminLogin).password(adminPassword).roles("ADMIN").build());
        return manager;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .antMatcher("/**")
                    .authorizeRequests()
                        .anyRequest()
                            .permitAll()
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
                    .httpBasic()
                .and()
                    .formLogin()
                        .permitAll()
                .and()
                .csrf().disable();

    }
}
