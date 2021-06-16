/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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

var Solver =function (Direction, Element) {

    return {

        /**
         * @return next hero action
         */
        get: function (board) {


            // TODO your code here
            
            var directions = [Direction.UP, Direction.LEFT, Direction.RIGHT, Direction.DOWN];
           
            return directions[randomInt(0, directions.length)];
        }
    };
};


function randomInt(min, max){
    return Math.round(Math.random() * (min+max) - min);
}


if (module) module.exports = Solver;
