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
     * @constructor
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
     * @constructor
     */
    GetCellToTheTopOf(cell) {
        return this.GetCellByCoordinates(cell.coordinateX, cell.coordinateY - 1);
    }

    /**
     * @param {Cell} cell
     * @return {Cell}
     * @constructor
     */
    GetCellToTheRightOf(cell) {
        return this.GetCellByCoordinates(cell.coordinateX + 1, cell.coordinateY);
    }


    /**
     * @param {Cell} cell
     * @return {Cell}
     * @constructor
     */
    GetCellToTheBottomOf(cell) {
        return this.GetCellByCoordinates(cell.coordinateX, cell.coordinateY + 1);
    }

    /**
     * @param {Cell} cell
     * @return {Cell}
     * @constructor
     */
    GetCellToTheLeftOf(cell) {
        return this.GetCellByCoordinates(cell.coordinateX - 1, cell.coordinateY);
    }

    /**
     *
     * @param {Cell} cell
     * @param {string} cellType
     * @return {number}
     * @constructor
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