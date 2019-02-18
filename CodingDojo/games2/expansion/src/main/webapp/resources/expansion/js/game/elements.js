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

var elements = [];
var elementsTypes = [];
var elementsByChar = {};
var elementsByType = {};

var el = function(char, type, direction) {
    var result = {
        char: char,
        type: type,
        direction: direction
    };

    elementsByChar[char] = result;

    if (!elementsByType[type]) {
        elementsByType[type] = [];
    } else {
        elementsByType[type].push(result);
    }

    elements.push(result);

    if (elementsTypes.indexOf(type) == -1) {
        elementsTypes.push(type);
    }

    return result;
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

    ROBOT_OTHER: el('X', 'OTHER_ROBOT'),

    START: el('S', 'START'),
    EXIT: el('E', 'EXIT'),
    GOLD: el('$', 'GOLD'),
    HOLE: el('O', 'HOLE'),
    BOX: el('B', 'BOX'),

    getElements: function () {
        return elements.slice(0);
    },

    getElement: function (char) {
        return elementsByChar[char];
    },

    getElementsTypes: function () {
        return elementsTypes.slice(0);
    },

    getElementsOfType: function (type) {
        return elementsByType[type];
    }

};