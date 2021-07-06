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

using AutoFixture;
using FluentAssertions;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using SpaceRace.Api;
using SpaceRace.Api.Interfaces;

namespace SpaceRace.UnitTests
{
    [TestClass()]
    public class DirectionTests
    {
        private Fixture Autofixture { get; } = new Fixture();
        [TestMethod()]
        public void WithActTest()
        {
            Direction.ACT.WithAct().IsAct.Should().BeTrue();
            Direction.STOP.WithAct().IsAct.Should().BeTrue();
            Direction.SUICIDE.WithAct().IsAct.Should().BeTrue();
            Direction.DOWN.WithAct().IsAct.Should().BeTrue();
            Direction.LEFT.WithAct().IsAct.Should().BeTrue();
            Direction.RIGHT.WithAct().IsAct.Should().BeTrue();
            Direction.UP.WithAct().IsAct.Should().BeTrue();
        }

        [TestMethod()]
        public void ChangeTest()
        {
            void DirectionTest(IDirection direction, int dx, int dy)
            {
                var givenX = Autofixture.Create<int>();
                var givenY = Autofixture.Create<int>();
                var givenPoint = new Point(givenX, givenY);
                var expectedPoint = new Point(givenX + dx, givenY + dy);
                direction.Change(givenPoint).Should().BeEquivalentTo(expectedPoint);
            }
            DirectionTest(Direction.ACT, 0, 0);
            DirectionTest(Direction.SUICIDE, 0, 0);
            DirectionTest(Direction.STOP, 0, 0);
            DirectionTest(Direction.DOWN, 0, 1);
            DirectionTest(Direction.LEFT, -1, 0);
            DirectionTest(Direction.RIGHT, 1, 0);
            DirectionTest(Direction.UP, 0, -1);
            DirectionTest(Direction.ACT.WithAct(), 0, 0);
            DirectionTest(Direction.SUICIDE.WithAct(), 0, 0);
            DirectionTest(Direction.STOP.WithAct(), 0, 0);
            DirectionTest(Direction.DOWN.WithAct(), 0, 1);
            DirectionTest(Direction.LEFT.WithAct(), -1, 0);
            DirectionTest(Direction.RIGHT.WithAct(), 1, 0);
            DirectionTest(Direction.UP.WithAct(), 0, -1);
        }

        [TestMethod()]
        public void ToStringTest()
        {
            void DirectionTest(IDirection direction, string expectedString)
            {
                $"{direction}".Should().Be(expectedString);
            }
            DirectionTest(Direction.ACT, "Act");
            DirectionTest(Direction.SUICIDE, "Act(0)");
            DirectionTest(Direction.STOP, "Stop");
            DirectionTest(Direction.DOWN, "Down");
            DirectionTest(Direction.LEFT, "Left");
            DirectionTest(Direction.RIGHT, "Right");
            DirectionTest(Direction.UP, "Up");
            DirectionTest(Direction.ACT.WithAct(), "Act");
            DirectionTest(Direction.SUICIDE.WithAct(), "Act(0)");
            DirectionTest(Direction.STOP.WithAct(), "Stop,Act");
            DirectionTest(Direction.DOWN.WithAct(), "Down,Act");
            DirectionTest(Direction.LEFT.WithAct(), "Left,Act");
            DirectionTest(Direction.RIGHT.WithAct(), "Right,Act");
            DirectionTest(Direction.UP.WithAct(), "Up,Act");
        }
    }
}
