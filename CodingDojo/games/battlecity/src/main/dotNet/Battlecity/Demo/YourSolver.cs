using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using Battlecity.API;

namespace Demo
{
    internal class YourSolver : AbstractSolver
    {
        public YourSolver(string server) : base(server) { }

        /// <summary>
        /// Is called on each move to make decision what to do (next move)
        /// </summary>
        protected override string Get(Board board)
        {
            // Sample code

            string action = Direction.Up.ToString();
            return action;
        }
    }
}
