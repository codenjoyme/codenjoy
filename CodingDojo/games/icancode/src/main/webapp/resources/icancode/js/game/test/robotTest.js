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
var robotTest = function() {
    toString = function (data) {
        if (data === undefined || data == null) {
            return data;
        }
        return JSON.stringify(data).split('"').join('\'');
    }

    assertEquals = function (expected, actual) {
        expected = toString(expected);
        actual = toString(actual);
        if (expected !== actual) {
            console.log('Expected:');
            console.log(expected);

            console.log('Actual:');
            console.log(actual);

            throw Error('Expected: "' + expected + '" but was: "' + actual + '"');
        }
    }

    assertActions = function (expected, actual) {
        assertEquals(expected, actual);
        actual.length = 0;
    }

    assertMoved = function (expectedCameFrom, expectedPrevious, wasMoved) {
        if (!wasMoved) {
            expectedCameFrom = null;
            expectedPrevious = null;
        }
        assertEquals(expectedCameFrom, robot.cameFrom());
        assertEquals(expectedPrevious, robot.previousDirection());
    }

    assertCommand = function (name, wasMoved) {
        var upper = name.toUpperCase();

        robot[name]();
        assertMoved(null, null, false);
        assertActions(['clean', 'command[' + upper + ']', 'wait'], controllerActions);

        robot[name]('UP');
        assertMoved('DOWN', 'UP', wasMoved);
        assertActions(['clean', 'command[' + upper + ',UP]', 'wait'], controllerActions);

        robot[name]('LEFT');
        assertMoved('RIGHT', 'LEFT', wasMoved);
        assertActions(['clean', 'command[' + upper + ',LEFT]', 'wait'], controllerActions);

        robot[name]('DOWN');
        assertMoved('UP', 'DOWN', wasMoved);
        assertActions(['clean', 'command[' + upper + ',DOWN]', 'wait'], controllerActions);

        robot[name]('RIGHT');
        assertMoved('LEFT', 'RIGHT', wasMoved);
        assertActions(['clean', 'command[' + upper + ',RIGHT]', 'wait'], controllerActions);

        robot[name]('QWE');
        assertActions(["Unexpected direction value 'QWE' please use: 'UP', 'DOWN', 'LEFT' or 'RIGHT'."], loggerActions);
        assertMoved('LEFT', 'RIGHT', wasMoved);
        assertActions([], controllerActions);

        robot[name]();
        assertMoved('null', 'null', false);
        assertActions(['clean', 'command[' + upper + ']', 'wait'], controllerActions);

        // jumpUp, jumpLeft, jumpDown, jumpRight
        robot[name + 'Up']();
        assertMoved('DOWN', 'UP', wasMoved);
        assertActions(['clean', 'command[' + upper + ',UP]', 'wait'], controllerActions);

        robot[name + 'Left']();
        assertMoved('RIGHT', 'LEFT', wasMoved);
        assertActions(['clean', 'command[' + upper + ',LEFT]', 'wait'], controllerActions);

        robot[name + 'Down']();
        assertMoved('UP', 'DOWN', wasMoved);
        assertActions(['clean', 'command[' + upper + ',DOWN]', 'wait'], controllerActions);

        robot[name + 'Right']();
        assertMoved('LEFT', 'RIGHT', wasMoved);
        assertActions(['clean', 'command[' + upper + ',RIGHT]', 'wait'], controllerActions);
    }

    resetMocks = function () {
        robot.reset();
        loggerActions.length = 0;
        controllerActions.length = 0;
    }

    game = {};
    var loggerActions = [];
    var controllerActions = [];

    runTest = function () {
        var logger = {
            print: function (message) {
                loggerActions.push(message);
            }
        };

        var controller = {
            commit: function () {
                controllerActions.push('commit');
            },
            reset: function () {
                controllerActions.push('reset');
            },
            onMessage: function () {
                controllerActions.push('commit');
            },
            reconnect: function () {
                controllerActions.push('reconnect');
            },
            cleanCommand: function () {
                controllerActions.push('clean');
            },
            addCommand: function (command) {
                controllerActions.push('command[' + command + ']');
            },
            waitCommand: function () {
                controllerActions.push('wait');
            },
            winCommand: function () {
                controllerActions.push('win');
            }
        };

        board = 'board={"offset":{"x":0,"y":0},' +
            '"heroPosition":{"x":2,"y":7},' +
            '"layers":["' +
            '╔═══════┐' +   // 8
            '║S.$◄..O│' +   // 7
            '║....$O.│' +   // 6
            '║.$E....│' +   // 5
            '║˃..O...│' +   // 4
            '║.O...Z.│' +   // 3
            '║..˄....│' +   // 2
            '║..˅˂▼►▲│' +   // 1
            '└───────┘",' + // 0
          // 012345678
            '"---------' +  // 8
            '--☺----o-' +   // 7
            '-X----x☻-' +   // 6
            '-X---B---' +   // 5
            '--→B-↓B--' +   // 4
            '-♂♂♀✝B---' +   // 3
            '--&--↑←--' +   // 2
            '---------' +   // 1
            '---------",' + // 0
          // 012345678
            '"---------' +  // 1
            '------*--' +   // 2
            '---------' +   // 3
            '---------' +   // 3
            '------^--' +   // 4
            '---------' +   // 3
            '---------' +   // 2
            '---------' +   // 1
            '---------"' +  // 0
            ']}';


        robot = initRobot(logger, controller);
        var scanner = robot.getScanner();

        // --------- board.getWholeBoard -----------
        resetMocks();

        assertEquals([[['WALL'],['WALL'],['WALL'],['WALL'],['WALL'],['WALL'],['WALL'],['WALL'],
                    ['WALL']],[['WALL'],['NONE'],['NONE'],['ZOMBIE'],['LASER_MACHINE'],['OTHER_ROBOT'],
                    ['OTHER_ROBOT'],['START'],['WALL']],[['WALL'],['NONE'],['OTHER_ROBOT'],['HOLE','ZOMBIE'],
                    ['LASER_RIGHT'],['GOLD'],['NONE'],['MY_ROBOT'],['WALL']],[['WALL'],['LASER_MACHINE'],
                    ['LASER_MACHINE'],['ZOMBIE'],['BOX'],['EXIT'],['NONE'],['GOLD'],['WALL']],[['WALL'],
                    ['LASER_MACHINE'],['NONE'],['ZOMBIE_DIE'],['HOLE'],['NONE'],['NONE'],
                    ['LASER_MACHINE_READY'],['WALL']],[['WALL'],['LASER_MACHINE_READY'],['LASER_UP'],
                    ['BOX'],['LASER_DOWN'],['BOX'],['GOLD'],['NONE'],['WALL']],[['WALL'],
                    ['LASER_MACHINE_READY'],['LASER_LEFT'],['ZOMBIE_START'],['BOX','OTHER_ROBOT'],['NONE'],
                    ['HOLE','OTHER_ROBOT'],['MY_ROBOT'],['WALL']],[['WALL'],['LASER_MACHINE_READY'],
                    ['NONE'],['NONE'],['NONE'],['NONE'],['MY_ROBOT'],['HOLE','MY_ROBOT'],['WALL']],
                    [['WALL'],['WALL'],['WALL'],['WALL'],['WALL'],['WALL'],['WALL'],['WALL'],['WALL']]],
            scanner.getWholeBoard());

        // --------- getScanner --------------
        // at point
        resetMocks();

        assertEquals(['GOLD'],
            scanner.at(new Point(2, 5)));

        assertEquals(['GOLD'],
            scanner.at(2, 5));

        assertEquals(['OTHER_ROBOT'],
            scanner.at(new Point(1, 6)));

        assertEquals(['OTHER_ROBOT'],
            scanner.at(1, 6));

        assertEquals(['HOLE', 'OTHER_ROBOT'],
            scanner.at(6, 6));

        assertEquals(['BOX', 'OTHER_ROBOT'],
            scanner.at(6, 4));

        assertEquals(['OTHER_ROBOT'], //TODO here also laser
            scanner.at(2, 2));

        assertEquals(null,
            scanner.at(null));
        assertActions(["Expected direction or point but was 'null' please use: 'UP', 'DOWN', 'LEFT', 'RIGHT' or 'new Point(x, y)'."], loggerActions);

        assertEquals(null,
            scanner.at());
        assertActions(["Expected direction or point but was 'undefined' please use: 'UP', 'DOWN', 'LEFT', 'RIGHT' or 'new Point(x, y)'."], loggerActions);

        assertEquals(null,
            scanner.at('QWE'));
        assertActions(["Expected direction or point but was 'QWE' please use: 'UP', 'DOWN', 'LEFT', 'RIGHT' or 'new Point(x, y)'."], loggerActions);

        // out of board

        assertEquals(null,
            scanner.at(0, -1));
        assertActions(['Your point is out of board: [0,-1].'], loggerActions);

        assertEquals(null,
            scanner.at(new Point(9, 0)));
        assertActions(['Your point is out of board: [9,0].'], loggerActions);

        // at direction
        resetMocks();

        assertEquals(['START'],
            scanner.at('LEFT'));

        assertEquals(['WALL'],
            scanner.at('UP'));

        assertEquals(['NONE'],
            scanner.at('DOWN'));

        assertEquals(['GOLD'],
            scanner.at('RIGHT'));

        // atLeft, atRight, atUp, atDown
        resetMocks();

        assertEquals(['START'],
            scanner.atLeft());

        assertEquals(['WALL'],
            scanner.atUp());

        assertEquals(['NONE'],
            scanner.atDown());

        assertEquals(['GOLD'],
            scanner.atRight());

        // atNearRobot
        resetMocks();

        assertEquals(['OTHER_ROBOT'],
            scanner.atNearRobot(-1, -1));

        assertEquals(['MY_ROBOT'], // мoй другой робот что летает LAYER3
            scanner.atNearRobot(4, 0));

        assertEquals(['BOX', 'OTHER_ROBOT'], // чужой робот, что летает LAYER3
            scanner.atNearRobot(4, -3));

        assertEquals(null,
            scanner.atNearRobot());
        assertActions(["You tried to call function(x, y) where 'x' and 'y' are numbers, with parameters []."], loggerActions);

        assertEquals(null,
            scanner.atNearRobot('1', '2'));
        assertActions(["You tried to call function(x, y) where 'x' and 'y' are numbers, with parameters [1,2]."], loggerActions);

        assertEquals(null,
            scanner.atNearRobot('ASD', 'QWE', false));
        assertActions(["You tried to call function(x, y) where 'x' and 'y' are numbers, with parameters [ASD,QWE,false]."], loggerActions);

        // out of board

        // TODO немного вводит в заблуждение, передал: 0, -10, а в сообщении: 2,-9 - все потом что отнимаются координаты игрока моего
        assertEquals(null,
            scanner.atNearRobot(0, -10));
        assertActions(['Your point is out of board: [2,-3].'], loggerActions);

        assertEquals(null,
            scanner.atNearRobot(90, 0));
        assertActions(['Your point is out of board: [92,7].'], loggerActions);

        // getMe
        resetMocks();

        assertEquals({'x': 2, 'y': 7}, scanner.getMe());

        // TODO what if Hero not on board?
        // TODO what if Hero is flying
        // TODO ...or falling to hole
        // TODO ...or die on laser?

        // isAt
        resetMocks();

        assertEquals(false,
            scanner.isAt(2, 7, 'OTHER_ROBOT'));

        assertEquals(true,
            scanner.isAt(2, 7, 'MY_ROBOT'));

        assertEquals(false,
            scanner.isAt(2, 7, ['MY_ROBOT', 'GOLD']));
        assertEquals(true,
            scanner.isAnyOfAt(2, 7, ['MY_ROBOT', 'GOLD']));

        assertEquals(true,
            scanner.isAt(2, 7, ['MY_ROBOT', 'NONE']));

        assertEquals(false,
            scanner.isAt(2, 7, ['OTHER_ROBOT', 'HOLE', 'ZOMBIE']));

        assertEquals(true,
            scanner.isAt(6, 7, ['MY_ROBOT'])); // мой летает на LAYER3

        assertEquals(true,
            scanner.isAt(6, 4, ['OTHER_ROBOT'])); // чужой летает на LAYER3

        assertEquals(false,
            scanner.isAt(2, 1));
        assertActions(["You tried to call function(x, y, elements) where 'x' and 'y' are numbers, and 'elements' is string or array of strings, with parameters [2,1]."], loggerActions);

        assertEquals(false,
            scanner.isAt());
        assertActions(["You tried to call function(x, y, elements) where 'x' and 'y' are numbers, and 'elements' is string or array of strings, with parameters []."], loggerActions);

        assertEquals(false,
            scanner.isAt(1, 2, [3, 4]));
        assertActions(["You tried to call function(x, y, elements) where 'x' and 'y' are numbers, and 'elements' is string or array of strings, with parameters [1,2,3,4]."], loggerActions);

        // out of board

        assertEquals(false,
            scanner.isAt(-1, 0, 'MY_ROBOT'));
        assertActions(['Your point is out of board: [-1,0].'], loggerActions);

        assertEquals(false,
            scanner.isAt(0, 9, 'MY_ROBOT'));
        assertActions(['Your point is out of board: [0,9].'], loggerActions);

        // getAt
        resetMocks();

        assertEquals(['MY_ROBOT'],
            scanner.getAt(2, 7));

        assertEquals(['NONE'],
            scanner.getAt(2, 6));

        assertEquals(['EXIT'],
            scanner.getAt(3, 5));

        assertEquals(['GOLD'],
            scanner.getAt(3, 7));

        assertEquals(['MY_ROBOT'],
            scanner.getAt(6, 7));

        assertEquals(['BOX', 'OTHER_ROBOT'],
            scanner.getAt(6, 4));

        assertEquals(null,
            scanner.getAt());
        assertActions(["You tried to call function(x, y) where 'x' and 'y' are numbers, with parameters []."], loggerActions);

        assertEquals(null,
            scanner.getAt('1', 'HERO'));
        assertActions(["You tried to call function(x, y) where 'x' and 'y' are numbers, with parameters [1,HERO]."], loggerActions);

        // out of board

        assertEquals(null,
            scanner.getAt(-1, -1));
        assertActions(['Your point is out of board: [-1,-1].'], loggerActions);

        assertEquals(null,
            scanner.getAt(9, 9));
        assertActions(['Your point is out of board: [9,9].'], loggerActions);

        // findAll
        resetMocks();

        assertEquals([{'x': 2, 'y': 5}, {'x': 3, 'y': 7}, {'x': 5, 'y': 6}],
            scanner.findAll('GOLD'));

        assertEquals([{'x': 1, 'y': 5},{'x': 1, 'y': 6},{'x': 6, 'y': 6},{'x': 6, 'y': 4},{'x': 2, 'y': 2}],
            scanner.findAll('OTHER_ROBOT'));

        assertEquals([{'x': 2, 'y': 7}, {'x': 7, 'y': 7}, {'x': 6, 'y': 7}, {'x': 7, 'y': 6}],
            scanner.findAll('MY_ROBOT'));

        assertEquals([{'x': 1, 'y': 5}, {'x': 1, 'y': 6}, {'x': 6, 'y': 6}, {'x': 6, 'y': 4}, {'x': 2, 'y': 2},
                    {'x': 2, 'y': 7}, {'x': 7, 'y': 7}, {'x': 6, 'y': 7}, {'x': 7, 'y': 6}],
            scanner.findAll(['OTHER_ROBOT', 'MY_ROBOT']));

        assertEquals([{'x': 2, 'y': 5}, {'x': 3, 'y': 7}, {'x': 5, 'y': 6}],
            scanner.findAll(['GOLD']));

        assertEquals([],
            scanner.findAll([]));

        assertEquals([{'x': 2, 'y': 5}, {'x': 3, 'y': 7}, {'x': 5, 'y': 6}, {'x': 1, 'y': 7}, {'x': 3, 'y': 5}],
            scanner.findAll(['GOLD', 'START', 'EXIT']));

        assertEquals(null,
            scanner.findAll());
        assertActions(["You tried to call function(elements) where 'elements' is string or array of strings, with parameters []."], loggerActions);

        assertEquals(null,
            scanner.findAll(1, 2));
        assertActions(["You tried to call function(elements) where 'elements' is string or array of strings, with parameters [1,2]."], loggerActions);

        assertEquals(null,
            scanner.findAll([1, 2]));
        assertActions(["You tried to call function(elements) where 'elements' is string or array of strings, with parameters [1,2]."], loggerActions);

        // isAnyOfAt
        resetMocks();

        assertEquals(false,
            scanner.isAnyOfAt(2, 7, 'OTHER_ROBOT'));

        assertEquals(true,
            scanner.isAnyOfAt(6, 7, ['OTHER_ROBOT', 'MY_ROBOT']));

        assertEquals(true,
            scanner.isAnyOfAt(6, 4, ['OTHER_ROBOT', 'MY_ROBOT']));

        assertEquals(true,
            scanner.isAnyOfAt(2, 7, 'MY_ROBOT'));

        assertEquals(true,
            scanner.isAnyOfAt(2, 7, ['MY_ROBOT', 'GOLD']));
        
        assertEquals(false,
            scanner.isAt(2, 7, ['MY_ROBOT', 'GOLD']));

        assertEquals(false,
            scanner.isAnyOfAt(2, 7, ['OTHER_ROBOT', 'HOLE', 'ZOMBIE']));

        assertEquals(false,
            scanner.isAnyOfAt(2, 1));
        assertActions(["You tried to call function(x, y, elements) where 'x' and 'y' are numbers, and 'elements' is string or array of strings, with parameters [2,1]."], loggerActions);

        assertEquals(false,
            scanner.isAnyOfAt());
        assertActions(["You tried to call function(x, y, elements) where 'x' and 'y' are numbers, and 'elements' is string or array of strings, with parameters []."], loggerActions);

        assertEquals(false,
            scanner.isAnyOfAt(1, 2, [3, 4]));
        assertActions(["You tried to call function(x, y, elements) where 'x' and 'y' are numbers, and 'elements' is string or array of strings, with parameters [1,2,3,4]."], loggerActions);

        // at corners

        assertEquals(true,
            scanner.isAnyOfAt(0, 8, 'WALL'));

        assertEquals(true,
            scanner.isAnyOfAt(0, 0, ['OTHER_ROBOT', 'WALL']));

        assertEquals(false,
            scanner.isAnyOfAt(8, 8, 'GOLD'));

        assertEquals(false,
            scanner.isAnyOfAt(8, 0, ['GOLD', 'HOLE']));

        // outside of map

        assertEquals(false,
            scanner.isAnyOfAt(-1, -1, 'WALL'));
        assertActions(['Your point is out of board: [-1,-1].'], loggerActions);

        assertEquals(false,
            scanner.isAnyOfAt(-1, 9, ['OTHER_ROBOT', 'WALL']));
        assertActions(['Your point is out of board: [-1,9].'], loggerActions);

        assertEquals(false,
            scanner.isAnyOfAt(9, -1, 'GOLD'));
        assertActions(['Your point is out of board: [9,-1].'], loggerActions);

        assertEquals(false,
            scanner.isAnyOfAt(9, 9, ['GOLD', 'HOLE']));
        assertActions(['Your point is out of board: [9,9].'], loggerActions);

        // isNear
        resetMocks();

        assertEquals(true,
            scanner.isNear(2, 6, 'OTHER_ROBOT'));

        assertEquals(true,
            scanner.isNear(2, 6, 'MY_ROBOT'));

        assertEquals(false,
            scanner.isNear(2, 6, 'ZOMBIE'));

        assertEquals(false,
            scanner.isNear(2, 6, ['ZOMBIE', 'HOLE']));

        assertEquals(true,
            scanner.isNear(5, 7, ['OTHER_ROBOT', 'MY_ROBOT']));

        assertEquals(true,
            scanner.isNear(5, 4, ['OTHER_ROBOT', 'MY_ROBOT']));

        assertEquals(true,
            scanner.isNear(2, 6, ['MY_ROBOT', 'GOLD']));

        assertEquals(true,
            scanner.isNear(2, 6, ['OTHER_ROBOT', 'HOLE', 'ZOMBIE']));

        assertEquals(false,
            scanner.isNear(2, 2));
        assertActions(["You tried to call function(x, y, elements) where 'x' and 'y' are numbers, and 'elements' is string or array of strings, with parameters [2,2]."], loggerActions);

        assertEquals(false,
            scanner.isNear());
        assertActions(["You tried to call function(x, y, elements) where 'x' and 'y' are numbers, and 'elements' is string or array of strings, with parameters []."], loggerActions);

        assertEquals(false,
            scanner.isNear(1, 2, [3, 4]));
        assertActions(["You tried to call function(x, y, elements) where 'x' and 'y' are numbers, and 'elements' is string or array of strings, with parameters [1,2,3,4]."], loggerActions);

        // out of board

        assertEquals(false,
            scanner.isNear(-1, -1, 'GOLD'));
        assertActions(['Your point is out of board: [-1,-1].'], loggerActions);

        assertEquals(false,
            scanner.isNear(9, 9, 'GOLD'));
        assertActions(['Your point is out of board: [9,9].'], loggerActions);

        // isBarrierAt
        resetMocks();

        assertEquals(false,
            scanner.isBarrierAt(2, 6));

        assertEquals(true,
            scanner.isBarrierAt(0, 8));

        assertEquals(true,
            scanner.isBarrierAt(0, 8));

        assertEquals(true,
            scanner.isBarrierAt(3, 4));

        assertEquals(true,
            scanner.isBarrierAt(4, 7));

        assertEquals(false,
            scanner.isBarrierAt());
        assertActions(["You tried to call function(x, y) where 'x' and 'y' are numbers, with parameters []."], loggerActions);

        assertEquals(false,
            scanner.isBarrierAt('1', '2'));
        assertActions(["You tried to call function(x, y) where 'x' and 'y' are numbers, with parameters [1,2]."], loggerActions);

        assertEquals(false,
            scanner.isBarrierAt('ASD', 'QWE', false));
        assertActions(["You tried to call function(x, y) where 'x' and 'y' are numbers, with parameters [ASD,QWE,false]."], loggerActions);

        // out of board

        assertEquals(false,
            scanner.isBarrierAt(-1, -1));
        assertActions(['Your point is out of board: [-1,-1].'], loggerActions);

        assertEquals(false,
            scanner.isBarrierAt(9, 9));
        assertActions(['Your point is out of board: [9,9].'], loggerActions);

        // countNear
        resetMocks();

        assertEquals(1,
            scanner.countNear(2, 6, 'GOLD'));

        assertEquals(1,
            scanner.countNear(2, 6, 'MY_ROBOT'));

        assertEquals(0,
            scanner.countNear(5, 7, ['OTHER_ROBOT']));

        assertEquals(1,
            scanner.countNear(5, 7, ['MY_ROBOT']));

        assertEquals(1,
            scanner.countNear(5, 4, ['OTHER_ROBOT', 'MY_ROBOT']));

        assertEquals(1,
            scanner.countNear(2, 6, 'OTHER_ROBOT'));

        assertEquals(10, // TODO should be 3
            scanner.countNear(3, 6, 'NONE'));

        assertEquals(2, // TODO should be 3
            scanner.countNear(1, 7, 'WALL'));

        assertEquals(0,
            scanner.countNear(2, 2));
        assertActions(["You tried to call function(x, y, elements) where 'x' and 'y' are numbers, and 'elements' is string or array of strings, with parameters [2,2]."], loggerActions);

        assertEquals(0,
            scanner.countNear());
        assertActions(["You tried to call function(x, y, elements) where 'x' and 'y' are numbers, and 'elements' is string or array of strings, with parameters []."], loggerActions);

        assertEquals(0,
            scanner.countNear(1, 2, [3, 4]));
        assertActions(["You tried to call function(x, y, elements) where 'x' and 'y' are numbers, and 'elements' is string or array of strings, with parameters [1,2,3,4]."], loggerActions);

        // out of board

        assertEquals(0,
            scanner.countNear(-1, -1, 'GOLD'));
        assertActions(['Your point is out of board: [-1,-1].'], loggerActions);

        assertEquals(0,
            scanner.countNear(9, 9, 'GOLD'));
        assertActions(['Your point is out of board: [9,9].'], loggerActions);

        // getOtherRobots
        resetMocks();

        assertEquals([{'x': 1, 'y': 5}, {'x': 1, 'y': 6}, {'x': 2, 'y': 2}, {'x': 6, 'y': 6}, {'x': 6, 'y': 4}],
            scanner.getOtherRobots());

        // getLaserMachines
        resetMocks();

        assertEquals([{'x': 1, 'y': 4, 'direction': 'RIGHT'}, {'x': 3, 'y': 1, 'direction': 'DOWN'},
                {'x': 3, 'y': 2,'direction': 'UP'}, {'x': 4, 'y': 1, 'direction': 'LEFT'}, {'x': 4, 'y': 7, 'direction': 'LEFT'},
                {'x': 5, 'y': 1, 'direction': 'DOWN'}, {'x': 6, 'y': 1, 'direction': 'RIGHT'}, {'x': 7, 'y': 1, 'direction': 'UP'}],
            scanner.getLaserMachines());

        // getLasers
        resetMocks();

        assertEquals([{'x': 2, 'y': 4, 'direction': 'RIGHT'}, {'x': 5,'y': 2,'direction': 'UP'},
                {'x': 5, 'y': 4, 'direction': 'DOWN'}, {'x': 6, 'y': 2, 'direction': 'LEFT'}],
            scanner.getLasers());

        // getWalls
        resetMocks();

        assertEquals([{'x': 0, 'y': 0}, {'x': 0, 'y': 1}, {'x': 0, 'y': 2}, {'x': 0, 'y': 3}, {'x': 0, 'y': 4},
                    {'x': 0, 'y': 5}, {'x': 0, 'y': 6}, {'x': 0, 'y': 7}, {'x': 0, 'y': 8}, {'x': 1, 'y': 0}, {'x': 1, 'y': 8},
                    {'x': 2, 'y': 0}, {'x': 2, 'y': 8}, {'x': 3, 'y': 0}, {'x': 3, 'y': 8}, {'x': 4, 'y': 0}, {'x': 4, 'y': 8},
                    {'x': 5, 'y': 0}, {'x': 5, 'y': 8}, {'x': 6, 'y': 0}, {'x': 6, 'y': 8}, {'x': 7, 'y': 0}, {'x': 7, 'y': 8},
                    {'x': 8, 'y': 0}, {'x': 8, 'y': 1}, {'x': 8, 'y': 2}, {'x': 8, 'y': 3}, {'x': 8, 'y': 4}, {'x': 8, 'y': 5},
                    {'x': 8, 'y': 6}, {'x': 8, 'y': 7}, {'x': 8, 'y': 8}],
            scanner.getWalls());

        // getBoxes
        resetMocks();

        assertEquals([{'x': 3, 'y': 4}, {'x': 5, 'y': 3}, {'x': 5, 'y': 5}, {'x': 6, 'y': 4}],
            scanner.getBoxes());

        // getGold
        resetMocks();

        assertEquals([{'x': 2, 'y': 5}, {'x': 3, 'y': 7}, {'x': 5, 'y': 6}],
            scanner.getGold());

        // getStart
        resetMocks();

        assertEquals([{'x': 1, 'y': 7}],
            scanner.getStart());

        // getZombieStart
        resetMocks();

        assertEquals([{'x': 6, 'y': 3}],
            scanner.getZombieStart());

        // getExit
        resetMocks();

        assertEquals([{'x': 3, 'y': 5}],
            scanner.getExit());

        // getHoles
        resetMocks();

        assertEquals([{'x': 2, 'y': 3}, {'x': 4, 'y': 4}, {'x': 6, 'y': 6}, {'x': 7, 'y': 7}],
            scanner.getHoles());

        // getBarriers
        resetMocks();

        assertEquals([{'x':0,'y':0},{'x':0,'y':1},{'x':0,'y':2},{'x':0,'y':3},{'x':0,'y':4},
                    {'x':0,'y':5},{'x':0,'y':6},{'x':0,'y':7},{'x':0,'y':8},{'x':1,'y':0},{'x':1,'y':4},
                    {'x':1,'y':8},{'x':2,'y':0},{'x':2,'y':3},{'x':2,'y':8},{'x':3,'y':0},{'x':3,'y':1},
                    {'x':3,'y':2},{'x':3,'y':4},{'x':3,'y':8},{'x':4,'y':0},{'x':4,'y':1},{'x':4,'y':4},
                    {'x':4,'y':7},{'x':4,'y':8},{'x':5,'y':0},{'x':5,'y':1},{'x':5,'y':3},{'x':5,'y':5},
                    {'x':5,'y':8},{'x':6,'y':0},{'x':6,'y':1},{'x':6,'y':4},{'x':6,'y':6},{'x':6,'y':8},
                    {'x':7,'y':0},{'x':7,'y':1},{'x':7,'y':7},{'x':7,'y':8},{'x':8,'y':0},{'x':8,'y':1},
                    {'x':8,'y':2},{'x':8,'y':3},{'x':8,'y':4},{'x':8,'y':5},{'x':8,'y':6},{'x':8,'y':7},
                    {'x':8,'y':8}],
            scanner.getBarriers());

        // getElements
        resetMocks();

        assertEquals(['NONE', 'WALL', 'LASER_MACHINE', 'LASER_MACHINE_READY', 'START', 'EXIT', 'HOLE', 'BOX', 'ZOMBIE_START', 'GOLD', 'MY_ROBOT', 'OTHER_ROBOT', 'LASER_LEFT', 'LASER_RIGHT', 'LASER_UP', 'LASER_DOWN', 'ZOMBIE', 'ZOMBIE_DIE'],
            scanner.getElements());

        // getShortestWay deadloop
        resetMocks();

        assertEquals([],
            scanner.getShortestWay(new Point(0, 0), scanner.getMe()));

        // getShortestWay with 1 point
        resetMocks();

        assertEquals([{'x': 2, 'y': 7}],
            scanner.getShortestWay(scanner.getMe()));

        assertEquals([{'x': 2, 'y': 7}, {'x': 2, 'y': 6}, {'x': 2, 'y': 5}, {'x': 3, 'y': 5}],
            scanner.getShortestWay(scanner.getExit()[0]));

        assertEquals([{'x': 2, 'y': 7}, {'x': 1, 'y': 7}],
            scanner.getShortestWay(scanner.getStart()[0]));

        assertEquals([{'x':2,'y':7},{'x':2,'y':6},{'x':2,'y':5},{'x':2,'y':4},{'x':2,'y':3},
                     {'x':2,'y':2},{'x':3,'y':2},{'x':4,'y':2},{'x':5,'y':2},{'x':6,'y':2}],
            scanner.getShortestWay(new Point(6, 2)));

        // to elements

        assertEquals([{'x': 2, 'y': 7}],
            scanner.getShortestWay('MY_ROBOT'));

        assertEquals([{'x': 2, 'y': 7}, {'x': 2, 'y': 6}, {'x': 2, 'y': 5}, {'x': 3, 'y': 5}],
            scanner.getShortestWay('EXIT'));

        assertEquals([{'x': 2, 'y': 7}, {'x': 1, 'y': 7}],
            scanner.getShortestWay('START'));

        assertEquals([{'x':2,'y':7},{'x':2,'y':6},{'x':2,'y':5},{'x':2,'y':4},{'x':2,'y':3}],
            scanner.getShortestWay(['ZOMBIE']));

        assertEquals([{'x':2,'y':7},{'x':3,'y':7}],
            scanner.getShortestWay(['GOLD']));

        assertEquals([{'x':2,'y':7},{'x':3,'y':7}],
            scanner.getShortestWay(['ZOMBIE','GOLD']));

        assertEquals(null,
            scanner.getShortestWay(1));
        assertActions(["You tried to call function(elements) where 'elements' is string or array of strings, with parameters [1]."], loggerActions);

        assertEquals(null,
            scanner.getShortestWay(null));
        assertActions(["You tried to call function(elements) where 'elements' is string or array of strings, with parameters []."], loggerActions);

        assertEquals(null,
            scanner.getShortestWay([new Point(1, 2)]));
        assertActions(["You tried to call function(elements) where 'elements' is string or array of strings, with parameters [[1,2]]."], loggerActions);

        assertEquals(null,
            scanner.getShortestWay('string'));
        assertActions(["You tried to call function(elements) where 'elements' is string or array of strings, for non exists element [string]."], loggerActions);

        assertEquals(null,
            scanner.getShortestWay(['ZOMBIE', 'string']));
        assertActions(["You tried to call function(elements) where 'elements' is string or array of strings, for non exists element [ZOMBIE,string]."], loggerActions);

        // out of board

        assertEquals(null,
            scanner.getShortestWay(pt(-1, -1)));
        assertActions(['Your point is out of board: [-1,-1].'], loggerActions);

        assertEquals(null,
            scanner.getShortestWay(pt(9, 9)));
        assertActions(['Your point is out of board: [9,9].'], loggerActions);

        // getShortestWay with 2 points
        resetMocks();

        assertEquals([{'x': 2, 'y': 7}],
            scanner.getShortestWay(new Point(2, 7), scanner.getMe()));

        assertEquals([{'x': 2, 'y': 7}, {'x': 2, 'y': 6}, {'x': 2, 'y': 5}, {'x': 3, 'y': 5}],
            scanner.getShortestWay(new Point(2, 7), scanner.getExit()[0]));

        assertEquals([{'x': 2, 'y': 7}, {'x': 1, 'y': 7}],
            scanner.getShortestWay(new Point(2, 7), scanner.getStart()[0]));

        assertEquals([{'x':2,'y':7},{'x':2,'y':6},{'x':2,'y':5},{'x':2,'y':4},{'x':2,'y':3},
                    {'x':2,'y':2},{'x':3,'y':2},{'x':4,'y':2},{'x':5,'y':2},{'x':6,'y':2}],
            scanner.getShortestWay(new Point(2, 7), new Point(6, 2)));

        assertEquals(null,
            scanner.getShortestWay(1, 2));
        assertActions(["You tried to call function(point, point) with parameters [1,2]."], loggerActions);

        assertEquals(null,
            scanner.getShortestWay(null, 2));
        assertActions(["You tried to call function(point, point) with parameters [,2]."], loggerActions);

        assertEquals(null,
            scanner.getShortestWay([new Point(1, 2)], [1]));
        assertActions(["You tried to call function(point, point) with parameters [[1,2],1]."], loggerActions);

        assertEquals(null,
            scanner.getShortestWay(new Point(1, 2), 3));
        assertActions(["You tried to call function(point, point) with parameters [[1,2],3]."], loggerActions);

        // out of board

        assertEquals(null,
            scanner.getShortestWay(pt(-1, -1), pt(0, 0)));
        assertActions(['Your point is out of board: [-1,-1].'], loggerActions);

        assertEquals(null,
            scanner.getShortestWay(pt(0, 0), pt(9, 9)));
        assertActions(['Your point is out of board: [9,9].'], loggerActions);

        // isMyRobotAlive
        resetMocks();

        assertEquals(false,
            scanner.isMyRobotAlive());

        // ------------- other Robot methods ---------------
        // nextLevel
        resetMocks();

        robot.nextLevel();
        assertActions(['win', 'wait'], controllerActions);

        // log
        resetMocks();

        robot.log('message');
        robot.log('message2');
        assertActions(['message', 'message2'], loggerActions);

        // invert
        assertEquals('DOWN', robot.invert('UP'));
        assertEquals('UP', robot.invert('DOWN'));
        assertEquals('LEFT', robot.invert('RIGHT'));
        assertEquals('RIGHT', robot.invert('LEFT'));

        assertEquals(undefined, robot.invert('QWE'));
        assertActions(["Unexpected direction value 'QWE' please use: 'UP', 'DOWN', 'LEFT' or 'RIGHT'."], loggerActions);

        assertEquals(undefined, robot.invert(null));
        assertActions(["Unexpected direction value 'null' please use: 'UP', 'DOWN', 'LEFT' or 'RIGHT'."], loggerActions);

        assertEquals(undefined, robot.invert());
        assertActions(["Unexpected direction value 'undefined' please use: 'UP', 'DOWN', 'LEFT' or 'RIGHT'."], loggerActions);

        // go
        resetMocks();

        assertEquals(null, robot.cameFrom());
        assertEquals(null, robot.previousDirection());
        assertActions([], controllerActions);

        robot.go('UP');
        assertEquals('DOWN', robot.cameFrom());
        assertEquals('UP', robot.previousDirection());
        assertActions(['clean', 'command[UP]'], controllerActions);

        robot.go('LEFT');
        assertEquals('RIGHT', robot.cameFrom());
        assertEquals('LEFT', robot.previousDirection());
        assertActions(['clean', 'command[LEFT]'], controllerActions);

        robot.go('DOWN');
        assertEquals('UP', robot.cameFrom());
        assertEquals('DOWN', robot.previousDirection());
        assertActions(['clean', 'command[DOWN]'], controllerActions);

        robot.go('RIGHT');
        assertEquals('LEFT', robot.cameFrom());
        assertEquals('RIGHT', robot.previousDirection());
        assertActions(['clean', 'command[RIGHT]'], controllerActions);

        robot.go('QWE');
        assertActions(["Unexpected direction value 'QWE' please use: 'UP', 'DOWN', 'LEFT' or 'RIGHT'."], loggerActions);
        assertEquals('LEFT', robot.cameFrom());
        assertEquals('RIGHT', robot.previousDirection());
        assertActions([], controllerActions);

        // goUp, goLeft, goDown, goRight
        resetMocks();

        robot.goUp();
        assertEquals('DOWN', robot.cameFrom());
        assertEquals('UP', robot.previousDirection());
        assertActions(['clean', 'command[UP]'], controllerActions);

        robot.goLeft();
        assertEquals('RIGHT', robot.cameFrom());
        assertEquals('LEFT', robot.previousDirection());
        assertActions(['clean', 'command[LEFT]'], controllerActions);

        robot.goDown();
        assertEquals('UP', robot.cameFrom());
        assertEquals('DOWN', robot.previousDirection());
        assertActions(['clean', 'command[DOWN]'], controllerActions);

        robot.goRight();
        assertEquals('LEFT', robot.cameFrom());
        assertEquals('RIGHT', robot.previousDirection());
        assertActions(['clean', 'command[RIGHT]'], controllerActions);

        // reset
        robot.reset();
        assertActions(['clean'], controllerActions);
        assertEquals(null, robot.cameFrom());
        assertEquals(null, robot.previousDirection());

        // jump
        resetMocks();
        assertCommand('jump', true);

        // pull
        resetMocks();
        assertCommand('pull', true);

        // fire
        resetMocks();
        assertCommand('fire', false);

        // ------------  getMemory ---------------
        var memory = robot.getMemory();
        assertEquals(false, memory.has('key'));

        memory.save('key', 'value');
        assertEquals(true, memory.has('key'));
        assertEquals(false, memory.has('key2'));

        assertEquals('value', memory.load('key'));
        assertEquals(undefined, memory.load('key2'));

        memory.save('key2', 'value2');
        assertEquals(true, memory.has('key'));
        assertEquals(true, memory.has('key2'));

        assertEquals('value', memory.load('key'));
        assertEquals('value2', memory.load('key2'));

        memory.remove('key');
        assertEquals(false, memory.has('key'));
        assertEquals(true, memory.has('key2'));

        assertEquals(undefined, memory.load('key'));
        assertEquals('value2', memory.load('key2'));

        memory.clean();
        assertEquals(false, memory.has('key'));
        assertEquals(false, memory.has('key2'));

        assertEquals(undefined, memory.load('key'));
        assertEquals(undefined, memory.load('key2'));

    }

    runTest();
}