package com.codenjoy.dojo.web.controller;

import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.services.PlayerCommand;
import com.codenjoy.dojo.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;

/**
 * User: sanja
 * Date: 05.06.13
 * Time: 18:27
 */

@Controller
@RequestMapping("/joystick")
public class JoystickController {

    @Autowired
    private PlayerService playerService;

    @RequestMapping(method = RequestMethod.GET)
    public String joystick(ModelMap model,
                           @RequestParam("playerName") String playerName,
                           @RequestParam("key") String key)
    {
        Joystick joystick = playerService.getJoystick(playerName);
        Player player = playerService.findPlayer(playerName);

        new PlayerCommand(joystick, key, player).execute();

        return "ok";
    }
}
