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
var Point = function (x, y, element) {
    /**
     * type {Point}
     */
    return {
        equals: function (o) {
            return o.getX() == x && o.getY() == y;
        },

        toString: function () {
            return '[' + x + ',' + y + ']';
        },

        isOutOf: function (boardSize) {
            return x >= boardSize || y >= boardSize || x < 0 || y < 0;
        },

        getX: function () {
            return x;
        },

        getY: function () {
            return y;
        },

        getElement: function(){
            return element
        },

        shiftLeft: function (delta = 1) {
            return new Point(x - delta, y);
        },

        shiftRight: function (delta = 1) {
            return new Point(x + delta, y);
        },

        shiftTop: function (delta = 1) {
            return new Point(x, y + delta);
        },

        shiftBottom: function (delta = 1) {
            return new Point(x, y - delta);
        },

        change(direction) {
            if(direction.isAction) return new Point(x, y, element);
            switch (direction.getIndex()) {
                case 0:
                    return this.shiftLeft();
                case 1:
                    return this.shiftRight();
                case 2:
                    return this.shiftTop();
                case 3:
                    return this.shiftBottom();
                default:
                    throw new InvalidOperationException(`Can not change point, using "${direction}" direction`);
            }
        }
    }
};


if (module) module.exports = Point;
