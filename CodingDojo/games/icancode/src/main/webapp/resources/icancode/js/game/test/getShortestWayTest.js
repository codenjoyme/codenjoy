/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
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
var getShortestWayTest = function() {

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

    resetMocks = function () {
        robot.reset();
        loggerActions.length = 0;
    }

    game = {};
    var loggerActions = [];
    var controllerActions = [];

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

    runTest1 = function () {
        board = 'board={"heroPosition":{"x":16,"y":18},' +
            '"showName":true,' +
            '"levelFinished":false,' +
            '"offset":{"x":0,"y":0},' +
            '"layers":["' +
          //           1111111111
          // 01234567890123456789
            '    ╔═════════════┐ ' +     // 0
            '    ║Z...O.....$.Z│ ' +     // 1
            '    ║....O┌─╗.....│ ' +     // 2
            '  ╔═╝˅˂▼►▲│ ║.....│ ' +     // 3
            '  ║.$.▼...│ ║..$..│ ' +     // 4
            '  ║....┌──┘ └╗..O.│ ' +     // 5
            '  ║►O˅▼│     └─╗..│ ' +     // 6
            '  ║.˅►.╚═══┐   ║OO│ ' +     // 7
            '  ║..E.....╚═══╝..│ ' +     // 8       // TODO тут непроходимый барьер
            '  └────╗˃...O.O.O.│ ' +     // 9
            '       └╗...O┌────┘ ' +     // 10
            ' ╔═══┐  ║.O..│      ' +     // 11
            ' ║.$.│  ║.$O.╚════┐ ' +     // 12
            ' ║...╚══╝O..OO..O.│ ' +     // 13
            ' ║...OO...┌──╗....│ ' +     // 14
            ' └──╗O.$.O│  └────┘ ' +     // 15
            '    ║..OO.│         ' +     // 16
            ' ╔══╝.O...╚══════┐  ' +     // 17
            ' ║....O$........S│  ' +     // 18
            ' └───────────────┘  ","' +  // 19
          //           1111111111
          // 01234567890123456789
            '--------------------' +     // 0
            '-----------B-B-B----' +     // 1
            '-----B-------B------' +     // 2
            '------B♂B-----------' +     // 3
            '-------------B------' +     // 4
            '------B-------------' +     // 5
            '--------------------' +     // 6
            '--------------------' +     // 7
            '------BBB-----------' +     // 8 // TODO тут непроходимый барьер
            '---------→------→---' +     // 9
            '--------------------' +     // 10
            '--------------------' +     // 11
            '------------B-------' +     // 12
            '--------------------' +     // 13
            '---♀----------B-----' +     // 14
            '--------------------' +     // 15
            '--------------------' +     // 16
            '---------B----------' +     // 17
            '----------------☺---' +     // 18
            '--------------------","' +  // 19
          //           1111111111
          // 01234567890123456789
            '--------------------' +
            '--------------------' +
            '--------------------' +
            '--------------------' +
            '--------------------' +
            '--------------------' +
            '--------------------' +
            '--------------------' +
            '--------------------' +
            '--------------------' +
            '--------------------' +
            '--------------------' +
            '--------------------' +
            '--------------------' +
            '--------------------' +
            '--------------------' +
            '--------------------' +
            '--------------------' +
            '--------------------' +
            '--------------------"],' +
            '"levelProgress":{"total":18,"current":16,"lastPassed":15}}';


        robot = initRobot(logger, controller);
        var scanner = robot.getScanner();

        resetMocks();

        var me = scanner.getMe();
        assertEquals(['START','MY_ROBOT'],
            scanner.getAt(me.getX(), me.getY()));

        assertEquals([{'x':16,'y':18},{'x':15,'y':18},{'x':14,'y':18},{'x':13,'y':18},{'x':12,'y':18},{'x':11,'y':18},{'x':10,'y':18},{'x':9,'y':18},{'x':9,'y':17},{'x':9,'y':16},{'x':9,'y':15},{'x':9,'y':14},{'x':9,'y':13},{'x':9,'y':12},{'x':9,'y':11},{'x':9,'y':10},{'x':9,'y':9},{'x':10,'y':9},{'x':11,'y':9},{'x':12,'y':9},{'x':13,'y':9},{'x':14,'y':9},{'x':15,'y':9},{'x':16,'y':9},{'x':17,'y':9},{'x':17,'y':8},{'x':17,'y':7},{'x':17,'y':6},{'x':17,'y':5},{'x':17,'y':4},{'x':17,'y':3},{'x':17,'y':2},{'x':17,'y':1},{'x':16,'y':1},{'x':15,'y':1},{'x':14,'y':1},{'x':13,'y':1},{'x':12,'y':1},{'x':11,'y':1},{'x':10,'y':1},{'x':9,'y':1},{'x':8,'y':1},{'x':8,'y':2},{'x':8,'y':3},{'x':8,'y':4},{'x':7,'y':4},{'x':6,'y':4},{'x':5,'y':4},{'x':5,'y':5},{'x':4,'y':5},{'x':3,'y':5},{'x':3,'y':6},{'x':3,'y':7},{'x':3,'y':8},{'x':4,'y':8},{'x':5,'y':8}],
            scanner.getShortestWay(scanner.getExit()[0]));

    }

    runTest2 = function () {
        board = 'board={"heroPosition":{"x":7,"y":5},' +
            '"showName":true,' +
            '"levelFinished":false,' +
            '"offset":{"x":0,"y":0},' +
            '"layers":["' +
            '                    ' +
            '                    ' +
            '                    ' +
            '                    ' +
            '      ╔═════┐       ' +
            '      ║S.O..│       ' +
            '      └──╗..│       ' +
            '         ║..│       ' +
            '      ╔══╝..╚═┐     ' +
            '      ║$..OO..│     ' +
            '      ║.┌─╗...│     ' +
            '      ║.│ ║...│     ' +
            '      ║.╚═╝..E│     ' +
            '      ║.......│     ' +
            '      └───────┘     ' +
            '                    ' +
            '                    ' +
            '                    ' +
            '                    ' +
            '                    ","' +
            '--------------------' +
            '--------------------' +
            '--------------------' +
            '--------------------' +
            '--------------------' +
            '-------☺------------' +
            '--------------------' +
            '--------------------' +
            '--------------------' +
            '--------B-----------' +
            '--------------------' +
            '--------------------' +
            '--------------------' +
            '--------------------' +
            '--------------------' +
            '--------------------' +
            '--------------------' +
            '--------------------' +
            '--------------------' +
            '--------------------","' +
            '--------------------' +
            '--------------------' +
            '--------------------' +
            '--------------------' +
            '--------------------' +
            '--------------------' +
            '--------------------' +
            '--------------------' +
            '--------------------' +
            '--------------------' +
            '--------------------' +
            '--------------------' +
            '--------------------' +
            '--------------------' +
            '--------------------' +
            '--------------------' +
            '--------------------' +
            '--------------------' +
            '--------------------' +
            '--------------------"],' +
            '"levelProgress":{"total":18,"current":11,"lastPassed":16}}';

        robot = initRobot(logger, controller);
        var scanner = robot.getScanner();

        resetMocks();

        var me = scanner.getMe();
        assertEquals(['START','MY_ROBOT'],
            scanner.getAt(me.getX(), me.getY()));

        assertEquals([{'x':7,'y':5},{'x':8,'y':5},{'x':9,'y':5},{'x':10,'y':5},{'x':10,'y':6},{'x':10,'y':7},{'x':10,'y':8},{'x':11,'y':8},{'x':11,'y':9},{'x':11,'y':10},{'x':11,'y':11},{'x':11,'y':12},{'x':12,'y':12},{'x':13,'y':12}],
            scanner.getShortestWay(scanner.getStart()[0], scanner.getExit()[0]));

        assertEquals([{'x':7,'y':5},{'x':8,'y':5},{'x':9,'y':5},{'x':10,'y':5},{'x':10,'y':6},{'x':10,'y':7},{'x':10,'y':8},{'x':11,'y':8},{'x':11,'y':9},{'x':11,'y':10},{'x':11,'y':11},{'x':11,'y':12},{'x':11,'y':13},{'x':10,'y':13},{'x':9,'y':13},{'x':8,'y':13},{'x':7,'y':13},{'x':7,'y':12},{'x':7,'y':11},{'x':7,'y':10},{'x':7,'y':9}],
            scanner.getShortestWay(pt(7, 5), pt(7, 9)));

        assertEquals([{'x':11,'y':12},{'x':12,'y':12},{'x':13,'y':12}],
            scanner.getShortestWay(pt(11, 12), scanner.getExit()[0]));

        assertEquals([{'x':11,'y':12},{'x':11,'y':13},{'x':10,'y':13},{'x':9,'y':13},{'x':8,'y':13},{'x':7,'y':13},{'x':7,'y':12},{'x':7,'y':11},{'x':7,'y':10},{'x':7,'y':9}],
            scanner.getShortestWay(pt(11, 12), pt(7, 9)));

    }

    runTest1();
    runTest2();
}