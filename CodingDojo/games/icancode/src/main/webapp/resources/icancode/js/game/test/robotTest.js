/*-
 * #%L
 * iCanCode - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2018 EPAM
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
toString = function(data) {
    if (data === undefined || data == null) {
        return data;
    }
    return JSON.stringify(data).split('"').join('\'');
}

assertEquals = function(expected, actual) {
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

assertActions = function(expected, actual) {
    assertEquals(expected, actual);
    actual.length = 0;
}

assertMoved = function(expectedCameFrom, expectedPrevious, wasMoved) {
    if (!wasMoved) {
        expectedCameFrom = null;
        expectedPrevious = null;
    }
    assertEquals(expectedCameFrom, robot.cameFrom());
    assertEquals(expectedPrevious, robot.previousDirection());
}

assertCommand = function(name, wasMoved){
    var upper = name.toUpperCase();

    robot[name]();
    assertMoved(null, null, false);
    assertActions(['clean','command[' + upper + ']','wait'], controllerActions);

    robot[name]('UP');
    assertMoved('DOWN', 'UP', wasMoved);
    assertActions(['clean','command[' + upper + ',UP]','wait'], controllerActions);

    robot[name]('LEFT');
    assertMoved('RIGHT', 'LEFT', wasMoved);
    assertActions(['clean','command[' + upper + ',LEFT]','wait'], controllerActions);

    robot[name]('DOWN');
    assertMoved('UP', 'DOWN', wasMoved);
    assertActions(['clean','command[' + upper + ',DOWN]','wait'], controllerActions);

    robot[name]('RIGHT');
    assertMoved('LEFT', 'RIGHT', wasMoved);
    assertActions(['clean','command[' + upper + ',RIGHT]','wait'], controllerActions);

    robot[name]('QWE');
    assertActions(["Unexpected direction value 'QWE' please use: 'UP', 'DOWN', 'LEFT' or 'RIGHT'."], loggerActions);
    assertMoved('LEFT', 'RIGHT', wasMoved);
    assertActions([], controllerActions);

    robot[name]();
    assertMoved('null', 'null', false);
    assertActions(['clean','command[' + upper + ']','wait'], controllerActions);

    // jumpUp, jumpLeft, jumpDown, jumpRight
    robot[name + 'Up']();
    assertMoved('DOWN', 'UP', wasMoved);
    assertActions(['clean','command[' + upper + ',UP]','wait'], controllerActions);

    robot[name + 'Left']();
    assertMoved('RIGHT', 'LEFT', wasMoved);
    assertActions(['clean','command[' + upper + ',LEFT]','wait'], controllerActions);

    robot[name + 'Down']();
    assertMoved('UP', 'DOWN', wasMoved);
    assertActions(['clean','command[' + upper + ',DOWN]','wait'], controllerActions);

    robot[name + 'Right']();
    assertMoved('LEFT', 'RIGHT', wasMoved);
    assertActions(['clean','command[' + upper + ',RIGHT]','wait'], controllerActions);
}

resetMocks = function () {
    robot.reset();
    loggerActions.length = 0;
    controllerActions.length = 0;
}

runTest = function() {
    game = {};

    loggerActions = [];
    var logger = {
        print : function(message) {
            loggerActions.push(message);
        }
    };

    controllerActions = [];
    var controller = {
        commit : function() {
            controllerActions.push('commit');
        },
        reset : function() {
            controllerActions.push('reset');
        },
        onMessage : function() {
            controllerActions.push('commit');
        },
        reconnect : function() {
            controllerActions.push('reconnect');
        },
        cleanCommand : function() {
            controllerActions.push('clean');
        },
        addCommand : function(command) {
            controllerActions.push('command[' + command + ']');
        },
        waitCommand : function() {
            controllerActions.push('wait');
        },
        winCommand : function() {
            controllerActions.push('win');
        }
    };

    board = 'board={"layers":["' +
                        '╔═══════┐' + // 0
                        '║S.$◄..O│' + // 1
                        '║....$O.│' + // 2
                        '║.$E....│' + // 3
                        '║˃..O...│' + // 4
                        '║.O...Z.│' + // 5
                        '║..˄....│' + // 6
                        '║..˅˂▼►▲│' + // 7
                        '└───────┘",' + // 8
                      // 012345678
                       '"---------' + // 0
                        '--☺----o-' + // 1
                        '-X----x☻-' + // 2
                        '-X---B---' + // 3
                        '--→B-↓B--' + // 4
                        '-♂♂♀✝B---' + // 5
                        '--&--↑←--' + // 6
                        '---------' + // 7
                        '---------",' + // 8
                      // 012345678
                       '"---------' + // 0
                        '------*--' + // 1
                        '---------' + // 2
                        '---------' + // 3
                        '------^--' + // 4
                        '---------' + // 5
                        '---------' + // 6
                        '---------' + // 7
                        '---------"' + // 8
                        ']}';


    robot = initRobot(logger, controller);
    var scanner = robot.getScanner();

    // --------- board.getWholeBoard -----------
    resetMocks();

    assertEquals([[['WALL'],['WALL'],['WALL'],['WALL'],['WALL'],['WALL'],['WALL'],['WALL'],['WALL']],
        [['WALL'],['START'],['OTHER_ROBOT'],['OTHER_ROBOT'],['LASER_MACHINE'],['ZOMBIE'],['NONE'],['NONE'],['WALL']],
        [['WALL'],['MY_ROBOT'],['NONE'],['GOLD'],['LASER_RIGHT'],['HOLE','ZOMBIE'],['OTHER_ROBOT'],['NONE'],['WALL']],
        [['WALL'],['GOLD'],['NONE'],['EXIT'],['BOX'],['ZOMBIE'],['LASER_MACHINE'],['LASER_MACHINE'],['WALL']],
        [['WALL'],['LASER_MACHINE_READY'],['NONE'],['NONE'],['HOLE'],['ZOMBIE_DIE'],['NONE'],['LASER_MACHINE'],['WALL']],
        [['WALL'],['NONE'],['GOLD'],['BOX'],['LASER_DOWN'],['BOX'],['LASER_UP'],['LASER_MACHINE_READY'],['WALL']],
        [['WALL'],['MY_ROBOT'],['HOLE','OTHER_ROBOT'],['NONE'],['BOX','OTHER_ROBOT'],['ZOMBIE_START'],['LASER_LEFT'],['LASER_MACHINE_READY'],['WALL']],
        [['WALL'],['HOLE','MY_ROBOT'],['MY_ROBOT'],['NONE'],['NONE'],['NONE'],['NONE'],['LASER_MACHINE_READY'],['WALL']],
        [['WALL'],['WALL'],['WALL'],['WALL'],['WALL'],['WALL'],['WALL'],['WALL'],['WALL']]],
        scanner.getWholeBoard());

    // --------- getScanner --------------
    // at point
    resetMocks();

    assertEquals(['GOLD'],
        scanner.at(new Point(2, 3)));

    assertEquals(['GOLD'],
        scanner.at(2, 3));

    assertEquals(['OTHER_ROBOT'],
        scanner.at(new Point(1, 2)));

    assertEquals(['OTHER_ROBOT'],
        scanner.at(1, 2));

    assertEquals(['HOLE','OTHER_ROBOT'],
        scanner.at(6, 2));

    assertEquals(['BOX','OTHER_ROBOT'],
        scanner.at(6, 4));

    assertEquals(['OTHER_ROBOT'], //TODO here also laser
        scanner.at(2, 6));

    assertEquals(null,
        scanner.at(null));
    assertActions(["Expected direction or point but was 'null' please use: 'UP', 'DOWN', 'LEFT', 'RIGHT' or 'new Point(x, y)'."], loggerActions);

    assertEquals(null,
        scanner.at());
    assertActions(["Expected direction or point but was 'undefined' please use: 'UP', 'DOWN', 'LEFT', 'RIGHT' or 'new Point(x, y)'."], loggerActions);

    assertEquals(null,
        scanner.at('QWE'));
    assertActions(["Expected direction or point but was 'QWE' please use: 'UP', 'DOWN', 'LEFT', 'RIGHT' or 'new Point(x, y)'."], loggerActions);

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
        scanner.atNearRobot(-1, 1));

    assertEquals(['MY_ROBOT'], // мoй другой робот что летает LAYER3
        scanner.atNearRobot(4, 0));

    assertEquals(['BOX','OTHER_ROBOT'], // чужой робот, что летает LAYER3
        scanner.atNearRobot(4, 3));

    assertEquals(null,
        scanner.atNearRobot());
    assertActions(["You tried to call function(x, y) where 'x' and 'y' are numbers, with parameters []."], loggerActions);

    assertEquals(null,
        scanner.atNearRobot('1', '2'));
    assertActions(["You tried to call function(x, y) where 'x' and 'y' are numbers, with parameters [1,2]."], loggerActions);

    assertEquals(null,
        scanner.atNearRobot('ASD', 'QWE', false));
    assertActions(["You tried to call function(x, y) where 'x' and 'y' are numbers, with parameters [ASD,QWE,false]."], loggerActions);

    // getMe
    resetMocks();

    assertEquals({'x':2,'y':1}, scanner.getMe());

    // TODO what if Hero not on board?
    // TODO what if Hero is flying
    // TODO ...or falling to hole
    // TODO ...or die on laser?

    // isAt
    resetMocks();

    assertEquals(false,
        scanner.isAt(2, 1, 'OTHER_ROBOT'));

    assertEquals(true,
        scanner.isAt(2, 1, 'MY_ROBOT'));

    assertEquals(false,
        scanner.isAt(2, 1, ['MY_ROBOT', 'GOLD']));
    assertEquals(true,
        scanner.isAnyOfAt(2, 1, ['MY_ROBOT', 'GOLD']));

    assertEquals(true,
        scanner.isAt(2, 1, ['MY_ROBOT', 'NONE']));

    assertEquals(false,
        scanner.isAt(2, 1, ['OTHER_ROBOT', 'HOLE', 'ZOMBIE']));

    assertEquals(true,
        scanner.isAt(6, 1, ['MY_ROBOT'])); // мой летает на LAYER3

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

    // getAt
    resetMocks();

    assertEquals(['MY_ROBOT'],
        scanner.getAt(2, 1));

    assertEquals(['NONE'],
        scanner.getAt(2, 2));

    assertEquals(['EXIT'],
        scanner.getAt(3, 3));

    assertEquals(['GOLD'],
        scanner.getAt(3, 1));

    assertEquals(['MY_ROBOT'],
        scanner.getAt(6, 1));

    assertEquals(['BOX','OTHER_ROBOT'],
        scanner.getAt(6, 4));

    assertEquals(null,
        scanner.getAt());
    assertActions(["You tried to call function(x, y) where 'x' and 'y' are numbers, with parameters []."], loggerActions);

    assertEquals(null,
        scanner.getAt('1', 'HERO'));
    assertActions(["You tried to call function(x, y) where 'x' and 'y' are numbers, with parameters [1,HERO]."], loggerActions);

    // findAll
    resetMocks();

    assertEquals([{'x':2,'y':3},{'x':3,'y':1},{'x':5,'y':2}],
        scanner.findAll('GOLD'));

    assertEquals([{'x':1,'y':2},{'x':1,'y':3},{'x':6,'y':2},{'x':6,'y':4},{'x':2,'y':6}],
        scanner.findAll('OTHER_ROBOT'));

    assertEquals([{'x':2,'y':1},{'x':7,'y':1},{'x':6,'y':1},{'x':7,'y':2}],
        scanner.findAll('MY_ROBOT'));

    assertEquals([{'x':1,'y':2},{'x':1,'y':3},{'x':6,'y':2},{'x':6,'y':4},{'x':2,'y':6},{'x':2,'y':1},{'x':7,'y':1},{'x':6,'y':1},{'x':7,'y':2}],
        scanner.findAll(['OTHER_ROBOT','MY_ROBOT']));

    assertEquals([{'x':2,'y':3},{'x':3,'y':1},{'x':5,'y':2}],
        scanner.findAll(['GOLD']));

    assertEquals([],
        scanner.findAll([]));

    assertEquals([{'x':2,'y':3},{'x':3,'y':1},{'x':5,'y':2},{'x':1,'y':1},{'x':3,'y':3}],
        scanner.findAll(['GOLD','START','EXIT']));

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
        scanner.isAnyOfAt(2, 1, 'OTHER_ROBOT'));

    assertEquals(true,
        scanner.isAnyOfAt(6, 1, ['OTHER_ROBOT','MY_ROBOT']));

    assertEquals(true,
        scanner.isAnyOfAt(6, 4, ['OTHER_ROBOT','MY_ROBOT']));

    assertEquals(true,
        scanner.isAnyOfAt(2, 1, 'MY_ROBOT'));

    assertEquals(true,
        scanner.isAnyOfAt(2, 1, ['MY_ROBOT', 'GOLD']));
    assertEquals(false,
        scanner.isAt(2, 1, ['MY_ROBOT', 'GOLD']));

    assertEquals(false,
        scanner.isAnyOfAt(2, 1, ['OTHER_ROBOT', 'HOLE', 'ZOMBIE']));

    assertEquals(false,
        scanner.isAnyOfAt(2, 1));
    assertActions(["You tried to call function(x, y, elements) where 'x' and 'y' are numbers, and 'elements' is string or array of strings, with parameters [2,1]."], loggerActions);

    assertEquals(false,
        scanner.isAnyOfAt());
    assertActions(["You tried to call function(x, y, elements) where 'x' and 'y' are numbers, and 'elements' is string or array of strings, with parameters []."], loggerActions);

    assertEquals(false,
        scanner.isAnyOfAt(1, 2, [3, 4]));
    assertActions(["You tried to call function(x, y, elements) where 'x' and 'y' are numbers, and 'elements' is string or array of strings, with parameters [1,2,3,4]."], loggerActions);

    // isNear
    resetMocks();

    assertEquals(true,
        scanner.isNear(2, 2, 'OTHER_ROBOT'));

    assertEquals(true,
        scanner.isNear(2, 2, 'MY_ROBOT'));

    assertEquals(false,
        scanner.isNear(2, 2, 'ZOMBIE'));

    assertEquals(false,
        scanner.isNear(2, 2, ['ZOMBIE', 'HOLE']));

    assertEquals(true,
        scanner.isNear(5, 1, ['OTHER_ROBOT','MY_ROBOT']));

    assertEquals(true,
        scanner.isNear(5, 4, ['OTHER_ROBOT','MY_ROBOT']));

    assertEquals(true,
        scanner.isNear(2, 2, ['MY_ROBOT', 'GOLD']));

    assertEquals(true,
        scanner.isNear(2, 2, ['OTHER_ROBOT', 'HOLE', 'ZOMBIE']));

    assertEquals(false,
        scanner.isNear(2, 2));
    assertActions(["You tried to call function(x, y, elements) where 'x' and 'y' are numbers, and 'elements' is string or array of strings, with parameters [2,2]."], loggerActions);

    assertEquals(false,
        scanner.isNear());
    assertActions(["You tried to call function(x, y, elements) where 'x' and 'y' are numbers, and 'elements' is string or array of strings, with parameters []."], loggerActions);

    assertEquals(false,
        scanner.isNear(1, 2, [3, 4]));
    assertActions(["You tried to call function(x, y, elements) where 'x' and 'y' are numbers, and 'elements' is string or array of strings, with parameters [1,2,3,4]."], loggerActions);

    // isBarrierAt
    resetMocks();

    assertEquals(false,
        scanner.isBarrierAt(2, 2));

    assertEquals(true,
        scanner.isBarrierAt(0, 0));

    assertEquals(true,
        scanner.isBarrierAt(0, 0));

    assertEquals(true,
        scanner.isBarrierAt(3, 4));

    assertEquals(true,
        scanner.isBarrierAt(4, 1));

    assertEquals(false,
        scanner.isBarrierAt());
    assertActions(["You tried to call function(x, y) where 'x' and 'y' are numbers, with parameters []."], loggerActions);

    assertEquals(false,
        scanner.isBarrierAt('1', '2'));
    assertActions(["You tried to call function(x, y) where 'x' and 'y' are numbers, with parameters [1,2]."], loggerActions);

    assertEquals(false,
        scanner.isBarrierAt('ASD', 'QWE', false));
    assertActions(["You tried to call function(x, y) where 'x' and 'y' are numbers, with parameters [ASD,QWE,false]."], loggerActions);

    // countNear
    resetMocks();

    assertEquals(1,
        scanner.countNear(2, 2, 'GOLD'));

    assertEquals(1,
        scanner.countNear(2, 2, 'MY_ROBOT'));

    assertEquals(0,
        scanner.countNear(5, 1, ['OTHER_ROBOT']));

    assertEquals(1,
        scanner.countNear(5, 1, ['MY_ROBOT']));

    assertEquals(1,
        scanner.countNear(5, 4, ['OTHER_ROBOT','MY_ROBOT']));

    assertEquals(1,
        scanner.countNear(2, 2, 'OTHER_ROBOT'));

    assertEquals(10, // TODO should be 3
        scanner.countNear(3, 2, 'NONE'));

    assertEquals(2, // TODO should be 3
            scanner.countNear(1, 1, 'WALL'));

    assertEquals(false,
        scanner.countNear(2, 2));
    assertActions(["You tried to call function(x, y, elements) where 'x' and 'y' are numbers, and 'elements' is string or array of strings, with parameters [2,2]."], loggerActions);

    assertEquals(false,
        scanner.countNear());
    assertActions(["You tried to call function(x, y, elements) where 'x' and 'y' are numbers, and 'elements' is string or array of strings, with parameters []."], loggerActions);

    assertEquals(false,
        scanner.countNear(1, 2, [3, 4]));
    assertActions(["You tried to call function(x, y, elements) where 'x' and 'y' are numbers, and 'elements' is string or array of strings, with parameters [1,2,3,4]."], loggerActions);

    // getOtherRobots
    resetMocks();

    assertEquals([{'x':1,'y':2},{'x':1,'y':3},{'x':2,'y':6},{'x':6,'y':2},{'x':6,'y':4}],
        scanner.getOtherRobots());

    // getLaserMachines
    resetMocks();

    assertEquals([{'x':1,'y':4,'direction':'RIGHT'},{'x':3,'y':6,'direction':'UP'},{'x':3,'y':7,'direction':'DOWN'},{'x':4,'y':1,'direction':'LEFT'},{'x':4,'y':7,'direction':'LEFT'},{'x':5,'y':7,'direction':'DOWN'},{'x':6,'y':7,'direction':'RIGHT'},{'x':7,'y':7,'direction':'UP'}],
        scanner.getLaserMachines());

    // getLasers
    resetMocks();

    assertEquals([{'x':2,'y':4,'direction':'RIGHT'},{'x':5,'y':4,'direction':'DOWN'},{'x':5,'y':6,'direction':'UP'},{'x':6,'y':6,'direction':'LEFT'}],
        scanner.getLasers());

    // getWalls
    resetMocks();

    assertEquals([{'x':0,'y':0},{'x':0,'y':1},{'x':0,'y':2},{'x':0,'y':3},{'x':0,'y':4},{'x':0,'y':5},{'x':0,'y':6},{'x':0,'y':7},{'x':0,'y':8},{'x':1,'y':0},{'x':1,'y':8},{'x':2,'y':0},{'x':2,'y':8},{'x':3,'y':0},{'x':3,'y':8},{'x':4,'y':0},{'x':4,'y':8},{'x':5,'y':0},{'x':5,'y':8},{'x':6,'y':0},{'x':6,'y':8},{'x':7,'y':0},{'x':7,'y':8},{'x':8,'y':0},{'x':8,'y':1},{'x':8,'y':2},{'x':8,'y':3},{'x':8,'y':4},{'x':8,'y':5},{'x':8,'y':6},{'x':8,'y':7},{'x':8,'y':8}],
        scanner.getWalls());

    // getBoxes
    resetMocks();

    assertEquals([{'x':3,'y':4},{'x':5,'y':3},{'x':5,'y':5},{'x':6,'y':4}],
        scanner.getBoxes());

    // getGold
    resetMocks();

    assertEquals([{'x':2,'y':3},{'x':3,'y':1},{'x':5,'y':2}],
        scanner.getGold());

    // getStart
    resetMocks();

    assertEquals([{'x':1,'y':1}],
        scanner.getStart());

    // getZombieStart
    resetMocks();

    assertEquals([{'x':6,'y':5}],
        scanner.getZombieStart());

    // getExit
    resetMocks();

    assertEquals([{'x':3,'y':3}],
        scanner.getExit());

    // getHoles
    resetMocks();

    assertEquals([{'x':2,'y':5},{'x':4,'y':4},{'x':6,'y':2},{'x':7,'y':1}],
        scanner.getHoles());

    // getBarriers
    resetMocks();

    assertEquals([{'x':0,'y':0},{'x':0,'y':1},{'x':0,'y':2},{'x':0,'y':3},{'x':0,'y':4},{'x':0,'y':5},{'x':0,'y':6},{'x':0,'y':7},{'x':0,'y':8},{'x':1,'y':0},{'x':1,'y':8},{'x':2,'y':0},{'x':2,'y':8},{'x':3,'y':0},{'x':3,'y':8},{'x':4,'y':0},{'x':4,'y':8},{'x':5,'y':0},{'x':5,'y':8},{'x':6,'y':0},{'x':6,'y':8},{'x':7,'y':0},{'x':7,'y':8},{'x':8,'y':0},{'x':8,'y':1},{'x':8,'y':2},{'x':8,'y':3},{'x':8,'y':4},{'x':8,'y':5},{'x':8,'y':6},{'x':8,'y':7},{'x':8,'y':8},{'x':1,'y':4,'direction':'RIGHT'},{'x':3,'y':6,'direction':'UP'},{'x':3,'y':7,'direction':'DOWN'},{'x':4,'y':1,'direction':'LEFT'},{'x':4,'y':7,'direction':'LEFT'},{'x':5,'y':7,'direction':'DOWN'},{'x':6,'y':7,'direction':'RIGHT'},{'x':7,'y':7,'direction':'UP'},{'x':3,'y':4},{'x':5,'y':3},{'x':5,'y':5},{'x':6,'y':4}],
        scanner.getBarriers());

    // getElements
    resetMocks();

    assertEquals(['NONE','WALL','LASER_MACHINE','LASER_MACHINE_READY','START','EXIT','HOLE','BOX','ZOMBIE_START','GOLD','MY_ROBOT','OTHER_ROBOT','LASER_LEFT','LASER_RIGHT','LASER_UP','LASER_DOWN','ZOMBIE','ZOMBIE_DIE'],
        scanner.getElements());

    // getShortestWay deadloop
    resetMocks();

    assertEquals([],
        scanner.getShortestWay(new Point(0, 0), scanner.getMe()));

    // getShortestWay with 1 point
    resetMocks();

    assertEquals([{'x':2,'y':1}],
            scanner.getShortestWay(scanner.getMe()));

    assertEquals([{'x':2,'y':1},{'x':2,'y':2},{'x':2,'y':3},{'x':3,'y':3}],
        scanner.getShortestWay(scanner.getExit()[0]));

    assertEquals([{'x':2,'y':1},{'x':1,'y':1}],
        scanner.getShortestWay(scanner.getStart()[0]));

    assertEquals([{'x':2,'y':1},{'x':2,'y':2},{'x':2,'y':3},{'x':2,'y':4},{'x':2,'y':5},{'x':3,'y':5},{'x':4,'y':5},{'x':4,'y':6},{'x':5,'y':6},{'x':6,'y':6}],
        scanner.getShortestWay(new Point(6, 6)));

    assertEquals(null,
        scanner.getShortestWay(1));
    assertActions(["You tried to call function(point) with parameters [1]."], loggerActions);

    assertEquals(null,
        scanner.getShortestWay(null));
    assertActions(["You tried to call function(point) with parameters []."], loggerActions);

    assertEquals(null,
        scanner.getShortestWay([new Point(1, 2)]));
    assertActions(["You tried to call function(point) with parameters [[1,2]]."], loggerActions);

    assertEquals(null,
        scanner.getShortestWay('string'));
    assertActions(["You tried to call function(point) with parameters [string]."], loggerActions);

    // getShortestWay with 2 points
    resetMocks();

    assertEquals([{'x':2,'y':1}],
        scanner.getShortestWay(new Point(2, 1), scanner.getMe()));

    assertEquals([{'x':2,'y':1},{'x':2,'y':2},{'x':2,'y':3},{'x':3,'y':3}],
        scanner.getShortestWay(new Point(2, 1), scanner.getExit()[0]));

    assertEquals([{'x':2,'y':1},{'x':1,'y':1}],
        scanner.getShortestWay(new Point(2, 1), scanner.getStart()[0]));

    assertEquals([{'x':2,'y':1},{'x':2,'y':2},{'x':2,'y':3},{'x':2,'y':4},{'x':2,'y':5},{'x':3,'y':5},{'x':4,'y':5},{'x':4,'y':6},{'x':5,'y':6},{'x':6,'y':6}],
        scanner.getShortestWay(new Point(2, 1), new Point(6, 6)));

    assertEquals(null,
        scanner.getShortestWay(1, 2));
    assertActions(["You tried to call function(point, point) with parameters [1,2]."], loggerActions);

    assertEquals(null,
        scanner.getShortestWay(null, 2));
    assertActions(["You tried to call function(point, point) with parameters [,2]."], loggerActions);

    assertEquals(null,
        scanner.getShortestWay([new Point(1, 2)],[1]));
    assertActions(["You tried to call function(point, point) with parameters [[1,2],1]."], loggerActions);

    assertEquals(null,
        scanner.getShortestWay(new Point(1, 2), 3));

    assertActions(["You tried to call function(point, point) with parameters [[1,2],3]."], loggerActions);

    // isMyRobotAlive
    resetMocks();

    assertEquals(true,
        scanner.isMyRobotAlive());

    // ------------- other Robot methods ---------------
    // nextLevel
    robot.nextLevel();
    assertActions(['win','wait'], controllerActions);

    // log
    robot.log('message');
    robot.log('message2');
    assertActions(['Robot says: message','Robot says: message2'], loggerActions);

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
    assertActions(['clean','command[UP]'], controllerActions);

    robot.go('LEFT');
    assertEquals('RIGHT', robot.cameFrom());
    assertEquals('LEFT', robot.previousDirection());
    assertActions(['clean','command[LEFT]'], controllerActions);

    robot.go('DOWN');
    assertEquals('UP', robot.cameFrom());
    assertEquals('DOWN', robot.previousDirection());
    assertActions(['clean','command[DOWN]'], controllerActions);

    robot.go('RIGHT');
    assertEquals('LEFT', robot.cameFrom());
    assertEquals('RIGHT', robot.previousDirection());
    assertActions(['clean','command[RIGHT]'], controllerActions);

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
    assertActions(['clean','command[UP]'], controllerActions);

    robot.goLeft();
    assertEquals('RIGHT', robot.cameFrom());
    assertEquals('LEFT', robot.previousDirection());
    assertActions(['clean','command[LEFT]'], controllerActions);

    robot.goDown();
    assertEquals('UP', robot.cameFrom());
    assertEquals('DOWN', robot.previousDirection());
    assertActions(['clean','command[DOWN]'], controllerActions);

    robot.goRight();
    assertEquals('LEFT', robot.cameFrom());
    assertEquals('RIGHT', robot.previousDirection());
    assertActions(['clean','command[RIGHT]'], controllerActions);

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
