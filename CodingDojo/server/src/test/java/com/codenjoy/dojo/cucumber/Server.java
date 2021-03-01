package com.codenjoy.dojo.cucumber;

import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class Server {

    private final String SERVER_URL = "http://localhost";
    private final String THINGS_ENDPOINT = "/codenjoy-contest";

    @LocalServerPort
    private int port;

    public String endpoint() {
        return SERVER_URL + ":" + port + THINGS_ENDPOINT;
    }
}
