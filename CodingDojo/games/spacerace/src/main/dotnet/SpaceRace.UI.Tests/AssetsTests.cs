using System.IO;
using FluentAssertions;
using NUnit.Framework;
using SpaceRace.Api;

namespace SpaceRace.UI.Tests
{
    public class AssetsTests
    {
        [Theory]
        public void AllElementsShouldToHaveTheResource(Element element)
        {
            File.Exists($".\\assets\\{element}.png").Should().BeTrue();
        }

        [TestCase(Actions.Up)]
        [TestCase(Actions.Down)]
        [TestCase(Actions.Left)]
        [TestCase(Actions.Right)]
        [TestCase(Actions.Act)]
        public void GenericDirectionsShouldToHatheTheResources(Actions action)
        {
            File.Exists($".\\assets\\{action}_active.png").Should().BeTrue();
            File.Exists($".\\assets\\{action}_inactive.png").Should().BeTrue();
        }
    }
}