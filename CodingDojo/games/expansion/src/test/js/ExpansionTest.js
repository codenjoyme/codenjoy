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
/**
 * Created by Mikhail_Udalyi on 14.07.2016.
 */
QUnit.module('src/test/js/ExpansionTest.js');
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

    assert.deepEqual(Element.ROBOT_OTHER, el('X', 'OTHER_ROBOT'));

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

QUnit.test('Direction test', function(assert) {
    assert.equal(Direction.UP.name(), "UP");
    assert.equal(Direction.DOWN.name(), "DOWN");
    assert.equal(Direction.LEFT.name(), "LEFT");
    assert.equal(Direction.RIGHT.name(), "RIGHT");
    assert.equal(Direction.ACT.name(), "ACT");
    assert.equal(Direction.STOP.name(), "");

    assert.equal(Direction.valueOf(0).name(), Direction.LEFT.name());
    assert.equal(Direction.valueOf(1).name(), Direction.RIGHT.name());
    assert.equal(Direction.valueOf(2).name(), Direction.UP.name());
    assert.equal(Direction.valueOf(3).name(), Direction.DOWN.name());
    assert.equal(Direction.valueOf(4).name(), Direction.ACT.name());
    assert.equal(Direction.valueOf(5).name(), Direction.STOP.name());

    assert.equal(Direction.LEFT.inverted().name(), Direction.RIGHT.name());
    assert.equal(Direction.RIGHT.inverted().name(), Direction.LEFT.name());
    assert.equal(Direction.UP.inverted().name(), Direction.DOWN.name());
    assert.equal(Direction.DOWN.inverted().name(), Direction.UP.name());
    assert.equal(Direction.ACT.inverted().name(), Direction.STOP.name());
    assert.equal(Direction.STOP.inverted().name(), Direction.STOP.name());

    assert.equal(Direction.LEFT.clockwise().name(), Direction.DOWN.name());
    assert.equal(Direction.RIGHT.clockwise().name(), Direction.UP.name());
    assert.equal(Direction.UP.clockwise().name(), Direction.LEFT.name());
    assert.equal(Direction.DOWN.clockwise().name(), Direction.RIGHT.name());
    assert.equal(Direction.ACT.clockwise().name(), Direction.STOP.name());
    assert.equal(Direction.STOP.clockwise().name(), Direction.STOP.name());

    assert.equal(Direction.LEFT.contrClockwise().name(), Direction.UP.name());
    assert.equal(Direction.RIGHT.contrClockwise().name(), Direction.DOWN.name());
    assert.equal(Direction.UP.contrClockwise().name(), Direction.RIGHT.name());
    assert.equal(Direction.DOWN.contrClockwise().name(), Direction.LEFT.name());
    assert.equal(Direction.ACT.contrClockwise().name(), Direction.STOP.name());
    assert.equal(Direction.STOP.contrClockwise().name(), Direction.STOP.name());
});

QUnit.test('Point test', function(assert) {
    var point = new Point(0, 0);
    assert.equal(point.toString(), "[0,0]");

    point.move(2, -2);
    assert.equal(point.toString(), "[2,-2]");
    assert.equal(point.isBad(4), true);
});

/*QUnit.test('directions test', function(assert) {
     var board = new Board('{"layers":["OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOCDDDDEOOOOOOOOOOJ9BBaFOOOOOOOOOOIHHHHGOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAPAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"],"levelProgress":{"current":0,"total":6,"multiple":false}}');

});*/