using System.Collections.Generic;
using FluentAssertions;
using NUnit.Framework;
using SnakeBattle.Enums;
using SnakeBattle.Exceptions;
using SnakeBattle.Models;
using SnakeBattle.Utilities;

namespace SnakeBattle.Tests.Utilities
{
    [TestFixture]
    public class BoardStringParserTests
    {
        [OneTimeSetUp]
        public void WhenCreatingTheUserFromTheClient()
        {
            _boardStringParser = new BoardStringParser();
        }

        private BoardStringParser _boardStringParser;

        private const string BoardString =
            "●○#~&*ø●●";

        [Test]
        public void ParseReturnsBoardNavigatorWithCorrectSetOfCells()
        {
            // Arrange 

            var expectedCells = new List<Cell>
            {
                new Cell(CellType.Stone, 0, 0),
                new Cell(CellType.Apple, 1, 0),
                new Cell(CellType.StartFloor, 2, 0),

                new Cell(CellType.TailInactive, 0, 1),
                new Cell(CellType.HeadSleep, 1, 1),
                new Cell(CellType.EnemyTailInactive, 2, 1),

                new Cell(CellType.EnemyHeadSleep, 0, 2),
                new Cell(CellType.Stone, 1, 2),
                new Cell(CellType.Stone, 2, 2)
            };

            // Act
            var boardNavigator = _boardStringParser.Parse(BoardString);
            var actualCells = boardNavigator.GetCells();

            // Assert
            actualCells.Should().Equal(expectedCells);
        }

        [Test]
        public void ParseThrowsExceptionWhenBoardStringIsWrongLength()
        {
            // Arrange
            var badBoardString = BoardString + " ";

            // Act & Assert
            _boardStringParser
                .Invoking(parser => parser.Parse(badBoardString))
                .Should().Throw<BoardStringParserException>();
        }
    }
}