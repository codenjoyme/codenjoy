package com.codenjoy.dojo.web.rest;

import com.codenjoy.dojo.client.CodenjoyContext;
import com.codenjoy.dojo.services.ConfigProperties;
import com.codenjoy.dojo.services.GameServiceImpl;
import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.hash.Hash;
import com.codenjoy.dojo.services.mocks.FirstGameType;
import com.codenjoy.dojo.services.mocks.SecondGameType;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Collection;

public abstract class AbstractRestControllerTest {

    public static GameServiceImpl gameService() {
        return new GameServiceImpl(){
            @Override
            public Collection<? extends Class<? extends GameType>> findInPackage(String packageName) {
                return Arrays.asList(FirstGameType.class, SecondGameType.class);
            }
        };
    }

    protected MockMvc mvc;

    @Autowired
    protected WebApplicationContext context;

    @Autowired
    private ConfigProperties config;

    @Before
    public void setUp() {
        CodenjoyContext.setContext("codenjoy-contest");
        mvc = MockMvcBuilders.webAppContextSetup(context).build();

        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(
                        config.getAdminLogin(),
                        Hash.md5(config.getAdminPassword()))
                );
    }

}
