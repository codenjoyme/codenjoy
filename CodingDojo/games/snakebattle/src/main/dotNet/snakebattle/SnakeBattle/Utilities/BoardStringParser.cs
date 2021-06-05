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
﻿using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Reflection;
using SnakeBattle.Enums;
using SnakeBattle.Exceptions;
using SnakeBattle.Interfaces.Utilities;
using SnakeBattle.Models;
using SnakeBattle.Services;

namespace SnakeBattle.Utilities
{
    public class BoardStringParser : IBoardStringParser
    {
        private readonly IDictionary<char, CellType> _fromStringToType;

        public BoardStringParser()
        {
            _fromStringToType = new Dictionary<char, CellType>();

            var values = (CellType[]) Enum.GetValues(typeof(CellType));

            foreach (var cellType in values)
            {
                var fieldInfo = cellType.GetType().GetField(cellType.ToString());
                var descriptionAttribute = fieldInfo.GetCustomAttribute<DescriptionAttribute>();
                _fromStringToType[descriptionAttribute.Description[0]] = cellType;
            }
        }

        public BoardNavigator Parse(string boardString)
        {
            var boardSizeRaw = Math.Sqrt(boardString.Length);
            var boardSize = (int) boardSizeRaw;
            if (boardSizeRaw % 1 != 0)
            {
                throw new BoardStringParserException("Board size is not square");
            }

            var cells = new List<Cell>();
            for (var y = 0; y < boardSize; y++)
            {
                for (var x = 0; x < boardSize; x++)
                {
                    var cellChar = boardString[y * boardSize + x];
                    var cell = new Cell
                    {
                        CoordinateX = x,
                        CoordinateY = y,
                        Type = _fromStringToType.ContainsKey(cellChar)
                            ? _fromStringToType[cellChar]
                            : CellType.Unknown
                    };

                    cells.Add(cell);
                }
            }

            var board = new BoardNavigator(cells);

            return board;
        }
    }
}
