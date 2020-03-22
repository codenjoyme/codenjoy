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
using System;
using Bomberman.Api;
using Demo;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace DemoTests
{
    [TestClass]
    public class YourSolverTests
    {
        /// <summary>
        /// this unit test is an example how to test your solver
        /// </summary>
        [TestMethod]
        public void ShouldSomething()
        {
            YourSolver d = new YourSolver("any server");
            Board b = new Board("any board");

            string firstStep = d.Get(b);

            Assert.AreEqual("Act", firstStep);

            string secondStep = d.Get(b);

            Assert.AreEqual("Act", secondStep);

            string thirdStep = d.Get(b);

            Assert.AreEqual("Act", thirdStep);
        }
    }
}
