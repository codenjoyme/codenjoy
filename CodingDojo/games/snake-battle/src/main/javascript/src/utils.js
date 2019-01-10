// Here is utils that might help for bot development
export function getBoardAsString(board) {
    const size = getBoardSize(board);

    var result = "";
    for (var i = 0; i < size; i++) {
        result += board.substring(i * size, (i + 1) * size);
        result += "\n";
    }
    return result;
}

export function getBoardSize(board) {
    return Math.sqrt(board.length);
}
