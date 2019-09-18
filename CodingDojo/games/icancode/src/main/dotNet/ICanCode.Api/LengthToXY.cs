namespace ICanCode.Api
{
    public class LengthToXY
    {
        public int Size;

        public LengthToXY(int size)
        {
            Size = size;
        }

        private int InversionY(int y)
        {
            return Size - 1 - y;
        }

        private int InversionX(int x)
        {
            return x;
        }

        public int GetLength(int x, int y)
        {
            int xx = InversionX(x);
            int yy = InversionY(y);
            return yy * Size + xx;
        }

        public Point GetXY(int length)
        {
            int x = InversionX(length % Size);
            int y = InversionY(length / Size);
            return new Point(x, y);
        }
    }
}
