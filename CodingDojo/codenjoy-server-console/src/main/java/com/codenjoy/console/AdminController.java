package com.codenjoy.console;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by indigo on 2017-02-26.
 */
@RestController
public class AdminController {

    @Value("${console.login}")
    private String login;

    @Value("${console.password}")
    private String password;

    @RequestMapping("/codenjoy-restart")
    public String restart() {
        RestartCodenjoyServer console = new RestartCodenjoyServer(login, password);

        if (console.login()) {
            console.restart();
            console.logout();
            return "success";
        } else {
            return "fail";
        }
    }
}
