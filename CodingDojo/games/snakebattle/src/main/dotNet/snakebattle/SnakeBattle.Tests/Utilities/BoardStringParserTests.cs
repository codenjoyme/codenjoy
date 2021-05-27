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
                new()
                {
                    Type = CellType.Stone,
                    CoordinateX = 0,
                    CoordinateY = 0
                },
                new()
                {
                    Type = CellType.Apple,
                    CoordinateX = 1,
                    CoordinateY = 0
                },
                new()
                {
                    Type = CellType.StartFloor,
                    CoordinateX = 2,
                    CoordinateY = 0
                },
                new()
                {
                    Type = CellType.TailInactive,
                    CoordinateX = 0,
                    CoordinateY = 1
                },
                new()
                {
                    Type = CellType.HeadSleep,
                    CoordinateX = 1,
                    CoordinateY = 1
                },
                new()
                {
                    Type = CellType.EnemyTailInactive,
                    CoordinateX = 2,
                    CoordinateY = 1
                },
                new()
                {
                    Type = CellType.EnemyHeadSleep,
                    CoordinateX = 0,
                    CoordinateY = 2
                },
                new()
                {
                    Type = CellType.Stone,
                    CoordinateX = 1,
                    CoordinateY = 2
                },
                new()
                {
                    Type = CellType.Stone,
                    CoordinateX = 2,
                    CoordinateY = 2
                }
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