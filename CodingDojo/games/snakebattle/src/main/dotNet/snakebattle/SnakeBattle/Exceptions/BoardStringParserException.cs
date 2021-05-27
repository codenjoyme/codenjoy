using System;
using System.Runtime.Serialization;

namespace SnakeBattle.Exceptions
{
    [Serializable]
    public class BoardStringParserException : Exception
    {
        public BoardStringParserException()
        {
        }

        public BoardStringParserException(string message) : base(message)
        {
        }

        public BoardStringParserException(string message, Exception inner) : base(message, inner)
        {
        }

        protected BoardStringParserException(
            SerializationInfo info,
            StreamingContext context) : base(info, context)
        {
        }
    }
}