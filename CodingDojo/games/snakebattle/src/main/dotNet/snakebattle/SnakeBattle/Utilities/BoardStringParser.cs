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
                var descriptionAttribute = fieldInfo!.GetCustomAttribute<DescriptionAttribute>();
                _fromStringToType[descriptionAttribute!.Description[0]] = cellType;
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
            for (var coordinateY = 0; coordinateY < boardSize; coordinateY++)
            {
                for (var coordinateX = 0; coordinateX < boardSize; coordinateX++)
                {
                    var cellChar = boardString[coordinateY * boardSize + coordinateX];
                    var cell = new Cell(
                        _fromStringToType.ContainsKey(cellChar)
                            ? _fromStringToType[cellChar]
                            : CellType.Unknown,
                        coordinateX,
                        coordinateY
                    );

                    cells.Add(cell);
                }
            }

            var board = new BoardNavigator(cells);

            return board;
        }
    }
}