/*-
 * #%L
 * iCanCode - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 EPAM
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
/**
 * Created by Mikhail_Udalyi on 08.08.2016.
 */

var el = function (char, type, direction) {
    return {
        char: char,
        type: type,
        direction: direction
    }
}

var Element = {
    EMPTY: el('-', 'NONE'),
    FLOOR: el('.', 'NONE'),

    ANGLE_IN_LEFT: el('╔', 'WALL'),
    WALL_FRONT: el('═', 'WALL'),
    ANGLE_IN_RIGHT: el('┐', 'WALL'),
    WALL_RIGHT: el('│', 'WALL'),
    ANGLE_BACK_RIGHT: el('┘', 'WALL'),
    WALL_BACK: el('─', 'WALL'),
    ANGLE_BACK_LEFT: el('└', 'WALL'),
    WALL_LEFT: el('║', 'WALL'),
    WALL_BACK_ANGLE_LEFT: el('┌', 'WALL'),
    WALL_BACK_ANGLE_RIGHT: el('╗', 'WALL'),
    ANGLE_OUT_RIGHT: el('╝', 'WALL'),
    ANGLE_OUT_LEFT: el('╚', 'WALL'),
    SPACE: el(' ', 'WALL'),

    ROBOT: el('☺', 'MY_ROBOT'),
    ROBOT_FALLING: el('o', 'HOLE'),
    ROBOT_FLYING: el('*', 'MY_ROBOT'),
    ROBOT_FLYING_ON_BOX: el('№', 'BOX'),

    ROBOT_OTHER: el('X', 'OTHER_ROBOT'),
    ROBOT_OTHER_FALLING: el('x', 'HOLE'),
    ROBOT_OTHER_FLYING: el('^', 'OTHER_ROBOT'),
    ROBOT_OTHER_FLYING_ON_BOX: el('%', 'BOX'),

    START: el('S', 'START'),
    EXIT: el('E', 'EXIT'),
    GOLD: el('$', 'GOLD'),
    HOLE: el('O', 'HOLE'),
    BOX: el('B', 'BOX'),

    getElements: function (char) {
        var result = [];
        for (name in this) {
            if (typeof this[name] === 'function') {
                continue;
            }
            result.push(this[name]);
        }
        return result;
    },

    getElement: function (char) {
        var elements = this.getElements();
        for (name in elements) {
            if (elements[name].char == char) {
                return elements[name];
            }
        }
        return null;
    },

    getElementsTypes: function () {
        var result = [];
        var elements = this.getElements();
        for (name in elements) {
            var type = elements[name].type;
            if (result.indexOf(type) == -1) {
                result.push(type);
            }
        }
        return result;
    },

    getElementsOfType: function (type) {
        var result = [];
        var elements = this.getElements();
        for (name in elements) {
            if (elements[name].type == type) {
                result.push(elements[name]);
            }
        }
        return result;
    }

};