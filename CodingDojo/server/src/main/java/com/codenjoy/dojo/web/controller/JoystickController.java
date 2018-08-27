package com.codenjoy.dojo.web.controller;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.nullobj.NullPlayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import static com.codenjoy.dojo.web.controller.Validator.CANT_BE_NULL;

@Controller
@RequestMapping("/joystick")
public class JoystickController {

    @Autowired private Registration registration;
    @Autowired private PlayerService playerService;
    @Autowired private Validator validator;

    @RequestMapping(method = RequestMethod.GET)
    public String joystick(@RequestParam("playerName") String playerName,
                           @RequestParam("code") String code,
                           @RequestParam("command") String command)
    {
        validator.checkPlayerName(playerName, CANT_BE_NULL);
        validator.checkCode(code, CANT_BE_NULL);
        validator.checkCommand(command);

        Player registeredPlayer = playerService.get(registration.getEmail(code));
        if (registeredPlayer == NullPlayer.INSTANCE || !registeredPlayer.getName().equals(playerName)) {
            return "fail";
        }
        Joystick joystick = playerService.getJoystick(playerName);

        new PlayerCommand(joystick, command).execute();

        return "ok";
    }
}
