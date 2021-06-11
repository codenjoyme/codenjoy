const CellType = require("./Enums/CellType");
const Cell = require("./Models/cell");

class BoardStringParser {
    characterToCellType = [];

    constructor() {
        for (let cellTypeKey in CellType) {
            this.characterToCellType[CellType[cellTypeKey]] = cellTypeKey;
        }
    }

    /**
     * @param {string} boardString
     * @return {Cell[]}
     */
    parse(boardString) {
        const boardSize = Math.sqrt(boardString.length);

        if (boardSize % 1 !== 0) {
            throw "The map is not square";
        }

        const cells = [];
        for (let coordinateY = 0; coordinateY < boardSize; coordinateY++) {
            for (let coordinateX = 0; coordinateX < boardSize; coordinateX++) {
                const cellChar = boardString[coordinateY * boardSize + coordinateX];
                const cell = new Cell(
                    cellChar in this.characterToCellType
                        ? this.characterToCellType[cellChar]
                        : CellType.Unknown,
                    coordinateX,
                    coordinateY
                );

                cells.push(cell);
            }
        }

        return cells;
    }
}

module.exports = BoardStringParser;