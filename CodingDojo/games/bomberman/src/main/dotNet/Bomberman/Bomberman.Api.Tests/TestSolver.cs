using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Bomberman.Api.Tests
{
    public class TestSolver : AbstractSolver
    {
        public TestSolver(string server)
           : base(server)
        {
        }

        protected internal override string Get(Board gameBoard)
        {
            return Direction.Act.ToString();
        }
    }
}
