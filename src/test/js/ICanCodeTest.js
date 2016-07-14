/**
 * Created by Mikhail_Udalyi on 14.07.2016.
 */
/*TestCase("ICanCodeTest", {
    "first test": function () {
        var temp = true;
        assertEquals("true", temp);
    }
});*/

ICanCodeTestCase = TestCase("ICanCodeTestCase");

ICanCodeTestCase.prototype.testChars = function(){
    assertEquals(Element.EMPTY, el('-', 'NONE'));
    assertEquals(Element.FLOOR, el('.', 'NONE'));

    assertEquals(Element.ANGLE_IN_LEFT, el('╔', 'WALL'));
    assertEquals(Element.WALL_FRONT, el('═', 'WALL'));
    assertEquals(Element.ANGLE_IN_RIGHT, el('┐', 'WALL'));
    assertEquals(Element.WALL_RIGHT, el('│', 'WALL'));
    assertEquals(Element.ANGLE_BACK_RIGHT, el('┘', 'WALL'));
    assertEquals(Element.WALL_BACK, el('─', 'WALL'));
    assertEquals(Element.ANGLE_BACK_LEFT, el('└', 'WALL'));
    assertEquals(Element.WALL_LEFT, el('║', 'WALL'));
    assertEquals(Element.WALL_BACK_ANGLE_LEFT, el('┌', 'WALL'));
    assertEquals(Element.WALL_BACK_ANGLE_RIGHT, el('╗', 'WALL'));
    assertEquals(Element.ANGLE_OUT_RIGHT, el('╝', 'WALL'));
    assertEquals(Element.ANGLE_OUT_LEFT, el('╚', 'WALL'));
    assertEquals(Element.SPACE, el(' ', 'WALL'));

    assertEquals(Element.ROBOT, el('☺', 'MY_ROBOT'));
    assertEquals(Element.ROBOT_FALLING, el('o', 'HOLE'));
    assertEquals(Element.ROBOT_FLYING, el('*', 'MY_ROBOT'));
    assertEquals(Element.ROBOT_LASER, el('☻', 'MY_ROBOT'));

    assertEquals(Element.ROBOT_OTHER, el('X', 'OTHER_ROBOT'));
    assertEquals(Element.ROBOT_OTHER_FALLING, el('x', 'HOLE'));
    assertEquals(Element.ROBOT_OTHER_FLYING, el('^', 'OTHER_ROBOT'));
    assertEquals(Element.ROBOT_OTHER_LASER, el('&', 'OTHER_ROBOT'));

    assertEquals(Element.LASER_MACHINE_CHARGING_LEFT, el('˂', 'LASER_MACHINE'));
    assertEquals(Element.LASER_MACHINE_CHARGING_RIGHT, el('˃', 'LASER_MACHINE'));
    assertEquals(Element.LASER_MACHINE_CHARGING_UP, el('˄', 'LASER_MACHINE'));
    assertEquals(Element.LASER_MACHINE_CHARGING_DOWN, el('˅', 'LASER_MACHINE'));

    assertEquals(Element.LASER_MACHINE_READY_LEFT, el('◄', 'LASER_MACHINE_READY'));
    assertEquals(Element.LASER_MACHINE_READY_RIGHT, el('►', 'LASER_MACHINE_READY'));
    assertEquals(Element.LASER_MACHINE_READY_UP, el('▲', 'LASER_MACHINE_READY'));
    assertEquals(Element.LASER_MACHINE_READY_DOWN, el('▼', 'LASER_MACHINE_READY'));

    assertEquals(Element.LASER_LEFT, el('←', 'LASER_LEFT'));
    assertEquals(Element.LASER_RIGHT, el('→', 'LASER_RIGHT'));
    assertEquals(Element.LASER_UP, el('↑', 'LASER_UP'));
    assertEquals(Element.LASER_DOWN, el('↓', 'LASER_DOWN'));

    assertEquals(Element.START, el('S', 'START'));
    assertEquals(Element.EXIT, el('E', 'EXIT'));
    assertEquals(Element.GOLD, el('$', 'GOLD'));
    assertEquals(Element.HOLE, el('O', 'HOLE'));
    assertEquals(Element.BOX, el('B', 'BOX'));
};

ICanCodeTestCase.prototype.testflagsOfGame = function(){
    assertEquals(game.hasOwnProperty('enableDonate'), true);
    assertEquals(game.hasOwnProperty('enableJoystick'), true);
    assertEquals(game.hasOwnProperty('enableAlways'), true);
    assertEquals(game.hasOwnProperty('enablePlayerInfo'), true);
    assertEquals(game.hasOwnProperty('enableLeadersTable'), true);
    assertEquals(game.hasOwnProperty('enableChat'), true);
    assertEquals(game.hasOwnProperty('enableHotkeys'), true);
    assertEquals(game.hasOwnProperty('enableAdvertisement'), true);
    assertEquals(game.hasOwnProperty('showBody'), true);
};

/*ICanCodeTestCase.prototype.testAlwaysPass = function(){
    var expected = 1, actual = 1;
    assertEquals("The vales should be the same", expected, actual);
    assertEquals(expected, actual);

    var myStr = "hello";
    assertString("The variable should contain a string", myStr);
    assertString(myStr);
};

ICanCodeTestCase.prototype.testAlwaysFail = function(){
    assertEquals(1, 2);
};

ICanCodeTestCase.prototype.testAlwaysFailWithMessage = function(){
    assertEquals("1<>2", 1, 2);
};*/