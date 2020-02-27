namespace MinesweeperClient
{
    public struct Command
    {
        public readonly Direction Direction;
        public readonly bool IsAction;

        private Command(Direction direction, bool isAction)
        {
            Direction = direction;
            IsAction = isAction;
        }

        public override string ToString()
        {
            return IsAction && Direction != Direction.Act ? string.Format("{0}, {1}", Direction.Act.ToString(), Direction.ToString()) : Direction.ToString();
        }

        public static Command MoveTo(Direction direction)
        {
            return new Command(direction, false);
        }

        public static Command SetFlagTo(Direction direction)
        {
            return new Command(direction, true);
        }

        public static Command CreateCommand(Direction direction, bool isAction)
        {
            return new Command(direction, isAction);
        }
    }
}