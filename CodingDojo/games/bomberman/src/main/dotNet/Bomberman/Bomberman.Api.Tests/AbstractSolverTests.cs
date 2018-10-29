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
            var serverUrl = "http://127.0.0.1:8080/codenjoy-contest/board/player/player@mail.com?code=12345678901234567890";

            var result = AbstractSolver.GetWebSocketUrl(serverUrl);

            var expectedWebSocketUrl = "ws://127.0.0.1:8080/codenjoy-contest/ws?user=player%40mail.com&code=12345678901234567890";

            Assert.AreEqual(expectedWebSocketUrl, result);
        }
    }
}
