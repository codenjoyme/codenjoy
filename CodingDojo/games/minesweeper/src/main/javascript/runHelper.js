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

function createTable(tableData) {
    var table = decorateTable(document.createElement('table'));
    for (var x = 0; x < tableData.length; x++) {
        var row = document.createElement('tr');
        for (var y = 0; y < tableData[x].length; y++) {
            var cell = document.createElement('td');
            styleCellWithPropperClass(cell, tableData[x][y])
            row.appendChild(cell);
        }
        table.appendChild(row);
    }
    var tableDiv = document.getElementById('board-table');
    var tableItself = document.getElementById('table-game');

    tableItself ?
        tableDiv.replaceChild(table, tableItself) :
        tableDiv.appendChild(table);
    return table;
}

function drawBoard(board) {
    var boardArr = [];
    for (var x = 0; x < board.size(); x++) {
        boardArr[x] = [];
        for (var y = 0; y < board.size(); y++) {
            boardArr[x].push(board.getAt(y, x));
        }
    }
    return boardArr;
}

function decorateTable(table) {
    table.cellSpacing = 0;
    table.border = 0;
    table.cellPadding = 0;
    table.id = "table-game";
    return table;
}

function styleCellWithPropperClass(targetCell, input) {
    switch (input) {
        case Element.DETECTOR:
            targetCell.className = 'DETECTOR';
            break;
        case Element.BANG:
            targetCell.className = 'BANG';
            break;
        case Element.HERE_IS_BOMB:
            targetCell.className = 'HERE_IS_BOMB';
            break;
        case Element.FLAG:
            targetCell.className = 'FLAG';
            break;
        case Element.HIDDEN:
            targetCell.className = 'HIDDEN';
            break;
        case Element.ONE_MINE:
            targetCell.className = 'ONE_MINE';
            break;
        case Element.TWO_MINES:
            targetCell.className = 'TWO_MINES';
            break;
        case Element.THREE_MINES:
            targetCell.className = 'THREE_MINES';
            break;
        case Element.FOUR_MINES:
            targetCell.className = 'FOUR_MINES';
            break;
        case Element.FIVE_MINES:
            targetCell.className = 'FIVE_MINES';
            break;
        case Element.SIX_MINES:
            targetCell.className = 'SIX_MINES';
            break;
        case Element.SEVEN_MINES:
            targetCell.className = 'SEVEN_MINES';
            break;
        case Element.EIGHT_MINES:
            targetCell.className = 'EIGHT_MINES';
            break;
        case Element.BORDER:
            targetCell.className = 'BORDER';
            break;
        case Element.DESTROYED_BOMB:
            targetCell.className = 'DESTROYED_BOMB';
            break;
        default:
            targetCell.className = 'NONE';
            break;
    }
}

