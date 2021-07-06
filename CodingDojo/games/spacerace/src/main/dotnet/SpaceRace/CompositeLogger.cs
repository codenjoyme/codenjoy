using System.Collections.Generic;
using System.Linq;
using SpaceRace.Api;
using SpaceRace.Api.Interfaces;

namespace SpaceRace
{
    public class CompositeLogger : IApiLogger
    {
        private readonly List<IApiLogger> _loggers;

        public CompositeLogger(params IApiLogger[] loggers)
        {
            _loggers = loggers.ToList();
        }

        public void Log(params object[] messages)
        {
            _loggers.ForEach(x => x.Log(messages));
        }

        public void LogBoard(Board board)
        {
            _loggers.ForEach(x => x.LogBoard(board));
        }

        public void LogResult(IDirection direction)
        {
            _loggers.ForEach(x => x.LogResult(direction));
        }
    }
}