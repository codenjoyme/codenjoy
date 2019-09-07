using ICanCode.Api;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ICanCode.Client
{
    /// <summary>
    /// This is ICanCode client demo.
    /// </summary>
    internal class YourSolver : AbstractSolver
    {
        public YourSolver(string server)
            : base(server)
        {
        }

        /// <summary>
        /// Calls each move to make decision what to do (next move)
        /// </summary>
        public override Command WhatToDo(Board board)
        {
            var action = Command.Go(Direction.Up);
            return action;
        }
    }
}
