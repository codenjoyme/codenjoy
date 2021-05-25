/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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
var BattlecityTest = module.exports = function(){
	
	var Games = require('./../../games.js');
	var Direction = Games.require('./direction.js');
	var Point = require('./../../point.js');
	var Board = Games.require('./board.js');
	var Element = Games.require('./elements.js');

	assertEquals = function(expected, actual) {
		expected = String(expected)
		actual = String(actual)
		if (expected !== actual) {
			throw new Error('Expected: "' + expected + '" but was: "' + actual + '"');
		}
	}
	
    var board = new Board(
      /*14*/'☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼' +
      /*13*/'☼     »   » « ☼' +
      /*12*/'☼ ╬ ╬?╬ ╬ ╬ ╬?☼' +
      /*11*/'☼ ╬ ╬ ╬☼╬ ╬ ╬ ☼' +
      /*10*/'☼ ╬ ╬ ╬ ╬ ╬ ╬ ☼' +
      /*9*/ '☼▲╬ ╬     ╬ ╬ ☼' +
      /*8*/ '☼•    ╬ ╬     ☼' +
      /*7*/ '☼   ╬     ╬   ☼' +
      /*6*/ '☼     ╬ ╬     ☼' +
      /*5*/ '☼ ╬ ╬ ╬╬╬ ╬ ╬ ☼' +
      /*4*/ '☼˅╬ ╬ ╬ ╬ ╬ ╬ ☼' +
      /*3*/ '☼ ╬         ╬ ☼' +
      /*2*/ '☼ ╬?  ╬╬╬   ╬ ☼' +
      /*1*/ '☼     ╬ ╬     ☼' +
      /*0*/ '☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼');
           /*012345678901234*/

    assertEquals("15", board.size());

    assertEquals("[1,9]", board.getMe());

    assertEquals(true, board.isBarrierAt(2, 2));
    assertEquals(true, board.isBarrierAt(-1, 2));
    assertEquals(false, board.isBarrierAt(3, 2));

    assertEquals(false, board.isGameOver());

    assertEquals(true, board.isAt(2, 2, Element.WALL));
    assertEquals(false, board.isAt(3, 2, Element.WALL));
    assertEquals(true, board.isAt(1, 9, Element.TANK_UP));
    assertEquals(false, board.isAt(1, 9, Element.TANK_DOWN));

    assertEquals(false, board.isAt(3, board.size(), Element.BATTLE_WALL));
    assertEquals(true, board.isAt(3, board.size() - 1, Element.BATTLE_WALL));

    assertEquals(Element.WALL, board.getAt(2, 2));
    assertEquals(Element.TANK_UP, board.getAt(1, 9));
    assertEquals(Element.BATTLE_WALL, board.getAt(3, -1));

    assertEquals(
        "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
        "☼     »   » « ☼\n" +
        "☼ ╬ ╬?╬ ╬ ╬ ╬?☼\n" +
        "☼ ╬ ╬ ╬☼╬ ╬ ╬ ☼\n" +
        "☼ ╬ ╬ ╬ ╬ ╬ ╬ ☼\n" +
        "☼▲╬ ╬     ╬ ╬ ☼\n" +
        "☼•    ╬ ╬     ☼\n" +
        "☼   ╬     ╬   ☼\n" +
        "☼     ╬ ╬     ☼\n" +
        "☼ ╬ ╬ ╬╬╬ ╬ ╬ ☼\n" +
        "☼˅╬ ╬ ╬ ╬ ╬ ╬ ☼\n" +
        "☼ ╬         ╬ ☼\n" +
        "☼ ╬?  ╬╬╬   ╬ ☼\n" +
        "☼     ╬ ╬     ☼\n" +
        "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n", board.boardAsString());

    assertEquals("Board:\n" +
        "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
        "☼     »   » « ☼\n" +
        "☼ ╬ ╬?╬ ╬ ╬ ╬?☼\n" +
        "☼ ╬ ╬ ╬☼╬ ╬ ╬ ☼\n" +
        "☼ ╬ ╬ ╬ ╬ ╬ ╬ ☼\n" +
        "☼▲╬ ╬     ╬ ╬ ☼\n" +
        "☼•    ╬ ╬     ☼\n" +
        "☼   ╬     ╬   ☼\n" +
        "☼     ╬ ╬     ☼\n" +
        "☼ ╬ ╬ ╬╬╬ ╬ ╬ ☼\n" +
        "☼˅╬ ╬ ╬ ╬ ╬ ╬ ☼\n" +
        "☼ ╬         ╬ ☼\n" +
        "☼ ╬?  ╬╬╬   ╬ ☼\n" +
        "☼     ╬ ╬     ☼\n" +
        "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
        "\n" +
        "My tank at: [1,9]\n" +
        "Enemies at: [[3,2],[5,12],[13,12],[12,13],[6,13],[10,13],[1,4]]\n" +
        "Bulets at: [[1,8]]\n", board.toString());

    assertEquals("[3,2],[5,12],[13,12],[12,13],[6,13],[10,13],[1,4]",
        board.getEnemies());

    assertEquals("[0,0],[1,0],[2,0],[3,0],[4,0],[5,0],[6,0],[7,0],[8,0]," +
        "[9,0],[10,0],[11,0],[12,0],[13,0],[14,0],[0,1],[6,1],[8,1]," +
        "[14,1],[0,2],[2,2],[6,2],[7,2],[8,2],[12,2],[14,2],[0,3],[2,3]," +
        "[12,3],[14,3],[0,4],[2,4],[4,4],[6,4],[8,4],[10,4],[12,4],[14,4]," +
        "[0,5],[2,5],[4,5],[6,5],[7,5],[8,5],[10,5],[12,5],[14,5],[0,6]," +
        "[6,6],[8,6],[14,6],[0,7],[4,7],[10,7],[14,7],[0,8],[6,8],[8,8]," +
        "[14,8],[0,9],[2,9],[4,9],[10,9],[12,9],[14,9],[0,10],[2,10],[4,10]," +
        "[6,10],[8,10],[10,10],[12,10],[14,10],[0,11],[2,11],[4,11],[6,11]," +
        "[7,11],[8,11],[10,11],[12,11],[14,11],[0,12],[2,12],[4,12],[6,12]," +
        "[8,12],[10,12],[12,12],[14,12],[0,13],[14,13],[0,14]," +
        "[1,14],[2,14],[3,14],[4,14],[5,14],[6,14],[7,14],[8,14]," +
        "[9,14],[10,14],[11,14],[12,14],[13,14],[14,14]",
        board.getBarriers());

    assertEquals("[1,8]",
        board.getBullets());

    assertEquals("☼,☼,☼,•, , ,╬,╬",
        board.getNear(1, 9));

    assertEquals("[6,13],[10,13]",
        board.findAll(Element.AI_TANK_RIGHT));

    assertEquals("[1,8]",
        board.findAll(Element.BULLET));

    assertEquals("[1,4]",
        board.findAll(Element.OTHER_TANK_DOWN));

    assertEquals("",
        board.findAll(Element.BANG));

    assertEquals("[0,0],[1,0],[2,0],[3,0],[4,0],[5,0],[6,0],[7,0],[8,0],[9,0]," +
        "[10,0],[11,0],[12,0],[13,0],[14,0],[0,1],[14,1],[0,2],[14,2],[0,3]," +
        "[14,3],[0,4],[14,4],[0,5],[14,5],[0,6],[14,6],[0,7],[14,7],[0,8]," +
        "[14,8],[0,9],[14,9],[0,10],[14,10],[0,11],[7,11],[14,11],[0,12]," +
        "[14,12],[0,13],[14,13],[0,14],[1,14],[2,14],[3,14],[4,14],[5,14]," +
        "[6,14],[7,14],[8,14],[9,14],[10,14],[11,14],[12,14],[13,14],[14,14]",
        board.findAll(Element.BATTLE_WALL));

    assertEquals("[6,1],[8,1],[2,2],[6,2],[7,2],[8,2],[12,2],[2,3],[12,3]," +
        "[2,4],[4,4],[6,4],[8,4],[10,4],[12,4],[2,5],[4,5],[6,5],[7,5],[8,5]," +
        "[10,5],[12,5],[6,6],[8,6],[4,7],[10,7],[6,8],[8,8],[2,9],[4,9],[10,9]," +
        "[12,9],[2,10],[4,10],[6,10],[8,10],[10,10],[12,10],[2,11],[4,11]," +
        "[6,11],[8,11],[10,11],[12,11],[2,12],[4,12],[6,12],[8,12],[10,12]," +
        "[12,12]",
        board.findAll(Element.WALL));

    assertEquals("",
        board.findAll(Element.TANK_DOWN));

    assertEquals(true,
        board.isAnyOfAt(1, 9,
            [Element.TANK_UP,
            Element.WALL,
            Element.OTHER_TANK_DOWN]));

    assertEquals(true,
        board.isAnyOfAt(1, 9,
            [Element.TANK_UP]));

    assertEquals(true,
        board.isAnyOfAt(1, 9,
            Element.TANK_UP));

    assertEquals(false,
        board.isAnyOfAt(1, 9,
            [Element.WALL,
            Element.OTHER_TANK_DOWN]));

    assertEquals(false,
        board.isAnyOfAt(1, 9,
            [Element.WALL]));

    assertEquals(false,
        board.isAnyOfAt(1, 9,
            Element.WALL));

    assertEquals(false,
        board.isAnyOfAt(3, -1,
            Element.TANK_UP));

    assertEquals(true,
        board.isNear(0, 8,
            Element.TANK_UP));

    assertEquals(true,
        board.isNear(0, 9,
            Element.TANK_UP));

    assertEquals(true,
        board.isNear(0, 10,
            Element.TANK_UP));

    assertEquals(true,
        board.isNear(1, 8,
            Element.TANK_UP));

    assertEquals(false,
        board.isNear(1, 9,   // сам танк
            Element.TANK_UP));

    assertEquals(true,
        board.isNear(1, 10,
            Element.TANK_UP));

    assertEquals(true,
        board.isNear(2, 8,
            Element.TANK_UP));

    assertEquals(true,
        board.isNear(2, 9,
            Element.TANK_UP));

    assertEquals(true,
        board.isNear(2, 10,
            Element.TANK_UP));

    assertEquals(false,
        board.isNear(1, -9,
            Element.TANK_UP));

    assertEquals(false,
        board.isNear(-1, 9,
            Element.TANK_UP));

    assertEquals(false,
        board.isBarrierAt(9, 13));

    assertEquals(true,
        board.isBarrierAt(0, 0));

    assertEquals(true,
        board.isBarrierAt(3, -1));

    assertEquals(false,
        board.isBarrierAt(3, 3));

    assertEquals(0, board.countNear(0, 0, Element.WALL));
    assertEquals(1, board.countNear(2, 1, Element.WALL));
    assertEquals(5, board.countNear(5, 5, Element.WALL));
    assertEquals(5, board.countNear(7, 6, Element.WALL));

    assertEquals("[5,4]",
        Direction.DOWN.change(new Point(5, 5)));

    assertEquals("[5,6]",
        Direction.UP.change(new Point(5, 5)));

    assertEquals("[4,5]",
        Direction.LEFT.change(new Point(5, 5)));

    assertEquals("[6,5]",
        Direction.RIGHT.change(new Point(5, 5)));

    assertEquals("[5,4]",
        new Point(5, 5).moveTo(Direction.DOWN));

    assertEquals("[5,6]",
        new Point(5, 5).moveTo(Direction.UP));

    assertEquals("[4,5]",
        new Point(5, 5).moveTo(Direction.LEFT));

    assertEquals("[6,5]",
        new Point(5, 5).moveTo(Direction.RIGHT));
}
