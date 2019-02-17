function runTest() {
    var board = new Board(
      /*14*/'☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼' +
      /*13*/'☼        ☻    ☼' +
      /*12*/'☼            ☺☼' +
      /*11*/'☼   ╔══►      ☼' +
      /*10*/'☼   ╚═╗       ☼' +
      /*9*/ '☼   ╔╗║       ☼' +
      /*8*/ '☼ ╔═╝╚╝       ☼' +
      /*7*/ '☼ ╚═╕         ☼' +
      /*6*/ '☼             ☼' +
      /*5*/ '☼             ☼' +
      /*4*/ '☼             ☼' +
      /*3*/ '☼             ☼' +
      /*2*/ '☼             ☼' +
      /*1*/ '☼             ☼' +
      /*0*/ '☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼');
           /*012345678901234*/

    assertEquals("15", board.size());

    assertEquals("[9,13]", board.getStone());

    assertEquals("[13,12]", board.getApple());

    assertEquals("[0,0],[1,0],[2,0],[3,0],[4,0],[5,0]," +
        "[6,0],[7,0],[8,0],[9,0],[10,0],[11,0],[12,0]," +
        "[13,0],[14,0],[0,1],[14,1],[0,2],[14,2],[0,3]," +
        "[14,3],[0,4],[14,4],[0,5],[14,5],[0,6],[14,6]," +
        "[0,7],[14,7],[0,8],[14,8],[0,9],[14,9],[0,10]," +
        "[14,10],[0,11],[14,11],[0,12],[14,12],[0,13]," +
        "[9,13]," + // stone
        "[14,13],[0,14]", board.getBarriers());

    assertEquals("[0,0],[1,0],[2,0],[3,0],[4,0],[5,0]," +
        "[6,0],[7,0],[8,0],[9,0],[10,0],[11,0],[12,0]," +
        "[13,0],[14,0],[0,1],[14,1],[0,2],[14,2],[0,3]," +
        "[14,3],[0,4],[14,4],[0,5],[14,5],[0,6],[14,6]," +
        "[0,7],[14,7],[0,8],[14,8],[0,9],[14,9],[0,10]," +
        "[14,10],[0,11],[14,11],[0,12],[14,12],[0,13]," +
        "[14,13],[0,14]", board.getWalls());

    assertEquals("[2,7],[3,7],[4,7],[2,8],[3,8],[4,8]," +
        "[5,8],[6,8],[4,9],[5,9],[6,9],[4,10],[5,10]," +
        "[6,10],[4,11],[5,11],[6,11],[7,11]", board.getMyBody());

    assertEquals("[7,11]", board.getMyHead());

    assertEquals(true, board.isAt(9, 13, Elements.BAD_APPLE));
    assertEquals(false, board.isAt(9, 13, Elements.GOOD_APPLE));
    assertEquals(true, board.isAt(6, 8, Elements.TAIL_LEFT_UP));
    assertEquals(false, board.isAt(3, -1, Elements.TAIL_LEFT_UP));

    assertEquals(Elements.BAD_APPLE, board.getAt(9, 13));
    assertEquals(Elements.TAIL_LEFT_UP, board.getAt(6, 8));
    assertEquals(Elements.BREAK, board.getAt(3, -1));

    assertEquals("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
        "☼        ☻    ☼\n" +
        "☼            ☺☼\n" +
        "☼   ╔══►      ☼\n" +
        "☼   ╚═╗       ☼\n" +
        "☼   ╔╗║       ☼\n" +
        "☼ ╔═╝╚╝       ☼\n" +
        "☼ ╚═╕         ☼\n" +
        "☼             ☼\n" +
        "☼             ☼\n" +
        "☼             ☼\n" +
        "☼             ☼\n" +
        "☼             ☼\n" +
        "☼             ☼\n" +
        "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n", board.boardAsString());

    assertEquals("Board:\n" +
        "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
        "☼        ☻    ☼\n" +
        "☼            ☺☼\n" +
        "☼   ╔══►      ☼\n" +
        "☼   ╚═╗       ☼\n" +
        "☼   ╔╗║       ☼\n" +
        "☼ ╔═╝╚╝       ☼\n" +
        "☼ ╚═╕         ☼\n" +
        "☼             ☼\n" +
        "☼             ☼\n" +
        "☼             ☼\n" +
        "☼             ☼\n" +
        "☼             ☼\n" +
        "☼             ☼\n" +
        "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
        "\n" +
        "My head at: [7,11]\n" +
        "My body at: [2,7],[3,7],[4,7],[2,8],[3,8],[4,8]," +
            "[5,8],[6,8],[4,9],[5,9],[6,9],[4,10],[5,10]," +
            "[6,10],[4,11],[5,11],[6,11],[7,11]\n" +
        "Apple at: [[13,12]]\n" +
        "Stone at: [[9,13]]\n", board.toString());

    assertEquals("[4,8],[6,8]",
        board.findAll(Elements.TAIL_LEFT_UP));

    assertEquals("[3,7],[3,8],[5,10],[5,11],[6,11]",
        board.findAll(Elements.TAIL_HORIZONTAL));

    assertEquals("[9,13]",
        board.findAll(Elements.BAD_APPLE));

    assertEquals("[13,12]",
        board.findAll(Elements.GOOD_APPLE));

    assertEquals("[7,11]",
        board.findAll(Elements.HEAD_RIGHT));

    assertEquals("",
        board.findAll(Elements.HEAD_DOWN));

    assertEquals(true,
        board.isAnyOfAt(9, 13,
            [Elements.HEAD_DOWN,
            Elements.HEAD_UP,
            Elements.HEAD_RIGHT,
            Elements.HEAD_LEFT,
            Elements.BAD_APPLE]));

    assertEquals(true,
        board.isAnyOfAt(9, 13,
            [Elements.BAD_APPLE]));

    assertEquals(true,
        board.isAnyOfAt(9, 13,
            Elements.BAD_APPLE));

    assertEquals(false,
        board.isAnyOfAt(9, 13,
            [Elements.HEAD_DOWN,
            Elements.HEAD_UP,
            Elements.HEAD_RIGHT,
            Elements.HEAD_LEFT]));

    assertEquals(false,
        board.isAnyOfAt(9, 13,
            [Elements.HEAD_DOWN]));

    assertEquals(false,
        board.isAnyOfAt(9, 13,
            Elements.HEAD_DOWN));

    assertEquals(false,
        board.isAnyOfAt(3, -1,
            Elements.HEAD_DOWN));

    assertEquals(true,
        board.isNear(9, 12,
            Elements.BAD_APPLE));

    assertEquals(true,
        board.isNear(9, 14,
            Elements.BAD_APPLE));

    assertEquals(true,
        board.isNear(8, 13,
            Elements.BAD_APPLE));

    assertEquals(true,
        board.isNear(10, 13,
            Elements.BAD_APPLE));

    assertEquals(false,
        board.isNear(8, 12,
            Elements.BAD_APPLE));

    assertEquals(false,
        board.isNear(3, -1,
            Elements.BAD_APPLE));

    assertEquals(true,
        board.isBarrierAt(9, 13));

    assertEquals(true,
        board.isBarrierAt(0, 0));

    assertEquals(true,
        board.isBarrierAt(3, -1));

    assertEquals(false,
        board.isBarrierAt(3, 3));

    assertEquals(1,
        board.countNear(9, 12, Elements.BAD_APPLE));

    assertEquals(2,
        board.countNear(0, 0, Elements.BREAK));

    assertEquals(0,
        board.countNear(3, -1, Elements.GOOD_APPLE));
}

assertEquals = function(expected, actual) {
    expected = String(expected)
    actual = String(actual)
    if (expected !== actual) {
        throw Error('Expected: "' + expected + '" but was: "' + actual + '"');
    }
}