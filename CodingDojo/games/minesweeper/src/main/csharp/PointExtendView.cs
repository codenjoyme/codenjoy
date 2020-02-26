namespace MinesweeperClient
{
    public struct PointExtendView
    {
        public readonly int X;
        public readonly int Y;
        public readonly Element Element;

        public PointExtendView(int x, int y, Element element)
        {
            X = x;
            Y = y;
            Element = element;
        }

        /// <summary>
        /// Checks is current point on board or out of range.
        /// </summary>
        /// <param name="size">Board size to compare</param>
        public bool IsOutOf(int size)
        {
            return X >= size || Y >= size || X < 0 || Y < 0;
        }

        /// <summary>
        /// Returns new BoardPoint object shifted left to "delta" points
        /// </summary>
        public Point ShiftLeft(int delta = 1)
        {
            return new Point(X - delta, Y);
        }

        /// <summary>
        /// Returns new BoardPoint object shifted right to "delta" points
        /// </summary>
        public Point ShiftRight(int delta = 1)
        {
            return new Point(X + delta, Y);
        }

        /// <summary>
        /// Returns new BoardPoint object shifted top "delta" points
        /// </summary>
        public Point ShiftTop(int delta = 1)
        {
            return new Point(X, Y + delta);
        }

        /// <summary>
        /// Returns new BoardPoint object shifted bottom "delta" points
        /// </summary>
        public Point ShiftBottom(int delta = 1)
        {
            return new Point(X, Y - delta);

        }

        public Point Shift(Direction direction)
        {
            switch (direction)
            {
                case Direction.Left:
                    return ShiftLeft();
                case Direction.Right:
                    return ShiftRight();
                case Direction.Up:
                    return ShiftTop();
                case Direction.Down:
                    return ShiftBottom();
                case Direction.Act:
                    return new Point(X, Y);
                default:
                    return new Point(X, Y);
            }
        }

        public static bool operator ==(PointExtendView p1, PointExtendView p2)
        {
            if (ReferenceEquals(p1, p2))
                return true;

            if (ReferenceEquals(p1, null) || ReferenceEquals(p2, null))
                return false;

            return p1.X == p2.X && p1.Y == p2.Y;
        }

        public static bool operator !=(PointExtendView p1, PointExtendView p2)
        {
            return !(p1 == p2);
        }

        public override string ToString()
        {
            return string.Format("[{0},{1}]", X, Y);
        }

        public override bool Equals(object obj)
        {
            if (obj == null) return false;
            if (!(obj is Point)) return false;

            Point that = (Point)obj;

            return that.X == this.X && that.Y == this.Y;
        }

        public override int GetHashCode()
        {
            return (X.GetHashCode() ^ Y.GetHashCode());
        }
    }
}