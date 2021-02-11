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
        board = 'board={"heroPosition":{"x":16,"y":1},' +
            '"showName":true,' +
            '"levelFinished":false,' +
            '"offset":{"x":0,"y":0},' +
            '"layers":["' +
          //           1111111111
          // 01234567890123456789
            '    ╔═════════════┐ ' +     // 19
            '    ║Z...O.....$.Z│ ' +     // 18
            '    ║....O┌─╗.....│ ' +     // 17
            '  ╔═╝˅˂▼►▲│ ║.....│ ' +     // 16
            '  ║.$.▼...│ ║..$..│ ' +     // 15
            '  ║....┌──┘ └╗..O.│ ' +     // 14
            '  ║►O˅▼│     └─╗..│ ' +     // 13
            '  ║.˅►.╚═══┐   ║OO│ ' +     // 12
            '  ║..E.....╚═══╝..│ ' +     // 11      // TODO тут непроходимый барьер
            '  └────╗˃...O.O.O.│ ' +     // 10
            '       └╗...O┌────┘ ' +     //  9
            ' ╔═══┐  ║.O..│      ' +     //  8
            ' ║.$.│  ║.$O.╚════┐ ' +     //  7
            ' ║...╚══╝O..OO..O.│ ' +     //  6
            ' ║...OO...┌──╗....│ ' +     //  5
            ' └──╗O.$.O│  └────┘ ' +     //  4
            '    ║..OO.│         ' +     //  3
            ' ╔══╝.O...╚══════┐  ' +     //  2
            ' ║....O$........S│  ' +     //  1
            ' └───────────────┘  ","' +  //  0
          //           1111111111
          // 01234567890123456789
            '--------------------' +     // 19
            '-----------B-B-B----' +     // 18
            '-----B-------B------' +     // 17
            '------B♂B-----------' +     // 16
            '-------------B------' +     // 15
            '------B-------------' +     // 14
            '--------------------' +     // 13
            '--------------------' +     // 12
            '------BBB-----------' +     // 11  // TODO тут непроходимый барьер
            '---------→------→---' +     // 10
            '--------------------' +     //  9
            '--------------------' +     //  8
            '------------B-------' +     //  7
            '--------------------' +     //  6
            '---♀----------B-----' +     //  5
            '--------------------' +     //  4
            '--------------------' +     //  3
            '---------B----------' +     //  2
            '----------------☺---' +     //  1
            '--------------------","' +  //  0
          //           1111111111
          // 01234567890123456789
            '--------------------' +     // 19
            '--------------------' +     // 18
            '--------------------' +     // 17
            '--------------------' +     // 16
            '--------------------' +     // 15
            '--------------------' +     // 14
            '--------------------' +     // 13
            '--------------------' +     // 12
            '--------------------' +     // 11
            '--------------------' +     // 10
            '--------------------' +     //  9
            '--------------------' +     //  8
            '--------------------' +     //  7
            '--------------------' +     //  6
            '--------------------' +     //  5
            '--------------------' +     //  4
            '--------------------' +     //  3
            '--------------------' +     //  2
            '--------------------' +     //  1
            '--------------------"],' +  //  0
            '"levelProgress":{"total":18,"current":16,"lastPassed":15}}';


        robot = initRobot(logger, controller);
        var scanner = robot.getScanner();

        resetMocks();

        var me = scanner.getMe();
        assertEquals(['START','MY_ROBOT'],
            scanner.getAt(me.getX(), me.getY()));

        assertEquals([{'x':16,'y':1},{'x':15,'y':1},{'x':14,'y':1},{'x':13,'y':1},
                    {'x':12,'y':1},{'x':11,'y':1},{'x':10,'y':1},{'x':9,'y':1},{'x':9,'y':2},
                    {'x':9,'y':3},{'x':9,'y':4},{'x':9,'y':5},{'x':9,'y':6},{'x':9,'y':7},
                    {'x':9,'y':8},{'x':9,'y':9},{'x':9,'y':10},{'x':10,'y':10},{'x':11,'y':10},
                    {'x':12,'y':10},{'x':13,'y':10},{'x':14,'y':10},{'x':15,'y':10},{'x':16,'y':10},
                    {'x':17,'y':10},{'x':17,'y':11},{'x':17,'y':12},{'x':17,'y':13},{'x':17,'y':14},
                    {'x':17,'y':15},{'x':17,'y':16},{'x':17,'y':17},{'x':17,'y':18},{'x':16,'y':18},
                    {'x':15,'y':18},{'x':14,'y':18},{'x':13,'y':18},{'x':12,'y':18},{'x':11,'y':18},
                    {'x':10,'y':18},{'x':9,'y':18},{'x':8,'y':18},{'x':8,'y':17},{'x':8,'y':16},
                    {'x':8,'y':15},{'x':7,'y':15},{'x':6,'y':15},{'x':5,'y':15},{'x':5,'y':14},
                    {'x':4,'y':14},{'x':3,'y':14},{'x':3,'y':13},{'x':3,'y':12},{'x':3,'y':11},
                    {'x':4,'y':11},{'x':5,'y':11}],
            scanner.getShortestWay(scanner.getExit()[0]));

    }

    runTest2 = function () {
        board = 'board={"heroPosition":{"x":7,"y":14},' +
            '"showName":true,' +
            '"levelFinished":false,' +
            '"offset":{"x":0,"y":0},' +
            '"layers":["' +
          //           1111111111
          // 01234567890123456789
            '                    ' +   // 19
            '                    ' +   // 18
            '                    ' +   // 17
            '                    ' +   // 16
            '      ╔═════┐       ' +   // 15
            '      ║S.O..│       ' +   // 14
            '      └──╗..│       ' +   // 13
            '         ║..│       ' +   // 12
            '      ╔══╝..╚═┐     ' +   // 11
            '      ║$..OO..│     ' +   // 10
            '      ║.┌─╗...│     ' +   //  9
            '      ║.│ ║...│     ' +   //  8
            '      ║.╚═╝..E│     ' +   //  7
            '      ║.......│     ' +   //  6
            '      └───────┘     ' +   //  5
            '                    ' +   //  4
            '                    ' +   //  3
            '                    ' +   //  2
            '                    ' +   //  1
            '                    ","' +//  0
          //           1111111111
          // 01234567890123456789
            '--------------------' +   // 19
            '--------------------' +   // 18
            '--------------------' +   // 17
            '--------------------' +   // 16
            '--------------------' +   // 15
            '-------☺------------' +   // 14
            '--------------------' +   // 13
            '--------------------' +   // 12
            '--------------------' +   // 11
            '--------B-----------' +   // 10
            '--------------------' +   //  9
            '--------------------' +   //  8
            '--------------------' +   //  7
            '--------------------' +   //  6
            '--------------------' +   //  5
            '--------------------' +   //  4
            '--------------------' +   //  3
            '--------------------' +   //  2
            '--------------------' +   //  1
            '--------------------","' +//  0
          //           1111111111
          // 01234567890123456789
            '--------------------' +   // 19
            '--------------------' +   // 18
            '--------------------' +   // 17
            '--------------------' +   // 16
            '--------------------' +   // 15
            '--------------------' +   // 14
            '--------------------' +   // 13
            '--------------------' +   // 12
            '--------------------' +   // 11
            '--------------------' +   // 10
            '--------------------' +   //  9
            '--------------------' +   //  8
            '--------------------' +   //  7
            '--------------------' +   //  6
            '--------------------' +   //  5
            '--------------------' +   //  4
            '--------------------' +   //  3
            '--------------------' +   //  2
            '--------------------' +   //  1
            '--------------------"],' +//  0
            '"levelProgress":{"total":18,"current":11,"lastPassed":16}}';

        robot = initRobot(logger, controller);
        var scanner = robot.getScanner();

        resetMocks();

        var me = scanner.getMe();
        assertEquals(['START','MY_ROBOT'],
            scanner.getAt(me.getX(), me.getY()));

        assertEquals([{'x':7,'y':14},{'x':8,'y':14},{'x':9,'y':14},{'x':10,'y':14},{'x':10,'y':13},
                    {'x':10,'y':12},{'x':10,'y':11},{'x':11,'y':11},{'x':11,'y':10},{'x':11,'y':9},
                    {'x':11,'y':8},{'x':11,'y':7},{'x':12,'y':7},{'x':13,'y':7}],
            scanner.getShortestWay(scanner.getStart()[0], scanner.getExit()[0]));

        assertEquals([{'x':7,'y':14},{'x':8,'y':14},{'x':9,'y':14},{'x':10,'y':14},
                    {'x':10,'y':13},{'x':10,'y':12},{'x':10,'y':11},{'x':11,'y':11},
                    {'x':11,'y':10},{'x':11,'y':9},{'x':11,'y':8},{'x':11,'y':7},
                    {'x':11,'y':6},{'x':10,'y':6},{'x':9,'y':6},{'x':8,'y':6},{'x':7,'y':6},
                    {'x':7,'y':7},{'x':7,'y':8},{'x':7,'y':9},{'x':7,'y':10}],
            scanner.getShortestWay(pt(7, 14), pt(7, 10)));

        assertEquals([{'x':11,'y':7},{'x':12,'y':7},{'x':13,'y':7}],
            scanner.getShortestWay(pt(11, 7), scanner.getExit()[0]));

        assertEquals([{'x':11,'y':7},{'x':11,'y':6},{'x':10,'y':6},{'x':9,'y':6},
                    {'x':8,'y':6},{'x':7,'y':6},{'x':7,'y':7},{'x':7,'y':8},{'x':7,'y':9},{'x':7,'y':10}],
            scanner.getShortestWay(pt(11, 7), pt(7, 10)));

    }

    runTest1();
    runTest2();
}