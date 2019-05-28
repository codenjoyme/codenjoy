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


import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.services.PlayerCommand;
import com.codenjoy.dojo.services.PlayerService;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.nullobj.NullPlayer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/joystick")
@RequiredArgsConstructor
public class JoystickController {

    private final Registration registration;
    private final PlayerService playerService;
    private final Validator validator;

    @RequestMapping(method = RequestMethod.GET)
    public String joystick(@RequestParam("playerName") String playerName,
                           @RequestParam("code") String code,
                           @RequestParam("command") String command)
    {
        validator.checkCommand(command);
        String playerId = validator.checkPlayerCode(playerName, code);

        Player registeredPlayer = playerService.get(playerId);
        if (registeredPlayer == NullPlayer.INSTANCE || !registeredPlayer.getName().equals(playerName)) {
            return "fail";
        }
        Joystick joystick = playerService.getJoystick(playerName);

        new PlayerCommand(joystick, command).execute();

        return "ok";
    }
}
