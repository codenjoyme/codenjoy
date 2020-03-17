
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

