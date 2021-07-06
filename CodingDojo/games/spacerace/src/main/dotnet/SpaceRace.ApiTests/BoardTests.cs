/*-
* #%L
* Codenjoy - it's a dojo-like platform from developers to developers.
* %%
* Copyright (C) 2018 Codenjoy
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
using System.Linq;
using AutoFixture;
using FluentAssertions;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using SpaceRace.Api;

namespace SpaceRace.UnitTests
{
    [TestClass()]
    public class BoardTests
    {
        private Fixture AutoFixture { get; } = new Fixture();

        [TestMethod()]
        public void GetAllExtendTest()
        {
            var expectedResult = new Dictionary<Point, Element>
            {
                {new Point(0, 0), Element.Wall},
                {new Point(1, 0), Element.None},
                {new Point(2, 0), AutoFixture.Create<Element>()},
                {new Point(0, 1), Element.Wall},
                {new Point(1, 1), Element.BulletPack},
                {new Point(2, 1), AutoFixture.Create<Element>()},
                {new Point(0, 2), Element.Wall},
                {new Point(1, 2), Element.OtherHero},
                {new Point(2, 2), AutoFixture.Create<Element>()}
            };
            var boardString = string.Concat(expectedResult.Values.Select(x => (char)x));

            var board = new Board(boardString);

            board.GetAllExtend().Should().BeEquivalentTo(expectedResult);
        }
        [TestMethod()]
        public void FindAllAllTest()
        {

            var boardRepresentation = new Dictionary<Point, Element>
            {
                {new Point(0, 0), Element.Wall},
                {new Point(1, 0), Element.None},
                {new Point(2, 0), Element.OtherHero},
                {new Point(0, 1), Element.Wall},
                {new Point(1, 1), AutoFixture.Create<Element>()},
                {new Point(2, 1), Element.Bomb},
                {new Point(0, 2), Element.Wall},
                {new Point(1, 2), Element.OtherHero},
                {new Point(2, 2), Element.Stone}
            };

            var expectedWalls = boardRepresentation
                .Where(x => x.Value == Element.Wall)
                .Select(x => x.Key).ToList();

            var expectedBombsAndStones = boardRepresentation
                .Where(x => x.Value == Element.Bomb || x.Value == Element.Stone)
                .Select(x => x.Key).ToList();
            
            var boardString = string.Concat(boardRepresentation.Values.Select(x => (char)x));

            var board = new Board(boardString);
            
            board.FindAll(Element.Wall)
                .Should().BeEquivalentTo(expectedWalls);
            board.FindAll(Element.Stone, Element.Bomb)
                .Should().BeEquivalentTo(expectedBombsAndStones);
        }

        [TestMethod()]
        public void BoardTest()
        {
            var board = new Board("1234");
            board.Size.Should().Be(2);
            board = new Board("123456789");
            board.Size.Should().Be(3);
            board = new Board("1234123412341234");
            board.Size.Should().Be(4);
        }
    }
}