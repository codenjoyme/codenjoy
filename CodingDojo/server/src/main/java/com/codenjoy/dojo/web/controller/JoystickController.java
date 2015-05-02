package com.codenjoy.dojo.web.controller;

import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.dao.Registration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/joystick")
public class JoystickController {

    @Autowired private Registration registration;
    @Autowired private PlayerService playerService;

    @RequestMapping(method = RequestMethod.GET)
    public String joystick(@RequestParam("playerName") String playerName,
                           @RequestParam("code") String code,
                           @RequestParam("command") String command)
    {
        Player registeredPlayer = playerService.get(registration.getEmail(code));
        if (registeredPlayer == NullPlayer.INSTANCE || !registeredPlayer.getName().equals(playerName)) {
            return "fail";
        }
        Joystick joystick = playerService.getJoystick(playerName);

        new PlayerCommand(joystick, command).execute();

        return "ok";
    }
}
