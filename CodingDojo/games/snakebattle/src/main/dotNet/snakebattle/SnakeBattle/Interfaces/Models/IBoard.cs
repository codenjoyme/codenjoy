#nullable enable
using System;
using System.Collections.Generic;
using SnakeBattle.Enums;
using SnakeBattle.Models;

namespace SnakeBattle.Interfaces.Models
{
    public interface IBoard
    {
        int CountNear(Cell cell, CellType cellType);

        /// <summary>
        ///     Gets cell by provided coordinates
        /// </summary>
        /// <param name="coordinateX">Horizontal index of the cell from left side of the board </param>
        /// <param name="coordinateY">Vertical index of the cell from top side of the board</param>
        /// <returns></returns>
        Cell? GetCell(int coordinateX, int coordinateY);

        Cell? GetCell(CellType cellType);
        Cell? GetCell(IEnumerable<CellType> cellTypes);
        Cell? GetCell(Func<Cell, bool> predicate);
        IEnumerable<Cell> GetCells(CellType cellType);
        IEnumerable<Cell> GetCells(IEnumerable<CellType> cellTypes);
        IEnumerable<Cell> GetCells(Func<Cell, bool> predicate);
        Cell? GetCellToTheBottom(Cell baseCell);
        Cell? GetCellToTheLeft(Cell baseCell);
        Cell? GetCellToTheRight(Cell baseCell);
        Cell? GetCellToTheTop(Cell baseCell);
        IEnumerable<Cell> GetEnemiesHeads();
        Cell GetPlayerHead();
        Cell GetPlayerTail();
        bool IsCellTypeAtPoint(CellType cellTypeToCheck, int coordinateX, int coordinateY);
        bool IsNear(Cell cell, CellType cellType);
    }
}