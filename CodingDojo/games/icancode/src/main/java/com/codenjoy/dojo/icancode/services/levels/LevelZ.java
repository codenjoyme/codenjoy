package com.codenjoy.dojo.icancode.services.levels;

public class LevelZ implements Level {
    
    @Override
    public String help() {
        return "";
    }

    @Override
    public String defaultCode() {
        return new LevelB1().defaultCode();
    }

    @Override
    public String winCode() {
        // TODO закончить этот код
        return "function program(robot) {" +
                "    var scanner = robot.getScanner();" +
                "    " +
                "    var dest = scanner.getGold();" +
                "    dest = dest.concat(scanner.getExit());" +
                "    var minLength = 1000;" +
                "    var minIndex = -1;" +
                "    for (var index in dest) {" +
                "        var path = scanner.getShortestWay(dest[index]);" +
                "        if (path.length < minLength) {" +
                "            minIndex = index;" +
                "            minLength = path.length;" +
                "        }" +
                "    }" +
                "    " +
                "    if (minIndex == -1) {" +
                "        return;" +
                "    }" +
                "    var path = scanner.getShortestWay(dest[minIndex]);" +
                "    if (path.length === 1) {" +
                "        return;" +
                "    }" +
                "    var to = path[1];" +
                "    var from = scanner.getMe()" +
                "" +
                "    robot.goOverHole = function(direction) {" +
                "        if (scanner.at(direction) != \"HOLE\") {" +
                "            robot.go(direction);" +
                "        } else {" +
                "            if (direction == \"DOWN\") { // TODO crutch :)" +
                "                var afterHole = new Point(from.getX(), from.getY() + 2);" +
                "                if (scanner.at(afterHole) == \"WALL\") {" +
                "                    robot.go(\"RIGHT\");" +
                "                    return;" +
                "                }" +
                "            }" +
                "            robot.jump(direction);" +
                "        }" +
                "    };" +
                "    " +
                "    var dx = to.getX() - from.getX(); " +
                "    var dy = to.getY() - from.getY(); " +
                "    if (dx > 0) {" +
                "        robot.goOverHole(\"RIGHT\");" +
                "    } else if (dx < 0) {" +
                "        robot.goOverHole(\"LEFT\");" +
                "    } else if (dy > 0) {" +
                "        robot.goOverHole(\"DOWN\");" +
                "    } else if (dy < 0) {" +
                "        robot.goOverHole(\"UP\");" +
                "    }" +
                "}";
    }

    @Override
    public String map() {
        return "                                      " +
                "   ######      ###########            " +
                "   #$..˅#      #˃.....$..#            " +
                "   #BB.O#      #....Z....# ########   " +
                "   #B...#      #...B.BBBB# #˃.O..O#   " +
                "   #.SO.#  #####˃...$...O# #..$.BB#   " +
                "   #˃...####......O..S...# #O.S.O˂#   " +
                "   #..$......###....$..OO# #O....B#   " +
                "   #B...###### #.O.......# #B.#####   " +
                "   #B..O#      #.........###B.#       " +
                "   ##.### ######..BOO.........#       " +
                "    #.#   #..$..B.B....B.B..BB#       " +
                "    #.#   #$..###.B.#######B.B###     " +
                "    #.#   #...# #BB.#     #O..BB#     " +
                "    #.#   ##### #...#     #.$..˂#     " +
                "   ##.###       #.B.#  ####.Z...#     " +
                "   #..B.#  ######.BB#  #....BO.$#     " +
                "   #...$#  #B.......####.BB.B...#     " +
                "   #OZ..####O...O...$....######.#     " +
                "   #..O............O######    #.##### " +
                "   #˄...####.OB.....#       ###.B...# " +
                "   #BB..#  #BBB....˄#  ######˃....$.# " +
                "   ###.##  #˃..O..$O#  #˃......$.Z..# " +
                "     #.#   #####.####  ######˃......# " +
                " #####.###     #.#          #####..O# " +
                " #..O...˂#  ####.##########     #.O.# " +
                " #....O..#  #......$..B.BB#     #O..# " +
                " #$#######  #.#####.BB..BB#######.### " +
                " #.#        #.#   #....O....Z.....#   " +
                " #.# ########.##  ####....#####.B##   " +
                " #.# #.....˂...##### ###.##   #..#    " +
                " #.# #B.O....O..$..#   #.#    #B.#### " +
                " #.###..O.$E...###.#####.#### #.$...# " +
                " #.$....O.ZO.BB# #.BB˃...O..# #....˂# " +
                " #.#####BB.BBBB# #.....$E...# #.BE..# " +
                " #˄#   #.$....˂# ####.BO..OB# #˃....# " +
                " ###   #...$...#    ######### #...B.# " +
                "       #########              ####### ";
    }
    
}
