using System;
using SnakeBattle.Enums;

namespace SnakeBattle.Models
{
    public class Cell
    {
        public CellType Type { get; init; }
        public int CoordinateX { get; init; }
        public int CoordinateY { get; init; }

        private bool Equals(Cell other)
        {
            return Type == other.Type && CoordinateX == other.CoordinateX && CoordinateY == other.CoordinateY;
        }

        public override bool Equals(object obj)
        {
            if (ReferenceEquals(null, obj))
            {
                return false;
            }

            if (ReferenceEquals(this, obj))
            {
                return true;
            }

            if (obj.GetType() != GetType())
            {
                return false;
            }

            return Equals((Cell) obj);
        }

        public override int GetHashCode()
        {
            return HashCode.Combine((int) Type, CoordinateX, CoordinateY);
        }

        public static bool operator ==(Cell a, Cell b)
        {
            return
                a?.Type == b?.Type
                && a?.CoordinateX == b?.CoordinateX
                && a?.CoordinateY == b?.CoordinateY;
        }

        public static bool operator !=(Cell a, Cell b)
        {
            return !(a == b);
        }

        public override string ToString()
        {
            return $"x: {CoordinateX}, y: {CoordinateY}, {Type.ToString()}";
        }
    }
}