using SnakeBattle.Enums;

namespace SnakeBattle.Models
{
    public class Cell
    {
        public CellType Type { get; set; }
        public int CoordinateX { get; set; }
        public int CoordinateY { get; set; }

        public override string ToString()
        {
            return $"x: {CoordinateX}, y: {CoordinateY}, {Type.ToString()}";
        }
    }
}