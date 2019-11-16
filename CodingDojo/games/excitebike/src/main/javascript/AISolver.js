/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General var License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General var License for more details.
 * 
 * You should have received a copy of the GNU General var
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

//Game supports only two directions UP and DOWN or you can return nothing
var DirectionSolver = function (board) {
    return {
        /**
         * @return next hero action
         */
        get: function () {
            var me = board.getMe();
            //console.log(me.getX(), me.getY());

            // TODO your code here
            var dir = Direction.STOP;  // STUB get any random direction except Direction.STOP

            //return Direction.STOP;  // for nothing to do
            return dir;
        }
    };
};
