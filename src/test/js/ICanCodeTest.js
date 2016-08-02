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
 * Created by Mikhail_Udalyi on 14.07.2016.
 */
QUnit.test('Chars test', function(assert) {
    assert.deepEqual(Element.EMPTY, el('-', 'NONE'));
    assert.deepEqual(Element.FLOOR, el('.', 'NONE'));

    assert.deepEqual(Element.ANGLE_IN_LEFT, el('╔', 'WALL'));
    assert.deepEqual(Element.WALL_FRONT, el('═', 'WALL'));
    assert.deepEqual(Element.ANGLE_IN_RIGHT, el('┐', 'WALL'));
    assert.deepEqual(Element.WALL_RIGHT, el('│', 'WALL'));
    assert.deepEqual(Element.ANGLE_BACK_RIGHT, el('┘', 'WALL'));
    assert.deepEqual(Element.WALL_BACK, el('─', 'WALL'));
    assert.deepEqual(Element.ANGLE_BACK_LEFT, el('└', 'WALL'));
    assert.deepEqual(Element.WALL_LEFT, el('║', 'WALL'));
    assert.deepEqual(Element.WALL_BACK_ANGLE_LEFT, el('┌', 'WALL'));
    assert.deepEqual(Element.WALL_BACK_ANGLE_RIGHT, el('╗', 'WALL'));
    assert.deepEqual(Element.ANGLE_OUT_RIGHT, el('╝', 'WALL'));
    assert.deepEqual(Element.ANGLE_OUT_LEFT, el('╚', 'WALL'));
    assert.deepEqual(Element.SPACE, el(' ', 'WALL'));

    assert.deepEqual(Element.ROBOT, el('☺', 'MY_ROBOT'));
    assert.deepEqual(Element.ROBOT_FALLING, el('o', 'HOLE'));
    assert.deepEqual(Element.ROBOT_FLYING, el('*', 'MY_ROBOT'));
    assert.deepEqual(Element.ROBOT_LASER, el('☻', 'MY_ROBOT'));

    assert.deepEqual(Element.ROBOT_OTHER, el('X', 'OTHER_ROBOT'));
    assert.deepEqual(Element.ROBOT_OTHER_FALLING, el('x', 'HOLE'));
    assert.deepEqual(Element.ROBOT_OTHER_FLYING, el('^', 'OTHER_ROBOT'));
    assert.deepEqual(Element.ROBOT_OTHER_LASER, el('&', 'OTHER_ROBOT'));

    assert.deepEqual(Element.LASER_MACHINE_CHARGING_LEFT, el('˂', 'LASER_MACHINE'));
    assert.deepEqual(Element.LASER_MACHINE_CHARGING_RIGHT, el('˃', 'LASER_MACHINE'));
    assert.deepEqual(Element.LASER_MACHINE_CHARGING_UP, el('˄', 'LASER_MACHINE'));
    assert.deepEqual(Element.LASER_MACHINE_CHARGING_DOWN, el('˅', 'LASER_MACHINE'));

    assert.deepEqual(Element.LASER_MACHINE_READY_LEFT, el('◄', 'LASER_MACHINE_READY'));
    assert.deepEqual(Element.LASER_MACHINE_READY_RIGHT, el('►', 'LASER_MACHINE_READY'));
    assert.deepEqual(Element.LASER_MACHINE_READY_UP, el('▲', 'LASER_MACHINE_READY'));
    assert.deepEqual(Element.LASER_MACHINE_READY_DOWN, el('▼', 'LASER_MACHINE_READY'));

    assert.deepEqual(Element.LASER_LEFT, el('←', 'LASER_LEFT'));
    assert.deepEqual(Element.LASER_RIGHT, el('→', 'LASER_RIGHT'));
    assert.deepEqual(Element.LASER_UP, el('↑', 'LASER_UP'));
    assert.deepEqual(Element.LASER_DOWN, el('↓', 'LASER_DOWN'));

    assert.deepEqual(Element.START, el('S', 'START'));
    assert.deepEqual(Element.EXIT, el('E', 'EXIT'));
    assert.deepEqual(Element.GOLD, el('$', 'GOLD'));
    assert.deepEqual(Element.HOLE, el('O', 'HOLE'));
    assert.deepEqual(Element.BOX, el('B', 'BOX'));
});

QUnit.test('flags of game test', function(assert) {
    assert.equal(game.hasOwnProperty('enableDonate'), true);
    assert.equal(game.hasOwnProperty('enableJoystick'), true);
    assert.equal(game.hasOwnProperty('enableAlways'), true);
    assert.equal(game.hasOwnProperty('enablePlayerInfo'), true);
    assert.equal(game.hasOwnProperty('enableLeadersTable'), true);
    assert.equal(game.hasOwnProperty('enableChat'), true);
    assert.equal(game.hasOwnProperty('enableHotkeys'), true);
    assert.equal(game.hasOwnProperty('enableAdvertisement'), true);
    assert.equal(game.hasOwnProperty('showBody'), true);
});

/*QUnit.test('directions test', function(assert) {
     var board = new Board('{"layers":["OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOCDDDDEOOOOOOOOOOJ9BBaFOOOOOOOOOOIHHHHGOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAPAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"],"levelProgress":{"current":0,"total":6,"multiple":false}}');

});*/