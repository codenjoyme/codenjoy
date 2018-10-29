using System;
using Bomberman.Api;
using Demo;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace DemoTests
{
    [TestClass]
    public class DashasSolverTests
    {
        /// <summary>
        /// this unit test is an example how to test your solver
        /// </summary>
        [TestMethod]
        public void ShouldDropBombAndGoUpUp() 
        {
            DashasSolver d = new DashasSolver("any server");
            Board b = new Board("any board");
            string firstStep = d.Get(b);

            Assert.AreEqual("Act,Up", firstStep);

            string secondStep = d.Get(b);

            Assert.AreEqual("Up", secondStep);

            string thirdStep = d.Get(b);

            Assert.AreEqual("Up", thirdStep);
        }
    }
}
