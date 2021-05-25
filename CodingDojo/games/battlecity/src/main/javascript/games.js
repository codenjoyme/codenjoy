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
var Stuff = require('./stuff.js');

var Games = module.exports = {

    gameName : '',

    init : function(name) {
        this.gameName = name;
    },

    'require': function(name) {
        name = Stuff.clean(name);

        if (name == 'elements') {
            // case node
            var module = require('./games/' + this.gameName + '/elements.js');
            if (typeof module != 'undefined') {
                return Element = module;
            }

            // case browser stub
            if (this.gameName == 'bomberman') {
                return Element = BombermanElement;
            } else if (this.gameName == 'tetris') {
                return Element = TetrisElement;
            } else if (this.gameName == 'a2048') {
                return Element = A2048Element;
            } else if (this.gameName == 'battlecity') {
                return Element = BattlecityElement;
            } else if (this.gameName == 'excitebike') {
                return Element = ExcitebikeElement;
            } else if (this.gameName == 'icancode') {
                return Element = ICanCodeElement;
            } else if (this.gameName == 'minesweeper') {
                return Element = MinesweeperElement;
            } else if (this.gameName == 'snake') {
                return Element = SnakeElement;
            } else if (this.gameName == 'snakebattle') {
                return Element = SnakeBattleElement;
            }
        } else if (name == 'board') {
            // case node
            var module = require('./games/' + this.gameName + '/board.js');
            if (typeof module != 'undefined') {
                return Board = module;
            }

            // case browser stub
            if (this.gameName == 'bomberman') {
                return Board = BombermanBoard;
            } else if (this.gameName == 'tetris') {
                return Board = TetrisBoard;
            } else if (this.gameName == 'a2048') {
                return Board = A2048Board;
            } else if (this.gameName == 'battlecity') {
                return Board = BattlecityBoard;
            } else if (this.gameName == 'excitebike') {
                return Board = ExcitebikeBoard;
            } else if (this.gameName == 'icancode') {
                return Board = ICanCodeBoard;
            } else if (this.gameName == 'minesweeper') {
                return Board = MinesweeperBoard;
            } else if (this.gameName == 'snake') {
                return Board = SnakeBoard;
            } else if (this.gameName == 'snakebattle') {
                return Board = SnakeBattleBoard;
            }
        } else if (name == 'direction') {
            // case node
            var module = require('./games/' + this.gameName + '/direction.js');
            if (typeof module != 'undefined') {
                return module();
            }

            // case browser stub
            if (this.gameName == 'bomberman') {
                return Direction = BombermanDirection();
            } else if (this.gameName == 'tetris') {
                return Direction = TetrisDirection();
            } else if (this.gameName == 'a2048') {
                return Direction = A2048Direction();
            } else if (this.gameName == 'battlecity') {
                return Direction = BattlecityDirection();
            } else if (this.gameName == 'excitebike') {
                return Direction = ExcitebikeDirection();
            } else if (this.gameName == 'icancode') {
                return Direction = ICanCodeDirection();
            } else if (this.gameName == 'minesweeper') {
                return Direction = MinesweeperDirection();
            } else if (this.gameName == 'snake') {
                return Direction = SnakeDirection();
            } else if (this.gameName == 'snakebattle') {
                return Direction = SnakeBattleDirection();
            }
        } else if (name == 'test') {
            // case node
            var module = require('./games/' + this.gameName + '/test.js');
            if (typeof module != 'undefined') {
                return module;
            }

            // case browser stub
            if (this.gameName == 'bomberman') {
                return Test = BombermanTest;
            } else if (this.gameName == 'tetris') {
                return Test = TetrisTest;
            } else if (this.gameName == 'a2048') {
                return Test = A2048Test;
            } else if (this.gameName == 'battlecity') {
                return Test = BattlecityTest;
            } else if (this.gameName == 'excitebike') {
                return Test = ExcitebikeTest;
            } else if (this.gameName == 'icancode') {
                return Test = ICanCodeTest;
            } else if (this.gameName == 'minesweeper') {
                return Test = MinesweeperTest;
            } else if (this.gameName == 'snake') {
                return Test = SnakeTest;
            } else if (this.gameName == 'snakebattle') {
                return Test = SnakeBattleTest;
            }
        }
    }
};
