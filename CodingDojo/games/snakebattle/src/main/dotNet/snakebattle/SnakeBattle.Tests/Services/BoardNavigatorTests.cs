using FluentAssertions;
using NUnit.Framework;
using SnakeBattle.Enums;
using SnakeBattle.Models;
using SnakeBattle.Services;
using SnakeBattle.Utilities;

namespace SnakeBattle.Tests.Services
{
    [TestFixture]
    public class BoardNavigatorTests
    {
        [OneTimeSetUp]
        public void WhenCreatingTheUserFromTheClient()
        {
            var boardStringParser = new BoardStringParser();

            _boardNavigator = boardStringParser.Parse(BoardString);
        }

        private BoardNavigator _boardNavigator;

        private const string BoardString =
            "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼     ●   ○             ●   ☼☼#              ●            ☼☼☼  ○   ☼#         ○   ●     ☼☼☼                      ○    ☼☼# ○         ●               ☼☼☼                ~&    ●    ☼☼☼  ●   ☼☼☼        ☼  ☼      ☼☼#      ☼      ○   ☼  ☼      ☼☼☼      ☼○         ☼  ☼      ☼☼☼      ☼☼☼               ●  ☼☼#              ☼#           ☼☼☼○         ●               $☼☼☼    ●              ☼       ☼☼#             ○             ☼☼☼         ●             ●   ☼☼☼   ○             ☼#        ☼☼#       ☼☼ ☼                ☼☼☼ ●        ☼     ●     ○    ☼☼☼       ☼☼ ☼                ☼☼#          ☼                ☼☼☼   ●     ☼#    ●     ●     ☼☼☼           ○               ☼☼#                  ☼☼☼      ☼☼☼                           ☼☼☼  ●   ○        ☼☼☼#    ○   ☼*ø           ●               ☼☼☼               ○      ●    ☼☼☼      ●         ●          ☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼";

        [Test]
        public void GetCellByCoordinatesReturnsCorrectCell()
        {
            // Arrange
            var expectedCell = new Cell
            {
                Type = CellType.Stone,
                CoordinateX = 7,
                CoordinateY = 1
            };

            // Act
            var actualCell = _boardNavigator.GetCell(7, 1);

            // Assert
            actualCell.Should().BeEquivalentTo(expectedCell);
        }

        [Test]
        public void GetCellsByTypeReturnsCorrectCell()
        {
            // Arrange
            var expectedCell = new Cell
            {
                Type = CellType.Apple,
                CoordinateX = 11,
                CoordinateY = 1
            };

            // Act
            var actualCell = _boardNavigator.GetCell(CellType.Apple);

            // Assert
            actualCell.Should().BeEquivalentTo(expectedCell);
        }
    }
}