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

var initLevelInfo = function() {
    var levelInfo = [];

    var getInfo = function(level) {
        var result = levelInfo[level];
        if (!result) {
            result = {
                'help':'<pre>// under construction</pre>',
                'defaultCode':'function program(robot) {\n'  +
                '    // TODO write your code here\n' +
                '}',
                'winCode':'function program(robot) {\n'  +
                '    robot.nextLevel();\n' +
                '}',
                'refactoringCode':'function program(robot) {\n'  +
                '    robot.nextLevel();\n' +
                '}'
            };
        }
        return result;
    }

    levelInfo[1] = {
        'help':'Robot asks for new orders every second. He should know where to go.<br>' +
        'Help him - write program and save him from the Maze. <br>' +
        'Hurry, the zombies are not as fast as you.<br>' +
        'The code looks like this:<br>' +
        '<pre>function program(robot) {\n' +
        '    var scanner = robot.getScanner();\n' +
        '    if (scanner.atRight() != "WALL") {\n' +
        '        robot.goRight();\n' +
        '    } else {\n' +
        '        // TODO Uncomment one line that will help\n' +
        '        // robot.goDown();\n' +
        '        // robot.goUp();\n' +
        '        // robot.goLeft();\n' +
        '        // robot.goRight();\n' +
        '    }\n' +
        '}</pre>' +
        'Scanner will help robot to detect walls and other obstacles.<br>' +
        'You also can use methods for refactoring:<br>' +
        '<pre>scanner.at("RIGHT");\n' +
        'robot.go("LEFT");</pre>' +
        'If you want to know where we came from - use this expression:<br>' +
        '<pre>robot.cameFrom() == "LEFT"</pre>' +
        'If you want to know where we came to on our previous step, use:<br>' +
        '<pre>robot.previousDirection() == "RIGHT"</pre>' +
        'You can use these commands with previous to tell robot to go on one direction, like:<br>' +
        '<pre>robot.go(robot.previousDirection());</pre>' +
        'To show data in console you can use this method:<br>' +
        '<pre>var someVariable = "someData";\n' +
        'robot.log(someVariable);</pre>' +
        'Send program to Robot by clicking the Commit button.<br>' +
        'If something is wrong - check Robot message in the Console (the rightmost field).<br>' +
        'You can always stop the program by clicking the Reset button.',
        'defaultCode':'function program(robot) {\n' +
        '    var scanner = robot.getScanner();\n' +
        '    if (scanner.atRight() != "WALL") {\n' +
        '        robot.goRight();\n' +
        '    } else {\n' +
        '        // TODO write your code here\n' +
        '    }\n' +
        '}',
        'winCode':'',
        'autocomplete': {
            'robot.':{
                'synonyms':[],
                'values':['goDown()', 'goUp()', 'goLeft()', 'goRight()', 'getScanner()',
                        'cameFrom()', 'previousDirection()', 'go()', 'log()']
            },
            'scanner.':{
                'synonyms':['robot.getScanner().'],
                'values':['atRight()', 'atLeft()', 'atUp()', 'atDown()', 'at()']
            },
            ' == ':{
                'synonyms':[' != '],
                'values':['\'WALL\'', '\'RIGHT\'', '\'DOWN\'', '\'LEFT\'', '\'UP\'', 'null']
            },
            '.at(':{
                'synonyms':['.go('],
                'values':['\'RIGHT\'', '\'DOWN\'', '\'LEFT\'', '\'UP\'']
            }
        }
    };

    levelInfo[2] = {
        'help':'You can use new methods for the scanner:<br>' +
        '<pre>var destinationPoints = scanner.getGold();\n' +
        'var nextPoint = scanner.getShortestWay(destinationPoints[0]);\n' +
        'var exitPoint = scanner.getExit();\n' +
        'var robotPoint = scanner.getMe();</pre>' +
        'Coordinate {x:0, y:0} in the left-top corner of board.<br>' +
        'Try this code for check Robot position.<br>' +
        '<pre>robot.log(scanner.getMe());</pre>\n' +
        'So you should collect all the golden bags in the Maze.<br>' +
        'Remember! Your program should work for all previous levels too.',
        'defaultCode':'function program(robot) {\n' +
        '    var scanner = robot.getScanner();\n' +
        '    var dest = scanner.getGold();\n' +
        '    var next = scanner.getShortestWay(dest[0])[1];\n' +
        '    var exit = scanner.getExit();\n' +
        '    var from = scanner.getMe();\n' +
        '    // TODO write your code here\n' +
        '}',
        'winCode':'',
        'autocomplete':{
            'scanner.':{
                'synonyms':['robot.getScanner().'],
                'values':['getGold()', 'getExit()', 'getShortestWay()', 'getMe()']
            },
            ' == ':{
                'synonyms':[' != '],
                'values':['\'GOLD\'']
            },
        }
    };

    levelInfo[3] = {
        'help':'In this case, we have Holes. Robot will fall down, if you won’t avoid it.<br>' +
        'You can use this method to detect Holes:<br>' +
        '<pre>var scanner = robot.getScanner();\n' +
        'if (scanner.at("LEFT") == "HOLE") {\n' +
        '    // some statement here\n' +
        '}</pre>' +
        'And these new methods for jumping through it:<br>' +
        '<pre>robot.jumpLeft();\n' +
        'robot.jumpRight();\n' +
        'robot.jumpUp();\n' +
        'robot.jumpDown();\n' +
        'robot.jump("LEFT");</pre>' +
        'By the way, we did not tell you, but through zombies you can also jump over.<br>' +
        'If you want to find Zombie on map - try use "FEMALE_ZOMBIE" or "MALE_ZOMBIE" element.<br>' +
        'Also you can add your own method to robot by:' +
        '<pre>robot.doSmthNew = function(parameter) {\n' +
        '    // some statement here\n' +
        '}</pre>' +
        'Remember! Your program should work for all previous levels too.',
        'defaultCode':levelInfo[2].winCode,
        'winCode':'',
        'autocomplete':{
            'robot.':{
                'synonyms':[],
                'values':['goOverHole()', 'jump()', 'jumpLeft()', 'jumpRight()', 'jumpUp()', 'jumpDown()']
            },
            '.jump(':{
                'synonyms':[],
                'values':['\'RIGHT\'', '\'DOWN\'', '\'LEFT\'', '\'UP\'']
            },
            ' == ':{
                'synonyms':[' != '],
                'values':['\'HOLE\'', '\'FEMALE_ZOMBIE\'', '\'MALE_ZOMBIE\'']
            }
        }
    };

    levelInfo[4] = { // LEVEL C
        'help':'On this Maze you can see a lot of boxes. ' +
        'You can jump over box and pull/push any box.<br>' +
        'It is possible to move the box only forward or backward, "side pulling" is not allowed.<br>' +
        'There are 4 corresponding functions for each direction: ' +
        'pullLeft, pullRight, pullUp and pullDown. Also you can use generic pull method:<br>' +
        '<pre>robot.pull("UP");\n' +
        '// same as\n' +
        'robot.pullUp();</pre>' +
        'If you want to find box on map - try use "BOX" element.<br>' +
        'Remember! Your program should work for all previous levels too.',
        'defaultCode':levelInfo[3].winCode,
        'winCode':'',
        'autocomplete':{
            'robot.':{
                'synonyms':[],
                'values':['pull()', 'pullLeft()', 'pullRight()', 'pullUp()', 'pullDown()']
            },
            '.pull(':{
                'synonyms':[],
                'values':['\'RIGHT\'', '\'DOWN\'', '\'LEFT\'', '\'UP\'']
            }
        }
    }

    return {
        getInfo : getInfo
    }
}