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
class Cell {

    type;
    coordinateX;
    coordinateY;

    /**
     * 
     * @param {string} type
     * @param {int} coordinateX
     * @param {int} coordinateY
     */
    constructor(type, coordinateX, coordinateY) {
        this.type = type;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
    }

    /**
     * @param {Cell} otherCell
     * @return {boolean}
     */
    IsEqualToCell(otherCell){
        if(otherCell.type !== this.type){
            return false
        }
        if(otherCell.coordinateX !== this.coordinateX){
            return false
        }
        if(otherCell.coordinateY !== this.coordinateY){
            return false
        }
        
        return true;
    }
}

module.exports = Cell;