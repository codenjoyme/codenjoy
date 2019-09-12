namespace BikeClient
{
    public class QDirection
    {
        public int dx;
        public int dy;
        public int value;
        private string directionString;

        QDirection(int value, int dx, int dy, string directionName) {
            this.value = value;
            this.dx = dx;
            this.dy = dy;
            this.directionString = directionName;
        }
        
        public static QDirection  Up()
        {
            return new QDirection(2,0,-1, "UP");
        }

        public static QDirection Down()
        {
            return new QDirection(3,0,1, "DOWN");
        }

        public int ChangeX(int x)
        {
            return dx + x;
        }
        public int ChangeY(int y)
        {
            return dy + y;
        }

        public Point Change(Point point)
        {
            return Point.pt(point.X,point.Y);
        }

        public override string ToString()
        {
            return this.directionString;
        }
    }
}