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
class Board {
    /**
     * @type {Cell[]}
     */
    cells;

    /**
     * @param {Cell[]} cells
     */
    constructor(cells) {
        this.cells = cells;
    }

    /**
     * @param {int} coordinateX
     * @param {int} coordinateY
     * @return {Cell}
     */
    GetCellByCoordinates(coordinateX, coordinateY) {
        return this.cells.find(cellToCheck =>
            cellToCheck.coordinateX === coordinateX
            && cellToCheck.coordinateY === coordinateY
        );
    }

    /**
     * @param {string} cellType
     * @return {Cell}
     */
    GetCellByCellType(cellType) {
        return this.cells.find(cellToCheck => cellToCheck.type === cellType);
    }

    /**
     * @param {string[]} cellTypes
     * @return {Cell}
     */
    GetCellByCellTypes(cellTypes) {
        return this.cells.find(cellToCheck => cellTypes.includes(cellToCheck.type));
    }

    /**
     * @callback cellPredicate
     * @param {Cell}
     * @return {boolean}
     */

    /**
     *
     * @param {cellPredicate} predicate
     * @return {Cell}
     */
    GetCellByPredicate(predicate) {
        return this.cells.find(predicate)
    }

    /**
     * @return {Cell[]}
     */
    GetCells() {
        return this.cells;
    }

    /**
     * @param {string} cellType
     * @return {Cell[]}
     */
    GetCellsByCellType(cellType) {
        return this.cells.filter(cellToCheck => cellToCheck.type === cellType);
    }

    /**
     * @param {string[]} cellTypes
     * @return {Cell[]}
     */
    GetCellsByCellTypes(cellTypes) {
        return this.cells.filter(cellToCheck => cellTypes.includes(cellToCheck.type));
    }

    /**
     * @param {cellPredicate} predicate
     * @return {Cell[]}
     */
    GetCellsByPredicate(predicate) {
        return this.cells.filter(predicate)
    }

    /**
     * @param {Cell} cell
     * @return {Cell}
     */
    GetCellToTheTopOf(cell) {
        return this.GetCellByCoordinates(cell.coordinateX, cell.coordinateY - 1);
    }

    /**
     * @param {Cell} cell
     * @return {Cell}
     */
    GetCellToTheRightOf(cell) {
        return this.GetCellByCoordinates(cell.coordinateX + 1, cell.coordinateY);
    }


    /**
     * @param {Cell} cell
     * @return {Cell}
     */
    GetCellToTheBottomOf(cell) {
        return this.GetCellByCoordinates(cell.coordinateX, cell.coordinateY + 1);
    }

    /**
     * @param {Cell} cell
     * @return {Cell}
     */
    GetCellToTheLeftOf(cell) {
        return this.GetCellByCoordinates(cell.coordinateX - 1, cell.coordinateY);
    }

    /**
     *
     * @param {Cell} cell
     * @param {string} cellType
     * @return {number}
     */
    CountNear(cell, cellType) {
        let matchCount = 0;

        if (this.GetCellToTheTopOf(cell).type === cellType) {
            matchCount++;
        }

        if (this.GetCellToTheRightOf(cell).type === cellType) {
            matchCount++;
        }

        if (this.GetCellToTheBottomOf(cell).type === cellType) {
            matchCount++;
        }

        if (this.GetCellToTheLeftOf(cell).type === cellType) {
            matchCount++;
        }

        return matchCount;
    }
}

module.exports = Board;