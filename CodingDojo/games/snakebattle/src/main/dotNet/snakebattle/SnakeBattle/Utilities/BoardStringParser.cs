using System;
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