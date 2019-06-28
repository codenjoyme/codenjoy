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

        board = 'board={"heroPosition":{"x":16,"y":18},' +
            '"showName":true,' +
            '"levelFinished":false,' +
            '"scannerOffset":{"x":0,"y":0},' +
            '"layers":["' +

            '    ╔═════════════┐ ' +
            '    ║Z.....E...$.Z│ ' +
            '    ║....O┌─╗.....│ ' +
            '  ╔═╝.....│ ║.....│ ' +
            '  ║.$.....│ ║..$..│ ' +
            '  ║....┌──┘ └╗..O.│ ' +
            '  ║.O..│     └─╗..│ ' +
            '  ║..$.╚═══┐   ║OO│ ' +
            '  ║........╚═══╝OO│ ' + // TODO тут непроходимый барьер
            '  └────╗˃.........│ ' +
            '       └╗....┌────┘ ' +
            ' ╔═══┐  ║.O..│      ' +
            ' ║.$.│  ║.$..╚════┐ ' +
            ' ║...╚══╝.......O.│ ' +
            ' ║....O...┌──╗....│ ' +
            ' └──╗..$..│  └────┘ ' +
            '    ║...O.│         ' +
            ' ╔══╝.....╚══════┐  ' +
            ' ║....O$........S│  ' +
            ' └───────────────┘  ","' +

            '--------------------' +
            '--------------------' +
            '-----B-------B------' +
            '------B♂B-----------' +
            '-------------B------' +
            '------B----------♀--' +
            '--------------------' +
            '--------------------' +
            '--BBBBBBBB----------' + // TODO тут непроходимый барьер
            '---------→------→---' +
            '--------------------' +
            '--------------------' +
            '------------B-------' +
            '--------------------' +
            '--------------B-----' +
            '--------------------' +
            '--------------------' +
            '---------B----------' +
            '----------------☺---' +
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
            '"levelProgress":{"total":18,"current":16,"lastPassed":15}}';


        robot = initRobot(logger, controller);
        var scanner = robot.getScanner();

        resetMocks();

        var me = scanner.getMe();
        assertEquals(['START','MY_ROBOT'],
            scanner.getAt(me.getX(), me.getY()));

        assertEquals([{'x':16,'y':18},{'x':15,'y':18},{'x':14,'y':18},{'x':13,'y':18},{'x':12,'y':18},{'x':11,'y':18},{'x':10,'y':18},{'x':9,'y':18},{'x':8,'y':18},{'x':8,'y':17},{'x':8,'y':16},{'x':8,'y':15},{'x':8,'y':14},{'x':9,'y':14},{'x':9,'y':13},{'x':9,'y':12},{'x':9,'y':11},{'x':9,'y':10},{'x':9,'y':9},{'x':10,'y':9},{'x':11,'y':9},{'x':12,'y':9},{'x':13,'y':9},{'x':14,'y':9},{'x':15,'y':9},{'x':16,'y':9},{'x':16,'y':8},{'x':16,'y':7},{'x':16,'y':6},{'x':16,'y':5},{'x':16,'y':4},{'x':16,'y':3},{'x':16,'y':2},{'x':16,'y':1},{'x':15,'y':1},{'x':14,'y':1},{'x':13,'y':1},{'x':12,'y':1},{'x':11,'y':1}],
            scanner.getShortestWay(scanner.getExit()[0]));

    }

    runTest();
}