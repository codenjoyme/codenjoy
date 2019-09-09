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
var robotTest = function() {
    var toString = function (data) {
        if (data === undefined || data == null) {
            return data;
        }
        return JSON.stringify(data).split('"').join('\'');
    }

    var assertEquals = function (expected, actual) {
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

    var boardString = 'board={"offset":{"x":0,"y":0},' +
        '"heroPosition":{"x":2,"y":1},' +
        '"layers":["' +
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

    var boardJson = eval(boardString);
    var board = new Board(boardJson);

    // --------- board.layerN -----------

    assertEquals('╔═══════┐\n' +
                '║S.$◄..O│\n' +
                '║....$O.│\n' +
                '║.$E....│\n' +
                '║˃..O...│\n' +
                '║.O...Z.│\n' +
                '║..˄....│\n' +
                '║..˅˂▼►▲│\n' +
                '└───────┘\n',
    board.layer1());

    assertEquals('---------\n' +
                 '--☺----o-\n' +
                 '-X----x☻-\n' +
                 '-X---B---\n' +
                 '--→B-↓B--\n' +
                 '-♂♂♀✝B---\n' +
                 '--&--↑←--\n' +
                 '---------\n' +
                 '---------\n',
    board.layer2());

    assertEquals('---------\n' +
                '------*--\n' +
                '---------\n' +
                '---------\n' +
                '------^--\n' +
                '---------\n' +
                '---------\n' +
                '---------\n' +
                '---------\n',
    board.layer3());

    // --------- getScanner --------------
    // getAt

    assertEquals(Element.GOLD,
        board.getAt(LAYER1, 2, 3));

    assertEquals(Element.ROBOT_OTHER,
        board.getAt(LAYER2, 1, 2));

    assertEquals(Element.ROBOT_OTHER,
        board.getAt(LAYER2, 1, 2));

    assertEquals(Element.HOLE,
        board.getAt(LAYER1, 6, 2));

    assertEquals(Element.ROBOT_OTHER_FALLING,
        board.getAt(LAYER2, 6, 2));

    assertEquals(Element.BOX,
        board.getAt(LAYER2, 6, 4));

    assertEquals(Element.ROBOT_OTHER_FLYING,
        board.getAt(LAYER3, 6, 4));

    assertEquals(Element.FLOOR,
        board.getAt(LAYER1, 2, 6));

    assertEquals(Element.ROBOT_OTHER_LASER,
        board.getAt(LAYER2, 2, 6));

    assertEquals(Element.FLOOR,
        board.getAt(LAYER1, 2, 1));

    assertEquals(Element.ROBOT,
        board.getAt(LAYER2, 2, 1));

    assertEquals(Element.FLOOR,
        board.getAt(LAYER1, 2, 2));

    assertEquals(Element.EMPTY,
        board.getAt(LAYER2, 2, 2));

    assertEquals(Element.EMPTY,
        board.getAt(LAYER3, 2, 2));

    assertEquals(Element.EXIT,
        board.getAt(LAYER1, 3, 3));

    assertEquals(Element.GOLD,
        board.getAt(LAYER1, 3, 1));

    assertEquals(Element.FLOOR,
        board.getAt(LAYER1, 6, 1));

    assertEquals(Element.EMPTY,
        board.getAt(LAYER2, 6, 1));

    assertEquals(Element.ROBOT_FLYING,
        board.getAt(LAYER3, 6, 1));

    assertEquals(Element.BOX,
        board.getAt(LAYER2, 6, 4));

    assertEquals(Element.ROBOT_OTHER_FLYING,
        board.getAt(LAYER3, 6, 4));

    // isNear

    assertEquals(false,
        board.isNear(LAYER2, -1, 1, Element.ROBOT_OTHER)); // координаты невалидные

    // мoй другой робот что летает LAYER3
    assertEquals(Element.ROBOT_OTHER_FLYING,
            board.getAt(LAYER3, 6, 4));
    assertEquals(true,
        board.isNear(LAYER3, 5, 4, Element.ROBOT_OTHER_FLYING));

    // чужой робот, что летает LAYER3
    assertEquals(Element.ROBOT_FLYING,
            board.getAt(LAYER3, 6, 1));
    assertEquals(true,
        board.isNear(LAYER3, 6, 0, Element.ROBOT_FLYING));

    assertEquals(true,
        board.isNear(LAYER2, 2, 2, Element.ROBOT_OTHER));

    assertEquals(true,
        board.isNear(LAYER2, 2, 2, Element.ROBOT));

    assertEquals(false,
        board.isNear(LAYER2, 2, 2, Element.FEMALE_ZOMBIE));

    assertEquals(false,
        board.isNear(LAYER2, 2, 2, [Element.FEMALE_ZOMBIE, Element.HOLE]));

    assertEquals(false,
        board.isNear(LAYER2, 5, 1, [Element.ROBOT_OTHER, Element.ROBOT_FLYING]));

    assertEquals(true,
        board.isNear(LAYER3, 5, 1, [Element.ROBOT_OTHER, Element.ROBOT_FLYING]));

    assertEquals(false,
        board.isNear(LAYER2, 5, 4, [Element.ROBOT_OTHER_FLYING, Element.ROBOT]));

    assertEquals(true,
        board.isNear(LAYER3, 5, 4, [Element.ROBOT_OTHER_FLYING, Element.ROBOT]));

    assertEquals(true,
        board.isNear(LAYER2, 2, 2, [Element.ROBOT, Element.GOLD]));

    assertEquals(true,
        board.isNear(LAYER2, 2, 2, [Element.ROBOT_OTHER, Element.HOLE, Element.FEMALE_ZOMBIE]));

    // getMe

    assertEquals({'x': 2, 'y': 1}, board.getMe());

    // TODO what if Hero not on board?
    // TODO what if Hero is flying
    // TODO ...or falling to hole
    // TODO ...or die on laser?

    // isAt

    assertEquals(false,
        board.isAt(LAYER2, 2, 1, Element.ROBOT_OTHER));
    assertEquals(true,
        board.isAt(LAYER2, 2, 1, Element.ROBOT));

    assertEquals(false,
        board.isAt(LAYER1, 2, 1, Element.ROBOT_OTHER));
    assertEquals(false,
        board.isAt(LAYER1, 2, 1, Element.ROBOT));

    assertEquals(false,
        board.isAt(LAYER1, 2, 1, [Element.GOLD, Element.ROBOT]));
    assertEquals(true,
        board.isAt(LAYER2, 2, 1, [Element.GOLD, Element.ROBOT]));

    assertEquals(true,
        board.isAt(LAYER2, 2, 1, [Element.ROBOT, Element.FLOOR]));

    assertEquals(false,
        board.isAt(LAYER2, 2, 1, [Element.ROBOT_OTHER, Element.HOLE, Element.FEMALE_ZOMBIE]));

    // мой летает на LAYER3
    assertEquals(true,
        board.isAt(LAYER3, 6, 1, [Element.ROBOT_FLYING]));

    assertEquals(false,
        board.isAt(LAYER2, 6, 1, [Element.ROBOT_FLYING]));

    // чужой летает на LAYER3
    assertEquals(true,
        board.isAt(LAYER3, 6, 4, [Element.ROBOT_OTHER_FLYING]));

    assertEquals(false,
        board.isAt(LAYER3, 6, 4, [Element.ROBOT_OTHER]));

    // at corners

    assertEquals(true,
        board.isAt(LAYER1, 0, 0, Element.ANGLE_IN_LEFT));

    assertEquals(true,
        board.isAt(LAYER1, 0, 8, [Element.ROBOT_OTHER, Element.ANGLE_BACK_LEFT]));

    assertEquals(false,
        board.isAt(LAYER1, 8, 0, Element.GOLD));

    assertEquals(false,
        board.isAt(LAYER1, 8, 8, [Element.GOLD, Element.HOLE]));

    // get

    assertEquals([{'x': 2, 'y': 3}, {'x': 3, 'y': 1}, {'x': 5, 'y': 2}],
        board.get(LAYER1, Element.GOLD));

    assertEquals([{'x': 1, 'y': 2}, {'x': 1, 'y': 3}],
        board.get(LAYER2, Element.ROBOT_OTHER));

    assertEquals([{'x': 6, 'y': 4}],
        board.get(LAYER3, Element.ROBOT_OTHER_FLYING));

    assertEquals([{'x': 6, 'y': 2}],
        board.get(LAYER2, Element.ROBOT_OTHER_FALLING));

    assertEquals([{'x': 2, 'y': 6}],
        board.get(LAYER2, Element.ROBOT_OTHER_LASER));

    assertEquals([{'x': 2, 'y': 1}],
        board.get(LAYER2, Element.ROBOT));

    assertEquals([{'x':6,'y':1}],
        board.get(LAYER3, Element.ROBOT_FLYING));

    assertEquals([{'x': 7, 'y': 1}],
        board.get(LAYER2, Element.ROBOT_FALLING));

    assertEquals([{'x': 7, 'y': 2}],
        board.get(LAYER2, Element.ROBOT_LASER));

    assertEquals([{'x':1,'y':2},{'x':1,'y':3},{'x':2,'y':1},{'x':2,'y':6},{'x':6,'y':2},{'x':7,'y':1},{'x':7,'y':2}],
        board.get(LAYER2, [Element.ROBOT, Element.ROBOT_FLYING, Element.ROBOT_FALLING, Element.ROBOT_LASER,
                            Element.ROBOT_OTHER, Element.ROBOT_OTHER_FLYING, Element.ROBOT_OTHER_FALLING, Element.ROBOT_OTHER_LASER]));

    assertEquals([{'x': 6, 'y': 1}, {'x': 6, 'y': 4}],
        board.get(LAYER3, [Element.ROBOT, Element.ROBOT_FLYING, Element.ROBOT_FALLING, Element.ROBOT_LASER,
                            Element.ROBOT_OTHER, Element.ROBOT_OTHER_FLYING, Element.ROBOT_OTHER_FALLING, Element.ROBOT_OTHER_LASER]));

    assertEquals([{'x': 2, 'y': 3}, {'x': 3, 'y': 1}, {'x': 5, 'y': 2}],
        board.get(LAYER1, [Element.GOLD]));

    assertEquals([],
        board.get(LAYER1, []));

    assertEquals([{'x':1,'y':1},{'x':2,'y':3},{'x':3,'y':1},{'x':3,'y':3},{'x':5,'y':2}],
        board.get(LAYER1, [Element.GOLD, Element.START, Element.EXIT]));

    // isBarrierAt    

    assertEquals(false,
        board.isBarrierAt(2, 2));

    assertEquals(true,
        board.isBarrierAt(0, 0));

    assertEquals(true,
        board.isBarrierAt(0, 0));

    assertEquals(true,
        board.isBarrierAt(3, 4));

    assertEquals(true,
        board.isBarrierAt(4, 1));

    // countNear    

    assertEquals(1,
        board.countNear(LAYER1, 2, 2, Element.GOLD));

    assertEquals(1,
        board.countNear(LAYER2, 2, 2, Element.ROBOT));

    assertEquals(0,
        board.countNear(LAYER2, 5, 1, [Element.ROBOT_OTHER]));

    assertEquals(1,
        board.countNear(LAYER3, 5, 1, [Element.ROBOT_FLYING]));

    assertEquals(1,
        board.countNear(LAYER3, 5, 4, [Element.ROBOT_OTHER_FLYING, Element.ROBOT]));

    assertEquals(1,
        board.countNear(LAYER2, 2, 2, Element.ROBOT_OTHER));

    assertEquals(4,
        board.countNear(LAYER2, 3, 2, Element.EMPTY));

    assertEquals(2,
        board.countNear(LAYER1, 1, 1, [Element.WALL_FRONT, Element.WALL_LEFT]));

    // getOtherRobots
   
    assertEquals([{'x': 1, 'y': 2}, {'x': 1, 'y': 3}, {'x': 2, 'y': 6}, {'x': 6, 'y': 2}, {'x': 6, 'y': 4}],
        board.getOtherHeroes());

    // getLaserMachines
    
    assertEquals([{'x':1,'y':4,'direction':'right'},{'x':3,'y':6,'direction':'up'},{'x':3,'y':7,'direction':'down'},{'x':4,'y':1,'direction':'left'},{'x':4,'y':7,'direction':'left'},{'x':5,'y':7,'direction':'down'},{'x':6,'y':7,'direction':'right'},{'x':7,'y':7,'direction':'up'}],
        board.getLaserMachines());

    // getLasers
    
    assertEquals([{'x':2,'y':4,'direction':'right'},{'x':5,'y':4,'direction':'down'},{'x':5,'y':6,'direction':'up'},{'x':6,'y':6,'direction':'left'}],
        board.getLasers());

    // getWalls
    
    assertEquals([{'x': 0, 'y': 0}, {'x': 0, 'y': 1}, {'x': 0, 'y': 2}, {'x': 0, 'y': 3}, {'x': 0, 'y': 4}, {'x': 0,'y': 5},
         {'x': 0, 'y': 6}, {'x': 0, 'y': 7}, {'x': 0, 'y': 8}, {'x': 1, 'y': 0}, {'x': 1, 'y': 8}, {'x': 2,'y': 0},
         {'x': 2, 'y': 8}, {'x': 3, 'y': 0}, {'x': 3, 'y': 8}, {'x': 4, 'y': 0}, {'x': 4, 'y': 8}, {'x': 5,'y': 0},
         {'x': 5, 'y': 8}, {'x': 6, 'y': 0}, {'x': 6, 'y': 8}, {'x': 7, 'y': 0}, {'x': 7, 'y': 8}, {'x': 8,'y': 0},
         {'x': 8, 'y': 1}, {'x': 8, 'y': 2}, {'x': 8, 'y': 3}, {'x': 8, 'y': 4}, {'x': 8, 'y': 5}, {'x': 8,'y': 6},
         {'x': 8, 'y': 7}, {'x': 8, 'y': 8}],
        board.getWalls());

    // getBoxes
    
    assertEquals([{'x': 3, 'y': 4}, {'x': 5, 'y': 3}, {'x': 5, 'y': 5}, {'x': 6, 'y': 4}],
        board.getBoxes());

    // getGold
  
    assertEquals([{'x': 2, 'y': 3}, {'x': 3, 'y': 1}, {'x': 5, 'y': 2}],
        board.getGold());

    // getStart
    
    assertEquals([{'x': 1, 'y': 1}],
        board.getStarts());

    // getZombieStart
    
    assertEquals([{'x': 6, 'y': 5}],
        board.getZombieStart());

    // getExit
    
    assertEquals([{'x': 3, 'y': 3}],
        board.getExits());

    // getHoles
    
    assertEquals([{'x': 2, 'y': 5}, {'x': 4, 'y': 4}, {'x': 6, 'y': 2}, {'x': 7, 'y': 1}],
        board.getHoles());

    // getBarriers
    
    assertEquals([{'x':0,'y':0},{'x':0,'y':1},{'x':0,'y':2},{'x':0,'y':3},{'x':0,'y':4},{'x':0,'y':5},{'x':0,'y':6},{'x':0,'y':7},{'x':0,'y':8},{'x':1,'y':0},{'x':1,'y':4},{'x':1,'y':8},{'x':2,'y':0},{'x':2,'y':5},{'x':2,'y':8},{'x':3,'y':0},{'x':3,'y':4},{'x':3,'y':6},{'x':3,'y':7},{'x':3,'y':8},{'x':4,'y':0},{'x':4,'y':1},{'x':4,'y':4},{'x':4,'y':7},{'x':4,'y':8},{'x':5,'y':0},{'x':5,'y':3},{'x':5,'y':5},{'x':5,'y':7},{'x':5,'y':8},{'x':6,'y':0},{'x':6,'y':2},{'x':6,'y':4},{'x':6,'y':7},{'x':6,'y':8},{'x':7,'y':0},{'x':7,'y':1},{'x':7,'y':7},{'x':7,'y':8},{'x':8,'y':0},{'x':8,'y':1},{'x':8,'y':2},{'x':8,'y':3},{'x':8,'y':4},{'x':8,'y':5},{'x':8,'y':6},{'x':8,'y':7},{'x':8,'y':8}],
        board.getBarriers());

    // isMyRobotAlive
    
    assertEquals(false,
        board.isMeAlive());
}