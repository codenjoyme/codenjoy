package com.epam.dojo.icancode.model.items;

import com.codenjoy.dojo.services.Direction;
import com.epam.dojo.icancode.model.interfaces.IField;

public class ZombieBrain {

    public Direction whereToGo(IField field) {
        return Direction.UP;
    }
}
