package com.codenjoy.console;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @RequestMapping("/codenjoy-console")
    public String doit(@RequestParam(name = "action") String action) {
        RestartCodenjoyServer console = new RestartCodenjoyServer(login, password);

        if (console.login()) {

            if (StringUtils.isEmpty(action) || action.equals("restart")) {
                console.restart();
            } else if (action.equals("start")) {
                console.start();
            } else if (action.equals("stop")) {
                console.stop();
            } else {
                return "unexpected action: " + action;
            }

            console.logout();
            return "success: " + action;
        } else {
            return "fail: " + action;
        }
    }
}
