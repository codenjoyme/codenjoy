package com.codenjoy.dojo.icancode.services.levels;

import java.util.List;

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
        return  "                                      \n" +
                "   ######      ###########            \n" +
                "   #$..˅#      #˃.....$..#            \n" +
                "   #BB.O#      #....Z....# ########   \n" +
                "   #B...#      #...B.BBBB# #˃.O..O#   \n" +
                "   #.SO.#  #####˃...$...O# #..$.BB#   \n" +
                "   #˃...####......O..S...# #O.S.O˂#   \n" +
                "   #..$......###....$..OO# #O....B#   \n" +
                "   #B...###### #.O.......# #B.#####   \n" +
                "   #B..O#      #.........###B.#       \n" +
                "   ##.### ######..BOO.........#       \n" +
                "    #.#   #..$..B.B....B.B..BB#       \n" +
                "    #.#   #$..###.B.#######B.B###     \n" +
                "    #.#   #...# #BB.#     #O..BB#     \n" +
                "    #.#   ##### #...#     #.$..˂#     \n" +
                "   ##.###       #.B.#  ####.Z...#     \n" +
                "   #..B.#  ######.BB#  #....BO.$#     \n" +
                "   #...$#  #B.......####.BB.B...#     \n" +
                "   #OZ..####O...O...$....######.#     \n" +
                "   #..O............O######    #.##### \n" +
                "   #˄...####.OB.....#       ###.B...# \n" +
                "   #BB..#  #BBB....˄#  ######˃....$.# \n" +
                "   ###.##  #˃..O..$O#  #˃......$.Z..# \n" +
                "     #.#   #####.####  ######˃......# \n" +
                " #####.###     #.#          #####..O# \n" +
                " #..O...˂#  ####.##########     #.O.# \n" +
                " #....O..#  #......$..B.BB#     #O..# \n" +
                " #$#######  #.#####.BB..BB#######.### \n" +
                " #.#        #.#   #....O....Z.....#   \n" +
                " #.# ########.##  ####....#####.B##   \n" +
                " #.# #.....˂...##### ###.##   #..#    \n" +
                " #.# #B.O....O..$..#   #.#    #B.#### \n" +
                " #.###..O.$E...###.#####.#### #.$...# \n" +
                " #.$....O.ZO.BB# #.BB˃...O..# #....˂# \n" +
                " #.#####BB.BBBB# #.....$E...# #.BE..# \n" +
                " #˄#   #.$....˂# ####.BO..OB# #˃....# \n" +
                " ###   #...$...#    ######### #...B.# \n" +
                "       #########              ####### \n";
    }

    @Override
    public List<String> befungeCommands() {
        return Level.extendBefunge(new LevelG1());
    }
    
}
