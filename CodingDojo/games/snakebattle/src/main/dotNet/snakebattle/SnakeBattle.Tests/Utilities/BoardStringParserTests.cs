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