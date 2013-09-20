package com.codenjoy.dojo.web.controller;

import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.services.PlayerCommand;
import com.codenjoy.dojo.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
    public String joystick(HttpSession session,
                           @RequestParam("playerName") String playerName,
                           @RequestParam("command") String command)
    {
        String registeredPlayer = (String) session.getAttribute("playerName");
        if (registeredPlayer == null || !registeredPlayer.equals(playerName)) {
            return "fail";
        }
        Joystick joystick = playerService.getJoystick(playerName);
        Player player = playerService.findPlayer(playerName);

        new PlayerCommand(joystick, command, player).execute();

        return "ok"; // TODO fixme
    }
}
