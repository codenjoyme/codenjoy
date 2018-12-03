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
assertEquals = function(expected, actual) {
    expected = String(expected)
    actual = String(actual)
    if (expected !== actual) {
        throw Error('Expected: "' + expected + '" but was: "' + actual + '"');
    }
}

assertActions = function(expected, actual) {
    assertEquals(expected, actual);
    actual.length = 0;
}

assertMoved = function(expectedCameFrom, expectedPrevious, wasMoved) {
    if (!wasMoved) {
        expectedCameFrom = "null";
        expectedPrevious = "null";
    }
    assertEquals(expectedCameFrom, robot.cameFrom());
    assertEquals(expectedPrevious, robot.previousDirection());
}

assertCommand = function(name, wasMoved){
    var upper = name.toUpperCase();

    robot[name]();
    assertMoved("null", "null", false);
    assertActions("clean,command[" + upper + "],wait", controllerActions);

    robot[name]("UP");
    assertMoved("DOWN", "UP", wasMoved);
    assertActions("clean,command[" + upper + ",UP],wait", controllerActions);

    robot[name]("LEFT");
    assertMoved("RIGHT", "LEFT", wasMoved);
    assertActions("clean,command[" + upper + ",LEFT],wait", controllerActions);

    robot[name]("DOWN");
    assertMoved("UP", "DOWN", wasMoved);
    assertActions("clean,command[" + upper + ",DOWN],wait", controllerActions);

    robot[name]("RIGHT");
    assertMoved("LEFT", "RIGHT", wasMoved);
    assertActions("clean,command[" + upper + ",RIGHT],wait", controllerActions);

    robot[name]("QWE");
    assertActions("Unexpected direction value 'QWE' please use: 'UP', 'DOWN', 'LEFT' or 'RIGHT'.", loggerActions);
    assertMoved("LEFT", "RIGHT", wasMoved);
    assertActions("", controllerActions);

    robot[name]();
    assertMoved("null", "null", false);
    assertActions("clean,command[" + upper + "],wait", controllerActions);

    // jumpUp, jumpLeft, jumpDown, jumpRight
    robot[name + "Up"]();
    assertMoved("DOWN", "UP", wasMoved);
    assertActions("clean,command[" + upper + ",UP],wait", controllerActions);

    robot[name + "Left"]();
    assertMoved("RIGHT", "LEFT", wasMoved);
    assertActions("clean,command[" + upper + ",LEFT],wait", controllerActions);

    robot[name + "Down"]();
    assertMoved("UP", "DOWN", wasMoved);
    assertActions("clean,command[" + upper + ",DOWN],wait", controllerActions);

    robot[name + "Right"]();
    assertMoved("LEFT", "RIGHT", wasMoved);
    assertActions("clean,command[" + upper + ",RIGHT],wait", controllerActions);
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

    board = '{"layers":["╔═══┐' +
                        '║S..│' +
                        '║...│' +
                        '║..E│' +
                        '└───┘",' +
                        '"-----' +
                        '--☺$-' +
                        '-X---' +
                        '--$--' +
                        '-----"]}';

    robot = initRobot(logger, controller);
    var scanner = robot.getScanner();

    // findAll

    assertEquals("[2,3],[3,1]",
        scanner.findAll("GOLD"));

    assertEquals("[2,3],[3,1]",
        scanner.findAll(["GOLD"]));

    assertEquals("",
        scanner.findAll([]));

    assertEquals("[2,3],[3,1],[1,1],[3,3]",
        scanner.findAll(["GOLD","START","EXIT"]));

    // nextLevel
    robot.nextLevel();
    assertActions("win,wait", controllerActions);

    // log
    robot.log("message");
    robot.log("message2");
    assertActions("Robot says: message,Robot says: message2", loggerActions);

    // invert
    assertEquals("DOWN", robot.invert("UP"));
    assertEquals("UP", robot.invert("DOWN"));
    assertEquals("LEFT", robot.invert("RIGHT"));
    assertEquals("RIGHT", robot.invert("LEFT"));

    assertEquals(undefined, robot.invert("QWE"));
    assertActions("Unexpected direction value 'QWE' please use: 'UP', 'DOWN', 'LEFT' or 'RIGHT'.", loggerActions);

    assertEquals(undefined, robot.invert(null));
    assertActions("Unexpected direction value 'null' please use: 'UP', 'DOWN', 'LEFT' or 'RIGHT'.", loggerActions);

    assertEquals(undefined, robot.invert());
    assertActions("Unexpected direction value 'undefined' please use: 'UP', 'DOWN', 'LEFT' or 'RIGHT'.", loggerActions);

    // go
    resetMocks();

    assertEquals("null", robot.cameFrom());
    assertEquals("null", robot.previousDirection());
    assertActions("", controllerActions);

    robot.go("UP");
    assertEquals("DOWN", robot.cameFrom());
    assertEquals("UP", robot.previousDirection());
    assertActions("clean,command[UP]", controllerActions);

    robot.go("LEFT");
    assertEquals("RIGHT", robot.cameFrom());
    assertEquals("LEFT", robot.previousDirection());
    assertActions("clean,command[LEFT]", controllerActions);

    robot.go("DOWN");
    assertEquals("UP", robot.cameFrom());
    assertEquals("DOWN", robot.previousDirection());
    assertActions("clean,command[DOWN]", controllerActions);

    robot.go("RIGHT");
    assertEquals("LEFT", robot.cameFrom());
    assertEquals("RIGHT", robot.previousDirection());
    assertActions("clean,command[RIGHT]", controllerActions);

    robot.go("QWE");
    assertActions("Unexpected direction value 'QWE' please use: 'UP', 'DOWN', 'LEFT' or 'RIGHT'.", loggerActions);
    assertEquals("LEFT", robot.cameFrom());
    assertEquals("RIGHT", robot.previousDirection());
    assertActions("", controllerActions);

    // goUp, goLeft, goDown, goRight
    resetMocks();

    robot.goUp();
    assertEquals("DOWN", robot.cameFrom());
    assertEquals("UP", robot.previousDirection());
    assertActions("clean,command[UP]", controllerActions);

    robot.goLeft();
    assertEquals("RIGHT", robot.cameFrom());
    assertEquals("LEFT", robot.previousDirection());
    assertActions("clean,command[LEFT]", controllerActions);

    robot.goDown();
    assertEquals("UP", robot.cameFrom());
    assertEquals("DOWN", robot.previousDirection());
    assertActions("clean,command[DOWN]", controllerActions);

    robot.goRight();
    assertEquals("LEFT", robot.cameFrom());
    assertEquals("RIGHT", robot.previousDirection());
    assertActions("clean,command[RIGHT]", controllerActions);

    // reset
    robot.reset();
    assertActions("clean", controllerActions);
    assertEquals("null", robot.cameFrom());
    assertEquals("null", robot.previousDirection());

    // jump
    resetMocks();
    assertCommand("jump", true);

    // pull
    resetMocks();
    assertCommand("pull", true);

    // fire
    resetMocks();
    assertCommand("fire", false);

    // getMemory
    var memory = robot.getMemory();
    assertEquals(false, memory.has("key"));

    memory.save("key", "value");
    assertEquals(true, memory.has("key"));
    assertEquals(false, memory.has("key2"));

    assertEquals("value", memory.load("key"));
    assertEquals(undefined, memory.load("key2"));

    memory.save("key2", "value2");
    assertEquals(true, memory.has("key"));
    assertEquals(true, memory.has("key2"));

    assertEquals("value", memory.load("key"));
    assertEquals("value2", memory.load("key2"));

    memory.remove("key");
    assertEquals(false, memory.has("key"));
    assertEquals(true, memory.has("key2"));

    assertEquals(undefined, memory.load("key"));
    assertEquals("value2", memory.load("key2"));

    memory.clean();
    assertEquals(false, memory.has("key"));
    assertEquals(false, memory.has("key2"));

    assertEquals(undefined, memory.load("key"));
    assertEquals(undefined, memory.load("key2"));

    //
}
