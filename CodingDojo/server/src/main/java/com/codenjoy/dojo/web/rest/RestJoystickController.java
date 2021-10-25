package com.codenjoy.dojo.web.rest;

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
import com.codenjoy.dojo.services.nullobj.NullPlayer;
import com.codenjoy.dojo.services.security.GameAuthoritiesConstants;
import com.codenjoy.dojo.web.controller.Validator;
import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.codenjoy.dojo.web.controller.Validator.CANT_BE_NULL;
import static com.codenjoy.dojo.web.rest.RestJoystickController.URI;

@Controller
@RequestMapping(URI)
@AllArgsConstructor
public class RestJoystickController {

    public static final String URI = "/rest/joystick";

    private PlayerService playerService;
    private Validator validator;


    @GetMapping("/player/{player}/code/{code}/do/{command}")
    public void joystick(@PathVariable("player") String id,
                           @PathVariable("code") String code,
                           @PathVariable("command") String command)
    {
        validator.checkCommand(command);
        validator.checkPlayerCode(id, code);

        joystick(id, command);
    }


    @GetMapping(("/player/{player}/do/{command}"))
    @Secured(GameAuthoritiesConstants.ROLE_ADMIN)
    public void adminJoystick(@PathVariable("player") String id,
                           @PathVariable("command") String command)
    {
        validator.checkCommand(command);
        validator.checkPlayerId(id, CANT_BE_NULL);

        joystick(id, command);
    }

    private void joystick(String id, String command) {
        Player player = playerService.get(id);
        if (player == NullPlayer.INSTANCE || !player.getId().equals(id)) {
            throw new IllegalArgumentException(String.format("Player not found : '%s'", id));
        }

        Joystick joystick = playerService.getJoystick(id);
        new PlayerCommand(joystick, command).execute();
    }
}
