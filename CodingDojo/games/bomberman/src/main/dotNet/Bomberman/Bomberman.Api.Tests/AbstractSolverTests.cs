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
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace Bomberman.Api.Tests
{
    [TestClass]
    public class AbstractSolverTests
    {
        [TestMethod]
        public void ShouldProvideWebSocketUrlFromServerAddress()
        {
            var serverUrl = "http://codenjoy.com:80/codenjoy-contest/board/player/3edq63tw0bq4w4iem7nb?code=1234567890123456789";

            var result = AbstractSolver.GetWebSocketUrl(serverUrl);

            var expectedWebSocketUrl = "ws://codenjoy.com:80/codenjoy-contest/ws?user=3edq63tw0bq4w4iem7nb&code=1234567890123456789";

            Assert.AreEqual(expectedWebSocketUrl, result);
        }
    }
}
