#nullable enable
using System;
using System.Collections.Generic;
using System.Linq;
using SnakeBattle.Enums;
using SnakeBattle.Interfaces.Models;

namespace SnakeBattle.Models
{
    public class Board : IBoard
    {
        private readonly IEnumerable<Cell> _cells;

        public Board(IEnumerable<Cell> cells)
        {
            _cells = cells;
        }

        public int CountNear(Cell cell, CellType cellType)
        {
            var matchCount = 0;

            if (GetCellToTheTop(cell)?.Type == cellType)
            {
                matchCount++;
            }

            if (GetCellToTheRight(cell)?.Type == cellType)
            {
                matchCount++;
            }

            if (GetCellToTheBottom(cell)?.Type == cellType)
            {
                matchCount++;
            }

            if (GetCellToTheLeft(cell)?.Type == cellType)
            {
                matchCount++;
            }

            return matchCount;
        }

        /// <inheritdoc />
        public Cell? GetCell(int coordinateX, int coordinateY)
        {
            var foundCell = _cells
                .FirstOrDefault(cell => cell.CoordinateX == coordinateX && cell.CoordinateY == coordinateY);

            return foundCell;
        }

        public Cell? GetCell(CellType cellType)
        {
            var foundCell = _cells
                .FirstOrDefault(cell => cell.Type == cellType);

            return foundCell;
        }

        public Cell? GetCell(IEnumerable<CellType> cellTypes)
        {
            var foundCell = _cells
                .FirstOrDefault(cellToCheck =>
                    cellTypes.Any(cellTypeToCompareWith => cellToCheck.Type == cellTypeToCompareWith)
                );

            return foundCell;
        }

        public Cell? GetCell(Func<Cell, bool> predicate)
        {
            var foundCell = _cells
                .FirstOrDefault(predicate);

            return foundCell;
        }

        public Cell? GetCellToTheLeft(Cell baseCell)
        {
            var cell = GetCell(baseCell.CoordinateX - 1, baseCell.CoordinateY);

            return cell;
        }

        public Cell? GetCellToTheRight(Cell baseCell)
        {
            var cell = GetCell(baseCell.CoordinateX + 1, baseCell.CoordinateY);

            return cell;
        }

        public Cell? GetCellToTheTop(Cell baseCell)
        {
            var cell = GetCell(baseCell.CoordinateX, baseCell.CoordinateY - 1);

            return cell;
        }

        public Cell? GetCellToTheBottom(Cell baseCell)
        {
            var cell = GetCell(baseCell.CoordinateX, baseCell.CoordinateY + 1);

            return cell;
        }

        public IEnumerable<Cell> GetCells(CellType cellType)
        {
            var cells = _cells
                .Where(cell => cell.Type == cellType)
                .ToArray();

            return cells;
        }

        public IEnumerable<Cell> GetCells(IEnumerable<CellType> cellTypes)
        {
            var cells = _cells
                .Where(cellToCheck =>
                    cellTypes.Any(cellTypeToCompareWith => cellToCheck.Type == cellTypeToCompareWith))
                .ToArray();

            return cells;
        }

        public IEnumerable<Cell> GetCells(Func<Cell, bool> predicate)
        {
            var foundCell = _cells.Where(predicate);

            return foundCell;
        }

        public IEnumerable<Cell> GetEnemiesHeads()
        {
            var foundCell = GetCells(new[]
            {
                CellType.EnemyHeadDown,
                CellType.EnemyHeadLeft,
                CellType.EnemyHeadRight,
                CellType.EnemyHeadUp,
                CellType.EnemyHeadDead,
                CellType.EnemyHeadEvil,
                CellType.EnemyHeadFly,
                CellType.EnemyHeadSleep
            });

            return foundCell;
        }

        public Cell GetPlayerHead()
        {
            var foundCell = GetCell(new[]
            {
                CellType.HeadDown,
                CellType.HeadLeft,
                CellType.HeadRight,
                CellType.HeadUp,
                CellType.HeadEvil,
                CellType.HeadFly,
                CellType.HeadSleep,
                CellType.HeadDead
            });

            return foundCell!;
        }

        public Cell GetPlayerTail()
        {
            var foundCell = GetCell(new[]
            {
                CellType.TailEndDown,
                CellType.TailEndLeft,
                CellType.TailEndUp,
                CellType.TailEndRight,
                CellType.TailInactive
            });

            return foundCell!;
        }

        public bool IsCellTypeAtPoint(CellType cellTypeToCheck, int coordinateX, int coordinateY)
        {
            var cell = GetCell(coordinateX, coordinateY);

            if (cell == null)
            {
                return false;
            }

            return cell.Type == cellTypeToCheck;
        }

        public bool IsNear(Cell cell, CellType cellType)
        {
            var count = CountNear(cell, cellType);

            return count > 0;
        }
    }
}