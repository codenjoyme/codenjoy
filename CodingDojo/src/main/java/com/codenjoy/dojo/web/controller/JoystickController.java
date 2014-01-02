package com.codenjoy.dojo.web.controller;

import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.services.PlayerCommand;
import com.codenjoy.dojo.services.PlayerService;
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

    @Autowired private PlayerService playerService;

    @RequestMapping(method = RequestMethod.GET)
    public String joystick(@RequestParam("playerName") String playerName,
                           @RequestParam("code") String code,
                           @RequestParam("command") String command)
    {
        Player registeredPlayer = playerService.getByCode(code);
        if (registeredPlayer == Player.NULL || !registeredPlayer.getName().equals(playerName)) {
            return "fail";
        }
        Joystick joystick = playerService.getJoystick(playerName);
        Player player = playerService.get(playerName);

        new PlayerCommand(joystick, command, player).execute();

        return "ok";
    }
}
