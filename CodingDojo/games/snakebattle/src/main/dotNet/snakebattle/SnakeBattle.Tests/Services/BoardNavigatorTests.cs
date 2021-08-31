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
﻿using FluentAssertions;
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
