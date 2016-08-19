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

var levelInfo = [];

// ----------------------- get level info -------------------
var getLevelInfo = function(level) {
    if (!level) {
        level = currentLevel + 1;
    }

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
    'The code looks like this:<br>' +
    '<pre>function program(robot) {\n' +
    '    // TODO Uncomment one line that will help\n' +
    '    // robot.goDown();\n' +
    '    // robot.goUp();\n' +
    '    // robot.goLeft();\n' +
    '    // robot.goRight();\n' +
    '}</pre>' +
    'Send program to Robot by clicking the Commit button.<br>' +
    'If something is wrong - check Robot message in the Console (the rightmost field).<br>' +
    'You can always stop the program by clicking the Reset button.',
    'defaultCode':'function program(robot) {\n' +
    '    // TODO Uncomment one line that will help\n' +
    '    // robot.goDown();\n' +
    '    // robot.goUp();\n' +
    '    // robot.goLeft();\n' +
    '    // robot.goRight();\n' +
    '}',
    'winCode':'function program(robot) {\n' +
    '    robot.goRight();\n' +
    '}',
    'autocomplete': {
        'robot.':{
            'synonyms':[],
            'values':['goDown()', 'goUp()', 'goLeft()', 'goRight()']
        },
        'scanner.':{
            'synonyms':['robot.getScanner().'],
            'values':[]
        },
        ' == ':{
            'synonyms':[' != '],
            'values':[]
        }
    }
};

levelInfo[2] = {
    'help':'Looks like the Maze was changed. Our old program will not help.<br>' +
    'We need to change it! The robot must learn how to use the scanner.<br>' +
    'Scanner will help robot to detect walls and other obstacles.<br>' +
    'To use scanner is necessary to execute the following code:<br>' +
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
    'In this code, you can see the new IF-ELSE construction:<br>' +
    '<pre>if (expression) {\n' +
    '    // statement\n' +
    '} else {\n' +
    '    // statement\n' +
    '}</pre>',
    'defaultCode':'function program(robot) {\n' +
    '    var scanner = robot.getScanner();\n' +
    '    if (scanner.atRight() != "WALL") {\n' +
    '        robot.goRight();\n' +
    '    } else {\n' +
    '        // TODO write your code here\n' +
    '    }\n' +
    '}',
    'winCode':'function program(robot) {\n' +
    '    var scanner = robot.getScanner();\n' +
    '    if (scanner.atRight() != "WALL") {\n' +
    '        robot.goRight();\n' +
    '    } else {\n' +
    '        robot.goDown();\n' +
    '    }\n' +
    '}',
    'autocomplete': {
        'robot.':{
            'synonyms':[],
            'values':['getScanner()']
        },
        'scanner.':{
            'synonyms':['robot.getScanner().'],
            'values':['atRight()', 'atLeft()', 'atUp()', 'atDown()']
        },
        ' == ':{
            'synonyms':[' != '],
            'values':['\'WALL\'']
        }
    }
};

levelInfo[3] = {
    'help':'This Maze is very similar to the previous. But it’s not so easy.<br>' +
    'Try to solve it by adding new IF.<br>' +
    'You can use new methods for refactoring:<br>' +
    '<pre>scanner.at("RIGHT");\n' +
    'robot.go("LEFT");</pre>' +
    'If you want to know where we came from - use this expression:<br>' +
    '<pre>robot.cameFrom() == "LEFT"</pre>' +
    'If you want to know where we came to on our previous step, use:<br>' +
    '<pre>robot.previousDirection() == "RIGHT"</pre>' +
    'You can use these commands with previous to tell robot to go on one direction, like:<br>' +
    '<pre>robot.go(robot.previousDirection());</pre>' +
    'Be careful! The program should work for all previous levels too.',
    'defaultCode':levelInfo[2].winCode,
    'winCode':'function program(robot) {\n' +
    '    var scanner = robot.getScanner();\n' +
    '    if (robot.cameFrom() != null) {\n' +
    '        robot.go(robot.previousDirection());\n' +
    '    } else {\n' +
    '        if (scanner.atRight() != "WALL") {\n' +
    '            robot.goRight();\n' +
    '        } else if (scanner.atDown() != "WALL") {\n' +
    '            robot.goDown();\n' +
    '        } else {\n' +
    '            robot.goLeft();\n' +
    '        }\n' +
    '    }\n' +
    '}',
    'autocomplete':{
        'robot.':{
            'synonyms':[],
            'values':['cameFrom()', 'previousDirection()', 'go()']
        },
        'scanner.':{
            'synonyms':['robot.getScanner().'],
            'values':['at()']
        },
        ' == ':{
            'synonyms':[' != '],
            'values':['\'RIGHT\'', '\'DOWN\'', '\'LEFT\'', '\'UP\'', 'null']
        }
    }
};

levelInfo[4] = {
    'help':'Try to solve it by adding new IF. Now it should be easy!<br>' +
    'You can use this method to show data in console:<br>' +
    '<pre>var someVariable = "someData";\n' +
    'robot.log(someVariable);</pre>' +
    'Be careful! The program should work for all previous levels too.',
    'defaultCode':levelInfo[3].winCode,
    'winCode':'function program(robot) {\n' +
    '    var scanner = robot.getScanner();\n' +
    '    if (robot.cameFrom() != null) {\n' +
    '        robot.go(robot.previousDirection());\n' +
    '    } else {\n' +
    '        if (scanner.atRight() != "WALL") {\n' +
    '            robot.goRight();\n' +
    '        } else if (scanner.atDown() != "WALL") {\n' +
    '            robot.goDown();\n' +
    '        } else if (scanner.atLeft() != "WALL") {\n' +
    '            robot.goLeft();\n' +
    '        } else {\n' +
    '            robot.goUp();\n' +
    '        }\n' +
    '    }\n' +
    '}',
    'autocomplete':{
        'robot.':{
            'synonyms':[],
            'values':['log()']
        },
        'scanner.':{
            'synonyms':['robot.getScanner().'],
            'values':[]
        },
        ' == ':{
            'synonyms':[' != '],
            'values':['\'RIGHT\'', '\'DOWN\'', '\'LEFT\'', '\'UP\'', 'null']
        }
    }
};

levelInfo[5] = {
    'help':'Oops! Looks like we didn’t predict this situation.<br>' +
    'Think how to adapt the code to these new conditions.<br>' +
    'Use refactoring to make your code more abstract.<br>' +

    'Уou can complicate IF conditions by using operators AND/OR/NOT:<br>' +
    '<pre>if (variable1 && !variable2 || variable3) {\n' +
    '    // this code will run IF\n' +
    '    //          variable1 IS true AND variable2 IS true\n' +
    '    //       OR variable3 IS true (ignoring variable1, variable2)\n' +
    '}</pre>' +
    'These operators allow you to use any combination.<br>' +

    'Уou can extract functions, create new local variables:<br>' +
    '<pre>function program(robot) {\n' +
    '    var scanner = robot.getScanner();\n' +
    '    var localVariable = newFunction(scanner);\n' +
    '}\n' +
    'function newFunction(scanner) {\n' +
    '    return "some data";\n' +
    '}</pre>' +
    'New function used for encapsulate algorithm.<br>' +
    'Local variable saves value only during current step.<br>' +

    'Remember! Your program should work for all previous levels too.',
    'defaultCode':levelInfo[4].winCode,
    'winCode':'function program(robot) {\n' +
    '    var scanner = robot.getScanner();\n' +
    '    if (robot.cameFrom() != null &&\n' +
    '        scanner.at(robot.previousDirection()) != "WALL")\n' +
    '    {\n' +
    '        robot.go(robot.previousDirection());\n' +
    '    } else {\n' +
    '        robot.go(freeDirection(scanner));\n' +
    '    }\n' +
    '}\n' +
    '\n' +
    'function freeDirection(scanner) {\n' +
    '    if (scanner.atRight() != "WALL") {\n' +
    '        return "RIGHT";\n' +
    '    } else if (scanner.atDown() != "WALL") {\n' +
    '        return "DOWN";\n' +
    '    } else if (scanner.atLeft() != "WALL") {\n' +
    '        return "LEFT";\n' +
    '    } else {\n' +
    '        return "UP";\n' +
    '    }\n' +
    '}'
};

levelInfo[6] = {
    'help':'You should check all cases.<br>' +
    'Remember! Your program should work for all previous levels too.',
    'defaultCode':levelInfo[5].winCode,
    'winCode':levelInfo[5].winCode
};

levelInfo[7] = {
    'help':'It is too hard to solve this issue without refactoring.<br>' +
    'You can use new methods for refactoring:<br>' +
    '<pre>scanner.at("RIGHT");\n' +
    'robot.go("LEFT");</pre>' +
    'Also you can use new FOR construction:<br>' +
    '<pre>var directions = ["RIGHT", "DOWN", "LEFT", "UP"];\n' +
    'for (var index in directions) {\n' +
    '    var direction = directions[index];\n' +
    '    // do something with current direction\n' +
    '}</pre>' +
    'Remember! Your program should work for all previous levels too.',
    'defaultCode':levelInfo[6].winCode,
    'winCode':'function program(robot) {\n' +
    '    var scanner = robot.getScanner();\n' +
    '    if (robot.cameFrom() != null &&\n' +
    '        scanner.at(robot.previousDirection()) != "WALL")\n' +
    '    {\n' +
    '        robot.go(robot.previousDirection());\n' +
    '    } else {\n' +
    '        robot.go(freeDirection(scanner, robot));\n' +
    '    }\n' +
    '}\n' +
    '\n' +
    'function freeDirection(scanner, robot) {\n' + 
    '    var directions = ["RIGHT", "DOWN", "LEFT", "UP"];\n' + 
    '    for (var index in directions) {\n' + 
    '        var direction = directions[index];\n' + 
    '        if (robot.cameFrom() == direction) {\n' +
    '            continue;\n' +
    '        }\n' + 
    '        if (scanner.at(direction) != "WALL") {\n' + 
    '            return direction;\n' + 
    '        }\n' +  
    '    }\n' + 
    '}'
};

levelInfo[8] = {
    'help':levelInfo[6].help,
    'defaultCode':levelInfo[7].winCode,
    'winCode':levelInfo[7].winCode
}

levelInfo[9] = {
    'help':'This is final LevelA Maze. Good luck!<br>' +
    'Remember! Your program should work for all previous levels too.',
    'defaultCode':levelInfo[8].winCode,
    'winCode':levelInfo[8].winCode
};

levelInfo[10] = { // LEVEL B1
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
    '    var next = scanner.getShortestWay(dest[0]);\n' +
    '    var exit = scanner.getExit();\n' +
    '    var from = scanner.getMe();\n' +
    '    // TODO write your code here\n' +
    '}',
    'winCode':'function program(robot) {\n' +
    '    var scanner = robot.getScanner();\n' +
    '    var dest = scanner.getGold();\n' +
    '    if (dest.length === 0) {\n' +
    '        dest = scanner.getExit();\n' +
    '    }\n' +
    '    var to = scanner.getShortestWay(dest[0]);\n' +
    '    var from = scanner.getMe();\n' +
    '    \n' +
    '    var dx = to.getX() - from.getX();\n' +
    '    var dy = to.getY() - from.getY();\n' +
    '    if (dx > 0) {\n' +
    '        robot.goRight();\n' +
    '    } else if (dx < 0) {\n' +
    '        robot.goLeft();\n' +
    '    } else if (dy > 0) {\n' +
    '        robot.goDown();\n' +
    '    } else if (dy < 0) {\n' +
    '        robot.goUp();\n' +
    '    }\n' +
    '}',
    'autocomplete':{
        'robot.':{
            'synonyms':[],
            'values':[]
        },
        'scanner.':{
            'synonyms':['robot.getScanner().'],
            'values':['getGold()', 'getExit()', 'getExit()', 'getShortestWay()', 'getMe()']
        },
        ' == ':{
            'synonyms':[' != '],
            'values':[]
        },
    }
};

levelInfo[11] = { // LEVEL B2
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
    'Also you can add new method to robot by:' +
    '<pre>robot.doSmthNew = function(parameter) {\n' +
    '    // some statement here\n' +
    '}</pre>' +
    'Remember! Your program should work for all previous levels too.',
    'defaultCode':levelInfo[10].winCode,
    'winCode':'function program(robot) {\n' +
    '    var scanner = robot.getScanner();\n' +
    '    var dest = scanner.getGold();\n' +
    '    if (dest.length === 0) {\n' +
    '        dest = scanner.getExit();\n' +
    '    }\n' +
    '    var to = scanner.getShortestWay(dest[0]);\n' +
    '    var from = scanner.getMe();\n' +
    '\n' +
    '    robot.goOverHole = function(direction) {\n' +
    '        if (scanner.at(direction) != "HOLE") {\n' +
    '            robot.go(direction);\n' +
    '        } else {\n' +
    '            robot.jump(direction);\n' +
    '        }\n' +
    '    };\n' +
    '    \n' +
    '    var dx = to.getX() - from.getX(); \n' +
    '    var dy = to.getY() - from.getY(); \n' +
    '    if (dx > 0) {\n' +
    '        robot.goOverHole("RIGHT");\n' +
    '    } else if (dx < 0) {\n' +
    '        robot.goOverHole("LEFT");\n' +
    '    } else if (dy > 0) {\n' +
    '        robot.goOverHole("DOWN");\n' +
    '    } else if (dy < 0) {\n' +
    '        robot.goOverHole("UP");\n' +
    '    }\n' +
    '}',
    'autocomplete':{
        'robot.':{
            'synonyms':[],
            'values':['goOverHole()', 'jumpLeft()', 'jumpRight()', 'jumpUp()', 'jumpDown()']
        },
        'scanner.':{
            'synonyms':['robot.getScanner().'],
            'values':[]
        },
        ' == ':{
            'synonyms':[' != '],
            'values':['\'HOLE\'']
        },
    }
};

levelInfo[12] = { // LEVEL B3
    'help':'Oops! Looks like we didn’t predict this situation.<br>' +
    'Think how to adapt the code to these new conditions.<br>' +
    'Use IF construction to make your code more safely for Robot.<br>' +

    'You can use this method to detect Elements throughout the world:<br>' +
    '<pre>var scanner = robot.getScanner();\n' +
    'var point = new Point(4, 8);\n' +
    'if (scanner.at(point) == "HOLE") {\n' +
    '    // some statement here\n' +
    '}</pre>' +

    'Also you can use this method:<br>' +
    '<pre>var scanner = robot.getScanner();\n' +
    'var xOffset = 1;\n' +
    'var yOffset = -2;\n' +
    'if (scanner.atNearRobot(xOffset, yOffset) == "HOLE") {\n' +
    '    // some statement here\n' +
    '}</pre>' +
    'If Robot at {x:10, y:10}, this code will check {x:10 + 1, y:10 - 2} cell.<br>' +

    'Remember! Your program should work for all previous levels too.',
    'defaultCode':levelInfo[11].winCode,
    'winCode':'function program(robot) {\n' +
    '    var scanner = robot.getScanner();\n' +
    '    var dest = scanner.getGold();\n' +
    '    if (dest.length === 0) {\n' +
    '        dest = scanner.getExit();\n' +
    '    }\n' +
    '    var to = scanner.getShortestWay(dest[0]);\n' +
    '    var from = scanner.getMe();\n' +
    '\n' +
    '    robot.goOverHole = function(direction) {\n' +
    '        if (scanner.at(direction) != "HOLE") {\n' +
    '            robot.go(direction);\n' +
    '        } else {\n' +
    '            if (direction == "DOWN") { // TODO crutch :)\n' +
    '                var afterHole = new Point(from.getX(), from.getY() + 2);\n' +
    '                if (scanner.at(afterHole) == "WALL") {\n' +
    '                    robot.go("RIGHT");\n' +
    '                    return;\n' +
    '                }\n' +
    '            }\n' +
    '            robot.jump(direction);\n' +
    '        }\n' +
    '    };\n' +
    '    \n' +
    '    var dx = to.getX() - from.getX(); \n' +
    '    var dy = to.getY() - from.getY(); \n' +
    '    if (dx > 0) {\n' +
    '        robot.goOverHole("RIGHT");\n' +
    '    } else if (dx < 0) {\n' +
    '        robot.goOverHole("LEFT");\n' +
    '    } else if (dy > 0) {\n' +
    '        robot.goOverHole("DOWN");\n' +
    '    } else if (dy < 0) {\n' +
    '        robot.goOverHole("UP");\n' +
    '    }\n' +
    '}'
};