using NUnit.Framework;
using SnakeBattle.Enums;
using SnakeBattle.Models;
using SnakeBattle.Utilities;

namespace SnakeBattle.Tests.Models
{
    [TestFixture]
    public class BoardTests
    {
        [OneTimeSetUp]
        public void WhenCreatingTheUserFromTheClient()
        {
            var boardStringParser = new BoardStringParser();

            _board = boardStringParser.Parse(BoardString);
        }

        private Board _board;

        private const string BoardString =
            "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼     ●   ○             ●   ☼☼#              ●            ☼☼☼  ○   ☼#         ○   ●     ☼☼☼                      ○    ☼☼# ○         ●               ☼☼☼                ~&    ●    ☼☼☼  ●   ☼☼☼        ☼  ☼      ☼☼#      ☼      ○   ☼  ☼      ☼☼☼      ☼○         ☼  ☼      ☼☼☼      ☼☼☼               ●  ☼☼#              ☼#           ☼☼☼○         ●               $☼☼☼    ●              ☼       ☼☼#             ○             ☼☼☼         ●             ●   ☼☼☼   ○             ☼#        ☼☼#       ☼☼ ☼                ☼☼☼ ●        ☼     ●     ○    ☼☼☼       ☼☼ ☼                ☼☼#          ☼                ☼☼☼   ●     ☼#    ●     ●     ☼☼☼           ○               ☼☼#                  ☼☼☼      ☼☼☼                           ☼☼☼  ●   ○        ☼☼☼#    ○   ☼*ø           ●               ☼☼☼               ○      ●    ☼☼☼      ●         ●          ☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼";

        [Test]
        public void GetCellByCoordinatesReturnsCorrectCell()
        {
            // Act
            var actualCell = _board.GetCell(7, 1);

            // Assert
            Assert.NotNull(actualCell);
            Assert.That(actualCell.Type, Is.EqualTo(CellType.Stone));
            Assert.That(actualCell.CoordinateX, Is.EqualTo(7));
            Assert.That(actualCell.CoordinateY, Is.EqualTo(1));
        }

        [Test]
        public void GetCellsByTypeReturnsCorrectCell()
        {
            // Act
            var actualCell = _board.GetCell(CellType.Apple);

            // Assert
            Assert.NotNull(actualCell);
            Assert.That(actualCell.Type, Is.EqualTo(CellType.Apple));
            Assert.That(actualCell.CoordinateX, Is.EqualTo(11));
            Assert.That(actualCell.CoordinateY, Is.EqualTo(1));
        }
    }
}