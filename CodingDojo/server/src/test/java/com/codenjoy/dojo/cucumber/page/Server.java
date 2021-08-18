package com.codenjoy.dojo.cucumber.page;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
@RequiredArgsConstructor
public class Server {

    @Value("${server.path}")
    private String path;

    public String path() {
        return path;
    }

    public String relative(String absolute) {
        int position = absolute.indexOf(path);
        if (position == 0) {
            return absolute.substring(path.length());
        } else {
            return absolute;
        }
    }
}
