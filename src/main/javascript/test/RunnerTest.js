/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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

function runTest() {
    var board = new Board(
        /*9*/'          ' +
        /*8*/'          ' +
        /*7*/'          ' +
        /*6*/'■╔═╗■     ' +
        /*5*/' /═\\      ' +
        /*4*/'><▲▼|     ' +
        /*3*/'ḂĀĪŪĎḰĹṀŘŜ' +
        /*2*/'Ḟḃāīūďḟō  ' +
        /*1*/'B╚/╝      ' +
        /*0*/'■■■■■■■■■■');
            /*0123456789*/


    assertEquals("10", board.size());

    assertEquals("[0,1]", board.getMe());

    assertEquals("[0,3],[1,3],[2,3],[5,3],[4,3],[3,3],[6,3],[7,3],[8,3],[9,3],[1,2],[2,2],[6,2],[3,2],[5,2],[4,2],[7,2],[0,2]", board.getOtherHeroes());


    assertEquals("[0,0],[1,0],[2,0],[3,0],[4,0],[5,0],[6,0],[7,0],[8,0],[9,0],[1,2],[2,2],[6,2],[3,2],[5,2],[4,2],[7,2]," +
        "[0,2],[0,3],[1,3],[2,3],[5,3],[4,3],[3,3],[6,3],[7,3],[8,3],[9,3],[4,4],[0,6],[4,6]", board.getBarriers());

    assertEquals("[0,0],[1,0],[2,0],[3,0],[4,0],[5,0],[6,0],[7,0],[8,0],[9,0],[0,6],[4,6]", board.getFences());

    assertEquals("[0,4]", board.getAccelerators());

    assertEquals("[1,4]", board.getInhibitors());

    assertEquals("[2,4]", board.getLineUpChangers());

    assertEquals("[3,4]", board.getLineDownChangers());

    assertEquals("[4,4]", board.getObstacles());

    assertEquals("[2,1],[1,5]", board.getSpringboardDarkElements());

    assertEquals("[3,5]", board.getSpringboardLightElements());

    assertEquals("[1,1]", board.getSpringboardLeftDownElements());

    assertEquals("[3,1]", board.getSpringboardRightDownElements());

    assertEquals("[1,6]", board.getSpringboardLeftUpElements());

    assertEquals("[3,6]", board.getSpringboardRightUpElements());

    assertEquals("[2,5],[2,6]", board.getSpringboardTopElements());


    assertEquals(true, board.isAt(0, 4, Elements.ACCELERATOR));
    assertEquals(false, board.isAt(1, 4, Elements.ACCELERATOR));
    assertEquals(true, board.isAt(1, 4, Elements.INHIBITOR));
    assertEquals(false, board.isAt(2, 4, Elements.INHIBITOR));
    assertEquals(true, board.isAt(0, 0, Elements.FENCE));
    assertEquals(false, board.isAt(1, 4, Elements.FENCE));

    assertEquals(true, board.isAtMany(0, 0, [Elements.OBSTACLE, Elements.LINE_CHANGER_UP, Elements.FENCE, Elements.LINE_CHANGER_DOWN]));
    assertEquals(false, board.isAtMany(1, 4, [Elements.OBSTACLE, Elements.LINE_CHANGER_UP, Elements.FENCE, Elements.LINE_CHANGER_DOWN]));


    assertEquals(Elements.ACCELERATOR, board.getAt(0, 4));
    assertEquals(Elements.INHIBITOR, board.getAt(1, 4));
    assertEquals(Elements.FENCE, board.getAt(0, 0));


    assertEquals("          \n" +
        "          \n" +
        "          \n" +
        "■╔═╗■     \n" +
        " /═\\      \n" +
        "><▲▼|     \n" +
        "ḂĀĪŪĎḰĹṀŘŜ\n" +
        "Ḟḃāīūďḟō  \n" +
        "B╚/╝      \n" +
        "■■■■■■■■■■\n", board.boardAsString());

    assertEquals("Board:\n" +
        "          \n" +
        "          \n" +
        "          \n" +
        "■╔═╗■     \n" +
        " /═\\      \n" +
        "><▲▼|     \n" +
        "ḂĀĪŪĎḰĹṀŘŜ\n" +
        "Ḟḃāīūďḟō  \n" +
        "B╚/╝      \n" +
        "■■■■■■■■■■\n" +
        "\n" +
        "Me at: [0,1]\n" +
        "Enemy bikes at: [[0,3],[1,3],[2,3],[5,3],[4,3],[3,3],[6,3],[7,3],[8,3],[9,3],[1,2],[2,2],[6,2],[3,2],[5,2],[4,2],[7,2],[0,2]]\n" +
        "Accelerators at: [[0,4]]\n" +
        "Fences at: [[0,0],[1,0],[2,0],[3,0],[4,0],[5,0],[6,0],[7,0],[8,0],[9,0],[0,6],[4,6]]\n" +
        "Inhibitors at: [[1,4]]\n" +
        "Line Up Changers at: [[2,4]]\n" +
        "Line Down Changers at: [[3,4]]\n" +
        "Obstacles at: [[4,4]]\n" +
        "Springboard Dark Elements at: [[2,1],[1,5]]\n" +
        "Springboard Light Elements at: [[3,5]]\n" +
        "Springboard Left Down Elements at: [[1,1]]\n" +
        "Springboard Right Down Elements at: [[3,1]]\n" +
        "Springboard Left Up Elements at: [[1,6]]\n" +
        "Springboard Right Up Elements at: [[3,6]]\n" +
        "Springboard Top Elements at: [[2,5],[2,6]]\n", board.toString());


    assertEquals(true,
        board.isAnyOfAt(0, 4,
            [Elements.INHIBITOR,
            Elements.BIKE,
            Elements.OTHER_BIKE_FALLEN,
            Elements.ACCELERATOR]));

    assertEquals(true,
        board.isAnyOfAt(0, 0,
            [Elements.FENCE]));

    assertEquals(false,
        board.isAnyOfAt(0, 1,
            [Elements.FENCE]));

    assertEquals(true,
        board.isNear(1, 1,
            Elements.BIKE));

    assertEquals(false,
        board.isNear(2, 2,
            Elements.BIKE));


    assertEquals("[0,0],[1,0],[2,0],[3,0],[4,0],[5,0],[6,0],[7,0],[8,0],[9,0],[0,6],[4,6]",
        board.findAll(Elements.FENCE));

    assertEquals("",
        board.findAll(Elements.BIKE_FALLEN));


    assertEquals(true,
        board.isBarrierAt(0, 0));

    assertEquals(false,
        board.isBarrierAt(0, 4));

    assertEquals(false,
        board.isBarrierAt(4, 1));

    assertEquals(2,
        board.countNear(1, 0, Elements.FENCE));

    assertEquals(0,
        board.countNear(1, 0, Elements.BIKE));

    assertEquals(true, board.checkNearMe(Direction.DOWN, [Elements.ACCELERATOR, Elements.FENCE]));
    assertEquals(false, board.checkNearMe(Direction.DOWN, [Elements.ACCELERATOR, Elements.INHIBITOR, Elements.LINE_CHANGER_UP]));

    assertEquals(true, board.checkNearMeManyMoves([Direction.RIGHT, Direction.RIGHT, Direction.DOWN], [Elements.ACCELERATOR, Elements.FENCE]));
    assertEquals(false, board.checkNearMeManyMoves([Direction.RIGHT, Direction.RIGHT, Direction.DOWN], [Elements.ACCELERATOR, Elements.INHIBITOR, Elements.LINE_CHANGER_UP]));

    assertEquals(true, board.isOutOfFieldRelativeToMe(Direction.LEFT));
    assertEquals(false, board.isOutOfFieldRelativeToMe(Direction.RIGHT));

    assertEquals(true, board.hasOtherBikeAt(0,3));
    assertEquals(false, board.hasOtherBikeAt(0,1));
}

assertEquals = function (expected, actual) {
    expected = String(expected)
    actual = String(actual)
    if (expected !== actual) {
        throw Error('Expected: "' + expected + '" but was: "' + actual + '"');
    }
}
