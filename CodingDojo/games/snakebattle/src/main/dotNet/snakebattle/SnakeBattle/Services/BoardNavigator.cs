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

#nullable enable
using System;
using System.Collections.Generic;
using System.Linq;
using SnakeBattle.Enums;
using SnakeBattle.Interfaces.Services;
using SnakeBattle.Models;

namespace SnakeBattle.Services
{
    public class BoardNavigator : IBoardNavigator
    {
        private readonly IEnumerable<Cell> _cells;

        public BoardNavigator(IEnumerable<Cell> cells)
        {
            _cells = cells;
        }

        public int CountNear(Cell cell, CellType cellType)
        {
            var matchCount = 0;

            if (GetCellToTheTopOf(cell)?.Type == cellType)
            {
                matchCount++;
            }

            if (GetCellToTheRightOf(cell)?.Type == cellType)
            {
                matchCount++;
            }

            if (GetCellToTheBottomOf(cell)?.Type == cellType)
            {
                matchCount++;
            }

            if (GetCellToTheLeftOf(cell)?.Type == cellType)
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

        public IEnumerable<Cell> GetCells()
        {
            return _cells;
        }

        public Cell? GetCellToTheLeftOf(Cell baseCell)
        {
            var cell = GetCell(baseCell.CoordinateX - 1, baseCell.CoordinateY);

            return cell;
        }

        public Cell? GetCellToTheRightOf(Cell baseCell)
        {
            var cell = GetCell(baseCell.CoordinateX + 1, baseCell.CoordinateY);

            return cell;
        }

        public Cell? GetCellToTheTopOf(Cell baseCell)
        {
            var cell = GetCell(baseCell.CoordinateX, baseCell.CoordinateY - 1);

            return cell;
        }

        public Cell? GetCellToTheBottomOf(Cell baseCell)
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

        public IEnumerable<Cell> GetApples()
        {
            var cells = GetCells(CellType.Apple);

            return cells;
        }

        public IEnumerable<Cell> GetGold()
        {
            var cells = GetCells(CellType.Gold);

            return cells;
        }

        public IEnumerable<Cell> GetFlyingPills()
        {
            var cells = GetCells(CellType.FlyingPill);

            return cells;
        }

        public IEnumerable<Cell> GetFuryPills()
        {
            var cells = GetCells(CellType.FuryPill);

            return cells;
        }

        public bool IsPlayerAlive()
        {
            var cell = GetCell(new[]
            {
                CellType.HeadDead,
                CellType.HeadSleep
            });

            return cell != null;
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

        public bool IsCellTypeNear(Cell cell, CellType cellType)
        {
            var count = CountNear(cell, cellType);

            return count > 0;
        }
    }
}
