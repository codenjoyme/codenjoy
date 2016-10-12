package com.epam.dojo.icancode.client.ai;

import com.codenjoy.dojo.client.Direction;

/**
 * Created by indigo on 2016-10-12.
 */
public class Command {
    Direction direction;
    boolean jump;

    public Command(Direction direction, boolean jump) {
        this.direction = direction;
        this.jump = jump;
    }
}
