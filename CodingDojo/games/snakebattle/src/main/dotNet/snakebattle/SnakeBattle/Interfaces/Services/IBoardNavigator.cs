#nullable enable
using System;
using System.Collections.Generic;
using SnakeBattle.Enums;
using SnakeBattle.Models;

namespace SnakeBattle.Interfaces.Services
{
    public interface IBoardNavigator
    {
        int CountNear(Cell cell, CellType cellType);
        IEnumerable<Cell> GetApples();

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
        IEnumerable<Cell> GetCells();
        IEnumerable<Cell> GetCells(CellType cellType);
        IEnumerable<Cell> GetCells(IEnumerable<CellType> cellTypes);
        IEnumerable<Cell> GetCells(Func<Cell, bool> predicate);
        Cell? GetCellToTheBottomOf(Cell baseCell);
        Cell? GetCellToTheLeftOf(Cell baseCell);
        Cell? GetCellToTheRightOf(Cell baseCell);
        Cell? GetCellToTheTopOf(Cell baseCell);
        IEnumerable<Cell> GetEnemiesHeads();
        IEnumerable<Cell> GetFlyingPills();
        IEnumerable<Cell> GetFuryPills();
        IEnumerable<Cell> GetGold();
        Cell GetPlayerHead();
        Cell GetPlayerTail();
        bool IsCellTypeAtPoint(CellType cellTypeToCheck, int coordinateX, int coordinateY);
        bool IsCellTypeNear(Cell cell, CellType cellType);
        bool IsPlayerAlive();
    }
}