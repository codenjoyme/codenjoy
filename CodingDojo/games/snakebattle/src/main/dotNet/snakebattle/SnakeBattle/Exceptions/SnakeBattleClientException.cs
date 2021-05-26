using System;
using System.Runtime.Serialization;

namespace SnakeBattle.Exceptions
{
    [Serializable]
    public class SnakeBattleClientException : Exception
    {
        public SnakeBattleClientException()
        {
        }

        public SnakeBattleClientException(string message) : base(message)
        {
        }

        public SnakeBattleClientException(string message, Exception inner) : base(message, inner)
        {
        }

        protected SnakeBattleClientException(
            SerializationInfo info,
            StreamingContext context) : base(info, context)
        {
        }
    }
}