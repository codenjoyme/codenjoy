package com.codenjoy.dojo.clifford.model.items.robber;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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

import com.codenjoy.dojo.clifford.model.Field;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.joystick.DirectionActJoystick;

import static com.codenjoy.dojo.services.Direction.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RobberJoystick implements Joystick, DirectionActJoystick {

    private final Robber robber;

    public RobberJoystick(Robber robber) {
        this(robber, mock(RobberAI.class));
    }

    public RobberJoystick(Robber robber, RobberAI ai) {
        this.robber = robber;
        robber.setAi(ai);
    }

    public void disableMock() {
        robber.setAi(new AI());
    }

    private void overwriteDirection(Direction direction) {
        robber.setAi(mock(RobberAI.class));
        when(robber.getAi().getDirection(any(Field.class), any(Point.class), anyList()))
                .thenReturn(direction, null);
    }

    @Override
    public void down() {
        overwriteDirection(DOWN);
    }

    @Override
    public void up() {
        overwriteDirection(UP);
    }

    @Override
    public void left() {
        overwriteDirection(LEFT);
    }

    @Override
    public void right() {
        overwriteDirection(RIGHT);
    }

    @Override
    public void act(int... p) {
        overwriteDirection(ACT);
    }
}
