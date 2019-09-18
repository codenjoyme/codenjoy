namespace ICanCode.Api
{
    public class Command
    {
        private readonly string command;

        public Command(string command)
        {
            this.command = command;
        }

        /**
        * Says to Hero do nothing
        */
        public static Command DoNothing()
        {
            return new Command(string.Empty);
        }

        /**
         * Reset current level
         */
        public static Command Die()
        {
            return new Command("ACT(0)");
        }

        /**
         * Says to Hero jump to direction
         */
        public static Command Jump(Direction direction)
        {
            return new Command("ACT(1)" + "," + direction.ToString());
        }

        /**
         * Says to Hero pull box on this direction
         */
        public static Command Pull(Direction direction)
        {
            return new Command("ACT(2)" + "," + direction.ToString());
        }

        /**
         * Says to Hero fire on this direction
         */
        public static Command Fire(Direction direction)
        {
            return new Command("ACT(3)" + "," + direction.ToString());
        }

        /**
         * Says to Hero jump in place
         */
        public static Command Jump()
        {
            return new Command("ACT(1)");
        }

        /**
         * Says to Hero go to direction
         */
        public static Command Go(Direction direction)
        {
            return new Command(direction.ToString());
        }

        /**
         * Says to Hero goes to start point
         */
        public static Command Reset()
        {
            return new Command("ACT(0)");
        }

        public override string ToString()
        {
            return command.ToString();
        }
    }
}
