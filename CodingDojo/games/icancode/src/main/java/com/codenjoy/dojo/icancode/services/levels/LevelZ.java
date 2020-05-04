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
        return "function program(robot) {\n" +
                "    var scanner = robot.getScanner();\n" +
                "    \n" +
                "    var dest = scanner.getGold();\n" +
                "    dest = dest.concat(scanner.getExit());\n" +
                "    var minLength = 1000;\n" +
                "    var minIndex = -1;\n" +
                "    for (var index in dest) {\n" +
                "        var path = scanner.getShortestWay(dest[index]);\n" +
                "        if (path.length < minLength) {\n" +
                "            minIndex = index;\n" +
                "            minLength = path.length;\n" +
                "        }\n" +
                "    }\n" +
                "    \n" +
                "    if (minIndex == -1) {\n" +
                "        return;\n" +
                "    }\n" +
                "    var path = scanner.getShortestWay(dest[minIndex]);\n" +
                "    if (path.length === 1) {\n" +
                "        return;\n" +
                "    }\n" +
                "    var to = path[1];\n" +
                "    var from = scanner.getMe()\n" +
                "\n" +
                "    robot.goOverHole = function(direction) {\n" +
                "        if (scanner.at(direction) != \"HOLE\") {\n" +
                "            robot.go(direction);\n" +
                "        } else {\n" +
                "            if (direction == \"DOWN\") { // TODO crutch :)\n" +
                "                var afterHole = new Point(from.getX(), from.getY() + 2);\n" +
                "                if (scanner.at(afterHole) == \"WALL\") {\n" +
                "                    robot.go(\"RIGHT\");\n" +
                "                    return;\n" +
                "                }\n" +
                "            }\n" +
                "            robot.jump(direction);\n" +
                "        }\n" +
                "    };\n" +
                "    \n" +
                "    var dx = to.getX() - from.getX(); \n" +
                "    var dy = to.getY() - from.getY(); \n" +
                "    if (dx > 0) {\n" +
                "        robot.goOverHole(\"RIGHT\");\n" +
                "    } else if (dx < 0) {\n" +
                "        robot.goOverHole(\"LEFT\");\n" +
                "    } else if (dy > 0) {\n" +
                "        robot.goOverHole(\"UP\");\n" +
                "    } else if (dy < 0) {\n" +
                "        robot.goOverHole(\"DOWN\");\n" +
                "    }\n" +
                "}\n";
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
