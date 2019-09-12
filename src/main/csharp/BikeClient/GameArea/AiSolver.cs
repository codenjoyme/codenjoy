namespace BikeClient.GameArea
{
    public class AiSolver : Board
    {


        public string Play()
        {
            GetMe();

            return QDirection.Down().ToString();
        }
    }
}