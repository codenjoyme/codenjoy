package com.codenjoy.dojo.web.controller;

import com.codenjoy.dojo.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * User: sanja
 * Date: 05.06.13
 * Time: 18:27
 */

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
        Player player = playerService.get(playerName);

        new PlayerCommand(joystick, command, player).execute();

        return "ok";
    }
}
