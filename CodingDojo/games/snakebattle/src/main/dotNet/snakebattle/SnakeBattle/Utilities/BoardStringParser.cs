using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Reflection;
using SnakeBattle.Enums;
using SnakeBattle.Interfaces.Utilities;
using SnakeBattle.Models;

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

        public Board Parse(string boardString)
        {
            var boardSize = (int) Math.Sqrt(boardString.Length);

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

            var board = new Board(cells);

            return board;
        }
    }
}