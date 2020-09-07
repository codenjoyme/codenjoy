package com.codenjoy.dojo.icancode.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2020 Codenjoy
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
import com.codenjoy.dojo.services.PlayerCommand;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PlayerCommandTest {

    @Test
    public void shouldParseSaveProgramCommand() {
        // given
        String command =
                "message('apofig@gmail.com|$%&|function program(robot) {\n" +
                "    robot.goDown();\r" +
                "}')";
        Joystick joystick = mock(Joystick.class);

        // when
        new PlayerCommand(joystick, command).execute();

        // then
        verify(joystick).message("apofig@gmail.com|$%&|function program(robot) {\n" +
            "    robot.goDown();\r" +
            "}");
    }
}
